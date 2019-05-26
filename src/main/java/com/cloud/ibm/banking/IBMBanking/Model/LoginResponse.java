package com.cloud.ibm.banking.IBMBanking.Model;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;

public class LoginResponse extends CommonResponse
{
    public LoginResponse(int statusCode, CustomerInformation0Entity customerInfo)
    {
        super(statusCode);
        CustomerInfo = customerInfo;
    }

    private CustomerInformation0Entity CustomerInfo;

    public void setCustomerInfo(CustomerInformation0Entity customerInfo)
    {
        CustomerInfo = customerInfo;
    }

    public CustomerInformation0Entity getCustomerInfo()
    {
        return CustomerInfo;
    }
}
