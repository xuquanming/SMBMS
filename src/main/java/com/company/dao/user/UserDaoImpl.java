package com.company.dao.user;

import com.company.dao.BaseDao;
import com.company.pojo.User;
import com.mysql.cj.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

//import java.sql.SQLException;
//import pojo.Role;

public class UserDaoImpl implements UserDao{
	
	//增加用户信息
	@Override
	public int add(Connection connection, User user) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int updateRows = 0;
		if(null != connection){
			String sql = "insert into smbms_user (userCode,userName,userPassword," +
					"userRole,gender,birthday,phone,address,creationDate,createdBy) " +
					"values(?,?,?,?,?,?,?,?,?,?)";
			Object[] params = {user.getUserCode(),user.getUserName(),user.getUserPassword(),
							user.getUserRole(),user.getGender(),user.getBirthday(),
							user.getPhone(),user.getAddress(),user.getCreationDate(),user.getCreatedBy()};
			updateRows = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return updateRows;
	}
	
	//持久层只做查询数据库的内容
	//得到要登录的用户
	public User getLoginUser(Connection connection, String userCode) throws Exception{
		//准备三个对象
		PreparedStatement pstm = null;
		ResultSet rs = null;
		User user = null;
		//判断是否连接成功
		if(null != connection){
			String sql = "select * from smbms_user where userCode=?";
			Object[] params = {userCode};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			if(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserCode(rs.getString("userCode"));
				user.setUserName(rs.getString("userName"));
				user.setUserPassword(rs.getString("userPassword"));
				user.setGender(rs.getInt("gender"));
				user.setBirthday(rs.getDate("birthday"));
				user.setPhone(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setUserRole(rs.getInt("userRole"));
				user.setCreatedBy(rs.getInt("createdBy"));
				user.setCreationDate(rs.getTimestamp("creationDate"));
				user.setModifyBy(rs.getInt("modifyBy"));
				user.setModifyDate(rs.getTimestamp("modifyDate"));
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return user;
	}

	//通过条件查询-userList
	@Override
	public List<User> getUserList(Connection connection, String userName,int userRole,int currentPageNo, int pageSize)
			throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		ResultSet rs = null;
		List<User> userList = new ArrayList<User>();
		if(connection != null){
			StringBuffer sql = new StringBuffer();
			sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
			List<Object> list = new ArrayList<Object>();
			if(!StringUtils.isNullOrEmpty(userName)){
				sql.append(" and u.userName like ?");
				list.add("%"+userName+"%");
			}
			if(userRole > 0){
				sql.append(" and u.userRole = ?");
				list.add(userRole);
			}
			//在数据库中，分页显示 limit startIndex，pageSize；总数
			//当前页  (当前页-1)*页面大小
			//0,5	1,0	 01234
			//5,5	5,0	 56789
			//10,5	10,0 10~
			sql.append(" order by creationDate DESC limit ?,?");
			currentPageNo = (currentPageNo-1)*pageSize;
			list.add(currentPageNo);
			list.add(pageSize);
			
			Object[] params = list.toArray();
			System.out.println("sql ----> " + sql.toString());
			
			rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
			while(rs.next()){
				User _user = new User();
				_user.setId(rs.getInt("id"));
				_user.setUserCode(rs.getString("userCode"));
				_user.setUserName(rs.getString("userName"));
				_user.setGender(rs.getInt("gender"));
				_user.setBirthday(rs.getDate("birthday"));
				_user.setPhone(rs.getString("phone"));
				_user.setUserRole(rs.getInt("userRole"));
				_user.setUserRoleName(rs.getString("userRoleName"));
				userList.add(_user);
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return userList;
	}
	
	//通过userId删除user
	@Override
	public int deleteUserById(Connection connection,Integer delId) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		int flag = 0;
		if(null != connection){
			String sql = "delete from smbms_user where id=?";
			Object[] params = {delId};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return flag;
	}
	
	//通过userId获取user
	@Override
	public User getUserById(Connection connection, String id) throws Exception {
		// TODO Auto-generated method stub
		User user = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		if(null != connection){
			String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
			Object[] params = {id};
			rs = BaseDao.execute(connection, pstm, rs, sql, params);
			if(rs.next()){
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUserCode(rs.getString("userCode"));
				user.setUserName(rs.getString("userName"));
				user.setUserPassword(rs.getString("userPassword"));
				user.setGender(rs.getInt("gender"));
				user.setBirthday(rs.getDate("birthday"));
				user.setPhone(rs.getString("phone"));
				user.setAddress(rs.getString("address"));
				user.setUserRole(rs.getInt("userRole"));
				user.setCreatedBy(rs.getInt("createdBy"));
				user.setCreationDate(rs.getTimestamp("creationDate"));
				user.setModifyBy(rs.getInt("modifyBy"));
				user.setModifyDate(rs.getTimestamp("modifyDate"));
				user.setUserRoleName(rs.getString("userRoleName"));
			}
			BaseDao.closeResource(null, pstm, rs);
		}
		return user;
	}
	
	//修改用户信息
	@Override
	public int modify(Connection connection, User user) throws Exception {
		// TODO Auto-generated method stub
		int flag = 0;
		PreparedStatement pstm = null;
		if(null != connection){
			String sql = "update smbms_user set userName=?,"+
				"gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
			Object[] params = {user.getUserName(),user.getGender(),user.getBirthday(),
					user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
					user.getModifyDate(),user.getId()};
			flag = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}
		return flag;
	}
	
	//根据用户名或者角色查询用户总数(最难理解的SQL)
	@Override
	public int getUserCount(Connection connection, String userName, int userRole) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int count = 0;
		
		if(connection != null){
			StringBuffer sql = new StringBuffer();
			sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
			List<Object> list = new ArrayList<Object>();//存放我们的参数
			
			if(!StringUtils.isNullOrEmpty(userName)){
				sql.append(" and u.userName like ?");
				list.add("%"+userName+"%");
			}
			
			if(userRole > 0){
				sql.append(" and u.userRole = ?");
				list.add(userRole);
			}
				
				//怎么把List 转成数组
				Object[] params = list.toArray();
				//输出最后的完整语句
				System.out.println("UserDaoImpl->getUserCount:"+sql.toString());
				
				rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
				
				if(rs.next()) {
					count = rs.getInt("count");//从结果集中获取最终数量
					
				}
				BaseDao.closeResource(null, pstm, rs);
				
		}
		return count;
	}
	
	//修改当前用户密码
	@Override//修改当前密码
	public int updatePwd(Connection connection, int id, String password) throws Exception {
		// TODO 自动生成的方法存根
		PreparedStatement pstm = null;
		int execute =0;
		if(connection!=null) {
			String sql = "update smbms_user set  userPassword = ? where id = ?";
			Object[] params = {password,id};
			execute = BaseDao.execute(connection, pstm, sql, params);
			BaseDao.closeResource(null, pstm, null);
		}

		
		return execute;
		
		
				
	}
	
}



