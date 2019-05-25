package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "account_information0", schema = "banking", catalog = "")
public class AccountInformation0Entity
{
    private String accountName;
    private String password;
    private double balance;
    private int manageType;
    private Long lastDealTime;
    private int payingPassword;
    private String identity;

    @Id
    @Column(name = "account_name", nullable = false, length = 15)
    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 15)
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Basic
    @Column(name = "balance", nullable = false, precision = 0)
    public double getBalance()
    {
        return balance;
    }

    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    @Basic
    @Column(name = "manage_type", nullable = false)
    public int getManageType()
    {
        return manageType;
    }

    public void setManageType(int manageType)
    {
        this.manageType = manageType;
    }

    @Basic
    @Column(name = "last_deal_time", nullable = true)
    public Long getLastDealTime()
    {
        return lastDealTime;
    }

    public void setLastDealTime(Long lastDealTime)
    {
        this.lastDealTime = lastDealTime;
    }

    @Basic
    @Column(name = "paying_password", nullable = false)
    public int getPayingPassword()
    {
        return payingPassword;
    }

    public void setPayingPassword(int payingPassword)
    {
        this.payingPassword = payingPassword;
    }

    @Basic
    @Column(name = "identity", nullable = false, length = 50)
    public String getIdentity()
    {
        return identity;
    }

    public void setIdentity(String identity)
    {
        this.identity = identity;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInformation0Entity that = (AccountInformation0Entity) o;
        return Double.compare(that.balance, balance) == 0 &&
                manageType == that.manageType &&
                payingPassword == that.payingPassword &&
                Objects.equals(accountName, that.accountName) &&
                Objects.equals(password, that.password) &&
                Objects.equals(lastDealTime, that.lastDealTime) &&
                Objects.equals(identity, that.identity);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(accountName, password, balance, manageType, lastDealTime, payingPassword, identity);
    }
}
