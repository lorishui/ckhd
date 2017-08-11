package me.ckhd.opengame.online.version;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ckhd.opengame.app.entity.APPCk;
import me.ckhd.opengame.app.entity.Cfgparam;
import me.ckhd.opengame.app.entity.PayCodeConfig;
import me.ckhd.opengame.app.entity.PayInfoConfig;
import me.ckhd.opengame.app.utils.AppCkUtils;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.utils.SpringContextHolder;
import me.ckhd.opengame.online.entity.OnlinePay;
import me.ckhd.opengame.online.entity.RoleInfoOnline;
import me.ckhd.opengame.online.handle.BaseHandle;
import me.ckhd.opengame.online.sendOrder.task.OrderSenderBoot;
import me.ckhd.opengame.online.service.RoleService;
import me.ckhd.opengame.online.util.OrderStatus;
import me.ckhd.opengame.reyun.task.RenyunTaskBoot;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service("onlineVersion130")
public class OnlineVersion130 extends OnlineVersion110 {

    @Autowired
    public RoleService roleService;

    // pay start
    @Override
    public String pay(JSONObject codeJson, HttpServletRequest request) {
        String engName = codeJson.getString("a");
        JSONObject result = new JSONObject();
        String resultStr = null;
        OnlinePay onlinePay = null;
        try {
            String ckAppid = codeJson.containsKey("ckAppId") ? codeJson.getString("ckAppId") : null;
            String ckChannelId = codeJson.containsKey("ckChannelId") ? codeJson.getString("ckChannelId") : null;
            String productId = codeJson.containsKey("productId") ? codeJson.getString("productId") : null;
            String child_id = codeJson.containsKey("subCkAppId") ? codeJson.getString("subCkAppId") : null;
            // 控制是否开启appstore支付start
            if ("200".equals(ckChannelId) && engName.equals("appstore")) {

                // 支付开关
                String paySwitch = DictUtils.getDictLabel(ckAppid + "," + child_id, "ios_pay_control_switch", null);
                if (paySwitch != null) {
                    String[] paySwitchs = paySwitch.split(";");
                    // ip
                    if (paySwitchs.length >= 1) {
                        String ip = me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(request);
                        for (String disableIp : paySwitchs[0].split(",")) {
                            if (disableIp.equals(ip)) {
                                result.put("resultCode", 3100);
                                result.put("errMsg", "支付控制");
                                throw new Exception("3100");
                            }
                        }
                    }
                    if (paySwitchs.length >= 2) {
                        for (String disableIdfa : paySwitchs[1].split(",")) {
                            if (disableIdfa.equalsIgnoreCase(codeJson.getString("idfa"))) {
                                result.put("resultCode", 3100);
                                result.put("errMsg", "支付控制");
                                throw new Exception("3100");
                            }
                        }
                    }
                }
            }
            // end

            // 是否开启防刷/代充控制
            String controlValue = DictUtils.getDictValue(ckAppid, "charge_pay_other", "0");
            if (controlValue.equals("1")) {
                // 防刷/代充控制start
                if (codeJson.containsKey("idfa")
                        && !"00000000-0000-0000-0000-000000000000".equals(codeJson.getString("idfa"))) {
                    RoleInfoOnline role = new RoleInfoOnline();
                    getRoleInfo(role, codeJson);
                    role = roleService.get(role);
                    if (!codeJson.getString("idfa").equals(role.getUuid())) {
                        result.put("resultCode", 5000);
                        result.put("errMsg", "此账号充值异常，请联系官方客服。");
                        throw new Exception("3002");
                    }
                }
                // 防刷/代充控制end
            }

            if (ckAppid == null || ckChannelId == null || productId == null) {
                result.put("resultCode", 3001);
                result.put("errMsg", "必要参数缺失");
                throw new Exception("3001");
            }
            // 获取游戏信息
            APPCk appck = AppCkUtils.getAppCkByIdAndChild(ckAppid, codeJson.getString("subCkAppId"));
            if (appck == null) {
                result.put("resultCode", 3002);
                result.put("errMsg", "游戏不存在");
                throw new Exception("3002");
            }
            // 获取已配置的计费点信息
            PayCodeConfig payCodeConfig = getPayCodeConfigByChild(codeJson);
            if (payCodeConfig == null) {
                result.put("resultCode", 3003);
                result.put("errMsg", "未配置计费点信息");
                throw new Exception("3003");
            }

            String price = codeJson.containsKey("price") ? codeJson.getString("price") : null;
            // ### 网游的金额使用服务器的配置
            /*
             * if( "141".equals(codeJson.get("payType")) ){ price = payCodeConfig.getPrice(); }
             */
            // 白名单
            String imei = codeJson.containsKey("imei") ? codeJson.getString("imei") : "";
            if (StringUtils.isNotBlank(imei)) {
                String white_imei = DictUtils.getDictLabel(imei, "online_imei_white_list", "");
                if (!"1".equals(white_imei)) {
                    price = payCodeConfig.getPrice();
                }
            } else {
                price = payCodeConfig.getPrice();
            }

            String year = DateUtils.formatDateToStr("YYMMdd");
            // 获取Order id
            Integer orderId = onlineService.getOrderId(ckAppid);
            // 创建订单对象
            onlinePay = getOnliePay(codeJson, orderId, year, appck, price);
            // 获取已配置的支付信息
            PayInfoConfig payInfoConfig = null;
            if (!"141".equals(onlinePay.getPayType())) {
                payInfoConfig = getPayInfoConfigByChild(onlinePay);
            } else {
                payInfoConfig = getPayInfoConfig(onlinePay);
            }
            // 过滤掉配置关闭苹果支付的
            if ("200".equals(ckChannelId) && payInfoConfig != null) {
                String childCkAppId = payInfoConfig.getAppid();
                if (childCkAppId != null) {
                    String[] caArray = childCkAppId.split(",");
                    String childAppId = codeJson.getString("subCkAppId");
                    if (StringUtils.isNotBlank(childAppId)) {
                        for (String key : caArray) {
                            if (key.equals(childAppId)) {
                                result.put("resultCode", 3005);
                                result.put("errMsg", "未配置支付配置信息");
                                throw new Exception("3005");
                            }
                        }
                    }
                }
            }

            onlinePay.setPayCodeConfig(payCodeConfig);
            onlinePay.setPayInfoConfig(payInfoConfig);

            BaseHandle handle = getHandle(engName);
            if (handle != null) {
                onlinePay.setHttpServletRequest(request);
                onlinePay.setClientIp(me.ckhd.opengame.common.utils.StringUtils.getRemoteAddr(onlinePay
                        .getHttpServletRequest()));
                resultStr = handle.pay(onlinePay, codeJson);
            } else {
                result.put("resultCode", 2011);
                result.put("errMsg", "请求的内容不存在!");
            }
        } catch (Throwable e) {
            logger.error("下单失败!!!! ", e);
            if (result.size() == 0) {
                result.put("resultCode", 3004);
                result.put("errMsg", "内部错误");
            }
        } finally {
            if (onlinePay != null) {
                // 将服务器获取到的数据保存到数据库
                onlineService.savePayInfo(onlinePay);
            }
        }
        if (StringUtils.isBlank(resultStr)) {
            resultStr = result.toJSONString();
        }
        logger.info(String.format("支付返回客户端的数据:[%s]", resultStr));
        return resultStr;
    }

