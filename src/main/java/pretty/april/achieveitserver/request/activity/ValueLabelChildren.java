package pretty.april.achieveitserver.request.activity;

import lombok.Data;

import java.util.List;

@Data
public class ValueLabelChildren {
    private Integer id;
    private String name;
    private List<RetrieveActivityRequest> children;
}
