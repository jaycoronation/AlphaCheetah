package cheetah.alphacapital.reportApp.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.databinding.ActivityAddAumBinding;
import cheetah.alphacapital.reportApp.activity.admin.AumEmployeeMonthlySummeryActivity;
import cheetah.alphacapital.reportApp.fragment.ClientAUMFragment;
import cheetah.alphacapital.reportApp.getset.GetStartEndYearResponseModel;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAUMActivity extends BaseActivity {
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
    TextView txtTitle;
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
    TextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    TextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.edtYear)
    EditText edtYear;
    @BindView(R.id.inputYear)
    CustomTextInputLayout inputYear;
    @BindView(R.id.edtMonth)
    EditText edtMonth;
    @BindView(R.id.inputMonth)
    CustomTextInputLayout inputMonth;
    @BindView(R.id.edtMeetingNew)
    EditText edtMeetingNew;
    @BindView(R.id.inputMeetingNew)
    CustomTextInputLayout inputMeetingNew;
    @BindView(R.id.edtMeetingExisting)
    EditText edtMeetingExisting;
    @BindView(R.id.inputMeetingExisting)
    CustomTextInputLayout inputMeetingExisting;
    @BindView(R.id.edtInflowOutflow)
    EditText edtInflowOutflow;
    @BindView(R.id.inputInflowOutflow)
    CustomTextInputLayout inputInflowOutflow;
    @BindView(R.id.edtSip)
    EditText edtSip;
    @BindView(R.id.inputSip)
    CustomTextInputLayout inputSip;
    @BindView(R.id.edtClientReference)
    EditText edtClientReference;
    @BindView(R.id.inputClientReference)
    CustomTextInputLayout inputClientReference;
    @BindView(R.id.edtMonthEndAum)
    EditText edtMonthEndAum;
    @BindView(R.id.inputMonthEndAum)
    CustomTextInputLayout inputMonthEndAum;
    @BindView(R.id.edtSummaryMail)
    EditText edtSummaryMail;
    @BindView(R.id.inputSummaryMail)
    CustomTextInputLayout inputSummaryMail;
    @BindView(R.id.edtDayForwardMail)
    EditText edtDayForwardMail;
    @BindView(R.id.inputDayForwardMail)
    CustomTextInputLayout inputDayForwardMail;
    @BindView(R.id.edtNewClientsConverted)
    EditText edtNewClientsConverted;
    @BindView(R.id.inputNewClientsConverted)
    CustomTextInputLayout inputNewClientsConverted;
    @BindView(R.id.edtSelfAcquiredAUM)
    EditText edtSelfAcquiredAUM;
    @BindView(R.id.inputSelfAcquiredAUM)
    CustomTextInputLayout inputSelfAcquiredAUM;
    @BindView(R.id.edtDAR)
    EditText edtDAR;
    @BindView(R.id.inputDAR)
    CustomTextInputLayout inputDAR;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.llSubmit)
    LinearLayout llSubmit;
    @BindView(R.id.edtStartInvest)
    CustomAppEditText edtStartInvest;
    @BindView(R.id.edtEndInvest)
    CustomAppEditText edtEndInvest;
    private boolean isStatusBarHidden = false, is_status_active = false;
    private String clientId = "";
    private int NewMeeting = 0, ExistingMeeting = 0;
    private List<String> listYear = new ArrayList<>();
    private List<CommonGetSet> listMonth = new ArrayList<>();
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private BottomSheetDialog listDialog;
    private String selectedYear = "", selectedMonth = "", isFor = "", selectedClientName = "";
    AUMListGetSet.DataBean getSet = new AUMListGetSet.DataBean();

    private LinearLayout llClientSelection;
    private CustomTextInputLayout inputedtClient;
    private EditText edtClient;
    ActivityAddAumBinding binding;

    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClient = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();

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

        clientId = getIntent().getStringExtra("clientId");

        isFor = getIntent().getStringExtra("isFor");

        getSet = getIntent().getExtras().getParcelable("AUMListGetSet");

        binding = DataBindingUtil.setContentView(activity,R.layout.activity_add_aum);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();

        if (clientId.equals("0"))
        {
            llClientSelection.setVisibility(View.VISIBLE);
        }
        else
        {
            llClientSelection.setVisibility(View.GONE);
        }

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getClientData();
            getMeetingDetails();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
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
                                    }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NewClientListResponse> call, Throwable t)
                        {
                            Log.e("<><><>", "onFailure: " + t.getCause().toString());
                        }
                    });
        }
    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);

            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add AUM");
            }
            else
            {
                txtTitle.setText("Update AUM");
            }

            llBackNavigation.setVisibility(View.VISIBLE);

            Calendar c = Calendar.getInstance();
            String[]monthName={"January","February","March", "April", "May", "June", "July",
                    "August", "September", "October", "November",
                    "December"};
            String month=monthName[c.get(Calendar.MONTH)];
            System.out.println("Month name:"+month);
            int year=c.get(Calendar.YEAR);
            int date=c.get(Calendar.DATE);
            selectedMonth = String.valueOf(c.get(Calendar.MONTH));
            selectedYear = String.valueOf(year);
            binding.edtMonth.setText(String.valueOf(month));
            binding.edtYear.setText(String.valueOf(year));

           /* ivHeader.setImageResource(R.drawable.img_portfolio);

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

            llClientSelection = (LinearLayout) findViewById(R.id.llClientSelection);
            inputedtClient = (CustomTextInputLayout) findViewById(R.id.inputedtClient);
            edtClient = (EditText) findViewById(R.id.edtClient);

            //setSupportActionBar(toolbar);

            if (isFor.equalsIgnoreCase("add"))
            {
                tvSubmit.setText("Add");
            }
            else
            {
                tvSubmit.setText("Update");
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

        edtMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showListDialog(MONTH);
            }
        });

        edtYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        edtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (listClient.size() > 0)
                {
                    showListDialog("Client");
                }
                else
                {
                    Toast.makeText(activity, "No Client List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSubmit.setOnClickListener(v ->
        {

            hideKeyboard();
            //MitsUtils.hideKeyboard(activity);

            if (checkValidation())
            {
                if (sessionManager.isNetworkAvailable())
                {
                    String meetingNew = "0";
                    String meetingExisting = "0";
                    String inflowOutFlow = "0";
                    String sip = "0";
                    String clientReference = "0";
                    String monthEndAum = "0";
                    String dar = "0";
                    String newClientConverted = "0";
                    String selfAcquiredAUM = "0";
                    String summery_mail = "0";
                    String day_forward_mail = "0";

                    meetingNew = edtMeetingNew.getText().toString().trim();
                    meetingExisting = edtMeetingExisting.getText().toString().trim();
                    inflowOutFlow = edtInflowOutflow.getText().toString().trim();
                    sip = edtSip.getText().toString().trim();
                    clientReference = edtClientReference.getText().toString().trim();
                    monthEndAum = edtMonthEndAum.getText().toString().trim();
                    dar = edtDAR.getText().toString().trim();
                    newClientConverted = edtNewClientsConverted.getText().toString().trim();
                    selfAcquiredAUM = edtSelfAcquiredAUM.getText().toString().trim();
                    summery_mail = edtSummaryMail.getText().toString().trim();
                    day_forward_mail = edtDayForwardMail.getText().toString().trim();

                    addAUM(inflowOutFlow, sip, clientReference, monthEndAum, dar, newClientConverted, selfAcquiredAUM, summery_mail,
                            day_forward_mail, meetingNew, meetingExisting);
                }
                else
                {
                    Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkValidation()
    {
        boolean isValid = true;

        if(clientId.equals("0") && edtClient.getText().toString().length()==0)
        {
            inputedtClient.setError("Please select client.");
            isValid = false;
        }
        else if (selectedYear.length() == 0)
        {
            inputYear.setError("Please select year.");
            isValid = false;
        }
        else if (selectedMonth.length() == 0)
        {
            inputMonth.setError("Please select month.");
            isValid = false;
        }
        else if (edtMeetingNew.getText().equals(""))
        {
            inputMeetingNew.setError("Please enter new meeting counts.");
            isValid = false;
        }
        else if (edtMeetingExisting.getText().equals(""))
        {
            inputMeetingExisting.setError("Please enter existing meeting counts.");
            isValid = false;
        }
        else if (edtInflowOutflow.getText().equals(""))
        {
            inputInflowOutflow.setError("Please enter In-Out flow counts.");
            isValid = false;
        }
        else if (edtSip.getText().equals(""))
        {
            inputSip.setError("Please enter spi.");
            isValid = false;
        }
        else if (edtClientReference.getText().equals(""))
        {
            inputClientReference.setError("Please enter client references.");
            isValid = false;
        }
        else if (edtMonthEndAum.getText().equals(""))
        {
            inputMonthEndAum.setError("Please enter month end AUM.");
            isValid = false;
        }
        else if (edtDayForwardMail.getText().equals(""))
        {
            inputDayForwardMail.setError("Please enter day forward mail.");
            isValid = false;
        }
        else if (edtNewClientsConverted.getText().equals(""))
        {
            inputNewClientsConverted.setError("Please enter new clients converted .");
            isValid = false;
        }
        else if (edtSelfAcquiredAUM.getText().equals(""))
        {
            inputSelfAcquiredAUM.setError("Please enter self acquired AUM .");
            isValid = false;
        }
        else if (edtDAR.getText().equals(""))
        {
            inputDAR.setError("Please enter DAR.");
            isValid = false;
        }

        AppUtils.removeError(edtClient, inputedtClient);
        AppUtils.removeError(edtYear, inputYear);
        AppUtils.removeError(edtMonth, inputMonth);
        AppUtils.removeError(edtMeetingNew, inputMeetingNew);
        AppUtils.removeError(edtMeetingExisting, inputMeetingExisting);
        AppUtils.removeError(edtInflowOutflow, inputInflowOutflow);
        AppUtils.removeError(edtSip, inputSip);
        AppUtils.removeError(edtClientReference, inputClientReference);
        AppUtils.removeError(edtMonthEndAum, inputMonthEndAum);
        AppUtils.removeError(edtSummaryMail, inputSummaryMail);
        AppUtils.removeError(edtDayForwardMail, inputDayForwardMail);
        AppUtils.removeError(edtNewClientsConverted, inputNewClientsConverted);
        AppUtils.removeError(edtSelfAcquiredAUM, inputSelfAcquiredAUM);
        AppUtils.removeError(edtDAR, inputDAR);

        return isValid;
    }

    private void addAUM(final String inflowOutflow, final String sip, final String clientReference, final String monthEndAum,
                        final String dar, final String newClientConverted, final String selfAcquiredAUM,
                        final String summery_mail, final String day_forward_mail, final String meetingNew, final String meetingExisting)
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>() {
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

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            hashMap.put("Id", "0");
                        }
                        else
                        {
                            hashMap.put("Id", String.valueOf(getSet.getId()));
                        }
                        String employeeId = "";
                        if (getSet.getEmployee_id() != 0)
                        {
                            employeeId = String.valueOf(getSet.getEmployee_id());
                        }
                        else
                        {
                            employeeId = AppUtils.getEmployeeIdForAdmin(activity);
                        }

                        hashMap.put("employee_id", employeeId);
                        hashMap.put("AUM_Month", selectedMonth);
                        hashMap.put("AUM_Year", selectedYear);
                        hashMap.put("client_id", clientId);
                        hashMap.put("New_Meeting", meetingNew);
                        hashMap.put("Existing_Meeting", meetingExisting);
                        hashMap.put("Inflow_outflow", inflowOutflow);
                        hashMap.put("SIP", sip);
                        hashMap.put("ClientReferences", clientReference);
                        hashMap.put("Month_End_AUM", monthEndAum);
                        hashMap.put("DAR", dar);
                        hashMap.put("NewClientConverted", newClientConverted);
                        hashMap.put("SelfAcquiredAUM", selfAcquiredAUM);
                        hashMap.put("summery_mail", "0");
                        hashMap.put("day_forward_mail", day_forward_mail);

                        Log.e("add amu req ", "doInBackground: " + hashMap.toString());

                        String serverResponse = "";

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_AUM, hashMap);
                        }
                        else
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_AUM, hashMap);
                        }

                        Log.e("add amu resp ", "doInBackground: " + serverResponse);

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

                    if (ClientAUMFragment.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 111;
                        ClientAUMFragment.handler.sendMessage(message);
                    }

                    if (AumEmployeeMonthlySummeryActivity.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 111;
                        AumEmployeeMonthlySummeryActivity.handler.sendMessage(message);
                    }

                    finish();

                }
            }.execute();
        }

    }

    private void getMeetingDetails()
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>() {
                boolean isSuccess;

                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();

                    llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    getMonthYearData();
                    getMeetingData();
                    return null;
                }

                private void getMeetingData()
                {
                    try
                    {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("client_id", clientId);
                        hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("CurrentMonth", AppUtils.getCurrentMonth());
                        hashMap.put("CurrentYear", AppUtils.getCurrentYear());

                        Log.e("Meeting Req", "doInBackground: " + hashMap.toString());
                        String serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_CLIENT_MEETING_BY_EMPLOYEE, hashMap);
                        Log.e("Meeting Res", "doInBackground: " + serverResponse);
                        if (serverResponse != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(serverResponse);
                                if (jsonObject != null)
                                {
                                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");
                                    if (isSuccess)
                                    {
                                        if (jsonObject.has("data"))
                                        {
                                            JSONObject dataObject = jsonObject.getJSONObject("data");
                                            NewMeeting = AppUtils.getValidAPIIntegerResponseHas(dataObject, "NewMeeting");
                                            ExistingMeeting = AppUtils.getValidAPIIntegerResponseHas(dataObject, "ExistingMeeting");
                                        }
                                    }
                                }
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
                }

                private void getMonthYearData()
                {
                    try
                    {
                        String response = "";

                        HashMap<String, String> hashMap = new HashMap<>();
                        AppUtils.printLog(activity, "month year Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.MONTH_YEAR, hashMap);

                        AppUtils.printLog(activity, "month year Response ", response.toString());

                        JSONObject jsonObject = new JSONObject(response);

                        boolean isSuccessful = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        if (isSuccessful)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONObject dataObject = jsonObject.getJSONObject("data");


                                JSONArray yearArray = dataObject.getJSONArray("year");
                                if (yearArray.length() > 0)
                                {
                                    for (int i = 0; i < yearArray.length(); i++)
                                    {
                                        listYear.add(yearArray.getString(i));
                                    }
                                }

                                JSONArray monthArray = dataObject.getJSONArray("month");
                                if (monthArray.length() > 0)
                                {
                                    for (int i = 0; i < monthArray.length(); i++)
                                    {
                                        JSONObject monthObject = (JSONObject) monthArray.get(i);
                                        CommonGetSet getSet = new CommonGetSet();
                                        getSet.setName(AppUtils.getValidAPIStringResponse(monthObject.optString("name")));
                                        getSet.setId(AppUtils.getValidAPIIntegerResponseHas(monthObject, "id"));
                                        listMonth.add(getSet);
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);
                    llLoading.setVisibility(View.GONE);
                    if (isSuccess)
                    {
                        edtMeetingNew.setText(String.valueOf(NewMeeting));
                        edtMeetingExisting.setText(String.valueOf(ExistingMeeting));
                    }

                    if (isFor.equalsIgnoreCase("edit"))
                    {
                        selectedMonth = String.valueOf(getSet.getAUM_Month());
                        selectedYear = String.valueOf(getSet.getAUM_Year());

                        Log.e("<><> Year : " ,selectedYear);
                        Log.e("<><> Month : " ,selectedMonth);

                        edtYear.setText(String.valueOf(getSet.getAUM_Year()));
                        edtMonth.setText(AppUtils.getMonthName(getSet.getAUM_Month()));
                        edtMeetingNew.setText(String.valueOf(getSet.getNew_Meeting()));
                        edtMeetingExisting.setText(String.valueOf(getSet.getExisting_Meeting()));
                        edtInflowOutflow.setText(String.valueOf(getSet.getInflow_outflow()));
                        edtSip.setText(String.valueOf(getSet.getSIP()));
                        edtClientReference.setText(String.valueOf(getSet.getClientReferences()));
                        edtMonthEndAum.setText(String.valueOf(getSet.getMonth_End_AUM()));
                        edtSummaryMail.setText(String.valueOf(getSet.getSummery_mail()));
                        edtDayForwardMail.setText(String.valueOf(getSet.getDay_forward_mail()));
                        edtNewClientsConverted.setText(String.valueOf(getSet.getNewClientConverted()));
                        edtSelfAcquiredAUM.setText(String.valueOf(getSet.getSelfAcquiredAUM()));
                        edtDAR.setText(String.valueOf(getSet.getDAR()));
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        else
        {
            //
        }
    }


    private void getStartEndYear()
    {
        try
        {
            apiService.getStartEndYear(Integer.parseInt(selectedMonth), Integer.parseInt(selectedYear), Integer.parseInt(clientId)).enqueue(new Callback<GetStartEndYearResponseModel>()
            {
                @Override
                public void onResponse(Call<GetStartEndYearResponseModel> call, Response<GetStartEndYearResponseModel> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess())
                        {
                            try
                            {
                                binding.edtStartInvest.setText(String.valueOf(response.body().getData().getStartYear()));
                                if (response.body().getData().getStartYear() != 0)
                                {

                                }
                                if (response.body().getData().getLastYear() != 0)
                                {
                                    binding.edtEndInvest.setText(String.valueOf(response.body().getData().getLastYear()));
                                }

                                if (!binding.edtStartInvest.getText().toString().isEmpty())
                                {
                                    binding.edtStartInvest.setFocusable(false);
                                }
                                else
                                {
                                    binding.edtStartInvest.setFocusable(true);
                                }

                                if (!binding.edtEndInvest.getText().toString().isEmpty())
                                {
                                    binding.edtEndInvest.setFocusable(false);
                                }
                                else
                                {
                                    binding.edtEndInvest.setFocusable(true);
                                }
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            showToast(response.body().getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetStartEndYearResponseModel> call, Throwable t)
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            });
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    DialogListAdapter dialogListAdapter;

    public void showListDialog(final String isFor)
    {
        listDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        listDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_list, null);
        listDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(listDialog, activity);
        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        TextView btnNo = (TextView) listDialog.findViewById(R.id.btnNo);

        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select " + isFor);

        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);

        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);

        dialogListAdapter = new DialogListAdapter(isFor);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(dialogListAdapter);

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.show();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {
        String isFor = "";

        DialogListAdapter(String isFor)
        {
            this.isFor = isFor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            if (position == getItemCount() - 1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            if (isFor.equals(MONTH))
            {
                final CommonGetSet getSet = listMonth.get(position);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            edtMonth.setText(getSet.getName());

                            selectedMonth = String.valueOf(getSet.getId());
                            if (!edtClient.getText().toString().isEmpty() && !edtYear.getText().toString().isEmpty() && !edtMonth.getText().toString().isEmpty())
                            {
                                getStartEndYear();
                            }
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position).toString();
                                edtYear.setText(selectedYear);
                                if (!edtClient.getText().toString().isEmpty() && !edtYear.getText().toString().isEmpty() && !edtMonth.getText().toString().isEmpty())
                                {
                                    getStartEndYear();
                                }
                            }
                        }

                    }
                });
            }
            else
            {
                holder.tvValue.setText(listClient.get(position).getFirst_name() + " " + listClient.get(position).getLast_name());
                holder.itemView.setOnClickListener(v ->
                {
                    listDialog.dismiss();
                    clientId = String.valueOf(listClient.get(position).getId());
                    selectedClientName = listClient.get(position).getFirst_name() + " " + listClient.get(position).getLast_name();
                    edtClient.setText(selectedClientName);
                    if (!edtClient.getText().toString().isEmpty() && !edtYear.getText().toString().isEmpty() && !edtMonth.getText().toString().isEmpty())
                    {
                        getStartEndYear();
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(MONTH))
            {
                return listMonth.size();
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
            }
            else
            {
                return listClient.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvValue;
            private View viewLine;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = (TextView) itemView.findViewById(R.id.tvValue);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
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
