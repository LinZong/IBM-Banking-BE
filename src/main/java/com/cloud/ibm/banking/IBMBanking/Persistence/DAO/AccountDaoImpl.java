package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Model.Request.RegisterModel;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal1Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountFinancialProduct0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Helper.GenGUID;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.List;

import static com.cloud.ibm.banking.IBMBanking.Persistence.Helper.DateUtil.date2TimeStamp;
import static com.cloud.ibm.banking.IBMBanking.Persistence.Helper.DateUtil.timeStamp;
import static com.cloud.ibm.banking.IBMBanking.Service.AccountService.IdentityBucketIndex;


@Repository
public class AccountDaoImpl {

    private SessionFactory sessionFactory;

    public AccountDaoImpl() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public boolean IsAccountRegistered(String identity, int bucket) {
        try (Session session = sessionFactory.openSession()) {
            SQL query = new SQL();
            query
                    .SELECT("COUNT(*)")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("identity = :id");

            BigInteger count = (BigInteger) session.createSQLQuery(query.toString())
                    .setParameter("id", identity)
                    .getSingleResult();

            return ((count).intValue() == 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public List<AccountDeal1Entity> QueryMyDeal(int accountId,int bucket,Long begin, Long end)
    {

        Session session = sessionFactory.openSession();
        try {

            SQL deals = new SQL();
            deals.SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountDeal1Entity.class) + bucket)
                    .WHERE("account_id = :acc_id");

            if(begin != null) {
                deals.WHERE("time >= :timebegin");
            }
            if(end != null) {
                deals.WHERE("time < :timeend");
            }
            deals.ORDER_BY("time desc");


            Query query = session.createSQLQuery(deals.toString())
                    .setParameter("acc_id",accountId);

            if(begin != null) {
                query.setParameter("timebegin",begin);
            }
            if(end != null) {
                query.setParameter("timeend",end);
            }
            ((NativeQuery) query).addEntity(AccountDeal1Entity.class);

            return query.getResultList();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            session.close();
        }
        return null;
    }

    public boolean RegisterAccount(RegisterModel model, int bucket) {
        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();
        try {
            SQL query = new SQL();
            query.INSERT_INTO(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .VALUES("password, identity", ":pw, :id");

            int effect = session.createSQLQuery(query.toString())
                    .setParameter("pw", model.getPassword())
                    .setParameter("id", model.getIdentity())
                    .executeUpdate();

            if (effect == 1) {

                SQL cust = new SQL();

                cust.SELECT("*")
                        .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                        .WHERE("identity = :id");

                List result = session.createSQLQuery(cust.toString())
                        .setParameter("id", model.getIdentity())
                        .addEntity(AccountInformation0Entity.class)
                        .getResultList();

                if (!result.isEmpty()) {

                    SQL customer = new SQL();
                    customer.INSERT_INTO(BucketNamingStrategyCollections.collections.get(CustomerInformation0Entity.class) + bucket)
                            .VALUES("id", ":id");

                    int insertedCust = session.createSQLQuery(customer.toString())
                            .setParameter("id", ((AccountInformation0Entity) result.get(0)).getId())
                            .executeUpdate();
                    if (insertedCust == 1) {
                        tr.commit();
                        return true;
                    } else {
                        tr.rollback();
                        return false;
                    }
                } else {

                    tr.rollback();
                    return false;
                }

            } else {
                tr.rollback();
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        } finally {
            session.close();
        }
        return false;
    }

    public CustomerInformation0Entity GetCustomerInformation(int bucket, int id) {
        try (Session session = sessionFactory.openSession()) {
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

    public int save_money(double money, int id, int bucket) {
        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();
        try {

            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("balance = balance + :money", "last_deal_time = :lastDealTime")
                    .WHERE("id = :id");

            int count = session.createSQLQuery(query.toString())
                    .setParameter("money", money)
                    .setParameter("lastDealTime", timeStamp())
                    .setParameter("id", id)
                    .executeUpdate();

            int count1 = writeIntoPipeLine(money, id, bucket, session);

            tr.commit();
            return count + count1;

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return 0;
    }

    public int writeIntoPipeLine(double money, int id, int bucket, Session session) {
        SQL query1 = new SQL();
        query1
                .INSERT_INTO(BucketNamingStrategyCollections.collections.get(AccountDeal1Entity.class) + bucket)
                .VALUES("time", ":time")
                .VALUES("amount", ":amount")
                .VALUES("uuid", ":uuid")
                .VALUES("account_id", ":account_id");

        int count1 = session.createSQLQuery(query1.toString())
                .setParameter("time", timeStamp())
                .setParameter("amount", money)
                .setParameter("uuid", GenGUID.genGUID())
                .setParameter("account_id", GetUserById(id, bucket).getId())
                .executeUpdate();
        return count1;
    }

    public int withdraw_money(double money, int id, int bucket) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();
        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("balance = balance - :money", "last_deal_time = :lastDealTime")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("money", money)
                    .setParameter("lastDealTime", timeStamp())
                    .setParameter("id", id)
                    .executeUpdate();

            int count1 = writeIntoPipeLine(-money, id, bucket, session);
            tr.commit();
            return effects + count1;

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();

        } finally {
            session.close();
        }
        return -1;
    }

    public int transfer_money(double money, int id, int bucket, int otherId, String otherIdentity) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();
        int otherBucket = IdentityBucketIndex(otherIdentity);
        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("balance = balance - :money", "last_deal_time = :lastDealTime")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("money", money)
                    .setParameter("lastDealTime", timeStamp())
                    .setParameter("id", id)
                    .executeUpdate();

            int count1 = writeIntoPipeLine(-money, id, bucket, session);


            SQL query1 = new SQL();
            query1
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + otherBucket)
                    .SET("balance = balance + :money", "last_deal_time = :lastDealTime")
                    .WHERE("id = :otherId");
            int effects1 = session.createSQLQuery(query1.toString())
                    .setParameter("money", money)
                    .setParameter("lastDealTime", timeStamp())
                    .setParameter("otherId", otherId)
                    .executeUpdate();

            int count2 = writeIntoPipeLine(money, otherId, otherBucket, session);

            int totalEffect = effects + effects1 + count1 + count2;
            if (totalEffect != 4) {
                tr.rollback();
                return -1;
            } else {
                tr.commit();
                return 4;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();

        } finally {
            session.close();
        }
        return -1;
    }

    public int modifyUserInfo(String name, String address, String telNum, String email, int bucket, int id) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();

        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(CustomerInformation0Entity.class) + bucket)
                    .SET("name = :name")
                    .SET("address = :address")
                    .SET("tel_num = :tel_num")
                    .SET("mail = :mail")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .setParameter("address", address)
                    .setParameter("tel_num", telNum)
                    .setParameter("mail", email)
                    .executeUpdate();

            tr.commit();
            return effects;

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();

        } finally {
            session.close();
        }
        return -1;

    }

    public int modifyPassword(String password, int bucket, int id) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();

        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("password = :password")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("password", password)
                    .setParameter("id", id)
                    .executeUpdate();

            tr.commit();
            return effects;

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();

        } finally {
            session.close();
        }
        return -1;

    }

    public String oldPassword(int bucket, int id) {
        Session session = sessionFactory.openSession();

        try {
            SQL query = new SQL();
            query
                    .SELECT("password")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            String result = (String) session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .getSingleResult();


            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }

    public int oldPayingPassword(int bucket, int id) {
        Session session = sessionFactory.openSession();

        try {
            SQL query = new SQL();
            query
                    .SELECT("paying_password")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            Integer result = (Integer) session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .getSingleResult();

            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return -1;

    }

    public List<AccountDeal1Entity> pipeLine(int id, int bucket, String timeBegin, String timeEnd) {
        Session session = sessionFactory.openSession();
        try {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountDeal1Entity.class) + bucket)
                    .WHERE("account_id = :id AND time >= :time_begin AND time < :time_end");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .setParameter("time_begin", Long.parseLong(timeBegin))
                    .setParameter("time_end", Long.parseLong(timeEnd))
                    .addEntity(AccountDeal1Entity.class)
                    .getResultList();
            if (!result.isEmpty()) {
                return result;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return null;
    }



    public int buyProduct1(int id, double money, int bucket) {
        Session session = sessionFactory.openSession();

        if (ifHaveBuyProduct1(id, bucket, session)) {
            Transaction tr = session.beginTransaction();
            try {

                SQL query = new SQL();
                query
                        .UPDATE(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                        .SET("balance = balance + :money")
                        .WHERE("account_id = :id");

                int effects = session.createSQLQuery(query.toString())
                        .setParameter("money", money)
                        .setParameter("id", id)
                        .executeUpdate();

                tr.commit();
                return effects;

            } catch (Exception ex) {
                ex.printStackTrace();
                tr.rollback();

            } finally {
                session.close();
            }
            return -1;
        }
    else
    {
        return registerProduct1(money,id,bucket,session)+1;
    }
}
    public int registerProduct1(double money, int id, int bucket,Session session) {
        SQL query1 = new SQL();
        query1
                .INSERT_INTO(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                .VALUES("balance", ":money")
                .VALUES("account_id", ":account_id");

        int count1 = session.createSQLQuery(query1.toString())
                .setParameter("money", money)
                .setParameter("account_id",id)
                .executeUpdate();
        return count1;
    }

    public boolean ifHaveBuyProduct1(int id,int bucket,Session session)
    {
        try
        {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                    .WHERE("account_id = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .addEntity(AccountFinancialProduct0Entity.class)
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
        }
        return false;
    }

    public int modifyPayingPassword(int payingPassword, int bucket, int id)
    {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();

        try
        {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("paying_password = :payingPassword")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("payingPassword", payingPassword)
                    .setParameter("id", id)
                    .executeUpdate();

            tr.commit();
            return effects;

        } catch (Exception ex)
        {
            ex.printStackTrace();
            tr.rollback();

        } finally
        {
            session.close();
        }
        return -1;
    }

    public AccountInformation0Entity queryWithdrawAccount(int id, int bucket)
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

            if (!result.isEmpty())
            {
                return (AccountInformation0Entity) result.get(0);
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

    public WithBucket<AccountInformation0Entity> GetUserInfoByIdentity(String identity)
    {
        Session session = sessionFactory.openSession();
        int bucket = IdentityBucketIndex(identity);
        try
        {

            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("identity = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", identity)
                    .addEntity(AccountInformation0Entity.class)
                    .getResultList();

            if (!result.isEmpty())
            {
                return new WithBucket<>(bucket, (AccountInformation0Entity) result.get(0));
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

    public AccountInformation0Entity GetUserById(int id, int bucket)
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

            if (!result.isEmpty())
            {
                return (AccountInformation0Entity) result.get(0);
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
