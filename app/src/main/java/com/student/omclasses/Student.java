package com.student.omclasses;

public class Student {

    public Student() {
    }

    public Student(String name, String gender, String standard, String number, String number1, String occupation, String address, String email, String password) {
        this.name = name;
        this.gender = gender;
        this.standard = standard;
        this.number = number;
        this.number1 = number1;
        this.occupation = occupation;
        this.address = address;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getStandard() {
        return standard;
    }

    public String getNumber() {
        return number;
    }

    public String getNumber1() {
        return number1;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String name, gender, standard, number, number1, occupation, address, email, password;
}
