package com.yhs.file.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;

/*
 * 한 가지 의문점이 생겨야 좋은데요, 만약 InputStream으로 만든 FileChannel에서 read를 하지 않고 write를 수행하면 어떻게 될까요? 
 * 반대로, OutputStream에서 만들어낸 FileChannel에서 write를 하지 않고 raed를 하는 경우 어떻게 될까요? 위의 경우 Exception이 발생합니다. 
 * 한번 확인해 보시구요, 다음을 정리하도록 합시다.
	
	InputStream으로 만들어낸 FileStream에선 read 만 할 수 있다! (write하는 경우 Exception 발생!)
	OutputStream으로 만들어낸 FileStream에선 write 만 할 수 있다! (read하는 경우 Exception 발생!)
	이건 FileChannel을 만들어낼때 사용한 객체의 특성때문이라고 생각하시면 될 것 같습니다.

그런데, RandomAccessFile의 경우에는 어떨까요. RandomAccessFile은 seek로 탐색한 파일포인터 위치에서 읽거나 쓸 수 있는 객체입니다. 
당연하게도, read/wrtie 모두 수행 가능합니다. 하지만 seek으로 설정한 파일포인터 부터 읽거나/쓰기가 가능합니다.
 */
public class NioUseCaseTest {

	@Test
	public void getFileChanel() throws IOException {
		FileInputStream fis = new FileInputStream("test.txt");
		FileChannel cin = fis.getChannel();
		FileOutputStream fos = new FileOutputStream("test.txt");
		FileChannel cout = fos.getChannel();
		RandomAccessFile raf = new RandomAccessFile("test.txt", "rw");
		FileChannel cio = raf.getChannel();
	}
	
	@Test
	public void readWriteFileChannel() throws IOException {
		FileInputStream fis = new FileInputStream("input.txt");
		FileOutputStream fos = new FileOutputStream("output.txt");
		
		ByteBuffer buf = ByteBuffer.allocateDirect(10);
		
		FileChannel cin = fis.getChannel();
		FileChannel cout = fos.getChannel();
		
		cin.read(buf); // channel에서 읽어 buf에 저장!
		buf.flip();
		cout.write(buf); // buf의 내용을 channel에 저장!
	}
	
	@Test
	public void readWriteFileChannelByRandomAccessFile() throws IOException {
		RandomAccessFile raf = new RandomAccessFile("sample.txt", "rw");		
		FileChannel channel = raf.getChannel();
		
		ByteBuffer buf = ByteBuffer.allocateDirect(10);
		
		buf.clear();
		
		raf.seek(10); // 파일의 10째 바이트로 파일포인터 이동
		channel.read(buf); // channel에서 읽어 buf에 저장!buf.flip();
		
		raf.seek(40);    // 파일의 40째 바이트로 파일포인터 이동
		channel.write(buf); // buf의 내용을 channel에 저장!
		
		channel.close();
		raf.close();
	}

}
