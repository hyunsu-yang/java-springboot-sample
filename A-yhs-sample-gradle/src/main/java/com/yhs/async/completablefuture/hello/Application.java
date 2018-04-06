package com.yhs.async.completablefuture.hello;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/*
 * 스프링에서 @Async로 비동기처리하기
 * 자바 설정Java configuration으로 비동기 처리enabling asynchronous processing를 쓰려면 간단히 설정 클래스에 @EnableAsync를 추가해주기만 하면 된다
	annotation : 
	기본값, @EnableAsync은 스프링의 Async 어노테이션을 감지하며 EJB 3.1javax.ejb.Asynchronous; 이 옵션으로 사용자 정의된 다른 어노테이션 또한 감지할 수 있다.
	mode : 
	사용해야할 advice의 타입을 가르킨다 - JDK proxy 기반 또는 AspectJ weaving.
	proxyTargetClass 
	: 사용해야할 proxy의 타입을 가르킨다 - CGLIB 또는 JDK; 이 속성값은 오직 mode가 AdviceMode.PROXY 로 설정되어 있을때만 유효하다.
	order 
	: sets the order in which AsyncAnnotationBeanPostProcessor 가 적용해야할 순서를 설정한다; 단지 모든 현존의 프록시를 고려하기 위해 기본값으로 마지막부터 실행된다.
 */

@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        // close the application context to shut down the custom ExecutorService
        SpringApplication.run(Application.class, args).close();
    }

    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("GithubLookup-");
        executor.initialize();
        return executor;
    }
    

}