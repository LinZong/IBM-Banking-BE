package com.cloud.ibm.banking.IBMBanking.Model.Response;

public class CommonResponse
{
    private int StatusCode;

    public CommonResponse(int statusCode)
    {
        StatusCode = statusCode;
    }

    public int getStatusCode()
    {
        return StatusCode;
    }

    public void setStatusCode(int statusCode)
    {
        StatusCode = statusCode;
    }
}
