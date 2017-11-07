package com.candao.dubbo.gray.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.RpcContext;
import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.order.api.bean.Order;
import com.candao.dubbo.gray.user.api.IUserService;
import com.candao.dubbo.gray.user.api.bean.User;
import com.candao.dubbo.gray.user.service.datahandler.UserHandler;

@Service
public class UserService implements IUserService {
	
	@Resource
	public UserHandler userHandler;

	@Override
	public List<User> getPermissions(DistributedContext distributedContext) {
		List<User> list = new ArrayList<User>();
		
		User user2 = new User();
		user2.setPhone("135");
		user2.setUserName("刘练源");
		user2.setUid(1140120103);
		if (distributedContext.getGrayBean() != null) {
			String value = RpcContext.getContext().getUrl().getParameter(distributedContext.getGrayBean().getGrayKey());
			if (StringUtils.isEmpty(value)) {
				user2.setNickName("我是user-service2,灰度服务寻找不到,进入正常服务,host:" + RpcContext.getContext().getLocalHost()+":"+RpcContext.getContext().getLocalPort());
			}else{
				user2.setNickName("我是user-service2,灰度策略：" +distributedContext.getGrayBean().getGrayKey() + " " + value);
			}
		}else{
			user2.setNickName("我是user-service2,我是正常服务,host:" + RpcContext.getContext().getLocalHost()+":"+RpcContext.getContext().getLocalPort());
		}
		list.add(user2);
		return list;
	}

	@Override
	public List<?> getPermissionsWithOrder(DistributedContext distributedContext) {
		List<Order> list = userHandler.getOrder(distributedContext);
		return list;
	}
}
