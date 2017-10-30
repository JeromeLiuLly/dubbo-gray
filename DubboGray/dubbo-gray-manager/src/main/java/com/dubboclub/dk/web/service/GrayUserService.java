package com.dubboclub.dk.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dubboclub.dk.web.bean.User;
import com.dubboclub.dk.web.dao.GrayUserRepository;


@Service
public class GrayUserService {
	
	@Autowired
	private GrayUserRepository grayUserRepository;
	
	public List<User> getAllUser(){
		return grayUserRepository.findAll();
	}
	
	public User getUserByUseName(String userName){
		User grayUser = new User();
		grayUser.setStatus(1);
		grayUser.setUserName(userName);
		Example<User> example = Example.of(grayUser);
		
		Sort sort = new Sort(Sort.Direction.DESC, "weight");
		List<User> grayUsers = grayUserRepository.findAll(example,sort);
		
		System.out.println(JSONObject.toJSONString(grayUsers));
		if (grayUsers != null && grayUsers.size() > 0) {
			return grayUsers.get(0);
		}
		return null;
	}
	
	public User getUserById(Integer id){
		return grayUserRepository.getOne(id);
	}
	
	public void deleteUserById(Integer id){
		grayUserRepository.delete(id);
	}
	
	public User updateUser(User grayUser){
		return grayUserRepository.saveAndFlush(grayUser);
	}
	
	public User addUser(User grayUser){
		return grayUserRepository.save(grayUser);
	}

}
