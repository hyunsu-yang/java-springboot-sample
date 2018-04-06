package com.yhs.sync.atomicAndVolatile;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * 스레드를 사용할때 공유자원이 있다면 반드시 동기화작업이 선행되어야 한다.
	Synchromized는 가장 확실한 방법일 수는 있지만 	상황에 맞춰서 volatile이나 Atomic 클래를 활용해서 	보다 유연하게 사용할 수 있다.
 */
public class AtomicApp  {
	
	private static Logger logger = LoggerFactory.getLogger(AtomicApp.class);	
    private static AtomicBoolean stopRequested= new AtomicBoolean(false);

    private static  void requestStop(){
        stopRequested.set(true);;
    }

    private static  AtomicBoolean stopRequested(){
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backThread = new Thread(new Runnable(){
            public void run(){
                int i=0;
                System.out.println(2);
                System.out.println("");
                
                while(!stopRequested().get()) {
                	i++;
                	 /*System.out.println(i++);
                	 
                	 try {
						TimeUnit.MILLISECONDS.sleep(10);
					} catch (InterruptedException e) {
						 logger.error("",e);
					}*/
                }
                    
                System.out.println(4+"/"+i);
            } 
        });

        backThread.start();
        System.out.println(1);
        TimeUnit.SECONDS.sleep(1);
        
        System.out.println("");
        System.out.println(3);
        requestStop();

    }
}