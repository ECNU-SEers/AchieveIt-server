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
 * @since 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ProjectSkill implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer projectId;

    private Integer skillId;

    private String skillName;


}
