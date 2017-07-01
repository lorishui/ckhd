package me.ckhd.opengame.online.response.mm;

import java.util.HashMap;
import java.util.Map;

import me.ckhd.opengame.app.utils.IccidUtils;
import me.ckhd.opengame.common.utils.MyJsonUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.ipip.IPUtils;
import me.ckhd.opengame.mmapi.entity.MmAppOrder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.response.BasePayResponse;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;

/**
 * MM服务器下发业务中间接口
 */
public class PayResponse extends BasePayResponse{

	public PayResponse(OnlinePay _onlinePay) {
		super(_onlinePay);
	}

	public static void send(MmAppOrder mmAppOrder){
		
		OnlinePay onlinePay = new OnlinePay();
//		onlinePay.setAccessKey(accessKey);
//		onlinePay.setActualAmount(actualAmount);
//		onlinePay.setAppck(appck);
//		onlinePay.setAppId(mmAppOrder.getAppId());
////		onlinePay.setAppPayContent(appPayContent);
//		onlinePay.setCkAppId(mmAppOrder.getCkappId());
//
//		onlinePay.setChannelId(channelId);
//		onlinePay.setChannelOrderId(channelOrderId);
//		onlinePay.setChannelPayContent(channelPayContent);
//		onlinePay.setCkAppId(ckAppId);
//		onlinePay.setContent(content);
//		onlinePay.setCreateBy(createBy);
//		onlinePay.setCreateDate(createDate);
//		
//		
//		
//		onlinePay.setSqlMap(sqlMap);

		OrderSenderBoot.getInstance().add(onlinePay);
		
	}

	@Override
	public boolean isSuccess() {
		boolean flag=true;
		if(onlinePay!=null){
			result.put("resultCode", 0);
			result.put("errMsg", "");
		} else {
			result.put("resultCode", -1);
			result.put("errMsg", "未获取到支付信息");
			flag = false;
		}
		return flag;
	}

	@Override
	public Map<String, Object> getResult() {
		if (!isSuccess()) {
			return result;
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String exInfo = onlinePay.getAppPayContent();
		Map<String, Object> exInfoMap = MyJsonUtils.jsonStr2Map(exInfo);
		boolean mmextend = false;
		if (exInfoMap != null) {
			String mmSdkName = (String) exInfoMap.get("mmSdkName");
			if (mmSdkName != null) {
				mmextend = true;
				String currPayCode = (String) (onlinePay.getPayCodeConfig()
						.getExInfoMap().get(mmSdkName + "PayCode"));
				
				String ipProvince = IPUtils.getCityCode(StringUtils.getRemoteAddr(onlinePay.getHttpServletRequest()));
				// 计算省份
				String provinceCode = null;
				if(StringUtils.isNotBlank(getSimNo()) && getSimNo().trim().length() >=10){
					provinceCode = IccidUtils.getProvinceCodeBySimNO(getSimNo().trim(), "CMCC");
				}
				if(provinceCode == null || provinceCode.equals("99")){
					provinceCode = ipProvince;
				}
				boolean existCfgPayCode = false;
				if (currPayCode != null) {
					if(currPayCode.length() == 14){
						resultMap.put("orderId", onlinePay.getOrderId());
						resultMap.put("mmAppId", currPayCode.substring(0, 12));
						resultMap.put("payCode", currPayCode.substring(12));
						resultMap.put("ackTimes", onlinePay.getPayMap().get("ackTimes"));
						existCfgPayCode = true;
					}else if(currPayCode.length() > 14){
						try {
							String[] datas = currPayCode.split(";");
							resultMap.put("orderId", onlinePay.getOrderId());
							resultMap.put("mmAppId", currPayCode.substring(0, 12));
							resultMap.put("payCode", currPayCode.substring(12,14));
							resultMap.put("ackTimes", onlinePay.getPayMap().get("ackTimes"));
							if (provinceCode != null && datas.length >= 2) {
								for(int i = 1;i < datas.length;i++){
									String[] iPaycode = datas[i].split(":");
									if(("," + iPaycode[0] + ",").indexOf("," + provinceCode + ",") >= 0){
										resultMap.put("mmAppId", iPaycode[1].substring(0, 12));
										resultMap.put("payCode", iPaycode[1].substring(12));
										break;
									}
								}
							}
							existCfgPayCode = true;
						} catch (Exception e) {
							// 配置数据错误
							logger.error("mm rd cfg error,cfg data is:" + currPayCode);
						}
					}
				}
				if(!existCfgPayCode) {
					result.put("resultCode", -1);
					result.put("errMsg", "未配置计费信息");
				}
			}
		}
		if (!mmextend) {
			resultMap.put("orderId", onlinePay.getOrderId());
			resultMap.put("payCode", (String) (onlinePay.getPayCodeConfig()
					.getExInfoMap().get("payCode")));
			resultMap.put("ackTimes", onlinePay.getPayMap().get("ackTimes"));
		}
		resultMap.put("netIp", onlinePay.getClientIp());
		result.put("result", resultMap);
		return result;
	}
	
}
