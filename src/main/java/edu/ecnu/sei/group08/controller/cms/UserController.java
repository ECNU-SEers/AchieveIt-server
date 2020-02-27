package edu.ecnu.sei.group08.controller.cms;

import edu.ecnu.sei.group08.common.LocalUser;
import edu.ecnu.sei.group08.dto.user.ChangePasswordDTO;
import edu.ecnu.sei.group08.dto.user.LoginDTO;
import edu.ecnu.sei.group08.service.UserIdentityService;
import edu.ecnu.sei.group08.vo.UnifyResponseVO;
import edu.ecnu.sei.group08.vo.UserInfoVO;
import io.github.talelin.core.annotation.AdminRequired;
import io.github.talelin.core.annotation.LoginRequired;
import io.github.talelin.core.annotation.RefreshRequired;
import io.github.talelin.core.annotation.RouteMeta;
import edu.ecnu.sei.group08.common.utils.ResponseUtil;
import edu.ecnu.sei.group08.model.RoleDO;
import edu.ecnu.sei.group08.service.RoleService;
import edu.ecnu.sei.group08.model.UserDO;
import edu.ecnu.sei.group08.vo.UserPermissionsVO;
import edu.ecnu.sei.group08.service.UserService;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import edu.ecnu.sei.group08.dto.user.RegisterDTO;
import edu.ecnu.sei.group08.dto.user.UpdateInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/cms/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserIdentityService userIdentityService;

    @Autowired
    private DoubleJWT jwt;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @AdminRequired
    public UnifyResponseVO<String> register(@RequestBody @Validated RegisterDTO validator) {
        userService.createUser(validator);
        return ResponseUtil.generateUnifyResponse(9);
    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated LoginDTO validator) {
        UserDO user = userService.getUserByUsername(validator.getUsername());
        if (user == null) {
            throw new NotFoundException("user not found", 10021);
        }
        boolean valid = userIdentityService.verifyUsernamePassword(
                user.getId(),
                user.getUsername(),
                validator.getPassword());
        if (!valid)
            throw new ParameterException("username or password is fault", 10031);
        LocalUser.setLocalUser(user);
        return jwt.generateTokens(user.getId());
    }

    /**
     * 更新用户信息
     */
    @PutMapping
    @LoginRequired
    public UnifyResponseVO update(@RequestBody @Validated UpdateInfoDTO validator) {
        userService.updateUserInfo(validator);
        return ResponseUtil.generateUnifyResponse(4);
    }

    /**
     * 修改密码
     */
    @PutMapping("/change_password")
    @LoginRequired
    public UnifyResponseVO updatePassword(@RequestBody @Validated ChangePasswordDTO validator) {
        userService.changeUserPassword(validator);
        return ResponseUtil.generateUnifyResponse(2);
    }

    /**
     * 刷新令牌
     */
    @GetMapping("/refresh")
    @RefreshRequired
    public Tokens refreshToken() {
        UserDO user = LocalUser.getLocalUser();
        return jwt.generateTokens(user.getId());
    }

    /**
     * 查询拥有权限
     */
    @GetMapping("/permissions")
    @LoginRequired
    @RouteMeta(permission = "查询自己拥有的权限", module = "用户", mount = true)
    public UserPermissionsVO getPermissions() {
        UserDO user = LocalUser.getLocalUser();
        boolean admin = roleService.checkIsRootByUserId(user.getId());
        List<Map<String, List<Map<String, String>>>> permissions = userService.getStructualUserPermissions(user.getId());
        UserPermissionsVO userPermissions = new UserPermissionsVO(user, permissions);
        userPermissions.setAdmin(admin);
        return userPermissions;
    }

    /**
     * 查询自己信息
     */
    @LoginRequired
    @RouteMeta(permission = "查询自己信息", module = "用户", mount = true)
    @GetMapping("/information")
    public UserInfoVO getInformation() {
        UserDO user = LocalUser.getLocalUser();
        List<RoleDO> groups = roleService.getUserGroupsByUserId(user.getId());
        return new UserInfoVO(user, groups);
    }
}
