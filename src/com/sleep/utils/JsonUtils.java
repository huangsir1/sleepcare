package com.sleep.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import taiyi.web.jason.WebAPI;

public class JsonUtils {
	private static final String BASE_URL = WebAPI.DEFAULT_URL;
	public static final String USER_REGISTER = BASE_URL + "/api/user/register";
	public static final String IS_USER_REGISTER = BASE_URL + "/api/user/isUserRegister";
	public static final String SLEEP_REPORT_UPLOAD = BASE_URL + "/api/sleepReport/upload";
	public static final String BREATHE_REPORT_UPLOAD = BASE_URL + "/api/breatheReport/upload";
	
	public static Statuss sendRequest(String url, Object object) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestMethod("POST");// 提交模式
		// conn.setConnectTimeout(10000);//连接超时 单位毫秒
		// conn.setReadTimeout(2000);//读取超时 单位毫秒
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取输出流对象
		OutputStream os = conn.getOutputStream();
		String params = JSON.toJSONString(object);
		os.write(params.getBytes("UTF-8"));
		// 获取输入流对象
		InputStream is = conn.getInputStream();
		if (conn.getResponseCode() == 200) {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			StringBuilder sb = new StringBuilder();
			while ((temp = br.readLine()) != null) {
				sb.append(temp);
			}
			Statuss status = JSON.parseObject(sb.toString(), Statuss.class);
			return status;
		} 
		return Statuss.FAILED;
	}
	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String post(String actionUrl, Map<String, String> reportId,
			Map<String, File> file, Locale locale) throws IOException {

		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(5 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("language", locale.getLanguage());
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry : reportId.entrySet()) {
			sb.append(PREFIX);
			sb.append(BOUNDARY);
			sb.append(LINEND);
			sb.append("Content-Disposition: form-data; name=\""
					+ entry.getKey() + "\"" + LINEND);
			sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
			sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
			sb.append(LINEND);
			sb.append(entry.getValue());
			sb.append(LINEND);
		}

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		// 发送文件数据
		if (file != null)
			for (Map.Entry<String, File> files : file.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
						+ files.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(files.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

		// 请求结束标志
		byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
		outStream.write(end_data);
		outStream.flush();
		// 得到响应码
				/* 取得Response内容 */
				InputStream is = conn.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
				
				StringBuffer b = new StringBuffer();
				String line=null;
				while((line = bufferedReader.readLine())!=null){
				     b.append(line);
				}
				outStream.close();
				conn.disconnect();
				return b.toString();

	}
	
	
}
