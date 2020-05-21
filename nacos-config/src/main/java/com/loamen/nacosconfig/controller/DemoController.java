package com.loamen.nacosconfig.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("config")
public class DemoController {
    @NacosValue(value = "${server.name:1}", autoRefreshed = true)
    private String serverName;

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public String get() {
        return serverName;
    }
}
