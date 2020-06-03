package com.deakin.ghosttransmission.Ghosting.Visual;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.deakin.ghosttransmission.Listener.GyroscopeListener;

import java.math.BigDecimal;

public class GyroScreen {

    /**
     * Constants
     */
    float RAD_TO_DEG = 57.2958f;

    /**
     * Instance Variables
     */
    private SensorManager sensorManager = null;
    private GyroscopeListener gyroscopeListener = null;

    /**
     * Constructor
     *
     * @param sensorManager SensorManager instance
     * @param gyroscopeListener Gyroscope Listener for Updating UI
     */
    public GyroScreen(SensorManager sensorManager, GyroscopeListener gyroscopeListener) {
        setSensorManager(sensorManager);
        setGyroscopeListener(gyroscopeListener);
    }

    public void AddGyroEventListener() {
        SensorEventListener gyroEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // The new values of x, y, and z are converted from Radians to Degrees. This means the
                // input is the Rate of Change in Degrees/Second instead of the Rate of Change in Radians/Second
                getGyroscopeListener().onGyroChange(
                        event.values[0] * RAD_TO_DEG,
                        event.values[1] * RAD_TO_DEG,
                        event.values[2] * RAD_TO_DEG);
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
