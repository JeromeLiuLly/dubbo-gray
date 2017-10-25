package com.candao.dubbo.gray.order.api;

import java.util.List;

import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.order.api.bean.Order;

public interface IOrderService {
	
	List<Order> getOrders(DistributedContext context);

}
