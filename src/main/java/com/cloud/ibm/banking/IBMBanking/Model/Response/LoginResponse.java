package com.cloud.ibm.banking.IBMBanking.Model.Response;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;

public class LoginResponse extends CommonResponse
{
    private CustomerInformation0Entity CustomerInfo;
    private int bucket;
    private String identity;

    public LoginResponse(int statusCode, CustomerInformation0Entity customerInfo,int bucket,String identity)
    {
        super(statusCode);
        CustomerInfo = customerInfo;
        this.bucket=bucket;
        this.identity=identity;
    }

    public void setCustomerInfo(CustomerInformation0Entity customerInfo)
    {
        CustomerInfo = customerInfo;
    }

    public CustomerInformation0Entity getCustomerInfo()
    {
        return CustomerInfo;
    }

    public void setBucket(int bucket) {
        this.bucket = bucket;
    }

    public int getBucket() {
        return bucket;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
}
