package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.SimpleEmployeeDTO;
import pretty.april.achieveitserver.entity.Employee;
import pretty.april.achieveitserver.mapper.EmployeeMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService extends ServiceImpl<EmployeeMapper, Employee> {

    private EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeMapper employeeMapper) {
        this.employeeMapper = employeeMapper;
    }

    public List<SimpleEmployeeDTO> getSimpleEmployeeDTOS() {
        List<String> jobNumbers = employeeMapper.selectEmployeeJobNumbersNotInUserTable();
        return jobNumbers.stream().map(SimpleEmployeeDTO::new).collect(Collectors.toList());
    }
}
