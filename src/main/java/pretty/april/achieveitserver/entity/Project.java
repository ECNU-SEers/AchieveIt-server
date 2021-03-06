package pretty.april.achieveitserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2020-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String outerId;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private String state;

    private Integer clientId;

    private Integer supervisorId;

    private String supervisorName;

    private Integer managerId;

    private String managerName;

    private Boolean qaAssigned;

    private Boolean epgAssigned;

    private String instanceId;

    private Boolean configAssigned;

    private String remark;


}
