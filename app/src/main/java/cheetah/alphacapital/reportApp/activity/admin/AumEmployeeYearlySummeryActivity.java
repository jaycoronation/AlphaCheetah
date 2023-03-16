package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.AumEmployeeYearlySummeryResponse;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;

public class AumEmployeeYearlySummeryActivity extends BaseActivity
{
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
    EditText edtSearch;
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
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.rvAUMReports)
    RecyclerView rvAUMReports;
    @BindView(R.id.rvAUMReportsTotal)
    RecyclerView rvAUMReportsTotal;

    @BindView(R.id.tvEmployee)
    TextView tvEmployee;
    @BindView(R.id.llSelectEmployee)
    LinearLayout llSelectEmployee;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.llSelectMonth)
    LinearLayout llSelectMonth;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.llSelectYear)
    LinearLayout llSelectYear;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;
    private ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listData = new ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();
    private ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listTotalSummery = new ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();
    private AumEmployeeYearlySummeryAdapter aumEmployeeYearlySummeryAdapter;
    private AumEmployeeYearlySummeryTotalAdapter employeeYearlySummeryTotalAdapter;
    private String clientid = "";

    private String selectedEmployee = "", selectedEmployeeName = "", selectedYear = "", selectedMonth = "", selectedMonthName = "";
    private ArrayList<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
    private BottomSheetDialog listDialog;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private final String EMPLOYEE = "Employee";

    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

    private boolean isForEmployeeAUM = false;

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

        setContentView(R.layout.activity_aum_employee_yearly_summery);

        ButterKnife.bind(this);

        if(getIntent().hasExtra("isForEmployeeAUM"))
        {
            isForEmployeeAUM = getIntent().getBooleanExtra("isForEmployeeAUM",false);
        }

        selectedEmployee = getIntent().getExtras().getString("selectedEmployee");

        selectedEmployeeName = getIntent().getExtras().getString("selectedEmployeeName");

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getAllEmployee();
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

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("AUM summary");
            tvNoDataText.setText("No AUM summary List Found.");
            llBackNavigation.setVisibility(View.VISIBLE);

            linearLayoutManager = new LinearLayoutManager(activity);
            rvAUMReports.setLayoutManager(linearLayoutManager);
            rvAUMReportsTotal.setLayoutManager(new LinearLayoutManager(activity));
            selectedMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));

            tvYear.setText(selectedYear);
            tvEmployee.setText(selectedEmployeeName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        llBackNavigation.setOnClickListener(v ->
        {
            try
            {
                AppUtils.hideKeyboard(toolbar, activity);
                activity.finish();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llSelectMonth.setOnClickListener(v -> showListDialog(MONTH));

        llSelectYear.setOnClickListener(v -> showListDialog(YEAR));

        llSelectEmployee.setOnClickListener(v ->
        {
            if(!isForEmployeeAUM)
            {
                showListDialog(EMPLOYEE);
            }
        });
    }

    private void getAllEmployee()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AllEmployeeResponse> call = apiService.getAllEmployee(clientid, "0", "0");
        call.enqueue(new Callback<AllEmployeeResponse>()
        {
            @Override
            public void onResponse(Call<AllEmployeeResponse> call, Response<AllEmployeeResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
                        listEmployee.addAll(response.body().getData().getAllEmployee());
                        getMonthYearData();
                    }
                }
                else
                {
                    getMonthYearData();
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {
                getMonthYearData();
                AppUtils.apiFailedSnackBar(activity);

            }
        });
    }

    private void getMonthYearData()
    {
        Call<MonthYearResponse> call = apiService.getMonthYear();
        call.enqueue(new Callback<MonthYearResponse>()
        {
            @Override
            public void onResponse(Call<MonthYearResponse> call, Response<MonthYearResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().getYear().size() > 0)
                        {
                            listYear = new ArrayList<String>();

                            for (int i = 0; i < response.body().getData().getYear().size(); i++)
                            {
                                listYear.add(response.body().getData().getYear().get(i).toString().trim());
                                if (response.body().getData().getYear().get(i).equals(selectedYear))
                                {
                                    tvYear.setText(listYear.get(i).toString());
                                    break;
                                }
                            }
                        }

                        if (response.body().getData().getMonth().size() > 0)
                        {
                            listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
                            listMonth.addAll(response.body().getData().getMonth());
                        }
                    }
                    else
                    {
                        apiFailedSnackBar();
                    }
                }
                else
                {
                    apiFailedSnackBar();
                }

                getDataFromApi();
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                apiFailedSnackBar();
                getDataFromApi();
            }
        });
    }

    private void getDataFromApi()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AumEmployeeYearlySummeryResponse> call = apiService.getAumEmployeeYearlySummery(selectedYear, selectedEmployee);
        call.enqueue(new Callback<AumEmployeeYearlySummeryResponse>()
        {
            @Override
            public void onResponse(Call<AumEmployeeYearlySummeryResponse> call, Response<AumEmployeeYearlySummeryResponse> response)
            {
                if (response.isSuccessful())
                {
                    Log.e("<><> In Outer If "," Done");

                    Log.e("<><> In Outer If ",response.body().isSuccess() + "");
                    if (response.body().isSuccess())
                    {
                        Log.e("<><> In Inner If ",response.body().isSuccess() + "");
                        listData = new ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();

                        Log.e("<><> In Inner If "," Done");
                        listData.addAll(response.body().getData().getAumEmployeeYearlySummeryResult());

                        if (listData.size() > 0)
                        {
                            aumEmployeeYearlySummeryAdapter = new AumEmployeeYearlySummeryAdapter(listData);
                            rvAUMReports.setAdapter(aumEmployeeYearlySummeryAdapter);
                            llNoData.setVisibility(View.GONE);

                            listTotalSummery = new ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();

                            if (response.body().getData().getSummery().getTotal() != null)
                            {
                                AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getset = new AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                getset.setMonth_End_AUM(response.body().getData().getSummery().getTotal().getMonthEndAUM());
                                getset.setInflow_Outfolw(response.body().getData().getSummery().getTotal().getInflow_Outfolw());
                                getset.setMeetings_New(response.body().getData().getSummery().getTotal().getMeetings_New());
                                getset.setMeetings_Existing(response.body().getData().getSummery().getTotal().getMeetings_Existing());
                                getset.setSip(response.body().getData().getSummery().getTotal().getSIP());
                                getset.setReferences(response.body().getData().getSummery().getTotal().getReferences());
                                getset.setSummery_mail(response.body().getData().getSummery().getTotal().getSummery_mail());
                                getset.setDay_forward_mail(response.body().getData().getSummery().getTotal().getDay_forward_mail());
                                getset.setNew_Clients_Convered(response.body().getData().getSummery().getTotal().getNew_Clients_Convered());
                                getset.setSelf_Acquired_AUM(response.body().getData().getSummery().getTotal().getSelf_Acquired_AUM());
                                getset.setFull_month("Total");
                                getset.setDAR(response.body().getData().getSummery().getTotal().getDAR());
                                listTotalSummery.add(getset);
                            }

                            if (response.body().getData().getSummery().getAverage() != null)
                            {
                                AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getset = new AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                getset.setMonth_End_AUM(response.body().getData().getSummery().getAverage().getMonthEndAUM());
                                getset.setInflow_Outfolw(response.body().getData().getSummery().getAverage().getInflow_Outfolw());
                                getset.setMeetings_New(response.body().getData().getSummery().getAverage().getMeetings_New());
                                getset.setMeetings_Existing(response.body().getData().getSummery().getAverage().getMeetings_Existing());
                                getset.setSip(response.body().getData().getSummery().getAverage().getSIP());
                                getset.setReferences(response.body().getData().getSummery().getAverage().getReferences());
                                getset.setSummery_mail(response.body().getData().getSummery().getAverage().getSummery_mail());
                                getset.setDay_forward_mail(response.body().getData().getSummery().getAverage().getDay_forward_mail());
                                getset.setNew_Clients_Convered(response.body().getData().getSummery().getAverage().getNew_Clients_Convered());
                                getset.setSelf_Acquired_AUM(response.body().getData().getSummery().getAverage().getSelf_Acquired_AUM());
                                getset.setFull_month("Average");
                                getset.setDAR(response.body().getData().getSummery().getAverage().getDAR());
                                listTotalSummery.add(getset);
                            }

                            if (response.body().getData().getSummery().getTarget() != null)
                            {
                                AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getset = new AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                getset.setMonth_End_AUM(response.body().getData().getSummery().getTarget().getMonthEndAUM());
                                getset.setInflow_Outfolw(response.body().getData().getSummery().getTarget().getInflow_Outfolw());
                                getset.setMeetings_New(response.body().getData().getSummery().getTarget().getMeetings_New());
                                getset.setMeetings_Existing(response.body().getData().getSummery().getTarget().getMeetings_Existing());
                                getset.setSip(response.body().getData().getSummery().getTarget().getSIP());
                                getset.setReferences(response.body().getData().getSummery().getTarget().getReferences());
                                getset.setSummery_mail(response.body().getData().getSummery().getTarget().getSummery_mail());
                                getset.setDay_forward_mail(response.body().getData().getSummery().getTarget().getDay_forward_mail());
                                getset.setNew_Clients_Convered(response.body().getData().getSummery().getTarget().getNew_Clients_Convered());
                                getset.setSelf_Acquired_AUM(response.body().getData().getSummery().getTarget().getSelf_Acquired_AUM());
                                getset.setFull_month("Target");
                                getset.setDAR(-1);
                                listTotalSummery.add(getset);
                            }

                            if (response.body().getData().getSummery().getVariance() != null)
                            {
                                AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getset = new AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                                getset.setMonth_End_AUM(response.body().getData().getSummery().getVariance().getMonthEndAUM());
                                getset.setInflow_Outfolw(response.body().getData().getSummery().getVariance().getInflow_Outfolw());
                                getset.setMeetings_New(response.body().getData().getSummery().getVariance().getMeetings_New());
                                getset.setMeetings_Existing(response.body().getData().getSummery().getVariance().getMeetings_Existing());
                                getset.setSip(response.body().getData().getSummery().getVariance().getSIP());
                                getset.setReferences(response.body().getData().getSummery().getVariance().getReferences());
                                getset.setSummery_mail(response.body().getData().getSummery().getVariance().getSummery_mail());
                                getset.setDay_forward_mail(response.body().getData().getSummery().getVariance().getDay_forward_mail());
                                getset.setNew_Clients_Convered(response.body().getData().getSummery().getVariance().getNew_Clients_Convered());
                                getset.setSelf_Acquired_AUM(response.body().getData().getSummery().getVariance().getSelf_Acquired_AUM());
                                getset.setFull_month("Variance");
                                getset.setDAR(-1);
                                listTotalSummery.add(getset);
                            }

                            if(listTotalSummery.size() >0)
                            {
                                employeeYearlySummeryTotalAdapter = new AumEmployeeYearlySummeryTotalAdapter(listTotalSummery);
                                rvAUMReportsTotal.setAdapter(employeeYearlySummeryTotalAdapter);
                            }
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        Log.e("<><> In Inner else "," Done");
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    Log.e("<><> In Outer else "," Done");
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AumEmployeeYearlySummeryResponse> call, Throwable t)
            {

                llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public class AumEmployeeYearlySummeryAdapter extends RecyclerView.Adapter<AumEmployeeYearlySummeryAdapter.ViewHolder>
    {
        ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listItems;

        public AumEmployeeYearlySummeryAdapter(ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_employee_yearly_summery_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {

                if(position %2 ==0)
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
                }
                else
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.light_blue));
                }


                final AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getSet = listItems.get(position);
                holder.tvMonthEndAum.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMonth_End_AUM())));
                holder.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getInflow_Outfolw())));
                holder.tvMeetingNew.setText(String.valueOf(getSet.getMeetings_New()));
                holder.tvMeetingExisting.setText(String.valueOf(getSet.getMeetings_Existing()));
                holder.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSip())));
                holder.tvClientReference.setText(String.valueOf(getSet.getReferences()));
                holder.tvSummaryMail.setText(String.valueOf(getSet.getSummery_mail()));
                holder.tvDayForwardMail.setText(String.valueOf(getSet.getDay_forward_mail()));
                holder.tvNewClientConverted.setText(String.valueOf(getSet.getNew_Clients_Convered()));
                holder.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSelf_Acquired_AUM())));
                holder.tvMonth.setText(getSet.getFull_month());
                holder.tvDAR.setText(String.valueOf(getSet.getDAR()));

                holder.llMain.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(activity, AumEmployeeMonthlySummeryActivity.class);
                        intent.putExtra("selectedEmployee", selectedEmployee);
                        intent.putExtra("selectedMonth", String.valueOf(getSet.getMonth()));
                        intent.putExtra("selectedMonthName", getSet.getFull_month());
                        intent.putExtra("selectedYear",selectedYear);
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
            @BindView(R.id.tvMonthEndAum)
            TextView tvMonthEndAum;
            @BindView(R.id.tvInflowOutflow)
            TextView tvInflowOutflow;
            @BindView(R.id.tvMeeting_New)
            TextView tvMeetingNew;
            @BindView(R.id.tvMeeting_Existing)
            TextView tvMeetingExisting;
            @BindView(R.id.tvSip)
            TextView tvSip;
            @BindView(R.id.tvClientReference)
            TextView tvClientReference;
            @BindView(R.id.tvSummaryMail)
            TextView tvSummaryMail;
            @BindView(R.id.tvDayForwardMail)
            TextView tvDayForwardMail;
            @BindView(R.id.tvNewClientConverted)
            TextView tvNewClientConverted;
            @BindView(R.id.tvSelfAcquiredAUM)
            TextView tvSelfAcquiredAUM;
            @BindView(R.id.tvMonth)
            TextView tvMonth;
            @BindView(R.id.tvDAR)
            TextView tvDAR;
            @BindView(R.id.llMain)
            LinearLayout llMain;


            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
                tvMonth.setTextColor(ContextCompat.getColor(activity,R.color.blue_new));
            }
        }
    }


    public class AumEmployeeYearlySummeryTotalAdapter extends RecyclerView.Adapter<AumEmployeeYearlySummeryTotalAdapter.ViewHolder>
    {
        ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listItems;

        public AumEmployeeYearlySummeryTotalAdapter(ArrayList<AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_employee_yearly_summery_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {

                if(position %2 ==0)
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
                }
                else
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity,R.color.light_blue));
                }

                final AumEmployeeYearlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getSet = listItems.get(position);
                holder.tvMonthEndAum.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMonth_End_AUM()))));
                holder.tvInflowOutflow.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getInflow_Outfolw()))));
                holder.tvMeetingNew.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMeetings_New()))));
                holder.tvMeetingExisting.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMeetings_Existing()))));
                holder.tvSip.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSip()))));
                holder.tvClientReference.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getReferences()))));
                holder.tvSummaryMail.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSummery_mail()))));
                holder.tvDayForwardMail.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getDay_forward_mail()))));
                holder.tvNewClientConverted.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getNew_Clients_Convered()))));
                holder.tvSelfAcquiredAUM.setText(String.valueOf(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSelf_Acquired_AUM()))));
                holder.tvMonth.setText(getSet.getFull_month());

                if(getSet.getDAR() >0)
                {
                    holder.tvDAR.setText(String.valueOf(getSet.getDAR()));
                }
                else
                {
                    holder.tvDAR.setText("-");
                }
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
            @BindView(R.id.tvMonthEndAum)
            TextView tvMonthEndAum;
            @BindView(R.id.tvInflowOutflow)
            TextView tvInflowOutflow;
            @BindView(R.id.tvMeeting_New)
            TextView tvMeetingNew;
            @BindView(R.id.tvMeeting_Existing)
            TextView tvMeetingExisting;
            @BindView(R.id.tvSip)
            TextView tvSip;
            @BindView(R.id.tvClientReference)
            TextView tvClientReference;
            @BindView(R.id.tvSummaryMail)
            TextView tvSummaryMail;
            @BindView(R.id.tvDayForwardMail)
            TextView tvDayForwardMail;
            @BindView(R.id.tvNewClientConverted)
            TextView tvNewClientConverted;
            @BindView(R.id.tvSelfAcquiredAUM)
            TextView tvSelfAcquiredAUM;
            @BindView(R.id.tvMonth)
            TextView tvMonth;
            @BindView(R.id.tvDAR)
            TextView tvDAR;

            @BindView(R.id.llMain)
            LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
                tvMonth.setTextColor(ContextCompat.getColor(activity,R.color.black));
                tvMonth.setTypeface(AppUtils.getTypefaceBold(activity));
            }
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
                final MonthYearResponse.DataBean.MonthBean getSet = listMonth.get(position);
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
                                getDataFromApi();
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
                                    getDataFromApi();
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
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                final AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listEmployee.get(position);
                holder.tvValue.setText(getSet.getFirst_name() + " " + getSet.getLast_name());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedEmployee.equalsIgnoreCase(String.valueOf(getSet.getId())))
                        {
                            selectedEmployee = String.valueOf(getSet.getId());
                            selectedEmployeeName = getSet.getFirst_name() + " " + getSet.getLast_name();
                            tvEmployee.setText(selectedEmployeeName);
                            getDataFromApi();
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
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
            }
            else
            {
                return listEmployee.size();
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
