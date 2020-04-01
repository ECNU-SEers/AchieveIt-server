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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * 给某个项目添加一名成员
     *
     * @param projectId
     * @param request
     * @return
     */
    @PostMapping("/project/{projectId}/member")
    public Response<?> addProjectMember(@PathVariable @NotNull Integer projectId, @RequestBody @Valid AddProjectMemberRequest request) {
        memberService.addProjectMember(request, projectId);
        return ResponseUtils.successResponse();
    }

    /**
     * 对用户真实姓名模糊搜索获取某个项目所有成员的ID、Username和RealName信息
     *
     * @param projectId
     * @param keyword
     * @return
     */
    @GetMapping("/project/{projectId}/members/search")
    public Response<List<SimpleMemberDTO>> getSimpleMembers(@PathVariable Integer projectId,
                                                            @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(memberService.searchMembers(projectId, keyword));
    }

    /**
     * 分页获取某个项目的所有成员信息
     *
     * @param projectId
     * @param page
     * @param pageSize
     * @param keyword
     * @return
     */
    @GetMapping("/project/{projectId}/members")
    public Response<PageDTO<MemberDTO>> getMembers(@PathVariable Integer projectId,
                                                   @RequestParam @Min(1) Integer page,
                                                   @RequestParam Integer pageSize,
                                                   @RequestParam(required = false, defaultValue = "") String keyword) {
        return ResponseUtils.successResponse(memberService.getMembers(page, pageSize, projectId, keyword));
    }

    /**
     * 获取某个项目的某个成员的信息
     *
     * @param projectId
     * @param memberId
     * @return
     */
    @GetMapping("/project/{projectId}/member/{memberId}")
    public Response<MemberDTO> getMember(@PathVariable Integer projectId, @PathVariable Integer memberId) {
        return ResponseUtils.successResponse(memberService.getMember(projectId, memberId));
    }

    /**
     * 修改某个项目的某个成员的信息
     *
     * @param projectId
     * @param memberId
     * @param request
     * @return
     */
    @PutMapping("/project/{projectId}/member/{memberId}")
    public Response<?> editProjectMember(@PathVariable Integer projectId, @PathVariable Integer memberId, @RequestBody EditMemberRequest request) {
        memberService.editMember(memberId, projectId, request);
        return ResponseUtils.successResponse();
    }

    /**
     * 删除某个项目的某个成员
     *
     * @param projectId
     * @param memberId
     * @return
     */
    @DeleteMapping("/project/{projectId}/member/{memberId}")
    public Response<?> deleteMember(@PathVariable Integer projectId, @PathVariable Integer memberId) {
        memberService.deleteMember(memberId, projectId);
        return ResponseUtils.successResponse();
    }
}
