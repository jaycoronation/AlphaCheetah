package cheetah.alphacapital;

import androidx.multidex.MultiDexApplication;

import cheetah.alphacapital.service.ConnectivityReceiver;

public class MyApplication extends MultiDexApplication {
 
    private static MyApplication mInstance;
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        mInstance = this;
    }
 
    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
 
    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
