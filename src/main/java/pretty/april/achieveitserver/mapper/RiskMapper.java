package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.ProjectRisk;
import pretty.april.achieveitserver.model.Searchable;

import java.util.List;

@Mapper
public interface RiskMapper extends BaseMapper<ProjectRisk> {

    @Select("select id,name from project_risk where project_id = #{projectId} and name like concat('%',#{name},'%')")
    List<Searchable> selectLikeName(Integer projectId, String name);
}
