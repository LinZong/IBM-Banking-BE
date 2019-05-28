package com.cloud.ibm.banking.IBMBanking.Service;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
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

    public CommonResponse modifyPayingPasswordService(int payingPassword, int bucket, int id)
    {
        int count = accountDao.modifyPayingPassword(payingPassword,bucket,id);
        return new CommonResponse(count == 1 ? 1001 : 1000);
    }

}
