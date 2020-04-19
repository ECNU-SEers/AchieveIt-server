package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.mapper.ProjectMemberMapper;
import pretty.april.achieveitserver.request.member.RetrieveBasicMemberInfoRequest;

@Service
public class ProjectMemberService extends ServiceImpl<ProjectMemberMapper, ProjectMember> {

	 @Autowired
	 private UserRoleService userRoleService;
	 
	 @Autowired
	 private UserService userService;
	
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
	 * 查询一个用户参与的所有项目
	 * @param userId 用户ID
	 * @return
	 */
	public List<Integer> selectProjectIdByUserId(Integer userId) {
		return this.baseMapper.selectProjectIdByUserId(userId);
	}
	
	/**
	 * 利用项目ID和用户ID查询成员信息
	 * @param projectId
	 * @param userId
	 * @return
	 */
	public ProjectMember selectByProjectIdAndUserId(Integer projectId, Integer userId) {
		return this.baseMapper.selectByProjectIdAndUserId(projectId, userId);
	}
	
	/**
	 * 查询某个项目中具有“查询设备”和“管理设备”权限的成员
	 * @param projectId
	 * @return
	 */
	public List<RetrieveBasicMemberInfoRequest> selectProjectMembersWithDeviceQueryAndManagementPermission(Integer projectId) {
		List<Integer> allUserIds = userRoleService.getUserIdsByProjectId(projectId);
		List<RetrieveBasicMemberInfoRequest> projectMembers = new ArrayList<>();
		for (Integer userId: allUserIds) {
			List<Integer> allUserPermissions = userRoleService.getPermissionIdsByProjectIdAndUserId(projectId, userId);
			if (allUserPermissions.contains(new Integer(14)) && allUserPermissions.contains(new Integer(15))) {
				ProjectMember projectMember = this.selectByProjectIdAndUserId(projectId, userId);
				RetrieveBasicMemberInfoRequest request = new RetrieveBasicMemberInfoRequest();
				request.setUserId(userId);
				request.setUsername(projectMember.getUsername());
				request.setUserRealName(userService.getById(userId).getRealName());
				projectMembers.add(request);
			}
		}
		return projectMembers;
	}
}
