package app.utakata.ac.application;

public enum VoiceType {
    HIGH("high"),
    HIGHHIGH("highhigh"),
    LOW("low");

    private final String value;

    VoiceType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
