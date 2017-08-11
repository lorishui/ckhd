package me.ckhd.opengame.ipip;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.ckhd.opengame.common.utils.Constant;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtils {
	
	private final static Logger log = LoggerFactory.getLogger(HttpClientUtils.class);

	/**
	 * @param serverUrl
	 * @param data
	 * @param conTimeout
	 * @param readTimeout
	 * @return
	 */
	public static boolean get(String serverUrl, String fileUrl,int conTimeout, int readTimeout) {
		InputStream in = null;
		FileOutputStream out = null;
		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "*/*; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			in = conn.getInputStream();
			File file = new File(fileUrl);
			if(!file.exists()){
				file.mkdirs();
			}
			String file_url = fileUrl+File.separator+System.currentTimeMillis()+".dat";
			File dat = new File(file_url);
			out = new FileOutputStream(dat);
			byte[] b = new byte[1024];
			int ch = 0; 
			while(( ch = in.read(b)) != -1){
				out.write(b,0,ch);
			}
			in.close();
			out.close();
			//重命名文件
			String old_url = fileUrl+File.separator+Constant.IP_FILE_NAME;
			File oldFile = new File(old_url);
			if(oldFile.exists()){
				oldFile.delete();
			}
			File newFile = new File(file_url);
			boolean isSuccess = newFile.renameTo(new File(old_url));
			log.info(" ip renameTo "+isSuccess);
			return isSuccess;
		} catch (IOException e) {
			log.error("ip io failure", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("ip inputstream close error", e);
				}
			}

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("ip outputstream close error", e);
				}
			}
		}
		return false;
	}
	
	public static String post(String serverUrl, String data,int conTimeout, int readTimeout) {
		StringBuilder responseBuilder = null;
		BufferedReader reader = null;
		OutputStreamWriter wr = null;

		URL url;
		try {
			url = new URL(serverUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); 
			conn.setRequestProperty("Content-Type", "*/*; charset=UTF-8");
			conn.setConnectTimeout(conTimeout);
			conn.setReadTimeout(readTimeout);
			wr = new OutputStreamWriter(conn.getOutputStream());

			wr.write(data);
			wr.flush();

			reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			responseBuilder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line).append("\n");
			}
			return responseBuilder.toString();
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (wr != null) {
				try {
					wr.close();
				} catch (IOException e) {
					log.error("close error", e);
				}
			}

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("close error", e);
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		String url = "https://user.ipip.net/download.php?token=9b789f12f71029ee788156fca458e41647be3810";
		get(url, "D:\\ip", 10000, 10000);
	}
}
