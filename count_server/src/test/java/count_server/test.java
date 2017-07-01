package count_server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class test {
	
	public static void main(String[] args) throws ParseException {
		/*long start = 1495708740545l;
		long end = 1495708917559l;
		System.out.println(end-start);
		Set<String> set = new HashSet<String>();*/
		
		Date d = new Date();
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d2 = dfm.parse("2017-05-31 08:00:00");
		System.out.println(d.getTime());
		System.out.println(d2.getTime());
		System.out.println(d.getTime());
		System.out.println(d2.getTime());
		System.out.println(System.currentTimeMillis());
		System.out.println((d.getTime()/(3600*1000*24))/365);
		System.out.println((d2.getTime()/(3600*1000*24))/365);
		System.out.println((d.getTime()/((long)3600*1000*24*365)));
		System.out.println((d2.getTime()/((long)3600*1000*24*365)));
	}
}
