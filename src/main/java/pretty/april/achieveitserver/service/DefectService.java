package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pretty.april.achieveitserver.converter.DefectConverter;
import pretty.april.achieveitserver.dto.DefectDTO;
import pretty.april.achieveitserver.dto.PageDTO;
import pretty.april.achieveitserver.dto.SearchableDTO;
import pretty.april.achieveitserver.dto.TypeDTO;
import pretty.april.achieveitserver.entity.Defect;
import pretty.april.achieveitserver.entity.DefectType;
import pretty.april.achieveitserver.entity.User;
import pretty.april.achieveitserver.enums.DefectState;
import pretty.april.achieveitserver.exception.UserNotFoundException;
import pretty.april.achieveitserver.mapper.DefectMapper;
import pretty.april.achieveitserver.mapper.DefectTypeMapper;
import pretty.april.achieveitserver.mapper.UserMapper;
import pretty.april.achieveitserver.model.Searchable;
import pretty.april.achieveitserver.request.CreateDefectRequest;
import pretty.april.achieveitserver.request.DefectTransitionRequest;
import pretty.april.achieveitserver.request.EditDefectRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefectService {

    private DefectMapper defectMapper;

    private DefectConverter defectConverter;

    private UserMapper userMapper;

    private DefectTypeMapper defectTypeMapper;

    public DefectService(DefectMapper defectMapper, DefectConverter defectConverter, UserMapper userMapper, DefectTypeMapper defectTypeMapper) {
        this.defectMapper = defectMapper;
        this.defectConverter = defectConverter;
        this.userMapper = userMapper;
        this.defectTypeMapper = defectTypeMapper;
    }

    public Integer addDefect(CreateDefectRequest request, Integer creatorId, String creatorName, Integer projectId) {
        Defect defect = new Defect();
        BeanUtils.copyProperties(request, defect);
        defect.setProjectId(projectId);
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

    public void updateDefect(EditDefectRequest request, Integer defectId, Integer projectId) {
        if (defectMapper.selectOne(new QueryWrapper<Defect>()
                .eq("id", defectId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        BeanUtils.copyProperties(request, defect);
        defect.setId(defectId);
        LocalDateTime time = LocalDateTime.now();
        defect.setUpdatedAt(time);
        defectMapper.updateById(defect);
    }

    public void transition(Integer defectId, Integer projectId, DefectTransitionRequest request) {
        if (defectMapper.selectOne(new QueryWrapper<Defect>()
                .eq("id", defectId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
        Defect defect = new Defect();
        defect.setId(defectId);
        switch (request.getAction()) {
            case "assign":
                if (request.getAssigneeId() == null || StringUtils.isEmpty(request.getAssigneeName()) || request.getDue() == null) {
                    throw new IllegalArgumentException("Bad assign");
                }
                defect.setHandlerId(request.getAssigneeId());
                defect.setHandlerName(request.getAssigneeName());
                defect.setDue(request.getDue());
                defect.setState(DefectState.ASSIGNED.getValue());
                break;
            case "fix":
                defect.setState(DefectState.FIXED.getValue());
                break;
            case "close":
                defect.setState(DefectState.CLOSED.getValue());
                break;
            case "reopen":
                defect.setState(DefectState.OPEN.getValue());
                break;
            default:
                throw new IllegalArgumentException("Invalid transition action");
        }
        defectMapper.updateById(defect);
    }

    public void delete(Integer defectId, Integer projectId) {
        if (defectMapper.selectOne(new QueryWrapper<Defect>()
                .eq("id", defectId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find defect.");
        }
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

    public List<TypeDTO> getDefectTypes() {
        List<DefectType> defectTypes = defectTypeMapper.selectList(new QueryWrapper<>());
        return defectTypes.stream().map(o -> new TypeDTO(o.getId(), o.getName(), o.getRemark())).collect(Collectors.toList());
    }

    public List<SearchableDTO> searchDefects(Integer projectId, String name) {
        List<Searchable> searchables = defectMapper.selectLikeName(projectId, name);
        return searchables.stream().map(o -> new SearchableDTO(o.getId(), o.getName())).collect(Collectors.toList());
    }
}
