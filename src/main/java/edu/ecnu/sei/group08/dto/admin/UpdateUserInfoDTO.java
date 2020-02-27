package edu.ecnu.sei.group08.dto.admin;

import io.github.talelin.autoconfigure.validator.LongList;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserInfoDTO {

    @LongList(min = 1, message = "{group.ids.long-list}")
    private List<Long> roleIds;
}
