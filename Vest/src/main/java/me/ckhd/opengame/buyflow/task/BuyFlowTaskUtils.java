/*
 * ckhd
 */
package me.ckhd.opengame.buyflow.task;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import me.ckhd.opengame.buyflow.callback.AbstractMediaCallback;
import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.buyflow.service.BuyFlowService;
import me.ckhd.opengame.common.utils.MD5Util;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.online.entity.AppDeviceInfo;
import me.ckhd.opengame.online.entity.RoleInfoOnline;
import me.ckhd.opengame.online.service.AppDeviceInfoService;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ckhd 2017-05-03
 */
public class BuyFlowTaskUtils {

	protected static Logger logger = LoggerFactory.getLogger(BuyFlowTaskUtils.class);
	 
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	private static final String INVALID_IOS_DEVICE_ID = "00000000-0000-0000-0000-000000000000";
	
	public static void buyflowHandle(final RoleInfoOnline register) {

		executorService.submit(new Runnable() {

			@Override
			public void run() {
				try {
					BuyFlowService buyFlowService = SpringContextHolder
							.getBean(BuyFlowService.class);

					String ckAppId = register.getCkAppId();
					// iOS/Android子appid分开编码
					String childCkAppId = register.getChildCkAppId();
					// 设备号
					String deviceId = register.getUuid();
					
					int type = 1; // 1：激活
					if (register.getType() == 10) { // 0:设备登陆对应激活；10：新手完成对应注册
						type = 2; // 2：注册
					}
					
					if(INVALID_IOS_DEVICE_ID.equals(deviceId)){
						// 不处理
						return;
					}
					
					if (!DictUtils.getBuyFlowSwitch(ckAppId, childCkAppId)) {
						return;
					}

					BuyFlow buyFlow = new BuyFlow();
					buyFlow.setCkAppId(ckAppId);
					buyFlow.setChildCkAppId(childCkAppId);
					buyFlow.setDeviceId(deviceId);
					buyFlow.setMD5DeviceId(MD5Util.string2MD5(deviceId));
					// 用0查待激活，用1查待注册
					buyFlow.setState(type - 1); // 查询前一个type

					// 激活。24h内,状态是0的记录，更新成激活；角色创建。状态是1的记录，更新成激活
					BuyFlow queryBuyFlow = buyFlowService
							.queryBuyFlow24H(buyFlow);
					if (queryBuyFlow != null) {
						// 激活或注册，0->1, 1->2
						queryBuyFlow.setState(type);
						queryBuyFlow.setDeviceId(deviceId);
						queryBuyFlow.setUpdateDate(new Date());
						buyFlowService.updateState(queryBuyFlow);
						if (type == 2) {
							String media = queryBuyFlow.getMedia();
							
							// update 设备信息增加买量媒体数据 media,ad_item
							AppDeviceInfoService appDeviceInfoService = SpringContextHolder
									.getBean(AppDeviceInfoService.class);
							AppDeviceInfo appDeviceInfo = new AppDeviceInfo();
							appDeviceInfo.setCkAppId(queryBuyFlow.getCkAppId());
							appDeviceInfo.setDeviceId(deviceId);
							appDeviceInfo.setMedia(media);
							appDeviceInfo.setAdItem(queryBuyFlow.getAdItem());
							appDeviceInfo.setUpdateDate(new Date());
							appDeviceInfoService.updateBuyFlow(appDeviceInfo);
							
							if(StringUtils.isNotBlank(queryBuyFlow.getCallback())){
								AbstractMediaCallback callback = SpringContextHolder
										.getBean(media + "Callback");
								callback.callback(queryBuyFlow);
							}
						}
					}
				} catch (Throwable t) {
					logger.error("buy flow biz error", t);
				}
			}
		});
	}

	public static void stopTask() {
		executorService.shutdown();
	}
}
