package jp.ddo.haselab.bluetooth.example1.main;

import jp.ddo.haselab.bluetooth.example1.util.MyLog;
import jp.ddo.haselab.bluetooth.example1.util.MyBluetooth;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Map;

/**
 * 
 */
public final class ResultActivity extends Activity {

    MyBluetooth mBluetooth;
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

	this.mBluetooth = new MyBluetooth(this);
        TextView text;
        text = (TextView) findViewById(R.id.textview_result);

	boolean res = this.mBluetooth.init();
	if (res == false){
	    text.setText("can not init");
	    return;
	}

	mBluetooth.scan(mCallBack);
	return ;
    }

    MyBluetooth.Callback mCallBack = new MyBluetooth.Callback() {

	    @SuppressWarnings("synthetic-access")
                @Override
                public void doneGet(final Map<String,String>  arg) {

		TextView text;
		text = (TextView) findViewById(R.id.textview_result);
		
		if (arg.size() == 0) {
		    text.setText("empty devices.");
		    return;
		}

		StringBuilder str = new StringBuilder();
		for(Map.Entry<String, String> e : arg.entrySet()) {
		    str.append(e.getKey() + " : " + e.getValue() + "--");
		}
		text.setText(str.toString());
		
		Toast.makeText(ResultActivity.this,
			       "OK",
			       Toast.LENGTH_SHORT)
		    .show();
		return;
	    }
	};
}
