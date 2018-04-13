package com.yhs.async.completablefuture;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/*
 * CompletableFuture 실행 방법
 1. Executor or Thread 로 로직을 실행하고 complete 로 결과를 넘겨주는 방법
 2. boilerplate 코드 없이 바로 CompletableFuture 에 연산 로직을 넣어주는 방법 (runAsync, supplyAsync)
 */
public class CompletableFutureHowToCreateTest {

	@Test
	public void test() throws Exception {
		Future<String> test1 = calculateAsync();
		System.out.println("test1: " +test1.get());
		
		Future<String> test2 = calculateAsync2();		
		System.out.println("test1: " + test1.get() + " test2: " + test2.get());
	}
	
	
	/*
	 CompletableFuture 을 인자가 없는 생성자로 만든다. 
	 그 뒤 이 참조를 Consumer 에게 넘긴다. (여기서 말하는 Consumer 는 단순히 Future 를 참조를 가지며 Future 로 부터 넘어오는 값을 받는 쪽을 말한다.) 
	 그리고 Future 의 complete 메소드를 호출하며 Consumer 쪽으로 값을 전달할 수 있다. 
	 Consumer 는 get 메소드를 호출하여 이 값을 전달 받는다. 이 때 get 메소드가 호출되면 값을 받을 때까지 호출한 Thread 는 Blocking 이 된다. 
	 
	 여기 caculateAsync 메소드를 호출하면 바로 Future 구현체를 반환 받는다. 내부적으로는 다른 Thread 에서 0.5초 sleep 후 complete 를
	  호출하여 Consumer 쪽으로 “Hello” 를 전달한다. 그럼 Consumer 쪽에선 get 메소드를 호출하면 이 값을 받을 수 있다.

	get 메소드를 호출 할 때 어떤 예외가 있었는지 확인하고 있으면 Throw 를 한다. 이 때 발생할 수 있는 예외는 두 가지가 있다. 
	ExecutionException (Future 실행 중 예외 발생), InterruptedException (실행 중인 Thread 에서 interrupt 가 발생한 경우)
	 */	
	public Future<String> calculateAsync() throws InterruptedException {
	    CompletableFuture<String> completableFuture = new CompletableFuture<>();
	 
	    Executors.newCachedThreadPool().submit(() -> {
	        Thread.sleep(500);
	        completableFuture.complete("Hello");
	        return null;
	    });
	    
	    //또는 아래처러 해도 된다.
	   /* new Thread(() -> {
	        try {Thread.sleep(500);} catch (InterruptedException e) {}
	        completableFuture.complete("Hello");
	    });*/
	 
	    return completableFuture;
	}
	
	public Future<String> calculateAsync2() throws InterruptedException {
	    CompletableFuture<String> completableFuture = new CompletableFuture<>();
	 
	    completableFuture.complete("Hello2");
	    
	    return completableFuture;
	}
	
	/*
	 만약 Consumer 로 보낼 값을 바로 알고 있다면 Future 생성 동시에 바로 값을 넣어줄 수 있다. 이 때 get 메소드에서는 Blocking 되는 일 없이 바로 값을 가져온다. 
	 */	
	@Test
	public void completedFutureTest() throws Exception {
		Future<String> completableFuture = CompletableFuture.completedFuture("Hello");
	 
		String result = completableFuture.get();
		assertEquals("Hello", result);
	}
	
	
	/*
	 Future 실행을 cancel 하는 경우도 있다. 이는 Future 의 cancel 메소드를 호출한 경우인데 
	 이 상황에서 comsumer 쪽에서 get 메소드를 호출하면 CancellationException 을 Throw 한다. 
	 참고로 cancel 메소드는 mayInterruptIfRunning 라는 Boolean 인자를 받는데 CompetableFuture 에서는 Ture 이든 False 이든 아무 영향이 없다. 
	 */
	public Future<String> calculateAsyncWithCancellation() throws InterruptedException {
	    CompletableFuture<String> completableFuture = new CompletableFuture<>();
	 
	    Executors.newCachedThreadPool().submit(() -> {
	        Thread.sleep(500);
	        completableFuture.cancel(false);
	        return null;
	    });
	 
	    return completableFuture;
	}

	
	/*
	  위 예제는 Executor 로 로직을 실행하고 complete 로 결과를 넘겨주는 것을 볼 수 있다. 이런 boilerplate 코드 없이 바로 CompletableFuture 를 만들 수 없을까?
		runAsync, supplyAsync 메소드로 바로 연산 로직을 넣어줄 수 있다
	 */
	@Test
	public void completedFutureTest2() throws Exception {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
		 
		assertEquals("Hello", future.get());
	}
}
