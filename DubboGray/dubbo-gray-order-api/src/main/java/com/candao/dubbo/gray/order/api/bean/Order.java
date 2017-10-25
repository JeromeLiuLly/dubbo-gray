package com.candao.dubbo.gray.order.api.bean;

import java.io.Serializable;

public class Order implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2630867422747334063L;

	private String orderId;
	
	private String orderName;
	
	private Object user;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Object getUser() {
		return user;
	}

	public void setUser(Object user) {
		this.user = user;
	}
}
