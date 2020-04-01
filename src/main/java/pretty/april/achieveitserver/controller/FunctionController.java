package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AddFunctionRequest;
import pretty.april.achieveitserver.request.EditFunctionRequest;
import pretty.april.achieveitserver.security.UserContext;
import pretty.april.achieveitserver.service.FunctionService;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FunctionController {

    private FunctionService functionService;

    private UserService userService;

    public FunctionController(FunctionService functionService, UserService userService) {
        this.functionService = functionService;
        this.userService = userService;
    }

    /**
     * 为某个项目添加一个功能
     *
     * @param projectId
     * @param request
     * @return
     */
    @PostMapping("/project/{projectId}/function")
    public Response<Integer> addFunction(@PathVariable Integer projectId, @RequestBody @Valid AddFunctionRequest request) {
        return ResponseUtils.successResponse(functionService.addFunction(request, projectId));
    }

    /**
     * 编辑某个项目的某个功能信息
     *
     * @param projectId
     * @param functionId
     * @param request
     * @return
     */
    @PutMapping("/project/{projectId}/function/{functionId}")
    public Response<?> editFunction(@PathVariable Integer projectId, @PathVariable Integer functionId, @RequestBody EditFunctionRequest request) {
        functionService.editFunction(request, projectId, functionId);
        return ResponseUtils.successResponse();
    }

    /**
     * 获取某个项目的所有一级功能
     *
     * @param projectId
     * @param keyword
     * @return
     */
    @GetMapping("/project/{projectId}/functions")
    public Response<List<FunctionDTO>> getFunctions(@PathVariable Integer projectId,
                                                    @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(functionService.getFunctions(projectId, keyword));
    }

    /**
     * 获取某个项目的某个一级功能下的所有二级功能
     *
     * @param projectId
     * @param functionId
     * @return
     */
    @GetMapping("/project/{projectId}/functions/{functionId}/sub")
    public Response<List<FunctionDTO>> getSubFunctions(@PathVariable Integer projectId, @PathVariable Integer functionId) {
        return ResponseUtils.successResponse(functionService.getSubFunctions(projectId, functionId));
    }

    /**
     * 删除某个项目的某个功能
     *
     * @param functionId
     * @param projectId
     * @return
     */
    @DeleteMapping("/project/{projectId}/function/{functionId}")
    public Response<?> deleteFunction(@PathVariable Integer functionId, @PathVariable Integer projectId) {
        functionService.deleteFunction(projectId, functionId);
        return ResponseUtils.successResponse();
    }

    /**
     * 获取某个项目的所有功能ID和Name信息
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/functions/simple")
    public Response<List<SimpleFunctionDTO>> getSimpleFunctions(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(functionService.getSimpleFunctions(projectId));
    }

    /**
     * 对名称模糊搜索获取某个项目的所有功能ID和Name信息
     *
     * @param projectId
     * @param name
     * @return
     */
    @GetMapping("/project/{projectId}/functions/search")
    public Response<List<SearchableDTO>> getSimpleFunctions(@PathVariable Integer projectId, @RequestParam String name) {
        return ResponseUtils.successResponse(functionService.searchFunctions(projectId, name));
    }

    /**
     * 获取某个项目的某个功能的完整信息
     *
     * @param projectId
     * @param functionId
     * @return
     */
    @GetMapping("/project/{projectId}/functions/{functionId}")
    public Response<FullFunctionDTO> getFunction(@PathVariable Integer projectId, @PathVariable Integer functionId) {
        return ResponseUtils.successResponse(functionService.getFullFunction(projectId, functionId));
    }

    /**
     * 获取当前用户参与的所有进行中的项目的所有功能
     *
     * @return
     */
    @GetMapping("/functions/me")
    public Response<List<ValueLabelChildren>> getWorkHourFunctions() {
        UserContext userContext = (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseUtils.successResponse(functionService.getWorkHourFunctions(userContext.getUserId()));
    }
}
