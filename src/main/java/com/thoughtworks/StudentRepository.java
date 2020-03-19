package com.thoughtworks;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    public static Optional<Connection> conn;

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    private static Connection getConnection(){
        return conn;
    }

    public void save(Student student) {
        // 获取Conn:
        Connection connection = conn.orElse(getConnection());
        // 定义Sql

        // TODO:
    }

    public List<Student> query() {
        // TODO:
        return new ArrayList<>();
    }

    public List<Student> queryByClassId(String classId) {
        // TODO:
        return new ArrayList<>();
    }

    public void update(String id, Student student) {
        // TODO:
    }

    public void delete(String id) {
        // TODO:
    }
}
