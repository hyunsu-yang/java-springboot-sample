package com.yhs.async.completablefuture;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;

public class CompletableFutureAdvancedTest {

	@Test
	public void test() {
		fail("Not yet implemented");
	}
	
	/*
	 람다식 표현 방법
	 */
	@Test
	public void expressionLambdaTest() throws Exception {
		// Run a task specified by a Runnable Object asynchronously.
		CompletableFuture<Void> future = CompletableFuture.runAsync(new Runnable() {
		    @Override
		    public void run() {
		        // Simulate a long-running Job
		        try {
		            TimeUnit.SECONDS.sleep(1);
		        } catch (InterruptedException e) {
		            throw new IllegalStateException(e);
		        }
		        System.out.println("I'll run in a separate thread than the main thread." + Thread.currentThread().getId());
		    }
		});
		
		CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
			// Simulate a long-running Job
	        try {
	            TimeUnit.SECONDS.sleep(1);
	        } catch (InterruptedException e) {
	            throw new IllegalStateException(e);
	        }
	        System.out.println("I'll run in a separate thread than the main thread." + Thread.currentThread().getId());
		});
		
		future.get(1000, TimeUnit.MILLISECONDS);
		future2.get(1000, TimeUnit.MILLISECONDS);		
	}
	
	@Test
	public void runSameThreadTest() throws Exception {
		System.out.println("mainThread: " + Thread.currentThread().getId());
		try {TimeUnit.SECONDS.sleep(1);} catch (InterruptedException e) {throw new IllegalStateException(e);}
		
		CompletableFuture.supplyAsync(() -> {		   
		    System.out.println("supplyAsync: " +Thread.currentThread().getId());
		    return "Some Result";
		}).thenApply(result -> {
		    /* 
		      Executed in the same thread where the supplyAsync() task is executed
		      or in the main thread If the supplyAsync() task completes immediately (Remove sleep() call to verify)
		    */
			System.out.println("thenApply: " + Thread.currentThread().getId());
		    return "Processed Result";
		});
		
		 
	}
	
	@Test
	public void runDifferntThreadTest() throws Exception {
		System.out.println("mainThread: " + Thread.currentThread().getId());
		CompletableFuture.supplyAsync(() -> {
			 System.out.println("supplyAsync: " +Thread.currentThread().getId());
		    return "Some Result";
		}).thenApplyAsync(result -> {
			 // Executed in a different thread from ForkJoinPool.commonPool()
			System.out.println("thenApplyAsync: " + Thread.currentThread().getId());
		    return "Processed Result";
		});
	}
	
	
	@Test
	public void allOfTest() throws Exception {
		List<String> webPageLinks = Arrays.asList("webLink1", "webLink2", "webLink3");	// A list of 100 web page links
		
		// Download contents of all the web pages asynchronously
		List<CompletableFuture<String>> pageContentFutures = webPageLinks.stream()
		        .map(webPageLink -> downloadWebPage(webPageLink))
		        .collect(Collectors.toList());


		// Create a combined Future using allOf()
		CompletableFuture<Void> allFutures = CompletableFuture.allOf(
		        pageContentFutures.toArray(new CompletableFuture[pageContentFutures.size()])
		);		
		/*
		 The problem with CompletableFuture.allOf() is that it returns CompletableFuture<Void>. 
		 But we can get the results of all the wrapped CompletableFutures by writing few additional lines of code - 
		 */
		// When all the Futures are completed, call `future.join()` to get their results and collect the results in a list -
		CompletableFuture<List<String>> allPageContentsFuture = allFutures.thenApply(v -> {
		   return pageContentFutures.stream()
		           .map(pageContentFuture -> pageContentFuture.join())
		           .collect(Collectors.toList());
		});
		
		allPageContentsFuture.join().forEach(System.out::println);
	}
		
	
	CompletableFuture<String> downloadWebPage(String pageLink) {
		return CompletableFuture.supplyAsync(() -> {
			// Code to download and return the web page's content
			return "web page content";
		});
	} 
}
