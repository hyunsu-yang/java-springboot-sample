package com.yhs.async.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsingFixedThreadPool {
	public static void main(String argc[]) {
		System.out.println("Main thread starts here...");
		
		ExecutorService execService = Executors.newFixedThreadPool(2); 
		
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());
	
		execService.shutdown();
		
		System.out.println("Main thread ends here...");
	}
}
