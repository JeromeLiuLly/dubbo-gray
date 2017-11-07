package com.candao.dubbo.gray.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.order.api.IOrderService;
import com.candao.dubbo.gray.order.api.bean.Order;
import com.candao.dubbo.gray.order.service.datahandler.OrderHandler;
import com.candao.dubbo.gray.user.api.bean.User;

@Service
public class OrderService implements IOrderService {
	
	@Resource
	public OrderHandler orderHandler;
	
	@Override
	public List<Order> getOrders(DistributedContext context) {
		Order order = new Order();
		order.setOrderId(UUID.randomUUID().toString());
		
		if (context.getGrayBean() != null) {
			String value = RpcContext.getContext().getUrl().getParameter(context.getGrayBean().getGrayKey());
			if (StringUtils.isEmpty(value)) {
				order.setOrderName("我是order-service1,灰度服务寻找不到,进入正常服务,host:" +RpcContext.getContext().getLocalHost()+":"+RpcContext.getContext().getLocalPort());
			}else{
				order.setOrderName("我是order-service1,灰度策略：" +context.getGrayBean().getGrayKey() + " " + value);
			}
		}else{
			order.setOrderName("我是order-service1,是正常服务,host:" + RpcContext.getContext().getLocalHost()+":"+RpcContext.getContext().getLocalPort());
		}
		
		List<User> userList = orderHandler.getUser(context);
		order.setUser(userList.get(0));
		
		List<Order> list= new ArrayList<Order>();
		list.add(order);
		
		return list;
	}

}
