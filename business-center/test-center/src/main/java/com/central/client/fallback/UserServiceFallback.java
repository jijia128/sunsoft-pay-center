package com.central.client.fallback;


import com.central.client.TestUserClient;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysUser;
import org.springframework.stereotype.Component;


@Component
public class UserServiceFallback implements TestUserClient {

    @Override
    public LoginAppUser getLoginAppUser() {
        return new LoginAppUser();
    }

    @Override
    public SysUser findUserById(Long id) {
        return new SysUser();
    }

}
