package pretty.april.achieveitserver.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pretty.april.achieveitserver.dto.PermissionDTO;
import pretty.april.achieveitserver.dto.RoleDTO;
import pretty.april.achieveitserver.entity.Permission;
import pretty.april.achieveitserver.entity.Role;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorityConverter {

    public PermissionDTO toPermissionDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        BeanUtils.copyProperties(permission,permissionDTO);
        return permissionDTO;
    }

    public List<PermissionDTO> toPermissionDTOList(List<Permission> permissionList) {
        return permissionList.stream().map(this::toPermissionDTO).collect(Collectors.toList());
    }

    public RoleDTO toRoleDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        return roleDTO;
    }

    public List<RoleDTO> toRoleDTOList(List<Role> roleList) {
        return roleList.stream().map(this::toRoleDTO).collect(Collectors.toList());
    }

}
