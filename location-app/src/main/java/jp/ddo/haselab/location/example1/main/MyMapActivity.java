
package jp.ddo.haselab.location.example1.main;

import java.util.List;

import jp.ddo.haselab.location.example1.util.MyLog;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * 
 */
public final class MyMapActivity extends
        MapActivity {

    // マップの初期位置
    private static final GeoPoint FIRST_POINT = new GeoPoint(35364787,
                                                                138729758);

    // マップの初期拡大率
    private static final int FIRST_ZOOM_LEVEL = 8;

    private MapView mMapView;

    private MapController mMapController = null;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymap);

        MyLog.getInstance().verbose("start");

        this.mMapView = (MapView) findViewById(R.id.mymap_map);
        this.mMapView.setClickable(true);
        this.mMapView.setBuiltInZoomControls(true);

        this.mMapController = this.mMapView.getController();
        this.mMapController.animateTo(FIRST_POINT);
        this.mMapController.setZoom(FIRST_ZOOM_LEVEL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
