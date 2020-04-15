package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.ConfigDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.AddProjectConfigRequest;
import pretty.april.achieveitserver.request.EditConfigRequest;
import pretty.april.achieveitserver.service.ConfigService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ConfigController {

    private ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    /**
     * 获取某个项目的配置信息
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/config")
    public Response<ConfigDTO> getConfig(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(configService.getConfig(projectId));
    }

    /**
     * 修改某个项目的配置信息
     *
     * @param projectId
     * @param request
     * @return
     */
    @PutMapping("/project/{projectId}/config")
    public Response<?> editConfig(@PathVariable Integer projectId, @RequestBody EditConfigRequest request) {
        configService.editConfig(projectId, request);
        return ResponseUtils.successResponse();
    }

    /**
     * 为某个项目创建配置信息
     *
     * @param projectId
     * @param request
     * @return
     */
    @PostMapping("/project/{projectId}/config")
    public Response<?> createConfig(@PathVariable Integer projectId, @RequestBody @Valid AddProjectConfigRequest request) {
        configService.createConfig(projectId, request);
        return ResponseUtils.successResponse();
    }
}
