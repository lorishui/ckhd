package me.ckhd.opengame.reyun.utils;

/** 
 * @ClassName PayTypeEnum
 * @Description 支付方式的枚举
 * @author ASUS
 * @Date 2017年6月7日 上午11:20:52
 * @version 1.0.0
 */
public enum PayTypeEnum {
    
    /**
     * 苹果支付
     */
    APP_STORE("200","appstorepay");
    private String payTypeId;
    private String shortCode;
    
    private PayTypeEnum(String payTypeId, String shortCode) {
        this.payTypeId = payTypeId;
        this.shortCode = shortCode;
    }
    
    /**
     * @Description 根据支付id获取支付的简码
     * @return
     */
    public static String getShortCodeName(String payTypeId){
        for(int i =0;i<PayTypeEnum.values().length;i++){
            if(payTypeId.equals(PayTypeEnum.values()[i].getPayTypeId())){
                return PayTypeEnum.values()[i].getShortCode();
            }
        }
        return "default";
    }
    
    public String getPayTypeId() {
        return payTypeId;
    }
    
    public void setPayTypeId(String payTypeId) {
        this.payTypeId = payTypeId;
    }
    
    public String getShortCode() {
        return shortCode;
    }
  
    public void setShortCOde(String shortCode) {
        this.shortCode = shortCode;
    }
}
