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
	
	@RequestMapping("/user/{name}")
	public List<User> getUser(@PathVariable("name")String name){
		return externalService.getUserByName(name);
	}
	
	@RequestMapping("/user2/{name}")
	public List<User> getUser2(@PathVariable("name")String name){
		return externalService.getUserByName2(name);
	}
	
	
	@RequestMapping("/user3/{name}")
	public List<?> getUser3(@PathVariable("name")String name){
		return externalService.getUserByName3(name);
	}
	
	
	@RequestMapping("/order/{name}")
	public List<Order> getOrder(@PathVariable("name")String name){
		return externalService.getOrderByName(name);
	}
	
	@RequestMapping("/order2/{name}")
	public List<Order> geOrder2(@PathVariable("name")String name){
		return externalService.getOrderByName2(name);
	}
	
	
	@RequestMapping("/order3/{name}")
	public List<Order> getOrder3(@PathVariable("name")String name){
		return externalService.getOrderByName3(name);
	}
	
}
