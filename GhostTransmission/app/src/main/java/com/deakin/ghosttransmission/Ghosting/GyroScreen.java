package com.deakin.ghosttransmission.Ghosting;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.deakin.ghosttransmission.Listener.GyroscopeListener;

import java.math.BigDecimal;

public class GyroScreen {

    /**
     * Instance Variables
     */
    private SensorManager sensorManager = null;
    private GyroscopeListener gyroscopeListener = null;

    public GyroScreen(SensorManager sensorManager, GyroscopeListener gyroscopeListener) {
        setSensorManager(sensorManager);
        setGyroscopeListener(gyroscopeListener);
    }

    public void AddGyroEventListener() {
        SensorEventListener gyroEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                getGyroscopeListener().onGyroChange(
                        event.values[0] * 57.2958f,
                        event.values[1] * 57.2958f,
                        event.values[2] * 57.2958f);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

        Sensor gyroSensor = getSensorManager().getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        getSensorManager().registerListener(gyroEventListener, gyroSensor, 10000);
    }

    /**
     * Getters and Setters
     */
    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public GyroscopeListener getGyroscopeListener() {
        return gyroscopeListener;
    }

    public void setGyroscopeListener(GyroscopeListener gyroscopeListener) {
        this.gyroscopeListener = gyroscopeListener;
    }
}
