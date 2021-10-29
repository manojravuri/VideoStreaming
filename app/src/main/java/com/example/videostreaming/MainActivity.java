package com.example.videostreaming;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.videostreaming.listener.AccelerometerListener;
import com.example.videostreaming.listener.GyroscopeListener;
import com.example.videostreaming.listener.LightSensorListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.videostreaming.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private SensorManager sensorManager;

    private Sensor gyroscope;

    private Sensor accelerometer;

    private Sensor lightSensor;

    private AccelerometerListener accelerometerListener;

    private GyroscopeListener gyroscopeListener;

    private LightSensorListener lightSensorListener;

    public boolean isHasStartedWriting() {
        return hasStartedWriting;
    }

    public void setHasStartedWriting(boolean hasStartedWriting) {
        this.hasStartedWriting = hasStartedWriting;
    }

    private boolean hasStartedWriting=false;

    public static final String LIGHT_SENSOR_FILE_NAME="light_sensor_data.csv";

    public static final String GYRO_SENSOR_FILE_NAME="gyro_sensor_data.csv";

    public static final String ACCELEROMETER_SENSOR_FILE_NAME="accelerometer_sensor_data.csv";

    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        &&ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            /////Toast.makeText(MainActivity.this, "You have already granted this permission!",
              //      Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        accelerometerListener=new AccelerometerListener(this);

        gyroscopeListener=new GyroscopeListener(this);

        lightSensorListener=new LightSensorListener(this);

        sensorManager.registerListener(accelerometerListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(gyroscopeListener,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        &&ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void startWriting(View view)
    {
        System.out.println("Start button");
        hasStartedWriting=true;
    }

    public void stopWriting(View view)
    {
        System.out.println("Stop button");
         hasStartedWriting=false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}