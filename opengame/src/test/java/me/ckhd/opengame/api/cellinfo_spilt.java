package me.ckhd.opengame.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import me.ckhd.opengame.common.utils.FileUtils;
import me.ckhd.opengame.common.utils.StringUtils;

public class cellinfo_spilt {
	
	public static void main(String[] args) {
		String filepath = "D:\\统计\\cellinfo_v2\\cellinfo_v2.sql";
//		String filepath = "C:\\Users\\ASUS\\Desktop\\cell\\99.sql";
		String path = "C:\\Users\\ASUS\\Desktop\\cell1\\";

		File file = FileUtils.getFile(filepath);

		int i = 0;
		int n = 0;
		InputStreamReader read;
		BufferedReader reader = null;
		try {
			read = new InputStreamReader(new FileInputStream(file), "utf-8");

			reader = new BufferedReader(read);

			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = reader.readLine()) != null) {
				if(StringUtils.isNotBlank(str)){
					str=str.replace("),", "),\n");
					sb.append(str).append("\n");
				}
				System.out.println(str);
				i++;
				if( i%5 == 0){
					System.out.println(path +"f"+n%150+ ".sql");
					FileUtils.writeToFile(path +"f"+n%150+ ".sql", sb.toString(),"utf-8", true);
					sb = new StringBuilder();
					n++;
				}
			}
			System.out.println(path +"151"+ ".sql");
			FileUtils.writeToFile(path +"151"+ ".sql", sb.toString(),"utf-8", true);
			sb = new StringBuilder();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
