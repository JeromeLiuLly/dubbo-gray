package com.dubboclub.dk.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dubboclub.dk.web.bean.Switch;
import com.dubboclub.dk.web.dao.GraySwitchRepository;

@Service
public class GraySwitchService {
	
	@Autowired
	private GraySwitchRepository graySwitchRepository;
	
	public Switch getSwitch(){
		return graySwitchRepository.getOne(1);
	}
	
	public Switch updateSwitch(Switch graySwitch){
		return graySwitchRepository.saveAndFlush(graySwitch);
	}

}
