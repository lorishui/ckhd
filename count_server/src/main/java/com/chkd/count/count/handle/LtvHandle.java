package com.chkd.count.count.handle;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;

@Component("ltv")
public class LtvHandle extends BaseHandle {

    public LtvHandle() {
        beforeInit();
    }

    /**
     * imei idfa
     */
    @Override
    public void handle(JSONObject json) {
        StringBuffer sql = new StringBuffer();
        json.put("type", 5);
        try {
            LogUtils.log.info("ltv count start time:" + System.currentTimeMillis());
            LogUtils.log.info("ltv统计开始执行");
            String start = json.getString("start");
            /*
             * if( !StringUtils.isNullOrEmpty(start) ){ start =
             * start.replace("-", ""); }
             */
            int splitNum = 10000;// 分割行数
            // 1.获取ltv的设备数量
            StringBuffer payDeviceSql = new StringBuffer();
            payDeviceSql
                    .append(" SELECT ckAppId,childCkAppId,channelId,childChannelId,deviceId,SUM(a.`actualAmount`) as totalMoney ")
                    .append(" FROM app_online_pay a ").append(" WHERE a.create_date > '").append(start)
                    .append(" 00:00:00'").append(" and a.create_date <= '").append(start).append(" 23:59:59'")
                    .append("  AND a.orderStatus=3 AND a.isTest=0 ")
                    .append(" group by ckAppId,childCkAppId,channelId,childChannelId,deviceId");
            List<Map<String, Object>> payDeviceList = online.getOnlineListData(payDeviceSql.toString());
            String lineSeparato = System.lineSeparator();

            StringBuffer insertSql = new StringBuffer();
            insertSql
                    .append("REPLACE INTO app_device_ltv (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num,money)VALUES");
            int n = 0;
            // 2.获取设备对应的信息
            for (Map<String, Object> map : payDeviceList) {
                String ckAppId = (String) map.get("ckAppId");
                StringBuffer deviceSql = new StringBuffer(
                        "SELECT childCkAppId,ckChannelId,childChannelId,createTime FROM app_device_info WHERE ckAppId = '");
                deviceSql.append(ckAppId).append("' AND deviceId ='").append((String) map.get("deviceId")).append("'");
                List<Map<String, Object>> deviceList = online.getOnlineListData(deviceSql.toString());
                if (deviceList != null && deviceList.size() > 0) {
                    if (n <= splitNum) {
                        Date endTime = DateUtils.parseDateByParsePatterns(start, "yyyy-MM-dd");
                        Timestamp createTime = (Timestamp) deviceList.get(0).get("createTime");
                        String regTime = DateUtils.formatDate(createTime, "yyyy-MM-dd");
                        Date startTime = DateUtils.parseDateByParsePatterns(regTime, "yyyy-MM-dd");
                        long dayNum = DateUtils.getDateDifference(startTime, endTime);
                        if (dayNum >= 0) {
                            insertSql.append("('").append(start.replace("-", "")).append("','")
                                    .append(regTime.replace("-", "")).append("','").append(ckAppId).append("','")
                                    .append(deviceList.get(0).get("childCkAppId")).append("','")
                                    .append(deviceList.get(0).get("ckChannelId")).append("','")
                                    .append(deviceList.get(0).get("childChannelId")).append("','")
                                    .append(map.get("deviceId")).append("',").append(dayNum).append(",")
                                    .append(map.get("totalMoney")).append(")");
                            if (n == splitNum) {
                                insertSql.append(";").append(lineSeparato);
                            } else {
                                insertSql.append(",").append(lineSeparato);
                            }
                            n++;
                        }
                    } else {
                        online.save(insertSql.toString());
                        n = 0;
                        insertSql = new StringBuffer();
                        insertSql
                                .append("REPLACE INTO app_device_ltv (timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,deviceId,num,money)VALUES");
                    }
                }
            }
            if (n > 0 && insertSql.length() > 0) {
                insertSql.setLength(insertSql.length() - (lineSeparato.length() + 1));
                insertSql.append(";");
                online.save(insertSql.toString());// 不够1w补insert
            }
            StringBuffer countRetentionSql = new StringBuffer();
            countRetentionSql
                    .append("REPLACE INTO count_ltv_device(timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,ltvDay,money,num)")
                    .append(" select timeframe,regTime,ckAppId,childAppId,channelId,childChannelId,num,sum(money),count(0) ")
                    .append(" from app_device_ltv ").append(" where timeframe='").append(start.replace("-", ""))
                    .append("'").append(" GROUP BY ckAppId,childAppId,channelId,childChannelId,num,timeframe,regTime ");
            online.save(countRetentionSql.toString());
            LogUtils.log.info("根据设备号统计ltv的语句:" + countRetentionSql.toString());
            LogUtils.log.info("ltv count end time:" + System.currentTimeMillis());
            json.put("ErrorCode", 0);
            json.put("ErrorMsg", "SUCCESS");
            json.put("insertSql", replaceSql(sql.toString()));
        } catch (Throwable e) {
            json.put("ErrorCode", -1);
            json.put("ErrorMsg", "ltv统计失败");
            json.put("insertSql", sql.toString());
            LogUtils.log.error("根据设备号统计的ltv语句失败", e);
        }
    }

    private String replaceSql(String sql) {
        sql = sql.replace("\\", "\\\\");
        sql = sql.replace("'", "\\'");
        sql = sql.replace("\"", "\\\"");
        return sql;
    }

}