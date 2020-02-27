package edu.ecnu.sei.group08.bo;

import cn.hutool.core.bean.BeanUtil;
import edu.ecnu.sei.group08.model.RoleDO;
import edu.ecnu.sei.group08.model.PermissionDO;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupPermissionsBO {
    private Long id;

    private String name;

    private String info;

    private List permissions;

    public GroupPermissionsBO(RoleDO group, List<PermissionDO> permissions) {
        BeanUtil.copyProperties(group, this);
        this.permissions = permissions;
    }
}
