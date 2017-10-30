package com.dubboclub.dk.web.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.context.annotation.Lazy;

@Entity
@Table(name="grayswitch")
public class Switch implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8715996860655335947L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "gray_switch")
	private Integer graySwitch;

	public Switch() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGraySwitch() {
		return graySwitch;
	}

	public void setGraySwitch(Integer graySwitch) {
		this.graySwitch = graySwitch;
	}
}
