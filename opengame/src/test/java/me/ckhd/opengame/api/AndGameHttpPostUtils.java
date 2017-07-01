/*
 */
package me.ckhd.opengame.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AndGameHttpPostUtils {

	void testPost(String urlStr) {
		OutputStreamWriter out = null;
		BufferedReader in = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("POST");// POSTSS
//			con.addRequestProperty("Content-Type", "application/xml");
			con.setRequestProperty("Accept", "application/xml;charset=UTF-8");
//			
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");
			
			out = new OutputStreamWriter(con.getOutputStream());
			String xmlInfo = getXmlInfo();
			System.out.println("urlStr=" + urlStr);
			System.out.println("xmlInfo=" + xmlInfo);
			out.write(new String(xmlInfo.getBytes("UTF-8")));
			out.flush();
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = "";
			for (line = in.readLine(); line != null; line = in.readLine()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private String getXmlInfo() {
		String xml4and = 
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
		"<request> " +
		"<userId>xxx</userId>" + 
		"<contentId>000000000000</contentId>" + 
		"<consumeCode>000000000000</consumeCode>" + 
		"<cpid>701010</cpid>" + 
		"<hRet>0</hRet>" + 
		"<status>1800</status>" + 
		"<versionId>xxx</versionId>" + 
		"<cpparam>xxx</cpparam>" + 
		"<packageID></packageID>" + 
		"</request>";
		
		return xml4and;
	}

	public static void main(String[] args) {
		String url2 = "http://localhost:8080/opengame/ckhd/andiap";
		new AndGameHttpPostUtils().testPost(url2);
	}

}
