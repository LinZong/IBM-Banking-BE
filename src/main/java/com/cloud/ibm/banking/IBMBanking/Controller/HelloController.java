package com.cloud.ibm.banking.IBMBanking.Controller;

import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@EnableAutoConfiguration

public class HelloController {


    @Autowired
    private AccountDaoImpl accountDao;

    @RequestMapping("/save")
    public @ResponseBody String index() {

        accountDao.CreateUser();

        return "已保存!";
    }


    @PostMapping("/all")
    public @ResponseBody AccountInformation0Entity GetOne(@RequestBody Map<String,String> params) {

        AccountInformation0Entity result = accountDao.GetUserByIdentity(params.get("identity"),params.get("password"));

        if(result != null) {
            return result;
        }
        return null;
    }
}
