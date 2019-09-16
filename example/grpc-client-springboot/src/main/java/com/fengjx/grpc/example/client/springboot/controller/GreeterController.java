
package com.fengjx.grpc.example.client.springboot.controller;

import com.fengjx.grpc.example.client.springboot.grpc.service.GreeterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author fengjianxin
 */
@RestController
public class GreeterController {

    @Resource
    private GreeterService greeterService;


    @RequestMapping("/say-hello")
    public String sayHello(String name) {
        return greeterService.sayHello(name);
    }


}
