package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pretty.april.achieveitserver.dto.MemberDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.SimpleMemberDTO;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.*;
import pretty.april.achieveitserver.model.Member;
import pretty.april.achieveitserver.model.MemberDetails;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;
import pretty.april.achieveitserver.request.EditMemberRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private ProjectMemberMapper projectMemberMapper;

    private ProjectMapper projectMapper;

    private UserRoleMapper userRoleMapper;

    private UserMapper userMapper;

    private WorkingHourMapper workingHourMapper;

    public MemberService(ProjectMemberMapper projectMemberMapper, ProjectMapper projectMapper, UserRoleMapper userRoleMapper, UserMapper userMapper, WorkingHourMapper workingHourMapper) {
        this.projectMemberMapper = projectMemberMapper;
        this.projectMapper = projectMapper;
        this.userRoleMapper = userRoleMapper;
        this.userMapper = userMapper;
        this.workingHourMapper = workingHourMapper;
    }

    public void addProjectMember(AddProjectMemberRequest request, Integer projectId) {
        Project project = projectMapper.selectById(projectId);
        User member = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (member == null) {
            throw new IllegalArgumentException("Cannot find member");
        }
        if (projectMemberMapper.selectCount(new QueryWrapper<ProjectMember>()
                .eq("project_id", project)
                .eq("user_id", member.getId())) > 0) {
            throw new IllegalArgumentException("Member already in project");
        }
        User leader = null;
        if (request.getLeaderName() != null) {
            leader = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getLeaderName()));
            if (leader == null) {
                throw new IllegalArgumentException("Cannot find leader");
            }
        }
        if (project == null) {
            throw new IllegalArgumentException("Cannot find project");
        }

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setProjectName(project.getName());
        projectMember.setUserId(member.getId());
        projectMember.setUsername(member.getUsername());
        if (leader != null) {
            projectMember.setLeaderId(leader.getId());
            projectMember.setLeaderName(leader.getUsername());
        }

        projectMemberMapper.insert(projectMember);

        if (!CollectionUtils.isEmpty(request.getRoleId())) {
            List<UserRole> userRoles = request.getRoleId().stream()
                    .map(o -> new UserRole(member.getId(), o, projectId)).collect(Collectors.toList());
            userRoleMapper.insertBatch(userRoles);
        }
    }

    public PageDTO<MemberDTO> getMembers(Integer pageNo, Integer pageSize, Integer projectId, String keyword) {
        Long count = (long) projectMemberMapper.selectMemberCountByProjectId(projectId);
        List<MemberDetails> memberDetailsList = projectMemberMapper.selectMemberDetailsByProjectIdAndNameKeyword(projectId, keyword, pageSize, pageSize * (pageNo - 1));
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for (MemberDetails md : memberDetailsList) {
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(md, memberDTO);
            memberDTO.setWorkingHours(workingHourMapper.selectWorkingHour(md.getUserId(), projectId) / 3600);
            memberDTO.setRoles(userRoleMapper.selectRoleNamesByUserIdAndProjectId(md.getUserId(), projectId));
            User leader = userMapper.selectById(md.getLeaderId());
            if (leader != null) {
                memberDTO.setLeaderUsername(leader.getUsername());
                memberDTO.setLeaderRealName(leader.getRealName());
            }
            memberDTOList.add(memberDTO);
        }
        return new PageDTO<>((long) pageNo, (long) pageSize, count, memberDTOList);
    }

    public MemberDTO getMember(Integer projectId, Integer memberId) {
        MemberDetails memberDetails = projectMemberMapper.selectMemberDetailsByProjectIdAndMemberId(projectId, memberId);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(memberDetails, memberDTO);
        memberDTO.setWorkingHours(workingHourMapper.selectWorkingHour(memberId, projectId) / 3600);
        memberDTO.setRoles(userRoleMapper.selectRoleNamesByUserIdAndProjectId(memberId, projectId));
        return memberDTO;
    }

    public void editMember(Integer memberId, Integer projectId, EditMemberRequest request) {
        if (!StringUtils.isEmpty(request.getLeaderName()) && request.getLeaderId() != null) {
            ProjectMember projectMember = new ProjectMember();
            projectMember.setLeaderId(request.getLeaderId());
            projectMember.setLeaderName(request.getLeaderName());
            projectMemberMapper.update(projectMember, new QueryWrapper<ProjectMember>()
                    .eq("project_id", projectId)
                    .eq("user_id", memberId));
        }
        if (request.getRoles() != null) {
            List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                    .eq("user_id", memberId)
                    .eq("project_id", projectId));
            Set<Integer> oldRoles = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
            Set<Integer> newRoles = new HashSet<>(request.getRoles());
            Set<Integer> common = new HashSet<>(oldRoles);
            common.retainAll(newRoles);
            oldRoles.removeAll(common);
            newRoles.removeAll(common);
            if (!CollectionUtils.isEmpty(oldRoles)) {
                userRoleMapper.deleteBatch(memberId, projectId, new ArrayList<>(oldRoles));
            }
            if (!CollectionUtils.isEmpty(newRoles)) {
                List<UserRole> newUserRoles = newRoles.stream().map(o -> new UserRole(memberId, o, projectId)).collect(Collectors.toList());
                userRoleMapper.insertBatch(newUserRoles);
            }
        }
    }

    public void deleteMember(Integer memberId, Integer projectId) {
        userRoleMapper.delete(new QueryWrapper<UserRole>()
                .eq("user_id", memberId)
                .eq("project_id", projectId));
        projectMemberMapper.delete(new QueryWrapper<ProjectMember>()
                .eq("user_id", memberId)
                .eq("project_id", projectId));
    }

    public List<SimpleMemberDTO> searchMembers(Integer projectId, String keyword) {
        List<Member> members = projectMemberMapper.selectByProjectIdAndRealNameLike(projectId, keyword);
        return members.stream().map(o -> new SimpleMemberDTO(o.getId(), o.getUsername(), o.getRealName())).collect(Collectors.toList());
    }
}
