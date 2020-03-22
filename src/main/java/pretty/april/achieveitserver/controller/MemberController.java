package pretty.april.achieveitserver.controller;

import org.springframework.web.bind.annotation.*;
import pretty.april.achieveitserver.dto.Response;
import pretty.april.achieveitserver.request.AddProjectMemberRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class MemberController {

    @PostMapping("/project/{projectId}/member")
    public Response<?> addProjectMember(@PathVariable @NotNull Integer projectId, @RequestBody @Valid AddProjectMemberRequest request) {
        return null;
    }
}
