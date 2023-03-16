package cheetah.alphacapital.reportApp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import cheetah.alphacapital.checkboxlibs.SmoothCheckBox;
import cheetah.alphacapital.reportApp.activity.admin.ManageDailyActivityReportActivity;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;


public class AddDarActivity extends AppCompatActivity
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
    private LinearLayout llSubmit;
    private TextView txtBtnSubmit;
    private RadioGroup rgIsTodayReport;
    private RadioButton rbToday;
    private RadioButton rbYesterday;
    private ArrayList<AssignedClientGetSetOld> listClient = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<CommonGetSet> listActivityType = new ArrayList<CommonGetSet>();
    private ArrayList<String> listHours = new ArrayList<String>();
    private ArrayList<String> listMinutes = new ArrayList<String>();
    private HoursListAdpater hoursListAdpater;
    private ClientListAdapter clientListAdapter;
    private ActivityTypeAdpater activityTypeAdapter;
    private BottomSheetDialog bottomSheetDialog;
    private boolean isStatusBarHidden = false;
    private String status_id = "", status_name = "", activity_type_id = "", status_activity_type_name = "", selectedClientId = "", selectedClientName = "", selectedDueDate = "";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
       /* try
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
        setContentView(R.layout.activity_add_dar);

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
            rbToday.setTypeface(AppUtils.getTypefaceRegular(activity));
            rbYesterday.setTypeface(AppUtils.getTypefaceRegular(activity));

            llSubmit = (LinearLayout) findViewById(R.id.llSubmit);
            txtBtnSubmit = (TextView) findViewById(R.id.txtBtnSubmit);
            setSupportActionBar(toolbar);

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
            ivHeader.setLayoutParams(params);

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            llBackNavigation.setVisibility(View.VISIBLE);

            txtBtnSubmit.setText("Add Daily Activity Report");
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

        edtClient.setOnClickListener(new View.OnClickListener()
        {
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

        llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //MitsUtils.hideKeyboard(activity);
                AppUtils.hideKeyboard(edtDARMessage,activity);

                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        AddDAA_Async(edtDARMessage.getText().toString().trim(), edtHours.getText().toString().trim(), edtRMName.getText().toString().trim(), edtRemarksComment.getText().toString().trim(), edtMinutes.getText().toString().trim());
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
            private boolean isSuccessClient = false, isSuccessForScheme = false, isSuccessForStatus = false, isSuccessForType = false;
            private String messageForClient = "", messageForScheme = "", messageForStatus = "", messageType = "";

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                listClient = new ArrayList<AssignedClientGetSetOld>();
                listActivityType = new ArrayList<CommonGetSet>();
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                getAllEmployee();
                getAllActivityType();
                return null;
            }

            private void getAllEmployee()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("PageIndex", "0");
                    hashMap.put("PageSize", "0");
                    hashMap.put("employeeid", AppUtils.getEmployeeIdForAdmin(activity));

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GetAll_Assigned_Client_ByEmployee, hashMap);

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessClient = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    messageForClient = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (isSuccessClient)
                    {
                        if (jsonObject.has("AllClientByEmployee"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("AllClientByEmployee");

                            if (dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);
                                    assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                    assignedClientGetSetOld.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                    assignedClientGetSetOld.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                    listClient.add(assignedClientGetSetOld);
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

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
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

                    if (isFor.equalsIgnoreCase("Client"))
                    {
                        ArrayList<AssignedClientGetSetOld> list_Employee_Search = new ArrayList<AssignedClientGetSetOld>();

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

    public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder>
    {
        ArrayList<AssignedClientGetSetOld> listItems;
        private Dialog dialog;
        private String dialogFor = "";
        private EditText edtSearch;
        private boolean isdone;

        public ClientListAdapter(ArrayList<AssignedClientGetSetOld> list, Dialog dialog, String isFor, EditText edtSearch)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.dialogFor = isFor;
            this.edtSearch = edtSearch;
        }

        @Override
        public ClientListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_check_box, viewGroup, false);
            return new ClientListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ClientListAdapter.ViewHolder holder, final int position)
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


                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
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
                smoothCheckBox.setVisibility(View.GONE);
            }
        }
    }

    private void AppendClientList(ArrayList<AssignedClientGetSetOld> listItem, RecyclerView recyclerView, Dialog dialog, String isfor, EditText edtSearch)
    {
        clientListAdapter = new ClientListAdapter(listItem, dialog, isfor, edtSearch);
        recyclerView.setAdapter(clientListAdapter);
        clientListAdapter.notifyDataSetChanged();
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

    private void AddDAA_Async(final String dar_meassage, final String hours, final String rmName, final String remarksComment, final String minutes)
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
                        hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("client_id", selectedClientId);
                        hashMap.put("activity_type_id", activity_type_id);
                        hashMap.put("Dar_message", dar_meassage);
                        hashMap.put("RMName", rmName);

                        if (hours.length() == 0)
                        {
                            hashMap.put("TimeSpent", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent", hours);
                        }

                        if (minutes.length() == 0)
                        {
                            hashMap.put("TimeSpent_Min", "0");
                        }
                        else
                        {
                            hashMap.put("TimeSpent_Min", minutes);
                        }

                        hashMap.put("RemarksComment", remarksComment);

                        if (rbToday.isChecked())
                        {
                            hashMap.put("IsTodayReport", String.valueOf(true));
                        }
                        else
                        {
                            hashMap.put("IsTodayReport", String.valueOf(false));
                        }


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
                            if (!sessionManager.isAdmin())
                            {
                                if (ManageDailyActivityReportActivity.handler != null)
                                {
                                    Message msgObj = Message.obtain();
                                    msgObj.what = 100;
                                    ManageDailyActivityReportActivity.handler.sendMessage(msgObj);
                                }
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

