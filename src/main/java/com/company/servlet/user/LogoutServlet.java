package com.company.servlet.user;


import com.company.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2378036809621026885L;

	public LogoutServlet() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("已退出，请重新登陆" );
		//清除session
		request.getSession().removeAttribute(Constants.USER_SESSION);
		response.sendRedirect(request.getContextPath()+"/login.jsp");//返回登录页面
	}

	public void init() throws ServletException {

	}

}

