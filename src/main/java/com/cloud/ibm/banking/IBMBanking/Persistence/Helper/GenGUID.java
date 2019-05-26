package com.cloud.ibm.banking.IBMBanking.Persistence.Helper;

import java.util.UUID;

public class GenGUID {
    public static UUID genGUID()
    {
       UUID uuid=java.util.UUID.randomUUID();
       return uuid;
    }
}
