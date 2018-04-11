package com.yhs.async.completablefuture;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Test;

/*
동시에 n개의 요청을 호출하고 하나라도 호출이 완성되면 진행하기

아래 예제는 anyOf 메서드를 사용해서 3개의 요청 중에 하나라도 끝나면 다음으로 넘어가도록 구현되어 있다.
thenAcceptAsync 메서드에서는 3개 의 가장 먼저 끝나는 결과를 콘솔에 찍도록 되어 있다.
위의 AllOf 와 달리 3개중에 1개의 결과만 표시된다.
*/
public class AnyOfTest {

    private String buildMessage(int index) {

        try {
            Thread.sleep(2 * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Completed!! [" + index + "]";
    }

    @Test
    public void allOfTest() throws Exception {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> buildMessage(1));
        CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> buildMessage(2));
        CompletableFuture<String> cf3 = CompletableFuture.supplyAsync(() -> buildMessage(3));

        List<CompletableFuture<String>> completableFutures = Arrays.asList(cf1, cf2, cf3);

        CompletableFuture
            .anyOf(completableFutures.toArray(new CompletableFuture[3]))
            .thenAcceptAsync(result -> System.out.println(result));

        Thread.sleep(7 * 1000L);
    }
}