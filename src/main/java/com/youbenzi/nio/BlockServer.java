package com.youbenzi.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class BlockServer {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(8080));
		
		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			
			new Thread(new SocketHandler(socketChannel)).start();
		}
	}
	
}
