package me.ckhd.opengame.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;

import me.ckhd.opengame.common.utils.FileUtils;

public class PassWord {
	
	public static void main(String[] args) {

		HashSet<String> exist = new HashSet<String>(); 
		String filepath = "C:\\Users\\qibiao\\Desktop\\data04\\";
		
		File file = FileUtils.getFile(filepath + "sqlResult_5300.sql");

		String filename = filepath + System.currentTimeMillis() +"_";

		int i = -1;
		InputStreamReader read;
		BufferedReader reader = null;
		try {
			read = new InputStreamReader(
					new FileInputStream(file), "GBK");
			
			reader = new BufferedReader(read);

//			String cloumn = null;
			String str = null;
			StringBuilder sb = new StringBuilder();
			int index = 0;
			while((str=reader.readLine())!=null){

//				String now = str.substring(0, str.length()-1);
//                if(now.equals(cloumn)){
//                        continue;
//                }else{
//                        cloumn = now;
//                }
				
				index ++;
				if (str.indexOf("insert into ") >= 0){
					if(i > -1){
						sb.setCharAt(sb.length()-1, ';');
						FileUtils.writeToFile(filename + i + ".sql", sb.toString(),"GBK", true);
						sb = new StringBuilder(); 
					}
					i++;
					str = "insert into `app_account`(`imei`,`iccid`,`imsi`,`android_id`,`ip`,`ckappid`,`appid`,`channelid`,`pay_sdk`,`version_name`,`occur_time`,`occur_date`,`last_login_date`,`sign_md5`,`trust_sign`,`insert_time`) values";
				}else{
					String[] x = str.split(",");
					String key = x[0].substring(2,x[0].length()-1) +"_"+x[5].substring(1,x[5].length()-1);
//					if(index % 1000 == 0){
//						System.out.println(key);
//					}
					if(exist.contains(key)){
						continue;
					}
					exist.add(key);
					
				}
				sb.append(str);
				sb.append("\n");
				if(index % 1000 == 0){
					FileUtils.writeToFile(filename + i + ".sql", sb.toString(),"GBK", true);
					sb = new StringBuilder(); 
				}

			}
			if(sb.length() > 0){
				sb.setCharAt(sb.length()-1, ';');
				FileUtils.writeToFile(filename + i + ".sql", sb.toString(),"GBK", true);
			}			
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	
	
}
