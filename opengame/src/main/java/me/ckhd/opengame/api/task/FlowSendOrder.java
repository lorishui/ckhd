package me.ckhd.opengame.api.task;

import me.ckhd.opengame.api.entity.FlowOrderForward;
import me.ckhd.opengame.util.HttpClientUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowSendOrder implements Runnable{

	private final static Logger LOG = LoggerFactory.getLogger(FlowSendOrder.class);
	
	private FlowOrderForward flowOrderForward;
	
	public FlowSendOrder(FlowOrderForward flowOrderForward){
		this.flowOrderForward = flowOrderForward;
	}
	
	@Override
	public void run() {
		// 不做返回结果验证。
		String responseData = HttpClientUtils.post(flowOrderForward.getUrl(), flowOrderForward.getContent(), 2000, 2000);
		// 
		LOG.info(responseData);
	}
	
}
