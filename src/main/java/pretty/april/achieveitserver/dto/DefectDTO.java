package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DefectDTO {

    private Integer id;

    private Integer projectId;

    private String name;

    private String description;

    private Integer type;

    private Integer level;

    private Integer state;

    private Integer creatorId;

    private String creatorName;

    private Integer handlerId;

    private String handlerName;

    private LocalDateTime due;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
