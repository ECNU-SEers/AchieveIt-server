package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.converter.DefectConverter;
import pretty.april.achieveitserver.dto.DefectDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.entity.Defect;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.enums.DefectState;
import pretty.april.achieveitserver.exception.UserNotFoundException;
import pretty.april.achieveitserver.mapper.DefectMapper;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.request.AssignDefectRequest;
import pretty.april.achieveitserver.request.CreateDefectRequest;
import pretty.april.achieveitserver.request.EditDefectRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefectService {

    private DefectMapper defectMapper;

    private DefectConverter defectConverter;

    private UserMapper userMapper;

    public DefectService(DefectMapper defectMapper, DefectConverter defectConverter, UserMapper userMapper) {
        this.defectMapper = defectMapper;
        this.defectConverter = defectConverter;
        this.userMapper = userMapper;
    }

    public Integer addDefect(CreateDefectRequest request, Integer creatorId, String creatorName) {
        Defect defect = new Defect();
        BeanUtils.copyProperties(request, defect);
        if (request.getHandlerId() != null) {
            User user = userMapper.selectById(request.getHandlerId());
            if (user == null) {
                throw new UserNotFoundException();
            }
            defect.setHandlerId(user.getId());
            defect.setHandlerName(user.getUsername());
            defect.setState(DefectState.ASSIGNED.getValue());
        } else {
            defect.setState(DefectState.OPEN.getValue());
        }
        LocalDateTime time = LocalDateTime.now();
        defect.setCreatedAt(time);
        defect.setUpdatedAt(time);
        defect.setCreatorId(creatorId);
        defect.setCreatorName(creatorName);
        defectMapper.insert(defect);
        return defect.getId();
    }

    public void updateDefect(EditDefectRequest request, Integer defectId) {
        if (defectMapper.selectById(defectId) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        BeanUtils.copyProperties(request, defect);
        defect.setId(defectId);
        LocalDateTime time = LocalDateTime.now();
        defect.setUpdatedAt(time);
        defectMapper.updateById(defect);
    }

    public void assignDefect(AssignDefectRequest assignDefectRequest, Integer defectId) {
        if (defectMapper.selectById(defectId) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        BeanUtils.copyProperties(assignDefectRequest, defect);
        defect.setId(defectId);
        defect.setState(DefectState.ASSIGNED.getValue());
        defect.setUpdatedAt(LocalDateTime.now());
        defectMapper.updateById(defect);
    }

    public void fix(Integer defectId) {
        if (defectMapper.selectById(defectId) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        defect.setId(defectId);
        defect.setState(DefectState.FIXED.getValue());
        defect.setUpdatedAt(LocalDateTime.now());
        defectMapper.updateById(defect);
    }

    public void close(Integer defectId) {
        if (defectMapper.selectById(defectId) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        defect.setId(defectId);
        defect.setState(DefectState.CLOSED.getValue());
        defect.setUpdatedAt(LocalDateTime.now());
        defectMapper.updateById(defect);
    }

    public void reopen(Integer defectId) {
        if (defectMapper.selectById(defectId) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        defect.setId(defectId);
        defect.setState(DefectState.OPEN.getValue());
        defect.setUpdatedAt(LocalDateTime.now());
        defectMapper.updateById(defect);
    }

    public void delete(Integer defectId) {
        defectMapper.deleteById(defectId);
    }

    public PageDTO<DefectDTO> queryDefects(Integer pageNo, Integer pageSize, Integer projectId, Integer type, Integer level, Integer state) {
        Page<Defect> page = new Page<>(pageNo, pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("project_id", projectId);
        map.put("type", type);
        map.put("state", state);
        map.put("level", level);
        IPage<Defect> defects = defectMapper.selectPage(page,
                new QueryWrapper<Defect>()
                        .allEq(map, false)
                        .orderByDesc("created_at"));
        return new PageDTO<>(defects.getCurrent(), defects.getSize(), defects.getTotal(),
                defectConverter.defectDTOList(defects.getRecords()));
    }
}
