package com.dubboclub.dk.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dubboclub.dk.web.bean.GrayStrategy;

public interface GrayStrategyRepository extends JpaRepository<GrayStrategy, Integer> {
	
}
