package redis.clients.jedis;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/GeoCoordinate.class */
public class GeoCoordinate {
    private double longitude;
    private double latitude;

    public GeoCoordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeoCoordinate)) {
            return false;
        }
        GeoCoordinate that = (GeoCoordinate) o;
        return Double.compare(that.longitude, this.longitude) == 0 && Double.compare(that.latitude, this.latitude) == 0;
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.longitude);
        int result = (int) (temp ^ (temp >>> 32));
        long temp2 = Double.doubleToLongBits(this.latitude);
        return (31 * result) + ((int) (temp2 ^ (temp2 >>> 32)));
    }

    public String toString() {
        return "(" + this.longitude + "," + this.latitude + ")";
    }
}
