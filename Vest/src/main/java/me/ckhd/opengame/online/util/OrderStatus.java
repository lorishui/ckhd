package me.ckhd.opengame.online.util;

/**
 * 订单状态
 * 
 * @author leo
 *
 */
public class OrderStatus {

	//订单状态
	public static final String CREATE_PAYMENT_FAIL = "-1";// 创建订单失败
	public static final String NON_PAYMENT = "0";// 未支付
	public static final String ADVANCE_PAYMENT_SUCCESS = "1";// 预支付成功
	public static final String ADVANCE_PAYMENT_FAIL = "2";// 预支付失败
	public static final String PAY_SUCCESS = "3";// 支付成功
	public static final String PAY_FAIL = "4";// 支付失败
	
	
	//下发状态
	public static final String SEND_DOWN_ING = "1";// 下发中
	public static final String SEND_DOWN_SUCCESS = "2";// 下发成功
	public static final String SEND_DOWN_FAIL = "3";// 下发失败
	public static final String DELIVER_GOODS_SUCCESS = "4";// 发货成功
	public static final String DELIVER_GOODS_FAIL = "5";// 发货失败
}
