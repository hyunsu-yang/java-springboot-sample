package com.yhs.async.completablefuture;

import java.util.concurrent.CompletableFuture;

import org.junit.Test;

/**
 * java 에서 제공하던 Future interface 가 제공한 Async 이지만 get() 메서드로 Blocking 을 유발하는 단점
 * Spring Framework에서 제공하는 LinstenableFuture 로 해소 할 수 있었다.  (Spring Framework를 사용하는 개발자라면 RestTemplated 의 Async 버젼인 AsyncRestTemplate에 대해 관심을 가져 보기 바란다.)
 * 하지만 ListenableFuture를 써 본 개발자라면 한번쯤은 Callback 작성의 복잡함에 고민해 왔을 것이다. 심지어 Callback Hell 이라는 말이 커뮤니티에는 많이 돌고 
 * 그 불편함을 알았던지 Java 8 에서 드디어 CompletableFuture 를 제공한다. CompletableFuture는 아래와 같은 패턴으로 개발이 가능하여 비동기 서비스 코드의 가독성이 좋아졌다.
 */
public class completablefutureTest {
	 Runnable task = () -> {
	        try {
	            Thread.sleep(5 * 1000L);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println("TASK completed");
	    };

    @Test
    public void completableFuture() throws Exception {

        CompletableFuture
            .runAsync(task)
            .thenCompose(aVoid1 -> CompletableFuture.runAsync(task))
            .thenAcceptAsync(aVoid2 -> System.out.println("all tasks completed!!"))
            .exceptionally(throwable -> {
                System.out.println("exception occurred!!");
                return null;
            });

        Thread.sleep(11 * 1000L);
    }
	    
	    
    
    // 비동기처리 Return 값을 다음 처리의 Parameter 로 사용할때 사용한다
    //CompletableFuture 를 반환하는 Method를 Chain으로 실행하고 싶을때
    //즉 이전에 Async 프로세스로 응답 받은 값을 다음 Async 프로세스의 인자로 사용하는 경우에 아래와 같이 thenCompose, thenComposeAsync 를 사용할 수 있다.
    @Test
    public void thenComposeTest() throws Exception {
        Price price = new Price();
        price.getPriceAsync(1)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(price::getPriceAsync)
                .thenComposeAsync(r -> price.getPriceAsync(r));

        System.out.println("Non Blocking!!");

        // main thread 가 죽으면 child 도 다 죽어 버려서 대기함.
        Thread.sleep(3000l);

    }
    
    // 두가지 프로세스를 parallel 하게 동시에 진행하고 결과 값을 조합한 처리를 할때.
    // 두개의 비동기 요청을 동시에 진행해서 조합 할 수 있다.
    @Test
    public void thenCombineTest() throws Exception {
        Price price = new Price();
        CompletableFuture<Double> price1 = price.getPriceAsync(1);
        CompletableFuture<Double> price2 = price.getPriceAsync(2);
        price2.thenCombineAsync(price1, (a, b) -> a+b )
                .thenAcceptAsync(System.out::print);

        System.out.println("Non Blocking!!");

        // main thread 가 죽으면 child 도 다 죽어 버려서 대기함.
        Thread.sleep(5000l);
    }
    
	    
	class Price {	
	    public double getPrice (double oldprice) throws Exception{
	        return calculatePrice(oldprice);
	    }
	
	    public double calculatePrice(double oldprice) throws Exception {
	
	        System.out.println("Input :" + oldprice);
	        Thread.sleep(1000l);
	        System.out.println("Output :" + (oldprice + 1l));
	
	        return oldprice + 1l;
	    }
	
	    public CompletableFuture<Double> getPriceAsync(double oldPrice) {
	        CompletableFuture<Double> completableFuture = new CompletableFuture<>();
	        new Thread(() -> {
	            try {
	                double price = calculatePrice(oldPrice);
	                completableFuture.complete(price);
	            } catch (Exception ex) {
	                completableFuture.completeExceptionally(ex);
	            }
	        }).start();
	
	        return completableFuture;
	    }
	}
}
	
/*
 * 	exceptionally
		지금까지 실행된 completablefuture에서 발생한 Throwable 을 처리할 수 있다.
		ListenableFuture 등에서 Callback.onFailure 의 역할을 대신할 수 있다.
		기존 ListenableFuture 는 각 요청별로 실패에 대한 Callback 을 설정한 반면... CompletableFuture는 모든 Exception 을 통합적으로 처리할 수 있다.
		
		thenApply(Async)
			ListenableFuture 에서는 Callback 을 설정하는 것과 동일한 작업이다.
			thenApply or thenApplyAsync 는 Function 을 인자로 받기 때문에 다음 후속작업에 결과값을 return 해 줄 수 있다.
			
		thenAccept(Async)
			ListenableFuture 에서는 Callback 을 설정하는 것과 동일한 작업이지만 Consumer 인터페이스가 인자이기 때문에 후속 작업에 결과값을 return 할 수 없다.
 */
