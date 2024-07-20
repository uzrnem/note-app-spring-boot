package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestingClub {
	
	private Integer number;
	private static final Logger LOG = LoggerFactory.getLogger(TestingClub.class);
	
	public TestingClub(Integer number) {
		// TODO Auto-generated constructor stub
		this.number = number;
	}

	public void getNumberInfo() {
		LOG.info("current number is {}", number);
	}
}
