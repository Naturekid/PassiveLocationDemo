package com.example.cai.passivelocationdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends Activity {

    private final String TAG = "MainActivity";
    private TextView mTextView;
    private Button mButton;
    private EditText mEditMinDis;
    private EditText mEditMinTime;
    private Handler mHandler;
    private int minDis = 10;//米
    private int minTime = 10000;//毫秒
    private String locStr = "";
    private String statFile ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        mTextView = (TextView) findViewById(R.id.location);
        mButton = (Button) findViewById(R.id.locationBtn);
        mEditMinDis = (EditText) findViewById(R.id.minDis);
        mEditMinTime = (EditText) findViewById(R.id.minTime);
        mEditMinDis.setHint(Integer.toString(minDis));
        mEditMinTime.setHint(Integer.toString(minTime));

        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mButton.setText("被动定位中……");
                if(mEditMinDis.getText()!=null && !mEditMinDis.getText().toString().equals(""))
                    minDis = Integer.parseInt(mEditMinDis.getText().toString());
                if(mEditMinTime.getText()!=null && !mEditMinTime.getText().toString().equals(""))
                    minTime = Integer.parseInt(mEditMinTime.getText().toString());
                requestPassiveLocationUpdates();
            }
        });

        mHandler = new Handler() {
            @Override
            public void dispatchMessage(Message msg) {
                // TODO Auto-generated method stub
                super.dispatchMessage(msg);
                Location location = (Location) msg.obj;
                switch (msg.what) {
                    case 1:
                        if (location != null) {
                            locStr += "\nGPS:" + location.getLatitude() + "," + location.getLongitude() + "," + System.currentTimeMillis();
                            mTextView.setText(locStr);
                            mButton.setText("Passive Location");
                        }
                        break;
                    case 2:
                        if (location != null) {
                            locStr += "\nNetwork:" + location.getLatitude() + "," + location.getLongitude() + "," + System.currentTimeMillis();
                            mTextView.setText(locStr);
                            mButton.setText("Passive Location");
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }


    public void requestPassiveLocationUpdates() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub
                Log.e(TAG, "[Passive]" + provider + "onStatusChanged");
            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub
                Log.e(TAG, "[Passive]" + provider + "onProviderEnabled");
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub
                Log.e(TAG, "[Passive]" + provider + "onProviderDisabled");
            }

            @Override
            public void onLocationChanged(Location location) {
                // TODO Auto-generated method stub
                Log.e(TAG, "[Passive]-onLocationChanged");
                Message msg = new Message();
                // GPS
                if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
                    msg.what = 1;
                    msg.obj = location;
                    mHandler.sendMessage(msg);
                } else if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
                    // 网络
                    msg.what = 2;
                    msg.obj = location;
                    mHandler.sendMessage(msg);
                }
            }
        };

        // 采用被动定位，为了省电
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                minTime, minDis, listener);
    }


    private void saveData(String str){

        Date now = new Date();
        Calendar cal = Calendar.getInstance();

        statFile = this.getFilesDir() + "/" + cal.toString() + "_stat";

        File toFile = new File(statFile);
        if(!toFile.exists()){

        }



    }

}