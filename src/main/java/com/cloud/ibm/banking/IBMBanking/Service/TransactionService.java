package com.cloud.ibm.banking.IBMBanking.Service;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal0Entity;

import java.util.List;

public class TransactionService {

    public void testPayingPassWord(int Id,int payingPassingWord)
    {

    }

    public AccountDeal0Entity saveMoney(double money, AccountDeal0Entity accountDeal0Entity, int paying_password)
    {
        long time=accountDeal0Entity.getTime();
        double amount=accountDeal0Entity.getAmount();
        int id=accountDeal0Entity.getId();
        int productId=accountDeal0Entity.getProductId();
        int accountId=accountDeal0Entity.getAccountId();

        accountDeal0Entity.setAmount(money+amount);
        accountDeal0Entity.setTime(System.currentTimeMillis());
        return accountDeal0Entity;
    }

    public AccountDeal0Entity withdrawMoney(double money,AccountDeal0Entity accountDeal0Entity)
    {
        long time=accountDeal0Entity.getTime();
        double amount=accountDeal0Entity.getAmount();
        int id=accountDeal0Entity.getId();
        int productId=accountDeal0Entity.getProductId();
        int accountId=accountDeal0Entity.getAccountId();

        if(amount>=money)
        {
            accountDeal0Entity.setAmount(amount-money);
            accountDeal0Entity.setTime(System.currentTimeMillis());
        }
        else
        {
            System.out.println("当前账户余额不足");
        }
        return accountDeal0Entity;
    }

    public List<AccountDeal0Entity> transfer(double money, List<AccountDeal0Entity> accountDeal0Entities)
    {
        long time1=accountDeal0Entities.get(0).getTime();
        double amount1=accountDeal0Entities.get(0).getAmount();
        int id1=accountDeal0Entities.get(0).getId();
        int productId1=accountDeal0Entities.get(0).getProductId();
        int accountId1=accountDeal0Entities.get(0).getAccountId();

        long time2=accountDeal0Entities.get(1).getTime();
        double amount2=accountDeal0Entities.get(1).getAmount();
        int id2=accountDeal0Entities.get(1).getId();
        int productId2=accountDeal0Entities.get(1).getProductId();
        int accountId2=accountDeal0Entities.get(1).getAccountId();

        if(amount1>=money)
        {
            accountDeal0Entities.get(0).setAmount(amount1-money);
            accountDeal0Entities.get(0).setTime(System.currentTimeMillis());

            accountDeal0Entities.get(1).setAmount(amount2+money);
            accountDeal0Entities.get(1).setTime(System.currentTimeMillis());
        }
        else
        {
            System.out.println("当前账户余额不足");
        }
        return accountDeal0Entities;
    }
}
