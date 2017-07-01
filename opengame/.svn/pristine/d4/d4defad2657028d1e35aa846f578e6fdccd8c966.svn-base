package me.ckhd.opengame.sys;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import me.ckhd.opengame.common.utils.FileUtils;
import me.ckhd.opengame.woapi.util.MD5;

import org.apache.commons.io.filefilter.IOFileFilter;

import com.alibaba.fastjson.JSONObject;

public class Main {

	public static ExecutorService s = Executors.newFixedThreadPool(10);
	
	static AtomicInteger NUM = new AtomicInteger(0);
	
	public static void main(String[] args) {
		
		IOFileFilter iof = new IOFileFilter(){

			@Override
			public boolean accept(File file) {
				return true;
			}

			@Override
			public boolean accept(File dir, String name) {
				return true;
			}};
		Collection<File> files = FileUtils.listFiles(new File("D:\\aaaa"), iof, iof);
		
		final StringBuffer sb = new StringBuffer();
		int i=0;
		for(final File f:files){
			i++;
	//		System.out.println(f.getName());
			
			Runnable r = new Runnable(){

				@Override
				public void run() {
					long start = System.nanoTime();
					String result = ChaoJiYing.PostPic("linkxie", MD5.MD5Encode("12345678"), "892168", "1104", "4", "0", "false", f.getAbsolutePath());
					System.out.println(f.getAbsolutePath() + "," + (System.nanoTime() - start)/1000);
					NUM.incrementAndGet();
					Map map = (Map)JSONObject.parse(result);
					String  err_no = ""+map.get("err_no");
					String  pic_str = ""+map.get("pic_str");
					String file = f.getAbsolutePath()+"=" + pic_str;
					FileUtils.createFile(file);
//					sb.append(result);
				}
				
			};
			
			s.submit(r);

		}
		while(NUM.intValue() < files.size()){
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		s.shutdown();
	}
	
}
