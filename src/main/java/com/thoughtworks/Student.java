package com.thoughtworks;

public class Student {

    private String id;
    private String name;
    private String gender;
    private int admissionYear;
    private String birthday;
    private String classId;

    public Student() {
    }

    public Student(String id, String name, String gender, int admissionYear, String birthday,
                   String classId) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.admissionYear = admissionYear;
        this.birthday = birthday;
        this.classId = classId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public void setAdmissionYear(int admissionYear) {
        this.admissionYear = admissionYear;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return "Student{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", gender='" + gender + '\'' +
            ", admissionYear=" + admissionYear +
            ", birthday=" + birthday +
            ", classId='" + classId + '\'' +
            '}';
    }
}
