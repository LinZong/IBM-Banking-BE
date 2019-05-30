package com.cloud.ibm.banking.IBMBanking.Service;


import com.cloud.ibm.banking.IBMBanking.Model.FinancialProductStrategy;
import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.FinancialDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountFinancialProduct0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.FinancialProductEntity;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class FinancialProductsService {
    @Autowired
    private AccountDaoImpl accountDao;

    @Autowired
    private FinancialDaoImpl financialDao;

    private  static HashMap<Integer, FinancialProductStrategy> strategyHashMap;

    public  void PrepareStrategy()
    {
        List<FinancialProductEntity> products = financialDao.AllFinancialProducts();
        Gson gson = new Gson();
        for (FinancialProductEntity product : products)
        {
            strategyHashMap.put(product.getId(),gson.fromJson(product.getStrategy(),FinancialProductStrategy.class));
        }
    }

    public synchronized HashMap<Integer, FinancialProductStrategy> GetStrategyHashMap() {
        if(strategyHashMap == null) {
            strategyHashMap = new HashMap<>();
            PrepareStrategy();
        }
        return strategyHashMap;
    }

    public CommonResponse financialProducts(int account_id, double money, int bucket, int product_id)
    {
         boolean success = financialDao.BuyProduct(product_id,money,bucket,account_id,product_id == 1);

         return new CommonResponse(success ? 1001 : 1000);
    }

    public List<AccountFinancialProduct0Entity>
                QueryMyFinancialProduct(int status,int AccountID,int bucket,Long begin,Long end)
    {
        return financialDao.QueryOwnProduct(status,AccountID,bucket,begin,end);

    }

    public CommonResponse WithdrawProduct(int BuyRecordID,double WithdrawBalance,int Bucket)
    {
        FinancialDaoImpl.WithdrawStatus status = financialDao.WithdrawOwnProduct(Bucket,BuyRecordID,WithdrawBalance);
        switch (status)
        {
            case SUCCESSFUL:
                return new CommonResponse(1000);
            case NOT_ENOUGH_BALANCE:
                return new CommonResponse(1001);
            case NO_RECORD:
                return new CommonResponse(1002);
            case HAVE_WITHDRAWED:
                return new CommonResponse(1003);
            case WITHDRAW_OP_DB_FAILED:
                return new CommonResponse(1004);
            case SAVEMONEY_OP_DF_FAILED:
                return new CommonResponse(1005);
        }
        return new CommonResponse(1006);
    }

    public List<FinancialProductEntity> GetAllFinancialProducts()
    {
        return financialDao.AllFinancialProducts();
    }
}
