package com.cloud.ibm.banking.IBMBanking.Persistence.Entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.Objects;

public class AccountInformation0EntityPK implements Serializable {
    private int id;
    private String identity;

    @Column(name = "Id")
    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "identity")
    @Id
    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountInformation0EntityPK that = (AccountInformation0EntityPK) o;
        return id == that.id &&
                Objects.equals(identity, that.identity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identity);
    }
}
