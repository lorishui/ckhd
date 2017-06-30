package me.ckhd.opengame.online.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceUtils {
	
	static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	public static void execute(final me.ckhd.opengame.online.handle.common.tencent.OtherRequest other){
		executorService.execute(new Runnable() {		
			public void run() {
				other.getDataByTenCent();
			}
		});
	}
	
	public static void stopTask(){
		executorService.shutdown();
	}
}
