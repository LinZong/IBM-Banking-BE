package com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy;


import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class BucketNamingStrategy extends PhysicalNamingStrategyStandardImpl
{
    private String TablePrefix;
    private int TableIndex;

    public BucketNamingStrategy(String tablePrefix,int tableIdx)
    {
        TablePrefix = tablePrefix;
        TableIndex = tableIdx;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context)
    {
        return Identifier.toIdentifier(TablePrefix + TableIndex);
    }
}
