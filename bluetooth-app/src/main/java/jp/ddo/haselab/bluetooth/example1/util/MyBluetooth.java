package jp.ddo.haselab.bluetooth.example1.util;

import backport.android.bluetooth.BluetoothAdapter;
import backport.android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author hasegawa
 * 
 */
public final class MyBluetooth {

    /**
     * callback interface
     */
    public static interface Callback {
        /**
         * 取得完了コールバック
         */
        public void doneScan(final Map<String,String> arg);
    }


    public MyBluetooth(final Context arg){
	mContext = arg;
	mSearchedDevice = new HashMap<String,String>();
    }

    private BluetoothAdapter   mBluetoothAdapter;
    private Map<String,String> mSearchedDevice;
    private Context            mContext;

    public Map<String,String> getSearchedDevice(){
	return mSearchedDevice;
    }

    private void checkCalledInit(){
	if (mBluetoothAdapter == null) {
	    throw new IllegalStateException(
			"before called init.");
	}
    }

    public String getAdapterDeviceName(){
	checkCalledInit();
	return  mBluetoothAdapter.getName();
    }

    public boolean init(){
	MyLog.getInstance().verbose("start");
	this.mBluetoothAdapter =
	    BluetoothAdapter.getDefaultAdapter();

	// todo throws exception
	if (mBluetoothAdapter == null) {
	    MyLog.getInstance().verbose("can not use Bluetooth adpter.");
	    return  false;
	}

	// todo throws exception
	if (!mBluetoothAdapter.isEnabled()){
	    MyLog.getInstance().verbose("adapter is not enable."
				+ "[deviceName = " + getAdapterDeviceName()
				+ "]");
	    mBluetoothAdapter = null;
	    return  false;
	}	    
	return true;
    }
    

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    public void onReceive(final Context context,
				  final Intent intent) {
		MyLog.getInstance().verbose("start");
		String action = intent.getAction();
		if ( ! BluetoothDevice.ACTION_FOUND.equals(action)) {
		    return;
		}
		BluetoothDevice device =
		    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		mSearchedDevice.put(device.getAddress(),device.getName());
	    }
	};

    public boolean scan(final Callback callback){
	MyLog.getInstance().verbose("start");
	checkCalledInit();
	mSearchedDevice.clear();

	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	mContext.registerReceiver(mReceiver, filter); 
	boolean result = mBluetoothAdapter.startDiscovery();
	if (result == false){
	    return false;
	}
	final Handler handler = new Handler();
	final Runnable postRun = new Runnable() {
		public void run(){
		    callback.doneScan(mSearchedDevice);
		} // run{
	    }; //Runable()

	final Runnable threadRun =  new Runnable() {
		public void run() {
		    try{
			Thread.sleep(10 * 1000);
		    }catch(InterruptedException e){
			MyLog.getInstance().error("InterruptedEx",e);
		    }
		    finScan();
		    handler.post( postRun );
		} // run() {
	    }; // new runnable()
	
	     
	Thread th = new Thread( threadRun );
	th.start();
	return true;
    }

    public boolean isDiscovering  (){
	//MyLog.getInstance().verbose("start");
	checkCalledInit();
	return mBluetoothAdapter.isDiscovering();
    }

    public boolean cancel(){
	if (!isDiscovering()){
	    return true;
	}
	return mBluetoothAdapter.cancelDiscovery();
    }

    private void finScan(){
	MyLog.getInstance().verbose("start");
 	checkCalledInit();
	boolean result = mBluetoothAdapter.cancelDiscovery();
	mContext.unregisterReceiver(mReceiver);
    }
}

