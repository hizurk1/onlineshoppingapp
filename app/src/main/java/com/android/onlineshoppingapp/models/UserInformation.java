package com.android.onlineshoppingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class UserInformation implements Parcelable {
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
        Phone = phone;
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

    public static final Creator<UserInformation> CREATOR = new Creator<UserInformation>() {
        @Override
        public UserInformation createFromParcel(Parcel in) {
            return new UserInformation(in);
        }

        @Override
        public UserInformation[] newArray(int size) {
            return new UserInformation[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(Phone);
        parcel.writeString(sex);
        parcel.writeString(accountType);
    }
}
