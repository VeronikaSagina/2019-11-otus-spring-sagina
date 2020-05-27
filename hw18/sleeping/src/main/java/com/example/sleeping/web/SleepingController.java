package com.example.sleeping.web;

import com.example.sleeping.service.SleepingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"SleepingController"})
@RestController
@RequestMapping("/sleep")
public class SleepingController {

    private final SleepingService sleepingService;

    public SleepingController(SleepingService sleepingService) {
        this.sleepingService = sleepingService;
    }

    @ApiOperation(value = "sleep")
    @PostMapping(value = "/{seconds:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void sleep(@PathVariable("seconds") int seconds) {
        sleepingService.sleep(seconds);
    }
}
