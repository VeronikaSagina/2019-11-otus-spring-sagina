package com.example.sleeping.service;

import com.example.sleeping.feign.RandomProxy;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SleepingService {

    private static final Logger log = LoggerFactory.getLogger(SleepingService.class);

    private final RandomProxy randomProxy;

    public SleepingService(RandomProxy randomProxy) {
        this.randomProxy = randomProxy;
    }

    @HystrixCommand(commandProperties= {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value="5000")
    },
            fallbackMethod = "sleepFallback"
    )
    public void sleep(int seconds) {
        log.info("enter sleep method, seconds: " + seconds);
        try {
            int timeout = randomProxy.generate(seconds);
            log.info("seconds to sleep: " + timeout);
            TimeUnit.SECONDS.sleep(timeout);
            log.info("slept successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    public void sleepFallback(int seconds) {
        log.info("fallback, seconds: " + seconds);
    }
}
