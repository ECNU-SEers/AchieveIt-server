package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.request.AddRiskRequest;
import pretty.april.achieveitserver.request.EditRiskRequest;
import pretty.april.achieveitserver.request.ImportRiskRequest;
import pretty.april.achieveitserver.service.RiskService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class RiskController {

    private RiskService riskService;

    public RiskController(RiskService riskService) {
        this.riskService = riskService;
    }

    @PostMapping("/project/{projectId}/risk")
    public Response<Integer> addRisk(@PathVariable Integer projectId, @RequestBody @Valid AddRiskRequest request) {
        return ResponseUtils.successResponse(riskService.addRisk(projectId, request));
    }

    @PutMapping("/project/{projectId}/risk/{riskId}")
    public Response<?> editRisk(@PathVariable Integer projectId, @PathVariable Integer riskId, @RequestBody @Valid EditRiskRequest request) {
        riskService.editRisk(projectId, riskId, request);
        return ResponseUtils.successResponse();
    }

    @DeleteMapping("/project/{projectId}/risk/{riskId}")
    public Response<?> deleteRisk(@PathVariable Integer projectId, @PathVariable Integer riskId) {
        riskService.deleteRisk(projectId, riskId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/project/{projectId}/risks")
    public Response<PageDTO<RiskDTO>> getRisks(@PathVariable Integer projectId,
                                               @RequestParam Integer page,
                                               @RequestParam Integer pageSize,
                                               @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(riskService.getRisks(projectId, page, pageSize, keyword));
    }

    @GetMapping("/project/{projectId}/risks/search")
    public Response<List<SearchableDTO>> searchRisks(@PathVariable Integer projectId, @RequestParam String name) {
        return ResponseUtils.successResponse(riskService.searchRisks(projectId, name));
    }

    @GetMapping("/project/{projectId}/risk/{riskId}")
    public Response<RiskDTO> getRisk(@PathVariable Integer projectId, @PathVariable Integer riskId) {
        return ResponseUtils.successResponse(riskService.getRisk(projectId, riskId));
    }

    @PostMapping("/project/{projectId}/risk/import/other")
    public Response<List<Integer>> importRisksFromOtherProject(@PathVariable Integer projectId, @RequestBody @Valid ImportRiskRequest request) {
        return ResponseUtils.successResponse(riskService.importRisksFromOtherProject(projectId, request));
    }

    @PostMapping("/project/{projectId}/risk/import/std")
    public Response<List<Integer>> importRisksFromStdLib(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(riskService.importRisksFromStdLib(projectId));
    }

    /**
     * 获取某个项目可以作为风险责任人的所有成员
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/risk/owners")
    public Response<List<SimpleMemberDTO>> getAvailableRiskOwners(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(riskService.getAvailableRiskOwners(projectId));
    }

    /**
     * 获取某个项目可以作为风险相关者的所有成员
     *
     * @param projectId
     * @return
     */
    @GetMapping("/project/{projectId}/risk/relatedPeople")
    public Response<List<SimpleMemberDTO>> getAvailableRiskRelatedPeople(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(riskService.getAvailableRiskRelatedPeople(projectId));
    }
}
