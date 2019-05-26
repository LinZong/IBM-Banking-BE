package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl
{

    private SessionFactory sessionFactory;

    public AccountDaoImpl()
    {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public CustomerInformation0Entity GetCustomerInformation(int bucket,int id)
    {
       try (Session session = sessionFactory.openSession())
       {
           SQL query = new SQL();
           query
                   .SELECT("*")
                   .FROM(BucketNamingStrategyCollections.collections.get(CustomerInformation0Entity.class) + bucket)
                   .WHERE("id = :id");

           List result = session.createSQLQuery(query.toString())
                   .setParameter("id",id)
                   .addEntity(CustomerInformation0Entity.class)
                   .getResultList();

           return !result.isEmpty() ? (CustomerInformation0Entity) result.get(0) : null;
       }
    }

    public WithBucket<AccountInformation0Entity> GetUserByIdentity(String identity, String password)
    {
        Session session = sessionFactory.openSession();
        int tableCount = BucketNamingStrategyCollections.TableRange;
        try
        {
            for (int i = 0; i < tableCount; i++)
            {
                SQL query = new SQL();
                query
                        .SELECT("*")
                        .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + i)
                        .WHERE("identity = :id AND password = :pw");

                List result = session.createSQLQuery(query.toString())
                        .setParameter("id", identity)
                        .setParameter("pw", password)
                        .addEntity(AccountInformation0Entity.class)
                        .getResultList();

                if (!result.isEmpty())
                {
                    return new WithBucket<>(i, (AccountInformation0Entity) result.get(0));
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally
        {
            if (session != null)
            {
                session.close();
            }
        }
        return null;
    }


}
