package com.thoughtworks;

import com.thoughtworks.utils.JDBCUtils;
import com.thoughtworks.utils.SqlUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public void save(List<Person> people) {
        people.forEach(this::save);
    }

    public void save(Person person) {
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "INSERT INTO person(id, alias, sex)" +
                " VALUES (?, ?, ?)";
            SqlUtils.executeUpdate(conn, sql, person.getId(), person.getName(), person.getGender());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Person> query() {
        List<Person> people = new ArrayList<>();
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "select id, alias, sex from person";
            people = SqlUtils.executeQuery(conn, sql, Person.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public List<Person> queryByClassId(String classId) {
        List<Person> people = new ArrayList<>();
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "select id, name, gender, admin_year, birthday, class_id from person where class_id = ?";
            people = SqlUtils.executeQuery(conn, sql, Person.class, classId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return people;
    }

    public int update(String id, Person person) {
        try {
            Connection conn = JDBCUtils.getConnection();
            String sql = "UPDATE person SET id=?, name=?, gender=? where id=?";
            return SqlUtils.executeUpdate(conn, sql,
                person.getId(), person.getName(), person.getGender(), id);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int delete(String id) {
        try {
            String sql = "DELETE FROM person where id = ?";
            Connection conn = JDBCUtils.getConnection();
            return SqlUtils.executeUpdate(conn, sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
