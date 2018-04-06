package com.yhs.async.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * CachedThreadPool은 FixedThreadPool과 달리 태스크의 숫자에 따라 쓰레드의 숫자가 가변된다는 것이다. 
 * 그러므로 생성시 쓰레드 숫자를 지정할 필요가 없다
 *
 */
public class UsingCachedThreadPool {
	public static void main(String argc[]) {
		System.out.println("Main thread starts here...");
		
		//ExecutorService execService = Executors.newFixedThreadPool(2); 
		ExecutorService execService = Executors.newCachedThreadPool(); 
		
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
	
		execService.shutdown();
		
		System.out.println("Main thread ends here...");
	}
}