package me.ckhd.opengame.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

import org.slf4j.Logger;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


public class FreeMarkerTest {
	 private final Logger logger = org.slf4j.LoggerFactory.getLogger(FreeMarkerTest.class);
     
     private Configuration freemarker_cfg = null; 
    
     public static void main(String[] args){
         //@todo 自己的一个类
         /*Map<String,String> map = new HashMap<String, String>();
         map.put("user", "liupei");
         map.put("name", "刘培");
         //@todo 装入新闻
         //NewsItem = loadNewsItem(1);
            
         FreeMarkerTest test = new FreeMarkerTest();
        
         Map root = new HashMap();
         root.put("user", "liupei");
         root.put("name", "刘培");
         String sGeneFilePath = "/";
        
         String sFileName = "1.htm";
         boolean bOK = test.geneHtmlFile("test.ftl",root, sGeneFilePath,sFileName);*/
        
     }
    
     /**
      * 获取freemarker的配置. freemarker本身支持classpath,目录和从ServletContext获取.
      */
     protected Configuration getFreeMarkerCFG(){
         if (null == freemarker_cfg){
             // Initialize the FreeMarker configuration;
             // - Create a configuration instance
             freemarker_cfg = new Configuration();
             // - FreeMarker支持多种模板装载方式,可以查看API文档,都很简单:路径,根据Servlet上下文,classpath等等
             //htmlskin是放在classpath下的一个目录
             freemarker_cfg.setClassForTemplateLoading(this.getClass(), "/me/ckhd/opengame/template");
         }
         return freemarker_cfg;
     }
     
     /**
      * 生成静态文件.
      *
      * @param templateFileName 模板文件名,相对htmlskin路径,例如"/tpxw/view.ftl"
      * @param propMap 用于处理模板的属性Object映射
      * @param htmlFilePath 要生成的静态文件的路径,相对设置中的根路径,例如 "/tpxw/1/2005/4/"
      * @param htmlFileName 要生成的文件名,例如 "1.htm"
      */
     @SuppressWarnings("rawtypes")
	public boolean geneHtmlFile(String templateFileName,Map propMap, String htmlFilePath,String htmlFileName ){
            //@todo 从配置中取得要静态文件存放的根路径:需要改为自己的属性类调用
//         String sRootDir = "D:/test/" ;
         try{
             Template t = getFreeMarkerCFG().getTemplate(templateFileName);
             //如果根路径存在,则递归创建子目录
             creatDirs(htmlFilePath,htmlFilePath);
             File afile = new File(htmlFilePath + "/" + htmlFileName);
             Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(afile)));
             t.process(propMap, out);
         }catch (TemplateException e){
             logger.error("Error while processing FreeMarker template " + templateFileName,e);
             return false;
         }catch (IOException e){
             logger.error("Error while generate Static Html File " + htmlFileName,e);
             return false;
         }
         return true;
     }
    
    
     /**
      * 创建多级目录
      *
      * @param aParentDir String
      * @param aSubDir  以 / 开头
      * @return boolean 是否成功
      */
     public static boolean creatDirs(String aParentDir, String aSubDir){
         File aFile = new File(aParentDir);
         if (aFile.exists()){
             File aSubFile = new File(aParentDir + aSubDir);
             if (!aSubFile.exists()){
                 return aSubFile.mkdirs();
             }else{
                 return true;
             }
         }else{
             return false;
         }
     }

}
