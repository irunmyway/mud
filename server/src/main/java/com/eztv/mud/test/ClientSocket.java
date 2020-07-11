package com.eztv.mud.test;
 
import com.eztv.mud.utils.BDebug;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
/**
 * socket解决半包问题  采用包体长度(两字节)+包体内容来拆包
 * @author weir
 * 2017年9月19日下午4:31:36
 */
public class ClientSocket {
	public static void main(String args[]) throws IOException {
		Socket clientSocket = new Socket();
		clientSocket.connect(new InetSocketAddress(8089));
		new SendThread(clientSocket).start();
 
	}
 
	static class SendThread extends Thread {
		Socket socket;
		PrintWriter printWriter = null;
 
		public SendThread(Socket socket) {
			this.socket = socket;
			try {
				printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
 
		@Override
		public void run() {
			String reqMessage = "HelloWorld！ from clientsocket this is test half packages!HelloWorld！ from ces!HelloWorld！ frab";
			BDebug.trace("测试"+reqMessage.getBytes().length);
			for (int i = 0; i < 100; i++) {
				sendPacket(reqMessage);
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
 
		}
 
		public void sendPacket(String message) {
			byte[] contentBytes = message.getBytes();// 包体内容
			int contentlength = contentBytes.length;// 包体长度
			String head = String.valueOf(contentlength);// 头部内容
			byte[] headbytes = head.getBytes();// 头部内容字节数组
			byte[] bytes = new byte[headbytes.length + contentlength];// 包=包头+包体
			int i = 0;
			for (i = 0; i < headbytes.length; i++) {// 包头
				bytes[i] = headbytes[i];
			}
			for (int j = i, k = 0; k < contentlength; k++, j++) {// 包体
				bytes[j] = contentBytes[k];
			}
			try {
				OutputStream writer = socket.getOutputStream();
				writer.write(bytes);
				//writer.write(message.getBytes());
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
 
}
