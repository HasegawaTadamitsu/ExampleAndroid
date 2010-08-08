

package jp.ddo.haselab.location.example1.util;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.location.Address;
import android.location.Geocoder;

import java.util.Locale;
import java.util.List;
import java.io.IOException;


/**
 * GPS のみとする。 Wifi やら他の位置取得情報があるけど、使わない。
 */
public final class MyLocationMgr {

    /**
     * 値取得に対し、合計何秒待つか.その待ち時間
     */
    private final int locationTotalWaitSecondTime;

    private final String PROVIDER = LocationManager.GPS_PROVIDER;

    private Location lastLocation;

    private boolean  enableLocation;

    private boolean  nowRecoding;

    private static final int LOCATION_COUNT_PER_ONE_GET = 1000;

    private static final int DEFAULT_TIMEOUT_SEC = 10;

    private LocationManager mLocationMgr;

    private Geocoder mGeocoder;

    /**
     * CallBack interface
     */
    public static interface Callback {

        /**
         * 取得完了コールバック
         * 
         * @param arg
         *    取得した値。エラー発生時は、null.原因は、ログ出力される。
         */
        public void doneGet(final MyLocation arg);
    }

    /**
     * 待ち時間はデフォルト値を使用.
     * @param argContext
     *            コンテキスト。ここからサービスを取得.
     */
    public MyLocationMgr(final Context argContext){
	this(argContext,DEFAULT_TIMEOUT_SEC);
    }

    /**
     * 
     * @param argContext
     *            コンテキスト。ここからサービスを取得.
     * @param argTotalWaitSecondTime
     *            総待ち時間　単位秒
     */
    public MyLocationMgr(final Context argContext,
			 final int argTotalWaitSecondTime) {
        this.locationTotalWaitSecondTime = argTotalWaitSecondTime;
        this.enableLocation = false;
        this.nowRecoding = false;

        mLocationMgr = (LocationManager)
	    argContext.getSystemService(Context.LOCATION_SERVICE);
	
	mGeocoder = new Geocoder(argContext);
    }

    /**
     * get Location.
     * 
     * @param key      MyLocation 's key
     * @param callback 取得完了時に呼ばれる.
     * @return true 測定開始を行う。/false すでに測定中なので測定を行わない。
     */
    public boolean scan(final long key,
			final Callback callback) {

        if (this.nowRecoding == true) {
            return false;
        }

        this.nowRecoding = true;
	mLocationMgr.requestLocationUpdates(this.PROVIDER,
		   1,    // interval milli sec
		   0.1f, // interval meter
		   locationListener);

        this.enableLocation = true;
        this.lastLocation = null;

        final int intervalMilliSecondTime =
	    this.locationTotalWaitSecondTime * 1000
                / LOCATION_COUNT_PER_ONE_GET;

        final Handler handler = new Handler();
	final Runnable postFinishRun = new Runnable() {
		@SuppressWarnings("synthetic-access")
	        @Override
		public void run() {
		    MyLocationMgr.this.finLocation();
		    if (MyLocationMgr.this.lastLocation == null) {
			callback.doneGet(null);
			return;
		    }
		    String address = point2AddressString(
					 MyLocationMgr.this.lastLocation.getLatitude(),
					 MyLocationMgr.this.lastLocation.getLongitude());

		    callback.doneGet(new MyLocation(key,
						    MyLocationMgr.this.lastLocation,
						    address));
		}
	    };

	final Runnable threadRun =  new Runnable() {
		@SuppressWarnings("synthetic-access")
	        @Override
		    public void run() {
		    MyLog.getInstance().verbose("start");
		    for (int i = 0; i < LOCATION_COUNT_PER_ONE_GET; i++) {
			try {
			    Thread.sleep(intervalMilliSecondTime);
			} catch (InterruptedException e) {
			    MyLog.getInstance().error("sleep error.", e);
			}
			if (MyLocationMgr.this.lastLocation != null) {
			    break;
			}
			if (MyLocationMgr.this.enableLocation == false) {
			    break;
			}
			if (MyLocationMgr.this.nowRecoding == false) {
			    break;
			}
		    }
		    handler.post( postFinishRun );
		}
	    };
	Thread th = new Thread( threadRun );
	th.start();
	return true;
    }

    public  boolean cancel(){
	MyLog.getInstance().verbose("start");
	return finLocation();
    }

    private boolean finLocation(){
	if (!this.nowRecoding ) {
	    return false;
	}
	mLocationMgr.removeUpdates(locationListener);
	this.nowRecoding = false;
	return true;
    }

    final private LocationListener locationListener=
	new LocationListener(){
	    @SuppressWarnings("synthetic-access")
	    @Override
		public void onLocationChanged(final Location location) {
		MyLocationMgr.this.lastLocation = location;
	    }
	    
	    @Override
		public void onProviderDisabled(final String provider) {
		if (!provider.equals(MyLocationMgr.this.PROVIDER)) {
		    return;
		}
		MyLocationMgr.this.enableLocation = false;
	    }
	    
	    @Override
		public void onProviderEnabled(final String provider) {
		if (!provider.equals(MyLocationMgr.this.PROVIDER)) {
		    return;
		}
		MyLocationMgr.this.enableLocation = true;
	    }
	    
	    @Override
		public void onStatusChanged(final String provider,
					    final int status,
					    final Bundle extras) {
		if (!provider.equals(MyLocationMgr.this.PROVIDER)) {
		    return;
		}
		if (status == LocationProvider.AVAILABLE) {
		    MyLocationMgr.this.enableLocation = true;
		    return;
		}
		
		if (status == LocationProvider.OUT_OF_SERVICE ||
		    status == LocationProvider.TEMPORARILY_UNAVAILABLE) {
		    MyLocationMgr.this.enableLocation = false;
		    return;
		}
		
		MyLocationMgr.this.enableLocation = false;
		return;
	    }
	};


    private String point2AddressString(final double latitude,
				       final double longitude) {

	List<Address> listAddress;

	try {
	    listAddress = 
		mGeocoder.getFromLocation(latitude,
					  longitude, 2);
	} catch (IOException e){
	    MyLog.getInstance().error("getFromLocation error. [latitude=" +
		      latitude + "longitude=" + longitude + "]", e);
	    return "";
	}

	if (listAddress.isEmpty()){
	    return "";
	}
	
	String res1 = listAddress.get(0).getAddressLine(1);
	String res2 = listAddress.get(1).getAddressLine(1);

	if (res1 == null) {
	    return "";
	}

	if (res2 == null) {
	    return res1;
	}

	if (res1.length() > res2.length()) {
	    return res1;
	}else{
	    return res2;
	}
    }
}



