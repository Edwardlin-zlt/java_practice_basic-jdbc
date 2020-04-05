package com.thoughtworks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class AppTest {
    StudentRepository repository = new StudentRepository();

    @BeforeAll
    void setUp() {
        Student student1 = new Student("001", "张三", "男", 2020, "1994-01-12", "1-13");
        Student student2 = new Student("002", "李四", "男", 2020, "1994-05-25", "1-1");
        Student student3 = new Student("003", "王五", "男", 2019, "1995-04-02", "2-10");
        Student student4 = new Student("004", "周梅", "女", 2020, "1993-06-16", "1-1");
        Student student5 = new Student("005", "钱风", "男", 2020, "1993-01-10", "1-1");
        Student student6 = new Student("006", "吴兰", "女", 2019, "1995-06-09", "2-1");
        Student student7 = new Student("007", "李云", "男", 2019, "1993-08-11", "1-1");
        repository.save(Arrays.asList(student1, student2, student3, student4, student5, student6, student7));
    }

    @Test
    @DisplayName("查找所有学生并打印")
    void queryAll(){
        repository.query().forEach(System.out::println);
    }

}

