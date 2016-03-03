package cn.hlj.ems.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class EmsAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		System.out.println(exception);
		//username或password有误时产生的异常
		//org.springframework.security.authentication.BadCredentialsException: Bad credentials
		//enabled值为false时产生的异常
		//org.springframework.security.authentication.DisabledException: User is disabled
		//accountNonExpired值为false时产生的异常
		//org.springframework.security.authentication.AccountExpiredException: User account has expired
		
		//如果产生了异常，则将异常对象放入到请求域中
		if(exception!=null) {
			request.setAttribute("exception", exception);
		}
		request.getRequestDispatcher("/emp-login").forward(request, response);
	}

}
