package com.example.hubianluanzao;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    Camera camera;
    int light_value=5;
    private CameraManager manager;
    protected boolean isopent = false;
    boolean turn_on=false;
    private SensorManager sensorManager;
    //第三步：对传感器信号进行监听
    private SensorEventListener listener = new SensorEventListener() {
        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onSensorChanged(SensorEvent event) {
            //提示当前光照强度
            //Toast.makeText(MainActivity.this,
            //        "当前光照强度：" + event.values[0] + "勒克斯,"+turn_on, Toast.LENGTH_SHORT).show();
            if(event.values[0]>=light_value&&turn_on==true) {
                openOrCloseFlashlight(true);
            }else{
                openOrCloseFlashlight(false);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        ImageButton button1= (ImageButton) findViewById(R.id.buttonmain);
        final TextView textView1=(TextView)findViewById(R.id.text);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(turn_on==true){
                    turn_on=false;
                    textView1.setText("灯泡状态:OFF");
                }else {
                    turn_on=true;
                    textView1.setText("灯泡状态:ON");
                }
            }
        });
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //第二步：获取 Sensor 传感器类型
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //第四步：注册 SensorEventListener
        sensorManager.registerListener(listener,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void openOrCloseFlashlight(boolean flag){
        try{
            if(manager != null){
                manager.setTorchMode("0", flag);//0,指cameraId,通常0代表后置，1代表前置
            }

        }catch(CameraAccessException ex){

            ex.printStackTrace();

        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //传感器使用完毕，释放资源
        camera.release();
        if(sensorManager!=null){
            sensorManager.unregisterListener(listener);
        }
    }

}


