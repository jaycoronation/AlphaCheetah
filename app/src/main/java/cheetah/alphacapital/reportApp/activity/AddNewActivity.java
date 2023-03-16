package cheetah.alphacapital.reportApp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.ActivityListGetSet;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.SchemeGetSet;


public class AddNewActivity extends AppCompatActivity
{
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
    private CustomTextInputLayout inputActivityName;
    private EditText edtActivityName;
    private CustomTextInputLayout inputEmployee;
    private EditText edtEmployee;
    private CustomTextInputLayout inputScheme;
    private EditText edtScheme;
    private CustomTextInputLayout inputStatus;
    private EditText edtStatus;
    private CustomTextInputLayout inputHours;
    private EditText edtHours;
    private CustomTextInputLayout inputMinutes;
    private EditText edtMinutes;
    private CustomTextInputLayout inputDuedate;
    private EditText edtDuedate;
    private CustomTextInputLayout inputActivityType;
    private EditText edtActivityType;
    private LinearLayout llSubmit;
    private TextView txtBtnSubmit;

    private ArrayList<AssignedClientGetSetOld> listEmployee = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<SchemeGetSet> listScheme = new ArrayList<SchemeGetSet>();
    private ArrayList<CommonGetSet> listStatus = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listActivityType = new ArrayList<CommonGetSet>();
    private EmployeeListAdapter employeeListAdapter;
    private SchemeListAdapter schemeListAdapter;
    private StatusListAdapter statusListAdapter;
    private ActivityTypeAdpater activityTypeAdapter;
    private ArrayList<String> listHours = new ArrayList<String>();
    private ArrayList<String> listMinutes = new ArrayList<String>();
    private HoursListAdpater hoursListAdpater;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isStatusBarHidden = false;
    private String scheme_id = "", scheme_name = "", status_id = "", status_name = "", activity_type_id = "", status_activity_type_name = "", selectedEmployeeId = "", selectedEmployeeName = "", selectedDueDate = "";

    private AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
    private ActivityListGetSet editGetSet = new ActivityListGetSet();
    private boolean isForEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
      /*  try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }*/

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

        setContentView(R.layout.activity_add_new);

        assignedClientGetSetOld = getIntent().getExtras().getParcelable("ClientInfo");

        isForEdit = getIntent().getExtras().getBoolean("isForEdit", false);

        if (isForEdit)
        {
            editGetSet = getIntent().getExtras().getParcelable("EditGetSet");
        }
        else
        {
            editGetSet = new ActivityListGetSet();
        }

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getAllData();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
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
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            inputActivityName = (CustomTextInputLayout) findViewById(R.id.inputActivityName);
            edtActivityName = (EditText) findViewById(R.id.edtActivityName);
            inputEmployee = (CustomTextInputLayout) findViewById(R.id.inputEmployee);
            edtEmployee = (EditText) findViewById(R.id.edtEmployee);
            inputScheme = (CustomTextInputLayout) findViewById(R.id.inputScheme);
            edtScheme = (EditText) findViewById(R.id.edtScheme);
            inputStatus = (CustomTextInputLayout) findViewById(R.id.inputStatus);
            edtStatus = (EditText) findViewById(R.id.edtStatus);
            inputHours = (CustomTextInputLayout) findViewById(R.id.inputHours);
            edtHours = (EditText) findViewById(R.id.edtHours);
            inputMinutes = (CustomTextInputLayout) findViewById(R.id.inputMinutes);
            edtMinutes = (EditText) findViewById(R.id.edtMinutes);
            inputDuedate = (CustomTextInputLayout) findViewById(R.id.inputDuedate);
            edtDuedate = (EditText) findViewById(R.id.edtDuedate);
            inputActivityType = (CustomTextInputLayout) findViewById(R.id.inputActivityType);
            edtActivityType = (EditText) findViewById(R.id.edtActivityType);

            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            txtBtnSubmit = (TextView) findViewById(R.id.txtBtnSubmit);
            setSupportActionBar(toolbar);

            ImageView ivHeader = findViewById(R.id.ivHeader);
            ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;
            if(isStatusBarHidden)
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

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            llBackNavigation.setVisibility(View.VISIBLE);

            if (isForEdit)
            {
                txtBtnSubmit.setText("Edit Activity");
                txtTitle.setText("Edit Activity");
            }
            else
            {
                txtBtnSubmit.setText("Add Activity");
                txtTitle.setText("Add Activity");
            }

