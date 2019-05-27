package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Helper.GenGUID;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import com.cloud.ibm.banking.IBMBanking.Service.ReturnToFront;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

    public CustomerInformation0Entity GetCustomerInformation(int bucket, int id)
    {
        try (Session session = sessionFactory.openSession())
        {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(CustomerInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .addEntity(CustomerInformation0Entity.class)
                    .getResultList();

            return !result.isEmpty() ? (CustomerInformation0Entity) result.get(0) : null;
        }
    }

    public WithBucket<AccountInformation0Entity> save_money(double money, int id, int buckey)
    {
        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();
        try
        {

            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + buckey)
                    .SET("balance = :money")
                    .WHERE("id = :id");

            int count = session.createSQLQuery(query.toString())
                    .setParameter("money",(float) money)
                    .setParameter("id",id)
                    .executeUpdate();

            /*
                更新数据库不应该再采用getResultList()语句，而是应该使用executeUpdate语句执行SQL，并且接受此语句返回的受影响行数来确定数据有没有被真的更新

                其次，数据库写入操作(update, insert)都需要手动启事务。不能不加事务读写。
            * */
            tr.commit();

        } catch (Exception ex)
        {
            ex.printStackTrace();
            tr.rollback();
        } finally
        {
            if (session != null)
            {
                session.close();
            }
        }
        return null;
    }

    public int withdraw_money(double money, int id, int buckey, int payingPassword)
    {
        Session session = sessionFactory.openSession();
        if (!testPayingPassWord(id, payingPassword, buckey))
            return 1002; //支付密码错误
        else if (queryBalance(id, buckey) < money)
            return 1003;//余额不足
        else
        {
            try
            {
                SQL query = new SQL();
                query
                        .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + buckey)
                        .SET("balance = balance- :money")
                        .WHERE("id = :id");

                List result = session.createSQLQuery(query.toString())
                        .setParameter("money", money)
                        .setParameter("id", id)
                        .addEntity(AccountInformation0Entity.class)
                        .getResultList();
                if (!result.isEmpty())
                {
                    return 1001;
                }

            } catch (Exception ex)
            {
                ex.printStackTrace();
            } finally
            {
                if (session != null)
                {
                    session.close();
                }
            }
        }
        return 1000;
    }

    public int transfer_money(double money, int id, int buckey, int payingPassword, int otherId)
    {
        return 0;
    }

    public boolean testPayingPassWord(int id, int payingPassingWord, int bucket)
    {

        Session session = sessionFactory.openSession();
        try
        {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id")
                    .WHERE("payingPassword = :payingPassingWord");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .setParameter("payingPassword", payingPassingWord)
                    .addEntity(AccountInformation0Entity.class)
                    .getResultList();

            if (!result.isEmpty())
            {
                return true;
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            if (session != null)
            {
                session.close();
            }
        }
        return false;
    }

    public double queryBalance(int id, int bucket)
    {
        Session session = sessionFactory.openSession();
        try
        {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .addEntity(AccountInformation0Entity.class)
                    .getResultList();

            System.out.println(result);

        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            if (session != null)
            {
                session.close();
            }
        }
        return -1;
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
        } catch (Exception ex)
        {
            ex.printStackTrace();
        } finally
        {
            if (session != null)
            {
                session.close();
            }
        }
        return null;
    }
}
