package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.reportApp.fragment.EmpTargetFragment;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.TargetListResponse;

public class AddEmployeeTargetActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading;
    private TextView tvSubmit;
    private CustomTextInputLayout inputYear;
    private EditText edtYear;
    private CustomTextInputLayout inputMeetingsExisiting;
    private EditText edtMeetingsExisiting;
    private CustomTextInputLayout inputMeetingsNew;
    private EditText edtMeetingsNew;
    private CustomTextInputLayout inputReferenceClient;
    private EditText edtReferenceClient;
    private CustomTextInputLayout inputFreshAum;
    private EditText edtFreshAum;
    private CustomTextInputLayout inputSIP;
    private EditText edtSIP;
    private CustomTextInputLayout inputNewClientsConverted;
    private EditText edtNewClientsConverted;
    private CustomTextInputLayout inputSelfAcquiredAUM;
    private EditText edtSelfAcquiredAUM;

    private CustomTextInputLayout inputInflowOutFlow;
    private EditText edtInflowOutFlow;
    private CustomTextInputLayout inputSummaryMail;
    private EditText edtSummaryMail;
    private CustomTextInputLayout inputDayForwardMail;
    private EditText edtDayForwardMail;

    private LinearLayout llNoInternet;
    private LinearLayout llRetry;

    private LinearLayout llSubmit;
    private boolean isStatusBarHidden = false, is_status_active = false;

    private List<String> listYear = new ArrayList<>();

    private final String YEAR = "Year";

    private BottomSheetDialog listDialog;

    private String selectedYear = "", isFor = "";

    private int employeeId = 0;

    private TargetListResponse.DataBean getSet = new TargetListResponse.DataBean();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       /* try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
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

        employeeId = getIntent().getIntExtra("employeeId",0);

        isFor = getIntent().getStringExtra("isFor");

        if(isFor.equals("edit"))
        {
            getSet = new Gson().fromJson(getIntent().getStringExtra("data"),TargetListResponse.DataBean.class);
        }

        setContentView(R.layout.activity_add_target_employee);

        setupViews();

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getYearData();
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
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (CustomTextViewSemiBold) findViewById(R.id.txtTitle);

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            if (isFor.equalsIgnoreCase("add"))
            {
                txtTitle.setText("Add Employee Target");
            }
            else
            {
                txtTitle.setText("Update Employee Target");
            }
            llBackNavigation.setVisibility(View.VISIBLE);

           /* ImageView ivHeader = findViewById(R.id.ivHeader);
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

            inputYear = (CustomTextInputLayout) findViewById(R.id.inputYear);
            edtYear = (EditText) findViewById(R.id.edtYear);
            inputMeetingsExisiting = (CustomTextInputLayout) findViewById(R.id.inputMeetingsExisiting);
            edtMeetingsExisiting = (EditText) findViewById(R.id.edtMeetingsExisiting);
            inputMeetingsNew = (CustomTextInputLayout) findViewById(R.id.inputMeetingsNew);
            edtMeetingsNew = (EditText) findViewById(R.id.edtMeetingsNew);
            inputReferenceClient = (CustomTextInputLayout) findViewById(R.id.inputReferenceClient);
            edtReferenceClient = (EditText) findViewById(R.id.edtReferenceClient);
            inputFreshAum = (CustomTextInputLayout) findViewById(R.id.inputFreshAum);
            edtFreshAum = (EditText) findViewById(R.id.edtFreshAum);
            inputSIP = (CustomTextInputLayout) findViewById(R.id.inputSIP);
            edtSIP = (EditText) findViewById(R.id.edtSIP);
            inputNewClientsConverted = (CustomTextInputLayout) findViewById(R.id.inputNewClientsConverted);
            edtNewClientsConverted = (EditText) findViewById(R.id.edtNewClientsConverted);
            inputSelfAcquiredAUM = (CustomTextInputLayout) findViewById(R.id.inputSelfAcquiredAUM);
            edtSelfAcquiredAUM = (EditText) findViewById(R.id.edtSelfAcquiredAUM);
            tvSubmit = findViewById(R.id.tvSubmit);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);

            inputInflowOutFlow = (CustomTextInputLayout) findViewById(R.id.inputInflowOutFlow);
            edtInflowOutFlow = (EditText) findViewById(R.id.edtInflowOutFlow);

            inputSummaryMail = (CustomTextInputLayout) findViewById(R.id.inputSummaryMail);
            edtSummaryMail = (EditText) findViewById(R.id.edtSummaryMail);

            inputDayForwardMail = (CustomTextInputLayout) findViewById(R.id.inputDayForwardMail);
            edtDayForwardMail = (EditText) findViewById(R.id.edtDayForwardMail);

            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);

            setSupportActionBar(toolbar);

            if (isFor.equalsIgnoreCase("add"))
            {
                tvSubmit.setText("Add");
                edtYear.setEnabled(true);
            }
            else
            {
                tvSubmit.setText("Update");
                edtYear.setText(String.valueOf(getSet.getYear()));
                edtYear.setEnabled(false);
                edtMeetingsExisiting.setText(String.valueOf(getSet.getMeetings_exisiting()));
                edtMeetingsNew.setText(String.valueOf(getSet.getMeetings_new()));
                edtReferenceClient.setText(String.valueOf(getSet.getReference_client()));
                edtFreshAum.setText(String.valueOf(getSet.getFresh_aum()));
                edtSIP.setText(String.valueOf(getSet.getSip()));
                edtNewClientsConverted.setText(String.valueOf(getSet.getNew_clients_converted()));
                edtSelfAcquiredAUM.setText(String.valueOf(getSet.getSelf_acquired_aum()));
                edtInflowOutFlow.setText(String.valueOf(getSet.getInflow_outflow()));
                edtSummaryMail.setText(String.valueOf(getSet.getSummery_mail()));
                edtDayForwardMail.setText(String.valueOf(getSet.getDay_forward_mail()));
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

        edtYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //MitsUtils.hideKeyboard(activity);
                AppUtils.hideKeyboard(edtDayForwardMail,activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        addEmployeeTarget(edtYear.getText().toString().trim(),
                                edtMeetingsExisiting.getText().toString().trim(),
                                edtMeetingsNew.getText().toString().trim(),
                                edtReferenceClient.getText().toString().trim(),
                                edtFreshAum.getText().toString().trim(),
                                edtSIP.getText().toString().trim(),
                                edtNewClientsConverted.getText().toString().trim(),
                                edtSelfAcquiredAUM.getText().toString().trim(),
                                edtInflowOutFlow.getText().toString().trim(),
                                edtSummaryMail.getText().toString().trim(),
                                edtDayForwardMail.getText().toString().toString());
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

        if (edtYear.getText().toString().trim().equals(""))
        {
            inputYear.setError("Please select Year.");
            isValid = false;
        }
        else if (edtFreshAum.getText().toString().trim().equals(""))
        {
            inputFreshAum.setError("Please enter Fresh Aum.");
            isValid = false;
        }
        else if (edtMeetingsNew.getText().toString().trim().equals(""))
        {
            inputMeetingsNew.setError("Please enter Meetings New.");
            isValid = false;
        }
        else if (edtMeetingsExisiting.getText().toString().trim().equals(""))
        {
            inputMeetingsExisiting.setError("Please enter Meetings Exisiting.");
            isValid = false;
        }
        else if (edtInflowOutFlow.getText().toString().trim().equals(""))
        {
            inputInflowOutFlow.setError("Please enter Inflow/Outflow.");
            isValid = false;
        }
        else if (edtSIP.getText().toString().trim().equals(""))
        {
            inputSIP.setError("Please enter SIP.");
            isValid = false;
        }
        else if (edtReferenceClient.getText().toString().trim().equals(""))
        {
            inputReferenceClient.setError("Please enter Reference Client.");
            isValid = false;
        }
        else if (edtSummaryMail.getText().toString().trim().equals(""))
        {
            inputSummaryMail.setError("Please enter Summary Mail.");
            isValid = false;
        }
        else if (edtDayForwardMail.getText().toString().trim().equals(""))
        {
            inputDayForwardMail.setError("Please enter Day Forward Mail.");
            isValid = false;
        }
        else if (edtNewClientsConverted.getText().toString().trim().equals(""))
        {
            inputNewClientsConverted.setError("Please enter New Clients Converted.");
            isValid = false;
        }
        else if (edtSelfAcquiredAUM.getText().toString().trim().equals(""))
        {
            inputSelfAcquiredAUM.setError("Please enter SelfAcquiredAUM.");
            isValid = false;
        }


        AppUtils.removeError(edtYear, inputYear);
        AppUtils.removeError(edtMeetingsExisiting, inputMeetingsExisiting);
        AppUtils.removeError(edtMeetingsNew, inputMeetingsNew);
        AppUtils.removeError(edtReferenceClient, inputReferenceClient);
        AppUtils.removeError(edtFreshAum, inputFreshAum);
        AppUtils.removeError(edtSIP, inputSIP);
        AppUtils.removeError(edtNewClientsConverted, inputNewClientsConverted);
        AppUtils.removeError(edtSelfAcquiredAUM, inputSelfAcquiredAUM);

        return isValid;
    }

    private void addEmployeeTarget(final String year,
                                   final String meetings_Exisiting,
                                   final String meetings_New,
                                   final String reference_Client,
                                   final String fresh_Aum,
                                   final String sip,
                                   final String new_Clients,
                                   final String self_Acquired_AUM,
                                   final String inFlowOutFlow,
                                   final String summaryMail,
                                   final String dayForwardMail)
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

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            hashMap.put("Id", "0");
                        }
                        else
                        {
                            hashMap.put("Id", String.valueOf(getSet.getId()));
                        }

                        hashMap.put("employee_id", String.valueOf(employeeId));
                        hashMap.put("fresh_aum", fresh_Aum);
                        hashMap.put("meetings_exisiting", meetings_Exisiting);
                        hashMap.put("meetings_new", meetings_New);
                        hashMap.put("new_clients_converted", new_Clients);
                        hashMap.put("reference_client", reference_Client);
                        hashMap.put("self_acquired_aum", self_Acquired_AUM);
                        hashMap.put("sip", sip);
                        hashMap.put("year", year);

                        hashMap.put("Inflow_outflow", inFlowOutFlow);
                        hashMap.put("summery_mail", summaryMail);
                        hashMap.put("day_forward_mail", dayForwardMail);

                        Log.e("EMPLOYEE_TARGET req ", "doInBackground: " + hashMap.toString());

                        String serverResponse = "";

                        if (isFor.equalsIgnoreCase("add"))
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.ADD_EMPLOYEE_TARGET, hashMap);
                        }
                        else
                        {
                            serverResponse = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.UPDATE_EMPLOYEE_TARGET, hashMap);
                        }

                        Log.e("EMPLOYEE_TARGET resp ", "doInBackground: " + serverResponse);

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

                    if (EmpTargetFragment.handler != null)
                    {
                        Message message = Message.obtain();
                        message.what = 1;
                        EmpTargetFragment.handler.sendMessage(message);
                    }

                    activity.finish();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
            }.execute();
        }
        else
        {
            //
        }
    }

    private void getYearData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            new AsyncTask<Void, Void, Void>()
            {
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
                    return null;
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
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        else
        {
            //
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
        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener()
        {
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

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.show();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
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
            if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
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
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
            }
            else
            {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
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
