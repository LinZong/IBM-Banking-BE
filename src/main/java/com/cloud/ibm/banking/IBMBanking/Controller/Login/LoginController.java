package com.cloud.ibm.banking.IBMBanking.Controller.Login;


import com.cloud.ibm.banking.IBMBanking.Model.LoginResponse;
import com.cloud.ibm.banking.IBMBanking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class LoginController
{

    @Autowired
    private AccountService accountService;


    @PostMapping("/login")
    public @ResponseBody LoginResponse Login(@RequestBody Map<String,String> params)
    {
        LoginResponse resp = accountService.Login(params.get("identity"),params.get("password"));
        return resp;
    }
}
