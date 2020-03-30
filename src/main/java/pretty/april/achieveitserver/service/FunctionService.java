package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.*;
import pretty.april.achieveitserver.entity.ProjectFunction;
import pretty.april.achieveitserver.mapper.ProjectFunctionMapper;
import pretty.april.achieveitserver.mapper.ProjectMemberMapper;
import pretty.april.achieveitserver.model.Searchable;
import pretty.april.achieveitserver.request.AddFunctionRequest;
import pretty.april.achieveitserver.request.EditFunctionRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FunctionService {

    private ProjectFunctionMapper projectFunctionMapper;

    private ProjectMemberMapper projectMemberMapper;

    public FunctionService(ProjectFunctionMapper projectFunctionMapper, ProjectMemberMapper projectMemberMapper) {
        this.projectFunctionMapper = projectFunctionMapper;
        this.projectMemberMapper = projectMemberMapper;
    }

    public Integer addFunction(AddFunctionRequest request, Integer projectId) {
        ProjectFunction function = new ProjectFunction();
        function.setName(request.getName());
        function.setDescription(request.getDescription());
        function.setParentId(request.getParentId());
        function.setProjectId(projectId);
        projectFunctionMapper.insert(function);
        return function.getId();
    }

    public void editFunction(EditFunctionRequest request, Integer projectId, Integer functionId) {
        if (projectFunctionMapper.selectOne(new QueryWrapper<ProjectFunction>()
                .eq("id", functionId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find project function");
        }
        ProjectFunction function = new ProjectFunction();
        function.setId(functionId);
        function.setProjectId(projectId);
        function.setName(request.getName());
        function.setDescription(request.getDescription());
        projectFunctionMapper.updateById(function);
    }

    public List<FunctionDTO> getFunctions(Integer projectId, String keyword) {
        Page<ProjectFunction> page = new Page<>(1, 50);
        IPage<ProjectFunction> functions = projectFunctionMapper.selectPage(page, new QueryWrapper<ProjectFunction>()
                .eq("project_id", projectId).isNull("parent_id")
                .like("name", keyword));
        return functions.getRecords().stream()
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription(), projectFunctionMapper.selectCountSubFunction(o.getId())))
                .collect(Collectors.toList());
    }

    public List<FunctionDTO> getSubFunctions(Integer projectId, Integer functionId) {
        if (projectFunctionMapper.selectOne(new QueryWrapper<ProjectFunction>()
                .eq("id", functionId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find project function");
        }
        Page<ProjectFunction> page = new Page<>(1, 50);
        IPage<ProjectFunction> functions = projectFunctionMapper.selectPage(page, new QueryWrapper<ProjectFunction>()
                .eq("project_id", projectId).eq("parent_id", functionId));
        return functions.getRecords().stream()
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription(), 0)).collect(Collectors.toList());
    }

    public void deleteFunction(Integer projectId, Integer functionId) {
        if (projectFunctionMapper.selectOne(new QueryWrapper<ProjectFunction>()
                .eq("id", functionId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find project function");
        }
        projectFunctionMapper.deleteById(functionId);
    }

    public List<SimpleFunctionDTO> getSimpleFunctions(Integer projectId) {
        List<ProjectFunction> functions = projectFunctionMapper.selectList(new QueryWrapper<ProjectFunction>()
                .eq("project_id", projectId));
        return functions.stream().map(o -> new SimpleFunctionDTO(o.getId(), o.getName())).collect(Collectors.toList());
    }

    public List<SearchableDTO> searchFunctions(Integer projectId, String name) {
        List<Searchable> searchables = projectFunctionMapper.selectLikeName(projectId, name);
        return searchables.stream().map(o -> new SearchableDTO(o.getId(), o.getName())).collect(Collectors.toList());
    }

    public FullFunctionDTO getFullFunction(Integer projectId, Integer functionId) {
        if (projectFunctionMapper.selectOne(new QueryWrapper<ProjectFunction>()
                .eq("id", functionId).eq("project_id", projectId)) == null) {
            throw new IllegalArgumentException("Cannot find project function");
        }
        ProjectFunction function = projectFunctionMapper.selectById(functionId);
        List<ProjectFunction> subFunctions = projectFunctionMapper.selectList(new QueryWrapper<ProjectFunction>()
                .eq("parent_id", functionId));
        FullFunctionDTO fullFunctionDTO = new FullFunctionDTO();
        fullFunctionDTO.setId(function.getId());
        fullFunctionDTO.setName(function.getName());
        fullFunctionDTO.setDescription(function.getDescription());
        fullFunctionDTO.setSubFunctions(subFunctions.stream()
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription(), 0)).collect(Collectors.toList()));
        return fullFunctionDTO;
    }

    public ProjectFunction getById(Integer id) {
        return projectFunctionMapper.selectById(id);
    }

    public List<ValueLabelChildren> getWorkHourFunctions(Integer userId) {
        List<Searchable> searchables = projectMemberMapper.selectProjectIdNameByUserIdAndState(userId, "进行中");
        List<ValueLabelChildren> prj = new ArrayList<>();
        for (Searchable searchable : searchables) {
            ValueLabelChildren vlc = new ValueLabelChildren();
            vlc.setValue(searchable.getId());
            vlc.setLabel(searchable.getName());
            List<ProjectFunction> functions = projectFunctionMapper.selectList(new QueryWrapper<ProjectFunction>()
                    .eq("project_id", searchable.getId()).isNull("parent_id"));
            List<ValueLabelChildren> funcs = new ArrayList<>();
            for (ProjectFunction function : functions) {
                ValueLabelChildren func = new ValueLabelChildren();
                func.setLabel(function.getName());
                func.setValue(function.getId());

                List<ValueLabelChildren> subFuncs = new ArrayList<>();
                List<ProjectFunction> subFunctions = projectFunctionMapper.selectList(new QueryWrapper<ProjectFunction>()
                        .eq("parent_id", function.getId()));
                for (ProjectFunction subFunction : subFunctions) {
                    ValueLabelChildren subFunc = new ValueLabelChildren();
                    subFunc.setLabel(subFunction.getName());
                    subFunc.setValue(subFunction.getId());
                    subFuncs.add(subFunc);
                }

                func.setChildren(subFuncs);
                funcs.add(func);
            }
            vlc.setChildren(funcs);
            prj.add(vlc);
        }
        return prj;
    }
}
