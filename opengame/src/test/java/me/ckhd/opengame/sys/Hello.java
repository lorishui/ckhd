package me.ckhd.opengame.sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import me.ckhd.opengame.common.utils.FileUtils;

public class Hello {

	public static void main(String[] args) {

		String filepath = "D:\\BaiduYunDownload\\cellinfo_update_201701.txt";

		File file = FileUtils.getFile(filepath);

		String ooo = "insert into `app_account` values ";
		int i = 0;
		InputStreamReader read;
		BufferedReader reader = null;
		try {
			read = new InputStreamReader(new FileInputStream(file), "GBK");

			reader = new BufferedReader(read);

			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = reader.readLine()) != null) {
				if (i == 0 || str.trim().length() == 0) {
					i++;
					continue;
				}
				str = str.replace(" ", "','");
				sb.append(ooo + "(" +str.trim().substring(0, str.length() - 1) + ");\n");
				i++;
			}

			FileUtils.writeToFile(filepath + ".new.sql", sb.toString(),"GBK", true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
