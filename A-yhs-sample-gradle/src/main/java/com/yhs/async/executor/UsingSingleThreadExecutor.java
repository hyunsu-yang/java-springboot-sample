package com.yhs.async.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *  단지 쓰레드의 숫자가 단지 하나이고 하나의 태스크가 완료된 이후에 다음 태스크가 실행된다.
 *   이것을 사용하면 동기화를 고려해야 할 필요없이, 즉 synchronized를 사용하지 않고도 안전하게 실행할 수 있다.
 *
 */
public class UsingSingleThreadExecutor {
	public static void main(String argc[]) {
		System.out.println("Main thread starts here...");
		
		ExecutorService execService = Executors.newSingleThreadExecutor(); 
		
		execService.execute(new MyThreadTask());
		execService.execute(new MyThreadTask());

		execService.shutdown();
		
		System.out.println("Main thread ends here...");
	}
}