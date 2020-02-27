package edu.ecnu.sei.group08.mapper;

import edu.ecnu.sei.group08.model.RolePermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionMapper extends BaseMapper<RolePermissionDO> {

    int insertBatch(@Param("relations") List<RolePermissionDO> relations);

    int deleteBatchByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
}
