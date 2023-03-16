package cheetah.alphacapital.reportApp.activity.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.reportApp.activity.ActivityListActivity;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.AddClientActivity;
import cheetah.alphacapital.reportApp.activity.ClientDetailsActivity;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;

public class ManageClientActivityOld extends AppCompatActivity
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
    private LinearLayout llLoading, llNoData;

    private boolean isStatusBarHidden = false;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;

    private ImageView ivNoData, ivAddClient;
    private RecyclerView rvClientList;
    private LinearLayoutManager linearLayoutManager;


    private ArrayList<AssignedClientGetSetOld> listClient = new ArrayList<AssignedClientGetSetOld>();
    private ClientListAdapter clientListAdapter;
    public static Handler handler;

    private BottomSheetDialog listDialog;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private LinearLayout llSelectMonth, llSelectYear;
    private TextView tvMonth, tvYear;
    private String selectedYear = "", selectedMonth = "", selectedMonthName = "";
    private List<String> listYear = new ArrayList<>();
    private List<CommonGetSet> listMonth = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
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
        }

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

        setContentView(R.layout.activity_manage_cleint);

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
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (CustomTextViewSemiBold) findViewById(R.id.txtTitle);

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Manage Client");
            llBackNavigation.setVisibility(View.VISIBLE);

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

            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            llNoData = findViewById(R.id.llNoData);

            ivAddClient = (ImageView) findViewById(R.id.ivAddClient);
            rvClientList = (RecyclerView) findViewById(R.id.rvClientList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvClientList.setLayoutManager(linearLayoutManager);

            tvMonth = findViewById(R.id.tvMonth);
            tvYear = findViewById(R.id.tvYear);
            llSelectYear = findViewById(R.id.llSelectYear);
            llSelectMonth = findViewById(R.id.llSelectMonth);

            tvMonth.setText(selectedMonthName);
            tvYear.setText(selectedYear);

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

        ivAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, AddClientActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("ClientInfo", (Parcelable) new AssignedClientGetSetOld());
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
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("CurrentMonth", selectedMonth);
                    hashMap.put("CurrentYear", selectedYear);
                    hashMap.put("employeeid", sessionManager.getUserId());

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_CLIENTS, hashMap);

                    AppUtils.printLog(activity, "GetAll_Assigned_Client_ByEmployee Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);
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
                            clientListAdapter = new ClientListAdapter(listClient);
                            rvClientList.setAdapter(clientListAdapter);
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
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

    public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ViewHolder>
    {
        ArrayList<AssignedClientGetSetOld> listItems;
        private boolean isdone;

        public ClientListAdapter(ArrayList<AssignedClientGetSetOld> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ClientListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_manage_cleint, viewGroup, false);
            return new ClientListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ClientListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AssignedClientGetSetOld getSet = listItems.get(position);

                isdone = true;

                holder.tvFullName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                if (getSet.getEmail().length() > 0)
                {
                    holder.tvEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.tvEmail.setText("");
                }

                if (getSet.getContact_no().length() > 0)
                {
                    holder.tvPhone.setText(getSet.getContact_no());
                }
                else
                {
                    holder.tvPhone.setText("");
                }

                if (getSet.getAddress().length() > 0)
                {
                    holder.tvAddress.setText(getSet.getAddress());
                }
                else
                {
                    holder.tvAddress.setText("");
                }

                holder.switchApprove.setChecked(getSet.isIs_approved_by_admin());

                holder.switchApprove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        try
                        {
                            if (!isdone)
                            {
                                approveOrNotApprove(getSet, position);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                if (getSet.isIs_active())
                {
                    holder.tvStatus.setText("Active");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.black));
                }
                else
                {
                    holder.tvStatus.setText("Inactive");
                    holder.tvStatus.setTextColor(ContextCompat.getColor(activity, R.color.dash_text_red));
                }

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(activity, AddClientActivity.class);
                        intent.putExtra("isFor", "edit");
                        intent.putExtra("ClientInfo", (Parcelable) getSet);
                        startActivity(intent);
                    }
                });

                holder.ivView.setOnClickListener(new View.OnClickListener()
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
                });

                holder.itemView.setOnClickListener(new View.OnClickListener()
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
                });

                holder.ivDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            selectDeleteDialog(getSet, position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
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

        @Override
        public int getItemCount()
        {
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private TextView tvFullName, tvEmail, tvPhone, tvAddress, tvStatus;
            private Switch switchApprove;
            private ImageView ivEdit, ivDelete, ivView;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
                tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
                tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
                tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
                tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);

                switchApprove = (Switch) convertView.findViewById(R.id.switchApprove);
                ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
                ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                ivView = (ImageView) convertView.findViewById(R.id.ivView);
            }
        }
    }

    public void selectDeleteDialog(final AssignedClientGetSetOld getSet, final int pos)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView txtNo;
        final TextView txtYes;

        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);
        tvTitle = (TextView) sheetView.findViewById(R.id.tvTitle);
        tvDescription = (TextView) sheetView.findViewById(R.id.tvDescription);
        txtNo = (TextView) sheetView.findViewById(R.id.txtNo);
        txtYes = (TextView) sheetView.findViewById(R.id.txtYes);
        tvTitle.setText("Remove Client");
        tvDescription.setText("Are you sure you want to remove this Client?");


        llCancel.setOnClickListener(new View.OnClickListener()
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


        llRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                    removeClient(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeClient(final AssignedClientGetSetOld getSet, final int pos)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;
            private String message = "";

            @Override
            protected void onPreExecute()
            {
                llLoading.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("clientid", String.valueOf(getSet.getId()));
                    AppUtils.printLog(activity, "DELETE_CLIENT Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_CLIENT, hashMap);

                    AppUtils.printLog(activity, "DELETE_CLIENT Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

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
                llLoading.setVisibility(View.GONE);

                try
                {
                    if (isSuccess)
                    {
                        listClient.remove(pos);
                        clientListAdapter.notifyDataSetChanged();
                        if (listClient.size() > 0)
                        {
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

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void approveOrNotApprove(final AssignedClientGetSetOld getSet, final int position)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private String message = "";

            private boolean is_success = false;

            @Override
            protected void onPreExecute()
            {

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("clientid", String.valueOf(getSet.getId()));
                    if (getSet.isIs_approved_by_admin())
                    {
                        hashMap.put("status", String.valueOf(false));
                    }
                    else
                    {
                        hashMap.put("status", String.valueOf(true));
                    }


                    AppUtils.printLog(activity, "APPROVE_NEWADDED_CLIENT_BYEMPLOYEE Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.APPROVE_NEWADDED_CLIENT_BYEMPLOYEE, hashMap);

                    AppUtils.printLog(activity, "APPROVE_NEWADDED_CLIENT_BYEMPLOYEE Response ", response.toString());

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
                    if (is_success)
                    {
                        if (getSet.isIs_approved_by_admin())
                        {
                            getSet.setIs_approved_by_admin(false);

                        }
                        else
                        {
                            getSet.setIs_approved_by_admin(true);
                        }

                        clientListAdapter.notifyItemChanged(position);
                    }


                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
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
