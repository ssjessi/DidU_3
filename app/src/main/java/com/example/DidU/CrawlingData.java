package com.example.DidU;

public class CrawlingData {
    String id;
    String password;
    String lectureNumber;
    String classNumber;

    public CrawlingData() {
    }

    public CrawlingData(String id, String password, String lectureNumber, String classNumber) {
        this.id = id;
        this.password = password;
        this.lectureNumber = lectureNumber;
        this.classNumber = classNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLectureNumber() {
        return lectureNumber;
    }

    public void setLectureNumber(String lectureNumber) {
        this.lectureNumber = lectureNumber;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }
}
