package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.reportApp.fragment.ClientDARFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportListFragment;
import cheetah.alphacapital.reportApp.getset.DARResponse;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.reportApp.getset.RMlistResponse;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse.DataBean.AllClientByEmployeeBean;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.DARDetailsReportListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddDarEntryActivity extends BaseActivity {
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private View viewStatusBar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private EditText edtDARMessage;
    private CustomTextInputLayout inputedtClient, inputDARMessage;
    private EditText edtClient;
    private CustomTextInputLayout inputActivityType;
    private EditText edtActivityType;
    private CustomTextInputLayout inputHours;
    private EditText edtHours;
    private CustomTextInputLayout inputMinutes;
    private EditText edtMinutes;
    private CustomTextInputLayout inputRMName;
    private EditText edtRMName;
    private CustomTextInputLayout inputRemarksComment;
    private EditText edtRemarksComment;
    private LinearLayout llSubmit, llClientSelection, llReportMain;
    private TextView txtBtnSubmit;
    private RadioGroup rgIsTodayReport;
    private RadioButton rbToday;
    private RadioButton rbYesterday;
    private RadioButton rbDayBeforeYesterday;
    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClient = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();
    private ArrayList<CommonGetSet> listActivityType = new ArrayList<CommonGetSet>();
    private List<RMlistResponse.DataBean> listRMName = new ArrayList<>();
    private ArrayList<String> listHours = new ArrayList<String>();
    private ArrayList<String> listMinutes = new ArrayList<String>();
    private HoursListAdpater hoursListAdpater;
    private ClientListAdapter clientListAdapter;
    private RMListAdapter rmListAdapter;
    private ActivityTypeAdpater activityTypeAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isStatusBarHidden = false;
    private String status_id = "", status_name = "", activity_type_id = "", status_activity_type_name = "", selectedClientId = "0", selectedClientName = "", selectedDueDate = "";
    private Dialog dialog;

    boolean isEditClient = false;
    String clientId = "";
    private String isFor = "";
    private DARResponse.DataBean editBean = new DARResponse.DataBean();

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
        AppUtils.setLightStatusBar(activity);
        setContentView(R.layout.activity_add_dar);

        isFor = getIntent().getStringExtra("isFor");
        isEditClient = getIntent().getBooleanExtra("isEditClient", false);

        if (isFor.equals("edit"))
        {
            editBean = getIntent().getExtras().getParcelable("data");
        }

        setupViews();

        if (isEditClient)
        {
            llClientSelection.setVisibility(View.VISIBLE);
        }
        else
        {
            llClientSelection.setVisibility(View.GONE);
            clientId = getIntent().getStringExtra("clientId");
            Log.d("<><> clientid DAR:", clientId);
        }

        if (sessionManager.getClientList().length() > 0 && sessionManager.getActivityList().length() > 0 && sessionManager.getRMList().length() > 0)
        {
            getDataFromSession();
        }
        else
        {
            if (sessionManager.isNetworkAvailable())
            {
                Log.e("client List >>> ", "onCreate: " + sessionManager.getClientList());
                getClientData();
            }
            else
            {
                AppUtils.noInternetSnackBar(activity);
            }
        }
        onClickEvents();
    }

    void getClientData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            llLoading.setVisibility(View.VISIBLE);
            apiService.getAllClientByEmployee_new("",
                    "",
                    sessionManager.getUserId())
                    .enqueue(new Callback<NewClientListResponse>() {
                        @Override
                        public void onResponse(Call<NewClientListResponse> call, Response<NewClientListResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                try
                                {
                                    if (response.body().isSuccess())
                                    {
                                        listClient = response.body().getData().getAllClientByEmployee();
                                        sessionManager.setClientList(new Gson().toJson(listClient));
                                        getAllData();
                                    }
                                    else
                                    {
                                        getAllData();
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    getAllData();
                                }
                            }
                            else
                            {
                                getAllData();
                            }

                            //binding.loading.llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<NewClientListResponse> call, Throwable t)
                        {
                            Log.e("<><><>", "onFailure: " + t.getCause().toString());
                            //binding.loading.llLoading.setVisibility(View.GONE);
                            getAllData();
                        }
                    });
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllData()
    {
        new AsyncTask<Void, Void, Void>() {
            private boolean isSuccessForType = false;

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                listActivityType = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                getAllActivityType();
                return null;
            }

            private void getAllActivityType()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_ACTIVITY_TYPE, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALL_ACTIVITY_TYPE", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessForType = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccessForType)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "Id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "ActivityTypeName"));
                                commonGetSet.setSelect(AppUtils.getValidAPIBooleanResponseHas(dataObject, "IsActive"));
                                listActivityType.add(commonGetSet);
                            }
                        }

                        sessionManager.setActivityList(new Gson().toJson(listActivityType));
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(Void result)
            {
                getRMData();
                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    void getRMData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            llLoading.setVisibility(View.VISIBLE);
            apiService.getRMList()
                    .enqueue(new Callback<RMlistResponse>() {
                        @Override
                        public void onResponse(Call<RMlistResponse> call, Response<RMlistResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if (response.body().isSuccess())
                                {
                                    listRMName = response.body().getData();
                                    sessionManager.setRMList(new Gson().toJson(listRMName));
                                }
                            }
                            llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<RMlistResponse> call, Throwable t)
                        {
                            llLoading.setVisibility(View.GONE);
                        }
                    });
        }

    }

    void getDataFromSession()
    {
        if (sessionManager.getClientList().length() > 0)
        {
            try
            {
                listClient = new Gson().fromJson(sessionManager.getClientList(), new TypeToken<List<AllClientByEmployeeBean>>() {
                }.getType());
            }
            catch (JsonSyntaxException e)
            {
                e.printStackTrace();
            }
        }

        if (sessionManager.getActivityList().length() > 0)
        {
            try
            {
                listActivityType = new Gson().fromJson(sessionManager.getActivityList(), new TypeToken<List<CommonGetSet>>() {
                }.getType());
            }
            catch (JsonSyntaxException e)
            {
                e.printStackTrace();
            }
        }

        if (sessionManager.getRMList().length() > 0)
        {
            try
            {
                listRMName = new Gson().fromJson(sessionManager.getRMList(), new TypeToken<List<RMlistResponse.DataBean>>() {
                }.getType());
            }
            catch (JsonSyntaxException e)
            {
                e.printStackTrace();
            }
        }
    }

    void setEditData()
    {
        edtDARMessage.setText(editBean.getDar_message());
        edtRMName.setText(editBean.getRMName());
        edtClient.setText(editBean.getC_first_name());
        selectedClientId = String.valueOf(editBean.getClient_id());
        selectedClientName = editBean.getC_first_name();
        activity_type_id = String.valueOf(editBean.getActivity_type_id());
        status_activity_type_name = editBean.getActivity_type_name();
        edtActivityType.setText(status_activity_type_name);
        edtHours.setText(String.valueOf(editBean.getTimeSpent()));
        edtMinutes.setText(String.valueOf(editBean.getTimeSpent_Min()));
        edtRemarksComment.setText(editBean.getRemarksComment());

     /*   if (editBean.getReportDate_T_Y_D() == 1)
        {
            rbToday.setChecked(true);
        }
        else if (editBean.getReportDate_T_Y_D() == 2)
        {
            rbYesterday.setChecked(true);
        }
        else if (editBean.getReportDate_T_Y_D() == 3)
        {
            rbDayBeforeYesterday.setChecked(true);
        }*/
    }

    private void setupViews()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            viewStatusBar = (View) findViewById(R.id.viewStatusBar);
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
            txtLoading = (TextView) findViewById(R.id.txtLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            llClientSelection = (LinearLayout) findViewById(R.id.llClientSelection);
            llReportMain = (LinearLayout) findViewById(R.id.llReportMain);
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            inputDARMessage = (CustomTextInputLayout) findViewById(R.id.inputDARMessage);
            edtDARMessage = (EditText) findViewById(R.id.edtDARMessage);
            inputedtClient = (CustomTextInputLayout) findViewById(R.id.inputedtClient);
            edtClient = (EditText) findViewById(R.id.edtClient);
            inputActivityType = (CustomTextInputLayout) findViewById(R.id.inputActivityType);
            edtActivityType = (EditText) findViewById(R.id.edtActivityType);
            inputHours = (CustomTextInputLayout) findViewById(R.id.inputHours);
            edtHours = (EditText) findViewById(R.id.edtHours);
            inputMinutes = (CustomTextInputLayout) findViewById(R.id.inputMinutes);
            edtMinutes = (EditText) findViewById(R.id.edtMinutes);

            inputRMName = (CustomTextInputLayout) findViewById(R.id.inputRMName);
            edtRMName = (EditText) findViewById(R.id.edtRMName);
            inputRemarksComment = (CustomTextInputLayout) findViewById(R.id.inputRemarksComment);
            edtRemarksComment = (EditText) findViewById(R.id.edtRemarksComment);
            rgIsTodayReport = (RadioGroup) findViewById(R.id.rgIsTodayReport);
            rbToday = (RadioButton) findViewById(R.id.rbToday);
            rbYesterday = (RadioButton) findViewById(R.id.rbYesterday);
            rbDayBeforeYesterday = findViewById(R.id.rbDayBeforeYesterday);
            rbToday.setTypeface(AppUtils.getTypefaceRegular(activity));
            rbYesterday.setTypeface(AppUtils.getTypefaceRegular(activity));

            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            txtBtnSubmit = (TextView) findViewById(R.id.txtBtnSubmit);
            /*setSupportActionBar(toolbar);

            ImageView ivHeader = findViewById(R.id.ivHeader);
            ivHeader.setImageResource(R.drawable.img_portfolio);

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
            ivHeader.setLayoutParams(params);*/

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            llBackNavigation.setVisibility(View.VISIBLE);


            txtTitle.setText("Add Daily Activity Report");

            listHours = new ArrayList<String>();

            for (int i = 1; i <= 24; i++)
            {
                listHours.add(String.valueOf(i));
            }

            listMinutes = new ArrayList<String>();

            for (int i = 0; i <= 60; i++)
            {
                listMinutes.add(String.valueOf(i));
            }

            if (isFor.equals("edit"))
            {
                txtBtnSubmit.setText("Update Daily Activity Report");
                llReportMain.setVisibility(View.GONE);
                setEditData();
            }
            else
            {
                txtBtnSubmit.setText("Add Daily Activity Report");
                llReportMain.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(new View.OnClickListener() {
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

        edtRMName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (listRMName.size() > 0)
                {
                    showDialog("RM");
                }
                else
                {
                    Toast.makeText(activity, "No RM List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (listClient.size() > 0)
                {
                    showDialog("Client");
                }
                else
                {
                    Toast.makeText(activity, "No Client List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (listHours.size() > 0)
                {
                    showDialog("Hours");
                }
                else
                {
                    Toast.makeText(activity, "No Hours List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (listMinutes.size() > 0)
                {
                    showDialog("Minutes");
                }
                else
                {
                    Toast.makeText(activity, "No Minutes List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        edtActivityType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (listActivityType.size() > 0)
                    {
                        showDialog("Activity Type");
                    }
                    else
                    {
                        Toast.makeText(activity, "No Activity Type List Found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //MitsUtils.hideKeyboard(activity);
                hideKeyboard();

                if (checkValidation())
                {

                    DARDetailsReportListResponse.DataBean bean = new DARDetailsReportListResponse.DataBean();
                    bean.setEmployee_id(Integer.parseInt(sessionManager.getUserId()));
                    bean.setClient_id(Integer.parseInt(selectedClientId));
                    bean.setC_first_name(selectedClientName);
                    bean.setC_last_name(selectedClientName);
                    if (activity_type_id.isEmpty())
                    {
                        bean.setActivity_type_id(0);
                    }
                    else
                    {
                        bean.setActivity_type_id(Integer.parseInt(activity_type_id));
                    }
                    bean.setActivity_type_name(status_activity_type_name);
                    bean.setDar_message(edtDARMessage.getText().toString());
                    bean.setRMName(edtRMName.getText().toString());

                    if (edtHours.getText().toString().trim().length() == 0)
                    {
                        bean.setTimeSpent(0);
                    }
                    else
                    {
                        bean.setTimeSpent(Integer.parseInt(edtHours.getText().toString().trim()));
                    }
                    if (edtMinutes.getText().toString().trim().length() == 0)
                    {
                        bean.setTimeSpent_Min(0);
                    }
                    else
                    {
                        bean.setTimeSpent_Min(Integer.parseInt(edtMinutes.getText().toString().trim()));
                    }
                    bean.setRemarksComment(edtRemarksComment.getText().toString());

                    if (rbToday.isChecked())
                    {
                        bean.setReportDate_T_Y_D(1);
                    }
                    else if (rbYesterday.isChecked())
                    {
                        bean.setReportDate_T_Y_D(2);
                    }
                    else if (rbDayBeforeYesterday.isChecked())
                    {
                        bean.setReportDate_T_Y_D(3);
                    }

                    if (isFor.equals("edit"))
                    {
                        UpdateDARReport(bean);
                    }
                    else
                    {
                        AddDARReport(bean);
                    }

                    /*if (AddDARNewActivity.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 1;
                        message.obj = bean;
                        if (isFor.equals("add"))
                        {
                            message.arg1 = 0;
                        }
                        else
                        {
                            message.arg1 = 1;
                        }
                        AddDARNewActivity.handler.sendMessage(message);
                    }*/
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void AddDARReport(DARDetailsReportListResponse.DataBean bean)
    {
        try
        {
            new AsyncTask<Void, Void, Void>() {
                private String message = "";
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String response = "";
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("employee_id", sessionManager.getUserId());

                        if (isEditClient)
                        {
                            hashMap.put("client_id", String.valueOf(bean.getClient_id()));
                        }
                        else
                        {
                            hashMap.put("client_id", clientId);
                        }

                        hashMap.put("activity_type_id", String.valueOf(bean.getActivity_type_id()));
                        hashMap.put("Dar_message", String.valueOf(bean.getDar_message()));
                        hashMap.put("RMName", bean.getRMName());

                        if (bean.getTimeSpent() == 0)
                        {
                            hashMap.put("TimeSpent", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent", String.valueOf(bean.getTimeSpent()));
                        }

                        if (bean.getTimeSpent_Min() == 0)
                        {
                            hashMap.put("TimeSpent_Min", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent_Min", String.valueOf(bean.getTimeSpent_Min()));
                        }

                        hashMap.put("RemarksComment", bean.getRemarksComment());

                        hashMap.put("ReportDate_T_Y_D", String.valueOf(bean.getReportDate_T_Y_D()));


                        AppUtils.printLog(activity, "ADD_DAR_REPORT Request ", hashMap.toString());
                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_DAR_REPORT, hashMap);
                        AppUtils.printLog(activity, "ADD_DAR_REPORT Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);
                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");
                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        llLoading.setVisibility(View.GONE);

                        if (is_success)
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                            if (ClientDARFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                ClientDARFragment.handler.sendMessage(msgObj);
                            }

                            if (DARReportForEmployeeActivity.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                DARReportForEmployeeActivity.handler.sendMessage(msgObj);
                            }

                            if (EmpDARReportFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                EmpDARReportFragment.handler.sendMessage(msgObj);
                            }

                            finish();
                            AppUtils.finishActivityAnimation(activity);
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void UpdateDARReport(DARDetailsReportListResponse.DataBean bean)
    {
        try
        {
            new AsyncTask<Void, Void, Void>() {
                private String message = "";
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String response = "";
                        HashMap<String, String> hashMap = new HashMap<>();

                        hashMap.put("Id",String.valueOf(editBean.getId()));
                        hashMap.put("employee_id", sessionManager.getUserId());

                        if (isEditClient)
                        {
                            hashMap.put("client_id", String.valueOf(bean.getClient_id()));
                        }
                        else
                        {
                            hashMap.put("client_id", clientId);
                        }

                        hashMap.put("Dar_message", String.valueOf(bean.getDar_message()));
                        hashMap.put("activity_type_id", String.valueOf(bean.getActivity_type_id()));
                        if (bean.getTimeSpent() == 0)
                        {
                            hashMap.put("TimeSpent", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent", String.valueOf(bean.getTimeSpent()));
                        }

                        if (bean.getTimeSpent_Min() == 0)
                        {
                            hashMap.put("TimeSpent_Min", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent_Min", String.valueOf(bean.getTimeSpent_Min()));
                        }

                        hashMap.put("RMName", bean.getRMName());
                        hashMap.put("RemarksComment", bean.getRemarksComment());

                        AppUtils.printLog(activity, "UPDATE_DAR_REPORT Request ", hashMap.toString());
                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_DAR_REPORT, hashMap);
                        AppUtils.printLog(activity, "UPDATE_DAR_REPORT Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);
                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");
                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);
                    try
                    {
                        llLoading.setVisibility(View.GONE);

                        if (is_success)
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                            if (ClientDARFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                ClientDARFragment.handler.sendMessage(msgObj);
                            }

                            if (DARReportForEmployeeActivity.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                DARReportForEmployeeActivity.handler.sendMessage(msgObj);
                            }

                            if (EmpDARReportFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                EmpDARReportFragment.handler.sendMessage(msgObj);
                            }

                            if (EmpDARReportListFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 222;
                                EmpDARReportListFragment.handler.sendMessage(msgObj);
                            }

                            finish();
                            AppUtils.finishActivityAnimation(activity);
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showDialog(final String isFor)
    {
        try
        {

            dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_chooser);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            AppUtils.setLightStatusBarDialog(dialog, activity);

            final RecyclerView rvList = (RecyclerView) dialog.findViewById(R.id.rvList);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch);
            final TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            final ImageView ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
            final LinearLayout llSubmit = (LinearLayout) dialog.findViewById(R.id.llSubmit);
            rvList.setLayoutManager(new LinearLayoutManager(activity));

            if (isFor.equalsIgnoreCase("Activity Type"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Select Activity Type");
                llSubmit.setVisibility(View.GONE);
            }
            else if (isFor.equalsIgnoreCase("Client"))
            {
                edtSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.GONE);
                llSubmit.setVisibility(View.GONE);
                AppUtils.showKeyboard(edtSearch, activity);
            }
            else if (isFor.equalsIgnoreCase("RM"))
            {
                edtSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.GONE);
                llSubmit.setVisibility(View.GONE);
                AppUtils.showKeyboard(edtSearch, activity);
            }
            else if (isFor.equalsIgnoreCase("Hours"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Punch Your Working Time in an Hours");
                llSubmit.setVisibility(View.GONE);
            }
            else if (isFor.equalsIgnoreCase("Minutes"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Punch Your Working Time in Minutes");
                llSubmit.setVisibility(View.GONE);
            }

            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    AppUtils.hideKeyboard(edtSearch, activity);
                    try
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            rvList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    AppUtils.hideKeyboard(edtSearch, activity);
                    return false;
                }
            });

            edtSearch.addTextChangedListener(new TextWatcher() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    int textlength = edtSearch.getText().length();

                    if (isFor.equalsIgnoreCase("Client"))
                    {
                        ArrayList<AllClientByEmployeeBean> list_Employee_Search = new ArrayList<AllClientByEmployeeBean>();

                        for (int i = 0; i < listClient.size(); i++)
                        {
                            String employeeName = listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();

                            if (employeeName.toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                list_Employee_Search.add(listClient.get(i));
                            }
                        }

                        AppendClientList(list_Employee_Search, rvList, dialog, isFor, edtSearch);
                    }
                    else if (isFor.equalsIgnoreCase("RM"))
                    {
                        ArrayList<RMlistResponse.DataBean> list_Employee_Search = new ArrayList<RMlistResponse.DataBean>();

                        if (listRMName.size() > 0)
                        {
                            for (int i = 0; i < listRMName.size(); i++)
                            {
                                String employeeName = listRMName.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();

                                if (employeeName.toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                                {
                                    list_Employee_Search.add(listRMName.get(i));
                                }
                            }

                            AppendRMList(list_Employee_Search, rvList, dialog, isFor, edtSearch);
                        }
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
                {
                }

                @Override
                public void afterTextChanged(Editable arg0)
                {
                }
            });

            if (isFor.equalsIgnoreCase("Client"))
            {
                clientListAdapter = new ClientListAdapter(listClient, dialog, isFor, edtSearch);
                rvList.setAdapter(clientListAdapter);
            }
            else if (isFor.equalsIgnoreCase("RM"))
            {
                rmListAdapter = new RMListAdapter(listRMName, dialog, isFor, edtSearch);
                rvList.setAdapter(rmListAdapter);
            }
            else if (isFor.equalsIgnoreCase("Activity Type"))
            {
                activityTypeAdapter = new ActivityTypeAdpater(listActivityType, dialog, isFor, edtSearch);
                rvList.setAdapter(activityTypeAdapter);
            }
            else if (isFor.equalsIgnoreCase("Hours"))
            {
                hoursListAdpater = new HoursListAdpater(listHours, dialog, isFor, edtSearch);
                rvList.setAdapter(hoursListAdpater);
            }
            else if (isFor.equalsIgnoreCase("Minutes"))
            {
                hoursListAdpater = new HoursListAdpater(listMinutes, dialog, isFor, edtSearch);
                rvList.setAdapter(hoursListAdpater);
            }

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder> {
        ArrayList<AllClientByEmployeeBean> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;
        private boolean isdone;

        public ClientListAdapter(ArrayList<AllClientByEmployeeBean> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final AllClientByEmployeeBean assignedClientGetSetOld = listItems.get(position);

                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtTitle.setText(AppUtils.toDisplayCase(assignedClientGetSetOld.getFirst_name() + " " + assignedClientGetSetOld.getLast_name()));


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            AppUtils.hideKeyboard(edtSearch, activity);
                            dialog.dismiss();
                            selectedClientId = String.valueOf(assignedClientGetSetOld.getId());
                            selectedClientName = assignedClientGetSetOld.getFirst_name() + " " + assignedClientGetSetOld.getLast_name();
                            edtClient.setText(selectedClientName);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtTitle;
            private SmoothCheckBox smoothCheckBox;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothCheckBox);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
                smoothCheckBox.setVisibility(View.GONE);
            }
        }
    }

    public class RMListAdapter extends RecyclerView.Adapter<RMListAdapter.ViewHolder> {
        List<RMlistResponse.DataBean> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;
        private boolean isdone;

        public RMListAdapter(List<RMlistResponse.DataBean> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final RMlistResponse.DataBean bean = listItems.get(position);

                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtTitle.setText(AppUtils.toDisplayCase(bean.getFirst_name() + " " + bean.getLast_name()));


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            AppUtils.hideKeyboard(edtSearch, activity);
                            dialog.dismiss();
                            edtRMName.setText(bean.getFirst_name() + " " + bean.getLast_name());
                            /*selectedClientId = String.valueOf(bean.getId());
                            selectedClientName = bean.getFirst_name() + " " + bean.getLast_name();
                            edtClient.setText(selectedClientName);*/
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtTitle;
            private SmoothCheckBox smoothCheckBox;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothCheckBox);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
                smoothCheckBox.setVisibility(View.GONE);
            }
        }
    }

    private void AppendClientList(ArrayList<AllClientByEmployeeBean> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        clientListAdapter = new ClientListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(clientListAdapter);
        clientListAdapter.notifyDataSetChanged();
    }

    private void AppendRMList(ArrayList<RMlistResponse.DataBean> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        rmListAdapter = new RMListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(rmListAdapter);
        rmListAdapter.notifyDataSetChanged();
    }

    public class ActivityTypeAdpater extends RecyclerView.Adapter<ActivityTypeAdpater.ViewHolder> {
        ArrayList<CommonGetSet> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public ActivityTypeAdpater(ArrayList<CommonGetSet> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final CommonGetSet assignedClientGetSet = listItems.get(position);
                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(assignedClientGetSet.getName());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        AppUtils.hideKeyboard(edtSearch, activity);

                        try
                        {
                            dialog.dismiss();
                            dialog.cancel();

                            activity_type_id = String.valueOf(assignedClientGetSet.getId());
                            status_activity_type_name = assignedClientGetSet.getName();
                            edtActivityType.setText(status_activity_type_name);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtName;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    public class HoursListAdpater extends RecyclerView.Adapter<HoursListAdpater.ViewHolder> {
        ArrayList<String> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public HoursListAdpater(ArrayList<String> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(listItems.get(position).toString());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        AppUtils.hideKeyboard(edtSearch, activity);

                        try
                        {
                            dialog.dismiss();
                            dialog.cancel();

                            if (dialogFor.equalsIgnoreCase("Hours"))
                            {
                                edtHours.setText(listItems.get(position).toString());
                            }
                            else
                            {
                                edtMinutes.setText(listItems.get(position).toString());
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView txtName;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private boolean checkValidation()
    {
        boolean isValid = true;
        if (edtDARMessage.getText().toString().length() == 0)
        {
            inputDARMessage.setError("Please enter DAR Message.");
            isValid = false;
        }
        else if (edtHours.getText().toString().length() == 0 && edtMinutes.getText().toString().length() == 0)
        {
            inputHours.setError("Please enter your working time in an hours.");
            inputMinutes.setError("Please enter your working time in minutes.");
            isValid = false;
        }

        AppUtils.removeError(edtDARMessage, inputDARMessage);
        AppUtils.removeError(edtHours, inputHours);
        AppUtils.removeError(edtMinutes, inputMinutes);

        return isValid;
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

