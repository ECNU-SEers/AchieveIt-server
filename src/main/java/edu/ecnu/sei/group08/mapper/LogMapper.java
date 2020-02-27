package edu.ecnu.sei.group08.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.model.LogDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface LogMapper extends BaseMapper<LogDO> {

    IPage<LogDO> findLogsByUsernameAndRange(Page<LogDO> pager, String name, Date start, Date end);

    IPage<LogDO> searchLogsByUsernameAndKeywordAndRange(Page<LogDO> pager, String name, String keyword, Date start, Date end);

    IPage<String> getUserNames(Page<LogDO> pager);
}
