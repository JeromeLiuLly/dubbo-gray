package com.dubboclub.dk.web.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dubboclub.dk.web.bean.User;

public interface GrayUserRepository extends JpaRepository<User, Integer> {

}
