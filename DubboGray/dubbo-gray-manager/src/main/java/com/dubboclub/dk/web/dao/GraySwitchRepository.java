package com.dubboclub.dk.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.dubboclub.dk.web.bean.Switch;

public interface GraySwitchRepository extends JpaRepository<Switch, Integer> {

}
