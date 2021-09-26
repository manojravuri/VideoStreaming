package com.example.videostreaming;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.example.videostreaming.listener.AccelerometerListener;
import com.example.videostreaming.listener.GyroscopeListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.videostreaming.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private SensorManager sensorManager;

    private Sensor gyroscope;

    private Sensor accelerometer;

    private AccelerometerListener accelerometerListener;

    private GyroscopeListener gyroscopeListener;

    public boolean isHasStartedWriting() {
        return hasStartedWriting;
    }

    public void setHasStartedWriting(boolean hasStartedWriting) {
        this.hasStartedWriting = hasStartedWriting;
    }

    private boolean hasStartedWriting=false;

    public static final String FILE_NAME="sensor_data.csv";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        accelerometerListener=new AccelerometerListener(this);

        gyroscopeListener=new GyroscopeListener(this);

        sensorManager.registerListener(accelerometerListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(gyroscopeListener,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);


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