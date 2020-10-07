package com.company.service.user;

import com.company.dao.BaseDao;
import com.company.dao.user.UserDao;
import com.company.dao.user.UserDaoImpl;
import com.company.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//import org.junit.Test;
//import org.junit.Test;

public class UserServiceImpl implements UserService{
	//业务层都会调用dao层.所以我们要引入Dao层（重点）
	//只处理对应业务
	
	private UserDao userDao;
	public UserServiceImpl(){
		userDao = new UserDaoImpl();
	}
	
	@Override
	public boolean add(User user) {
		// TODO Auto-generated method stub
		
		boolean flag = false;
		Connection connection = null;
		try {
			connection = BaseDao.getConnection();
			connection.setAutoCommit(false);//开启JDBC事务管理
			int updateRows = userDao.add(connection,user);
			connection.commit();
			if(updateRows > 0){
				flag = true;
				System.out.println("add success!");
			}else{
				System.out.println("add failed!");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("rollback==================");
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			//在service层进行connection连接的关闭
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}
	
	public User login(String userCode,String userPassword) {
		// TODO Auto-generated method stub
		Connection connection = null;
		//通过业务层调用对应的具体数据库操作
		User user = null;
		try {
			connection = BaseDao.getConnection();
			user = userDao.getLoginUser(connection, userCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}

		// 匹配密码
		if (null != user) {
			if (!user.getUserPassword().equals(userPassword))
				user = null;
		}
				
		return user;
	}

	@Override
	public List<User> getUserList(String queryUserName,int queryUserRole,int currentPageNo, int pageSize) {
		// TODO Auto-generated method stub
		Connection connection = null;
		List<User> userList = null;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		System.out.println("currentPageNo ---- > " + currentPageNo);
		System.out.println("pageSize ---- > " + pageSize);
		try {
			connection = BaseDao.getConnection();
			userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return userList;
	}
	
	@Override
	public User selectUserCodeExist(String userCode) {
		// TODO Auto-generated method stub
		Connection connection = null;
		User user = null;
		try {
			connection = BaseDao.getConnection();
			user = userDao.getLoginUser(connection, userCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return user;
	}

	@Override
	public boolean deleteUserById(Integer delId) {
		// TODO Auto-generated method stub
		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnection();
			if(userDao.deleteUserById(connection,delId) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}

	@Override
	public User getUserById(String id) {
		// TODO Auto-generated method stub
		User user = null;
		Connection connection = null;
		try{
			connection = BaseDao.getConnection();
			user = userDao.getUserById(connection,id);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			user = null;
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return user;
	}
	
	@Override
	public boolean modify(User user) {
		// TODO Auto-generated method stub
		Connection connection = null;
		boolean flag = false;
		try {
			connection = BaseDao.getConnection();
			if(userDao.modify(connection,user) > 0)
				flag = true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return flag;
	}

	public boolean updatePwd(int id, String password) throws SQLException, Exception {
		// TODO 自动生成的方法存根
		Connection connection = null;
		boolean flag = false;
		//修改密码
		try {
			connection = BaseDao.getConnection();
			if(userDao.updatePwd(connection, id, password) > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			BaseDao.closeResource(connection, null, null);
			
		}
		return flag;
		
		
	}
	
	//查询记录数
	@Override
	public int getUserCount(String queryUserName, int queryUserRole) {
		// TODO Auto-generated method stub
		Connection connection = null;
		int count = 0;
		System.out.println("queryUserName ---- > " + queryUserName);
		System.out.println("queryUserRole ---- > " + queryUserRole);
		try {
			connection = BaseDao.getConnection();
			count = userDao.getUserCount(connection, queryUserName,queryUserRole);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		//System.out.println("count"+count);
		return count;
	}
	
	/*@Test
	public void test() {
		UserServiceImpl userService = new UserServiceImpl();
		int userCount = userService.getUserCount(null,3);
		System.out.println(userCount);
	}
	
	*/

	
}
