package cheetah.alphacapital.reportApp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.admin.ActivityTypeListActivity;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.ActivityTypeResponse;
import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;

public class AddActivityTypeActivity extends BaseActivity
{

    @BindView(R.id.ivHeader)
    ImageView ivHeader;
    @BindView(R.id.viewStatusBar)
    View viewStatusBar;
    @BindView(R.id.emptyView)
    View emptyView;
    @BindView(R.id.llBackNavigation)
    LinearLayout llBackNavigation;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.txtTitle)
    CustomTextViewSemiBold txtTitle;
    @BindView(R.id.edtSearch)
    CustomEditText edtSearch;
    @BindView(R.id.ivSerach)
    ImageView ivSerach;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;
    @BindView(R.id.llNotification)
    LinearLayout llNotification;
    @BindView(R.id.ivChangePassword)
    ImageView ivChangePassword;
    @BindView(R.id.ivLogout)
    ImageView ivLogout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pbLoading)
    ProgressBar pbLoading;
    @BindView(R.id.txtLoading)
    AppCompatTextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    AppCompatTextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.edtActivityTypeName)
    CustomAppEditText edtActivityTypeName;
    @BindView(R.id.inputActivityTypeName)
    CustomTextInputLayout inputActivityTypeName;
    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;
    private boolean isStatusBarHidden = false;
    private String isFor = "";
    ActivityTypeResponse.DataBean getSet = new ActivityTypeResponse.DataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
            super.onCreate(savedInstanceState);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        activity = this;

        sessionManager = new SessionManager(activity);

        //AppUtils.setLightStatusBar(activity);

        isFor = getIntent().getStringExtra("isFor");

        getSet = getIntent().getExtras().getParcelable("ActivityTypeResponse");

        setContentView(R.layout.activity_add_activity_type);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();


    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);

            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Activity Type");
            }
            else
            {
                txtTitle.setText("Update Activity Type");
                edtActivityTypeName.setText(String.valueOf(getSet.getActivityTypeName()));
            }

            llBackNavigation.setVisibility(View.VISIBLE);

            /*ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;
            if (isStatusBarHidden)
            {
                height = 56 + 25;
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
            }
            else
            {
                toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);

            setSupportActionBar(toolbar);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    AppUtils.hideKeyboard(toolbar, activity);
                    activity.finish();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hideKeyboard();
                //MitsUtils.hideKeyboard(activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        addActivityType(edtActivityTypeName.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    private boolean checkValidation()
    {
        boolean isValid = true;

        if (edtActivityTypeName.getText().toString().length()==0)
        {
            inputActivityTypeName.setError("Please enter activity type name.");
            isValid = false;
        }

        AppUtils.removeError(edtActivityTypeName, inputActivityTypeName);

        return isValid;
    }

    private void addActivityType(final String activityName)
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>()
            {
                String message = "";
                boolean success = false;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();

                        if (!isFor.equalsIgnoreCase("add"))
                        {
                            hashMap.put("Id", String.valueOf(getSet.getId()));
                        }

                        hashMap.put("ActivityTypeName", activityName);

                        Log.e("add activity type req ", "doInBackground: " + hashMap.toString());

                        String serverResponse = "";

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_ACTIVITY_TYPE, hashMap);
                        }
                        else
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_ACTIVITY_TYPE, hashMap);
                        }

                        Log.e("add activity type resp ", "doInBackground: " + serverResponse);

                        if (serverResponse != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(serverResponse);

                                success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                                message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    llLoading.setVisibility(View.GONE);

                    if (isFor.equalsIgnoreCase("add"))
                    {
                        if (ActivityTypeListActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            ActivityTypeListActivity.handler.sendMessage(message);
                        }
                    }
                    else
                    {
                        getSet.setActivityTypeName(activityName);

                        if (ActivityTypeListActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.obj = getSet;
                            message.what = 101;
                            ActivityTypeListActivity.handler.sendMessage(message);
                        }
                    }

                    finish();
                }
            }.execute();
        }

    }



    @Override
    public void onBackPressed()
    {
        try
        {
            activity.finish();
            //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        super.onBackPressed();
    }
}
