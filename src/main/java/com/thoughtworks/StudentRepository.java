package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    public static Connection conn;

    static {
        try {
            conn = JDBCUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(List<Student> students) {
        students.forEach(this::save);
    }

    public void save(Student student) {
        try {
            String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = setStudentInfoIntoSQL(student, conn, sql);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Student> query() {
        List<Student> students = new ArrayList<>();
        try {
            String sql = "SELECT * FROM students;";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            students = generateStudentsFromQueryResult(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            String sql = "SELECT * from students where class_id=?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, classId);
            ResultSet resultSet = statement.executeQuery();
            return generateStudentsFromQueryResult(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void update(String id, Student student) {
        try {
            String sql = "UPDATE students SET id=?, name=?, gender=?, admin_year=?, birthday=?, class_id=? where id=?";
            PreparedStatement statement = setStudentInfoIntoSQL(student, conn, sql);
            statement.setInt(7, Integer.parseInt(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        try {
            String sql = "DELETE FROM students where id = ?";
            PreparedStatement statement = conn.prepareStatement(sql); // TODO　如何关闭资源
            statement.setInt(1, Integer.parseInt(id));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
