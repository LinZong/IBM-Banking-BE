package com.cloud.ibm.banking.IBMBanking.Model.Response;

import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal1Entity;

import java.util.List;

public class pipeLineResponse extends CommonResponse{
   private List<AccountDeal1Entity> accountDeal1Entity;

   public pipeLineResponse(int statusCode,List<AccountDeal1Entity> accountDeal1Entity)
   {
       super(statusCode);
       this.accountDeal1Entity=accountDeal1Entity;
   }

    public void setAccountDeal1Entity(List<AccountDeal1Entity> accountDeal1Entity) {
        this.accountDeal1Entity = accountDeal1Entity;
    }

    public List<AccountDeal1Entity> getAccountDeal1Entity() {
        return accountDeal1Entity;
    }
}
