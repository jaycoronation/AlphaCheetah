package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cheetah.alphacapital.databinding.ActivityAddEmployeeBinding;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.EmployeeDetailResponse;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.reportApp.getset.EmployeeTypeResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;


public class AddEmployeeActivity extends AppCompatActivity
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
    private TextInputLayout inputFirstName;
    private EditText edtFirstName;
    private TextInputLayout inputLastName;
    private EditText edtLastName;
    private TextInputLayout inputPassword;
    private EditText edtPassword;
    private TextInputLayout inputContactNumber;
    private EditText edtContactNumber;
    private TextInputLayout inputSignUpEmail;
    private EditText edtSignUpEmail;
    private TextInputLayout inputAddress;
    private EditText edtAddress;
    private TextInputLayout inputCountry;
    private EditText edtCountry;
    private TextInputLayout inputState;
    private EditText edtState;
    private TextInputLayout inputCity;
    private EditText edtCity;
    private TextInputLayout inputIsAdmin;
    private EditText edtIsAdmin;
    private TextInputLayout inputIsActive;
    private EditText edtIsActive;
    private LinearLayout llSubmit;
    private ProgressBar pbCountry;
    private ProgressBar pbState;
    private ProgressBar pbCity;

    private TextInputLayout inputEmpType;
    private EditText edtEmpType;

    private TextInputLayout inputBirthdate;
    private EditText edtBirthdate;
    private TextInputLayout inputFathername;
    private EditText edtFatherName;
    private TextInputLayout inputSpouseName;
    private EditText edtSpouseName;

    private TextInputLayout inputChildrenName;
    private EditText edtChildernName;
    private TextInputLayout inputDegreeName;
    private EditText edtDegreeName;
    private TextInputLayout inputExamName;
    private EditText edtExamName;

    private ArrayList<CommonGetSet> listEmpType = new ArrayList<>();
    private ArrayList<CommonGetSet> listCountry = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listState = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listCity = new ArrayList<CommonGetSet>();
    private CommonListAdapter commonListAdapter;
    private boolean isStatusBarHidden = false, is_status_active = false,is_Admin =false;
    private String country_id = "", state_id = "", city_id = "", country_name = "", state_name = "", city_name = ""
            ,empTypeName = "",empTypeId = "";

    private String isFor = "";
    private EmployeeListResponse.DataBean.AllEmployeeBean getSet = new EmployeeListResponse.DataBean.AllEmployeeBean();

    private ApiInterface apiService;

    private ActivityAddEmployeeBinding binding;
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

        isFor = getIntent().getStringExtra("isFor");

        binding = DataBindingUtil.setContentView(activity,R.layout.activity_add_employee);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        setupViews();
        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getEmployeeType();
            GetAllCountry();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void getEmployeeDetails()
    {
        if (sessionManager.isNetworkAvailable())
        {
            Log.e("<><><>SESSION USERID",sessionManager.getUserId());
            llLoading.setVisibility(View.VISIBLE);
            apiService.getEmployeeDetails(sessionManager.getUserId()).enqueue(new Callback<EmployeeDetailResponse>() {
                @Override
                public void onResponse(Call<EmployeeDetailResponse> call, Response<EmployeeDetailResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.edtFirstName.setText(AppUtils.toDisplayCase(response.body().getData().getFirst_name()));
                            binding.edtLastName.setText(AppUtils.toDisplayCase( response.body().getData().getLast_name()));
                            binding.edtContactNumber.setText( response.body().getData().getContact_no());
                            binding.edtSignUpEmail.setText( response.body().getData().getEmail());
                            binding.edtPassword.setText( response.body().getData().getPassword());
                            binding.edtAddress.setText( response.body().getData().getAddress());
                            binding.edtCountry.setText( response.body().getData().getCountry_name());
                            binding.edtState.setText( response.body().getData().getState_name());
                            binding.edtCity.setText( response.body().getData().getCity_name());
                            binding.edtEmpType.setText( response.body().getData().getEmp_type());
                            binding.edtBirthdate.setText(AppUtils.universalDateConvert(response.body().getData().getBirthdate(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy"));
                            binding.edtFatherName.setText( response.body().getData().getParents_name());
                            binding.edtSpouseName.setText( response.body().getData().getSpouse_name());
                            binding.edtChildernName.setText( response.body().getData().getChildren_name());
                            binding.edtDegreeName.setText( response.body().getData().getDegrees());
                            binding.edtExamName.setText( response.body().getData().getExaminations_cleared());
                            binding.edtIsAdmin.setText(response.body().getData().isIs_admin() == true ? "Yes" : "No");
                            binding.edtIsActive.setText( response.body().getData().isIs_active() == true ? "Yes" : "No");
                            //binding.tvDateOfJoin.setText(AppUtils.universalDateConvert(response.body().getData().getCreated_date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd MMM, yyyy"));
                        }
                        else
                        {
                            AppUtils.showToast(activity, "Error while getting Employee details");
                        }
                    }
                    else
                    {
                        AppUtils.showToast(activity, "Error while getting Employee details");
                    }
                    llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<EmployeeDetailResponse> call, Throwable t)
                {
                    AppUtils.showToast(activity, "Error while getting Employee details");
                    llLoading.setVisibility(View.GONE);
                }
            });
        }
        else
        {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
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
            inputFirstName = (TextInputLayout) findViewById(R.id.inputFirstName);
            edtFirstName = (EditText) findViewById(R.id.edtFirstName);
            inputLastName = (TextInputLayout) findViewById(R.id.inputLastName);
            edtLastName = (EditText) findViewById(R.id.edtLastName);
            inputPassword = (TextInputLayout) findViewById(R.id.inputPassword);
            edtPassword = (EditText) findViewById(R.id.edtPassword);
            inputContactNumber = (TextInputLayout) findViewById(R.id.inputContactNumber);
            edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
            inputSignUpEmail = (TextInputLayout) findViewById(R.id.inputSignUpEmail);
            edtSignUpEmail = (EditText) findViewById(R.id.edtSignUpEmail);
            inputAddress = (TextInputLayout) findViewById(R.id.inputAddress);
            edtAddress = (EditText) findViewById(R.id.edtAddress);
            inputCountry = (TextInputLayout) findViewById(R.id.inputCountry);
            edtCountry = (EditText) findViewById(R.id.edtCountry);
            inputState = (TextInputLayout) findViewById(R.id.inputState);
            edtState = (EditText) findViewById(R.id.edtState);
            inputCity = (TextInputLayout) findViewById(R.id.inputCity);
            edtCity = (EditText) findViewById(R.id.edtCity);
            inputIsAdmin = (TextInputLayout) findViewById(R.id.inputIsAdmin);
            edtIsAdmin = (EditText) findViewById(R.id.edtIsAdmin);
            inputIsActive = (TextInputLayout) findViewById(R.id.inputIsActive);
            edtIsActive = (EditText) findViewById(R.id.edtIsActive);
            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            pbCountry = (ProgressBar) findViewById(R.id.pbCountry);
            pbState = (ProgressBar) findViewById(R.id.pbState);
            pbCity = (ProgressBar) findViewById(R.id.pbCity);
            edtPassword.setTransformationMethod(new PasswordTransformationMethod());
            edtPassword.setTypeface(AppUtils.getTypefaceRegular(activity));


            inputEmpType = findViewById(R.id.inputEmpType);
            edtEmpType = findViewById(R.id.edtEmpType);

            inputBirthdate = findViewById(R.id.inputBirthdate);
            inputFathername = findViewById(R.id.inputFathername);
            inputSpouseName = findViewById(R.id.inputSpouseName);
            inputChildrenName = findViewById(R.id.inputChildrenName);
            inputDegreeName = findViewById(R.id.inputDegreeName);
            inputExamName = findViewById(R.id.inputExamName);

            edtBirthdate = findViewById(R.id.edtBirthdate);
            edtFatherName = findViewById(R.id.edtFatherName);
            edtSpouseName = findViewById(R.id.edtSpouseName);
            edtChildernName = findViewById(R.id.edtChildernName);
            edtDegreeName = findViewById(R.id.edtDegreeName);
            edtExamName = findViewById(R.id.edtExamName);

            if(isFor.equals("edit"))
            {
                if (getIntent().hasExtra("ClientInfo"))
                {
                    getSet = new Gson().fromJson(getIntent().getStringExtra("ClientInfo"),EmployeeListResponse.DataBean.AllEmployeeBean.class);
                    Log.e("<><><>GETSET", getSet.getFirst_name() + getSet.getLast_name());
                }
                else
                {
                    getEmployeeDetails();
                }
            }

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
            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Employee");
            }
            else
            {
                txtTitle.setText("Update Employee");
            }
            llBackNavigation.setVisibility(View.VISIBLE);

            if (isFor.equalsIgnoreCase("edit"))
            {
                binding.edtFirstName.setText(getSet.getFirst_name());
                binding.edtFirstName.setSelection(getSet.getFirst_name().length());
                binding.edtLastName.setText(getSet.getLast_name());
                binding.edtPassword.setText(getSet.getPassword());
                binding.edtContactNumber.setText(getSet.getContact_no());
                binding.edtSignUpEmail.setText(getSet.getEmail());
                binding.edtAddress.setText(getSet.getAddress());
                binding.edtCountry.setText(getSet.getCountry_name());
                binding.edtCity.setText(getSet.getCity_name());
                binding.edtState.setText(getSet.getState_name());

                binding.edtEmpType.setText(getSet.getEmp_type()!=null ? getSet.getEmp_type() : "");
                binding.edtBirthdate.setText(getSet.getBirthdate()!=null ? getSet.getBirthdate() : "");//Change
                binding.edtFatherName.setText(getSet.getParents_name()!=null ? getSet.getParents_name() : "");
                binding.edtSpouseName.setText(getSet.getSpouse_name()!=null ? getSet.getSpouse_name() : "");
                binding.edtChildernName.setText(getSet.getChildren_name()!=null ? getSet.getChildren_name() : "");
                binding.edtDegreeName.setText(getSet.getDegrees()!=null ? getSet.getDegrees() : "");
                binding.edtExamName.setText(getSet.getExaminations_cleared()!=null ? getSet.getExaminations_cleared() : "");

                if(getSet.getBirthdate()!=null && getSet.getBirthdate().length()>0)
                {
                    try
                    {
                        binding.edtBirthdate.setText(AppUtils.universalDateConvert(getSet.getBirthdate().trim(),"yyyy-MM-dd'T'HH:mm:ss","dd/MM/yyyy"));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                try
                {
                    empTypeName = getSet.getEmp_type()!=null && getSet.getEmp_type().length()>0 ? getSet.getEmp_type() : "";
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                if (getSet.isIs_active())
                {
                    binding.edtIsActive.setText("Active");
                    is_status_active = true;
                }
                else
                {
                    binding.edtIsActive.setText("InActive");
                    is_status_active = false;
                }

                if (getSet.isIs_admin())
                {
                    binding.edtIsAdmin.setText("True");
                    is_Admin = true;
                }
                else
                {
                    binding.edtIsAdmin.setText("False");
                    is_Admin = false;
                }

                country_id = String.valueOf(getSet.getCountry_id());
                state_id = String.valueOf(getSet.getState_id());
                city_id = String.valueOf(getSet.getCity_id());
                country_name = getSet.getCountry_name();
                state_name = getSet.getState_name();
                city_name = getSet.getCity_name();
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

        edtCountry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (listCountry.size() > 0)
                    {
                        showDialog("Country");
                    }
                    else
                    {
                        Toast.makeText(activity, "No Country List Found.", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtState.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (country_id.length() != 0)
                    {
                        if (listState.size() > 0)
                        {
                            showDialog("State");
                        }
                        else
                        {
                            Toast.makeText(activity, "No State List Found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, "Please select Country.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if (country_id.length() != 0)
                    {
                        if (state_id.length() != 0)
                        {
                            if (listCity.size() > 0)
                            {
                                showDialog("City");
                            }
                            else
                            {
                                Toast.makeText(activity, "No City List Found.", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(activity, "Please select State.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, "Please select Country.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        edtIsAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    showIsAdminDialog();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtIsActive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    showIsActiveDialog();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        edtEmpType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("Employee Type");
            }
        });

        edtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(edtBirthdate);
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //MitsUtils.hideKeyboard(activity);
                AppUtils.hideKeyboard(edtAddress,activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        AddEmployeeAsync(edtFirstName.getText().toString().trim(),
                                edtLastName.getText().toString().trim(),
                                edtContactNumber.getText().toString().trim(),
                                edtSignUpEmail.getText().toString().trim(),
                                edtPassword.getText().toString().trim(),
                                edtAddress.getText().toString().trim(),
                                edtExamName.getText().toString().trim(),
                                edtFatherName.getText().toString().trim(),
                                edtChildernName.getText().toString().trim(),
                                edtSpouseName.getText().toString().trim(),
                                edtBirthdate.getText().toString().trim()+" 12:00 AM",
                                edtDegreeName.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void AddEmployeeAsync(final String firstname,
                                  final String lastname,
                                  final String contactNumber,
                                  final String email,
                                  final String password,
                                  final String address,
                                  final String exam,
                                  final String fatherName,
                                  final String childrenName,
                                  final String spouseName,
                                  final String birthdate,
                                  final String degree)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private int client_id_res;
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
                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            hashMap.put("id", String.valueOf(getSet.getId()));
                        }
                        else
                        {
                            hashMap.put("id", "0");
                        }
                        hashMap.put("first_name", firstname);
                        hashMap.put("last_name", lastname);
                        hashMap.put("password", password);
                        hashMap.put("contact_no", contactNumber);
                        hashMap.put("email", email);
                        hashMap.put("address", address);
                        hashMap.put("country_id", country_id);
                        hashMap.put("state_id", state_id);
                        hashMap.put("city_id", city_id);

                        hashMap.put("emp_type",empTypeName);

                        hashMap.put("examinations_cleared",exam);
                        hashMap.put("parents_name",fatherName);
                        hashMap.put("spouse_name",spouseName);
                        hashMap.put("birthdate",birthdate);
                        hashMap.put("children_name",childrenName);
                        hashMap.put("degrees",degree);


                        if (is_status_active)
                        {
                            hashMap.put("is_active", String.valueOf(true));
                        }
                        else
                        {
                            hashMap.put("is_active", String.valueOf(false));
                        }

                        if (is_Admin)
                        {
                            hashMap.put("is_admin", String.valueOf(true));
                        }
                        else
                        {
                            hashMap.put("is_admin", String.valueOf(false));
                        }



                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            AppUtils.printLog(activity, "UPDATE_EMPLOYEE Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_EMPLOYEE, hashMap);
                            AppUtils.printLog(activity, "UPDATE_EMPLOYEE Response ", response.toString());
                        }
                        else
                        {
                            AppUtils.printLog(activity, "ADD_EMPLOYEE Request ", hashMap.toString());
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_EMPLOYEE, hashMap);
                            AppUtils.printLog(activity, "ADD_EMPLOYEE Response ", response.toString());
                        }

                        JSONObject jsonObject = new JSONObject(response);

                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                        if (is_success)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONObject dataObj = jsonObject.getJSONObject("data");

                                client_id_res = AppUtils.getValidAPIIntegerResponseHas(dataObj, "id");
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
                protected void onPostExecute(Void result)
                {
                    super.onPostExecute(result);

                    try
                    {
                        if (is_success)
                        {
                            llLoading.setVisibility(View.GONE);
                            if (ManageEmployeeActivity.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                ManageEmployeeActivity.handler.sendMessage(message);
                            }

                            activity.finish();
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
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

    private void GetAllCountry()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;

            @Override
            protected void onPreExecute()
            {
                pbCountry.setVisibility(View.VISIBLE);
                listCountry = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_COUNTRY, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALL_COUNTRY", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccess)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "name"));
                                listCountry.add(commonGetSet);
                            }
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
            protected void onPostExecute(Void result)
            {
                pbCountry.setVisibility(View.GONE);
                super.onPostExecute(result);

                if (isFor.equalsIgnoreCase("edit"))
                {
                    GetAllState(country_id);
                }

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void GetAllState(final String countryId)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;

            @Override
            protected void onPreExecute()
            {
                pbState.setVisibility(View.VISIBLE);
                listState = new ArrayList<CommonGetSet>();

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("countryid", countryId);

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Get_All_State, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALL_COUNTRY", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccess)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "name"));
                                listState.add(commonGetSet);
                            }
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
            protected void onPostExecute(Void result)
            {
                pbState.setVisibility(View.GONE);
                super.onPostExecute(result);

                if (isFor.equalsIgnoreCase("edit"))
                {
                    GetAllCity(state_id);
                }


            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void GetAllCity(final String stateId)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;

            @Override
            protected void onPreExecute()
            {
                pbCity.setVisibility(View.VISIBLE);
                listCity = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("stateid", stateId);

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Get_All_City, hashMap);

                    AppUtils.printLog(activity, "<><> GET_ALL_COUNTRY", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccess)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++)
                            {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                CommonGetSet commonGetSet = new CommonGetSet();
                                commonGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataObject, "name"));
                                listCity.add(commonGetSet);
                            }
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
            protected void onPostExecute(Void result)
            {
                pbCity.setVisibility(View.GONE);
                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
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
            rvList.setLayoutManager(new LinearLayoutManager(activity));

            AppUtils.showKeyboard(edtSearch, activity);

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

            if(isFor.equals("Employee Type"))
            {
                edtSearch.setVisibility(View.GONE);
            }

            edtSearch.addTextChangedListener(new TextWatcher()
            {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    int textlength = edtSearch.getText().length();

                    ArrayList<CommonGetSet> list_search = new ArrayList<CommonGetSet>();

                    if (isFor.equalsIgnoreCase("Country"))
                    {
                        for (int i = 0; i < listCountry.size(); i++)
                        {
                            if (textlength <= listCountry.get(i).getName().length())
                            {
                                if (listCountry.get(i).getName().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                                {
                                    list_search.add(listCountry.get(i));
                                }
                            }
                        }
                    }
                    else if (isFor.equalsIgnoreCase("State"))
                    {
                        for (int i = 0; i < listState.size(); i++)
                        {
                            if (textlength <= listState.get(i).getName().length())
                            {
                                if (listState.get(i).getName().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                                {
                                    list_search.add(listState.get(i));
                                }
                            }
                        }
                    }
                    else if (isFor.equalsIgnoreCase("City"))
                    {
                        for (int i = 0; i < listCity.size(); i++)
                        {
                            if (textlength <= listCity.get(i).getName().length())
                            {
                                if (listCity.get(i).getName().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                                {
                                    list_search.add(listCity.get(i));
                                }
                            }
                        }
                    }

                    AppendList(list_search, rvList, dialog, isFor, edtSearch);
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

            if (isFor.equalsIgnoreCase("Country"))
            {
                commonListAdapter = new CommonListAdapter(listCountry, dialog, isFor, edtSearch);
                rvList.setAdapter(commonListAdapter);
            }
            else if (isFor.equalsIgnoreCase("State"))
            {
                commonListAdapter = new CommonListAdapter(listState, dialog, isFor, edtSearch);
                rvList.setAdapter(commonListAdapter);
            }
            else if (isFor.equalsIgnoreCase("City"))
            {
                commonListAdapter = new CommonListAdapter(listCity, dialog, isFor, edtSearch);
                rvList.setAdapter(commonListAdapter);
            }
            else if(isFor.equalsIgnoreCase("Employee Type"))
            {
                commonListAdapter = new CommonListAdapter(listEmpType, dialog, isFor, edtSearch);
                rvList.setAdapter(commonListAdapter);
            }


            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class CommonListAdapter extends RecyclerView.Adapter<CommonListAdapter.ViewHolder>
    {
        ArrayList<CommonGetSet> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;

        public CommonListAdapter(ArrayList<CommonGetSet> list, Dialog dialog, String isFor, EditText edtSearch)
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
                final CommonGetSet getSet = listItems.get(position);
                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(getSet.getName());

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
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (dialogFor.equalsIgnoreCase("Country"))
                        {
                            country_id = String.valueOf(getSet.getId());
                            country_name = getSet.getName();
                            edtCountry.setText(country_name);

                            state_id = "";
                            state_name = "";
                            city_id = "";
                            city_name = "";
                            edtState.setText("");
                            edtCity.setText("");

                            GetAllState(country_id);
                        }
                        else if (dialogFor.equalsIgnoreCase("State"))
                        {
                            state_id = String.valueOf(getSet.getId());
                            state_name = getSet.getName();
                            edtState.setText(state_name);

                            city_id = "";
                            city_name = "";
                            edtCity.setText("");

                            GetAllCity(state_id);
                        }
                        else if (dialogFor.equalsIgnoreCase("City"))
                        {
                            city_id = String.valueOf(getSet.getId());
                            city_name = getSet.getName();
                            edtCity.setText(city_name);
                        }
                        else if (dialogFor.equalsIgnoreCase("Employee Type"))
                        {

                            empTypeId = String.valueOf(getSet.getId());
                            empTypeName = getSet.getName();
                            edtEmpType.setText(empTypeName);
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

    private void AppendList(ArrayList<CommonGetSet> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        commonListAdapter = new CommonListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(commonListAdapter);
        commonListAdapter.notifyDataSetChanged();
    }

    private void showIsAdminDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_status);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            AppUtils.setLightStatusBarDialog(dialog, activity);
            final TextView txtInActive;
            final TextView txtActive;
            final ImageView ivBack;

            txtInActive = (TextView) dialog.findViewById(R.id.txtInActive);
            txtActive = (TextView) dialog.findViewById(R.id.txtActive);
            ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
            txtInActive.setText("False");
            txtActive.setText("True");

            ivBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
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


            txtInActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_Admin = false;
                        edtIsAdmin.setText("False");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            txtActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_Admin = true;
                        edtIsAdmin.setText("True");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showIsActiveDialog()
    {
        try
        {
            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_status);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);
            AppUtils.setLightStatusBarDialog(dialog, activity);
            final TextView txtInActive;
            final TextView txtActive;
            final ImageView ivBack;

            txtInActive = (TextView) dialog.findViewById(R.id.txtInActive);
            txtActive = (TextView) dialog.findViewById(R.id.txtActive);
            ivBack = (ImageView) dialog.findViewById(R.id.ivBack);


            ivBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
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


            txtInActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_status_active = false;
                        edtIsActive.setText("InActive");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            txtActive.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        is_status_active = true;
                        edtIsActive.setText("Active");
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean checkValidation()
    {
        boolean isValid = true;
        if (edtFirstName.getText().toString().length() == 0)
        {
            inputFirstName.setError("Please enter first name.");
            isValid = false;
        }
        else if (edtLastName.getText().toString().length() == 0)
        {
            inputLastName.setError("Please enter last name.");
            isValid = false;
        }
        else if (edtContactNumber.getText().toString().length() != 10)
        {
            inputContactNumber.setError("Please enter valid contact number.");
            isValid = false;
        }
        else if (edtSignUpEmail.getText().toString().length() == 0)
        {
            inputSignUpEmail.setError("Please enter email Id.");
            isValid = false;
        }
        else if (!AppUtils.validateEmail(edtSignUpEmail.getText().toString().trim()))
        {
            inputSignUpEmail.setError("Please enter valid email Id.");
            isValid = false;
        }
        else if (edtPassword.getText().toString().length() == 0)
        {
            inputPassword.setError("Please enter employee password.");
            isValid = false;
        }
        else if (edtAddress.getText().toString().length() == 0)
        {
            inputAddress.setError("Please enter address.");
            isValid = false;
        }
        else if (country_id.length() == 0)
        {
            inputCountry.setError("Please select country name.");
            isValid = false;
        }
        else if (state_id.length() == 0)
        {
            inputState.setError("Please select state name.");
            isValid = false;
        }
        else if (city_id.length() == 0)
        {
            inputCity.setError("Please select city name.");
            isValid = false;
        }
        else if (empTypeName.length() == 0)
        {
            inputEmpType.setError("Please select employee Type.");
            isValid = false;
        }
        else if (edtBirthdate.getText().toString().length() == 0)
        {
            inputBirthdate.setError("Please select birthdate.");
            isValid = false;
        }
        else if (edtFatherName.getText().toString().length() == 0)
        {
            inputFathername.setError("Please enter father name.");
            isValid = false;
        }
        else if (edtIsAdmin.getText().toString().length() == 0)
        {
            inputIsAdmin.setError("Please select isAdmin.");
            isValid = false;
        }
        else if (edtIsActive.getText().toString().length() == 0)
        {
            inputIsActive.setError("Please select isActive.");
            isValid = false;
        }

        AppUtils.removeError(edtFirstName, inputFirstName);
        AppUtils.removeError(edtLastName, inputLastName);
        AppUtils.removeError(edtContactNumber, inputContactNumber);
        AppUtils.removeError(edtSignUpEmail, inputSignUpEmail);
        AppUtils.removeError(edtPassword, inputPassword);
        AppUtils.removeError(edtAddress, inputAddress);
        AppUtils.removeError(edtCountry, inputCountry);
        AppUtils.removeError(edtState, inputState);
        AppUtils.removeError(edtCity, inputCity);
        AppUtils.removeError(edtIsAdmin, inputIsAdmin);
        AppUtils.removeError(edtIsActive, inputIsActive);

        return isValid;
    }

    void getEmployeeType()
    {
        if(sessionManager.isNetworkAvailable())
        {
            apiService.getEmployeeType().enqueue(new Callback<EmployeeTypeResponse>() {
                @Override
                public void onResponse(Call<EmployeeTypeResponse> call, Response<EmployeeTypeResponse> response) {
                    if(response.isSuccessful())
                    {
                        if(response.body().isSuccess())
                        {
                            List<String> types = response.body().getData();
                            if(types.size()>0)
                            {
                                for (int i = 0; i < types.size(); i++)
                                {
                                    CommonGetSet getSet = new CommonGetSet();
                                    getSet.setId(0);
                                    getSet.setName(types.get(i));
                                    listEmpType.add(getSet);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<EmployeeTypeResponse> call, Throwable t) {

                }
            });
        }
    }

    private void showDatePickerDialog(EditText textView)
    {
        try
        {
            DialogFragment newFragment = new SelectDateFragment(textView);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        EditText textView;

        public SelectDateFragment(EditText textView)
        {
            this.textView = textView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            DatePickerDialog datepicker = null;

            if (textView.getText().length() > 0 && !textView.getText().equals("Select Date"))
            {
                try
                {
                    String[] datearr = textView.getText().toString().split("/");
                    int dateint = Integer.parseInt(datearr[0]);
                    int monthint = Integer.parseInt(datearr[1]);
                    int yearint = Integer.parseInt(datearr[2]);
                    datepicker = new DatePickerDialog(getActivity(), this, yearint, monthint - 1, dateint);
                    datepicker.getDatePicker().setMaxDate(new Date().getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    final Calendar calendar = Calendar.getInstance();
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    datepicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);
                    datepicker.getDatePicker().setMaxDate(new Date().getTime());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            return datepicker;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm + 1, dd, textView);
        }

    }

    public static void populateSetDate(int year, int month, int day, TextView textView)
    {
        String Selected_date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date convertedDate2 = new Date();
        try
        {
            convertedDate2 = dateFormat.parse(Selected_date);
            String showDate = df.format(convertedDate2);
            Log.e("showDate", showDate.toString());
            textView.setText(showDate);
            textView.setVisibility(View.VISIBLE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

