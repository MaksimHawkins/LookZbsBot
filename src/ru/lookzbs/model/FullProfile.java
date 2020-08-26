package ru.lookzbs.model;

public class FullProfile {

    private boolean gender;
    private int age;
    private String city;
    private String photo;
    private int rating;
    private int rates_count;
    private boolean shown;
    private boolean mycity;
    private int gender_filter;
    private int age_from;
    private int age_to;
    private int status;

    public FullProfile(boolean gender, int age, String city, String photo, int rating, int rates_count, boolean shown, boolean mycity, int gender_filter, int age_from, int age_to, int status) {
        this.gender = gender;
        this.age = age;
        this.city = city;
        this.photo = photo;
        this.rating = rating;
        this.rates_count = rates_count;
        this.shown = shown;
        this.mycity = mycity;
        this.gender_filter = gender_filter;
        this.age_from = age_from;
        this.age_to = age_to;
        this.status = status;
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

    public int getRatesCount() {
        return rates_count;
    }

    public boolean isShown() {
        return shown;
    }

    public boolean isMycity() {
        return mycity;
    }

    public int getGenderFilter() {
        return gender_filter;
    }

    public int getAgeFrom() {
        return age_from;
    }

    public int getAgeTo() {
        return age_to;
    }

    public int getStatus() {
        return status;
    }
}
