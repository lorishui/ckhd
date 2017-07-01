package me.ckhd.opengame.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import me.ckhd.opengame.common.utils.FileUtils;

public class cellinfo {
	public static void main(String[] args) {
//		String filepath = "D:\\统计\\cellinfo_v2\\cellinfo_v2.sql";
//		String filepath = "C:\\Users\\ASUS\\Desktop\\cell\\99.sql";
		String filepath = "C:\\Users\\ASUS\\Desktop\\cell1\\";
		String path = "C:\\Users\\ASUS\\Desktop\\cell2\\";
		for(int l=6;l<=99;l++){
			String file1path=filepath+(l+".sql");
			File file = FileUtils.getFile(file1path);

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
						System.out.println(str);
						sb.append(str+"\n");
						i++;
						if( i%5 == 0){
							System.out.println(path +l+(n%2)+ ".sql");
							FileUtils.writeToFile(path +l+(n%2)+ ".sql", sb.toString(),"gbk", true);
							sb = new StringBuilder();
							n++;
						}
				}
				System.out.println(path +l+(n%2)+ ".sql");
				FileUtils.writeToFile(path +l+(n%2)+ ".sql", sb.toString(),"gbk", true);
				sb = new StringBuilder();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
