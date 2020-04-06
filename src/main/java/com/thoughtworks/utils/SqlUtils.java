package com.thoughtworks;

import com.thoughtworks.utils.ColumnName;
import com.thoughtworks.utils.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            Field[] fields = clazz.getDeclaredFields();
            while (rs.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    Object columnValue = rs.getObject(columnLabel);
                    Field field = fields[0];
                    try {
                        field = clazz.getDeclaredField(columnLabel);
                    } catch (NoSuchFieldException e) {
                        for (Field f : fields) {
                            String fieldDbAlias = Optional.ofNullable(f.getAnnotation(ColumnName.class))
                                .map(ColumnName::value)
                                .orElse(null);
                            if (fieldDbAlias != null && fieldDbAlias.equals(columnLabel)) {
                                field = f;
                                break;
                            }
                        }
                    }
                    field.setAccessible(true);
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
