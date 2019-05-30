package com.cloud.ibm.banking.IBMBanking.Service;

import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.LoginResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.pipeLineResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal1Entity;
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

    public CommonResponse saveMoney(double money, int id, int bucket) {
        int count = accountDao.save_money(money, id, bucket);
        return new CommonResponse(count == 2 ? 1001 : 1000);
    }

    public pipeLineResponse queryPipeLine(int id,int bucket,String timeBegin,String timeEnd) {
        List<AccountDeal1Entity> pipeLine = accountDao.pipeLine(id,bucket,timeBegin,timeEnd);
        if(pipeLine!=null)
        {
            return new pipeLineResponse(1001,pipeLine);
        }
        return new pipeLineResponse(1000,null);
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
                return new CommonResponse(effect == 2 ? 1100 : 1103);
            }
        }
        else {
            // TODO 返回账号不存在
            return new CommonResponse(1104);
        }
    }

    public CommonResponse transfer(double money, int id, int bucket, int payingPassword, String otherIdentity) {
        // 先查询转账账户在不在，再查询这个人的信息，之后比对支付密码和剩余钱数
        WithBucket<AccountInformation0Entity> otherUser=accountDao.GetUserInfoByIdentity(otherIdentity);
        if(otherUser==null)
        {
            return new CommonResponse(1105);
        }
        else {
            int otherId=otherUser.getResult().getId();
            AccountInformation0Entity user = accountDao.queryWithdrawAccount(id,bucket);
            if (user != null) {
                float currBalance = user.getBalance();
                int payingPw = user.getPayingPassword();
                if (payingPw != payingPassword) {
                    // TODO 告知支付密码不正确
                    return new CommonResponse(1101);

                } else if (currBalance < money) {
                    // TODO 告知账户余额不足
                    return new CommonResponse(1102);
                } else {
                    int effect = accountDao.transfer_money(money, id, bucket,otherId,otherIdentity);
                    return new CommonResponse(effect == 4 ? 1100 : 1103);//TODO 1103数据库写入失败
                }
            } else {
                // TODO 返回账号不存在
                return new CommonResponse(1104);
            }
        }
    }
}
