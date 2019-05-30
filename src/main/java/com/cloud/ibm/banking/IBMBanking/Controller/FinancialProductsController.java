package com.cloud.ibm.banking.IBMBanking.Controller;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Service.FinancialProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class FinancialProductsController {
    @Autowired
    private FinancialProductsService financialProductsService;

    @PostMapping("/financialPd")
    public @ResponseBody
    CommonResponse financialProducts(@RequestBody Map<String,String> params)
    {
        CommonResponse resp = financialProductsService.financialProducts(Integer.parseInt(params.get("account_id")),
              Double.parseDouble(params.get("money")),Integer.parseInt(params.get("bucket")));
        return resp;
    }
}
