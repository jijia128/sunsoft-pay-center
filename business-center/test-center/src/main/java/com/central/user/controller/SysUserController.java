package com.central.user.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.central.easypoi.user.SysUserExcel;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.central.model.common.PageResult;
import com.central.model.common.Result;
import com.central.model.common.utils.SysUserUtil;
import com.central.model.user.LoginAppUser;
import com.central.model.user.SysRole;
import com.central.model.user.SysUser;
import com.central.user.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月12日 上午22:57:51
 *用户
 */
@Slf4j
@RestController
@Api(tags = "用户模块api")
public class SysUserController {

    @Autowired
    private SysUserService appUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * 当前登录用户 LoginAppUser
     *
     * @return
     */
    @ApiOperation(value = "根据access_token当前登录用户")
    @GetMapping("/users/current")
    public LoginAppUser getLoginAppUser() {
        return SysUserUtil.getLoginAppUser();
    }

    @PreAuthorize("hasAuthority('user:get/users/{id}')")
    @GetMapping("/users/{id}")
    public SysUser findUserById(@PathVariable Long id) {
        return appUserService.findById(id);
    }



    /**
     * 导出数据
     * @return
     */
    @PostMapping("/users/exportUser")
    @PreAuthorize("hasAuthority('user:post/users/exportUser')")
    public void exportUser(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) {
        List<SysUserExcel> result = appUserService.findAllUsers(params);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=myExcel.xls");
        OutputStream ouputStream = null;
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("用户导出","用户"),
                SysUserExcel.class, result );
        try {
            ouputStream = response.getOutputStream();
            workbook.write(ouputStream);
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        } finally {
            try {
                ouputStream.flush();
                ouputStream.close();
            } catch (Exception e) {
                throw new RuntimeException("系统异常");
            }
        }
    }

}
