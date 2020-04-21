package pretty.april.achieveitserver.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.StateChange;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.mapper.StateChangeMapper;
import pretty.april.achieveitserver.request.statechange.RetrieveStateChangeRequest;

@Service
public class StateChangeService extends ServiceImpl<StateChangeMapper, StateChange> {

	@Autowired
	private ProjectService projectService;
	@Autowired
	private UserService userService;
	
	public List<RetrieveStateChangeRequest> showStateChangeList(String outerId) {
		Integer projectId = projectService.getProjectByOuterId(outerId).getId();
		List<StateChange> stateChanges = this.getAllStateChangesByProjectId(projectId);
		List<RetrieveStateChangeRequest> retrieveStateChangeRequest = new ArrayList<>();
		for (StateChange stateChange: stateChanges) {
			RetrieveStateChangeRequest request = new RetrieveStateChangeRequest();
			BeanUtils.copyProperties(stateChange, request);
			if (request.getFormerState()==null) {
				request.setFormerState("无");
			}
			
			User user = userService.getById(stateChange.getOperatorId());
			request.setUsername(user.getUsername());
			request.setRealName(user.getRealName());
			
			if (request.getLatterState().equals("申请立项")) {
				request.setOperation("新建项目");
			} else if (request.getLatterState().equals("已立项")) {
				request.setOperation("审批通过");
			} else if (request.getLatterState().equals("立项驳回")) {
				request.setOperation("驳回申请");
			} else if (request.getLatterState().equals("进行中")) {
				request.setOperation("配置信息");
			} else if (request.getLatterState().equals("已交付")) {
				request.setOperation("项目交付");
			} else if (request.getLatterState().equals("申请归档")) {
				request.setOperation("申请归档");
			} else if (request.getLatterState().equals("已归档")) {
				request.setOperation("项目归档");
			}
			
			if (request.getRemark()==null || "".equals(request.getRemark())) {
				request.setRemark("无");
			}
			
			retrieveStateChangeRequest.add(request);
		}
		return retrieveStateChangeRequest;
	}
	
	public List<StateChange> getAllStateChangesByProjectId(Integer projectId) {
		return this.baseMapper.selectByProjectId(projectId);
	}
	
	
}
