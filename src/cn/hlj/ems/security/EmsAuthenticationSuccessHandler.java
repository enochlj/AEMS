package cn.hlj.ems.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class EmsAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 为了使 Struts2 的过滤器能够拦截到转发的struts2请求,需要在 web.xml 文件中配置过滤器时, 加入
		// <dispatcher>REQUEST</dispatcher><dispatcher>FORWARD</dispatcher>
		request.getRequestDispatcher("/emp-login").forward(request, response);
	}

}
