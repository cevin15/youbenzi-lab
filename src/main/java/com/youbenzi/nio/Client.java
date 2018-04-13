package com.youbenzi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
		ByteBuffer writeBuffer = ByteBuffer.wrap("你好你好，我是客户端，收到请回复。".getBytes());
		
		socketChannel.write(writeBuffer);
		
		ByteBuffer readBuffer = ByteBuffer.allocate(1024);
		
		int num = socketChannel.read(readBuffer);
		if (num > -1) {
			readBuffer.flip();
			byte[] bs = new byte[readBuffer.limit()];
			readBuffer.get(bs);
			
			System.out.println(new String(bs, "utf-8"));
			
		}
		readBuffer.clear();
	}
	
}
