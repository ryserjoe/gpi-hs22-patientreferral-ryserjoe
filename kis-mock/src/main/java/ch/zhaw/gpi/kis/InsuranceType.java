package ch.zhaw.gpi.kis;

public enum InsuranceType {
    V1("privat"),
    V2("halbprivat"),
    V3("allgemein");

    private final String id;

    InsuranceType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
