package com.central.gate.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by owen on 2017/9/10.
 */
@Component
public class AccessFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
       
        try {
        	//获取认证用户
        	Authentication user = SecurityContextHolder.getContext().getAuthentication();
    		if(user==null){
                return null;
    		}
            //解决zuul token 传递问题
            if(user instanceof OAuth2Authentication){
                Authentication athentication = (Authentication)user;
                OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) athentication.getDetails() ;
                ctx.addZuulRequestHeader("Authorization", "bearer "+details.getTokenValue());
            }
          
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }
}
