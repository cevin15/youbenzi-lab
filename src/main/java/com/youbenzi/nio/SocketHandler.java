package com.youbenzi.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketHandler implements Runnable {

	private SocketChannel socketChannel;
	
	public SocketHandler(SocketChannel socketChannel) {
		super();
		this.socketChannel = socketChannel;
	}

	@Override
	public void run() {
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		
		try {
			int num = socketChannel.read(buffer);
			if (num != -1) {
				buffer.flip();
				byte[] bs = new byte[buffer.limit()];
				buffer.get(bs);
				
				System.out.println(new String(bs, "utf-8"));
			}
			buffer.clear();
			
			ByteBuffer writeBuffer = ByteBuffer.wrap(("我已收到您的信息").getBytes());
			try {
				socketChannel.write(writeBuffer);
				socketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writeBuffer.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
