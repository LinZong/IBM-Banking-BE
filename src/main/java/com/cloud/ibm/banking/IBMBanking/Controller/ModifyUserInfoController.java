package com.cloud.ibm.banking.IBMBanking.Controller;


import com.cloud.ibm.banking.IBMBanking.Model.Response.CommonResponse;
import com.cloud.ibm.banking.IBMBanking.Service.ModifyUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@EnableAutoConfiguration
public class ModifyUserInfoController {
    @Autowired
    private ModifyUserInfoService modifyUserInfoService;

    @PostMapping("/modifyUserInfo")
    public @ResponseBody
    CommonResponse saveMoney(@RequestBody Map<String,String> params)
    {
        CommonResponse resp = modifyUserInfoService.modifyUserInfoService(params.get("name"),
                params.get("address"),params.get("telNum"),params.get("email"),Integer.parseInt(params.get("bucket")),
                Integer.parseInt(params.get("id")));
        return resp;
    }
}
