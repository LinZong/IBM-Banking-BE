package com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.*;

import java.util.HashMap;

public class BucketNamingStrategyCollections
{
    public static final HashMap<Class<? extends BankingEntity>, String> collections = new HashMap<>();
    public static final int TableRange = 5;
    public static final Class[] Buckets = new Class[]{
            AccountInformation0Entity.class,
            CustomerInformation0Entity.class,
            AccountDeal1Entity.class,
            AccountFinancialProduct0Entity.class,
            FinancialProductEntity.class
    };

    public static final String[] BucketPrefix = new String[]{
            "account_information",
            "customer_information",
            "account_deal",
            "account_financial_product",
            "financial_product"
    };

    static {
        for (int i = 0; i < TableRange; i++)
        {
            collections.put(Buckets[i],BucketPrefix[i]);
        }
    }
}
