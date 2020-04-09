package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.Employee;

import java.util.List;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select e.job_number from employee e left join user u on e.job_number=u.username where u.username is null")
    List<String> selectEmployeeJobNumbersNotInUserTable();
}
