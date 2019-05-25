package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "account_deal1", schema = "banking", catalog = "")
public class AccountDeal1Entity
{
    private Long time;
    private Double amount;
    private String accountName;

    @Basic
    @Column(name = "time", nullable = true)
    public Long getTime()
    {
        return time;
    }

    public void setTime(Long time)
    {
        this.time = time;
    }

    @Basic
    @Column(name = "amount", nullable = true, precision = 0)
    public Double getAmount()
    {
        return amount;
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    @Basic
    @Column(name = "account_name", nullable = false, length = 15)
    public String getAccountName()
    {
        return accountName;
    }

    public void setAccountName(String accountName)
    {
        this.accountName = accountName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountDeal1Entity that = (AccountDeal1Entity) o;
        return Objects.equals(time, that.time) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(accountName, that.accountName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(time, amount, accountName);
    }

    private Integer id;

    @Id
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}
