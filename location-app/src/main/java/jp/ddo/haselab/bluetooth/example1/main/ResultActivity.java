package jp.ddo.haselab.bluetooth.example1.main;

import jp.ddo.haselab.bluetooth.example1.util.MyLog;
import jp.ddo.haselab.bluetooth.example1.util.MyBluetooth;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.util.Map;

/**
 * 
 */
public final class ResultActivity extends Activity {

    private MyBluetooth mBluetooth;
    private ProgressDialog mProgressDialog;
    private static final String MSSAGE ="scan";

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
        setContentView(R.layout.result);

	final int timeoutTimeSecond = 5;
	this.mBluetooth = new MyBluetooth(this,timeoutTimeSecond);
        TextView text;
        text = (TextView) findViewById(R.id.textview_result);

	boolean res = this.mBluetooth.init();
	if (res == false){
	    text.setText("can not init");
	    return;
	}

	boolean resScan = this.mBluetooth.scan(mCallBack);
	if (resScan == false){
	    text.setText("can not scan.startDiscovery");
	    return;
	}
 
	mProgressDialog = new ProgressDialog(this);
	mProgressDialog.setTitle("scan...");
	mProgressDialog.setMessage(MSSAGE);
	mProgressDialog.setIndeterminate(false);
	mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	mProgressDialog.setCancelable(true);
	mProgressDialog.setMax(timeoutTimeSecond);
	mProgressDialog.setButton("cancel",
	    new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int hoge) {
		    mBluetooth.cancel();
		}
	    }
					    );
	mProgressDialog.show();

	return ;
    }


    MyBluetooth.Callback mCallBack = new MyBluetooth.Callback() {

	    @SuppressWarnings("synthetic-access")
                @Override
                public void doneScan(final Map<String,String>  arg) {
		mProgressDialog.dismiss();

		TextView text;
		text = (TextView) findViewById(R.id.textview_result);
		
		if (arg.size() == 0) {
		    text.setText("fin scan. but empty devices.");
		    return;
		}

		StringBuilder str = new StringBuilder();
		for(Map.Entry<String, String> e : arg.entrySet()) {
		    str.append(e.getKey() + " : " + e.getValue() + "\n");
		}
		text.setText(str.toString());
		
		return;
	    }
	    @Override
                public void progress(final float var,
				     final int findCount){
		mProgressDialog.setProgress((int)var);
		if(findCount != 0){
		    mProgressDialog.setMessage(MSSAGE
					       + "  "
					       + findCount 
					       +" found");
		}
	    }
	};
}
