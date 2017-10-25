package com.candao.dubbo.gray.entry.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.common.bean.DistributedContext.GrayBean;
import com.candao.dubbo.gray.order.api.IOrderService;
import com.candao.dubbo.gray.order.api.bean.Order;
import com.candao.dubbo.gray.user.api.IUserService;
import com.candao.dubbo.gray.user.api.bean.User;

@Service
public class ExternalService {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IOrderService orderService;
	
	
	/**
	 * 测试灰度开关关闭状态
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserById(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = false;
		return userService.getPermissions(context);
	}
	
	/**
	 * 测试灰度开关启动,正常用户调用
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserById2(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = true;
		context.grayBean = null; 
		return userService.getPermissions(context);
	}
	
	/**
	 * 测试灰度开关启动,灰度用户调用
	 * 
	 * @param id
	 * @return
	 */
	public List<User> getUserById3(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = true;
		GrayBean gray = new GrayBean();
		gray.setGrayKey("storeId");
		gray.setGrayStrategy(id.intValue());
		gray.setGrayValue("liulianyuan");
		gray.setUserName("liulianyuan");
		
		context.grayBean = gray; 
		return userService.getPermissions(context);
	}
	
	
	/**
	 * 测试灰度开关关闭状态,验证请求是否具有携带性
	 * 
	 * @param id
	 * @return
	 */
	public List<Order> getOrderById(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = false;
		return orderService.getOrders(context);
	}
	
	/**
	 * 测试灰度开关开启状态,正常用户调用,验证请求是否具有携带性
	 * 
	 * @param id
	 * @return
	 */
	public List<Order> getOrderById2(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = true;
		context.grayBean = null; 
		return orderService.getOrders(context);
	}
	
	/**
	 * 测试灰度开关开启状态,灰度用户调用,验证请求是否具有携带性
	 * 
	 * @param id
	 * @return
	 */
	public List<Order> getOrderById3(Long id){
		DistributedContext context = DistributedContext.newInstance();
		context.graySwitch = true;
		GrayBean gray = new GrayBean();
		gray.setGrayKey("storeId");
		gray.setGrayStrategy(id.intValue());
		gray.setGrayValue("liulianyuan");
		gray.setUserName("liulianyuan");
		
		context.grayBean = gray; 
		return orderService.getOrders(context);
	}
	
}
