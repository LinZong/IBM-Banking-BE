package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account_information0", schema = "banking", catalog = "")
@IdClass(AccountInformation0EntityPK.class)
public class AccountInformation0Entity {
    private int id;
    private String password;
    private double balance;
    private int manageType;
    private int payingPassword;
    private String identity;
    private long lastDealTime;

    @Id
    @Column(name = "Id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "balance")
    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Basic
    @Column(name = "manage_type")
    public int getManageType() {
        return manageType;
    }

    public void setManageType(int manageType) {
        this.manageType = manageType;
    }

    @Basic
    @Column(name = "paying_password")
    public int getPayingPassword() {
        return payingPassword;
    }

    public void setPayingPassword(int payingPassword) {
        this.payingPassword = payingPassword;
    }

    @Id
    @Column(name = "identity")
    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Basic
    @Column(name = "last_deal_time")
    public long getLastDealTime() {
        return lastDealTime;
    }

    public void setLastDealTime(long lastDealTime) {
        this.lastDealTime = lastDealTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInformation0Entity entity = (AccountInformation0Entity) o;
        return id == entity.id &&
                Double.compare(entity.balance, balance) == 0 &&
                manageType == entity.manageType &&
                payingPassword == entity.payingPassword &&
                lastDealTime == entity.lastDealTime &&
                Objects.equals(password, entity.password) &&
                Objects.equals(identity, entity.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, balance, manageType, payingPassword, identity, lastDealTime);
    }
}
