package pretty.april.achieveitserver.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import pretty.april.achieveitserver.request.client.RetrieveClientInfoRequest;
import pretty.april.achieveitserver.service.ClientService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClientServiceTest {

	@Autowired
	private ClientService clientService;
	
	@Test
	public void getIdByOuterIdAndCompanyTest() {
		String outerId = "C01";
		String company = "阿里巴巴";
		assertEquals(1,clientService.getIdByOuterIdAndCompany(outerId, company));
	}
	
	@Test
	public void getCompanyByIdTest() {
		Integer id = new Integer(1);
		assertEquals("阿里巴巴", clientService.getCompanyById(id));
	}
	
	@Test
	public void getClientById() {
		Integer id = new Integer(1);
		assertNotNull(clientService.getClientById(id));
	}
	
	@Test
	public void getAllOuterIdAndCompany() {
		List<RetrieveClientInfoRequest> allClients = clientService.getAllOuterIdAndCompany();
		assertEquals(1, allClients.size());
	}
	
}
