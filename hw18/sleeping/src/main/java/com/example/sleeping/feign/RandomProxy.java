package com.example.sleeping.feign;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(
        name = "random-client",
        url = "http://localhost:8090",
        fallbackFactory = RandomProxy.RandomProxyFallbackFactory.class,
        decode404 = true
)
@RequestMapping
public interface RandomProxy {

    @PostMapping(value = "/random/{max:\\d+}", produces = MediaType.APPLICATION_JSON_VALUE)
    int generate(@PathVariable("max") int max);

    @Component
    class RandomProxyFallbackFactory implements FallbackFactory<RandomProxy> {
        private static final Logger log = LoggerFactory.getLogger(RandomProxyFallbackFactory.class);
        @Override
        public RandomProxy create(Throwable throwable) {
            return max -> {
                log.error("Exception occurred", throwable);
                return max / 2;
            };
        }
    }
}
