
package jp.ddo.haselab.bluetooth.example1.main;

import jp.ddo.haselab.bluetooth.example1.util.MyLog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 主処理Activity. main 画面の処理を行います。
 * 
 * @author T.Hasegawa
 */
public final class MainActivity extends
        Activity implements
        OnClickListener {


    /**
     * クリック時の処理. 各種ボタンの処理を行います。
     * 
     * @param v
     *            押されたview
     */
    @Override
    public void onClick(final View v) {

        int id = v.getId();
        if (id == R.id.button_start) {
            MyLog.getInstance().verbose("start button");
            Intent intent = new Intent(this, ResultActivity.class);
            startActivity(intent);
            return;
        }
        return;
    }

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

        setContentView(R.layout.main);

        Button button;
        button = (Button) findViewById(R.id.button_start);
        button.setOnClickListener(this);
    }

}
