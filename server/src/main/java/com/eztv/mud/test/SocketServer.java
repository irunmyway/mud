package com.eztv.mud.test;
 
import com.eztv.mud.utils.BDebug;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
/*
 * packet=packetHead+content
 * 先读出包体长度，再读出包体，不够就一直读
 */
public class SocketServer {
	public static void main(String args[]) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(8089));
			while (true) {
				Socket socket = serverSocket.accept();
				new ReceiveThread(socket).start();
 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 
	static class ReceiveThread extends Thread {
		public static final int PACKET_HEAD_LENGTH=2;//包头长度
		private Socket socket;
		private volatile byte[] bytes = new byte[0];
 
		public ReceiveThread(Socket socket) {
			this.socket = socket;
		}
 
		public byte[] mergebyte(byte[] a, byte[] b, int begin, int end) {
			byte[] add = new byte[a.length + end - begin];
			int i = 0;
			for (i = 0; i < a.length; i++) {
				add[i] = a[i];
			}
			for (int k = begin; k < end; k++, i++) {
				add[i] = b[k];
			}
			return add;
		}
 
		@Override
		public void run() {
			int count =0;
			while (true) {
				try {
					InputStream reader = socket.getInputStream();
					if (bytes.length < PACKET_HEAD_LENGTH) {
						byte[] head = new byte[PACKET_HEAD_LENGTH - bytes.length];
						int couter = reader.read(head);
						if (couter < 0) {
							continue;
						}
						bytes = mergebyte(bytes, head, 0, couter);
						if (couter < PACKET_HEAD_LENGTH) {
							continue;
						}
					}
					// 下面这个值请注意，一定要取2长度的字节子数组作为报文长度，你懂得
					byte[] temp = new byte[0];
					temp = mergebyte(temp, bytes, 0, PACKET_HEAD_LENGTH);
					String templength = new String(temp);
					int bodylength = Integer.parseInt(templength);//包体长度
					if (bytes.length - PACKET_HEAD_LENGTH < bodylength) {//不够一个包
						byte[] body = new byte[bodylength + PACKET_HEAD_LENGTH - bytes.length];//剩下应该读的字节(凑一个包)
						int couter = reader.read(body);
						if (couter < 0) {
							continue;
						}
						bytes = mergebyte(bytes, body, 0, couter);
						if (couter < body.length) {
							continue;
						}
					}
					byte[] body = new byte[0];
					body = mergebyte(body, bytes, PACKET_HEAD_LENGTH, bytes.length);
					count++;
					System.out.println("server receive body:  " + count+new String(body));
					bytes = new byte[0];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
