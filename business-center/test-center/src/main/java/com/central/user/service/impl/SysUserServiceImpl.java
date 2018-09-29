package com.central.user.service.impl;

import java.util.*;
import com.central.easypoi.user.SysUserExcel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.central.model.user.SysUser;
import com.central.user.dao.SysUserDao;
import com.central.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;

	@Override
	public SysUser findById(Long id) {
		return sysUserDao.findById(id);
	}

	@Override
	public List<SysUserExcel> findAllUsers(Map<String, Object> params) {
		List<SysUserExcel> sysUserExcels = new ArrayList<>();
		List<SysUser> list = sysUserDao.findList(params);

		for (SysUser sysUser : list){
			SysUserExcel sysUserExcel = new SysUserExcel();
			BeanUtils.copyProperties(sysUser,sysUserExcel);
			sysUserExcels.add(sysUserExcel);
		}
		return sysUserExcels;
	}
}
