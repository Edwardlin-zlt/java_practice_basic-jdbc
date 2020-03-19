package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository {
    public static Connection conn;

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    private static Connection getConnection() throws SQLException {
        conn = JDBCUtils.getConnection();
        return conn;
    }

    public void save(Student student) {
        PreparedStatement preparedStatement = null; // 可不可以把这两句放到static去，每次close之后变回null
        Connection connection = null;
        try {
            connection = Optional.ofNullable(conn).orElse(getConnection()); // 可不可以不关闭该类的conn让每个方法调用的时候都用该类的同一个conn,这样就不用每次都去get一个Conn了
            String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?);"; // sql 的：写不写
            preparedStatement = setStudentInfoIntoSQL(student, connection, sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        } finally {
//            JDBCUtils.close(preparedStatement, connection);
//        }
    }

    public List<Student> query() {
        PreparedStatement statement = null;
        Connection connection = null;
        List<Student> students = new ArrayList<>();
        try {
            connection = JDBCUtils.getConnection();
            String sql = "SELECT * FROM students;";
            statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            students = generateStudentsFromQueryResult(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
//        } finally {
//            JDBCUtils.close(statement, connection);
//        }
        return students;
    }

    private List<Student> generateStudentsFromQueryResult(ResultSet resultSet) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        while (resultSet.next()) {
            Student stu = new Student();
            stu.setId(new DecimalFormat("000").format(resultSet.getInt("id")));
            stu.setName(resultSet.getString("name"));
            stu.setGender(resultSet.getString("gender"));
            stu.setAdmissionYear(Integer.parseInt(resultSet.getString("admin_year").split("-")[0]));
            stu.setBirthday(resultSet.getDate("birthday"));
            stu.setClassId(resultSet.getString("class_id"));
            students.add(stu);
        }
        return students;
    }

    public List<Student> queryByClassId(String classId) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "SELECT * from students where class_id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, classId);
            ResultSet resultSet = statement.executeQuery();
            return generateStudentsFromQueryResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        } finally {
//            JDBCUtils.close(statement, connection);
//        }
        return new ArrayList<>();
    }

    public void update(String id, Student student) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "UPDATE students SET id=?, name=?, gender=?, admin_year=?, birthday=?, class_id=? where id=?";
            statement = setStudentInfoIntoSQL(student, connection, sql);
            statement.setInt(7, Integer.parseInt(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        } finally {
//            JDBCUtils.close(statement, connection);
//        }
    }

    private PreparedStatement setStudentInfoIntoSQL(Student student, Connection connection, String sql) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, Integer.parseInt(student.getId()));
        statement.setString(2, student.getName());
        statement.setString(3, student.getGender());
        statement.setString(4, String.valueOf(student.getAdmissionYear()));
        statement.setDate(5,  new java.sql.Date(student.getBirthday().getTime()));
        statement.setString(6, student.getClassId());
        return statement;
    }

    public void delete(String id) {
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "DELETE FROM students where id = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        } finally {
//            JDBCUtils.close(statement, connection);
//        }
    }
}
