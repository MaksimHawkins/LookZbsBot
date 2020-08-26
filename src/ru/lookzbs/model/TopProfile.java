package ru.lookzbs.model;

public class TopProfile {

    private boolean gender;
    private int age;
    private String city;
    private String photo;
    private int rating;

    public TopProfile(boolean gender, int age, String city, String photo, int rating) {
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.photo = photo;
        this.rating = rating;
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

    public int getRating() {
        return rating;
    }
}
