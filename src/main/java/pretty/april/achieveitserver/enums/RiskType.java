package pretty.april.achieveitserver.enums;

import lombok.Getter;

@Getter
public enum RiskType {
    SELF_IDENTIFY(1),
    IMPORT_FROM_OTHER_PROJECT(2),
    IMPORT_FROM_ORG_STD_RISK_LIB(3);

    private int value;

    RiskType(int value) {
        this.value = value;
    }
}
