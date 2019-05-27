package com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.BankingEntity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;

import java.util.HashMap;

public class BucketNamingStrategyCollections
{
    public static final HashMap<Class<? extends BankingEntity>, String> collections = new HashMap<>();
    public static final int TableRange = 3;
    public static final Class[] Buckets = new Class[]{
            AccountInformation0Entity.class,
            CustomerInformation0Entity.class,
            AccountDeal0Entity.class};

    public static final String[] BucketPrefix = new String[]{
            "account_information",
            "customer_information",
            "account_deal"
    };

    static {
        for (int i = 0; i < TableRange; i++)
        {
            collections.put(Buckets[i],BucketPrefix[i]);
        }
    }
}
