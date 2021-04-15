package com.example.iuam_idache.classes;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.iuam_idache.activities.MainActivity;

import org.reactivestreams.Publisher;

import java.util.Set;
import java.util.UUID;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import polar.com.sdk.api.PolarBleApi;
import polar.com.sdk.api.PolarBleApiCallback;
import polar.com.sdk.api.PolarBleApiDefaultImpl;
import polar.com.sdk.api.errors.PolarInvalidArgument;
import polar.com.sdk.api.model.PolarAccelerometerData;
import polar.com.sdk.api.model.PolarDeviceInfo;
import polar.com.sdk.api.model.PolarHrData;
import polar.com.sdk.api.model.PolarSensorSetting;

public class Polar0H1 {
    Context context;
    public CallbackPolar cb;

    PolarBleApi api;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String API_LOGGER_TAG = "API LOGGER";

    Disposable ecgDisposable;
    Disposable accDisposable;
    Disposable gyrDisposable;
    Disposable magDisposable;
    Disposable ppgDisposable;
    Disposable ppiDisposable;

    String DEVICE_ID = "7D41F628";


    public Polar0H1(CallbackPolar myPolarCB, String device_id, Context this_con) {
        cb = myPolarCB;
        DEVICE_ID = device_id;
        context = this_con;
    }

    public void init() {
        // Notice PolarBleApi.ALL_FEATURES are enabled
        api = PolarBleApiDefaultImpl.defaultImplementation(context, PolarBleApi.ALL_FEATURES);
        api.setPolarFilter(false);

        api.setApiLogger(s -> Log.d(API_LOGGER_TAG, s));

        Log.d(TAG, "version: " + PolarBleApiDefaultImpl.versionInfo());

        api.setApiCallback(new PolarBleApiCallback() {
            @Override
            public void blePowerStateChanged(boolean powered) {
                Log.d(TAG, "BLE power: " + powered);
            }

            @Override
            public void deviceConnected(@NonNull PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "CONNECTED: " + polarDeviceInfo.deviceId);
                DEVICE_ID = polarDeviceInfo.deviceId;
            }

            @Override
            public void deviceConnecting(@NonNull PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "CONNECTING: " + polarDeviceInfo.deviceId);
                DEVICE_ID = polarDeviceInfo.deviceId;
            }

            @Override
            public void deviceDisconnected(@NonNull PolarDeviceInfo polarDeviceInfo) {
                Log.d(TAG, "DISCONNECTED: " + polarDeviceInfo.deviceId);
                ecgDisposable = null;
                accDisposable = null;
                gyrDisposable = null;
                magDisposable = null;
                ppgDisposable = null;
                ppiDisposable = null;
            }

            @Override
            public void streamingFeaturesReady(@NonNull final String identifier,
                                               @NonNull final Set<PolarBleApi.DeviceStreamingFeature> features) {
                for(PolarBleApi.DeviceStreamingFeature feature : features) {
                    Log.d(TAG, "Streaming feature " + feature.toString() + " is ready");
                }
            }

            @Override
            public void hrFeatureReady(@NonNull String identifier) {
                Log.d(TAG, "HR READY: " + identifier);
                // hr notifications are about to start
            }

            @Override
            public void disInformationReceived(@NonNull String identifier, @NonNull UUID uuid, @NonNull String value) {
                Log.d(TAG, "uuid: " + uuid + " value: " + value);
            }

            @Override
            public void batteryLevelReceived(@NonNull String identifier, int level) {
                Log.d(TAG, "BATTERY LEVEL: " + level);
            }

            @Override
            public void hrNotificationReceived(@NonNull String identifier, @NonNull PolarHrData data) {
                //Log.d(TAG, "HR value: " + data.hr + " rrsMs: " + data.rrsMs + " rr: " + data.rrs + " contact: " + data.contactStatus + "," + data.contactStatusSupported);
                cb.getHr(data.hr);
            }

            @Override
            public void polarFtpFeatureReady(@NonNull String s) {
                Log.d(TAG, "FTP ready");
            }
        });

    }

    public void connect() {
        try {
            api.connectToDevice(DEVICE_ID);
        } catch (PolarInvalidArgument polarInvalidArgument) {
            polarInvalidArgument.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            api.disconnectFromDevice(DEVICE_ID);
        } catch (PolarInvalidArgument polarInvalidArgument) {
            polarInvalidArgument.printStackTrace();
        }
    }

    public void getStreamACC(){
        if (accDisposable == null) {
            accDisposable = api.requestStreamSettings(DEVICE_ID, PolarBleApi.DeviceStreamingFeature.ACC)
                    .toFlowable()
                    .flatMap((Function<PolarSensorSetting, Publisher<PolarAccelerometerData>>) settings -> {
                        PolarSensorSetting sensorSetting = settings.maxSettings();
                        return api.startAccStreaming(DEVICE_ID, sensorSetting);
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            polarAccelerometerData -> {

                                for (PolarAccelerometerData.PolarAccelerometerDataSample data : polarAccelerometerData.samples) {

                                    cb.getACC(data.x,data.y,data.z);
                                    //Log.d(TAG, "    x: " + data.x + " y: " + data.y + " z: " + data.z);

                                }
                            },
                            throwable -> Log.e(TAG, "" + throwable),
                            () -> Log.d(TAG, "complete")
                    );
        } else {
            // NOTE dispose will stop streaming if it is "running"
            accDisposable.dispose();
            accDisposable = null;
        }
    }

    public void foregroundEntered() {
        api.foregroundEntered();
    }

    public void backgroundEntered() {
        api.backgroundEntered();
    }

    public void shutdown() {
        api.shutDown();
    }
}
