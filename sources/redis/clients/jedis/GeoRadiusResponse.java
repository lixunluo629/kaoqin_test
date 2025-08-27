package redis.clients.jedis;

import redis.clients.util.SafeEncoder;

/* loaded from: jedis-2.9.3.jar:redis/clients/jedis/GeoRadiusResponse.class */
public class GeoRadiusResponse {
    private byte[] member;
    private double distance;
    private GeoCoordinate coordinate;

    public GeoRadiusResponse(byte[] member) {
        this.member = member;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public byte[] getMember() {
        return this.member;
    }

    public String getMemberByString() {
        return SafeEncoder.encode(this.member);
    }

    public double getDistance() {
        return this.distance;
    }

    public GeoCoordinate getCoordinate() {
        return this.coordinate;
    }
}
