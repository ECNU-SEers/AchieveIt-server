package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AddFunctionRequest;
import pretty.april.achieveitserver.request.EditFunctionRequest;
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

    @PostMapping("/project/{projectId}/function")
    public Response<Integer> addFunction(@PathVariable Integer projectId, @RequestBody @Valid AddFunctionRequest request) {
        return ResponseUtils.successResponse(functionService.addFunction(request, projectId));
    }

    @PutMapping("/project/{projectId}/function/{functionId}")
    public Response<?> editFunction(@PathVariable Integer projectId, @PathVariable Integer functionId, @RequestBody EditFunctionRequest request) {
        functionService.editFunction(request, projectId, functionId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/project/{projectId}/functions")
    public Response<List<FunctionDTO>> getFunctions(@PathVariable Integer projectId,
                                                    @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(functionService.getFunctions(projectId, keyword));
    }

    @GetMapping("/project/{projectId}/functions/{functionId}/sub")
    public Response<List<FunctionDTO>> getSubFunctions(@PathVariable Integer projectId, @PathVariable Integer functionId) {
        return ResponseUtils.successResponse(functionService.getSubFunctions(projectId, functionId));
    }

    @DeleteMapping("/project/{projectId}/function/{functionId}")
    public Response<?> deleteFunction(@PathVariable Integer functionId, @PathVariable Integer projectId) {
        functionService.deleteFunction(projectId, functionId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/project/{projectId}/functions/simple")
    public Response<List<SimpleFunctionDTO>> getSimpleFunctions(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(functionService.getSimpleFunctions(projectId));
    }

    @GetMapping("/project/{projectId}/functions/search")
    public Response<List<SearchableDTO>> getSimpleFunctions(@PathVariable Integer projectId, @RequestParam String name) {
        return ResponseUtils.successResponse(functionService.searchFunctions(projectId, name));
    }

    @GetMapping("/project/{projectId}/functions/{functionId}")
    public Response<FullFunctionDTO> getFunction(@PathVariable Integer projectId, @PathVariable Integer functionId) {
        return ResponseUtils.successResponse(functionService.getFullFunction(projectId, functionId));
    }

    @GetMapping("/functions/me")
    public Response<List<ValueLabelChildren>> getWorkHourFunctions() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        return ResponseUtils.successResponse(functionService.getWorkHourFunctions(user.getId()));
    }
}
