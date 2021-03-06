package pretty.april.achieveitserver.enums;

import lombok.Getter;

@Getter
public enum DefectState {
    OPEN(1),
    ASSIGNED(2),
    FIXED(3),
    CLOSED(4);

    private int value;

    DefectState(int value) {
        this.value = value;
    }
}
