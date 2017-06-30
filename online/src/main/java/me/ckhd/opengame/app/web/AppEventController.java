package me.ckhd.opengame.app.web;

import java.util.Date;

import me.ckhd.opengame.app.entity.AppEvent;
import me.ckhd.opengame.app.service.AppEventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "app/event")
public class AppEventController {

	private Logger log = Logger.getLogger(getClass());
	@Autowired
	private AppEventService eventService;
	
//	@RequiresPermissions("sys:user:view")
	@RequestMapping(value = "")
	@ResponseBody
	public String list(AppEvent event) {
		event.setOperateTime(new Date());
		event.setInstoreTime(new Date());
		event.setCkappId("1001");
		try {
			eventService.saveEvent(event);
		} catch (Exception e) {
			log.error("", e);
			return "false";
		}
		return "true";
	}
}