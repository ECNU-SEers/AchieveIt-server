package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.statechange.RetrieveStateChangeRequest;
import pretty.april.achieveitserver.service.StateChangeService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/project/state")
public class StateChangeController {

	@Autowired
	private StateChangeService stateChangeService;
	
	@GetMapping("/change")
	public Response<List<RetrieveStateChangeRequest>> showStateChangeList(@RequestParam(value="outerId") String outerId) {
		return ResponseUtils.successResponse(stateChangeService.showStateChangeList(outerId));
	}
}
