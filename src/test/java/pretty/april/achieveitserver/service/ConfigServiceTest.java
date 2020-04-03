package pretty.april.achieveitserver.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pretty.april.achieveitserver.request.AddProjectConfigRequest;
import pretty.april.achieveitserver.request.EditConfigRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class ConfigServiceTest {

    @Autowired
    private ConfigService configService;

    @Test
    void getConfig() {
        int projectId = 90901213;
        AddProjectConfigRequest request = new AddProjectConfigRequest();
        request.setFileServerDir("test");
        request.setIsFileServerDirConfirmed(false);
        request.setIsMailConfirmed(false);
        request.setMail("test");
        configService.createConfig(projectId, request);
        assertNotNull(configService.getConfig(projectId));
    }

    @Test
    void editConfig() {
        int projectId = 90901213;
        AddProjectConfigRequest request = new AddProjectConfigRequest();
        request.setFileServerDir("test");
        request.setIsFileServerDirConfirmed(true);
        request.setIsMailConfirmed(true);
        request.setMail("test");
        configService.createConfig(projectId, request);
        EditConfigRequest editConfigRequest = new EditConfigRequest();
        editConfigRequest.setGitRepoAddress("test2");
        request.setIsFileServerDirConfirmed(true);
        request.setIsMailConfirmed(true);
        request.setMail("test2");
        configService.editConfig(projectId, editConfigRequest);
    }

    @Test
    void createConfig() {
        int projectId = 90901564;
        AddProjectConfigRequest request = new AddProjectConfigRequest();
        request.setFileServerDir("test");
        request.setIsFileServerDirConfirmed(false);
        request.setIsMailConfirmed(false);
        request.setMail("test");
        configService.createConfig(projectId, request);
    }
}