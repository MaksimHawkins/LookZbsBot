package ru.lookzbs.model;

public class Profile {

    private boolean gender;
    private int age;
    private String city;
    private String photo;

    public Profile(boolean gender, int age, String city, String photo) {
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.photo = photo;
    }

    public boolean getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getPhoto() {
        return photo;
    }
}
