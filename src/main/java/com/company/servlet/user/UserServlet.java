package com.company.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.company.pojo.Role;
import com.company.pojo.User;
import com.company.service.role.RoleService;
import com.company.service.role.RoleServiceImpl;
import com.company.service.user.UserService;
import com.company.service.user.UserServiceImpl;
import com.company.util.Constants;
import com.company.util.PageSupport;
import com.mysql.cj.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import java.io.Writer;
//import com.mysql.cj.util.StringUtils;

@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		String method = req.getParameter("method");
		if (method.equals( "savepwd") && method != null) {
			this.updatePwd(req, resp);
		}else if (method.equals( "pwdmodify") && method != null) {
			this.pwdModify(req, resp);
		}else if(method.equals( "query") && method != null){
			this.query(req, resp);
		}else if(method != null && method.equals("getrolelist")){
			this.getRoleList(req, resp);
		}else if(method != null && method.equals("ucexist")){
			this.userCodeExist(req, resp);
		}else if(method != null && method.equals("deluser")){
			this.delUser(req, resp);
		}else if(method != null && method.equals("view")){
			this.getUserById(req, resp,"userview.jsp");
		}else if(method != null && method.equals("modifyexe")){
			this.modify(req, resp);
		}
		// 想添加新的增删改查，直接用if(method != "savepwd" && method != null);
		
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO 自动生成的方法存根
		doGet(req, resp);
	}
	
	//重点、难点
	private void query(HttpServletRequest req, HttpServletResponse resp) {
		// TODO 自动生成的方法存根
		//查询用户列表
		//从前端获取数据
		//查询用户列表
		String queryUserName = req.getParameter("queryname");
		String temp = req.getParameter("queryUserRole");
		String pageIndex = req.getParameter("pageIndex");
		int queryUserRole = 0;
		
		//获取用户列表
		UserServiceImpl userService = new UserServiceImpl();
		List<User> userList = null;
		
		//第一此请求肯定是走第一页，页面大小固定的
		//设置页面容量
    	int pageSize = Constants.pageSize;;//把它设置在配置文件里,后面方便修改
    	//当前页码
    	int currentPageNo = 1;
		
		if(queryUserName == null){
			queryUserName = "";
		}
		if(temp != null && !temp.equals("")){
			queryUserRole = Integer.parseInt(temp);
		}
		if(pageIndex != null) {
			currentPageNo = Integer.parseInt(pageIndex);
		}
		//获取用户总数（分页	上一页：下一页的情况）
		//总数量（表）	
    	int totalCount	= userService.getUserCount(queryUserName,queryUserRole);
			
		//总页数支持
		PageSupport pageSupport = new PageSupport();
		pageSupport.setCurrentPageNo(currentPageNo);
		pageSupport.setPageSize(pageSize);
		pageSupport.setTotalCount(totalCount);
		
		int totalPageCount =pageSupport.getTotalPageCount();//总共有几页
		//(totalCount+pageSize-1/pageSize)取整
		// pageSupport.getTotalCount()
		
		//System.out.println("totalCount ="+totalCount);
		//System.out.println("pageSize ="+pageSize);
		//System.out.println("totalPageCount ="+totalPageCount);
		//控制首页和尾页
		//如果页面小于 1 就显示第一页的东西
		if(currentPageNo < 1) {
			currentPageNo = 1;
		}else if(currentPageNo > totalPageCount) {//如果页面大于了最后一页就显示最后一页
			currentPageNo =totalPageCount;
		}
		
		userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
		req.setAttribute("userList", userList);
		
		RoleServiceImpl roleService = new RoleServiceImpl();
		List<Role> roleList = roleService.getRoleList();
		req.setAttribute("roleList", roleList);
		req.setAttribute("totalCount", totalCount); 
		req.setAttribute("currentPageNo", currentPageNo);
		req.setAttribute("totalPageCount", totalPageCount);
		req.setAttribute("queryUserName", queryUserName);
		req.setAttribute("queryUserRole", queryUserRole);
		
		//返回前端
		try {
			req.getRequestDispatcher("userlist.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public void updatePwd(HttpServletRequest req, HttpServletResponse resp) {
		// 通过session获得用户id
		Object o = req.getSession().getAttribute(Constants.USER_SESSION);
		String newpassword = req.getParameter("newpassword");
		boolean flag = false;
		if (o != null && newpassword != null) {
			UserService userService = new UserServiceImpl();

			try {
				flag = userService.updatePwd(((User) o).getId(), newpassword);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if (flag) {
				req.setAttribute("message", "密码修改成功，请退出，使用新密码登录");
				// 密码修改成功,移除session(移除后不能再次修改密码,建议不移除)
				req.getSession().removeAttribute(Constants.USER_SESSION);
			} else {
				// 密码修改失败
				req.setAttribute("message", "密码修改失败");
			}

		} else {
			// 密码修改有问题
			req.setAttribute("message", "新密码有问题");
		}
		try {
			req.getRequestDispatcher("/jsp/pwdmodify.jsp").forward(req, resp);
		} catch (ServletException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
		// 通过session获得用户id
		Object o = req.getSession().getAttribute(Constants.USER_SESSION);
		String oldpassword = req.getParameter("oldpassword");
		//万能Map：结果集
		Map<String,String> resultMap = new HashMap<String,String>();
		if(o==null) {//session失效，session过期了
			resultMap.put("result","sessionerror");
		}else if(StringUtils.isNullOrEmpty(oldpassword)){//输入密码为空
			resultMap.put("result","error");
		}else {//
			String userPassword = ((User)o).getUserPassword();//seesion中的用户密码
			if(oldpassword.equals(userPassword)) {
				resultMap.put("result","true");
			}else {
				resultMap.put("result","false");
			}
		}
		
		
		try {
			resp.setContentType("application/json");
			PrintWriter writer = resp.getWriter();
			/*
			 * resultMap = ["result","sessionerror","result",error]
			 * josn格式={key,value}
			 */
			writer.write(JSONArray.toJSONString(resultMap));
			//writer.write(JsonArray.class.toString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		
	}
	
	@SuppressWarnings("unused")
	private void getPwdByUserId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Object o = request.getSession().getAttribute(Constants.USER_SESSION);
		String oldpassword = request.getParameter("oldpassword");
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(null == o ){//session过期
			resultMap.put("result", "sessionerror");
		}else if(StringUtils.isNullOrEmpty(oldpassword)){//旧密码输入为空
			resultMap.put("result", "error");
		}else{
			String sessionPwd = ((User)o).getUserPassword();
			if(oldpassword.equals(sessionPwd)){
				resultMap.put("result", "true");
			}else{//旧密码输入不正确
				resultMap.put("result", "false");
			}
		}
		
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(resultMap));
		outPrintWriter.flush();
		outPrintWriter.close();
	}

	private void modify(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("uid");
		String userName = request.getParameter("userName");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String userRole = request.getParameter("userRole");
		
		User user = new User();
		user.setId(Integer.valueOf(id));
		user.setUserName(userName);
		user.setGender(Integer.valueOf(gender));
		try {
			user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setPhone(phone);
		user.setAddress(address);
		user.setUserRole(Integer.valueOf(userRole));
		user.setModifyBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		user.setModifyDate(new Date());
		
		UserService userService = new UserServiceImpl();
		if(userService.modify(user)){
			response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
		}else{
			request.getRequestDispatcher("usermodify.jsp").forward(request, response);
		}
	
	}
	
	private void getUserById(HttpServletRequest request, HttpServletResponse response,String url)
			throws ServletException, IOException {
		String id = request.getParameter("uid");
		if(!StringUtils.isNullOrEmpty(id)){
			//调用后台方法得到user对象
			UserService userService = new UserServiceImpl();
			User user = userService.getUserById(id);
			request.setAttribute("user", user);
			request.getRequestDispatcher(url).forward(request, response);
		}
		
	}
	
	private void delUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("uid");
		Integer delId = 0;
		try{
			delId = Integer.parseInt(id);
		}catch (Exception e) {
			// TODO: handle exception
			delId = 0;
		}
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(delId <= 0){
			resultMap.put("delResult", "notexist");
		}else{
			UserService userService = new UserServiceImpl();
			if(userService.deleteUserById(delId)){
				resultMap.put("delResult", "true");
			}else{
				resultMap.put("delResult", "false");
			}
		}
		
		//把resultMap转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(resultMap));
		outPrintWriter.flush();
		outPrintWriter.close();
	}
	
	private void userCodeExist(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//判断用户账号是否可用
		String userCode = request.getParameter("userCode");
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		if(StringUtils.isNullOrEmpty(userCode)){
			//userCode == null || userCode.equals("")
			resultMap.put("userCode", "exist");
		}else{
			UserService userService = new UserServiceImpl();
			User user = userService.selectUserCodeExist(userCode);
			if(null != user){
				resultMap.put("userCode","exist");
			}else{
				resultMap.put("userCode", "notexist");
			}
		}
		
		//把resultMap转为json字符串以json的形式输出
		//配置上下文的输出类型
		response.setContentType("application/json");
		//从response对象中获取往外输出的writer对象
		PrintWriter outPrintWriter = response.getWriter();
		//把resultMap转为json字符串 输出
		outPrintWriter.write(JSONArray.toJSONString(resultMap));
		outPrintWriter.flush();//刷新
		outPrintWriter.close();//关闭流
	}

	private void getRoleList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Role> roleList = null;
		RoleService roleService = new RoleServiceImpl();
		roleList = roleService.getRoleList();
		//把roleList转换成json对象输出
		response.setContentType("application/json");
		PrintWriter outPrintWriter = response.getWriter();
		outPrintWriter.write(JSONArray.toJSONString(roleList));
		outPrintWriter.flush();
		outPrintWriter.close();
	}

	@SuppressWarnings("unused")
	private void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("add()================");
		String userCode = request.getParameter("userCode");
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("userPassword");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String userRole = request.getParameter("userRole");
		
		User user = new User();
		user.setUserCode(userCode);
		user.setUserName(userName);
		user.setUserPassword(userPassword);
		user.setAddress(address);
		try {
			user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		user.setGender(Integer.valueOf(gender));
		user.setPhone(phone);
		user.setUserRole(Integer.valueOf(userRole));
		user.setCreationDate(new Date());
		user.setCreatedBy(((User)request.getSession().getAttribute(Constants.USER_SESSION)).getId());
		
		UserService userService = new UserServiceImpl();
		if(userService.add(user)){
			response.sendRedirect(request.getContextPath()+"/jsp/user.do?method=query");
		}else{
			request.getRequestDispatcher("useradd.jsp").forward(request, response);
		}

	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}
}


