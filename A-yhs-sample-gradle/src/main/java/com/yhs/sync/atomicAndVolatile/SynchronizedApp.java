package com.yhs.sync.atomicAndVolatile;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedApp  {
	
	private static Logger logger = LoggerFactory.getLogger(SynchronizedApp.class);	
    private static boolean stopRequested=false;

    private static synchronized  void requestStop(){
        stopRequested = true;
    }

    private static synchronized  boolean stopRequested(){
        return stopRequested;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread backThread = new Thread(new Runnable(){
            public void run(){
                int i=0;
                System.out.println(2);
                System.out.println("");
                
                while(!stopRequested()) {
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