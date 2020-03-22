package pretty.april.achieveitserver.activiti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;


/**
 * 
 * @author chenjialei
 * 测试类：完成活动流部署等前期工作
 */
public class ActivitiTest {
	
	/**
	 * 创建activiti所需要的25张表
	 */
//	@Test
//	public void createTableTest() {
////		1.创建ProcessEngineConfiguration对象
//		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
//				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
//		
////		2.创建ProcessEngine对象
//		ProcessEngine processEngine = configuration.buildProcessEngine();
//		
//		//3.输出processEngine对象
//		System.out.println(processEngine);
//	}
	
	/**
	 * 立项流程的流程定义部署
	 * 受到影响的activiti表：
	 *   ACT_GE_BYTEARRAY  流程定义的.bpmn&.png文件
	 *   ACT_RE_DEPLOYMENT 部署信息
	 *   ACT_RE_PROCDEF    流程定义的信息
	 */
	@Test
	public void processDefinitionDeploymentTest() {
//		1.创建ProcessEngine对象
		ProcessEngineConfiguration configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		ProcessEngine processEngine = configuration.buildProcessEngine();
		
//		2.得到RepositoryService对象
		RepositoryService repositoryService = processEngine.getRepositoryService();
		
//		3.进行部署
		Deployment deployment = repositoryService.createDeployment() //创建部署对象
						 .addClasspathResource("diagram/projectApproval.bpmn")  //添加bpmn资源
						 .addClasspathResource("diagram/projectApproval.png")  //添加png资源
						 .name("project approval process")  //部署的命名
						 .deploy();  //部署
		
//		4.输出部署的一些信息（非必须）
		System.out.println(deployment.getName());
		System.out.println(deployment.getId());
	}
}
