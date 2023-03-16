package cheetah.alphacapital;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.DashboardActivity;
import cheetah.alphacapital.reportApp.activity.EmployeeLoginActivity;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends Activity
{
    private Activity activity;
    private SessionManager sessionManager;
    private View ivLogo;
    private CountDownTimer timer;
    private ApiInterface apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        activity = SplashActivity.this;

        apiService = ApiClient.getClient().create(ApiInterface.class);

        sessionManager = new SessionManager(activity);

        AppUtils.saveDeviceInfo(activity);

        setupViews();

        setupData();

        goThrough();

    }

    private void setupViews()
    {
        ivLogo = findViewById(R.id.ivLogo);
    }

    private void setupData()
    {
        try
        {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.splash_anim);
            ivLogo.setAnimation(animation);
            ivLogo.startAnimation(animation);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void goThrough()
    {
        timer = new CountDownTimer(2500, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
            }

            @Override
            public void onFinish()
            {
                try
                {
                    if (sessionManager.isLoggedIn())
                    {
                        //changed
                        Intent intent = new Intent(activity, DashboardActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        activity.finish();
                    }
                    else
                    {
                        Intent intent = new Intent(activity, EmployeeLoginActivity.class);
                        activity.startActivity(intent);
                        activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
                        activity.finish();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        timer.start();
    }
}
