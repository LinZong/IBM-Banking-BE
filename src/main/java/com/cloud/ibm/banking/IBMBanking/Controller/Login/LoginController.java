package com.cloud.ibm.banking.IBMBanking.Controller.Login;


import com.cloud.ibm.banking.IBMBanking.Model.Request.RegisterModel;
import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Model.Response.LoginResponse;
import com.cloud.ibm.banking.IBMBanking.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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
        return accountService.Login(params.get("identity"),params.get("password"));
    }

    @PostMapping("/register")
    public @ResponseBody CommonResponse Register(@RequestBody RegisterModel register)
    {
        return accountService.Register(register);
    }

    @RequestMapping("/checkregister")
    public @ResponseBody CommonResponse IfAccountRegistered(@RequestParam(name = "identity") String identity)
    {
        return accountService.DetectMultiRegister(identity);
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