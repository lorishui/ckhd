package me.ckhd.opengame.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import me.ckhd.opengame.common.utils.FileUtils;
import me.ckhd.opengame.common.utils.StringUtils;

public class cellinfo_e {
	public static void main(String[] args) {
		String filepath = "D:\\统计\\cellinfo_v2\\cellinfo_v2.sql";
//		String filepath = "C:\\Users\\ASUS\\Desktop\\cell\\99.sql";
		String path = "C:\\Users\\ASUS\\Desktop\\cell2\\";

		File file = FileUtils.getFile(filepath);

		int i = 0;
		int n = 0;
		InputStreamReader read;
		BufferedReader reader = null;
		try {
			read = new InputStreamReader(new FileInputStream(file), "UTF-8");

			reader = new BufferedReader(read);

			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = reader.readLine()) != null) {
				if(str.indexOf("INSERT") != -1){
					str = str.replace("INSERT", "REPLACE");
					System.out.println(str);
					if(StringUtils.isNotBlank(str)){
//						str=str.replace("),", "),\n");
						sb.append(str).append("\n");
					}
					i++;
					if( i%5 == 0){
						System.out.println(path +n%150+ ".sql");
						FileUtils.writeToFile(path +n%150+ ".sql", sb.toString(),"gbk", true);
						sb = new StringBuilder();
						n++;
					}
				}
			}
			System.out.println(path +n%150+ ".sql");
			FileUtils.writeToFile(path +n%150+ ".sql", sb.toString(),"gbk", true);
			sb = new StringBuilder();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
