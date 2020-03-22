package pretty.april.achieveitserver.converter;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import pretty.april.achieveitserver.dto.DefectDTO;
import pretty.april.achieveitserver.entity.Defect;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefectConverter {

    public DefectDTO toDefectDTO(Defect defect) {
        DefectDTO defectDTO = new DefectDTO();
        BeanUtils.copyProperties(defect, defectDTO);
        return defectDTO;
    }

    public List<DefectDTO> defectDTOList(List<Defect> defectList) {
        return defectList.stream().map(this::toDefectDTO).collect(Collectors.toList());
    }
}
