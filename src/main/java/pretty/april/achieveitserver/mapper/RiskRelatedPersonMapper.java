package pretty.april.achieveitserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import pretty.april.achieveitserver.entity.RiskRelatedPerson;

import java.util.List;

@Mapper
public interface RiskRelatedPersonMapper extends BaseMapper<RiskRelatedPerson> {

    @Insert("<script>" +
            "insert into risk_related_person (risk_id,user_id,username) values " +
            "<foreach collections='rrps' item='rrp' open='' close='' separator=','>" +
            "(#{rrp.riskId},#{rrp.userId},#{rrp.username})" +
            "</foreach>" +
            "</script>")
    void insertRiskRelatedPersonBatch(List<RiskRelatedPerson> rrps);
}
