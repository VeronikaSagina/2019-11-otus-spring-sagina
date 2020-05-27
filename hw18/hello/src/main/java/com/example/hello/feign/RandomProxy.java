package com.example.hello.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "random-client", url = "http://localhost:8090")
@RequestMapping("/random")
public interface RandomProxy {

    @PostMapping(value = "/{max:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    int generate(@PathVariable("max") int max);
}
