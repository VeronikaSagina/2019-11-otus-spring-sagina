package com.example.random.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@Api(tags = {"RandomController"})
@RestController
@RequestMapping
public class RandomController {

    private static final Random random = new Random();

    @ApiOperation(value = "put random value")
    @PostMapping(value = "/random/{max:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    public int generate(@PathVariable("max") int max) {
        return random.nextInt(max);
    }
}
