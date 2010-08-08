
package jp.ddo.haselab.location.example1.util;

import android.location.Location;

/**
 * My locatin.
 * 
 * @author hasegawa
 * 
 */
public final class MyLocation {

    private final long   id;
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final double accuracy;
    private final double speed;
    private final double bearing;
    private final String address;

    MyLocation(final long id1,
	       final double a,
	       final double b,
	       final double c,
	       final double d,
	       final double e,
	       final double f,
	       final String addr) {
        this.id = id1;
        this.latitude = a;
        this.longitude = b;
        this.altitude = c;
        this.accuracy = d;
        this.speed = e;
        this.bearing = f;
	this.address = addr;
    }

    /**
     * constractor.
     * @param id1           id
     * @param location   at android.location
     * @param address addressString
     */
    public MyLocation(final long id1,
		      final Location location,
		      final String address) {

        this(id1,
	     location.getLatitude(),
	     location.getLongitude(),
	     location.getAltitude(),
	     location.getAccuracy(),
	     location.getSpeed(),
	     location.getBearing(),
	     address);
    }

    /**
     * 加速度
     * 
     * @return 加速度
     */
    public double getAccuracy() {
        return this.accuracy;
    }

    /**
     * address
     * @return address
     */
    public String getAddress(){
        return this.address;
    }
    /**
     * 標高
     * 
     * @return 標高
     */
    public double getAltitude() {
        return this.altitude;
    }

    /**
     * 方向
     * 
     * @return 方向
     */
    public double getBearing() {
        return this.bearing;
    }

    /**
     * ID
     * 
     * @return id
     */
    public long getId() {
        return this.id;
    }

    /**
     * 緯度
     * 
     * @return 緯度
     */
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * 経度
     * 
     * @return 経度
     */
    public double getLongitude() {
        return this.longitude;
    }

    /**
     * speed
     * 
     * @return speed
     */
    public double getSpeed() {
        return this.speed;
    }

    @Override
    public String toString() {

        return "MyLocation [id=" + this.id
                + ", address="
                + this.address
                + ", latitude="
                + this.latitude
                + ", longitude="
                + this.longitude
                + ", altitude="
                + this.altitude
                + ", accuracy="
                + this.accuracy
                + ", speed="
                + this.speed
                + ", bearing="
                + this.bearing
                + "]";
    }

}
