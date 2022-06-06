package com.android.onlineshoppingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class UserInformation implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String Phone;
    private String sex;
    private Date dateOfBirth;
    private String accountType;

    public UserInformation(String firstName, String lastName, String email, String phone, String sex, Date dateOfBirth, String accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.Phone = phone;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.accountType = accountType;
    }

    public UserInformation() {
    }

    protected UserInformation(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        Phone = in.readString();
        sex = in.readString();
        accountType = in.readString();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
