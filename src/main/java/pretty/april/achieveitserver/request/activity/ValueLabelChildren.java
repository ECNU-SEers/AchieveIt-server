package pretty.april.achieveitserver.request.activity;

import lombok.Data;

import java.util.List;

@Data
public class ValueLabelChildren {
    private Integer value;
    private String label;
    private List<RetrieveActivityRequest> children;
}
