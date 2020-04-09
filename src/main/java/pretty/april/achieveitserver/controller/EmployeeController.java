package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.SimpleEmployeeDTO;
import pretty.april.achieveitserver.service.EmployeeService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public Response<List<SimpleEmployeeDTO>> getSimpleEmployees() {
        return ResponseUtils.successResponse(employeeService.getSimpleEmployeeDTOS());
    }
}
