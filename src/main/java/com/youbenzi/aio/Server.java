package com.youbenzi.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class Server {

	public static void main(String[] args) throws IOException {
		AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(8080));
		
		Attachment att = new Attachment();
        att.setServer(serverSocketChannel);
		
		serverSocketChannel.accept(att, new CompletionHandler<AsynchronousSocketChannel, Attachment>() {

			@Override
			public void completed(AsynchronousSocketChannel client, Attachment attachment) {
				try {
                    SocketAddress clientAddr = client.getRemoteAddress();
                    System.out.println("收到新的连接：" + clientAddr);

                    // 收到新的连接后，server 应该重新调用 accept 方法等待新的连接进来
                    attachment.getServer().accept(att, this);

                    Attachment newAtt = new Attachment();
                    newAtt.setServer(serverSocketChannel);
                    newAtt.setClient(client);
                    newAtt.setReadMode(true);
                    newAtt.setBuffer(ByteBuffer.allocate(2048));

                    // 这里也可以继续使用匿名实现类，不过代码不好看，所以这里专门定义一个类
                    client.read(newAtt.getBuffer(), newAtt, new ChannelHandler());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
			}

			@Override
			public void failed(Throwable exc, Attachment attachment) {
				System.out.println("accept failed");
			}
		});
		
		// 为了防止 main 线程退出
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
        }
	}
	
}
