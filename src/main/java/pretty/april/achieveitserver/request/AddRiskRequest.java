package pretty.april.achieveitserver.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddRiskRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String type;

    @NotNull
    private Integer level;

    @NotNull
    private Integer impact;

    private String strategy;

    private Integer ownerId;

    private String ownerName;

    private Integer trackingFreq;

    @NotBlank
    private String description;

    private List<Integer> relatedPersons;
}