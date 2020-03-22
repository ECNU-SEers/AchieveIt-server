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
 * @since 2020-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArchivedInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer projectId;

    private Boolean basicData;

    private Boolean proposal;

    private Boolean quotation;

    private Boolean estimation;

    private Boolean planning;

    private Boolean processClipping;

    private Boolean costManage;

    private Boolean reqChange;

    private Boolean riskManage;

    private Boolean clientAccPro;

    private Boolean clientAcc;

    private Boolean infoSummary;

    private Boolean bestExperience;

    private Boolean devTool;

    private Boolean devTemplate;

    private Boolean checklist;

    private Boolean qaSummary;


}
