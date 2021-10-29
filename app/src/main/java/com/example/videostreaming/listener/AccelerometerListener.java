package com.example.videostreaming.listener;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.ArrayMap;
import android.util.Log;

import com.example.videostreaming.MainActivity;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AccelerometerListener implements SensorEventListener {

    String TAG="accelerometerLog";

    MainActivity mainActivity;

    public AccelerometerListener(MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)  {
        Log.d(TAG, "acceleration change: X:"+sensorEvent.values[0]+"  Y:"+sensorEvent.values[1]+"Z:  "+sensorEvent.values[2]);
        if(mainActivity.isHasStartedWriting())
        {
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = mainActivity.ACCELEROMETER_SENSOR_FILE_NAME;
            String filePath = baseDir + File.separator + fileName;
            //Log.d("File path",filePath);
            File f = new File(filePath);
            CSVWriter writer;
            FileWriter mFileWriter;
            try{
            // File exist
            if(f.exists()&&!f.isDirectory())
            {
                    mFileWriter = new FileWriter(filePath, true);
                    writer = new CSVWriter(mFileWriter);
            }
            else
            {
                f.createNewFile();
                writer = new CSVWriter(new FileWriter(filePath));
            }

            float[] sensorValues = (sensorEvent.values);
            String[] data=new String[sensorValues.length+1];
            SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
            data[0] = jdf.format(new Date(System.currentTimeMillis()));
            for(int i=0;i<sensorValues.length;i++)
            {
                data[i+1]=String.valueOf(sensorValues[i]);
            }
            writer.writeNext(data);


                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
