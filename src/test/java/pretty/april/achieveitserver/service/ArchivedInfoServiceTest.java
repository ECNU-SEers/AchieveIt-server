package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.ArchivedInfo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArchivedInfoServiceTest {

	@Autowired
	private ArchivedInfoService archivedInfoService;
	
	@Test
	public void updateArchivedInfoTest() {
		ArchivedInfo request = new ArchivedInfo();
		request.setId(1);
		request.setProjectId(1);
		request.setBasicData(true);
		request.setProposal(true);
		request.setQuotation(true);
		request.setEstimation(true);
		request.setPlanning(true);
		request.setProcessClipping(true);
		request.setCostManage(true);
		request.setReqChange(true);
		request.setRiskManage(true);
		request.setClientAccPro(true);
		request.setClientAcc(true);
		request.setInfoSummary(true);
		request.setBestExperience(true);
		request.setDevTool(true);
		request.setDevTemplate(true);
		request.setChecklist(true);
		request.setQaSummary(true);
		assertEquals(1, archivedInfoService.updateArchivedInfo(request));
	}
	
	@Test
	public void getArchivedInfoTest() {
		ArchivedInfo archivedInfo = archivedInfoService.getArchivedInfo(1);
		System.out.println(archivedInfo.getProjectId());
		assertNotNull(archivedInfo);
	}
}
