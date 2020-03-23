package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import pretty.april.achieveitserver.entity.UserRole;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
	
	/**
	 * 查询某个用户的所有角色
	 * @param userId
	 * @return 用户角色
	 */
	@Select("SELECT role_id FROM user_role WHERE user_id = #{userId}")
	List<Integer> selectByUserId(Integer userId);
}
