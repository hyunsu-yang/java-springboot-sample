package com.yhs.async.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
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

/*
 CompletableFuture를 코드를 통해 기본적인 사용법을 알아보자.
 
  run 패턴은
  	인자값 없고 리턴 값이 없을 때
  	Runnable Interface 사용
 supply 패턴은 
 	인자값 없고 리턴 값이 있을 때 사용
 	Supply Interface 사용
 accept 패턴은
 	인자를 받아서 소비 (리턴 없음)
 	Consumer Interface 사용
 apply 패턴은
	인자를 받아서 다른 결과로 리턴 
	Function Interface 사용	
 	
 보통 supply 패턴일 때 completableFuture.get() 해서 결과를 리턴 받는다.
 get() 이 아닐 경우는 thenAceept() 를 통해서 소비할 수도 있다.	


1. 가장 간단한 비동기 계산
CompletableFuture.supplyAsync(this::sendMsg);
팩토리 메서드 supplyAsync는 supplier를 인수로 받아서 CompletableFuture를 반환한다. 비동기적으로 실행해서 결과를 생성한다.
ForkJoinPool의 Executor 중 하나가 Supplier를 실행할 것이다. (ForkJoinPool에 대해서는 다음 링크를 확인)
두번째 인수를 받는 오버로드 함수에서 다른 Executor를 선택적으로 전달할 수 있다.

2. 콜백함수 붙이기
콜백의 장점은 비동기 계산을 수행할 때 결과를 기다리지 않고 다음 작업을 정의할 수 있다는데 있다.
CompletableFuture.supplyAsync(this::sendMsg) 
                 .thenAccept(this::notify);
thenAccept는 앞선 계산의 결과의 콜백 메서드로 전달된 Consumer를 실행한다.

3. 여러 콜백함수 묶기
thenAccpet는 Consumer가 void를 반환하기 때문에 다른 콜백함수로 값을 전달할 수가 없다.
이때 thenApply를 사용하면 CompletableFuture를 반환하기 때문에 또다른 콜백함수를 연결할 수 있다.
CompletableFuture.supplyAsync(this::findReceiver)
                     .thenApply(this::sendMsg)
                     .thenAccept(this::notify);

4. 다른 비동기 코드와 조합하기
다른 CompletionStage와의 조합을 통해 새로운 CompletionStage를 만들어야 할 필요도 있다.
이때 thenCompose를 사용한다. (thenApply vs thenCompose 링크)
thenCompose가 flatmap과 비슷하다고 생각하면 좋다.
CompletableFuture.supplyAsync(this::findReceiver) 
                 .thenCompose(CompletableFuture.supplyAsync(() -> other.getReceiver()));

5. 에러 처리
CompletableFuture 작업을 진행하던 중 에러가 발생하게 되면 특정 함수를 통해 에러를 처리할 수 있다.
CompletableFuture.supplyAsync(this::failingMsg) 
                 .exceptionally(ex -> new Result(Status.FAILED))
                 .thenAccept(this::notify);

6. 여러개의 계산에 의존적인 콜백
CompletableFuture<String> to = CompletableFuture.supplyAsync(this::findReceiver);
CompletableFuture<String> text = CompletableFuture.supplyAsync(this::createContent);
to.thenCombine(text, this::sendMsg);
thenCombine은 2개의 비동기 계산을 먼저 처리한 결과로 BiFunction을 실행하도록 되어 있다.
 
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
		 ExecutorService executor = Executors.newFixedThreadPool(1);

	      CompletableFuture
	        .runAsync(()->{ 
	          try{Thread.sleep(1);} catch(Exception e){};
	          
	          System.out.println("Hello!");

	          try{Thread.sleep(1);} catch(Exception e){};
	        },executor)
	        .thenRun(()->System.out.println("World"));
	      	     
	      System.out.println("async request is ready.");
	      try{Thread.sleep(10);} catch(Exception e){};
	}
	
	/*
	 then 이라는 접두어는 앞의 타스크들은 모두 반드시 완료되어야 한다는 의미이다. 앞의 타스크들은 하나혹은 두개일수 있다.
	 
	Apply라는 동사는 뒤에 Function형 람다식이 올거라는 것을 의미한다. 
	Function형은 첫번째 임의의 형태<T>의 입력값을 받아 처리한후 두번째 임의의 형태<R>의 값으로 출력하는 람다식이다.
	
	아래 예제는 1초 이후에 supplyAsync을 이용해 다른 쓰레드에서 스트링을 리턴시키고, 
	리턴값을 thenApply 를 통해 다른 스트링과의 조합을 한후, thenAccept를 통해 화면에 뿌려주는 형태의 체이닝이다.	
	 */
	@Test
	public void 연속적인연결패턴_thenApply_test() {
		Supplier<String> sup = () -> {
            try{Thread.sleep(100);} catch(Exception ex){};
            return "result A on thread "+Thread.currentThread().getId();
            };	
		
		CompletableFuture cf= CompletableFuture.supplyAsync(sup)
            .thenApply(str->str+" + tailed")
            .thenAccept(finalResult->System.out.println(finalResult));

		System.out.println("Task execution requested on on thread " + Thread.currentThread().getId());
	}
	
	/*
	  Run이라는 동사의 의미는 인자를 받지 않고 결과를 리턴하지 않는 타스크입력값을 받는 다는 의미이다. 
	 */
	@Test
	public void 연속적인연결패턴_thenRunAsync_test() {
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		 CompletableFuture cf= CompletableFuture.runAsync(() -> {
             try{Thread.sleep(1);} catch(Exception ex){
               System.out.println("Exception"+ex.getMessage());
             };
             System.out.println("runAsync on thread "+Thread.currentThread().getId());
           }, executor)
           .thenRunAsync(
             ()->System.out.println("thenRunAsync on thread "+Thread.currentThread().getId())
           );

		 System.out.println("Task execution requested on thread " + Thread.currentThread().getId());
	}
	
	/*
	 thenComposeAsync
	 	: ComletableFuture 의 수행중 완전히 다른 CompletableFuture 를 조합하여 실행할수 있다

	 실행되고 있는 타스크안에서 람다식을 통해 다른 CompletionStage 타스크를 조합할수 있다. 
	 여기서 조합이라는 의미는 완료시점을 하나로 맞출수 있다는 뜻이다. thenComposeAsync 는 중복적으로 사용되어 여러 타스크를 조합할수가 있다. 
	 예를 들어 웹서버에서 하나의 리퀴스트를 받아 4개의 내부 리퀘스트를 보낸후 결과를 모두 취합하여 최초 리퀘스트에 리턴해줄 필요가 있을때 적당하다.
	 
	 thenApply 와 thenAccept 차이
	 
	 	thenApply
	 		: 리턴 값이 존재, completableFuture.get() 값을 이렇게 전달 받을 수 있음.
	 		
 		thenAccept
 			: 리턴 값이 없음. completableFuture.get() 값이 null 됨.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 조합패턴_thenComposeAsync_test() throws Exception {
		  ExecutorService e = Executors.newCachedThreadPool();
		  
	      long startTime = System.currentTimeMillis();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(5000);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId()+" now="+(System.currentTimeMillis()-startTime));
	          return 100;
	        }).thenApply(rtn -> rtn + 100 );

	      CompletableFuture cf2= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId()+" now="+(System.currentTimeMillis()-startTime));
	          return 200;
	        }).thenAccept(rtn -> System.out.println(rtn + 100));

	      CompletableFuture cf3= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf3 supplyAsync on thread "+Thread.currentThread().getId()+" now="+(System.currentTimeMillis()-startTime));
	          return 300;
	        },e).thenAccept(rtn -> System.out.println(rtn + 100));

	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());	      
	      cf3.thenComposeAsync(data1 -> cf2).thenComposeAsync(data2 -> cf1).join();

	      System.out.println("final cf1.get() = " + cf1.get()+ " cf2.get()="+cf2.get()+" cf3.get()="+cf3.get()+" now="+(System.currentTimeMillis()-startTime));
	}
	
	/*
	 thenCombineAsync
	 두 타스크들 결과를 모두 사용한 타스크간 결합 
		: 두 타스크의 결과를 모두 기다렸다가 결과들을 조합하여 그다음 일을 하는것)
		
		=> 앞의 타스크와 파라미터로 받은 타스크의 결과를 입력값으로 받아서 새로운 결과를 리턴한다. get으로서 결과를 얻을수 있다.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 조합패턴_thenCombineAsync_test() throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 500;
	        });
	      CompletableFuture cf2= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 10;
	        },e)
	        .thenCombineAsync(cf1,(Integer x,Integer y)->x*y);

	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());
	      System.out.println("final result = " + cf2.get());
	      System.out.println("end");
	}
	
	/*
	 thenAcceptBoth
	 	: 앞의 타스크의 결과, 파라미터로 받은 타스크의 결과를 받아 리턴은 하지않고 결과를 소비하는 패턴이다.
	 	
	 	cf2의 결과가 null 인 이유는 
	 	이미 thenAcceptBothAsync 의 consumer 를 통해서 결과를 소비했기에
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 조합패턴_thenAcceptBoth_test() throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 500;
	        });
  
	      CompletableFuture cf2= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 10;
	        },e)
	        .thenAcceptBothAsync(cf1,(Integer x,Integer y)->System.out.println("x="+x+" y="+y));

	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());
	      System.out.println("final result = " + cf2.get());
	      System.out.println("end");
	}
	
	/*
	 runAfterBothAsync
	 	: 앞의 타스크의 완료, 파라미터로 받은 타스크의 완료이후에 아무런 파라미터를 받지 않고 로직을 수행한다. 리턴은 하지 않는다.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 조합패턴_runAfterBothAsync_test() throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 500;
	        });
	      
	      CompletableFuture cf2= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 10;
	        },e)
	        .runAfterBothAsync(cf1, ()->System.out.println("runAfterBothAsync on thread "+Thread.currentThread().getId()));

	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());
	      System.out.println("final result = " + cf2.get());
	}
	
	/*
	 applyToEither
	 	:두 타스크중에 먼저 결과를 내는 쪽의 결과값을 가지고 타스크를 실행한다.
	 	
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 먼저도달_applyToEither_test() throws Exception {
		 ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(5000);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 100;

	        });

	      CompletableFuture cf2= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 200;

	        });

	      CompletableFuture cf3= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf3 supplyAsync on thread "+Thread.currentThread().getId());
	          return 300;
	        },e)

	        .applyToEither(cf2, rtn -> rtn + 10)
	        .applyToEither(cf1, Function.identity());

	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());  
	      System.out.println("final result = " + cf3.get());
	}
	
	/*
	 acceptEitherAsync
	 	:acceptEitherAsync는 앞의 타스크와 파라미터로 받은 타스크중에 더 빨리 결과를 리턴하는 타스크의 값을 가지고 람다식을 통해 값을 처리할수 있다. 
	 	람다식에서는 값을 사용하기만 할뿐 리턴하지는 않는다. acceptEitherAsync 를 연속적으로 붙여 사용할수 있는데, 이중 가장 먼저 값을 리턴한 타스크의 람다식만 수행된다.	 	
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 먼저도달_acceptEitherAsync_test() throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(500);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 100;
	        });

	      CompletableFuture cf2= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 200;
	        });

	      CompletableFuture cf3= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(3000);} catch(Exception ex){};
	          System.out.println("cf3 supplyAsync on thread "+Thread.currentThread().getId());
	          return 300;

	        },e)
	        .acceptEitherAsync(cf2, (x)->System.out.println("cf2 acceptEitherAsync x="+x))
	        .acceptEitherAsync(cf1, (x)->System.out.println("cf1 acceptEitherAsync x="+x));
	      
	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());	      
	      System.out.println("blocking result: " + cf3.get());
	}
	
	/*
	 acceptEitherAsync
	 	:runAfterEitherAsync는 앞의 타스크와 파라미터로 받은 타스크중에 하나라도 먼저 끝나는 타스크가 있으면 람다식을 수행하는 메소드이다. 
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void 먼저도달_runAfterEitherAsync_test() throws Exception {
		ExecutorService e = Executors.newCachedThreadPool();

	      CompletableFuture cf1= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(500);} catch(Exception ex){};
	          System.out.println("cf1 supplyAsync on thread "+Thread.currentThread().getId());
	          return 100;
	        });

	      CompletableFuture cf2= CompletableFuture.supplyAsync(()->{
	          try{Thread.sleep(1000);} catch(Exception ex){};
	          System.out.println("cf2 supplyAsync on thread "+Thread.currentThread().getId());
	          return 200;

	        });

	      CompletableFuture cf3= CompletableFuture.supplyAsync(() -> {
	          try{Thread.sleep(700);} catch(Exception ex){};
	          System.out.println("cf3 supplyAsync on thread "+Thread.currentThread().getId());
	          return 300;
	        },e)

	      .runAfterEitherAsync(cf2, ()->System.out.println("cf2 runAfterEitherAsync x="+Thread.currentThread().getId()))
	      .runAfterEitherAsync(cf1, ()->System.out.println("cf1 runAfterEitherAsync x="+Thread.currentThread().getId()));
	      
	      System.out.println("Task execution requested on thread " + Thread.currentThread().getId());
	      System.out.println("blocking result: " + cf3.get());
	}

}
