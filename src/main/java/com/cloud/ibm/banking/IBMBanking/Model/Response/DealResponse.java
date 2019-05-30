package com.cloud.ibm.banking.IBMBanking.Model.Response;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal1Entity;

import java.util.List;

public class DealResponse extends CommonResponse
{
    private List<AccountDeal1Entity> deal1Entities;

    public DealResponse(int statusCode, List<AccountDeal1Entity> deal1Entities)
    {
        super(statusCode);
        this.deal1Entities = deal1Entities;
    }

    public List<AccountDeal1Entity> getDeal1Entities()
    {
        return deal1Entities;
    }

    public void setDeal1Entities(List<AccountDeal1Entity> deal1Entities)
    {
        this.deal1Entities = deal1Entities;
    }
}
