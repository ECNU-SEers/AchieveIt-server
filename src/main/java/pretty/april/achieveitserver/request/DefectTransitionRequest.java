package pretty.april.achieveitserver.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Data
public class DefectTransitionRequest {

    @Pattern(regexp = "^assign$|^fix$|^close$|^reopen$")
    private String action;

    private Integer assigneeId;

    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime due;
}
