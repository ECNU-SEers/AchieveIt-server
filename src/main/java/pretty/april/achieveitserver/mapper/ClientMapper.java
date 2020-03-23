package pretty.april.achieveitserver.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import pretty.april.achieveitserver.entity.Client;
import pretty.april.achieveitserver.request.client.RetrieveClientInfoRequest;

@Mapper
public interface ClientMapper extends BaseMapper<Client> {

	/**
	 * 查看所有客户的客户ID和客户名称（公司名）
	 * @return 所有客户的客户ID和客户名称（公司名）
	 */
	@Select("SELECT outer_id, company FROM client")
	List<RetrieveClientInfoRequest> selectOuterIdAndCompany();
}
