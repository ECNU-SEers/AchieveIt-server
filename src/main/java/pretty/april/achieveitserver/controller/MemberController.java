package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.MemberDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.dto.SimpleMemberDTO;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;
import pretty.april.achieveitserver.request.EditMemberRequest;
import pretty.april.achieveitserver.service.MemberService;
import pretty.april.achieveitserver.utils.ResponseUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/project/{projectId}/member")
    public Response<?> addProjectMember(@PathVariable @NotNull Integer projectId, @RequestBody @Valid AddProjectMemberRequest request) {
        memberService.addProjectMember(request, projectId);
        return ResponseUtils.successResponse();
    }

    @GetMapping("/project/{projectId}/members/simple")
    public Response<List<SimpleMemberDTO>> getSimpleMembers(@PathVariable Integer projectId) {
        return ResponseUtils.successResponse(memberService.getSimpleMembers(projectId));
    }

    @GetMapping("/project/{projectId}/members")
    public Response<PageDTO<MemberDTO>> getMembers(@PathVariable Integer projectId,
                                                   @RequestParam Integer page,
                                                   @RequestParam Integer pageSize) {
        return ResponseUtils.successResponse(memberService.getMembers(page, pageSize, projectId));
    }

    @GetMapping("/project/{projectId}/member/{memberId}")
    public Response<MemberDTO> getMember(@PathVariable Integer projectId, @PathVariable Integer memberId) {
        return ResponseUtils.successResponse(memberService.getMember(projectId, memberId));
    }

    @PutMapping("/project/{projectId}/member/{memberId}")
    public Response<?> editProjectMember(@PathVariable Integer projectId, @PathVariable Integer memberId, @RequestBody EditMemberRequest request) {
        memberService.editMember(memberId, projectId, request);
        return ResponseUtils.successResponse();
    }

    @DeleteMapping("/project/{projectId}/member/{memberId}")
    public Response<?> deleteMember(@PathVariable Integer projectId, @PathVariable Integer memberId) {
        memberService.deleteMember(memberId, projectId);
        return ResponseUtils.successResponse();
    }
}
