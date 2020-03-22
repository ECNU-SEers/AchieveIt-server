package pretty.april.achieveitserver.activiti;

import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

@Service
public class ProcessManagementService {

	private ProcessEngineConfiguration configuration;
	private ProcessEngine processEngine;
	
	public ProcessManagementService() {
		this.configuration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		this.processEngine = configuration.buildProcessEngine();
	}

	/**
	 * 完成部署后，启动流程实例
	 * 受到影响的activiti表有哪些：
	 *   ACT_HI_ACTINST       已完成的活动信息
	 *   ACT_HI_IDENTITYLINK  参与者信息
	 *   ACT_HI_PROCINST      流程实例
	 *   ACT_HI_TASKINST      任务实例
	 *   ACT_RU_EXECUTION     执行表
	 *   ACT_RU_IDENTITYLINK  参与者信息
	 *   ACT_RU_TASK          任务
	 * @param processDefinitionKey 流程定义的key
	 * @param businessKey 业务表中对应的key
	 * @return 是否成功启动流程实例
	 */
	public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> map) {	

		// 1.得到RuntimeService对象
		RuntimeService runtimeService = processEngine.getRuntimeService();

		// 2.启动一个流程实例，流程定义的key（ACT_RE_PROCDEF的key字段），businessKey（对应业务表中记录的id），任务的执行人ID和姓名
		ProcessInstance processInstanceWithBusinessKey = runtimeService.startProcessInstanceByKey(processDefinitionKey,
				businessKey, map);

//			3.输出实例的一些信息（非必须）
//			System.out.println("流程部署ID："+processInstanceWithBusinessKey.getDeploymentId());  //null 找不到
//			System.out.println("流程定义ID："+processInstanceWithBusinessKey.getProcessDefinitionId());  //ACT_RE_PROCDEF的ID_字段
//			System.out.println("流程实例ID："+processInstanceWithBusinessKey.getId());  //ACT_HI_PROCINST的ID_字段
//			System.out.println("活动ID："+processInstanceWithBusinessKey.getActivityId());  //null 找不到
//			System.out.println("BusinessKey："+processInstanceWithBusinessKey.getBusinessKey()); //ACT_RU_EXECUTION的BUSINESS_KEY_
//			ACT_RU_EXECUTION的BUSINESS_KEY_要存入业务标识

		return processInstanceWithBusinessKey;
	}

	/**
	 * 查询某个用户的待办任务
	 * @param processDefinitionKey 流程定义的key
	 * @param taskAssignee 任务执行人
	 * @return 
	 */
	public List<Task> queryActivityTask(String processDefinitionKey, String taskAssignee) {
//		1.得到TaskService对象
		TaskService taskService = processEngine.getTaskService();
		
//		2.创建流程实例：流程定义的key（ACT_RE_PROCDEF的key字段）需要知道
		List<Task> taskList = taskService.createTaskQuery()
				   .processDefinitionKey(processDefinitionKey)
				   .taskAssignee(taskAssignee)
				   .list();
		
//		3.输出实例的一些信息（非必须）
//		for (Task task: taskList) {
//			System.out.println("流程实例ID："+task.getProcessInstanceId());  //ACT_HI_PROCINST的ID_字段
//			System.out.println("任务ID："+task.getId());  //ACT_HI_TASKINST的ID_字段
//			System.out.println("任务负责人："+task.getAssignee());  //ACT_HI_TASKINST的ASSIGNEE_字段
//			System.out.println("任务名称："+task.getName());  //ACT_HI_TASKINST的NAME_字段
//			System.out.println("\n");
//		}
		return taskList;
	}

	/**
	 * 处理某个用户的某一个任务
	 * 受到影响的activiti表
	 *   ACT_HI_ACTINST      多了一条记录，走到下一个节点，END_TIME_=NULL
	 *   ACT_HI_IDENTITYLINK 添加下一个节点的ASSIGNEE
	 *   ACT_HI_TASKINST     ACT_HI_TASKINST中ACT_HI_TASKINST的ID_字段为2505的END_TIME_不为NULL了；
	 *                 		 且多了一条记录，走到下一个节点，END_TIME_=NULL
	 *   ACT_RU_EXECUTION    ACT_ID_改变
	 *   ACT_RU_IDENTITYLINK 添加下一个节点的ASSIGNEE
	 *   ACT_RU_TASK         删除旧节点记录，添加新节点记录（只保留一条记录，使查询速度快）
	 * @param task 某一个用户的某一个任务
	 * @return 是否成功处理该任务
	 */
	public boolean handleActivityTask(Task task) {
		try {
//			1.得到TaskService对象
			TaskService taskService = processEngine.getTaskService();
			
//			2.处理任务，结合当前用户列表的查询操作，得到任务ID（ACT_HI_TASKINST的ID_字段）
			taskService.complete(task.getId());
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return false;
	}
	
	public boolean handleActivityTask(Task task, Map<String, Object> map) {
		try {
//			1.得到TaskService对象
			TaskService taskService = processEngine.getTaskService();
			
//			2.处理任务，结合当前用户列表的查询操作，得到任务ID（ACT_HI_TASKINST的ID_字段）
			taskService.complete(task.getId(), map);
			
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
		}
		return false;
	}
}
