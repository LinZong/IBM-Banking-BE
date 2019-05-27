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


/*
*  A simple workflow of backend-api. Demonstrate the login api above.
*
*  Prepare:(Auto Injected accountService with @Autowired annotation) -->
*       LoginController -> MapUrl (/login) -> Route to service layer (AccountService)
*       -> call Login method.
*
*       AccountService :
*           Call AccountDAO to fetch db with conditions identity and password
*           judge correction by whether there are records returned?
*
*               -> if returned -> fetch customer_information
*                       -> return to controller
*                           -> return to front-end.
*
*                -> not found -> return to front-end
*                                   -> failed.
*
*
*
*        Controller : Route request to specified service layer.
*
*        Service : Handle request, do logic, combine results.
*
*        DAO (Database Access Object) : operates db logic here.
*
*
*
* */