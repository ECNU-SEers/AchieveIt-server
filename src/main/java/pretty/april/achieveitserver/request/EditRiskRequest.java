package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EditRiskRequest {

    private String name;

    private String type;

    private Integer level;

    private Integer impact;

    private String strategy;

    private Integer ownId;

    private Integer trackingFreq;

    private String source;

    private String description;

    private List<Integer> relatedPersons;

    private Integer state;
}
