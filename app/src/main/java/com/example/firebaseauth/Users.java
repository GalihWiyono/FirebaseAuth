package com.example.firebaseauth;

public class Users {

    private String email, password, fullname, phonenumer;

    public Users() {
    }

    public Users(String email, String password, String fullname, String phonenumer) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.phonenumer = phonenumer;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhonenumer() {
        return phonenumer;
    }

    public void setPhonenumer(String phonenumer) {
        this.phonenumer = phonenumer;
    }
}
