package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SqlUtils {
    public static int executeUpdate(Connection conn, String sql, Object... args) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        } finally {
            JDBCUtils.close(null, ps, conn);
        }
    }

    public static <T> List<T> executeQuery(Connection conn, String sql, Class<T> clazz, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<T> objects = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            rs = ps.executeQuery();

//            ResultSetMetaData metaData = rs.getMetaData();
            Field[] fields = clazz.getDeclaredFields();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String columnLabel = Optional.ofNullable(field.getAnnotation(ColumnName.class))
                        .map(ColumnName::value)
                        .orElse(field.getName());
                    Object columnValue = rs.getObject(columnLabel);
//                    if (columnValue instanceof java.sql.Date) {
//                        columnValue =
//                    }
                    field.set(t, columnValue);
                    // field 对应的　set 方法
                }
                objects.add(t);
            }

//            int columnCount = metaData.getColumnCount();
//            while (rs.next()) {
//                T t= clazz.newInstance();
//                for (int i = 0; i < columnCount; i++) {
//                    String columnLabel = metaData.getColumnLabel(i + 1);
//                    Object columnValue = rs.getObject(columnLabel);
//
//                    fieldName =
//                    Field field = clazz.getDeclaredField(columnLabel);
//                    field.setAccessible(true);
//                    field.set(t, columnValue);
//                }
//                objects.add(t);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(rs, ps, conn);
        }
        return objects;
    }

}
