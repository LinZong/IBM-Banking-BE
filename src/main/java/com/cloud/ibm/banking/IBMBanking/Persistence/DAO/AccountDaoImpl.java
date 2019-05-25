package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableInterceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class AccountDaoImpl
{

    private SessionFactory sessionFactory;

    public AccountInformation0Entity findAccountFromAllTable(Integer id, int tableBegin, int tableEnd)
    {

        sessionFactory = new Configuration().configure().buildSessionFactory();

        SplitTableInterceptor splitTableInterceptor =  new SplitTableInterceptor();
        splitTableInterceptor.setTargetTableName("account");
        splitTableInterceptor.setTempTableName("account_information"+id);

        Session session = sessionFactory.openSession();

        TypedQuery<AccountInformation0Entity> eq = session.createQuery("FROM AccountInformation0Entity ",AccountInformation0Entity.class);
        List<AccountInformation0Entity> ac = ((Query<AccountInformation0Entity>) eq).list();



        return null;
    }
}
