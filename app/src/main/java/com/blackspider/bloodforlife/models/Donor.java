package com.blackspider.bloodforlife.models;

import java.io.Serializable;

/**
 * Created by Arhan Ashik on 2/17/2018.
 */

public class Donor implements Serializable{
    private int id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String birthDate;
    private int weight;
    private String bloodGroup;
    private String image;
    private String city;
    private String lat;
    private String lng;
    private String occupation;
    private String institute;
    private boolean isDonor;
    private String availableHour;
    private String lastDonationDate;
    private int totalDonation;
    private boolean isDrugAddicted;
    private String diseases;

    public Donor() {
    }

    public Donor(String name, String email, String password, String bloodGroup, String city, String lat, String lng, String lastDonationDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.lat = lat;
        this.lng = lng;
        this.lastDonationDate = lastDonationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public boolean isDonor() {
        return isDonor;
    }

    public void setDonor(boolean donor) {
        isDonor = donor;
    }

    public String getAvailableHour() {
        return availableHour;
    }

    public void setAvailableHour(String availableHour) {
        this.availableHour = availableHour;
    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }

    public int getTotalDonation() {
        return totalDonation;
    }

    public void setTotalDonation(int totalDonation) {
        this.totalDonation = totalDonation;
    }

    public boolean isDrugAddicted() {
        return isDrugAddicted;
    }

    public void setDrugAddicted(boolean drugAddicted) {
        isDrugAddicted = drugAddicted;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }
}
