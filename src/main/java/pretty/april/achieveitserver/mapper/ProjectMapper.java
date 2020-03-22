package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import pretty.april.achieveitserver.entity.Project;

import java.util.List;

@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 查询项目ID为$outerId的项目数
     *
     * @param outerId
     * @return 项目数
     */
    @Select("SELECT COUNT(*) FROM project WHERE outer_id = #{outerId}")
    int selectCountByOuterId(String outerId);

    /**
     * 查询项目名称中包含关键字的所有项目
     *
     * @param keyword
     * @return
     */
    @Select("SELECT * FROM project WHERE name LIKE \"%\"#{keyword}\"%\"")
    List<Project> selectByNameLikeKeyword(String keyword);
}
