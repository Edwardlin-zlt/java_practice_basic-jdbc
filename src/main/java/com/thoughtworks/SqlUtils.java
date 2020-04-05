package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlUtils {
    public static int executeUpdate(Connection conn, String sql, Object... args){
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
              ps.setObject(i+1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            JDBCUtils.close(null, ps, conn);
        }
    }

    public static <T> List<T> executeQuery(Connection conn, String sql, Class<T> clazz, Object... args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> objects = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                T t= clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(columnLabel);
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.set(t, columnValue);
                }
                objects.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, ps, conn);
        }
        return objects;
    }

}
