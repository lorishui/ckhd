package me.ckhd.opengame.reyun.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import me.ckhd.opengame.common.utils.SendMailUtil;
import me.ckhd.opengame.common.utils.SpringContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName RenyunTask
 * @Description 热云的任务发送启动器
 * @author ASUS
 * @Date 2017年6月7日 下午2:07:51
 * @version 1.0.0
 */
public class RenyunTaskBoot implements Runnable{
    
    private static Logger log = LoggerFactory.getLogger(RenyunTaskBoot.class.getName());
    /**
     * 队列任务书的的最大数量
     */
    private static final int QUEUE_MAX_NUM = 1024;
    /**
     * 任务处理线程池
     */
    private static final ExecutorService taskService = Executors.newFixedThreadPool(4);
    /**
     * 任务队列
     */
    private static final BlockingQueue<JSONObject> queue = new LinkedBlockingQueue<JSONObject>(QUEUE_MAX_NUM);
    /**
     * 热云事件处理实现类实例
     */
    private static final RenyunSDKImpl renyunSDKimpl = SpringContextHolder.getBean("RenyunSDKImpl");
    
    /**
     * 判断是否停止
     */
    private static AtomicBoolean stop = new AtomicBoolean(false);
    
    private JSONObject data;
    
    /**
     * @Description 任务加入处理队列
     * @param task
     */
    public static void addTask(JSONObject task){
        boolean isSuccess = queue.offer(task);
        if(!isSuccess){//加入队列失败，说明队列已满，发送到对应的负责人
            SendMailUtil.sendCommonMail("729478581@qq.com", "热云队列满了", "处理或请求存在问题请知悉！");
        }
    }
    
    /**
     * 任务处理线程
     */
    public void run() {
        try{
            Integer num = renyunSDKimpl.payment(data);
            if (num.equals(2)) {
                Integer n = 1;
                if (data.containsKey("sendRenyunNum")) {
                    n = data.getInteger("sendRenyunNum");
                }
                data.put("sendRenyunNum", n);
                if(n<4){
                    queue.offer(data);
                }
            }
        }catch(Throwable e){
            log.error("热云数据上报出错:"+data.toJSONString(), e);
        }
    }
    
    /** 
     * @Description 任务启动
     */
    public static void startUp(){
        log.info("reyun startUp");
        taskService.execute(new Thread(){
            @Override
            public void run() {
                JSONObject json = null;
                try {
                    while (true) {
                        json = queue.poll(15, TimeUnit.SECONDS);
                        if(stop.get()){
                            break;
                        }
                        if (json!=null && json.size()>0) {
                            RenyunTaskBoot renyun = new RenyunTaskBoot();
                            renyun.setData(json);
                            taskService.execute(renyun);
                        }
                    }
                } catch (InterruptedException e) {
                    log.error("热云数据上报接口启动失败", e);
                }
            }
        });
    }

    /** 
     * @Description 任务启动
     */
    public static void shutdown(){
        stop.set(true);//停止循环
        taskService.shutdown();
        log.info("reyun shutdown");
    }
    
    public JSONObject getData() {
        return data;
    }

    
    public void setData(JSONObject data) {
        this.data = data;
    }
    
}
