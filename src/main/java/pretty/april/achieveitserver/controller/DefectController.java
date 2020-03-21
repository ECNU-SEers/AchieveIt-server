package pretty.april.achieveitserver.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.DefectDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.request.AssignDefectRequest;
import pretty.april.achieveitserver.request.CreateDefectRequest;
import pretty.april.achieveitserver.request.EditDefectRequest;
import pretty.april.achieveitserver.service.DefectService;
import pretty.april.achieveitserver.service.UserService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/defect")
    public Response<Integer> createDefect(@Valid @RequestBody CreateDefectRequest createDefectRequest) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(username);
        Integer defectId = defectService.addDefect(createDefectRequest, user.getId(), username);
        return ResponseUtils.successResponse(defectId);
    }

    @PutMapping("/defect/{defectId}")
    public Response<?> editDefect(@PathVariable Integer defectId, @RequestBody @Valid EditDefectRequest editDefectRequest) {
        defectService.updateDefect(editDefectRequest, defectId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/defect/{defectId}/assign")
    public Response<?> assignDefect(@PathVariable Integer defectId, @RequestBody AssignDefectRequest assignDefectRequest) {
        defectService.assignDefect(assignDefectRequest, defectId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/defect/{defectId}/fix")
    public Response<?> fix(@PathVariable Integer defectId) {
        defectService.fix(defectId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/defect/{defectId}/close")
    public Response<?> close(@PathVariable Integer defectId) {
        defectService.close(defectId);
        return ResponseUtils.successResponse();
    }

    @PutMapping("/defect/{defectId}/reopen")
    public Response<?> reopen(@PathVariable Integer defectId) {
        defectService.reopen(defectId);
        return ResponseUtils.successResponse();
    }

    @DeleteMapping("/defect/{defectId}")
    public Response<?> delete(@PathVariable Integer defectId) {
        defectService.delete(defectId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/defects")
    public Response<PageDTO<DefectDTO>> getDefects(@RequestParam Integer page,
                                                   @RequestParam Integer pageSize,
                                                   @RequestParam(required = false) Integer projectId,
                                                   @RequestParam(required = false) Integer state,
                                                   @RequestParam(required = false) Integer level,
                                                   @RequestParam(required = false) Integer type) {
        return ResponseUtils.successResponse(defectService.queryDefects(page, pageSize,projectId, type, level, state));
    }
}
