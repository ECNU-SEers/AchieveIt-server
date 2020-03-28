package pretty.april.achieveitserver.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/external/system")
public class ExternalSystemController {

	@PostMapping("/git")
	public Response<String> git(@RequestParam(value="projectId") String projectId,
								@RequestParam(value="userId") Integer userId,
								@RequestParam(value="roles") List<String> roles,
								@RequestParam(value="enterTime") LocalDateTime enterTime,
								@RequestParam(value="exitTime") LocalDateTime exitTime,
								@RequestParam(value="repository") String repository) {
		return ResponseUtils.successResponse("ok");
	}
	
	@PostMapping("/mail")
	public Response<String> mail(@RequestParam(value="projectId") String projectId,
								@RequestParam(value="userId") Integer userId,
								@RequestParam(value="roles") List<String> roles,
								@RequestParam(value="enterTime") LocalDateTime enterTime,
								@RequestParam(value="exitTime") LocalDateTime exitTime,
								@RequestParam(value="mailList") String mailList) {
		return ResponseUtils.successResponse("ok");
	}
	
	@PostMapping("/file")
	public Response<String> file(@RequestParam(value="projectId") String projectId,
								@RequestParam(value="userId") Integer userId,
								@RequestParam(value="roles") List<String> roles,
								@RequestParam(value="enterTime") LocalDateTime enterTime,
								@RequestParam(value="exitTime") LocalDateTime exitTime,
								@RequestParam(value="fileServerContent") String fileServerContent) {
		return ResponseUtils.successResponse("ok");
	}
	
	@PostMapping("/sendmail")
	public Response<String> sendmail(@RequestParam(value="content") String content,
								@RequestParam(value="receiverId") Integer receiverIds) {
		return ResponseUtils.successResponse("ok");
	}
	
}
