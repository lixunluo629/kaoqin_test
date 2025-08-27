package springfox.documentation.service;

/* loaded from: springfox-core-2.2.2.jar:springfox/documentation/service/AllowableRangeValues.class */
public class AllowableRangeValues implements AllowableValues {
    private final String min;
    private final String max;

    public AllowableRangeValues(String min, String max) {
        this.min = min;
        this.max = max;
    }

    public String getMin() {
        return this.min;
    }

    public String getMax() {
        return this.max;
    }
}
