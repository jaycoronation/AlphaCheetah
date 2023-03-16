package cheetah.alphacapital.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cheetah.alphacapital.R;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.DashboardActivity;
import cheetah.alphacapital.reportApp.activity.TaskDetailsActivity;
import cheetah.alphacapital.reportApp.activity.TaskDetailsActivityNew;
import cheetah.alphacapital.reportApp.activity.TaskListActivity;
import cheetah.alphacapital.reportApp.getset.CommentCountGetSet;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.CommentsGetSet;
import cheetah.alphacapital.reportApp.getset.PushNotificationGetSet;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FireBaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "Alpha Cheetah SERVICE";
    private SessionManager sessionManager;
    private static final String ADMIN_CHANNEL_ID = "admin_channel";
    private NotificationManager notificationManager;
    private ApiInterface apiService;

    //Nougat Grouping
    private final static String GROUP_KEY_BUNDLED = "group_key_bundled";

    //List to manage notification stacking
    public static ArrayList<PushNotificationGetSet> listNotifications;
    private ArrayList<String> conversationIds;
    private ArrayList<Integer> listExistingIds = new ArrayList<>();

    //To display summary text
    private String conversationCount = "", messageCount = "", chatFromName = "";

    private NotificationCompat.Builder notification, builder;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        sessionManager = new SessionManager(this);

        try
        {
            if (!sessionManager.isLoggedIn())
            {
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            Map<String, String> map = remoteMessage.getData();

            JSONObject object = new JSONObject(map);

            System.out.println("###Notification Res:: " + object.toString());

            Log.e("NOTI JSON", "======> " + object.toString());

            if (listNotifications == null || listNotifications.size() == 0)
            {
                listNotifications = new ArrayList<>();
            }

            PushNotificationGetSet pushNotificationGetSet = new PushNotificationGetSet();

            if(object.has("message_id"))
            {
                pushNotificationGetSet.setMessage_id(AppUtils.getValidAPIStringResponse(object.getString("message_id")));
            }

            if(object.has("contentTitle"))
            {
                pushNotificationGetSet.setContentTitle(AppUtils.getValidAPIStringResponse(object.getString("contentTitle")));
            }

            if(object.has("tickerText"))
            {
                pushNotificationGetSet.setTickerText(AppUtils.getValidAPIStringResponse(object.getString("tickerText")));
            }


            if (object.has("message"))
            {
                String abc = AppUtils.getValidAPIStringResponse(object.getString("message"));

                JSONObject messageObj = new JSONObject(abc);

                if(messageObj.has("task_name"))
                {
                    pushNotificationGetSet.setTask_name(AppUtils.getValidAPIStringResponse(messageObj.getString("task_name")));
                }

                if(messageObj.has("user_id"))
                {
                    pushNotificationGetSet.setUser_id(AppUtils.getValidAPIStringResponse(messageObj.getString("user_id")));
                }

                if(messageObj.has("task_id"))
                {
                    pushNotificationGetSet.setTask_id(AppUtils.getValidAPIStringResponse(messageObj.getString("task_id")));
                }

                if(messageObj.has("msg_txt"))
                {
                    pushNotificationGetSet.setMsg_txt(AppUtils.getValidAPIStringResponse(messageObj.getString("msg_txt")));
                }

                if(messageObj.has("user_name"))
                {
                    pushNotificationGetSet.setUser_name(AppUtils.getValidAPIStringResponse(messageObj.getString("user_name")));
                }


                if(messageObj.has("notification_type"))
                {
                    pushNotificationGetSet.setNotification_type(AppUtils.getValidAPIStringResponse(messageObj.getString("notification_type")));
                }

                if(messageObj.has("comment"))
                {
                    if (pushNotificationGetSet.getNotification_type().equalsIgnoreCase("comment"))
                    {
                        pushNotificationGetSet.setNewcommentcount(AppUtils.getValidAPIIntegerResponse(messageObj.getString("newcommentcount")));
                    }
                }

                listNotifications.add(pushNotificationGetSet);
            }

            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Log.e("NOTI JSON", "======> " + pushNotificationGetSet.toString());

            generateNotification(pushNotificationGetSet);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void generateNotification(PushNotificationGetSet pushNotificationGetSet)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            setupChannelsForNormal(pushNotificationGetSet);
        }
        else
        {
            sendNotification(pushNotificationGetSet);
        }
    }


    /***************************************************************************************************/
    private void sendNotification(PushNotificationGetSet pushNotificationGetSet)
    {
        try
        {
            Random generator = new Random();

            int notifRandomId = 1000 - generator.nextInt(1000);

            Intent intent = null;


            if (pushNotificationGetSet.getTask_id().length() > 0)
            {
                intent = new Intent(this, TaskDetailsActivity.class);
                intent.putExtra("taskId", String.valueOf(pushNotificationGetSet.getTask_id()));
                intent.putExtra("isFromNotification", true);
                intent.putExtra("task_added_by", pushNotificationGetSet.getUser_name());
                if (pushNotificationGetSet.getNotification_type().equalsIgnoreCase("comment"))
                {
                    intent.putExtra("isForChat", true);
                }
                else
                {
                    intent.putExtra("isForChat", false);
                }
            }
            else
            {
                intent = new Intent(this, DashboardActivity.class);
            }


            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            CharSequence boldTitle = Html.fromHtml("<b>" + pushNotificationGetSet.getTask_name() + "</b> ");

            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_new);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).setAutoCancel(true).setContentTitle(boldTitle).setContentText(pushNotificationGetSet.getMsg_txt()).setSmallIcon(R.drawable.ic_notification_new).setDefaults(Notification.DEFAULT_ALL).setLargeIcon(icon).setContentIntent(pendingIntent);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.setBigContentTitle(boldTitle);
            bigTextStyle.bigText(pushNotificationGetSet.getMsg_txt());
            notificationBuilder.setStyle(bigTextStyle);

            notificationManager.notify(notifRandomId, notificationBuilder.build());

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannelsForNormal(PushNotificationGetSet pushNotificationGetSet)
    {
        Random generator = new Random();
        int notifRandomId = 1000 - generator.nextInt(1000);

        Intent intent = null;

        try
        {
            if (pushNotificationGetSet.getTask_id().length() > 0)
            {
                intent = new Intent(this, TaskDetailsActivityNew.class);
                intent.putExtra("task_id", String.valueOf(pushNotificationGetSet.getTask_id()));
                intent.putExtra("isFromNotification", true);
                intent.putExtra("task_added_by", pushNotificationGetSet.getUser_name());
                intent.putExtra("isForChat", false);
            }
            else
            {
                intent = new Intent(this, DashboardActivity.class);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pIntent = PendingIntent.getActivity(this, notifRandomId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            CharSequence boldTitle = Html.fromHtml("<b>" + pushNotificationGetSet.getTask_name() + "</b> ");

            CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
            String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

            NotificationChannel adminChannel;
            adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            adminChannel.setDescription(adminChannelDescription);
            adminChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            adminChannel.setShowBadge(true);
            adminChannel.enableLights(true);


            AudioAttributes audioAttributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE).build();
            adminChannel.setLightColor(0xff0000ff);

            adminChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            adminChannel.enableVibration(true);
            adminChannel.setVibrationPattern(new long[]{100, 1000});

            if (notificationManager != null)
            {
                notificationManager.createNotificationChannel(adminChannel);

                Notification notification = new Notification.Builder(this, ADMIN_CHANNEL_ID).setContentTitle(boldTitle).setContentText(pushNotificationGetSet.getMsg_txt()).setColor(Color.parseColor("#019EEB")).setStyle(new Notification.BigTextStyle().bigText(pushNotificationGetSet.getMsg_txt())).setSmallIcon(R.drawable.ic_notification_new).setAutoCancel(true).setContentIntent(pIntent).build();

                notificationManager.notify(notifRandomId, notification);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /***************************************************************************************************/

    /*private PendingIntent createOnDismissedIntent(Context context)
    {
        Intent intent = new Intent(context, PushNotificationDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(),0, intent, 0);
        return pendingIntent;
    }*/
    @Override
    public void onNewToken(@NonNull String token)
    {
        Log.d("TOKEN", "Refreshed token: " + token);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.saveTokenId(token);
        if (sessionManager.isLoggedIn())
        {
            sendRegistrationToServer(token);
        }
        super.onNewToken(token);
    }

    private void sendRegistrationToServer(String token)
    {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.updateDeviceToken(Integer.valueOf(sessionManager.getUserId()), token,"true").enqueue(new Callback<CommentResponse>()
        {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response)
            {

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t)
            {

            }
        });
    }

}
