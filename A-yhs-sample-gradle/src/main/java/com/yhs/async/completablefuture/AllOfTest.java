package com.yhs.async.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.junit.Test;

/*
 * 동시에 n개의 요청을 호출하고 모든 호출이 완성되면 진행하기 
 * 동시에 3개의 Rest 요청을 보내고 3개의 요청이 완료되면 Callback 을 진행하기 위한 예제
	CompletableFuture.allOf 를 사용한다.
	아래 예는 5초 뒤에 "Completed!!" 를 반환하는 buildMessage 라는 메서드를 동시에 3개를 호출하고 3개가 완료되었을때 그 결과를 마지막 thenAcceptAsync 에서 모두 모아서 처리를 한다.
	선후 관계가 없는 데이터를 동시에 조회 할때, 적절히 사용할 수 있다.
 */
public class AllOfTest {

    private String buildMessage() {

        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Completed!!";
    }

    @Test
    public void allOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(this::buildMessage);
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(this::buildMessage);

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture
            .allOf(completableFutures.toArray(new CompletableFuture[3]))
          .thenApplyAsync(result ->
                                completableFutures
                                    .stream()
                                    .map(a -> a.join())
                                    .collect(Collectors.toList())
            )
            .thenAcceptAsync(messages -> messages.forEach(message -> System.out.println(message)));

        Thread.sleep(7 * 1000L);
    }
}