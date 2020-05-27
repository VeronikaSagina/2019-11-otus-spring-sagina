package com.example.hello.service;

import com.example.hello.feign.RandomProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private final RandomProxy randomProxy;

    public HelloService(RandomProxy randomProxy) {
        this.randomProxy = randomProxy;
    }

    @HystrixCommand(fallbackMethod = "getDefaultPhrase")
    public String getPhrase(String name) {
        int random = randomProxy.generate(3);
        switch (random) {
            case 0:
                return "Как нога, " + name + "?";
            case 1:
                return "Awe, " + name + "!";
            case 2:
                return "Здоровеньки булы, " + name + "!";
        }
        throw new UnsupportedOperationException("unknown value: " + random);
    }

    public String getDefaultPhrase(String name) {
        return "Hello, " + name + "!";
    }
}
