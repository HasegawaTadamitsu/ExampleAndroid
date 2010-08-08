
package jp.ddo.haselab.location.example1.main;

import jp.ddo.haselab.location.example1.util.MyLog;
import jp.ddo.haselab.location.example1.util.MyLocationMgr;
import jp.ddo.haselab.location.example1.util.MyLocation;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * 
 * @author T.Hasegawa
 */
public final class GPSActivity extends  Activity  {

    private MyLocationMgr myLocationMgr;
    private long DEFAULT_LOCATION_KEY = 0;


    /**
     * create. 
     * 各種ボタンのイベント登録など行います。
     * 
     * @param savedInstanceState
     *            hmm
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.getInstance().verbose("start");
        setContentView(R.layout.gps);
    }

    @Override
    protected void onResume(){
	super.onResume();
        MyLog.getInstance().verbose("start");
	myLocationMgr = new MyLocationMgr(this);
	myLocationMgr.scan(DEFAULT_LOCATION_KEY,
				  locationCallback);
    }

    @Override
    protected void onPause(){
	super.onPause();
        MyLog.getInstance().verbose("start");
	if( myLocationMgr != null){
	    myLocationMgr.cancel();
	    myLocationMgr = null;
	}
    }

    @Override
    protected void onDestroy(){
	super.onDestroy();
        MyLog.getInstance().verbose("start");
	if( myLocationMgr != null){
	    myLocationMgr.cancel();
	    myLocationMgr = null;
	}
    }

    final MyLocationMgr.Callback locationCallback =
  	  new MyLocationMgr.Callback(){
	@Override
        public void doneGet(final MyLocation arg){
	    MyLog.getInstance().verbose("start");
	    if (arg != null) {
		MyLog.getInstance().verbose("data is null");
		setTextValue(arg);
	    }
	    if (myLocationMgr != null) {
		myLocationMgr.scan(DEFAULT_LOCATION_KEY,
				   locationCallback);
	    }
	}
    };


    private void setTextValue(final MyLocation data){
	TextView text = (TextView)findViewById(R.id.gps_textview_address);
        text.setText(data.getAddress());

	text = (TextView)findViewById(R.id.gps_textview_latitude);
        text.setText(""+ data.getLatitude());

	text = (TextView)findViewById(R.id.gps_textview_longitude);
        text.setText("" + data.getLongitude());

	text = (TextView)findViewById(R.id.gps_textview_altitude);
        text.setText("" + data.getAltitude());

	text = (TextView)findViewById(R.id.gps_textview_accuracy);
        text.setText("" + data.getAccuracy());

	text = (TextView)findViewById(R.id.gps_textview_speed);
        text.setText("" + data.getSpeed());

	text = (TextView)findViewById(R.id.gps_textview_bearing);
        text.setText("" + data.getBearing());

    }


}
