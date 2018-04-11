package com.yhs.async.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

/**
* A (pure) function that adds one to a given Integer
* They are Futures that also allow you to string tasks together in a chain. 
* You can use them to tell some worker thread to "go do some task X, 
* and when you're done, go do this other thing using the result of X". 
* Here's a simple example:
**/
public  class PlusOne implements Function<Integer, Integer> {	
	
    @Override
    public Integer apply(Integer x) {
        return x + 1;
    }


	public static void main(String[] args) throws Exception {
	    ExecutorService exec = Executors.newSingleThreadExecutor();
	    CompletableFuture<Integer> f = CompletableFuture.supplyAsync(new MySupplier(), exec);
	    System.out.println(f.isDone()); // False
	    CompletableFuture<Integer> f2 = f.thenApply(new PlusOne());
	    System.out.println(f2.get()); // Waits until the "calculation" is done, then prints 2
	}
	
	
	
	public static class MySupplier implements Supplier<Integer> {
	    @Override
	    public Integer get() {
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            //Do nothing
	        }
	        return 1;
	    }
	}
}