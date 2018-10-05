package com.central.client;

import com.central.client.fallback.UserServiceFallback;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(value = "api-test",configuration = FullLogConfiguration.class)
//@FeignClient(value = "api-test",configuration = DisableHystrixConfigutation.class)
@FeignClient(value = "api-test",fallback = UserServiceFallback.class)
public interface TestUserClient {

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/test/current")
    LoginAppUser getLoginAppUser();

    /**
     * 根据用户ID查找用户
     * @return
     */
    @GetMapping("/test/{id}")
    SysUser findUserById(@PathVariable(value = "id") Long id);

    @GetMapping("/user/{id}")
    public SysUser findById(@PathVariable(value = "id") Long id);

}
