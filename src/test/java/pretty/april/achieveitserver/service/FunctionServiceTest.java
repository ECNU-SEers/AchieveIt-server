package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.request.AddFunctionRequest;
import pretty.april.achieveitserver.request.EditFunctionRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class FunctionServiceTest {

    @Autowired
    private FunctionService functionService;

    @Test
    void addFunction() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        AddFunctionRequest addSubFunctionRequest = new AddFunctionRequest();
        addSubFunctionRequest.setDescription("test");
        addSubFunctionRequest.setName("test");
        addSubFunctionRequest.setParentId(functionId);
        Integer subFunctionId = functionService.addFunction(addSubFunctionRequest, projectId);
        assertNotNull(subFunctionId);
    }

    @Test
    void editFunction() {
        int projectId = 1;
        AddFunctionRequest request = new AddFunctionRequest();
        request.setDescription("test");
        request.setName("test");
        request.setParentId(null);
        Integer functionId = functionService.addFunction(request, projectId);
        assertNotNull(functionId);
        EditFunctionRequest editFunctionRequest = new EditFunctionRequest();
        editFunctionRequest.setDescription("test2");
        editFunctionRequest.setName("test2");
        functionService.editFunction(editFunctionRequest, projectId, functionId);
    }

    @Test
    void getFunctions() {
        int projectId = 1;
        AddFunctionRequest request = new AddFunctionRequest();
        request.setDescription("test");
        request.setName("test");
        request.setParentId(null);
        Integer functionId = functionService.addFunction(request, projectId);
        assertNotNull(functionId);
        String keyword = "test";
        assertNotNull(functionService.getFunctions(projectId, keyword));
    }

    @Test
    void getSubFunctions() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        AddFunctionRequest addSubFunctionRequest = new AddFunctionRequest();
        addSubFunctionRequest.setDescription("test");
        addSubFunctionRequest.setName("test");
        addSubFunctionRequest.setParentId(functionId);
        Integer subFunctionId = functionService.addFunction(addSubFunctionRequest, projectId);
        assertNotNull(subFunctionId);
        assertNotNull(functionService.getSubFunctions(projectId, functionId));
    }

    @Test
    void deleteFunction() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        functionService.deleteFunction(projectId, functionId);
    }

    @Test
    void getSimpleFunctions() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        assertNotNull(functionService.getSimpleFunctions(projectId));
    }

    @Test
    void searchFunctions() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        String keyword = "test";
        assertNotNull(functionService.searchFunctions(projectId, keyword));
    }

    @Test
    void getFullFunction() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        AddFunctionRequest addSubFunctionRequest = new AddFunctionRequest();
        addSubFunctionRequest.setDescription("test");
        addSubFunctionRequest.setName("test");
        addSubFunctionRequest.setParentId(functionId);
        Integer subFunctionId = functionService.addFunction(addSubFunctionRequest, projectId);
        assertNotNull(subFunctionId);
        assertNotNull(functionService.getFullFunction(projectId, functionId));
    }

    @Test
    void getById() {
        int projectId = 1;
        AddFunctionRequest addFunctionRequest = new AddFunctionRequest();
        addFunctionRequest.setDescription("test");
        addFunctionRequest.setName("test");
        addFunctionRequest.setParentId(null);
        Integer functionId = functionService.addFunction(addFunctionRequest, projectId);
        assertNotNull(functionId);
        assertNotNull(functionService.getById(functionId));
    }

    @Test
    void getWorkHourFunctions() {
        int userId = 1;
        assertNotNull(functionService.getWorkHourFunctions(userId));
    }
}