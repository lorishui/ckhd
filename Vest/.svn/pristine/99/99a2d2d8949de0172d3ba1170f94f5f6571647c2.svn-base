package me.ckhd.opengame.buyflow.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.util.HttpClientUtils;

public abstract class AbstractMediaCallback {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public void callback(BuyFlow buyFlow) {
		if(StringUtils.isNotBlank(buyFlow.getCallback())){
			try {
				String content = HttpClientUtils.doGet(buyFlow.getCallback(), "", null);
				logger.info("返回的信息：" + content);
			} catch (Throwable t) {
				logger.error(getClass().getName(), t);
			}
		}
	}
}
