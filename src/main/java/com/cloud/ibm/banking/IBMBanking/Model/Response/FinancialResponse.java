package com.cloud.ibm.banking.IBMBanking.Model.Response;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountFinancialProduct0Entity;

import java.util.List;

public class FinancialResponse extends CommonResponse
{
    private List<AccountFinancialProduct0Entity> accountFinancialInfo;

    public List<AccountFinancialProduct0Entity> getAccountFinancialInfo()
    {
        return accountFinancialInfo;
    }

    public FinancialResponse(int statusCode, List<AccountFinancialProduct0Entity> accountFinancialInfo)
    {
        super(statusCode);
        this.accountFinancialInfo = accountFinancialInfo;
    }

    public void setAccountFinancialInfo(List<AccountFinancialProduct0Entity> accountFinancialInfo)
    {
        this.accountFinancialInfo = accountFinancialInfo;
    }
}
