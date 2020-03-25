package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.mapper.ProjectMemberMapper;

@Service
public class ProjectMemberService extends ServiceImpl<ProjectMemberMapper, ProjectMember> {

	/**
	 * 利用project表id查询某个project的所有成员个数
	 * @param projectId
	 * @return 某个project的所有成员个数
	 */
	public int selectCountByProjectId(Integer projectId) {
		return this.baseMapper.selectCountByProjectId(projectId);
	}
	
	/**
	 * 利用project表id查询某个project的所有成员记录
	 * @param projectId
	 * @return
	 */
	public List<ProjectMember> selectByProjectId(Integer projectId) {
		return this.baseMapper.selectByProjectId(projectId);
	}

	/**
	 * 利用project表id和用户id查询某个project的所有成员个数
	 * @param projectId
	 * @param userId
	 * @return某个project的所有成员个数
	 */
	public boolean checkMemberExistByProjectIdAndUserId(Integer projectId, Integer userId) {
		QueryWrapper<ProjectMember> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(ProjectMember::getProjectId, projectId).eq(ProjectMember::getUserId, userId);
		int row = this.baseMapper.selectCount(queryWrapper);
		return row > 0;
	}
	
	/**
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	public List<Integer> selectProjectIdByUserId(Integer userId) {
		return this.baseMapper.selectProjectIdByUserId(userId);
	}
}
