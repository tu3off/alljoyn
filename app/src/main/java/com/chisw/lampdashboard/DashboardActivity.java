package com.chisw.lampdashboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.alljoyn.bus.BusAttachment;
import org.alljoyn.bus.BusListener;
import org.alljoyn.bus.alljoyn.DaemonInit;

public class DashboardActivity extends AppCompatActivity {

    private Handler busHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        HandlerThread busThread = new HandlerThread("BusHandlerThread");
        busThread.start();
        busHandler = new BusHandler(busThread.getLooper(), this);
        busHandler.sendEmptyMessage(BusHandler.CONNECT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        busHandler.sendEmptyMessage(BusHandler.DISCONNECT);
    }

    private static class BusHandler extends Handler {
        public static final int CONNECT = 0;
        public static final int DISCONNECT = 1;

        private final Context context;
        private BusAttachment bus;

        public BusHandler(Looper looper, Context context) {
            super(looper);
            this.context = context.getApplicationContext();
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONNECT:
                    connect();
                    break;
                case DISCONNECT:
                    disconnect();
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        private void disconnect() {
            bus.disconnect();
            getLooper().quit();
        }

        private void connect() {
            DaemonInit.PrepareDaemon(context);
            bus = new BusAttachment(getClass().getName(), BusAttachment.RemoteMessage.Receive);
            bus.registerBusListener(new AdvertiseListener());
            bus.connect();
        }
    }

    private static class AdvertiseListener extends BusListener {
        @Override
        public void foundAdvertisedName(String name, short transport, String namePrefix) {
            Log.d("AdvertiseListener", ("Name: " + name + " ; transport: " + String.valueOf(transport) + " ; prefix: " + namePrefix));
        }
    }
}
