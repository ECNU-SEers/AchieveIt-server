package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.SimpleEmployeeDTO;
import pretty.april.achieveitserver.model.Supervisor;
import pretty.april.achieveitserver.model.Username;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取所有用户ID和Name信息
     *
     * @return
     */
    @GetMapping("/employees")
    public Response<List<SimpleEmployeeDTO>> getEmployees() {
        return ResponseUtils.successResponse(userService.getEmployees());
    }

    /**
     * 所有的项目上级
     *
     * @return
     */
    @GetMapping("/supervisors")
    public Response<List<Supervisor>> getAllSupervisors() {
        return ResponseUtils.successResponse(userService.getAllSupervisors());
    }
    
    /**
     * 所有的组织配置管理员
     *
     * @return
     */
    @GetMapping("/config/organizers")
    public Response<List<Supervisor>> getAllConfigOrganizers() {
        return ResponseUtils.successResponse(userService.getAllConfigOrganizer());
    }
    
    /**
     * 所有的QA经理
     *
     * @return
     */
    @GetMapping("/qa/managers")
    public Response<List<Supervisor>> getAllQAManagers() {
        return ResponseUtils.successResponse(userService.getAllQAManager());
    }
    
    /**
     * 所有的EPGLeader
     *
     * @return
     */
    @GetMapping("/epg/leaders")
    public Response<List<Supervisor>> getAllEPGLeaders() {
        return ResponseUtils.successResponse(userService.getAllEPGLeader());
    }
}
