package me.ckhd.opengame.online.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.ckhd.opengame.online.request.tencent.OtherRequest;

public class ExecutorServiceUtils {
	static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*2);
	
	public static void execute(final OtherRequest other){
		executorService.execute(new Runnable() {		
			@Override
			public void run() {
				other.getDataByTenCent();
			}
		});
	}
	
	public static void stopTask(){
		executorService.shutdown();
	}
}
