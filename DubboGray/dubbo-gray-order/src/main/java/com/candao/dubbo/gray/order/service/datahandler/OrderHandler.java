package com.candao.dubbo.gray.order.service.datahandler;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.user.api.IUserService;
import com.candao.dubbo.gray.user.api.bean.User;

@Component
public class OrderHandler {
	
	@Resource
	private IUserService userService;
	
	public List<User> getUser(DistributedContext context){
		return userService.getPermissions(context);
	}

}
