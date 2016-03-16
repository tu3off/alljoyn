package com.chisw.lampdashboard;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.alljoyn.bus.*;
import org.alljoyn.bus.alljoyn.DaemonInit;

import java.util.Map;

public class DashboardActivity extends AppCompatActivity {

    static {
        System.loadLibrary("alljoyn_java");
    }

    private Handler busHandler;
    private BusAttachment bus;

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

    private class BusHandler extends Handler {
        public static final int CONNECT = 0;
        public static final int DISCONNECT = 1;

        private final Context context;


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
            bus = new BusAttachment(context.getString(R.string.app_name), BusAttachment.RemoteMessage.Receive);
            bus.connect();
            bus.registerBusListener(new AdvertiseListener());
            bus.registerAboutListener(new MyAboutListener());
            String[] s = new String[]{};
            bus.whoImplements(s);


        }
    }

    private static class AdvertiseListener extends BusListener {
        @Override
        public void foundAdvertisedName(String name, short transport, String namePrefix) {
            Log.d("AdvertiseListener", ("Name: " + name + " ; transport: " + String.valueOf(transport) + " ; prefix: " + namePrefix));
        }
    }

    class MyAboutListener implements AboutListener {
        public void announced(String busName, int version, short port, AboutObjectDescription[] objectDescriptions, Map<String, Variant> aboutData) {
            // Place code here to handle Announce signal.
            Log.d("MyAboutListener", busName);
            Log.d("MyAboutListener", "version:" + version);
            Log.d("MyAboutListener", "port:" + port);

            SessionOpts sessionOpts = new SessionOpts();
            sessionOpts.traffic = SessionOpts.TRAFFIC_MESSAGES;
            sessionOpts.isMultipoint = true;
            sessionOpts.proximity = SessionOpts.PROXIMITY_ANY;
            sessionOpts.transports = SessionOpts.TRANSPORT_ANY;
            final Mutable.ShortValue sPort = new Mutable.ShortValue(port);
            bus.bindSessionPort(sPort,sessionOpts,new SessionPortListener(){
                @Override
                public boolean acceptSessionJoiner(short i, String s, SessionOpts sessionOpts) {
                    return super.acceptSessionJoiner(i, s, sessionOpts);
                }

                @Override
                public void sessionJoined(short i, int i1, String s) {
                    super.sessionJoined(i, i1, s);
                }
            });
        }
    }
}
