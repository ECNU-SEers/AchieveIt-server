package pretty.april.achieveitserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.client.RetrieveClientInfoRequest;
import pretty.april.achieveitserver.service.ClientService;
import pretty.april.achieveitserver.utils.ResponseUtils;

@RestController
@RequestMapping("/api/client")
public class ClientController {

	@Autowired
	private ClientService clientService;
	
	@GetMapping("/allClients")
	public Response<List<RetrieveClientInfoRequest>> getAllOuterIdAndCompany() {
		return ResponseUtils.successResponse(clientService.getAllOuterIdAndCompany());
	}
}
