package com.candao.dubbo.gray.user.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.rpc.RpcContext;
import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.user.api.IUserService;
import com.candao.dubbo.gray.user.api.bean.User;

@Service
public class UserService implements IUserService {

	@Override
	public List<User> getPermissions(DistributedContext distributedContext) {
		List<User> list = new ArrayList<User>();
		User user2 = new User();
		user2.setPhone("13570983345");
		user2.setUserName("刘汉杰");
		user2.setUid(1140120103);
		if (distributedContext.getGrayBean() != null) {
			String value = RpcContext.getContext().getUrl().getParameter(distributedContext.getGrayBean().getGrayKey());
			user2.setNickName("灰度策略：" +distributedContext.getGrayBean().getGrayKey() + " " + value);
		}else{
			user2.setNickName("我是正常服务");
		}
		list.add(user2);
		return list;
	}
}
