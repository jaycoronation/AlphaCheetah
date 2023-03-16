package cheetah.alphacapital.reportApp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
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

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cheetah.alphacapital.reportApp.activity.admin.ManageClientActivity;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;


public class AddClientActivity extends AppCompatActivity
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
    private CustomTextInputLayout inputFirstName;
    private EditText edtFirstName;
    private CustomTextInputLayout inputLastName;
    private EditText edtLastName;
    private CustomTextInputLayout inputOrganization;
    private EditText edtOrganization;
    private CustomTextInputLayout inputContactNumber;
    private EditText edtContactNumber;
    private CustomTextInputLayout inputSignUpEmail;
    private EditText edtSignUpEmail;
    private CustomTextInputLayout inputAddress;
    private EditText edtAddress;
    private CustomTextInputLayout inputCountry;
    private EditText edtCountry;
    private CustomTextInputLayout inputState;
    private EditText edtState;
    private CustomTextInputLayout inputCity;
    private EditText edtCity;
    private CustomTextInputLayout inputStatus;
    private EditText edtStatus;
    private LinearLayout llSubmit;
    private ProgressBar pbCountry;
    private ProgressBar pbState;
    private ProgressBar pbCity;
    private ArrayList<CommonGetSet> listCountry = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listState = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listCity = new ArrayList<CommonGetSet>();
    private CommonListAdapter commonListAdapter;
    private boolean isStatusBarHidden = false, is_status_active = false;
    private String country_id = "", state_id = "", city_id = "", country_name = "", state_name = "", city_name = "";


    private String isFor = "";
    private ClientListResponse.DataBean getSet = new ClientListResponse.DataBean();

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

        if (getIntent().hasExtra("ClientInfo"))
        {
            getSet = getIntent().getExtras().getParcelable("ClientInfo");
        }

        if (getIntent().hasExtra("clientDetailBean"))
        {
            getSet = new Gson().fromJson(getIntent().getStringExtra("clientDetailBean"), ClientListResponse.DataBean.class);
        }

        setContentView(R.layout.activity_add_client);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            GetAllCountry();
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
            inputFirstName = (CustomTextInputLayout) findViewById(R.id.inputFirstName);
            edtFirstName = (EditText) findViewById(R.id.edtFirstName);
            inputLastName = (CustomTextInputLayout) findViewById(R.id.inputLastName);
            edtLastName = (EditText) findViewById(R.id.edtLastName);
            inputOrganization = (CustomTextInputLayout) findViewById(R.id.inputOrganization);
            edtOrganization = (EditText) findViewById(R.id.edtOrganization);
            inputContactNumber = (CustomTextInputLayout) findViewById(R.id.inputContactNumber);
            edtContactNumber = (EditText) findViewById(R.id.edtContactNumber);
            inputSignUpEmail = (CustomTextInputLayout) findViewById(R.id.inputSignUpEmail);
            edtSignUpEmail = (EditText) findViewById(R.id.edtSignUpEmail);
            inputAddress = (CustomTextInputLayout) findViewById(R.id.inputAddress);
            edtAddress = (EditText) findViewById(R.id.edtAddress);
            inputCountry = (CustomTextInputLayout) findViewById(R.id.inputCountry);
            edtCountry = (EditText) findViewById(R.id.edtCountry);
            inputState = (CustomTextInputLayout) findViewById(R.id.inputState);
            edtState = (EditText) findViewById(R.id.edtState);
            inputCity = (CustomTextInputLayout) findViewById(R.id.inputCity);
            edtCity = (EditText) findViewById(R.id.edtCity);
            inputStatus = (CustomTextInputLayout) findViewById(R.id.inputStatus);
            edtStatus = (EditText) findViewById(R.id.edtStatus);
            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            pbCountry = (ProgressBar) findViewById(R.id.pbCountry);
            pbState = (ProgressBar) findViewById(R.id.pbState);
            pbCity = (ProgressBar) findViewById(R.id.pbCity);

           /* setSupportActionBar(toolbar);

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
                txtTitle.setText("Add Client");
            }
            else
            {
                txtTitle.setText("Update Client");
            }
            llBackNavigation.setVisibility(View.VISIBLE);

            if (isFor.equalsIgnoreCase("edit"))
            {
                edtFirstName.setText(getSet.getFirst_name());
                edtFirstName.setSelection(getSet.getFirst_name().length());
                edtLastName.setText(getSet.getLast_name());
                edtOrganization.setText(getSet.getOrganization());
                edtContactNumber.setText(getSet.getContact_no());
                edtSignUpEmail.setText(getSet.getEmail());
                edtAddress.setText(getSet.getAddress());
                edtCountry.setText(getSet.getCountry_name());
                edtCity.setText(getSet.getCity_name());
                edtState.setText(getSet.getState_name());
                if (getSet.isIs_active())
                {
                    edtStatus.setText("Active");
                    is_status_active = true;
                }
                else
                {
                    edtStatus.setText("InActive");
                    is_status_active = false;
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

        edtStatus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    showStatusDialog();
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

                //MitsUtils.hideKeyboard(activity);
                AppUtils.hideKeyboard(edtAddress,activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        AddClientAsync(edtFirstName.getText().toString().trim(), edtLastName.getText().toString().trim(), edtOrganization.getText().toString().trim(), edtContactNumber.getText().toString().trim(), edtSignUpEmail.getText().toString().trim(), edtAddress.getText().toString().trim());
                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void AddClientAsync(final String firstname, final String lastname, final String organization, final String contactNumber, final String email, final String address)
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
                        hashMap.put("organization", organization);
                        hashMap.put("contact_no", contactNumber);
                        hashMap.put("email", email);
                        hashMap.put("address", address);
                        hashMap.put("country_id", country_id);
                        hashMap.put("state_id", state_id);
                        hashMap.put("city_id", city_id);

                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            hashMap.put("is_approved_by_admin", String.valueOf(getSet.isIs_approved_by_admin()));
                        }
                        else
                        {
                            hashMap.put("is_approved_by_admin", String.valueOf(false));
                        }

                        if (is_status_active)
                        {
                            hashMap.put("is_active", String.valueOf(true));
                        }
                        else
                        {
                            hashMap.put("is_active", String.valueOf(false));
                        }

                        AppUtils.printLog(activity, "ADD_CLIENT Request ", hashMap.toString());

                        if (isFor.equalsIgnoreCase("edit"))
                        {
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_CLIENT_DETAILS, hashMap);
                        }
                        else
                        {
                            response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_CLIENT, hashMap);
                        }

                        AppUtils.printLog(activity, "ADD_CLIENT Response ", response.toString());

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
                            if (isFor.equalsIgnoreCase("add"))
                            {
                                mapClientWithEmployee(String.valueOf(client_id_res));
                            }
                            else
                            {
                                llLoading.setVisibility(View.GONE);

                                ClientListResponse.DataBean getSetedit = new ClientListResponse.DataBean();
                                getSetedit.setId(getSet.getId());
                                getSetedit.setFirst_name(firstname);
                                getSetedit.setLast_name(lastname);
                                getSetedit.setOrganization(organization);
                                getSetedit.setContact_no(contactNumber);
                                getSetedit.setEmail(email);
                                getSetedit.setAddress(address);
                                getSetedit.setCountry_name(edtCountry.getText().toString().trim());
                                getSetedit.setState_name(edtState.getText().toString().trim());
                                getSetedit.setCity_name(edtCity.getText().toString().trim());
                                getSetedit.setIs_active(is_status_active);
                                getSetedit.setCountry_id(Integer.parseInt(country_id));
                                getSetedit.setState_id(Integer.parseInt(state_id));
                                getSetedit.setCity_id(Integer.parseInt(city_id));

                                if (ManageClientActivity.handler != null)
                                {
                                    Message message = Message.obtain();
                                    message.what = 101;
                                    message.obj = getSetedit;
                                    ManageClientActivity.handler.sendMessage(message);
                                }

                                activity.finish();
                            }
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

    private void mapClientWithEmployee(final String client_id_res)
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
                    hashMap.put("client_id", client_id_res);
                    hashMap.put("employee_ids", AppUtils.getEmployeeIdForAdmin(activity));

                    AppUtils.printLog(activity, "Map_Client_With_Employee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Map_Client_With_Employee, hashMap);

                    AppUtils.printLog(activity, "Map_Client_With_Employee Response ", response.toString());

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
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        if (ManageClientActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            ManageClientActivity.handler.sendMessage(message);
                        }

                        if (AssignedClientListActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            AssignedClientListActivity.handler.sendMessage(message);
                        }

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
        public CommonListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new CommonListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CommonListAdapter.ViewHolder holder, final int position)
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

    private void showStatusDialog()
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
                        edtStatus.setText("InActive");
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
                        edtStatus.setText("Active");
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
        else if (edtOrganization.getText().toString().length() == 0)
        {
            inputOrganization.setError("Please enter organization name.");
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
        else if (edtStatus.getText().toString().length() == 0)
        {
            inputStatus.setError("Please select status.");
            isValid = false;
        }


        AppUtils.removeError(edtFirstName, inputFirstName);
        AppUtils.removeError(edtLastName, inputLastName);
        AppUtils.removeError(edtOrganization, inputOrganization);
        AppUtils.removeError(edtContactNumber, inputContactNumber);
        AppUtils.removeError(edtSignUpEmail, inputSignUpEmail);
        AppUtils.removeError(edtAddress, inputAddress);
        AppUtils.removeError(edtCountry, inputCountry);
        AppUtils.removeError(edtState, inputState);
        AppUtils.removeError(edtCity, inputCity);
        AppUtils.removeError(edtStatus, inputStatus);

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

