/*
 * 创酷互动® 2016
 */
package me.ckhd.opengame.stats.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.GameReport;
import me.ckhd.opengame.stats.service.GameReportService;
import me.ckhd.opengame.stats.task.GameReportTask;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author qibiao
 * @date 2016-06-13
 */
@RequestMapping(value = "${adminPath}/stats/gamereport")
@Controller
public class GameReportController extends BaseController {
	@Autowired
	private GameReportService gameReportService;
	
	protected static Logger logger = LoggerFactory
			.getLogger(GameReportController.class);
	
	/**
	 * @return
	 */
	@RequestMapping(value = "dailyreport")
	public String dailyReport(GameReport gameReport, Model model, HttpServletRequest request){
		
		// 查询是否已经存在，若正在处理，则显示提示，否则显示生成报表连接
		String ckAppId = gameReport.getCkAppId();
		String date = gameReport.getDate();
		
		if(StringUtils.isNotBlank(ckAppId) && StringUtils.isNotBlank(date)){
			
			if("true".equals(request.getParameter("addTask"))){
				GameReportTask.getInstance().addTask(gameReport);
				model.addAttribute("message", "任务提交成功，请稍后查看统计结果");
			}
			
			// 查询是否存在已经统计好的记录
			List<GameReport> gameReports = gameReportService.findList(gameReport);
			model.addAttribute("gameReports", gameReports);
			model.addAttribute("countTask", gameReportService.countTask(gameReport));
		}
		
		if(StringUtils.isBlank(gameReport.getDate())){
			gameReport.setDate(DateUtils.getDate());
		}
		
		return "modules/stats/gameReport";
	}
	/**
	 * 当前是否存在处理任务
	 * @return
	 */
	@RequestMapping(value = "reportview")
	public String reportView(GameReport gameReport, Model model){
		try{
			GameReport report = gameReportService.findData(gameReport);
			
			model.addAttribute("report", report);
		}catch(Throwable t){
			logger.error("", t);
		}
		return "modules/stats/gameReportDetail";
	}

	public GameReportService getGameReportService() {
		return gameReportService;
	}

	public void setGameReportService(GameReportService gameReportService) {
		this.gameReportService = gameReportService;
	}
	
}