package com.candao.dubbo.gray.user.api;

import java.util.List;

import com.candao.dubbo.gray.common.bean.DistributedContext;
import com.candao.dubbo.gray.user.api.bean.User;

public interface IUserService {
	List<User> getPermissions(DistributedContext distributedContext);
}