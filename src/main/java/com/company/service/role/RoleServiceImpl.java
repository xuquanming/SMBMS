package com.company.service.role;

import java.sql.Connection;
import java.util.List;

import com.company.dao.BaseDao;
import com.company.dao.role.RoleDao;
import com.company.dao.role.RoleDaoImpl;
import com.company.pojo.Role;

public class RoleServiceImpl implements RoleService{
	
	private RoleDao roleDao;
	
	public RoleServiceImpl(){
		roleDao = new RoleDaoImpl();
	}
	
	@Override
	public List<Role> getRoleList() {
		Connection connection = null;
		List<Role> roleList = null;
		try {
			connection = BaseDao.getConnection();
			roleList = roleDao.getRoleList(connection);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			BaseDao.closeResource(connection, null, null);
		}
		return roleList;
	}
	
}