package com.cloud.ibm.banking.IBMBanking.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil
{
    private static SessionFactory sessionFactory;

    public static SessionFactory GetSessionFactory()
    {
        if(sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public synchronized static SplitTableInterceptor SplitTableInterceptorFactory(String OriginalTableName,
                                                                     String ModifiedTableName)
    {
        SplitTableInterceptor si = new SplitTableInterceptor();
        si.setTargetTableName(OriginalTableName);
        si.setTempTableName(ModifiedTableName);
        return si;
    }


}
