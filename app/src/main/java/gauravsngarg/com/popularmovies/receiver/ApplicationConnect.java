package gauravsngarg.com.popularmovies.receiver;

import android.app.Application;

/**
 * Created by GG on 28/07/18.
 */

public class ApplicationConnect extends Application{

    private static ApplicationConnect mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ApplicationConnect getInstance(){
        return mInstance;
    }

    public  void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener){
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
