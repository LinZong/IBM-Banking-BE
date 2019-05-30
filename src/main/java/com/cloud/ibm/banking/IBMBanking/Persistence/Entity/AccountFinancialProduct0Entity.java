package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account_financial_product0", schema = "banking", catalog = "")
public class AccountFinancialProduct0Entity {
    private int id;
    private int productId;
    private long buyTime;
    private int status;
    private Long withdrawTime;
    private int accountId;
    private Double balance;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "product_id")
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Basic
    @Column(name = "buy_time")
    public long getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(long buyTime) {
        this.buyTime = buyTime;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "withdraw_time")
    public Long getWithdrawTime() {
        return withdrawTime;
    }

    public void setWithdrawTime(Long withdrawTime) {
        this.withdrawTime = withdrawTime;
    }

    @Basic
    @Column(name = "account_id")
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    @Basic
    @Column(name = "balance")
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountFinancialProduct0Entity that = (AccountFinancialProduct0Entity) o;
        return id == that.id &&
                productId == that.productId &&
                buyTime == that.buyTime &&
                status == that.status &&
                accountId == that.accountId &&
                Objects.equals(withdrawTime, that.withdrawTime) &&
                Objects.equals(balance, that.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, buyTime, status, withdrawTime, accountId, balance);
    }
}
