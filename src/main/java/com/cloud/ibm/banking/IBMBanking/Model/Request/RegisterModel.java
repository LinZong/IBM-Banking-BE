package com.cloud.ibm.banking.IBMBanking.Model.Request;

public class RegisterModel {
    private String identity;
    private String password;

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean ValidModel()
    {
        return !identity.isEmpty() && !password.isEmpty();
    }
}
