package com.example.hello.web;

import com.example.hello.service.HelloService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"HelloController"})
@RestController
@RequestMapping
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @ApiOperation(value = "say hello")
    @GetMapping("/hello/{name}")
    public String getPhrase(@PathVariable("name") String name) {
        return helloService.getPhrase(name);
    }

}
