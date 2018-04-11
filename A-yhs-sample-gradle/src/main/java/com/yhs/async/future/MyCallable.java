package com.yhs.async.future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
* A task that sleeps for a second, then returns 1
* java 에서 제공하던 Future interface 가 제공한 Async 이지만 get() 메서드로 Blocking 을 유발하는 단점
**/
public class MyCallable implements Callable<Integer> {

    @Override	
    public Integer call() throws Exception {
        Thread.sleep(1000);
        return 1;
    }
    
    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Integer> f = exec.submit(new MyCallable());

        System.out.println(f.isDone()); //False

        System.out.println(f.get()); //Waits until the task is done, then prints 1
    }

}

