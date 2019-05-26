package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Helper.HibernateUtil;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl
{

    private SessionFactory sessionFactory;

    public AccountDaoImpl() {
        sessionFactory = HibernateUtil.GetSessionFactory();
    }

   public AccountInformation0Entity GetUserByIdentity(String identity, String password)
   {
       try (Session session = sessionFactory.openSession())
       {

           for (int i = 0; i < 10; i++)
           {
               SQL Sql = new SQL();
               Sql.SELECT("*").FROM("account_information" + i).WHERE("identity = " + identity, "password = " + password);

               String queryString = Sql.toString();

               List result = session.createSQLQuery(queryString).addEntity(AccountInformation0Entity.class).getResultList();
               if (!result.isEmpty())
               {
                   return (AccountInformation0Entity) result.get(0);
               }
           }
           return null;
       }
   }

   public AccountInformation0Entity CreateUser()
   {
       try (Session session = sessionFactory.openSession()) {
           Transaction tr2 = session.beginTransaction();
           AccountInformation0Entity entity = new AccountInformation0Entity();
           entity.setBalance(150);
           entity.setManageType(1);
           entity.setPassword("123456");
           entity.setIdentity("15521332013");
           entity.setLastDealTime(20190525L);
           entity.setId(1);

           session.save(entity);
           tr2.commit();
           return entity;
       }
   }
}
