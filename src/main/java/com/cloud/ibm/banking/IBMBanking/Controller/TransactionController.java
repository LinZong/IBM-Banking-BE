package com.cloud.ibm.banking.IBMBanking.Controller;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;

import com.cloud.ibm.banking.IBMBanking.Model.Response.DealResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.FinancialResponse;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountDeal1Entity;
import com.cloud.ibm.banking.IBMBanking.Service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/mydeal")
    public @ResponseBody DealResponse QueryMyDeal(@RequestBody Map<String,String> params)
    {
        int id = Integer.parseInt(params.get("account_id"));
        int bucket = Integer.parseInt(params.get("bucket"));
        Long begin = params.getOrDefault("timebegin", "").equals("") ? null : Long.parseLong(params.get("timebegin"));
        Long end = params.getOrDefault("timeend", "").equals("") ? null : Long.parseLong(params.get("timeend"));

        return transactionService.QueryMyDeal(begin,end,id,bucket);

    }

    @PostMapping("/save")
    public @ResponseBody
    CommonResponse saveMoney(@RequestBody Map<String,String> params)
    {
       CommonResponse resp = transactionService.saveMoney(Double.parseDouble(params.get("money")),
              Integer.parseInt(params.get("id")),Integer.parseInt(params.get("bucket")));
        return resp;
    }

    @PostMapping("/withdraw")
    public @ResponseBody
    CommonResponse withdrawMoney(@RequestBody Map<String,String> params)
    {
        CommonResponse resp = transactionService.withdrawMoney(Double.parseDouble(params.get("money")),
                Integer.parseInt(params.get("id")),Integer.parseInt(params.get("bucket")),Integer.parseInt(params.get("payingPassword")));
        return resp;
    }

    @PostMapping("/transfer")
    public @ResponseBody
    CommonResponse transfer(@RequestBody Map<String,String> params)
    {

        CommonResponse resp = transactionService.transfer(Double.parseDouble(params.get("money")),
                Integer.parseInt(params.get("id")),Integer.parseInt(params.get("bucket")),
                Integer.parseInt(params.get("payingPassword")),
               params.get("otherIdentity"));
        return resp;
    }

    @PostMapping("/pipeLine")
    public @ResponseBody
    CommonResponse QueryPipeLine(@RequestBody Map<String,String> params)
    {
        CommonResponse resp = transactionService.queryPipeLine(Integer.parseInt(params.get("id")),
                Integer.parseInt(params.get("bucket")),
                params.get("time1"), params.get("time2"));
        return resp;
    }
}
