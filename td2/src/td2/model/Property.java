package td2.model;

public enum Property {
    NAME("nom obligatoire"),
    GIVEN_NAME("prénom obligatoire"),
    COUNTRY("pays obligatoire"),
    CITY("ville obligatoire");

    public String tooltip;

    private Property(String tooltip) {
        this.tooltip = tooltip;
    }
}
