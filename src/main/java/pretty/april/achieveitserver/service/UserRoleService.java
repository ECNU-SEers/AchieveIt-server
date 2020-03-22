package pretty.april.achieveitserver.service;

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
}