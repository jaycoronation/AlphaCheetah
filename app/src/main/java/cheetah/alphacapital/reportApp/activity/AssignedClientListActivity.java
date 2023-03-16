package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;


public class AssignedClientListActivity extends AppCompatActivity
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
    private LinearLayout llRetry,llMonthYear;
    private TextView txtRetry;
    private LinearLayout llNoData;
    private ImageView ivNoData, ivAddClient;
    private RecyclerView rvClientList;
    private LinearLayoutManager linearLayoutManager;

    private boolean isStatusBarHidden = false;

    private BottomSheetDialog listDialog;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private LinearLayout llSelectMonth, llSelectYear;
    private TextView tvMonth, tvYear;
    private String selectedYear = "", selectedMonth = "", selectedMonthName = "";
    private List<String> listYear = new ArrayList<>();
    private List<CommonGetSet> listMonth = new ArrayList<>();

    private ArrayList<AssignedClientGetSetOld> listClient = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<AssignedClientGetSetOld> listClient_Search = new ArrayList<AssignedClientGetSetOld>();
    private AssignedClientListAdapter assignedClientListAdapter;

    EditText edtSearch;
    CardView cvCard;
    ImageView ivSerach;
    ImageView ivClose;
    private Timer timer = new Timer();
    private final long DELAY = 400;

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*try
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

        setContentView(R.layout.activity_assigned_client_list);

        selectedYear = getIntent().getExtras().getString("selectedYear");
        selectedMonth = getIntent().getExtras().getString("selectedMonth");
        selectedMonthName = getIntent().getExtras().getString("selectedMonthName");
        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                getMonthYearData();
                llNoInternet.setVisibility(View.GONE);
                getAllAssignedClientByEmployee();
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                try
                {
                    //Reload All Feeds
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getAllAssignedClientByEmployee();
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });
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
            llNoData = (LinearLayout) findViewById(R.id.llNoData);
            ivNoData = (ImageView) findViewById(R.id.ivNoData);
            ivAddClient = (ImageView) findViewById(R.id.ivAddClient);
            rvClientList = (RecyclerView) findViewById(R.id.rvClientList);
            llMonthYear = (LinearLayout) findViewById(R.id.llMonthYear);
            tvMonth = findViewById(R.id.tvMonth);
            tvYear = findViewById(R.id.tvYear);
            llSelectYear = findViewById(R.id.llSelectYear);
            llSelectMonth = findViewById(R.id.llSelectMonth);

            edtSearch = (EditText) findViewById(R.id.edtSearch);
            cvCard = (CardView) findViewById(R.id.cvCard);
            ivSerach= (ImageView) findViewById(R.id.ivSerach);
            ivClose= (ImageView) findViewById(R.id.ivClose);

            tvMonth.setText(selectedMonthName);
            tvYear.setText(selectedYear);


            linearLayoutManager = new LinearLayoutManager(activity);
            rvClientList.setLayoutManager(linearLayoutManager);
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
            txtTitle.setText("Client List");
            llBackNavigation.setVisibility(View.VISIBLE);

            /*rvClientList.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy)
                {
                    if (dy > 0)
                    {
                        llMonthYear.setVisibility(View.GONE);
                    }
                    else
                    {
                        llMonthYear.setVisibility(View.VISIBLE);
                    }

                    super.onScrolled(recyclerView, dx, dy);
                }
            });*/

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

        llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getMonthYearData();
                        llNoInternet.setVisibility(View.GONE);
                        getAllAssignedClientByEmployee();
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ivAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddClientActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("ClientInfo", (Parcelable) new ClientListResponse.DataBean());
                startActivity(intent);
            }
        });

        llSelectMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(MONTH);
            }
        });

        llSelectYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        ivSerach.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doClose();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    cs = edtSearch.getText().toString().trim();
                    CharSequence finalCs = cs;

                    if (finalCs.length() > 0)
                    {
                        try
                        {
                            timer.cancel();
                            timer = new Timer();

                            timer.schedule(new TimerTask()
                            {
                                @Override
                                public void run()
                                {
                                    activity.runOnUiThread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            listClient_Search = new ArrayList<AssignedClientGetSetOld>();
                                            if (listClient != null && listClient.size() > 0)
                                            {
                                                for (int i = 0; i < listClient.size(); i++)
                                                {
                                                    final String text = listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();

                                                    String text1 = AppUtils.getCapitalText(text);

                                                    String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                    if (text1.contains(cs1))
                                                    {
                                                        listClient_Search.add(listClient.get(i));
                                                    }
                                                }


                                                if (listClient_Search.size() > 0)
                                                {
                                                    assignedClientListAdapter = new AssignedClientListAdapter(listClient_Search, activity);
                                                    rvClientList.setAdapter(assignedClientListAdapter);
                                                    llNoData.setVisibility(View.GONE);
                                                }
                                                else
                                                {
                                                    llNoData.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    });
                                }
                            }, DELAY);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        if (listClient.size() > 0)
                        {
                            assignedClientListAdapter = new AssignedClientListAdapter(listClient, activity);
                            rvClientList.setAdapter(assignedClientListAdapter);
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
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
    }

    private void doClose()
    {
        llBackNavigation.setVisibility(View.VISIBLE);
        txtTitle.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        cvCard.setVisibility(View.GONE);
        ivSerach.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.GONE);
        //MitsUtils.hideKeyboard(activity);
        AppUtils.hideKeyboard(edtSearch,activity);
        edtSearch.setText("");
    }

    private void doSearch()
    {
        edtSearch.requestFocus();
        llBackNavigation.setVisibility(View.GONE);
        txtTitle.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        cvCard.setVisibility(View.VISIBLE);
        ivSerach.setVisibility(View.GONE);
        ivClose.setVisibility(View.VISIBLE);
        //MitsUtils.openKeyboard(edtSearch, activity);
    }

    private void getAllAssignedClientByEmployee()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private String message = "";

            private boolean is_success = false;

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                listClient = new ArrayList<AssignedClientGetSetOld>();
                llNoData.setVisibility(View.GONE);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("employeeid", AppUtils.getEmployeeIdForAdmin(activity));
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("CurrentMonth", selectedMonth);
                    hashMap.put("CurrentYear", selectedYear);

                    AppUtils.printLog(activity, "GetAll api name ", AppAPIUtils.GetAll_Assigned_Client_ByEmployee_For_Details.toString());

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GetAll_Assigned_Client_ByEmployee_For_Details, hashMap);

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject jsonMain = jsonObject.getJSONObject("data");

                            JSONArray allClientByEmployeeArray = jsonMain.getJSONArray("AllClientByEmployee");

                            if (allClientByEmployeeArray.length() > 0)
                            {
                                for (int i = 0; i < allClientByEmployeeArray.length(); i++)
                                {
                                    AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                    JSONObject dataObject = (JSONObject) allClientByEmployeeArray.get(i);
                                    assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                    assignedClientGetSetOld.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                    assignedClientGetSetOld.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                    assignedClientGetSetOld.setOrganization(AppUtils.getValidAPIStringResponseHas(dataObject, "organization"));
                                    assignedClientGetSetOld.setContact_no(AppUtils.getValidAPIStringResponseHas(dataObject, "contact_no"));
                                    assignedClientGetSetOld.setEmail(AppUtils.getValidAPIStringResponseHas(dataObject, "email"));
                                    assignedClientGetSetOld.setAddress(AppUtils.getValidAPIStringResponseHas(dataObject, "address"));
                                    assignedClientGetSetOld.setCountry_name(AppUtils.getValidAPIStringResponseHas(dataObject, "country_name"));
                                    assignedClientGetSetOld.setState_name(AppUtils.getValidAPIStringResponseHas(dataObject, "state_name"));
                                    assignedClientGetSetOld.setCity_name(AppUtils.getValidAPIStringResponseHas(dataObject, "city_name"));
                                    assignedClientGetSetOld.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                    assignedClientGetSetOld.setCountry_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "country_id"));
                                    assignedClientGetSetOld.setState_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "state_id"));
                                    assignedClientGetSetOld.setCity_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "city_id"));
                                    assignedClientGetSetOld.setIs_approved_by_admin(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_approved_by_admin"));
                                    assignedClientGetSetOld.setIs_active(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active"));
                                    assignedClientGetSetOld.setTotalMeeting(AppUtils.getValidAPILongResponseHas(dataObject, "TotalMeeting"));
                                    assignedClientGetSetOld.setAUM(AppUtils.getValidAPILongResponseHas(dataObject, "AUM"));
                                    assignedClientGetSetOld.setMonth_End_AUM(AppUtils.getValidAPILongResponseHas(dataObject, "Month_End_AUM"));
                                    assignedClientGetSetOld.setInflow_outflow(AppUtils.getValidAPILongResponseHas(dataObject, "Inflow_outflow"));
                                    assignedClientGetSetOld.setTotalPunchingHour_new(AppUtils.getValidAPIStringResponseHas(dataObject, "TotalPunchingHour_new"));
                                    assignedClientGetSetOld.setSIP(AppUtils.getValidAPILongResponseHas(dataObject, "SIP"));
                                    assignedClientGetSetOld.setTotalPunchingHour(AppUtils.getValidAPIIntegerResponseHas(dataObject, "TotalPunchingHour"));

                                    if (dataObject.has("lstCollaborativeEmployee"))
                                    {
                                        String employeeName = "";

                                        ArrayList<CommonGetSet> listCollaborativeEmployee = new ArrayList<CommonGetSet>();

                                        JSONArray lstCollaborativeEmployeeArray = dataObject.getJSONArray("lstCollaborativeEmployee");

                                        if (lstCollaborativeEmployeeArray.length() > 0)
                                        {
                                            for (int j = 0; j < lstCollaborativeEmployeeArray.length(); j++)
                                            {
                                                CommonGetSet commonGetSet = new CommonGetSet();
                                                JSONObject dataCollaborativeEmployee = (JSONObject) lstCollaborativeEmployeeArray.get(j);
                                                commonGetSet.setName(AppUtils.getValidAPIStringResponseHas(dataCollaborativeEmployee, "first_name") + " " + AppUtils.getValidAPIStringResponseHas(dataCollaborativeEmployee, "last_name"));

                                                listCollaborativeEmployee.add(commonGetSet);

                                                if (j == lstCollaborativeEmployeeArray.length() - 1)
                                                {
                                                    employeeName = employeeName + commonGetSet.getName();
                                                }
                                                else
                                                {
                                                    employeeName = employeeName + commonGetSet.getName() + ",";
                                                }
                                            }
                                        }

                                        assignedClientGetSetOld.setListCollaborativeEmployee(listCollaborativeEmployee);

                                        assignedClientGetSetOld.setEmployeeName(employeeName);

                                    }

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
                        if (listClient.size() > 0)
                        {
                            ivSerach.setVisibility(View.VISIBLE);
                            assignedClientListAdapter = new AssignedClientListAdapter(listClient, activity);
                            rvClientList.setAdapter(assignedClientListAdapter);
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            ivSerach.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        ivSerach.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
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

    public class AssignedClientListAdapter extends RecyclerView.Adapter<AssignedClientListAdapter.ViewHolder>
    {
        ArrayList<AssignedClientGetSetOld> listItems;
        private Activity activity;
        private SessionManager sessionManager;

        public AssignedClientListAdapter(ArrayList<AssignedClientGetSetOld> listClient, Activity activity)
        {
            this.listItems = listClient;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public AssignedClientListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_assigned_client_list_new, viewGroup, false);
            return new AssignedClientListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AssignedClientListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AssignedClientGetSetOld getSet = listItems.get(position);

                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));


                if (getSet.getOrganization().length() > 0)
                {
                    holder.txtOrganization.setText("(" + AppUtils.toDisplayCase(getSet.getOrganization()) + ")");
                }
                else
                {
                    holder.txtOrganization.setText("");
                }

                if (getSet.getTotalMeeting() > 0)
                {
                    holder.txtMeetingTotal.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getTotalMeeting())));
                }
                else
                {
                    holder.txtMeetingTotal.setText("N.A");
                }

                if (getSet.getTotalPunchingHour_new().length() > 0)
                {
                    holder.txtPunchingHours.setText(String.valueOf(getSet.getTotalPunchingHour_new()) + " Hours");
                }
                else
                {
                    holder.txtPunchingHours.setText("N.A");
                }

                if (getSet.getMonth_End_AUM()> 0)
                {
                    holder.txtAUMTotal.setText(activity.getResources().getString(R.string.ruppe) + AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMonth_End_AUM())));
                    //holder.txtAUMTotal.setText(AppUtils.getFormattedAmount(activity,Long.parseLong(getSet.getAUM())));
                }
                else
                {
                    holder.txtAUMTotal.setText("N.A");
                }
                if (getSet.getSIP() > 0)
                {
                    holder.txtSIP.setText(activity.getResources().getString(R.string.ruppe) + AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSIP())));
                }
                else
                {
                    holder.txtSIP.setText("N.A");
                }

                if (getSet.getInflow_outflow() > 0)
                {
                    holder.txtInflowOutflow.setText(activity.getResources().getString(R.string.ruppe) + AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getInflow_outflow())));
                }
                else
                {
                    holder.txtInflowOutflow.setText("N.A");
                }

                if (getSet.getEmployeeName().length() > 0)
                {
                    holder.txtCollaborativeEmployee.setText(getSet.getEmployeeName());
                }
                else
                {
                    holder.txtCollaborativeEmployee.setText("N.A");
                }


                /*holder.llClientDetails.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, ClientDetailsActivity.class);
                            intent.putExtra("clientid", String.valueOf(getSet.getId()));
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            activity.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });*/

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            ClientListResponse.DataBean bean = new ClientListResponse.DataBean();
                            bean.setId(getSet.getId());
                            bean.setFirst_name(getSet.getFirst_name());
                            bean.setLast_name(getSet.getLast_name());
                            bean.setOrganization(getSet.getOrganization());
                            bean.setContact_no(getSet.getContact_no());
                            bean.setEmail(getSet.getEmail());
                            bean.setAddress(getSet.getAddress());
                            bean.setCountry_id(getSet.getCountry_id());
                            bean.setCountry_name(getSet.getCountry_name());
                            bean.setState_id(getSet.getState_id());
                            bean.setState_name(getSet.getState_name());
                            bean.setCity_id(getSet.getCity_id());
                            bean.setCity_name(getSet.getCity_name());
                            bean.setIs_approved_by_admin(getSet.isIs_approved_by_admin());
                            bean.setIs_active(getSet.isIs_active());
                            bean.setCreated_date(getSet.getCreated_date());

                            Intent intent = new Intent(activity, ClientDetailsActivity.class);
                            intent.putExtra("clientid", String.valueOf(bean.getId()));
                            intent.putExtra("ClientInfo", (Parcelable) bean);
                            activity.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                //click will not work due to visibility gone from xml
                /*holder.imgInfo.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, ActivityListActivity.class);
                            intent.putExtra("ClientInfo", (Parcelable) getSet);
                            activity.startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });*/
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
            private TextView txtName, txtPunchingHours;
            private TextView txtOrganization;
            private TextView txtMeetingTotal;
            private TextView txtAUMTotal;
            private TextView txtSIP, txtInflowOutflow;
            private TextView txtCollaborativeEmployee;
            private ImageView imgInfo;
            private LinearLayout llClientDetails;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                txtPunchingHours = (TextView) convertView.findViewById(R.id.txtPunchingHours);
                txtOrganization = (TextView) convertView.findViewById(R.id.txtOrganization);
                txtMeetingTotal = (TextView) convertView.findViewById(R.id.txtMeetingTotal);
                txtAUMTotal = (TextView) convertView.findViewById(R.id.txtAUMTotal);
                txtSIP = (TextView) convertView.findViewById(R.id.txtSIP);
                txtInflowOutflow = (TextView) convertView.findViewById(R.id.txtInflowOutflow);
                txtCollaborativeEmployee = (TextView) convertView.findViewById(R.id.txtCollaborativeEmployee);
                imgInfo = (ImageView) convertView.findViewById(R.id.imgInfo);
                llClientDetails = (LinearLayout) convertView.findViewById(R.id.llClientDetails);
            }
        }
    }

    private void getMonthYearData()
    {
        new AsyncTask<Void, Void, Void>()
        {


            @Override
            protected void onPreExecute()
            {
                try
                {
                    listYear = new ArrayList<>();
                    listMonth = new ArrayList<>();
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
                return null;
            }


            @Override
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
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
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListAdapter.ViewHolder holder, final int position)
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
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            tvMonth.setText(getSet.getName());
                            selectedMonth = String.valueOf(getSet.getId());
                            if (sessionManager.isNetworkAvailable())
                            {
                                llNoInternet.setVisibility(View.GONE);
                                getAllAssignedClientByEmployee();
                            }
                            else
                            {
                                llNoInternet.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
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
                                tvYear.setText(selectedYear);
                                if (sessionManager.isNetworkAvailable())
                                {
                                    llNoInternet.setVisibility(View.GONE);
                                    getAllAssignedClientByEmployee();
                                }
                                else
                                {
                                    llNoInternet.setVisibility(View.VISIBLE);
                                }
                            }
                        }

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
            else
            {
                return listYear.size();
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

