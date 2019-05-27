package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Model.Request.RegisterModel;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
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

            tr.commit();
            return effect == 1;
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
                    .SET("balance = balance + :money")
                    .WHERE("id = :id");

            int count = session.createSQLQuery(query.toString())
                    .setParameter("money", (float) money)
                    .setParameter("id", id)
                    .executeUpdate();

            tr.commit();
            return count;

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

    public int withdraw_money(double money, int id, int bucket) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();
        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("balance = balance - :money","last_deal_time = :lastDealTime")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("money", money)
                    .setParameter("lastDealTime",timeStamp())
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

    public int transfer_money(double money, int id, int bucket, int otherId, String otherIdentity) {
        Session session = sessionFactory.openSession();

        Transaction tr = session.beginTransaction();
        int otherBucket = IdentityBucketIndex(otherIdentity);
        try {
            SQL query = new SQL();
            query
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .SET("balance = balance - :money")
                    .WHERE("id = :id");

            int effects = session.createSQLQuery(query.toString())
                    .setParameter("money", money)
                    .setParameter("id", id)
                    .executeUpdate();
            SQL query1 = new SQL();
            query1
                    .UPDATE(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + otherBucket)
                    .SET("balance = balance + :money")
                    .WHERE("id = :otherId");
            int effects1 = session.createSQLQuery(query1.toString())
                    .setParameter("money", money)
                    .setParameter("otherId", otherId)
                    .executeUpdate();

            tr.commit();
            return effects + effects1;

        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();

        } finally {
            session.close();
        }
        return -1;
    }

    public AccountInformation0Entity queryWithdrawAccount(int id, int bucket) {

        Session session = sessionFactory.openSession();
        try {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .addEntity(AccountInformation0Entity.class)
                    .getResultList();

            if (!result.isEmpty()) {
                return (AccountInformation0Entity) result.get(0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }


    public WithBucket<AccountInformation0Entity> GetUserByIdentity(String identity, String password) {
        Session session = sessionFactory.openSession();
        int tableCount = BucketNamingStrategyCollections.TableRange;
        try {
            for (int i = 0; i < tableCount; i++) {
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

                if (!result.isEmpty()) {
                    return new WithBucket<>(i, (AccountInformation0Entity) result.get(0));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public WithBucket<AccountInformation0Entity> GetUserInfoByIdentity(String identity) {
        Session session = sessionFactory.openSession();
        int tableCount = BucketNamingStrategyCollections.TableRange;
        try {
            for (int i = 0; i < tableCount; i++) {
                SQL query = new SQL();
                query
                        .SELECT("*")
                        .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + i)
                        .WHERE("identity = :id");

                List result = session.createSQLQuery(query.toString())
                        .setParameter("id", identity)
                        .addEntity(AccountInformation0Entity.class)
                        .getResultList();

                if (!result.isEmpty()) {
                    return new WithBucket<>(i, (AccountInformation0Entity) result.get(0));
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    public AccountInformation0Entity GetUserById(int id, int bucket) {
        Session session = sessionFactory.openSession();
        try {
            SQL query = new SQL();
            query
                    .SELECT("*")
                    .FROM(BucketNamingStrategyCollections.collections.get(AccountInformation0Entity.class) + bucket)
                    .WHERE("id = :id");

            List result = session.createSQLQuery(query.toString())
                    .setParameter("id", id)
                    .addEntity(AccountInformation0Entity.class)
                    .getResultList();

            if (!result.isEmpty()) {
                return (AccountInformation0Entity) result.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }
}
