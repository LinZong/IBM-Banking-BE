package com.cloud.ibm.banking.IBMBanking.Model;

public class FinancialProductStrategy
{
    private int Id;
    private double Rate;
    private int RateCycle;
    private String Description;

    public int getId()
    {
        return Id;
    }

    public void setId(int id)
    {
        Id = id;
    }

    public double getRate()
    {
        return Rate;
    }

    public void setRate(double rate)
    {
        Rate = rate;
    }

    public int getRateCycle()
    {
        return RateCycle;
    }

    public void setRateCycle(int rateCycle)
    {
        RateCycle = rateCycle;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }
}
