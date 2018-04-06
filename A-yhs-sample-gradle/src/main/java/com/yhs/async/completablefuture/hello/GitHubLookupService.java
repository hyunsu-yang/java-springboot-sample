package com.yhs.async.completablefuture.hello;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/*
 *@Async 는 두가지 제약사항
	1. public 메소드에만 적용해야한다
	2. 셀프 호출 self invocation – 같은 클래스안에서 async 메소드를 호출 – 은 작동하지않음
	(이유는 간단한데 메소드가 public이어야 프록시가 될수 있기 때문이고 셀프호출은 프록시를 우회하고 해당 메소드를 직접 호출하기때문에 작동하지않는 것)


*/

@Service
public class GitHubLookupService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubLookupService.class);

    private final RestTemplate restTemplate;

    public GitHubLookupService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        
       // Future<String> test =  new AsyncResult<String>("tset");
        
       return CompletableFuture.completedFuture(results);
    }

}
