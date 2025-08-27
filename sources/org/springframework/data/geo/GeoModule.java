package org.springframework.data.geo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.List;
import org.springframework.hateoas.Link;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule.class */
public class GeoModule extends SimpleModule {
    private static final long serialVersionUID = 1;

    public GeoModule() {
        super("Spring Data Geo Mixins", new Version(1, 0, 0, null, "org.springframework.data", "spring-data-commons-geo"));
        setMixInAnnotation(Distance.class, DistanceMixin.class);
        setMixInAnnotation(Point.class, PointMixin.class);
        setMixInAnnotation(Box.class, BoxMixin.class);
        setMixInAnnotation(Circle.class, CircleMixin.class);
        setMixInAnnotation(Polygon.class, PolygonMixin.class);
    }

    @JsonIgnoreProperties({"unit"})
    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule$DistanceMixin.class */
    static abstract class DistanceMixin {
        @JsonIgnore
        abstract double getNormalizedValue();

        DistanceMixin(@JsonProperty("value") double value, @JsonProperty("metric") @JsonDeserialize(as = Metrics.class) Metric metic) {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule$PointMixin.class */
    static abstract class PointMixin {
        PointMixin(@JsonProperty("x") double x, @JsonProperty("y") double y) {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule$CircleMixin.class */
    static abstract class CircleMixin {
        CircleMixin(@JsonProperty("center") Point center, @JsonProperty("radius") Distance radius) {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule$BoxMixin.class */
    static abstract class BoxMixin {
        BoxMixin(@JsonProperty(Link.REL_FIRST) Point first, @JsonProperty("second") Point point) {
        }
    }

    /* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/geo/GeoModule$PolygonMixin.class */
    static abstract class PolygonMixin {
        PolygonMixin(@JsonProperty("points") List<Point> points) {
        }
    }
}
