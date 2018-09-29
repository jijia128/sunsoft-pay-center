package com.central.client.controller;

import com.central.client.bean.User;
import com.central.client.service.TestUserService;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class PayController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TestUserService testUserService;

    /**
     * 获取当前用户
     * @return LoginAppUser
     */
    @GetMapping("/users/current")
    public LoginAppUser getLoginAppUser() {
        return testUserService.getLoginAppUser();
    }

    /**
     * 根据用户ID查找用户
     * @return SysUser
     */
    @GetMapping("/users/{id}")
    public SysUser findUserById(@PathVariable Long id) {
        return testUserService.findUserById(id);
    }

    /**
     * 导出用户
     * @param params
     * @param request
     * @param response
     */
    @PostMapping("/users/exportUser")
    public void exportUser(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        testUserService.exportUser(params);
    }


}
