package me.ckhd.opengame.buyflow.callback;

import me.ckhd.opengame.buyflow.entity.BuyFlow;

import org.springframework.stereotype.Component;

@Component("huichuanCallback")
public class huichuanCallback extends AbstractMediaCallback{
    
    public void callback(BuyFlow buyFlow) {
        super.callback(buyFlow);
    }
    
}
