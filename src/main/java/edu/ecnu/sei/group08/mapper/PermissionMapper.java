package edu.ecnu.sei.group08.mapper;

import edu.ecnu.sei.group08.model.PermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<PermissionDO> {

    /**
     * 通过分组ids得到所有分组下的权限
     *
     * @param roleIds 分组ids
     * @return 权限
     */
    List<PermissionDO> selectPermissionsByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 通过分组id得到所有分组下的权限
     *
     * @param roleId 分组id
     * @return 权限
     */
    List<PermissionDO> selectPermissionsByRoleId(@Param("roleId") Long roleId);
}
