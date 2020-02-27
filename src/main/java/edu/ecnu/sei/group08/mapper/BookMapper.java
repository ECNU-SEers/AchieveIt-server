package edu.ecnu.sei.group08.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.ecnu.sei.group08.model.BookDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMapper extends BaseMapper<BookDO> {

    List<BookDO> selectByTitleLikeKeyword(@Param("q") String q);

    List<BookDO> selectByTitle(@Param("title") String title);
}
