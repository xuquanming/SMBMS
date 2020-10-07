package com.company.filter;

import javax.servlet.*;
import java.io.IOException;

//import javax.servlet.*;

public class CharacterEncodingFilter implements Filter {
	
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}


	//@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//response.setContentType("text/html;charset=utf-8");
		chain.doFilter(request, response);
		/*HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		//获取资源名
		String url=request.getRequestURI();
		//如果资源名包含css样式表、js样式表或者png图片就放行
		if(url.contains(".css")||url.contains(".js")||url.contains(".png")) {
			chain.doFilter(request, response);
			return;
		}
		req.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");


		// 放行
		chain.doFilter(request, response);*/
	}
	
	//@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}