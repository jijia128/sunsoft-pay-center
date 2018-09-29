package com.central.user.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.central.easypoi.user.SysUserExcel;
import com.central.model.common.PageResult;
import com.central.model.common.Result;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysRole;
import com.central.model.user.SysUser;

/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 */
public interface SysUserService {

	SysUser findById(Long id);


	/**
	 * 查询全部用户
	 * @param params
	 * @return
	 */
	List<SysUserExcel> findAllUsers(Map<String, Object> params);

}
