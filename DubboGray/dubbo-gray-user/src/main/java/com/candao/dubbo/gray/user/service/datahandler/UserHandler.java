package com.candao.dubbo.gray.user.service.datahandler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.order.api.IOrderService;
import com.candao.dubbo.gray.order.api.bean.Order;

@Component
public class UserHandler {

	@Resource
	private IOrderService orderService;
	
	public List<Order> getOrder(DistributedContext context){
		return orderService.getOrders(context);
	}
	
}
