package com.central.client.service;


import com.central.client.bean.User;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysUser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component
public class UserServiceFallback implements TestUserService {

    @Override
    public LoginAppUser getLoginAppUser() {
        return new LoginAppUser();
    }

    @Override
    public SysUser findUserById(Long id) {
        return new SysUser();
    }

    @Override
    public void exportUser(Map<String, Object> params) {

    }
}
