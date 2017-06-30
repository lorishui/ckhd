package online;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import me.ckhd.opengame.common.utils.FileUtils;


public class splitSql {
	
	public static void main(String[] args)  {
		try{
			File file = new File("D:\\sql\\sqlResult_1214930.sql");
			File outF = new File("D:\\sql\\out.sql");
			InputStream in = new FileInputStream(file);
			Reader read = new InputStreamReader(in,"utf-8");
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(read);
			StringBuffer sb = new StringBuffer();
			String str =null;
			int i=1;
			String line = System.lineSeparator();
			while( (str=reader.readLine())!= null ){
				/*if( str.indexOf("insert") == -1 ){
					str = str.substring(0,str.lastIndexOf(")")+1)+",";
				}*/
				if( i%10000 == 0 ){
					str = str.substring(0,str.lastIndexOf(")")+1)+";";
					System.out.println(str);
					sb.append(str).append(line);
					FileUtils.write(outF, sb.toString(), "utf-8", true);
					sb = new StringBuffer();
					sb.append("insert into `event_user_role`(`ckAppId`,`childCkAppId`,`ckChannelId`,`childChannelId`,`uuid`,`idfv`,`userId`,`roleId`,`roleName`,`serverId`,`serverName`,`zoneId`,`roleLevel`,`type`,`createDate`) values").append(line);
				}else{
					sb.append(str).append(line);
				}
				i++;
			}
			if(sb.length()>0){
				FileUtils.write(outF, sb.toString(), "utf-8", true);
			}
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}
