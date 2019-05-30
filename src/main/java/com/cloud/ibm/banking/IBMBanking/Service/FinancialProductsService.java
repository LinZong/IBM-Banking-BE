package com.cloud.ibm.banking.IBMBanking.Service;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinancialProductsService {
    @Autowired
    private AccountDaoImpl accountDao;

    public CommonResponse financialProducts(int account_id,double money,int bucket)
    {
        int count = accountDao.buyProduct1( account_id,money, bucket);
        if(count==1)
            return new CommonResponse(1001);
        else if(count==2)
            return new CommonResponse(1002);
        else
            return new CommonResponse(1000);
    }
}
