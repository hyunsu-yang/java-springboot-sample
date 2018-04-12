package com.yhs.async.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.junit.Test;

/*
 *  CompletableFuture ?
 	"미래에 처리할 업무(Task)로서,  Task 결과가 완료되었을때 값을 리턴하거나, 다른 Task가 실행되도록 발화(trigger)시키는 Task."
 
 2. CompletableFuture의 장점

	1) 명시적 쓰레드 선언없이 쓰레드를 사용할수 있다.
	2) 함수형 프로그래밍방식으로 비동기적으로 동시성/병렬 프로그래밍을 가능하게 함으로서 의도를 명확하게 드러내는 함축적인 프로그래밍을 가능하게 한다.
	3) 각 타스크마다 순서적 연결을 할수도 있고, 타스크의 예외처리도 가능하다.

3. 쓰레드의 단점과 Java의 개선과정
	자바를 해온 사람들이라면 쓰레드의 개념에 익숙할 것이다. 쓰레드란 여러개의 코드 실행이 동시적으로 실행되도록 프로그래밍 기법을 말한다.
	 이 기법을 사용해 IO등이 길어서 시간이 오래 걸리는 작업들을 빠른 시간안에 해결할수 있다. 그러나 쓰레드엔 단점이 존재한다. 왜냐면 
	1) 쓰레드 간에는 항상 공유 메모리에 대한 공유타이밍 이슈가 존재해서 동기화를 잘 시켜줘야 한다. (메모리 이슈, 데드락발생)
	2) 여러 쓰레드간의 완료 이후 통신을 위해 부가적인 메소드들을 만들어내야 한다.
	3) 쓰레드하나의 생성을 위해서는 최소 하나의 클래스를 생성해야 하는데, 이는 로직과 상관없는 많은 코드를 작성해야 한다는 것이다.	
	4) 쓰레드를 생성하고 삭제하는데에는 많은 비용이 소모된다.

비동기 타스크간의 연결, 중간에 발생하는 예외처리에가 어려웠는데 이것을 해결하기 위해 CompletableFuture가 등장
 */
public class CompletableFuture2Test {

	/*
	 예제에서 확인되듯이 두개의 비동기 타스크가 자연스럽게 연결되었다. ( A완료시 B실행)
		1) Executor를 통해 비동기 타스크가 수행될 쓰레드를 생성하고
		2) CompletableFuture.runAsync를 통해 다른 쓰레드에서 비동기 식으로 동작할 로직를 선언하고
		3) CompletableFuture.thenRun 를 통해 첫번째 타스크가 완료된 이후에 연속적으로 동작할 로직을 선언했다. 
	 */
	@Test
	public void basic() {
		 ExecutorService executor = Executors.newFixedThreadPool(2);

	      CompletableFuture
	        .runAsync(()->{ 
	          try{Thread.sleep(1);} catch(Exception e){};
	          
	          System.out.println("Hello!");

	          try{Thread.sleep(1);} catch(Exception e){};
	        },executor)
	        .thenRun(()->System.out.println("World"));

	      System.out.println("async request is ready.");
	}
	
	@Test
	public void 연속적인연결_패턴_test() {
		Supplier<String> sup = () -> {
            try{Thread.sleep(1);} catch(Exception ex){};
            return "result A on thread "+Thread.currentThread().getId();
            };	
		
		CompletableFuture cf= CompletableFuture.supplyAsync(sup)
            .thenApply(str->str+" + tailed")
            .thenAccept(finalResult->System.out.println(finalResult));

		System.out.println("Task execution requested on on thread " + Thread.currentThread().getId());
	}

}