    /**
     * 通过bean.name 获取对象
     * 
     * @param className
     * @param code
     * @return
     */
    protected BaseHandle getHandle(String engName) {

        BaseHandle BaseHandle = null;
        try {
            BaseHandle = (BaseHandle) SpringContextHolder.getBean(engName);
        } catch (Throwable e) {
            logger.error("spring获取bean出问题!!!!", e);
        }
        return BaseHandle;
    }

    /**
     * 带上子AppId的配置获取计费点,支付方式
     * 
     * @param condeJson
     * @return
     */
    public PayCodeConfig getPayCodeConfigByChild(JSONObject condeJson) {
        PayCodeConfig payCodeConfig = new PayCodeConfig();
        payCodeConfig.setCkAppId(condeJson.containsKey("ckAppId") ? condeJson.getString("ckAppId") : "");
        payCodeConfig.setChildCkAppId(condeJson.containsKey("subCkAppId") ? condeJson.getString("subCkAppId") : "");
        payCodeConfig.setChannelId(condeJson.containsKey("ckChannelId") ? condeJson.getString("ckChannelId") : "");
        payCodeConfig.setPaytype(condeJson.getString("payType"));
        if (condeJson.containsKey("productId") && StringUtils.isNotBlank(condeJson.getString("productId"))) {
            payCodeConfig.setProductId(condeJson.containsKey("productId") ? condeJson.getString("productId") : "");
            return onlineService.getPayCodeByChild(payCodeConfig);
        } else {
            return null;
        }
    }

