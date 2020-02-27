package edu.ecnu.sei.group08.mapper;

import edu.ecnu.sei.group08.model.UserRoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

    int insertBatch(@Param("relations") List<UserRoleDO> relations);
}
