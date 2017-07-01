package me.ckhd.opengame.sys.web;

import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.user.utils.RedisClientTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "${adminPath}/sys/redis")
public class RedisController extends BaseController{
	@Autowired
	private RedisClientTemplate redisClientTemplate;
	
	@RequestMapping("del")
	@ResponseBody
	public Object del(String key){
		Object obj = null;
		if(StringUtils.isNotBlank(key)){
			int n = 0;
			obj = redisClientTemplate.get(key);
			if(obj == null){
				n = 1;
				redisClientTemplate.getObject(key);
			}
			if(obj != null){
				if(n==1) {
					redisClientTemplate.delObject(key);
				}else{
					redisClientTemplate.delete(key);
				}
			}
		}
		return obj;
	}
	
	@RequestMapping("jsp")
	public Object html(){
		return "modules/sys/redisJsp";
	}
}
