package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.RiskDTO;
import pretty.april.achieveitserver.dto.SearchableDTO;
import pretty.april.achieveitserver.dto.UsernameDTO;
import pretty.april.achieveitserver.entity.OrgStdRisk;
import pretty.april.achieveitserver.entity.ProjectRisk;
import pretty.april.achieveitserver.entity.RiskRelatedPerson;
import pretty.april.achieveitserver.enums.RiskState;
import pretty.april.achieveitserver.enums.RiskType;
import pretty.april.achieveitserver.mapper.OrgStdRiskMapper;
import pretty.april.achieveitserver.mapper.RiskMapper;
import pretty.april.achieveitserver.mapper.RiskRelatedPersonMapper;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.model.Searchable;
import pretty.april.achieveitserver.model.Username;
import pretty.april.achieveitserver.request.AddRiskRequest;
import pretty.april.achieveitserver.request.EditRiskRequest;
import pretty.april.achieveitserver.request.ImportRiskRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiskService extends ServiceImpl<RiskMapper, ProjectRisk> {

    private RiskMapper riskMapper;

    private UserMapper userMapper;

    private RiskRelatedPersonMapper riskRelatedPersonMapper;

    private OrgStdRiskMapper orgStdRiskMapper;

    public RiskService(RiskMapper riskMapper, UserMapper userMapper, RiskRelatedPersonMapper riskRelatedPersonMapper, OrgStdRiskMapper orgStdRiskMapper) {
        this.riskMapper = riskMapper;
        this.userMapper = userMapper;
        this.riskRelatedPersonMapper = riskRelatedPersonMapper;
        this.orgStdRiskMapper = orgStdRiskMapper;
    }

    public Integer addRisk(Integer projectId, AddRiskRequest request) {
        ProjectRisk risk = new ProjectRisk();
        BeanUtils.copyProperties(request, risk);
        risk.setState(1);
        risk.setProjectId(projectId);
        risk.setSource(RiskType.SELF_IDENTIFY.getValue());
        riskMapper.insert(risk);
        if (!CollectionUtils.isEmpty(request.getRelatedPersons())) {
            List<Username> usernames = userMapper.selectUsernameBatch(request.getRelatedPersons());
            List<RiskRelatedPerson> riskRelatedPeople = usernames.stream()
                    .map(o -> new RiskRelatedPerson(risk.getId(), o.getId(), o.getUsername())).collect(Collectors.toList());
            riskRelatedPersonMapper.insertRiskRelatedPersonBatch(riskRelatedPeople);
        }
        return risk.getId();
    }

    public void editRisk(Integer projectId, Integer riskId, EditRiskRequest request) {
        if (riskMapper.selectOne(new QueryWrapper<ProjectRisk>()
                .eq("project_id", projectId).eq("risk_id", riskId)) == null) {
            throw new IllegalArgumentException("Cannot find risk");
        }
        ProjectRisk risk = new ProjectRisk();
        risk.setProjectId(projectId);
        risk.setId(riskId);
        BeanUtils.copyProperties(request, risk);
        riskMapper.updateById(risk);
    }

    public void deleteRisk(Integer projectId, Integer riskId) {
        if (riskMapper.selectOne(new QueryWrapper<ProjectRisk>()
                .eq("project_id", projectId).eq("risk_id", riskId)) == null) {
            throw new IllegalArgumentException("Cannot find risk");
        }
        riskMapper.deleteById(riskId);
    }

    public PageDTO<RiskDTO> getRisks(Integer projectId, Integer pageNo, Integer pageSize) {
        Page<ProjectRisk> page = new Page<>(pageNo, pageSize);
        IPage<ProjectRisk> iPage = riskMapper.selectPage(page, new QueryWrapper<ProjectRisk>().eq("project_id", projectId));
        List<RiskDTO> riskDTOS = new ArrayList<>((int) iPage.getSize());
        for (ProjectRisk risk : iPage.getRecords()) {
            riskDTOS.add(getRiskDTO(risk.getId()));
        }
        return new PageDTO<>(iPage.getCurrent(), iPage.getSize(), iPage.getTotal(), riskDTOS);
    }

    public List<SearchableDTO> searchRisks(Integer projectId, String name) {
        List<Searchable> searchables = riskMapper.selectLikeName(projectId, name);
        return searchables.stream().map(o -> new SearchableDTO(o.getId(), o.getName())).collect(Collectors.toList());
    }

    public RiskDTO getRisk(Integer projectId, Integer riskId) {
        if (riskMapper.selectOne(new QueryWrapper<ProjectRisk>()
                .eq("project_id", projectId).eq("risk_id", riskId)) == null) {
            throw new IllegalArgumentException("Cannot find risk");
        }
        return getRiskDTO(riskId);
    }

    private RiskDTO getRiskDTO(Integer riskId) {
        RiskDTO riskDTO = new RiskDTO();
        ProjectRisk risk = riskMapper.selectById(riskId);
        BeanUtils.copyProperties(risk, riskDTO);
        List<RiskRelatedPerson> riskRelatedPeople = riskRelatedPersonMapper.selectList(new QueryWrapper<RiskRelatedPerson>()
                .eq("risk_id", risk.getId()));
        List<UsernameDTO> usernameDTOS = riskRelatedPeople.stream()
                .map(o -> new UsernameDTO(o.getUserId(), o.getUsername())).collect(Collectors.toList());
        riskDTO.setRiskRelatedPeople(usernameDTOS);
        return riskDTO;
    }

    public List<Integer> importRisksFromOtherProject(Integer projectId, ImportRiskRequest request) {
        List<ProjectRisk> risks = riskMapper.selectList(new QueryWrapper<ProjectRisk>()
                .eq("project_id", request.getProjectId()));
        for (ProjectRisk risk : risks) {
            risk.setId(null);
            risk.setProjectId(projectId);
            risk.setState(RiskState.STILL_EXISTS.getValue());
            risk.setOwnerId(null);
            risk.setOwnerName(null);
            risk.setSource(RiskType.IMPORT_FROM_OTHER_PROJECT.getValue());
        }
        this.saveBatch(risks);
        return risks.stream().map(ProjectRisk::getId).collect(Collectors.toList());
    }

    public List<Integer> importRisksFromStdLib(Integer projectId) {
        List<OrgStdRisk> orgStdRisks = orgStdRiskMapper.selectList(new QueryWrapper<>());
        List<ProjectRisk> projectRisks = new ArrayList<>();
        for (OrgStdRisk orgStdRisk : orgStdRisks) {
            ProjectRisk risk = new ProjectRisk();
            BeanUtils.copyProperties(orgStdRisk, risk);
            risk.setId(null);
            risk.setProjectId(projectId);
            risk.setState(RiskState.STILL_EXISTS.getValue());
            risk.setOwnerId(null);
            risk.setOwnerName(null);
            risk.setSource(RiskType.IMPORT_FROM_ORG_STD_RISK_LIB.getValue());
            projectRisks.add(risk);
        }
        this.saveBatch(projectRisks);
        return projectRisks.stream().map(ProjectRisk::getId).collect(Collectors.toList());
    }
}
