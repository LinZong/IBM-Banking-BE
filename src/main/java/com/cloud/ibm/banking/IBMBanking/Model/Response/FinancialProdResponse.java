package com.cloud.ibm.banking.IBMBanking.Model.Response;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.FinancialProductEntity;

import java.util.List;

public class FinancialProdResponse extends CommonResponse
{

    private List<FinancialProductEntity> productEntities;

    public FinancialProdResponse(int statusCode, List<FinancialProductEntity> productEntities)
    {
        super(statusCode);
        this.productEntities = productEntities;
    }

    public List<FinancialProductEntity> getProductEntities()
    {
        return productEntities;
    }

    public void setProductEntities(List<FinancialProductEntity> productEntities)
    {
        this.productEntities = productEntities;
    }
}
