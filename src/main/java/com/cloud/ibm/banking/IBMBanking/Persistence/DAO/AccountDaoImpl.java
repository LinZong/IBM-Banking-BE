package com.cloud.ibm.banking.IBMBanking.Persistence.DAO;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.HibernateUtil;
import org.apache.ibatis.jdbc.SQL;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccountDaoImpl
{

    private SessionFactory sessionFactory;

    public AccountDaoImpl() {
        sessionFactory = HibernateUtil.GetSessionFactory();
    }

   public AccountInformation0Entity GetUserByIdentity(String identity,String password)
   {
       try (Session session = sessionFactory.openSession())
       {

           for (int i = 0; i < 10; i++)
           {
               SQL Sql = new SQL();
               Sql.SELECT("*").FROM("account_information" + i).WHERE("identity = " + identity, "password = " + password);

               String queryString = Sql.toString();

               List result = session.createSQLQuery(Sql.toString()).addEntity(AccountInformation0Entity.class).getResultList();
               if (!result.isEmpty())
               {
                   return (AccountInformation0Entity) result.get(0);
               }
           }
           return null;
       }
   }
}
