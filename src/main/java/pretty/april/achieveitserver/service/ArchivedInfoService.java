package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ArchivedInfo;
import pretty.april.achieveitserver.mapper.ArchivedInfoMapper;

@Service
public class ArchivedInfoService extends ServiceImpl<ArchivedInfoMapper, ArchivedInfo> {

	public Integer updateArchivedInfo(ArchivedInfo updateArchivedInforequest) {
		return this.baseMapper.updateById(updateArchivedInforequest);
	}
	
	public ArchivedInfo getArchivedInfo(Integer projectId) {
		return this.baseMapper.selectOne(new QueryWrapper<ArchivedInfo>().eq("project_id", projectId));
	}
}
