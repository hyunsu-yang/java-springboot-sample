package com.yhs.sync.atomicAndVolatile;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NonSynchronizedApp  {
	
	private static Logger logger = LoggerFactory.getLogger(NonSynchronizedApp.class);	
    private static boolean stopRequested=false;


    public static void main(String[] args) throws InterruptedException {
        Thread backThread = new Thread(new Runnable(){
            public void run(){
                int i=0;
                System.out.println(2);
                System.out.println("");
                
                while(!stopRequested) {
                	i++;
                	 /*
                	 System.out.println(i++);
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
        stopRequested = true;

    }
}