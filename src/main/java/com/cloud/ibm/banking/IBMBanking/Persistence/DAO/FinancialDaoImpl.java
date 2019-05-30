package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;


import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountFinancialProduct0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Helper.DateUtil;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategy;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FinancialDaoImpl {


    @Autowired
    private AccountDaoImpl accountDao;

    private SessionFactory sessionFactory;

    public FinancialDaoImpl() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    private boolean BuyProduct(int ProductID, int ValueCount, int bucket, int AccountID, boolean AppendBuy) {

        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();
        try {
            if (AppendBuy) {
                // 先查询此前有没有购买，如果有就新增，没有就插入

                SQL DetectIfBuy = new SQL();

                DetectIfBuy.SELECT("*")
                        .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                        .WHERE("id = :id AND product_id = :prod_id AND status = 1");

                AccountFinancialProduct0Entity buyRecord = (AccountFinancialProduct0Entity) session.createSQLQuery(DetectIfBuy.toString())
                        .setParameter("id", AccountID)
                        .setParameter("prod_id", ProductID)
                        .addEntity(AccountFinancialProduct0Entity.class)
                        .getSingleResult();


                if (buyRecord != null) {
                    // 准备追加购买

                    SQL appendBuy = new SQL();

                    appendBuy.UPDATE(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                            .SET("balance = balance + :append")
                            .WHERE("id = :record_id");

                    int appendBuyEffect = session.createSQLQuery(appendBuy.toString())
                            .setParameter("append", ValueCount)
                            .setParameter("record_id", buyRecord.getId())
                            .executeUpdate();
                    if (appendBuyEffect == 1) {
                        tr.commit();
                        return true;
                    } else {
                        tr.rollback();
                        return false;
                    }
                }
            }

            // 新增购买, 与追加购买但是数据库中没有记录的情况合并


            SQL firstBuy = new SQL();

            firstBuy.INSERT_INTO(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                    .VALUES("product_id", ":prod_id")
                    .VALUES("buy_time", ":buy_time")
                    .VALUES("status", "1")
                    .VALUES("account_id", String.valueOf(AccountID))
                    .VALUES("balance", ":balance");

            int firstBuyEffect = session.createSQLQuery(firstBuy.toString())
                    .setParameter("prod_id", ProductID)
                    .setParameter("buy_time", DateUtil.timeStamp())
                    .setParameter("balance", ValueCount)
                    .executeUpdate();

            if (firstBuyEffect == 1) {
                tr.commit();
                return true;
            } else {
                tr.rollback();
                return false;
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            return false;
        }
        finally {
            session.close();
        }
    }

    public void QueryOwnProduct(int Status,int AccountID,int bucket,Long TimeBegin,Long TimeEnd)
    {

        // Status 0 为正常取出,
        // Status 1 是正在生效
        // Status 2 是提前取出

        SQL owner = new SQL();

        owner.SELECT("*")
                .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class))
                .WHERE("account_id = :acc_id AND status = :");

    }
}
