package com.crux.qxm;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

//import com.crashlytics.android.Crashlytics;
import com.crux.qxm.db.realmModels.userSessionTracker.UserActivitySessionTracker;
import com.crux.qxm.db.realmService.RealmService;
import com.crux.qxm.di.appFeature.component.AppComponent;
import com.crux.qxm.di.appFeature.component.DaggerAppComponent;
import com.crux.qxm.di.module.ContextModule;
import com.crux.qxm.utils.notification.QxmNotificationOpenedHandler;
import com.crux.qxm.utils.notification.QxmNotificationReceivedHandler;
import com.onesignal.OneSignal;

//import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class App extends MultiDexApplication {
    private static final String TAG = "QxmApp";
    //    private RefWatcher refWatcher;
    private AppComponent appComponent;

    public static App get(AppCompatActivity activity) {
        return (App) activity.getApplication();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initializing crashlytics for facebook
//        Fabric.with(this, new Crashlytics());

        // region Dagger Initialization
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(this))
                .build();
        // endregion


        // region LeakCanary Initialization
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }*/
        // refWatcher = LeakCanary.install(this);
        // Normal app init code...

        // endregion
        initializeRealm();

        // TODO:: createNotificationChannels MVP2
//        createNotificationChannels();

        // region OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new QxmNotificationReceivedHandler(this))
                .setNotificationOpenedHandler(new QxmNotificationOpenedHandler(this))
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        // endregion

    }

    // region CreateNotificationChannels MVP-2
    /*

    private void createNotificationChannels() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            List<NotificationChannel> qxmNotificationChannels = new ArrayList<>();

            // FOLLOWER NOTIFICATION CHANNEL
            NotificationChannel channelFollow = new NotificationChannel(
                    CHANNEL_FOLLOWER_ID,
                    CHANNEL_FOLLOWER_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channelFollow.setDescription(getString(R.string.channel_follower_description));
            qxmNotificationChannels.add(channelFollow);

            // QXM NOTIFICATION CHANNEL
            NotificationChannel channelQxm = new NotificationChannel(
                    CHANNEL_QXM_ID,
                    CHANNEL_QXM_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelQxm.setDescription(getString(R.string.channel_qxm_description));
            qxmNotificationChannels.add(channelQxm);

            // QXM PRIVATE NOTIFICATION CHANNEL
            NotificationChannel channelPrivateQxm = new NotificationChannel(
                    CHANNEL_PRIVATE_QXM_ID,
                    CHANNEL_PRIVATE_QXM_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelPrivateQxm.setDescription(getString(R.string.channel_private_qxm_description));
            qxmNotificationChannels.add(channelPrivateQxm);

            // QXM ENROLL NOTIFICATION CHANNEL
            NotificationChannel channelEnroll = new NotificationChannel(
              CHANNEL_ENROLL_ID,
              CHANNEL_ENROLL_NAME,
              NotificationManager.IMPORTANCE_HIGH
            );
            channelEnroll.setDescription(getString(R.string.channel_enroll_description));
            qxmNotificationChannels.add(channelEnroll);

            // QXM EVALUATION NOTIFICATION CHANNEL
            NotificationChannel channelEvaluation = new NotificationChannel(
                    CHANNEL_EVALUATION_ID,
                    CHANNEL_EVALUATION_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            channelEvaluation.setDescription(getString(R.string.channel_evaluation_description));
            qxmNotificationChannels.add(channelEvaluation);

            // QXM RESULT NOTIFICATION CHANNEL
            NotificationChannel channelResult = new NotificationChannel(
                    CHANNEL_RESULT_ID,
                    CHANNEL_RESULT_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channelResult.setDescription(getString(R.string.channel_result_description));
            qxmNotificationChannels.add(channelResult);

            // QXM GROUP NOTIFICATION CHANNEL
            NotificationChannel channelGroup = new NotificationChannel(
                    CHANNEL_GROUP_ID,
                    CHANNEL_GROUP_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channelGroup.setDescription(getString(R.string.channel_group_description));
            qxmNotificationChannels.add(channelGroup);

            NotificationManager managerCompat = getSystemService(NotificationManager.class);
            Objects.requireNonNull(managerCompat).createNotificationChannels(qxmNotificationChannels);
        }
    }
    */
    // endregion

    // region InitializeRealm

    private void initializeRealm() {
        //Security key
        char[] chars = "QXM.DB-Password^Key4RealmCRUXApp".toCharArray();
        byte[] key = new byte[chars.length * 2];
        for (int i = 0; i < chars.length; i++) {
            key[i * 2] = (byte) (chars[i] >> 8);
            key[i * 2 + 1] = (byte) chars[i];
        }

        /* realmConfiguration */
        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .encryptionKey(key)
                .name("qxmdb.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);

        initUserSession();
    }

    private void initUserSession() {
        RealmService realmService = new RealmService(Realm.getDefaultInstance());
        if (realmService.getUserActivitySessionTrackerData() == null) {
            realmService.saveUserActivitySessionTracker(new UserActivitySessionTracker());
        }
    }

    // endregion

    public AppComponent getAppComponent() {
        return appComponent;
    }

    /*public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }*/
}


