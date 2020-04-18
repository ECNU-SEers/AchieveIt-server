package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.UserRoleMapper;

@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

	/**
	 * 根据用户id和角色id检查用户角色是否存在
	 * @param userId
	 * @param roleId
	 * @return true代表存在
	 */
	public boolean checkUserRoleExistByUserIdAndRoleId(Integer userId, Integer roleId) {
		QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(UserRole::getUserId, userId).eq(UserRole::getRoleId, roleId);
		int row = this.baseMapper.selectCount(queryWrapper);
		return row > 0;
	}
	
	/**
	 * 利用用户ID查询某个用户的所有角色
	 * @param userId 用户ID
	 * @return 该用户的所有角色
	 */
	public List<Integer> getUserRoleByUserId(Integer userId) {
		return this.baseMapper.selectByUserId(userId);
	}
	
	/**
     * 查询某个项目中所有的成员
     * @param projectId
     * @return
     */
	public List<Integer> getUserIdsByProjectId(Integer projectId) {
		return this.baseMapper.selectUserIdsByProjectId(projectId);
	}
	
	/**
     * 查询某个项目中某个成员的所有权限
     * @param projectId
     * @param userId
     * @return
     */
	public List<Integer> getPermissionIdsByProjectIdAndUserId(Integer projectId, Integer userId) {
		return this.baseMapper.selectPermissionIdsByProjectIdAndUserId(projectId, userId);
	}
}
