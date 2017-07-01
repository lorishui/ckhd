package com.chkd.count.count.handle;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.RandomUtils;

import com.alibaba.fastjson.JSONObject;
import com.chkd.count.common.utils.DateUtils;
import com.chkd.count.common.utils.LogUtils;
import com.chkd.count.common.utils.SpringContextHolder;
import com.chkd.count.count.dao.OnlineCurdDataHandle;
import com.mysql.jdbc.StringUtils;

/**
 * 具体的操作
 * @author ASUS
 *
 */
public abstract class BaseHandle {
	public static OnlineCurdDataHandle online = null;
	
	public void beforeInit(){
		if( online == null ){
			online = SpringContextHolder.getBean("curd");
		}
	}
	
	/**
	 * 前置操作
	 * @param json
	 */
	public void beforeHandle(JSONObject json){
	}
	
	/**
	 * 后置操作
	 * @param json
	 */
	public void afterHandle(JSONObject json){
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO t_count_message(id,countId,ErrorCode,ErrorMsg,insertsql,createDate) VALUES ('");
			String id = genrateId();
			sb.append(id).append("','").append(json.getString("type")).append("','")
			.append(json.getString("ErrorCode")).append("','").append(json.getString("ErrorMsg")).append("','")
			.append(json.getString("insertSql")).append("','").append(DateUtils.formatDateToStr("yyyy-MM-dd HH:mm:ss")).append("');");
			online.save(sb.toString());
		}catch(Throwable e){
			LogUtils.log.error("记录数据操作报错", e);
		}
	}
	
	public String genrateId(){
		StringBuffer sb = new StringBuffer();
		sb.append(System.currentTimeMillis());
		int n = RandomUtils.nextInt(999);
		if(n>99){
			sb.append(n);
		}else if(n>9){
			sb.append("0").append(n);
		}else{
			sb.append("00").append(n);
		}
		return sb.toString();
	}
	
	/**
	 * 操作
	 * @param json
	 * @throws Throwable 
	 */
	public void handle(JSONObject json) throws Throwable{
		try{
			LogUtils.log.info("count start time:"+System.currentTimeMillis());
			//获取数据
			List<Map<String,Object>> list = null;
			if(json.containsKey("type") && json.getInteger("type")==1 ){
				list = online.getListData(json.getString("getSql"));
			}else{
				list = online.getOnlineListData(json.getString("getSql"));
			}
			//处理数据
			String lineSeparator = System.getProperty("line.separator", "\n");
	//		List<String> insertSql = new ArrayList<String>();
			if( list!=null && list.size()>0 ){
				StringBuffer sb = new StringBuffer();
				sb.append(json.getString("insertSql")).append(lineSeparator);
				String values = json.getString("values");
				Set<String> set = list.get(0).keySet();
				for(int i=0;i<list.size();i++){
					if( i/10000>0 && i%10000==0 ){
						sb.append(json.getString("insertSql"));
					}
					String value = values;
					for(String key:set){
						String keys = "#{"+key+"}";
						if(StringUtils.isNullOrEmpty((String)list.get(i).get(key))){
							value = value.replace(keys, "''");
						}
						if("String".equals(list.get(i).get(key).getClass().getSimpleName())){
							value = value.replace(keys, "'"+list.get(i).get(key)+"'");
						}
						if("Integer".equals(list.get(i).get(key).getClass().getSimpleName()) ||
								"int".equals(list.get(i).get(key).getClass().getSimpleName())	){
							value = value.replace(keys, list.get(i).get(key)+"");
						}
					}
					if( ((i+1)/10000>0 && (i+1)%10000==0) || i==(list.size()-1) ){
						sb.append(value).append(";").append(lineSeparator);
					}else{
						sb.append(value).append(",").append(lineSeparator);
					}
				}
				LogUtils.log.info("insert sql:"+lineSeparator+sb.toString());
				//插入数据
				online.save(sb.toString());
				json.put("ErrorCode", 2000);
				json.put("ErrorMsg", "SUCCESS");
				json.put("insertSql", sb.toString());
			}else{
				json.put("ErrorCode", 2001);
				json.put("ErrorMsg", "没有筛选的数据");
			}
			LogUtils.log.info("count end time:"+System.currentTimeMillis());
		}catch(Throwable e){
			LogUtils.log.error("统计发生错误！", e);
			json.put("ErrorCode", 2002);
			json.put("ErrorMsg", "统计发生异常");
			throw e;
		}
	}
	
	/**
	 * 执行的方法
	 * @param json
	 * @return
	 */
	public boolean invoke(JSONObject json){
		try{
			beforeHandle(json);
			handle(json);
			afterHandle(json);
			return true;
		}catch(Throwable e){
			afterHandle(json);
			LogUtils.log.error("数据处理出问题", e);
		}
		return false;
	}
	
}
