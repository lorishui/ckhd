package me.ckhd.opengame.stats.web;

import java.util.Calendar;
import java.util.List;

import me.ckhd.opengame.common.persistence.Verdict;
import me.ckhd.opengame.common.utils.DateUtils;
import me.ckhd.opengame.common.web.BaseController;
import me.ckhd.opengame.stats.entity.UserFlow;
import me.ckhd.opengame.stats.service.UserFlowService;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "${adminPath}/stats/userFlow")
@Controller
public class UserFlowController extends BaseController{
    
    @Autowired
    private UserFlowService userFlowService;
    
    @RequiresPermissions("stats:appOnlinePay:view")
    @RequestMapping(value = "list")
    public String getOnliePayCunot(UserFlow userFlow,Model model){
        if (!Verdict.isAllow(userFlow.getCkAppId())) {
            return "403";
        }
        String startDate = userFlow.getStartTime();
        String endDate = userFlow.getEndTime();
        //String ckAppId = onlinePay.getCkAppId();
        List<UserFlow> data = null;
        if( StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)){
           data = userFlowService.getCountByChannel(userFlow);
        }else{
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            String startDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
            userFlow.setStartTime(startDateTime);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            String endDateTime = DateUtils.formatDate(c.getTime(),"yyyy-MM-dd HH:mm:ss");
            userFlow.setEndTime(endDateTime);
        }
        model.addAttribute("userFlow",userFlow);
        model.addAttribute("data", data);
        return "modules/stats/userFlow";
    }
    
}
