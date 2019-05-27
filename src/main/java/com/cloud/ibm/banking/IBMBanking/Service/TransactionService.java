package com.cloud.ibm.banking.IBMBanking.Service;

import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public CommonResponse withdrawMoney(double money, int id, int bucket, int payingPassword) {

        // 先查询这个人的信息，比对支付密码和剩余钱数

        AccountInformation0Entity user = accountDao.queryWithdrawAccount(id,bucket);
        if(user != null) {
            float currBalance = user.getBalance();
            int payingPw = user.getPayingPassword();
            if (payingPw != payingPassword) {
                // TODO 告知支付密码不正确
                return new CommonResponse(1101);

            }
            else if(currBalance < money) {
                // TODO 告知账户余额不足
                return new CommonResponse(1102);
            }
            else {
                int effect = accountDao.withdraw_money(money,id, bucket);
                return new CommonResponse(effect == 1 ? 1100 : 1103);
            }
        }
        else {
            // TODO 返回账号不存在
            return new CommonResponse(1104);
        }
    }

    public CommonResponse transfer(double money, int id, int bucket, int payingPassword, int otherId) {
        int user = accountDao.transfer_money(money, id, bucket, payingPassword, otherId);
        return new CommonResponse(user);
    }
}
