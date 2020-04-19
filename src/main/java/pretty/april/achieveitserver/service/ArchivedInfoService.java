package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.ArchivedInfo;
import pretty.april.achieveitserver.mapper.ArchivedInfoMapper;

@Service
public class ArchivedInfoService extends ServiceImpl<ArchivedInfoMapper, ArchivedInfo> {

	public Integer updateArchivedInfo(ArchivedInfo updateArchivedInforequest) {
		return this.baseMapper.updateById(updateArchivedInforequest);
	}
	
	public ArchivedInfo getArchivedInfo(Integer id) {
		return this.baseMapper.selectById(id);
	}
}
