package com.sleep.utils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {
	
	private static final int  THREA_COUNT = 3;
	static DownloadThread[] threads;

	public static synchronized void DownloadFile(String path, File directory){
		//连接服务器，获取一个文件，获取文件的长度，在本地创建一个大小跟服务器大小一样的临时文件
		
		try {
			String file = directory.toString();
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			int code = conn.getResponseCode();
			if (code==200) {
				//服务器返回数据的长度，即文件的长度
				int length = conn.getContentLength();
				System.out.println("文件长度"+length);
				//在客户端创建一个大小和服务端一样大小临时文件
				RandomAccessFile rafAccessFile = new RandomAccessFile(file, "rwd");
				//指定临时文件长度
				rafAccessFile.setLength(length);
				rafAccessFile.close();
				//假设三个线程下载资源,平均每个线程下载的文件的大小
				int blockSize = length / THREA_COUNT;	
				for(int threadId=1;threadId<=THREA_COUNT;threadId++){
					//第一个线程下载的开始位置
					int startIndex = (threadId-1) * blockSize;
					int endIndex = threadId * blockSize - 1;
					//最后一个线程下的长一点
					if(threadId == THREA_COUNT){
						endIndex = length;
					}
					threads[threadId]  = new DownloadThread(threadId, startIndex, endIndex, path,file);
					threads[threadId].start();
				}
			}else {
				System.out.print("错误");
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static class DownloadThread extends Thread{
		
		private int threadId;
		private int startIndex;
		private int endIndex;
		private String path;
		private String file;
		
		public DownloadThread(int threadId, int startIndex, int endIndex,
				String path,String file) {
			super();
			this.threadId = threadId;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.path = path;
			this.file = file;
		}

		@Override
		public void run() {
		
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5000);
				//重要：请求服务器下载的部分文件，指定文件的位置
				conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
				
				//从服务器请求部分资源返回206
				int code = conn.getResponseCode();
				System.out.println(""+code);
				//返回当前请求位置的输入流
				InputStream is = conn.getInputStream();
				RandomAccessFile rafAccessFile = new RandomAccessFile(file, "rwd");
				//从哪开始写
				rafAccessFile.seek(startIndex);
				
				int len = 0;
				byte[] buffer = new byte[1024];
				while((len=is.read(buffer))!=-1){
					rafAccessFile.write(buffer, 0, len);
				}
				
				
				
				is.close();
				rafAccessFile.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}