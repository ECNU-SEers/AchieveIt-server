package pretty.april.achieveitserver.entity;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mybatis-plus
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DeviceInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer deviceId;
    
    private LocalDate inspectDate;
    
    private Boolean intact;

    private String remark;


}
