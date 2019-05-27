package com.cloud.ibm.banking.IBMBanking.Service;

import com.cloud.ibm.banking.IBMBanking.Model.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Model.LoginResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class TransactionService {
    @Autowired
    private AccountDaoImpl accountDao;

    public CommonResponse saveMoney(double money, int id, int buckey) {
        WithBucket<AccountInformation0Entity> user = accountDao.save_money(money, id, buckey);
        if (user != null) {
            return new CommonResponse(1001);
        }
        return new CommonResponse(1000);
    }

    public CommonResponse withdrawMoney(double money, int id, int buckey, int payingPassword) {
        int user = accountDao.withdraw_money(money, id, buckey, payingPassword);
        return new CommonResponse(user);
    }

    public CommonResponse transfer(double money, int id, int buckey, int payingPassword, int otherId) {
        int user = accountDao.transfer_money(money, id, buckey, payingPassword, otherId);
        return new CommonResponse(user);
    }
}
