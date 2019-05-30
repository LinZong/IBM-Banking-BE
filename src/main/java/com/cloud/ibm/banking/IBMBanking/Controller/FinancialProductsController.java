package com.cloud.ibm.banking.IBMBanking.Controller;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.FinancialProdResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.FinancialResponse;
import com.cloud.ibm.banking.IBMBanking.Service.FinancialProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class FinancialProductsController
{
    @Autowired
    private FinancialProductsService financialProductsService;

    @PostMapping("/financialPd")
    public @ResponseBody
    CommonResponse BuyFinancialProducts(@RequestBody Map<String, String> params)
    {
        CommonResponse resp = financialProductsService.financialProducts(Integer.parseInt(params.get("account_id")),
                Double.parseDouble(params.get("money")), Integer.parseInt(params.get("bucket")), Integer.parseInt(params.get("product_id")));
        return resp;
    }

    @PostMapping("/myfinancial")
    public @ResponseBody
    FinancialResponse QueryMyFinancialProducts(@RequestBody Map<String, String> params)
    {
        int status = Integer.parseInt(params.get("status"));
        int AccountID = Integer.parseInt(params.get("account_id"));
        int bucket = Integer.parseInt(params.get("bucket"));
        Long begin = params.getOrDefault("timebegin", "").equals("") ? null : Long.parseLong(params.get("timebegin"));
        Long end = params.getOrDefault("timeend", "").equals("") ? null : Long.parseLong(params.get("timeend"));
        return new FinancialResponse(1000,financialProductsService.QueryMyFinancialProduct(status, AccountID, bucket, begin, end));

    }

    @PostMapping("/withdrawfinancial")
    public @ResponseBody
    CommonResponse WithdrawFinancialProduct(@RequestBody Map<String, String> params)
    {
        return financialProductsService.WithdrawProduct(Integer.parseInt(
                params.get("buy_record")),
                Double.parseDouble(params.get("withdrawbalance")),
                Integer.parseInt(params.get("bucket")));

    }

    @RequestMapping("/allproduct")
    public @ResponseBody FinancialProdResponse GetAllFinancialProducts()
    {
        return new FinancialProdResponse(1000,financialProductsService.GetAllFinancialProducts());
    }
}
