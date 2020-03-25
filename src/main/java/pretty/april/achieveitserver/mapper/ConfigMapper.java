package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pretty.april.achieveitserver.entity.ProjectConfig;

@Mapper
public interface ConfigMapper extends BaseMapper<ProjectConfig> {
}
