package com.yhs.async.threadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Resource
    GreetingService greetingService;

    private static final String template = "Hello, %s!";
    // atomic (CAS : compare and swap) == sycronized  같은 기능
    private final AtomicLong counter = new AtomicLong();

    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {

        logger.info("Hello {}", name);
        greetingService.method1();
        greetingService.method2();
        greetingService.method3();

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}
