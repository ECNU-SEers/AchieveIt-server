package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.Defect;
import pretty.april.achieveitserver.model.Searchable;

import java.util.List;

@Mapper
public interface DefectMapper extends BaseMapper<Defect> {

    @Select("select id,name from defect where project_id = #{projectId} and name like concat('%',#{name},'%')")
    List<Searchable> selectLikeName(@Param("projectId") Integer projectId, @Param("name") String name);
}
