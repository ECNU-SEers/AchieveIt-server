package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.RolePermission;
import pretty.april.achieveitserver.mapper.RolePermissionMapper;

@Service
public class RolePermissionService extends ServiceImpl<RolePermissionMapper, RolePermission> {

	/**
	 * 根据角色id和权限id检查用户角色对应的权限是否存在
	 * @param roleId
	 * @param permissionId
	 * @return true代表存在
	 */
	public boolean checkRolePermissionExistByRoleIdAndPermissionId(Integer roleId, Integer permissionId) {
		QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(RolePermission::getRoleId, roleId).eq(RolePermission::getPermissionId, permissionId);
		int row = this.baseMapper.selectCount(queryWrapper);
		return row > 0;
	}
	
	/**
	 * 利用角色ID查询某个用户的所有权限
	 * @param roleId 角色ID
	 * @return 该用户的所有权限
	 */
	public List<Integer> getRolePermissionByRoleId(Integer roleId) {
		return this.baseMapper.selectByRoleId(roleId);
	}
}
