package pretty.april.achieveitserver.entity;

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
public class ProjectMember implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer projectId;

    private Integer userId;

    private String username;

    private String projectName;

    private Integer leaderId;

    private String leaderName;


}
