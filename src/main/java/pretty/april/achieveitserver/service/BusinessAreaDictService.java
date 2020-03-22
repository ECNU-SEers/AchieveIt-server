package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.mapper.BusinessAreaDictMapper;

@Service
public class BusinessAreaDictService extends ServiceImpl<BusinessAreaDictMapper, BusinessAreaDict> {

	/**
	 * 通过业务领域名称查找业务领域Id
	 * @param businessAreaName 业务名称
	 * @return 该业务名称对应的业务Id
	 */
	public Integer getBusinessAreaIdByBusinessAreaName(String businessAreaName) {
		QueryWrapper<BusinessAreaDict> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(BusinessAreaDict::getName, businessAreaName);
		BusinessAreaDict businessArea = this.getOne(queryWrapper);
		return businessArea.getId();
	}
	
	/**
	 * 查询所有业务领域
	 * @return 所有业务领域
	 */
	public List<BusinessAreaDict> getAllBusinessAreas() {
		return this.baseMapper.selectAllBusinessAreaDict();
	}
}
