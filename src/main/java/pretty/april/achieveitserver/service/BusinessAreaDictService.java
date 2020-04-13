package pretty.april.achieveitserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.BusinessAreaDict;
import pretty.april.achieveitserver.mapper.BusinessAreaDictMapper;
import pretty.april.achieveitserver.request.dict.AddBusinessAreaDictRequest;
import pretty.april.achieveitserver.request.dict.UpdateBusinessAreaDictRequest;

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
	
	/**
	 * 批量增加业务领域
	 * @param businessAreas
	 * @return
	 */
	public int addBusinessAreas(List<String> businessAreas) {
		return this.baseMapper.insertBatch(businessAreas);
	}
	
	/**
	 * 增加业务领域
	 * @param request
	 * @return
	 */
	public int insertBusinessArea(AddBusinessAreaDictRequest request) {
		String businessAreaName = request.getBusinessAreaName();
		BusinessAreaDict businessAreaDict = new BusinessAreaDict();
		businessAreaDict.setName(businessAreaName);
		return this.baseMapper.insert(businessAreaDict);
	}
	
	/**
	 * 修改业务领域
	 * @param request
	 * @return
	 */
	public int updateBusinessArea(UpdateBusinessAreaDictRequest request) {
		Integer id = request.getBusinessAreaId();
		String newBusinessAreaName = request.getNewBusinessAreaName();
		BusinessAreaDict businessArea = this.baseMapper.selectById(id);
		businessArea.setName(newBusinessAreaName);
		return this.baseMapper.updateById(businessArea);
	}
	
	/**
	 * 删除业务领域
	 * @param businessAreaIds
	 * @return
	 */
	public int deleteBusinessAreas(List<Integer> businessAreaIds) {
		return this.baseMapper.deleteBatchIds(businessAreaIds);
	}
}
