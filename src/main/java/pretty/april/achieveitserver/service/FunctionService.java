package pretty.april.achieveitserver.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import pretty.april.achieveitserver.dto.FullFunctionDTO;
import pretty.april.achieveitserver.dto.FunctionDTO;
import pretty.april.achieveitserver.dto.SearchableDTO;
import pretty.april.achieveitserver.dto.SimpleFunctionDTO;
import pretty.april.achieveitserver.entity.ProjectFunction;
import pretty.april.achieveitserver.mapper.ProjectFunctionMapper;
import pretty.april.achieveitserver.model.Searchable;
import pretty.april.achieveitserver.request.AddFunctionRequest;
import pretty.april.achieveitserver.request.EditFunctionRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunctionService {

    private ProjectFunctionMapper projectFunctionMapper;

    public FunctionService(ProjectFunctionMapper projectFunctionMapper) {
        this.projectFunctionMapper = projectFunctionMapper;
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

    public List<FunctionDTO> getFunctions(Integer projectId) {
        Page<ProjectFunction> page = new Page<>(1, 50);
        IPage<ProjectFunction> functions = projectFunctionMapper.selectPage(page,
                new QueryWrapper<ProjectFunction>().eq("project_id", projectId).isNull("parent_id"));
        return functions.getRecords().stream()
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription())).collect(Collectors.toList());
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
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription())).collect(Collectors.toList());
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
                .map(o -> new FunctionDTO(o.getId(), o.getName(), o.getDescription())).collect(Collectors.toList()));
        return fullFunctionDTO;
    }
    
    public ProjectFunction getById(Integer id) {
    	return projectFunctionMapper.selectById(id);
    }
}
