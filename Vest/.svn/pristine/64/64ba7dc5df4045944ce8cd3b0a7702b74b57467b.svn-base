package me.ckhd.opengame.buyflow.web;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.buyflow.entity.BuyFlow;
import me.ckhd.opengame.buyflow.service.BuyFlowService;
import me.ckhd.opengame.common.utils.CoderUtils;
import me.ckhd.opengame.common.utils.StringUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.sys.utils.DictUtils;

import org.apache.shiro.codec.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "buyflow")
@Controller
public class GdtBuyFlowController extends BaseController {

	private static final String GDT = "gdt";
	private static final String CONV_TYPE = "MOBILEAPP_ACTIVITE";
	private static final String CALLBACK = "http://t.gdt.qq.com/conv/app/%s/conv?";
	
	@Autowired
	private BuyFlowService buyFlowService;
	
	/**
	 * 生成feedback url http://www.example.com/gdt.cgi 
	 * 广点通请求链接：
	 * 安卓  http://www.example.com/gdt.cgi?muid=40c7084b4845eebce9d07b8a18a055fc&click_time=1406276499
	 * 			&appid=000000&click_id=007210548a030059ccdfd1d4&app_type=android&advertiser_id=20000
	 * 
	 * ios http://www.example.com/gdt.cgi?muid=40c7084b4845eebce9d07b8a18a055fc&click_time=1406276499
	 * 			&appid=000000&click_id=007210548a030059ccdfd1d4&app_type=ios&advertiser_id=20000
	 * 
	 * callback  http://t.gdt.qq.com/conv/app/{appid}/conv?v={data}&conv_type={conv_type}&app_type={app_type}&advertiser_id={uid}
	 *
	 * @return
	 */
	@RequestMapping("gdt/{appParam}")
	@ResponseBody
	public String handle(@PathVariable String appParam,@RequestParam String muid,@RequestParam long click_time,
			@RequestParam String appid,@RequestParam String click_id, @RequestParam String app_type, @RequestParam String advertiser_id,
			HttpServletRequest request) {

		try {
			if(StringUtils.isBlank(muid)){
				return "{\"ret\":0,\"msg\":\"success\"}";
			}
			String[] appParams = appParam.split(",");

			BuyFlow buyFlow = new BuyFlow();
			buyFlow.setCkAppId(appParams[0]);
			buyFlow.setChildCkAppId(appParams[1]);
			if(appParams.length>=3){
				buyFlow.setAdItem(appParams[2]);
			}
			buyFlow.setMedia(GDT);
			buyFlow.setDeviceId(muid);
			buyFlow.setMD5DeviceId(muid);
			// ip
			buyFlow.setIp(StringUtils.getRemoteAddr(request));
			buyFlow.setMonitorTime(new Date(click_time*1000));
			
			
			//组装callback
			String encrypt_key = DictUtils.getDictValue("gdt_encrypt_"+advertiser_id,"buy_flow_key", "");
			String sign_key = DictUtils.getDictValue("gdt_sign_"+advertiser_id, "buy_flow_key", "");
			StringBuilder callback = new StringBuilder(String.format(CALLBACK, appid));
			StringBuilder queryString = new StringBuilder("click_id="+click_id+"&muid="+muid+"&conv_time="+System.currentTimeMillis()/1000);
			
			
			String encodePage = URLEncoder.encode(callback.toString()+queryString.toString(),"utf-8");
			String signature = CoderUtils.md5(sign_key+"&GET&"+encodePage, "utf-8");
			String base_data = queryString.append("&sign="+URLEncoder.encode(signature, "utf-8")).toString();
			String data = Base64.encodeToString(SimpleXor(base_data,encrypt_key).getBytes("utf-8"));
			
			callback.append("v=").append(URLEncoder.encode(data,"utf-8"))
					.append("&conv_type=").append(CONV_TYPE)
					.append("&app_type=").append(app_type.toUpperCase())
					.append("&advertiser_id=").append(advertiser_id);
			buyFlow.setCallback(callback.toString());
			
			
			if (!buyFlowService.existBuyFlow(buyFlow)) {
				buyFlow.setState(BuyFlow.STATE.NEW.getValue());
			} else {
				buyFlow.setState(BuyFlow.STATE.OLDREGISTER.getValue());
			}
			
			buyFlowService.save(buyFlow);
		} catch (Throwable t) {
			logger.error(GDT, t);
		}
		return "{\"ret\":0,\"msg\":\"success\"}";
	}

	private static String SimpleXor(String base_data, String encrypt_key) {
		char[] source = base_data.toCharArray();
		char[] key = encrypt_key.toCharArray();
		for (int i=0,j=0; i<source.length ; i++) {
			source[i] = (char)(source[i] ^ key[j]);
			j++;
			j = j % (key.length);
		}
		return new String(source);
		
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String page = "http://t.gdt.qq.com/conv/app/112233/conv?muid=0f074dc8e1f0547310e729032ac0730b&conv_"
					+"time=1422263664&client_ip=10.11.12.13";
		
		String property = "test_sign_key&GET&"+URLEncoder.encode(page, "utf-8");
		String signature = CoderUtils.md5(property, "UTF-8");
		
		String queryString = "muid=0f074dc8e1f0547310e729032ac0730b&conv_time=1422263664&client_ip=10.11.12.13";
		
		String baseData = queryString +"&sign=" + signature;
		
		byte[] data = Base64.encode(SimpleXor(baseData, "test_encrypt_key").getBytes());
		
		System.out.println("GRAaEGJVCFNFTRQXZw5UH0RQR0NsVF4GRUtJRGxZBBpEUkBEPUMNDBwPLwA2BgBERVFBRm1TXVVETVYXMwIAFwA6GgRiVF5NQ0heRW1FVEpSFhoTMVhWAkYdRxJqWFdKEgFKRT1WWVdBSRRCbFIATxJSQENuBw==".equals(data));		
		
	}
}
