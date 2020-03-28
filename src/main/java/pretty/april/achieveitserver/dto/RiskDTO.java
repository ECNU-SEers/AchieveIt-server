package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class RiskDTO {

    private Integer id;

    private String name;

    private Integer projectId;

    private String type;

    private String description;

    private Integer level;

    private Integer impact;

    private String strategy;

    private Integer state;

    private Integer ownerId;

    private String ownerName;

    private Integer trackingFreq;

    private Integer source;

    private List<UsernameDTO> riskRelatedPeople;
}
