package com.candao.dubbo.gray.entry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.candao.dubbo.gray.entry.service.ExternalService;
import com.candao.dubbo.gray.order.api.bean.Order;
import com.candao.dubbo.gray.user.api.bean.User;

@RestController
@RequestMapping("/external")
public class ExternalController {

	
	@Autowired
	private ExternalService externalService;
	
	@RequestMapping("/user/{id}")
	public List<User> getUser(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getUserById(num);
	}
	
	@RequestMapping("/user2/{id}")
	public List<User> getUser2(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getUserById2(num);
	}
	
	
	@RequestMapping("/user3/{id}")
	public List<User> getUser3(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getUserById3(num);
	}
	
	
	@RequestMapping("/order/{id}")
	public List<Order> getOrder(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getOrderById(num);
	}
	
	@RequestMapping("/order2/{id}")
	public List<Order> geOrder2(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getOrderById2(num);
	}
	
	
	@RequestMapping("/order3/{id}")
	public List<Order> getOrder3(@PathVariable("id")String id){
		Long num = Long.valueOf(id);
		return externalService.getOrderById3(num);
	}
	
}
