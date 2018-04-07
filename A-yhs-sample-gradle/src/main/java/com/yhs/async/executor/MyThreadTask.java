package com.yhs.async.executor;

import java.util.concurrent.TimeUnit;

class MyThreadTask implements Runnable {	
	private static int count = 0;
	private int id;
	@Override
	public void run(){
		for(int i = 0; i<5; i++) {
			System.out.println("<" + id + ">TICK TICK " + i);
			try {
				TimeUnit.MICROSECONDS.sleep((long)Math.random()*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public MyThreadTask() {
		this.id = ++count;
		// ThreadPool을  사용할 필요 없고 Task 생성과 동시에 바로 실행을 위해 아래와 같은 방법도 가능
		//new Thread(this).start();
	}
}