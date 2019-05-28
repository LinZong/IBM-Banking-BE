package com.cloud.ibm.banking.IBMBanking.Service;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyPasswordService {
    @Autowired
    private AccountDaoImpl accountDao;

    public CommonResponse modifyPasswordService(String password, int bucket, int id)
    {
        int count = accountDao.modifyPassword(password,bucket,id);
        return new CommonResponse(count == 1 ? 1001 : 1000);
    }

    public CommonResponse oldPasswordService(String oldPassword, int bucket, int id)
    {
       String password = accountDao.oldPassword(bucket,id);
        return new CommonResponse(password.equals(oldPassword) ? 1001 : 1000);
    }

    public CommonResponse modifyPayingPasswordService(int payingPassword, int bucket, int id)
    {
        int count = accountDao.modifyPayingPassword(payingPassword,bucket,id);
        return new CommonResponse(count == 1 ? 1001 : 1000);
    }

    public CommonResponse oldPayingPasswordService(int oldPayingPassword, int bucket, int id)
    {
        int payingPassword = accountDao.oldPayingPassword(bucket,id);
        return new CommonResponse(payingPassword==oldPayingPassword ? 1001 : 1000);
    }
}