    /**
     * 设置角色查询信息
     * 
     * @param role
     * @param json
     */
    public void getRoleInfo(RoleInfoOnline role, JSONObject json) {
        role.setCkAppId(json.getString("ckAppId"));
        role.setRoleId(json.getString("roleId"));
        role.setZoneId(json.getString("zoneId"));
        role.setServerId(json.getString("serverId"));
    }

    /**
     * 谷歌使用 支付参数精确到游戏子id
     */
    @Override
    public String callback(String code, String engName, HttpServletRequest request, HttpServletResponse response) {
        boolean isExistChannelOutId = false;
        JSONObject json = new JSONObject();
        String returnStr = null;
        OnlinePay newPay = null;
        OnlinePay onlinePay = null;
        boolean isSuccess = false;
        try {
            // 处理逻辑
            BaseHandle handle = SpringContextHolder.getBean(engName);
            if (handle == null) {
                logger.info(engName + "提示：地址非法");
                return "地址非法";
            }
            onlinePay = new OnlinePay();
            if ("anzhi".equals(engName)) {
                OnlinePay ob = new OnlinePay();
                ob.setCkAppId(request.getParameter("ckAppId"));
                ob.setChannelId("9");
                ob.setPayType("141");
                PayInfoConfig payInfo = getPayInfoConfig(ob);
                onlinePay.setPayInfoConfig(payInfo);
            }
            handle.parseParamter(code, request, onlinePay);
            if (StringUtils.isNotBlank(onlinePay.getOrderId())) {
                // 百度处理
                if ("baidu".equals(engName)) {
                    newPay = onlineService.getOrderById(onlinePay.getOrderId());
                    if (newPay == null) {
                        newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());
                    }
                } else {
                    newPay = onlineService.getOrderByOrderId(onlinePay.getOrderId());
                }
                // appstore支付，googleplay支付验证
                if (newPay != null && !OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus())) {
                    if (validChannel.containsKey(engName)) {
                        if (StringUtils.isNotBlank(onlinePay.getChannelOrderId())) {
                            OnlinePay validPay = onlineService.getOrderByChannelOrderIdOther(onlinePay
                                    .getChannelOrderId());
                            if (validPay != null) {
                                isExistChannelOutId = true;
                                returnStr = handle.getReturnSuccess();
                                logger.info("提示：订单已经验证！外部订单号[" + newPay.getChannelOrderId() + "] 返回值" + engName + ":"
                                        + returnStr);
                                return returnStr;
                            }
                        }
                    }
                }
            } else if (StringUtils.isNotBlank(onlinePay.getChannelOrderId())) {
                newPay = onlineService.getOrderByChannelOrderIdOther(onlinePay.getChannelOrderId());
                if (newPay == null) {
                    newPay = onlineService.getOrderByPrepayid(onlinePay.getChannelOrderId());
                    if (newPay == null) {
                        logger.info(String.format("外部订单号订为[%s]的订单不存在!", onlinePay.getChannelOrderId()));
                        json.put("resultCode", 4003);
                        json.put("errMsg", "无效订单");
                        logger.info(engName + "提示：无效订单");
                    }
                }
            } else {
                json.put("resultCode", 4004);
                json.put("errMsg", "无法定位记录");
                logger.info(engName + "提示：无法定位记录");
            }
            if (newPay != null) {
                // 已成功的订单处理
                if (OrderStatus.PAY_SUCCESS.equals(newPay.getOrderStatus())) {
                    returnStr = handle.getReturnSuccess();
                    logger.info("提示：订单已经验证！订单号[" + newPay.getOrderId() + "] 返回值" + engName + ":" + returnStr);
                    return returnStr;
                }
                // 获取计费点信息
                PayCodeConfig payCodeConfig = getPayCodeConfig(newPay);
                newPay.setPayCodeConfig(payCodeConfig);
                PayInfoConfig payInfoConfig = null;
                payInfoConfig = getPayInfoConfigByChild(newPay);
                newPay.setPayInfoConfig(payInfoConfig);
                JSONObject result = new JSONObject();
                returnStr = handle.verifyData(newPay, result, response);
                if (result.getInteger("code") == 2000) {
                    newPay.setProductName(payCodeConfig == null ? null : payCodeConfig.getProductName());
                    APPCk appCk = AppCkUtils.getAppCkByIdAndChild(newPay.getCkAppId(), newPay.getChildCkAppId());
                    // 5.判断是否为网游或者是否配置下发地址,如果非网游或者没有设置下发地址则不下发,反之下发到游戏
                    if (StringUtils.isNotBlank(appCk.getPayCallbackUrl())
                            || StringUtils.isNotBlank(newPay.getCallBackUrl())) {
                        newPay.setSercetKey(appCk.getSecretKey());
                        newPay.setIsTest(onlinePay.getIsTest());
                        if (StringUtils.isBlank(newPay.getActualAmount())) {
                            newPay.setActualAmount(onlinePay.getActualAmount());
                        }
                        newPay.setChannelOrderId(onlinePay.getChannelOrderId());
                        newPay.setCallBackContent(onlinePay.getCallBackContent());
                        Map<String, Object> map = handle.getSendOrder(newPay);
                        newPay.setSendNum(Integer.valueOf(map.get("sendNum") == null ? "0" : map.get("sendNum")
                                .toString()));
                        newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
                        newPay.setSendStatus(OrderStatus.SEND_DOWN_ING);
                        newPay.setErrMsg("");
                        newPay.setContent(map.get("content").toString());
                        onlineService.savePayInfo(newPay);
                        isSuccess = true;
                        // 加入发货队列
                        OrderSenderBoot.getInstance().add(newPay);
                    } else {
                        newPay.setOrderStatus(OrderStatus.PAY_SUCCESS);
                        newPay.setErrMsg("");
                        newPay.setIsTest(onlinePay.getIsTest());
                        newPay.setCallBackContent(onlinePay.getCallBackContent());
                        if (StringUtils.isBlank(newPay.getActualAmount())) {
                            newPay.setActualAmount(onlinePay.getActualAmount());
                        }
                        newPay.setChannelOrderId(onlinePay.getChannelOrderId());
                    }
                    // ####热云支付成功报备接口
                    if (StringUtils.isNotBlank(newPay.getChannelId()) && "200".equals(newPay.getChannelId())) {
                        JSONObject task = new JSONObject();
                        /*
                         * if(newPay.getPayInfoConfig()!=null){ task.put("renyunKey",
                         * newPay.getPayInfoConfig().getAppkey()); }
                         */
                        Cfgparam cfgparam = new Cfgparam();
                        cfgparam.setCkAppId(newPay.getCkAppId());
                        cfgparam.setChildCkAppId(newPay.getChildCkAppId());
                        cfgparam.setCkChannelId(newPay.getChannelId());
                        Map<String, Object> resultMap = iOSInitCfgService.getCfg(cfgparam);
                        // reyun
                        if (resultMap != null && resultMap.size() > 0 && resultMap.containsKey("reyun")) {
                            Object obj = resultMap.get("reyun");
                            if (obj != null && obj instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<String, Object> reyun = (Map<String, Object>) obj;
                                if (reyun.containsKey("key")) {
                                    task.put("renyunKey", reyun.get("key"));
                                    task.put("userId", newPay.getUserId());
                                    task.put("currencyAmount", Integer.parseInt(newPay.getActualAmount()) / 100d);
                                    task.put("deviceid", newPay.getDeviceId());
                                    task.put("paymentType", newPay.getChannelId());// 其他的处理传paytype
                                    task.put("orderId", newPay.getOrderId());
                                    RenyunTaskBoot.addTask(task);
                                }
                            }
                        }
                    }
                } else {
                    onlinePay.setOrderStatus(OrderStatus.PAY_FAIL);
                    onlinePay.setCallBackContent(code);
                    returnStr = handle.getReturnFailure();
                    logger.info("提示：验证不通过!订单号[" + newPay.getOrderId() + "] 返回值" + engName + ":" + returnStr);
                }
            }
        } catch (Throwable e) {
            if (json.size() == 0) {
                json.put("resultCode", 4002);
                json.put("errMsg", "内部错误");
            }
            logger.info("支付异常提示：" + json.toJSONString());
            logger.error("支付发生错误：" + e.getMessage(), e);
        } finally {
            // 出错的保存
            if (newPay != null && StringUtils.isNotBlank(newPay.getId()) && !isSuccess) {
                if (StringUtils.isBlank(newPay.getActualAmount())) {
                    newPay.setActualAmount(onlinePay.getActualAmount());
                }
                if (!isExistChannelOutId) {
                    newPay.setCallBackContent(onlinePay.getCallBackContent());
                }
                newPay.setChannelOrderId(onlinePay.getChannelOrderId());
                newPay.setIsTest(onlinePay.getIsTest());
                newPay.setErrMsg(StringUtils.isNotBlank(returnStr) ? returnStr : json.toJSONString());
                onlineService.savePayInfo(newPay);
            }
        }
        if (returnStr == null) {
            returnStr = json.toJSONString();
        }
        logger.info(String.format("返回渠道的数据%s:[%s]", engName, returnStr));
        return returnStr;
    }
}
