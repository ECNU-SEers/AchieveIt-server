package pretty.april.achieveitserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageDTO<T> {

    private Long page;
    private Long pageSize;
    private Long total;
    private List<T> items;
}
