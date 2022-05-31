package com.android.onlineshoppingapp.models;

import java.util.List;

public class UserAddress extends UserInformation {

    private List<String> listOfAddress;

    public UserAddress() {
        super();
        listOfAddress = null;
    }

    public UserAddress(List<String> listOfAddress) {
        super();
        this.listOfAddress = listOfAddress;
    }

    public List<String> getListOfAddress() {
        return listOfAddress;
    }

    public void setListOfAddress(List<String> listOfAddress) {
        this.listOfAddress = listOfAddress;
    }
}
