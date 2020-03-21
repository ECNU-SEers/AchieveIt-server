package pretty.april.achieveitserver.enums;

public enum DefectState {
    OPEN(0),
    ASSIGNED(1),
    FIXED(2),
    CLOSED(3);

    private int value;

    DefectState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
