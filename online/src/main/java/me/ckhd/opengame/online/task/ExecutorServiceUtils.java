package me.ckhd.opengame.online.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.ckhd.opengame.util.LoghubUtils;
import me.ckhd.opengame.util.LoghubUtils.IExecute;

public class ExecutorServiceUtils {
	
	static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	public static void execute(final me.ckhd.opengame.online.handle.common.tencent.OtherRequest other){
		executorService.execute(new Runnable() {		
			public void run() {
				LoghubUtils.executeBackgroundTask(new IExecute<Void>(){
					public Void execute(List<String> message) {
						other.getDataByTenCent();
						return null;
					}

					public void log(String message) {
						LoghubUtils.getBackgroundTasklogger().debug(message);
					}
				});
			}
		});
	}
	
	public static void stopTask(){
		executorService.shutdown();
	}
}
