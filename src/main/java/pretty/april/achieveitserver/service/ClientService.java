package pretty.april.achieveitserver.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import pretty.april.achieveitserver.entity.Client;
import pretty.april.achieveitserver.mapper.ClientMapper;

@Service
public class ClientService extends ServiceImpl<ClientMapper, Client> {

	/**
	 * 通过客户ID和客户名称（公司名称）查找客户表Id
	 * @param outerId 客户ID
	 * @param company 公司名称
	 * @return
	 */
	public Integer getIdByOuterIdAndCompany(String outerId, String company) {
		QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda().eq(Client::getOuterId, outerId).eq(Client::getCompany, company);
		Client client = this.getOne(queryWrapper);
		return client.getId();
	}

	/**
	 * 通过客户表ID查找客户名称
	 * @param id 客户表Id
	 * @return 该客户表id对应的客户Id
	 */
	public String getCompanyById(Integer id) {
		return this.baseMapper.selectById(id).getCompany();
	}

	/**
	 * 通过客户表ID找到该客户
	 * @param id 客户表ID
	 * @return 客户
	 */
	public Client getClientById(Integer id) {
		return this.baseMapper.selectById(id);
	}
}
