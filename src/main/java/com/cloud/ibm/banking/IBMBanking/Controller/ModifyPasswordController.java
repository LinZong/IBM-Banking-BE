package com.cloud.ibm.banking.IBMBanking.Controller;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Service.ModifyPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class ModifyPasswordController {
    @Autowired
    private ModifyPasswordService modifyPasswordService;

    @PostMapping("/modifyPassword")
    public @ResponseBody
    CommonResponse ModifyPassword(@RequestBody Map<String, String> params) {
        CommonResponse resp = modifyPasswordService.modifyPasswordService(params.get("password"),
                Integer.parseInt(params.get("bucket")), Integer.parseInt(params.get("id")));
        return resp;
    }

    @PostMapping("/modifyPayingPassword")
    public @ResponseBody
    CommonResponse ModifyPayingPassword(@RequestBody Map<String, String> params) {
        CommonResponse resp = modifyPasswordService.modifyPayingPasswordService(Integer.parseInt(params.get("payingPassword")) ,
                Integer.parseInt(params.get("bucket")), Integer.parseInt(params.get("id")));
        return resp;
    }
}

