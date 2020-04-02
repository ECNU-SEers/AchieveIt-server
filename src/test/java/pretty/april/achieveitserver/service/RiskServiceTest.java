package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.entity.Project;
import pretty.april.achieveitserver.mapper.ProjectMapper;
import pretty.april.achieveitserver.request.AddRiskRequest;
import pretty.april.achieveitserver.request.EditRiskRequest;
import pretty.april.achieveitserver.request.ImportRiskRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class RiskServiceTest {

    @Autowired
    private RiskService riskService;

    @Autowired
    private ProjectMapper projectMapper;

    private Integer projectId;

    @BeforeEach
    void init() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        projectId = project.getId();
        assertNotNull(projectId);
    }

    @Test
    void addRisk() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
    }

    @Test
    void editRisk() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
        EditRiskRequest editRiskRequest = new EditRiskRequest();
        editRiskRequest.setName("test2");
        editRiskRequest.setLevel(2);
        editRiskRequest.setType("test");
        riskService.editRisk(projectId, riskId, editRiskRequest);
    }

    @Test
    void deleteRisk() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
        riskService.deleteRisk(projectId, riskId);
    }

    @Test
    void getRisks() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
        String keyword = "test";
        assertNotNull(riskService.getRisks(projectId, 1, 5, keyword));
    }

    @Test
    void searchRisks() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
        String keyword = "test";
        assertNotNull(riskService.searchRisks(projectId, keyword));
    }

    @Test
    void getRisk() {
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(projectId, addRiskRequest);
        assertNotNull(riskId);
        assertNotNull(riskService.getRisk(projectId, riskId));
    }

    @Test
    void importRisksFromOtherProject() {
        Project project = new Project();
        project.setName("test");
        project.setState("进行中");
        project.setClientId(1);
        project.setRemark("test");
        project.setManagerId(1);
        project.setSupervisorId(1);
        projectMapper.insert(project);
        Integer anotherProjectId = project.getId();
        assertNotNull(anotherProjectId);
        AddRiskRequest addRiskRequest = new AddRiskRequest();
        addRiskRequest.setName("test");
        addRiskRequest.setImpact(1);
        addRiskRequest.setLevel(1);
        addRiskRequest.setType("");
        addRiskRequest.setDescription("test");
        Integer riskId = riskService.addRisk(anotherProjectId, addRiskRequest);
        assertNotNull(riskId);
        ImportRiskRequest importRiskRequest = new ImportRiskRequest();
        importRiskRequest.setProjectId(anotherProjectId);
        riskService.importRisksFromOtherProject(projectId, importRiskRequest);
    }

    @Test
    void importRisksFromStdLib() {
        riskService.importRisksFromStdLib(projectId);
    }
}