package online;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ckhd.opengame.common.utils.DateUtils;

/**
 * 更新online系统对比工具
 * @author ASUS
 *
 */
public class UpdateCompared {
	public static List<String> list = new ArrayList<String>();
	
	public static void main(String[] args) {
//		String url = "D:\\work\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\online";
		//class的獲取
		String url = "D:\\work\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp1\\wtpwebapps\\online\\WEB-INF\\classes";//D:\work\opengame
		String file_url = "D:\\work\\online\\src\\main\\java";
//		String url = "D:\\work\\opengame\\src\\main\\";
		File file = new File(file_url);
		compareDate(file, new Date(),url,file_url);
		for(int i=0;i<list.size();i++){
			System.out.println(list.get(i));
		}
		String des = "D:\\olgame";
		String fileName = "online_"+DateUtils.getDate("yyyyMMddHH")+"\\online\\WEB-INF\\classes";
		des = des+File.separator+fileName;
		File desFIle = new File(des);
		createFile(desFIle, url.length());
	}

	
	public static void compareDate(File file,Date date,String url,String file_url){
		if(file.exists()){
			if(file.isDirectory()){
				File[] arr = file.listFiles();
				for(File f:arr){
					compareDate(f, date, url,file_url);
				}
			}else if(file.isFile()){
//				System.out.println(file.getPath());
				long n = file.lastModified();
				if( date.getTime()-n <= 1*60*60*1000 ){
					String url_str = file.getPath().replace(file_url, url).replace("java", "class");
					String dic_str = url_str.substring(0, url_str.lastIndexOf(File.separator));
					list.add(file.getPath().replace(file_url, url).replace("java", "class"));
					//内部类 匿名类
					File inClassFile = new File(dic_str);
					if(inClassFile.exists()){
						File[] in_arr = inClassFile.listFiles();
						for(File in_f:in_arr){
							if(in_f.isFile()){
								String inName = in_f.getName().substring(0, in_f.getName().lastIndexOf("."));
								String javaName = file.getName().substring(0, file.getName().lastIndexOf("."))+"$";
								if( inName.indexOf(javaName) != -1){
									list.add(in_f.getPath().replace(file_url, url));
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param file
	 * @param length 截取地址长度
	 */
	public static void createFile(File file,int length){
		if(!file.exists()){
			file.mkdirs();
		}
		for(String path:list){
			String filePath = file.getAbsolutePath()+path.substring(length);
			System.out.println(filePath);
			File source = new File(path);
			File desc = new File(filePath);
			File descDic = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
			if(!descDic.exists()){
				descDic.mkdirs();
			}
			if(!desc.exists()){
				try {
					desc.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			FileInputStream in = null;
			FileOutputStream out = null;
			BufferedInputStream buffer = null;
			BufferedOutputStream bufferOut = null;
			try{
				in = new FileInputStream(source);
				out = new FileOutputStream(desc);
				buffer = new BufferedInputStream(in);
				bufferOut = new BufferedOutputStream(out);
				byte[] b = new byte[8192];
				int n=0;
				while((n=buffer.read(b))!=-1){
					bufferOut.write(b, 0, n);
				}
				bufferOut.close();
				buffer.close();
				out.close();
				in.close();
			}catch(Throwable e){
				
			}finally{
				if(bufferOut!=null){
					try {
						bufferOut.close();
					} catch (IOException e) {
					}
				}
				if(buffer!=null){
					try {
						buffer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(out!=null){
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(in!=null){
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
}
