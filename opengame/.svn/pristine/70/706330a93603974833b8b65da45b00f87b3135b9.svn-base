/*
 * ckhd
 */
package me.ckhd.opengame.app.task;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import me.ckhd.opengame.app.web.ProviceCalcComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author qibiao
 *
 */
public class CellInfoStatsTask {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public final ScheduledExecutorService cellinfoStatScheduled = Executors
			.newScheduledThreadPool(1);

	// 单例
	private static class CellInfoStatsTaskHolder {
		private static final CellInfoStatsTask INSTANCE = new CellInfoStatsTask();
	}

	public static CellInfoStatsTask getInstance() {
		return CellInfoStatsTaskHolder.INSTANCE;
	}

	public void start() {

		final ConcurrentMap<String, AtomicLong> statsMap = ProviceCalcComponent.statsMap;

		final Runnable cellinfoStatsLog = new Runnable() {
			public void run() {

				logger.warn("(No warn)stats info, OLD_VERSION num:"
						+ statsMap.get(ProviceCalcComponent.OLD_VERSION).longValue());
				logger.warn("(No warn)stats info, NEW_VERSION_NODATA num:"
						+ statsMap.get(ProviceCalcComponent.NEW_VERSION_NODATA).longValue());
				logger.warn("(No warn)stats info, NEW_VERSION_NOHIT num:"
						+ statsMap.get(ProviceCalcComponent.NEW_VERSION_NOHIT).longValue());
				logger.warn("(No warn)stats info, NEW_VERSION_HIT num:"
						+ statsMap.get(ProviceCalcComponent.NEW_VERSION_HIT).longValue());

			}
		};

		cellinfoStatScheduled.scheduleAtFixedRate(cellinfoStatsLog, 10, 3,
				TimeUnit.MINUTES);

	}

	public void shutdown() {
		cellinfoStatScheduled.shutdown();
	}

}
