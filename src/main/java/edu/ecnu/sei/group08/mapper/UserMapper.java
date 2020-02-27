package edu.ecnu.sei.group08.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.model.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMapper extends BaseMapper<UserDO> {

    /**
     * 查询用户名为$username的人数
     *
     * @param username 用户名
     * @return 人数
     */
    int selectCountByUsername(String username);

    /**
     * 查询用户id为$id的人数
     *
     * @param id 用户id
     * @return 人数
     */
    int selectCountById(Long id);

    /**
     * 通过分组id分页获取用户数据
     *
     * @param pager   分页
     * @param roleId 分组id
     * @return 分页数据
     */
    IPage<UserDO> selectPageByRoleId(Page pager, Long roleId);
}
