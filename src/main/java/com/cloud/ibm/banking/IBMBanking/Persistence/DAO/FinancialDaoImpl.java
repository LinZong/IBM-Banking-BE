package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;


import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountFinancialProduct0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.FinancialProductEntity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Helper.DateUtil;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.BucketNamingStrategyCollections;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FinancialDaoImpl {


    @Autowired
    private AccountDaoImpl accountDao;

    private SessionFactory sessionFactory;

    public FinancialDaoImpl() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public boolean BuyProduct(int ProductID, double ValueCount, int bucket, int AccountID, boolean AppendBuy) {

        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();
        try {
            if (AppendBuy) {
                // 先查询此前有没有购买，如果有就新增，没有就插入

                SQL DetectIfBuy = new SQL();

                DetectIfBuy.SELECT("*")
                        .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                        .WHERE("id = :id AND product_id = :prod_id AND status = 1");

                List<AccountFinancialProduct0Entity> buyRecord = (List<AccountFinancialProduct0Entity>) session.createSQLQuery(DetectIfBuy.toString())
                        .setParameter("id", AccountID)
                        .setParameter("prod_id", ProductID)
                        .addEntity(AccountFinancialProduct0Entity.class)
                        .getResultList();


                if (!buyRecord.isEmpty()) {
                    // 准备追加购买

                    SQL appendBuy = new SQL();

                    appendBuy.UPDATE(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                            .SET("balance = balance + :append")
                            .WHERE("id = :record_id");

                    int appendBuyEffect = session.createSQLQuery(appendBuy.toString())
                            .setParameter("append", ValueCount)
                            .setParameter("record_id", buyRecord.get(0).getId())
                            .executeUpdate();
                    if (appendBuyEffect == 1) {
                        accountDao.withdraw_money(ValueCount,AccountID,bucket);
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
                    accountDao.withdraw_money(ValueCount,AccountID,bucket);
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

    public List<AccountFinancialProduct0Entity> QueryOwnProduct(int Status,int AccountID,int bucket,Long TimeBegin,Long TimeEnd)
    {

        // Status 0 为正常取出,
        // Status 1 是正在生效
        // Status 2 是提前取出

        SQL owner = new SQL();

        Session session = sessionFactory.openSession();

        owner.SELECT("*")
                .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                .WHERE("account_id = :acc_id AND status = :status");

        if(TimeBegin != null) {
            owner.WHERE("buy_time >= :time_begin");
        }

        if(TimeEnd != null) {
            owner.WHERE("buy_time < :time_end");
        }

        Query query = session.createSQLQuery(owner.toString())
                .setParameter("acc_id",AccountID)
                .setParameter("status",Status);

        if(TimeBegin != null)
        {
            query.setParameter("time_begin",TimeBegin);
        }
        if(TimeEnd != null)
        {
            query.setParameter("time_end",TimeEnd);
        }

        ((NativeQuery) query).addEntity(AccountFinancialProduct0Entity.class);
        List queryResult = query.getResultList();
        return queryResult;
    }

    public List<AccountFinancialProduct0Entity> QuerySingleBuyRecord(Session session, int bucket, int buyRecordID)
    {
        SQL query = new SQL();
        query.SELECT("*")
                .FROM(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                .WHERE("id = :id");

        return (List<AccountFinancialProduct0Entity>) session.createSQLQuery(query.toString())
                .setParameter("id",buyRecordID)
                .addEntity(AccountFinancialProduct0Entity.class)
                .getResultList();
    }

    public WithdrawStatus WithdrawOwnProduct(int bucket, int BuyRecordID,double WithdrawValue)
    {
        Session session = sessionFactory.openSession();
        Transaction tr = session.beginTransaction();

        // 只有余额宝可以分次取，其他的理财产品全部只能一次取完。由Service层保证金额
        // 一次取完的理财产品WithdrawValue一定是这个产品的金额最大值

        List<AccountFinancialProduct0Entity> recordList = QuerySingleBuyRecord(session,bucket,BuyRecordID);
        if(recordList.isEmpty()) {
            tr.rollback();
            return WithdrawStatus.NO_RECORD;
        }

        AccountFinancialProduct0Entity record = recordList.get(0);

        if(record.getStatus() != 1) {
            tr.rollback();
            return WithdrawStatus.HAVE_WITHDRAWED;
        }

        double BuyAmount = record.getBalance();
        if(BuyAmount < WithdrawValue)
        {
            tr.rollback();
            return WithdrawStatus.NOT_ENOUGH_BALANCE;
        }

        SQL withdraw = new SQL();
        withdraw.UPDATE(BucketNamingStrategyCollections.collections.get(AccountFinancialProduct0Entity.class) + bucket)
                .SET("balance = :balance")
                .WHERE("id = :id");
        if(record.getProductId() != 1) {
            // 探测是超时还是没有超时,现在先不判定，等理财产品信息做完加载之后再判断

            withdraw.SET("status = 0");
        }

        Query WithdrawQuery = session.createSQLQuery(withdraw.toString())
                .setParameter("id",BuyRecordID);

        if(record.getProductId() == 1) {
            WithdrawQuery.setParameter("balance",(BuyAmount - WithdrawValue));
        }
        else {
            WithdrawQuery.setParameter("balance",0);
            WithdrawValue = BuyAmount;
        }

        int WithdrawCount = WithdrawQuery.executeUpdate();
        if(WithdrawCount == 1) {
            // 已经修改完购买理财产品的情况，开始增加余额
            int incrementCount = accountDao.save_money(WithdrawValue,record.getAccountId(),bucket);
            if(incrementCount == 2) {
                tr.commit();
                return WithdrawStatus.SUCCESSFUL;
            }
            else {
                tr.rollback();
                return WithdrawStatus.SAVEMONEY_OP_DF_FAILED;
            }
        }
        else {
            tr.rollback();
            return WithdrawStatus.WITHDRAW_OP_DB_FAILED;
        }
    }

    public enum WithdrawStatus {
        SUCCESSFUL,
        NOT_ENOUGH_BALANCE,
        NO_RECORD,
        HAVE_WITHDRAWED,
        WITHDRAW_OP_DB_FAILED,
        SAVEMONEY_OP_DF_FAILED
    }


    public List<FinancialProductEntity> AllFinancialProducts()
    {
        Session session = sessionFactory.openSession();
        SQL all = new SQL();
        all.SELECT("*")
                .FROM(BucketNamingStrategyCollections.collections.get(FinancialProductEntity.class));

        return session.createSQLQuery(all.toString())
                .addEntity(FinancialProductEntity.class)
                .getResultList();
    }

}
