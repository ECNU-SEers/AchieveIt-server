package pretty.april.achieveitserver.entity;

import java.time.LocalDate;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mybatis-plus
 * @since 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String outerId;

    private Integer projectId;

    private Integer managerId;

    private String state;

    private LocalDate startDate;

    private LocalDate dueDate;

    private LocalDate returnDate;


}
