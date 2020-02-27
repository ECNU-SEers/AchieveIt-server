package edu.ecnu.sei.group08.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.ecnu.sei.group08.common.mybatis.Page;
import edu.ecnu.sei.group08.model.LogDO;
import edu.ecnu.sei.group08.mapper.LogMapper;
import edu.ecnu.sei.group08.service.LogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    public IPage<LogDO> getLogPage(Long page, Long count, String name, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<LogDO> iPage = this.baseMapper.findLogsByUsernameAndRange(pager, name, start, end);
        return iPage;
    }

    @Override
    public IPage<LogDO> searchLogPage(Long page, Long count, String name, String keyword, Date start, Date end) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<LogDO> iPage = this.baseMapper.searchLogsByUsernameAndKeywordAndRange(pager, name, "%" + keyword + "%", start, end);
        return iPage;
    }

    @Override
    public IPage<String> getUserNamePage(Long page, Long count) {
        Page<LogDO> pager = new Page<>(page, count);
        IPage<String> iPage = this.baseMapper.getUserNames(pager);
        return iPage;
    }

    @Override
    public boolean createLog(String message, String permission, Long userId, String username, String method, String path, Integer status) {
        LogDO record = LogDO.builder()
                .message(message)
                .userId(userId)
                .username(username)
                .statusCode(status)
                .method(method)
                .path(path)
                .build();
        if (permission != null) {
            record.setPermission(permission);
        }
        return this.baseMapper.insert(record) > 0;
    }
}
