package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pretty.april.achieveitserver.dto.MemberDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.SearchableDTO;
import pretty.april.achieveitserver.dto.SimpleMemberDTO;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.entity.UserRole;
import pretty.april.achieveitserver.mapper.*;
import pretty.april.achieveitserver.model.MemberDetails;
import pretty.april.achieveitserver.model.Searchable;
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
        if (project == null) {
            throw new IllegalArgumentException("Cannot find project");
        }

        if (userMapper.selectById(request.getUserId()) == null) {
            throw new IllegalArgumentException("Cannot find member");
        }

        if (userMapper.selectById(request.getLeaderId()) == null) {
            throw new IllegalArgumentException("Cannot find leader");
        }

        ProjectMember projectMember = new ProjectMember();
        projectMember.setProjectId(projectId);
        projectMember.setProjectName(project.getName());
        projectMember.setUserId(request.getUserId());
        projectMember.setUsername(request.getUsername());
        projectMember.setLeaderId(request.getLeaderId());
        projectMember.setLeaderName(request.getLeaderName());
        projectMemberMapper.insert(projectMember);

        if (!CollectionUtils.isEmpty(request.getRoleId())) {
            List<UserRole> userRoles = request.getRoleId().stream()
                    .map(o -> new UserRole(request.getUserId(), o, projectId)).collect(Collectors.toList());
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
            memberDTOList.add(memberDTO);
        }
        return new PageDTO<>((long) pageNo, (long) pageSize, count, memberDTOList);
    }

    public List<SimpleMemberDTO> getSimpleMembers(Integer projectId) {
        List<ProjectMember> projectMembers = projectMemberMapper.selectList(new QueryWrapper<ProjectMember>()
                .eq("project_id", projectId));
        return projectMembers.stream().map(o -> new SimpleMemberDTO(o.getUserId(), o.getUsername())).collect(Collectors.toList());
    }

    public MemberDTO getMember(Integer projectId, Integer memberId) {
        MemberDetails memberDetails = projectMemberMapper.selectMemberDetailsByProjectIdAndMemberId(projectId, memberId);
        MemberDTO memberDTO = new MemberDTO();
        BeanUtils.copyProperties(memberDetails, memberDTO);
        memberDTO.setWorkingHours(workingHourMapper.selectWorkingHour(memberId, projectId));
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
        if (!CollectionUtils.isEmpty(request.getRoles())) {
            List<UserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<UserRole>()
                    .eq("user_id", memberId)
                    .eq("project_id", projectId));
            Set<Integer> oldRoles = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
            Set<Integer> newRoles = new HashSet<>(request.getRoles());
            Set<Integer> common = new HashSet<>(oldRoles);
            common.retainAll(newRoles);
            oldRoles.removeAll(common);
            newRoles.removeAll(common);
            userRoleMapper.deleteBatch(memberId, projectId, new ArrayList<>(oldRoles));
            userRoleMapper.deleteBatch(memberId, projectId, new ArrayList<>(newRoles));
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

    public List<SearchableDTO> searchMembers(Integer projectId, String name) {
        List<Searchable> searchables = projectMemberMapper.selectLikeName(projectId, name);
        return searchables.stream().map(o -> new SearchableDTO(o.getId(), o.getName())).collect(Collectors.toList());
    }
}
