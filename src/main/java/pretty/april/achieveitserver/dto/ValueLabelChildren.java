package pretty.april.achieveitserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class ValueLabelChildren {
    private Integer value;
    private String label;
    private List<ValueLabelChildren> children;
}
