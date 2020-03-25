package pretty.april.achieveitserver.entity;

import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2020-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String outerId;

    private String type;

    private Integer projectId;

    private Integer managerId;

    private String state;

    private LocalDate startDate;

    private LocalDate dueDate;

    private LocalDate returnDate;


}
