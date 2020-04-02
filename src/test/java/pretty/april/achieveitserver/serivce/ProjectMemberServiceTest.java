package pretty.april.achieveitserver.serivce;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.entity.ProjectMember;
import pretty.april.achieveitserver.service.ProjectMemberService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectMemberServiceTest {

	@Autowired
	private ProjectMemberService projectMemberService;
	
	@Test
	public void selectCountByProjectIdTest() {
		Integer projectId = new Integer(57);
		assertEquals(0, projectMemberService.selectCountByProjectId(projectId));
	}
	
	@Test
	public void selectByProjectIdTest() {
		Integer projectId = new Integer(1);
		List<ProjectMember> projectMembers = projectMemberService.selectByProjectId(projectId);
		assertEquals(1, projectMembers.size());
	}
	
	@Test
	public void checkMemberExistByProjectIdAndUserIdTest_userId_1() {
		Integer projectId = new Integer(1);
		Integer userId = new Integer(1);
		assertTrue(projectMemberService.checkMemberExistByProjectIdAndUserId(projectId, userId));
	}
	
	@Test
	public void checkMemberExistByProjectIdAndUserIdTest_userId_2() {
		Integer projectId = new Integer(1);
		Integer userId = new Integer(2);
		assertFalse(projectMemberService.checkMemberExistByProjectIdAndUserId(projectId, userId));
	}
	
	@Test
	public void selectProjectIdByUserIdTest() {
		Integer userId = new Integer(1);
		assertEquals(6, projectMemberService.selectProjectIdByUserId(userId).size());
	}
}