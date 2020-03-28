package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.CreateDefectRequest;
import pretty.april.achieveitserver.request.DefectTransitionRequest;
import pretty.april.achieveitserver.request.EditDefectRequest;
import pretty.april.achieveitserver.service.DefectService;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DefectController {

    private UserService userService;

    private DefectService defectService;

    public DefectController(UserService userService, DefectService defectService) {
        this.userService = userService;
        this.defectService = defectService;
    }

    @PostMapping("/project/{projectId}/defect")
    public Response<Integer> createDefect(@PathVariable Integer projectId, @Valid @RequestBody CreateDefectRequest createDefectRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer defectId = defectService.addDefect(createDefectRequest, user.getId(), username, projectId);
        return ResponseUtils.successResponse(defectId);
    }

    @PutMapping("/project/{projectId}/defect/{defectId}")
    public Response<?> editDefect(@PathVariable Integer projectId, @PathVariable Integer defectId, @RequestBody @Valid EditDefectRequest editDefectRequest) {
        defectService.updateDefect(editDefectRequest, defectId, projectId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/project/{projectId}/defect/{defectId}/transition")
    public Response<?> transition(@PathVariable Integer projectId, @PathVariable Integer defectId, @RequestBody @Valid DefectTransitionRequest request) {
        defectService.transition(defectId, projectId, request);
        return ResponseUtils.successResponse();
    }

    @DeleteMapping("/project/{projectId}/defect/{defectId}")
    public Response<?> delete(@PathVariable Integer projectId, @PathVariable Integer defectId) {
        defectService.delete(defectId, projectId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/defects")
    public Response<PageDTO<DefectDTO>> getDefects(@RequestParam Integer page,
                                                   @RequestParam Integer pageSize,
                                                   @RequestParam(required = false) Integer projectId,
                                                   @RequestParam(required = false) Integer state,
                                                   @RequestParam(required = false) Integer level,
                                                   @RequestParam(required = false) Integer type) {
        return ResponseUtils.successResponse(defectService.queryDefects(page, pageSize, projectId, type, level, state));
    }

    @GetMapping("/defect/type")
    public Response<List<TypeDTO>> getDefectTypes() {
        return ResponseUtils.successResponse(defectService.getDefectTypes());
    }

    @GetMapping("/project/{projectId}/defects/search")
    public Response<List<SearchableDTO>> searchDefects(@RequestParam String name, @PathVariable Integer projectId) {
        return ResponseUtils.successResponse(defectService.searchDefects(projectId, name));
    }
}
