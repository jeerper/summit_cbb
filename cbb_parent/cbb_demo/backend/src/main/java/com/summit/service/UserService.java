package com.summit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.summit.config.datasource.SwitchDataSource;
import com.summit.dao.DemoDao;
import com.summit.domain.User;

@Service
public class UserService {
	@Autowired
    private DemoDao demoDao;

	@SwitchDataSource("master")
    public List<User> getAllUser1(){
		System.out.println(demoDao.getAllUser1());
        return demoDao.getAllUser1();
    }
    //使用数据源2查询
	@SwitchDataSource("slave")
    public List<User> getAllUser2(){
		System.out.println(demoDao.getAllUser1());
        return demoDao.getAllUser1();
    }

}
