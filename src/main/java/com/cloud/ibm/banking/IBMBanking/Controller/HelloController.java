package com.cloud.ibm.banking.IBMBanking.Controller;

import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration

public class HelloController {

    @Autowired
    private AccountDaoImpl accountDao;

    @RequestMapping("/save")
    public @ResponseBody String index() {

//        Account account = new Account();
//        account.setAccountName("郑泽屁明");
//        account.setBalance(100f);
//        account.setManageType(1);
//        account.setPassword("666666");
//
//        accountDao.repository.save(account);

        return "已保存!";
    }


    @RequestMapping("/all")
    public @ResponseBody  String GetOne() {
        accountDao.findAccountFromAllTable(3,0,9);

        return "没找到!";
    }
}
