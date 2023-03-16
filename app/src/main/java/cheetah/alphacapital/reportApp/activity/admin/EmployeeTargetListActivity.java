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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cheetah.alphacapital.classes.EndlessRecyclerViewScrollListener;
import cheetah.alphacapital.reportApp.getset.EmployeeTargetGetSet;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;

public class EmployeeTargetListActivity extends AppCompatActivity
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

    private ImageView ivNoData, ivAddTarget;
    private RecyclerView rvTargetList;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading = false;
    private int pageIndex = 0;
    private String pagesize = "50";
    private boolean isLastPage = false;
    private LinearLayout llLoadingMore;

    private ArrayList<EmployeeTargetGetSet> listEmployeeTarget = new ArrayList<EmployeeTargetGetSet>();
    private EmployeeTargetListAdapter employeeTargetListAdapter;
    public static Handler handler;
    AssignedClientGetSetOld assignedClientGetSetOld;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        /*try
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

        setContentView(R.layout.activity_employee_target);

        if (getIntent().hasExtra("ClientInfo"))
        {
            assignedClientGetSetOld = getIntent().getExtras().getParcelable("ClientInfo");
        }

        if (getIntent().hasExtra("getSet"))
        {
        }
        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                pageIndex = 0;
                isLastPage = false;
                getAllEmployeeTarget(true);
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
                            pageIndex = 0;
                            isLastPage = false;
                            getAllEmployeeTarget(true);
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
            txtTitle.setText("Employee Target");
            llBackNavigation.setVisibility(View.VISIBLE);

            /*ImageView ivHeader = findViewById(R.id.ivHeader);
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
*/
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            llLoadingMore = findViewById(R.id.llLoadingMore);
            llNoData = findViewById(R.id.llNoData);

            ivAddTarget = (ImageView) findViewById(R.id.ivAddTarget);
            rvTargetList = (RecyclerView) findViewById(R.id.rvTargetList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvTargetList.setLayoutManager(linearLayoutManager);

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
                   // activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        ivAddTarget.setOnClickListener(v ->
        {
            try
            {
                Intent intent = new Intent(activity, AddEmployeeTargetActivity.class);
                intent.putExtra("isFor", "add");
                intent.putExtra("ClientInfo", (Parcelable) assignedClientGetSetOld);
                startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        rvTargetList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager)
        {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy)
            {
                super.onScrolled(view, dx, dy);
            }

            @Override
            public void onLoadMore(int page, int totalItemsCount)
            {
                try
                {
                    if (!isLoading && !isLastPage)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llLoadingMore.setVisibility(View.VISIBLE);
                            getAllEmployeeTarget(false);
                        }
                        else
                        {
                            llLoadingMore.setVisibility(View.GONE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    private void getAllEmployeeTarget(final boolean isFirstTime)
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
                    isLoading = true;
                    if (isFirstTime)
                    {
                        llLoading.setVisibility(View.VISIBLE);
                        listEmployeeTarget = new ArrayList<EmployeeTargetGetSet>();
                    }
                    else
                    {
                        llLoadingMore.setVisibility(View.VISIBLE);
                    }

                    llNoData.setVisibility(View.GONE);
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
                    hashMap.put("pageindex", String.valueOf(pageIndex));
                    hashMap.put("pagesize", pagesize);
                    hashMap.put("AUM_Year", "0");
                    hashMap.put("employee_id", String.valueOf(assignedClientGetSetOld.getId()));

                    AppUtils.printLog(activity, "GET_ALL_EMPLOYEE_TARGET Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_EMPLOYEE_TARGET, hashMap);

                    AppUtils.printLog(activity, "GET_ALL_EMPLOYEE_TARGET Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {

                        if (jsonObject.has("data"))
                        {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            pageIndex = pageIndex + 1;

                            if (dataArray.length() == 0 || dataArray.length() % 50 != 0)
                            {
                                isLastPage = true;
                            }

                            if (dataArray.length() > 0)
                            {
                                for (int i = 0; i < dataArray.length(); i++)
                                {
                                    EmployeeTargetGetSet employeeTargetGetSet = new EmployeeTargetGetSet();
                                    JSONObject dataObject = (JSONObject) dataArray.get(i);
                                    employeeTargetGetSet.setId(AppUtils.getValidAPIStringResponseHas(dataObject, "id"));
                                    employeeTargetGetSet.setEmployee_id(AppUtils.getValidAPIStringResponseHas(dataObject, "employee_id"));
                                    employeeTargetGetSet.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                    employeeTargetGetSet.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                    employeeTargetGetSet.setYear(AppUtils.getValidAPIStringResponseHas(dataObject, "year"));
                                    employeeTargetGetSet.setMeetings_exisiting(AppUtils.getValidAPIStringResponseHas(dataObject, "meetings_exisiting"));
                                    employeeTargetGetSet.setMeetings_new(AppUtils.getValidAPIStringResponseHas(dataObject, "meetings_new"));
                                    employeeTargetGetSet.setReference_client(AppUtils.getValidAPIStringResponseHas(dataObject, "reference_client"));
                                    employeeTargetGetSet.setFresh_aum(AppUtils.getValidAPIStringResponseHas(dataObject, "fresh_aum"));
                                    employeeTargetGetSet.setSip(AppUtils.getValidAPIStringResponseHas(dataObject, "sip"));
                                    employeeTargetGetSet.setNew_clients_converted(AppUtils.getValidAPIStringResponseHas(dataObject, "new_clients_converted"));
                                    employeeTargetGetSet.setSelf_acquired_aum(AppUtils.getValidAPIStringResponseHas(dataObject, "self_acquired_aum"));
                                    listEmployeeTarget.add(employeeTargetGetSet);
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
                    isLoading = false;
                    llLoading.setVisibility(View.GONE);
                    llLoadingMore.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        if (isFirstTime)
                        {
                            if (listEmployeeTarget.size() > 0)
                            {
                                employeeTargetListAdapter = new EmployeeTargetListAdapter(listEmployeeTarget, activity);
                                rvTargetList.setAdapter(employeeTargetListAdapter);
                                llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.GONE);
                            if (employeeTargetListAdapter.getItemCount() > 0)
                            {
                                employeeTargetListAdapter.notifyDataSetChanged();
                            }
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

    public class EmployeeTargetListAdapter extends RecyclerView.Adapter<EmployeeTargetListAdapter.ViewHolder>
    {
        ArrayList<EmployeeTargetGetSet> listItems;
        private Activity activity;
        private SessionManager sessionManager;

        public EmployeeTargetListAdapter(ArrayList<EmployeeTargetGetSet> listEmployeeTarget, Activity activity)
        {
            this.listItems = listEmployeeTarget;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public EmployeeTargetListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_employee_target, viewGroup, false);
            return new EmployeeTargetListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EmployeeTargetListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final EmployeeTargetGetSet getSet = listItems.get(position);
                holder.tvFullName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));
                holder.tvYear.setText(getSet.getYear());
                holder.tvFreshAUM.setText(AppUtils.convertToDecimalValueForListing(getSet.getFresh_aum()));
                holder.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(getSet.getSelf_acquired_aum()));
                holder.tvSip.setText(AppUtils.convertToDecimalValueForListing(getSet.getSip()));
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(activity, AddEmployeeTargetActivity.class);
                        intent.putExtra("isFor", "edit");
                        intent.putExtra("ClientInfo", (Parcelable) assignedClientGetSetOld);
                        intent.putExtra("EmployeeTargetGetSet", (Parcelable) getSet);
                        startActivity(intent);
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
            private TextView tvFullName;
            private TextView tvYear;
            private TextView tvFreshAUM;
            private TextView tvSelfAcquiredAUM;
            private TextView tvSip;
            private ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                tvFullName = (TextView) convertView.findViewById(R.id.tvFullName);
                tvYear = (TextView) convertView.findViewById(R.id.tvYear);
                tvFreshAUM = (TextView) convertView.findViewById(R.id.tvFreshAUM);
                tvSelfAcquiredAUM = (TextView) convertView.findViewById(R.id.tvSelfAcquiredAUM);
                tvSip = (TextView) convertView.findViewById(R.id.tvSip);
                ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
                ivEdit.setVisibility(View.GONE);
            }
        }
    }

    public void selectDeleteDialog(final EmployeeTargetGetSet getSet, final int pos)
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
        tvTitle.setText("Remove Employee");
        tvDescription.setText("Are you sure you want to remove this Employee?");


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
                    removeEmployee(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeEmployee(final EmployeeTargetGetSet getSet, final int pos)
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
                    hashMap.put("employeeid", String.valueOf(getSet.getId()));
                    AppUtils.printLog(activity, "DELETE_EMPLOYEE Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_EMPLOYEE, hashMap);

                    AppUtils.printLog(activity, "DELETE_EMPLOYEE Response ", response.toString());

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
                        listEmployeeTarget.remove(pos);
                        employeeTargetListAdapter.notifyDataSetChanged();
                        if (listEmployeeTarget.size() > 0)
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
