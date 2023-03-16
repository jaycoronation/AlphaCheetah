package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
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
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.reportApp.activity.AddAUMActivity;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.AumEmployeeMonthlySummeryResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;

public class AumEmployeeMonthlySummeryActivity extends BaseActivity
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
    @BindView(R.id.tvMonthYearName)
    TextView tvMonthYearName;

    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;
    private ArrayList<AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listData = new ArrayList<AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();
    private AumEmployeeYearlySummeryAdapter aumEmployeeYearlySummeryAdapter;

    private String selectedYear = "", selectedMonth = "", selectedMonthName = "",selectedEmployee ="";

    private long Month_End_AUM = 0, sip = 0, Inflow_Outfolw = 0, Meetings_Existing = 0, Meetings_New = 0, References = 0, New_Clients_Convered = 0, Self_Acquired_AUM = 0, summery_mail = 0, day_forward_mail = 0, DAR = 0;

    public static Handler handler;

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

        setContentView(R.layout.activity_aum_employee_monthly_summery);

        ButterKnife.bind(this);

        selectedMonth = getIntent().getExtras().getString("selectedMonth");

        selectedMonthName = getIntent().getExtras().getString("selectedMonthName");

        selectedYear = getIntent().getExtras().getString("selectedYear");

        selectedEmployee  = getIntent().getExtras().getString("selectedEmployee");

        setupViews();

        onClickEvents();

        try
        {
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
        catch (Exception e)
        {
            e.printStackTrace();
        }

        handler = new Handler(msg -> {
            if (msg.what == 111)
            {
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
            return false;
        });


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
            //ivHeader.setImageResource(R.drawable.img_portfolio);
            tvMonthYearName.setText(selectedMonthName + " - " + selectedYear);
            /*int height = 56;
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
            linearLayoutManager = new LinearLayoutManager(activity);
            rvAUMReports.setLayoutManager(linearLayoutManager);
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


    }

    private void getDataFromApi()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AumEmployeeMonthlySummeryResponse> call = apiService.getAumMonthlySummeryByClient(selectedYear, selectedMonth, selectedEmployee);
        call.enqueue(new Callback<AumEmployeeMonthlySummeryResponse>()
        {
            @Override
            public void onResponse(Call<AumEmployeeMonthlySummeryResponse> call, Response<AumEmployeeMonthlySummeryResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listData = new ArrayList<AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean>();
                        listData.addAll(response.body().getData().getAumEmployeeYearlySummeryResult());
                        listData.sort((o1, o2) -> String.valueOf(o1.getClient_name()).compareTo(o2.getClient_name()));

                        if (listData.size() > 0)
                        {
                            for (int i = 0; i < listData.size(); i++)
                            {
                                Month_End_AUM = Month_End_AUM + listData.get(i).getMonth_End_AUM();
                                sip = sip + listData.get(i).getSip();
                                Inflow_Outfolw = Inflow_Outfolw + listData.get(i).getInflow_Outfolw();
                                Meetings_New = Meetings_New + listData.get(i).getMeetings_New();
                                Meetings_Existing = Meetings_Existing + listData.get(i).getMeetings_Existing();
                                References = References + listData.get(i).getReferences();
                                New_Clients_Convered = New_Clients_Convered + listData.get(i).getNew_Clients_Convered();
                                Self_Acquired_AUM = Self_Acquired_AUM + listData.get(i).getSelfAcquiredAUM();
                                summery_mail = summery_mail + listData.get(i).getSummery_mail();
                                day_forward_mail = day_forward_mail + listData.get(i).getDay_forward_mail();
                                DAR = DAR + listData.get(i).getDAR();
                            }

                            AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getset = new AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean();
                            getset.setMonth_End_AUM(Month_End_AUM);
                            getset.setInflow_Outfolw(Inflow_Outfolw);
                            getset.setMeetings_New(Meetings_New);
                            getset.setMeetings_Existing(Meetings_Existing);
                            getset.setSip(sip);
                            getset.setReferences(References);
                            getset.setSummery_mail(summery_mail);
                            getset.setDay_forward_mail(day_forward_mail);
                            getset.setNew_Clients_Convered(New_Clients_Convered);
                            getset.setSelf_Acquired_AUM(Self_Acquired_AUM);
                            getset.setClient_name("Total");
                            getset.setDAR(DAR);
                            listData.add(getset);

                            aumEmployeeYearlySummeryAdapter = new AumEmployeeYearlySummeryAdapter(listData);
                            rvAUMReports.setAdapter(aumEmployeeYearlySummeryAdapter);
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
                    }
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AumEmployeeMonthlySummeryResponse> call, Throwable t)
            {

                llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public class AumEmployeeYearlySummeryAdapter extends RecyclerView.Adapter<AumEmployeeYearlySummeryAdapter.ViewHolder>
    {
        ArrayList<AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listItems;

        public AumEmployeeYearlySummeryAdapter(ArrayList<AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_aum_employee_monthly_summery_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {

                if (position % 2 == 0)
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                }
                else
                {
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_blue));
                }

                final AumEmployeeMonthlySummeryResponse.DataBean.AumEmployeeYearlySummeryResultBean getSet = listItems.get(position);
                holder.tvMonthEndAum.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getMonth_End_AUM())));
                holder.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getInflow_Outfolw())));
                holder.tvMeetingNew.setText(String.valueOf(getSet.getMeetings_New()));
                holder.tvMeetingExisting.setText(String.valueOf(getSet.getMeetings_Existing()));
                holder.tvMeeting12Month.setText(String.valueOf(getSet.getLastYearTotalMeeting()));
                holder.tvMaxAum.setText(String.valueOf(getSet.getMax_month_aum()));
                holder.tvStartInvest.setText(String.valueOf(getSet.getStart_invest_year()));
                holder.tvLastYear.setText(String.valueOf(getSet.getLast_invest_year()));
                holder.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSip())));
                holder.tvClientReference.setText(String.valueOf(getSet.getReferences()));
                holder.tvSummaryMail.setText(String.valueOf(getSet.getSummery_mail()));
                holder.tvSelfAcquiredAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(getSet.getSelfAcquiredAUM())));
                holder.tvName.setText(getSet.getClient_name());
                holder.tvDAR.setText(String.valueOf(getSet.getDAR()));

                holder.ivEdit.setVisibility(View.VISIBLE);

                /*if (getSet.getEmployee_id().equals(sessionManager.getUserId()))
                {
                    holder.ivEdit.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivEdit.setVisibility(View.GONE);
                }*/

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            final AUMListGetSet.DataBean aumDataBean = new AUMListGetSet.DataBean();
                            aumDataBean.setId(getSet.getId());
                            aumDataBean.setEmployee_id(Integer.parseInt(getSet.getEmployee_id()));
                            aumDataBean.setClient_id(Integer.parseInt(getSet.getClient_id()));
                            aumDataBean.setNew_Meeting(getSet.getMeetings_New());
                            aumDataBean.setExisting_Meeting(getSet.getMeetings_Existing());
                            aumDataBean.setInflow_outflow(getSet.getInflow_Outfolw());
                            aumDataBean.setSIP(getSet.getSip());
                            aumDataBean.setClientReferences(getSet.getReferences());
                            aumDataBean.setMonth_End_AUM(getSet.getMonth_End_AUM());
                            aumDataBean.setAUM_Month(Integer.parseInt(selectedMonth));
                            aumDataBean.setAUM_Year(Integer.parseInt(selectedYear));
                            aumDataBean.setSummery_mail(getSet.getSummery_mail());
                            aumDataBean.setDay_forward_mail(getSet.getDay_forward_mail());
                            aumDataBean.setNewClientConverted(getSet.getNewClientConverted());
                            aumDataBean.setSelfAcquiredAUM(getSet.getSelfAcquiredAUM());
                            aumDataBean.setDAR(getSet.getDAR());

                            Intent intent = new Intent(activity, AddAUMActivity.class);
                            intent.putExtra("clientId", getSet.getClient_id());
                            intent.putExtra("isFor", "edit");
                            intent.putExtra("AUMListGetSet", (Parcelable) aumDataBean);
                            activity.startActivity(intent);
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
            @BindView(R.id.tvMonthEndAum)
            TextView tvMonthEndAum;
            @BindView(R.id.tvMeeting12Month)
            TextView tvMeeting12Month;
            @BindView(R.id.tvMaxAum)
            TextView tvMaxAum;
            @BindView(R.id.tvStartInvest)
            TextView tvStartInvest;
            @BindView(R.id.tvLastYear)
            TextView tvLastYear;
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
            @BindView(R.id.tvSelfAcquiredAUM)
            TextView tvSelfAcquiredAUM;
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvDAR)
            TextView tvDAR;
            @BindView(R.id.llMain)
            LinearLayout llMain;
            @BindView(R.id.ivEdit)
            ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
                tvName.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
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
