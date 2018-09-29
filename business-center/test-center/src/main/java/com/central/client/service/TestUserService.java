package com.central.client.service;

import com.central.client.config.FullLogConfiguration;
import com.central.client.bean.User;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

//@FeignClient(value = "api-test",configuration = FullLogConfiguration.class)
//@FeignClient(value = "api-test",configuration = DisableHystrixConfigutation.class)
@FeignClient(value = "api-test",fallback = UserServiceFallback.class)
public interface TestUserService {

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/users/current")
    LoginAppUser getLoginAppUser();

    /**
     * 根据用户ID查找用户
     * @return
     */
    @GetMapping("/users/{id}")
    SysUser findUserById(@PathVariable(value="id") Long id);

    /**
     * 导出用户
     * @param params
     */
    @PostMapping("/users/exportUser")
    void exportUser(@RequestParam Map<String, Object> params);


}
