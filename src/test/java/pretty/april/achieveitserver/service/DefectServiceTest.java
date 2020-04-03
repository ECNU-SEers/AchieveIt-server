package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.request.CreateDefectRequest;
import pretty.april.achieveitserver.request.DefectTransitionRequest;
import pretty.april.achieveitserver.request.EditDefectRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class DefectServiceTest {

    @Autowired
    private DefectService defectService;

    @Test
    void addDefect() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int handlerId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setDue(LocalDateTime.now().plusDays(1));
        request.setHandlerId(handlerId);
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        defectService.addDefect(request, creatorId, creatorName, projectId);
    }

    @Test
    void updateDefect() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int handlerId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setDue(LocalDateTime.now().plusDays(1));
        request.setHandlerId(handlerId);
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        Integer defectId = defectService.addDefect(request, creatorId, creatorName, projectId);
        assertNotNull(defectId);
        EditDefectRequest editDefectRequest = new EditDefectRequest();
        editDefectRequest.setDescription("test1");
        editDefectRequest.setLevel(2);
        editDefectRequest.setName("test1");
        editDefectRequest.setType(2);
        defectService.updateDefect(editDefectRequest, defectId, projectId);
    }

    @Test
    void transition() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        Integer defectId = defectService.addDefect(request, creatorId, creatorName, projectId);
        assertNotNull(defectId);
        DefectTransitionRequest defectTransitionRequest = new DefectTransitionRequest();
        defectTransitionRequest.setAction("assign");
        defectTransitionRequest.setAssigneeId(1);
        defectTransitionRequest.setAssigneeName("admin");
        defectTransitionRequest.setDue(LocalDateTime.now().plusDays(2));
        defectService.transition(defectId, projectId, defectTransitionRequest);

        DefectTransitionRequest fixTransition = new DefectTransitionRequest();
        fixTransition.setAction("fix");
        defectService.transition(defectId, projectId, fixTransition);

        DefectTransitionRequest closeTransition = new DefectTransitionRequest();
        closeTransition.setAction("close");
        defectService.transition(defectId, projectId, closeTransition);

        DefectTransitionRequest reopenTransition = new DefectTransitionRequest();
        reopenTransition.setAction("reopen");
        defectService.transition(defectId, projectId, reopenTransition);
    }

    @Test
    void delete() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        Integer defectId = defectService.addDefect(request, creatorId, creatorName, projectId);
        assertNotNull(defectId);
        defectService.delete(defectId, projectId);
    }

    @Test
    void queryDefects() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        Integer defectId = defectService.addDefect(request, creatorId, creatorName, projectId);
        assertNotNull(defectId);
        assertNotNull(defectService.queryDefects(1, 5, projectId, 1, 1, null, null));
    }

    @Test
    void getDefectTypes() {
        assertNotNull(defectService.getDefectTypes());
    }

    @Test
    void searchDefects() {
        int creatorId = 1;
        String creatorName = "admin";
        int projectId = 1;
        int level = 1;
        int type = 1;
        CreateDefectRequest request = new CreateDefectRequest();
        request.setDescription("test");
        request.setLevel(level);
        request.setType(type);
        request.setName("test");
        Integer defectId = defectService.addDefect(request, creatorId, creatorName, projectId);
        assertNotNull(defectId);
        String keyword = "test";
        assertNotNull(defectService.searchDefects(projectId, keyword));
    }
}