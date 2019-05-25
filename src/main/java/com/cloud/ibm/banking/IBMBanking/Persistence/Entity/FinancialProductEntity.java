package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "financial_product", schema = "banking", catalog = "")
public class FinancialProductEntity {
    private int id;
    private String name;
    private String strategy;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "strategy")
    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialProductEntity that = (FinancialProductEntity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(strategy, that.strategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, strategy);
    }
}
