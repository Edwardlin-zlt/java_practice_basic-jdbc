package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    public void save(Student student) {
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO student(id, name, gender, admin_year, birthday, class_id)" +
                " VALUES (?, ?, ?, ?, ?, ?)";
            SqlUtils.executeUpdate(conn, sql,
                student.getId(), student.getName(), student.getGender(), student.getAdmissionYear(),
                student.getBirthday(), student.getClassId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> query() {
        List<Student> students = new ArrayList<>();
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "select id, name, gender, admin_year, birthday, class_id from student";
            students = SqlUtils.executeQuery(conn, sql, Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public List<Student> queryByClassId(String classId) {
        List<Student> students = new ArrayList<>();
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "select id, name, gender, admin_year, birthday, class_id from student where id = ?";
            students = SqlUtils.executeQuery(conn, sql, Student.class, classId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public int update(String id, Student student) {
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "UPDATE student SET id=?, name=?, gender=?, admin_year=?, birthday=?, class_id=? where id=?";
            return SqlUtils.executeUpdate(conn, sql,
                student.getId(), student.getName(), student.getGender(),
                student.getAdmissionYear(), student.getBirthday(), student.getClassId(),
                student.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(String id) {
        try {
            String sql = "DELETE FROM student where id = ?";
            Connection conn = JDBCUtils.getConnection();
            return SqlUtils.executeUpdate(conn, sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
