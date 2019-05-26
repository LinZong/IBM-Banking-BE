package com.cloud.ibm.banking.IBMBanking.Controller;

import com.cloud.ibm.banking.IBMBanking.Persistence.DAO.AccountDaoImpl;
import com.cloud.ibm.banking.IBMBanking.Persistence.Entity.AccountInformation0Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class LogController {
    @Autowired
    AccountDaoImpl accountDao;
    AccountInformation0Entity information0Entity;

    @RequestMapping("/log")
    public @ResponseBody int logAccount(String identity, String password) {
        information0Entity = accountDao.GetUserByIdentity(identity, password);
        return information0Entity.getId();
    }
}
