package pretty.april.achieveitserver.enums;

import lombok.Getter;

@Getter
public enum RiskState {
    STILL_EXISTS(1),
    ALREADY_EXCLUDED(2);

    private int value;

    RiskState(int value) {
        this.value = value;
    }
}