            listHours = new ArrayList<String>();

            for (int i = 1; i <= 24; i++)
            {
                listHours.add(String.valueOf(i + " Hours "));
            }

            listMinutes = new ArrayList<String>();

            for (int i = 0; i <= 60; i++)
            {
                listMinutes.add(String.valueOf(i + " Minutes "));
            }
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

        edtEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (listEmployee.size() > 0)
                {
                    showDialog("Employee");
                }
                else
                {
                    Toast.makeText(activity, "No Employee List Found.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtScheme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (listScheme.size() > 0)
                    {
                        showDialog("Scheme");
                    }
                    else
                    {
                        Toast.makeText(activity, "No Scheme List Found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (listStatus.size() > 0)
                    {
                        showDialog("Status");
                    }
                    else
                    {
                        Toast.makeText(activity, "No Status List Found.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        edtDuedate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectReminderDialog();
            }
        });

        edtActivityType.setOnClickListener(new View.OnClickListener()
        {
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


        edtHours.setOnClickListener(new View.OnClickListener()
        {
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

        edtMinutes.setOnClickListener(new View.OnClickListener()
        {
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

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //MitsUtils.hideKeyboard(activity);
                AppUtils.hideKeyboard(edtActivityName,activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        AddActivityAsync(edtActivityName.getText().toString().trim(),
                                edtHours.getText().toString().trim(),
                                edtDuedate.getText().toString().trim(),
                                edtMinutes.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void getAllData()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccessForEmployee = false, isSuccessForScheme = false, isSuccessForStatus = false, isSuccessForType = false;
            private String messageForEmployee = "", messageForScheme = "", messageForStatus = "", messageType = "";

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                listEmployee = new ArrayList<AssignedClientGetSetOld>();
                listScheme = new ArrayList<SchemeGetSet>();
                listStatus = new ArrayList<CommonGetSet>();
                listActivityType = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                getAllEmployee();
                getAllScheme();
                getAllStatus();
                getAllActivityType();
                return null;
            }

            private void getAllEmployee()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("clientid", String.valueOf(assignedClientGetSetOld.getId()));

                    AppUtils.printLog(activity, "Get_All_Employee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Get_All_Employee, hashMap);

                    AppUtils.printLog(activity, "Get_All_Employee Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessForEmployee = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    messageForEmployee = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (isSuccessForEmployee)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            if (dataObj.has("ClientAssignedEmployee"))
                            {
                                JSONArray dataArray = dataObj.getJSONArray("ClientAssignedEmployee");

                                if (dataArray.length() > 0)
                                {
                                    for (int i = 0; i < dataArray.length(); i++)
                                    {
                                        AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                                        assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                        assignedClientGetSetOld.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                        assignedClientGetSetOld.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                        assignedClientGetSetOld.setContact_no(AppUtils.getValidAPIStringResponseHas(dataObject, "contact_no"));
                                        assignedClientGetSetOld.setEmail(AppUtils.getValidAPIStringResponseHas(dataObject, "email"));
                                        assignedClientGetSetOld.setAddress(AppUtils.getValidAPIStringResponseHas(dataObject, "address"));
                                        assignedClientGetSetOld.setCountry_name(AppUtils.getValidAPIStringResponseHas(dataObject, "country_name"));
                                        assignedClientGetSetOld.setState_name(AppUtils.getValidAPIStringResponseHas(dataObject, "state_name"));
                                        assignedClientGetSetOld.setCity_name(AppUtils.getValidAPIStringResponseHas(dataObject, "city_name"));
                                        assignedClientGetSetOld.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                        assignedClientGetSetOld.setCountry_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "country_id"));
                                        assignedClientGetSetOld.setState_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "state_id"));
                                        assignedClientGetSetOld.setIs_active(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active"));
                                        listEmployee.add(assignedClientGetSetOld);
                                    }
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

            private void getAllScheme()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("clientid", String.valueOf(assignedClientGetSetOld.getId()));
                    AppUtils.printLog(activity, "GET_ALL_SCHEME Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_SCHEME, hashMap);

                    AppUtils.printLog(activity, "GET_ALL_SCHEME Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessForScheme = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    messageForScheme = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (isSuccessForScheme)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            if (dataObj.has("ClientAssignedScheme"))
                            {
                                JSONArray dataArray = dataObj.getJSONArray("ClientAssignedScheme");

                                if (dataArray.length() > 0)
                                {
                                    for (int i = 0; i < dataArray.length(); i++)
                                    {
                                        SchemeGetSet schemeGetSet = new SchemeGetSet();
                                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                                        schemeGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                        schemeGetSet.setScheme_type_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "scheme_type_id"));
                                        schemeGetSet.setScheme_type_name(AppUtils.getValidAPIStringResponseHas(dataObject, "scheme_type_name"));
                                        schemeGetSet.setScheme_name(AppUtils.getValidAPIStringResponseHas(dataObject, "scheme_name"));
                                        listScheme.add(schemeGetSet);
                                    }
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

            private void getAllStatus()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALLACTIVITY_STATUS, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALLACTIVITY_STATUS", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessForStatus = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccessForStatus)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "activity_status"));
                                listStatus.add(commonGetSet);
                            }
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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
                llLoading.setVisibility(View.GONE);

                try
                {
                    if (isForEdit)
                    {
                        edtActivityName.setText(editGetSet.getActivity_message());
                        edtEmployee.setText(editGetSet.getAll_employee_name());
                        edtScheme.setText(editGetSet.getScheme_name());
                        edtStatus.setText(editGetSet.getActivity_status());
                        edtActivityType.setText(editGetSet.getActivityTypeName());
                        //  edtHours.setText(String.valueOf(editGetSet.getActivity_hours()));

                        if (editGetSet.getDue_date().length() > 0)
                        {
                            edtDuedate.setText(AppUtils.universalDateConvert(editGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"));
                            selectedDueDate = edtDuedate.getText().toString().trim();
                            AppUtils.printLog(activity, "Selected Due Date", selectedDueDate);
                        }

                        selectedEmployeeId = editGetSet.getAll_employee_ids();
                        scheme_name = editGetSet.getScheme_name();
                        scheme_id = String.valueOf(editGetSet.getScheme_id());
                        status_name = editGetSet.getActivity_status();
                        status_id = String.valueOf(editGetSet.getActivity_status_id());

                        activity_type_id = String.valueOf(editGetSet.getActivity_type_id());
                        status_activity_type_name = editGetSet.getActivityTypeName();

                        if (isSuccessForEmployee)
                        {
                            ArrayList<Integer> listSelectedIds = new ArrayList<Integer>();

                            ArrayList<String> stringList = new ArrayList<>(Arrays.asList(editGetSet.getAll_employee_ids().split(",")));

                            for (int k = 0; k < stringList.size(); k++)
                            {
                                listSelectedIds.add(Integer.parseInt(stringList.get(k)));
                            }

                            if (listEmployee.size() > 0)
                            {
                                for (int i = 0; i < listEmployee.size(); i++)
                                {
                                    for (int j = 0; j < listSelectedIds.size(); j++)
                                    {
                                        if (listSelectedIds.get(j) == listEmployee.get(i).getId())
                                        {
                                            AssignedClientGetSetOld getSet = listEmployee.get(i);
                                            getSet.setIs_selected(true);
                                            listEmployee.set(i, getSet);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void AddActivityAsync(final String activityName, final String hours, final String duedate,final String minutes)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
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
                        hashMap.put("employee_id",  AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("assign_employee_ids", selectedEmployeeId);
                        hashMap.put("client_id", String.valueOf(assignedClientGetSetOld.getId()));
                        hashMap.put("scheme_id", scheme_id);
                        hashMap.put("activity_status_id", status_id);
                        hashMap.put("activity_message", activityName);
                        hashMap.put("due_date", duedate);

                        if(hours.length()==0)
                        {
                            hashMap.put("activityhours", "0");
                        }
                        else
                        {
                            hashMap.put("activityhours", hours);
                        }

                        if(minutes.length()==0)
                        {
                            hashMap.put("activitymin", "0");
                        }
                        else
                        {
                            hashMap.put("activitymin", minutes);
                        }


                        hashMap.put("activity_type_id", activity_type_id);

                        if (isForEdit)
                        {
                            hashMap.put("id", String.valueOf(editGetSet.getId()));
                            AppUtils.printLog(activity, "UPDATE_ACTIVITY Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_ACTIVITY, hashMap);
                        }
                        else
                        {
                            AppUtils.printLog(activity, "ADD_ACTIVITY Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_ACTIVITY, hashMap);
                        }

                        AppUtils.printLog(activity, "ADD_ACTIVITY Response ", response.toString());

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
                            if (ActivityListActivity.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                ActivityListActivity.handler.sendMessage(message);
                            }

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            activity.finish();
                        }
                        else
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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

            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
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

            if (isFor.equalsIgnoreCase("Status"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Select Status");
                llSubmit.setVisibility(View.GONE);
            }
            else if (isFor.equalsIgnoreCase("Activity Type"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Select Activity Type");
                llSubmit.setVisibility(View.GONE);
            }
            else if (isFor.equalsIgnoreCase("Employee"))
            {
                edtSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.GONE);
                llSubmit.setVisibility(View.VISIBLE);
                AppUtils.showKeyboard(edtSearch, activity);
            }
            else if (isFor.equalsIgnoreCase("Hours"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Punch Your Working in Hours");
                llSubmit.setVisibility(View.GONE);
            }
            else if (isFor.equalsIgnoreCase("Minutes"))
            {
                edtSearch.setVisibility(View.GONE);
                txtTitle.setVisibility(View.VISIBLE);
                txtTitle.setText("Punch Your Working in Minutes");
                llSubmit.setVisibility(View.GONE);
            }
            else
            {
                edtSearch.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.GONE);
                llSubmit.setVisibility(View.GONE);
                AppUtils.showKeyboard(edtSearch, activity);
            }



            ivBack.setOnClickListener(new View.OnClickListener()
            {
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

            rvList.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    AppUtils.hideKeyboard(edtSearch, activity);
                    return false;
                }
            });

            llSubmit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        if (employeeListAdapter.getSelectedEmployeeId().length() > 0)
                        {
                            selectedEmployeeId = employeeListAdapter.getSelectedEmployeeId();
                            selectedEmployeeName = employeeListAdapter.getSelectedEmployeeName();
                            edtEmployee.setText(selectedEmployeeName);
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        else
                        {
                            Toast.makeText(activity, "Please Select atleast one employee.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            edtSearch.addTextChangedListener(new TextWatcher()
            {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    int textlength = edtSearch.getText().length();

                    if (isFor.equalsIgnoreCase("Employee"))
                    {
                        ArrayList<AssignedClientGetSetOld> list_Employee_Search = new ArrayList<AssignedClientGetSetOld>();

                        for (int i = 0; i < listEmployee.size(); i++)
                        {
                            String employeeName = listEmployee.get(i).getFirst_name() + " " + listEmployee.get(i).getLast_name();

                            if (employeeName.toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                list_Employee_Search.add(listEmployee.get(i));
                            }
                        }

                        AppendEmployeeList(list_Employee_Search, rvList, dialog, isFor, edtSearch);
                    }
                    else if (isFor.equalsIgnoreCase("Scheme"))
                    {
                        ArrayList<SchemeGetSet> listScheme_Search = new ArrayList<SchemeGetSet>();
                        for (int i = 0; i < listScheme.size(); i++)
                        {
                            if (textlength <= listScheme.get(i).getScheme_name().length())
                            {
                                if (listScheme.get(i).getScheme_name().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                                {
                                    listScheme_Search.add(listScheme.get(i));
                                }
                            }
                        }
                        AppendSchemeList(listScheme_Search, rvList, dialog, isFor, edtSearch);
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

            if (isFor.equalsIgnoreCase("Employee"))
            {
                employeeListAdapter = new EmployeeListAdapter(listEmployee, dialog, isFor, edtSearch);
                rvList.setAdapter(employeeListAdapter);
            }
            else if (isFor.equalsIgnoreCase("Scheme"))
            {
                schemeListAdapter = new SchemeListAdapter(listScheme, dialog, isFor, edtSearch);
                rvList.setAdapter(schemeListAdapter);
            }
            else if (isFor.equalsIgnoreCase("Status"))
            {
                statusListAdapter = new StatusListAdapter(listStatus, dialog, isFor, edtSearch);
                rvList.setAdapter(statusListAdapter);
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

    public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>
    {
        ArrayList<AssignedClientGetSetOld> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;
        private boolean isdone;

        public EmployeeListAdapter(ArrayList<AssignedClientGetSetOld> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public EmployeeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new EmployeeListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EmployeeListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AssignedClientGetSetOld assignedClientGetSetOld = listItems.get(position);

                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtTitle.setText(AppUtils.toDisplayCase(assignedClientGetSetOld.getFirst_name() + " " + assignedClientGetSetOld.getLast_name()));

                isdone = true;

                if (assignedClientGetSetOld.isIs_selected())
                {
                    holder.smoothCheckBox.setChecked(true, true);
                }
                else
                {
                    holder.smoothCheckBox.setChecked(false, true);
                }

                holder.smoothCheckBox.setClickable(false);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (!isdone)
                        {
                            if (assignedClientGetSetOld.isIs_selected())
                            {
                                assignedClientGetSetOld.setIs_selected(false);
                                holder.smoothCheckBox.setChecked(false, true);
                            }
                            else
                            {
                                assignedClientGetSetOld.setIs_selected(true);
                                holder.smoothCheckBox.setChecked(true, true);
                            }

                            notifyItemChanged(position);
                        }

                    }
                });

                isdone = false;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public String getSelectedEmployeeId()
        {
            String ids = "";

            try
            {
                for (int i = 0; i < listItems.size(); i++)
                {
                    if (listItems.get(i).isIs_selected())
                    {
                        if (ids.equalsIgnoreCase(""))
                        {
                            ids = String.valueOf(listItems.get(i).getId());
                        }
                        else
                        {
                            ids = ids + "," + String.valueOf(listItems.get(i).getId());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return ids;

        }

        public String getSelectedEmployeeName()
        {
            String name = "";

            try
            {
                for (int i = 0; i < listItems.size(); i++)
                {
                    if (listItems.get(i).isIs_selected())
                    {
                        if (name.equalsIgnoreCase(""))
                        {
                            name = String.valueOf(listItems.get(i).getFirst_name() + " " + listItems.get(i).getLast_name());
                        }
                        else
                        {
                            name = name + "," + String.valueOf(listItems.get(i).getFirst_name() + " " + listItems.get(i).getLast_name());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return name;

        }

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView txtTitle;
            private SmoothCheckBox smoothCheckBox;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                smoothCheckBox = (SmoothCheckBox) convertView.findViewById(R.id.smoothCheckBox);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private void AppendEmployeeList(ArrayList<AssignedClientGetSetOld> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        employeeListAdapter = new EmployeeListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(employeeListAdapter);
        employeeListAdapter.notifyDataSetChanged();
    }

    public class SchemeListAdapter extends RecyclerView.Adapter<SchemeListAdapter.ViewHolder>
    {
        ArrayList<SchemeGetSet> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public SchemeListAdapter(ArrayList<SchemeGetSet> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public SchemeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new SchemeListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final SchemeListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final SchemeGetSet assignedClientGetSet = listItems.get(position);

                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(assignedClientGetSet.getScheme_name());


                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        AppUtils.hideKeyboard(edtSearch, activity);
                        try
                        {
                            dialog.dismiss();
                            dialog.cancel();
                            scheme_id = String.valueOf(assignedClientGetSet.getId());
                            scheme_name = assignedClientGetSet.getScheme_name();
                            edtScheme.setText(scheme_name);
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
        public class ViewHolder extends RecyclerView.ViewHolder
        {
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

    private void AppendSchemeList(ArrayList<SchemeGetSet> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        schemeListAdapter = new SchemeListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(schemeListAdapter);
        schemeListAdapter.notifyDataSetChanged();
    }

    public class StatusListAdapter extends RecyclerView.Adapter<StatusListAdapter.ViewHolder>
    {
        ArrayList<CommonGetSet> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public StatusListAdapter(ArrayList<CommonGetSet> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public StatusListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new StatusListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final StatusListAdapter.ViewHolder holder, final int position)
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

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                        AppUtils.hideKeyboard(edtSearch, activity);

                        try
                        {
                            dialog.dismiss();
                            dialog.cancel();

                            status_id = String.valueOf(assignedClientGetSet.getId());
                            status_name = assignedClientGetSet.getName();
                            edtStatus.setText(status_name);
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
        public class ViewHolder extends RecyclerView.ViewHolder
        {
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

    public class ActivityTypeAdpater extends RecyclerView.Adapter<ActivityTypeAdpater.ViewHolder>
    {
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
        public ActivityTypeAdpater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new ActivityTypeAdpater.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ActivityTypeAdpater.ViewHolder holder, final int position)
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

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
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
        public class ViewHolder extends RecyclerView.ViewHolder
        {
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

    public class HoursListAdpater extends RecyclerView.Adapter<HoursListAdpater.ViewHolder>
    {
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
        public HoursListAdpater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new HoursListAdpater.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final HoursListAdpater.ViewHolder holder, final int position)
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

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
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
        public class ViewHolder extends RecyclerView.ViewHolder
        {
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

    public void selectReminderDialog()
    {
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_select_reminder, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final TextView txtDate, txtTime, txtRemoveReminder, txtSetReminder;
        final LinearLayout llSetReminder, llRemoveReminder, llTime, llDate;
        llSetReminder = (LinearLayout) sheetView.findViewById(R.id.llSetReminder);
        llRemoveReminder = (LinearLayout) sheetView.findViewById(R.id.llRemoveReminder);
        llTime = (LinearLayout) sheetView.findViewById(R.id.llTime);
        llDate = (LinearLayout) sheetView.findViewById(R.id.llDate);

        txtDate = (TextView) sheetView.findViewById(R.id.txtDate);
        txtTime = (TextView) sheetView.findViewById(R.id.txtTime);
        txtRemoveReminder = (TextView) sheetView.findViewById(R.id.txtRemoveReminder);
        txtSetReminder = (TextView) sheetView.findViewById(R.id.txtSetReminder);

        llRemoveReminder.setVisibility(View.GONE);

        try
        {
            if (edtDuedate.getText().toString().length() > 0)
            {
                String date = edtDuedate.getText().toString();
                String[] datearr = date.split("\\s");
                txtDate.setText(datearr[0]);
                String selectedTime = datearr[1] + " " + datearr[2];
                txtTime.setText(selectedTime);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        llDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    showDatePickerDialog(txtDate);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (txtDate.getText().length() > 0 && !txtDate.getText().equals("Select Date"))
                {
                    AppUtils.showTimePickerDialog(txtTime, activity);
                }
                else
                {
                    Toast.makeText(activity, "Please select date.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        llSetReminder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (checkValidationDueDate(txtDate, txtTime))
                    {
                        selectedDueDate = txtDate.getText().toString().trim() + " " + txtTime.getText().toString().trim();
                        AppUtils.printLog(activity, "Selected Due Date", selectedDueDate);
                        edtDuedate.setText(selectedDueDate);

                        bottomSheetDialog.dismiss();
                        bottomSheetDialog.cancel();
                    }
                }
                catch (Resources.NotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        });

        llRemoveReminder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private boolean checkValidationDueDate(TextView txtDate, TextView txtTime)
    {
        boolean isValid = true;

        if (txtDate.getText().length() == 0 || txtDate.getText().equals("Select Date"))
        {
            Toast.makeText(activity, "Please select reminder date.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        else if (txtTime.getText().length() == 0 || txtTime.getText().equals("Select Time"))
        {
            Toast.makeText(activity, "Please select reminder time.", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void showDatePickerDialog(TextView textView)
    {
        try
        {
            DialogFragment newFragment = new AppUtils.SelectDateFragment(textView);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkValidation()
    {
        boolean isValid = true;
        if (edtActivityName.getText().toString().length() == 0)
        {
            inputActivityName.setError("Please enter activity name.");
            isValid = false;
        }
        else if (selectedEmployeeId.length() == 0)
        {
            inputEmployee.setError("Please select employee.");
            isValid = false;
        }
        else if (scheme_id.toString().length() == 0)
        {
            inputScheme.setError("Please select scheme.");
            isValid = false;
        }
        else if (status_id.toString().length() == 0)
        {
            inputStatus.setError("Please select status.");
            isValid = false;
        }
        else if (activity_type_id.toString().length() == 0)
        {
            inputActivityType.setError("Please select activity type.");
            isValid = false;
        }
        else if (edtHours.getText().toString().length() == 0 && edtMinutes.getText().toString().length() == 0)
        {
            inputHours.setError("Please enter your working time in an hours.");
            inputMinutes.setError("Please enter your working time in minutes.");
            isValid = false;
        }

        AppUtils.removeError(edtActivityName, inputActivityName);
        AppUtils.removeError(edtEmployee, inputEmployee);
        AppUtils.removeError(edtScheme, inputScheme);
        AppUtils.removeError(edtStatus, inputStatus);
        AppUtils.removeError(edtActivityType, inputActivityType);
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

