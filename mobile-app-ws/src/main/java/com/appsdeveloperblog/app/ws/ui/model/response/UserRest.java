package com.appsdeveloperblog.app.ws.ui.model.response;

import java.util.List;

public class UserRest {
    String userId;
    String firstName;
    String lastName;
    String email;
    List<AddressesRest> addresses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public List<AddressesRest> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressesRest> addresses) {
        this.addresses = addresses;
    }
}
