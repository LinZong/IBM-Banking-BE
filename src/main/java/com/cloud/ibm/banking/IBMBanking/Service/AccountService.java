package com.cloud.ibm.banking.IBMBanking.Service;


import com.cloud.ibm.banking.IBMBanking.Model.Request.RegisterModel;
import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.LoginResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.CustomerInformation0Entity;
import com.cloud.ibm.banking.IBMBanking.Persistence.SplitTableStrategy.WithBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*
*   This is a very simple account login service.
*   It can accept identity (email || phone) and password from front-end
*   then contacts to account DAO to fetch account info.
*
*   if it can get an account detailed by identity and password
*   we can regard this login is successful.
*
*   So we can fetch it's customer info from another table with the same
*   bucket num and item id.
*
*
*   NOTE :
*
*   1. When registering an account, should make sure that
*   account_information and customer_information
*   are all share the same bucket num. Thought that can search cust_info
*   more effectively.
*
*   2.  Should add more logic to sync login status with front-end.
*
*   3. Provide another api to let user change its customer_information.
*
*   IMPORTANT :
*
*   1. Define a series of status code to tell front-end what happened.
*       For example, a LoginResponse with statuscode 1000 indicates
*       `Identity or password is wrong`, and 1001 is `Login Successfully!`.
*
*       All API Need StatusCode!
*
*       feel free to inherit base response bean `CommonResponse` and constructs
*       a more detailed response body.
*
* */

@Component
public class AccountService
{
    @Autowired
    private AccountDaoImpl accountDao;


    public LoginResponse Login(String identity, String password)
    {
         WithBucket<AccountInformation0Entity> user = accountDao.GetUserByIdentity(identity, password);
         if(user!=null)
         {
             int bucket = user.getBucketNum();
             int id = user.getResult().getId();

             CustomerInformation0Entity cust = accountDao.GetCustomerInformation(bucket,id);
             return new LoginResponse(1001,cust,bucket,user.getResult().getIdentity());
         }
         return new LoginResponse(1000,null,-1,null);
    }

    public static int IdentityBucketIndex(String identity)
    {
        long total = 0;
        int identityLength = identity.length();
        for (int i = 0; i < identityLength; i++) {
            total += identity.charAt(i);
        }

        return (int) (total % 3);
    }


    public CommonResponse Register(RegisterModel register)
    {
        if(register.ValidModel())
        {
            int bucket = IdentityBucketIndex(register.getIdentity());
            boolean FreshAccount = accountDao.IsAccountRegistered(register.getIdentity(),bucket);
            if(FreshAccount)
            {
                // 执行写库逻辑
                boolean OK = accountDao.RegisterAccount(register,bucket);
                return new CommonResponse(OK ? 1302 : 1301);
            }
            return new CommonResponse(1303);
        }
        return new CommonResponse(1300); // 告知注册信息不合法
    }

    public CommonResponse DetectMultiRegister(String identity)
    {
        int bucket = IdentityBucketIndex(identity);
        boolean result = accountDao.IsAccountRegistered(identity,bucket);
        return new CommonResponse(result ? 1201 : 1200);
    }
}


