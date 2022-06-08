package com.android.onlineshoppingapp.models;

import java.util.List;

public class UserAddress {

    private String name;
    private String phone;
    private String detail;
    private String city;
    private String district;
    private String town;
    private boolean defaultAddress;
    private Long cityCode;
    private Long districtCode;
    private Long townCode;

    public UserAddress(String name, String phone, String detail, String city, String district, String town, boolean defaultAddress, Long cityCode, Long districtCode, Long townCode) {
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.city = city;
        this.district = district;
        this.town = town;
        this.defaultAddress = defaultAddress;
        this.cityCode = cityCode;
        this.districtCode = districtCode;
        this.townCode = townCode;
    }

    public UserAddress() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getFullAddress() {
        return this.getDetail() + ", " + this.getTown() + ", " + this.getDistrict() + ", " + this.getCity();
    }

    public Long getCityCode() {
        return cityCode;
    }

    public void setCityCode(Long cityCode) {
        this.cityCode = cityCode;
    }

    public Long getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(Long districtCode) {
        this.districtCode = districtCode;
    }

    public Long getTownCode() {
        return townCode;
    }

    public void setTownCode(Long townCode) {
        this.townCode = townCode;
    }
}
