package com.cloud.ibm.banking.IBMBanking.Service;

import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModifyUserInfoService {
    @Autowired
    private AccountDaoImpl accountDao;

    public CommonResponse modifyUserInfoService(String name,String address,String telNum,String email,int bucket,int id)
    {
        int count = accountDao.modifyUserInfo(name,address,telNum,email,bucket,id);
        return new CommonResponse(count == 1 ? 1001 : 1000);
    }
}
