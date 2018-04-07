package com.yhs.file.nio;

import java.nio.ByteBuffer;

/*
 * 보시는바와 같이 byte[]와 non-direct ByteBuffer에 비해 direct ByteBuffer의 할당 속도가 매우 느린 것으로 나타납니다. 
 * 따라서 ByteBuffer가 필요할때마다 그때그떄 할당해서 사용한다면 프로그램 퍼포먼스에 큰 영향을 줄 수 있습니다. 
 * 이 오버헤드는 무시 할 수 있을 것 같지 않습니다. 따라서 ByteBuffer를 재활용하여 사용합시다. 재활용 방법으로는 ByteBufferPool이 가능 할 것
 */
public class NioByteBufferTestApp {
	
	 public static void main(String[] args) {
		 long startTime = System.currentTimeMillis();
		 for(int i = 0; i <= 1000000 ; i++){
		     //ByteBuffer buf = ByteBuffer.allocate(1024);
		      ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		      //byte[] buf = new byte[1024];
		 }
		 
		 long endTime = System.currentTimeMillis();
		 long elapsedTime = endTime - startTime;
		 System.out.println("elapsedTime = " + elapsedTime);
	 }
}
