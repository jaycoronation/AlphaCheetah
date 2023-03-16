package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.databinding.ActivityEmployeeListForDarBinding;
import cheetah.alphacapital.reportApp.fragment.DARReportFragment;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.DARSummaryResponseModel;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;

public class ViewDailyActivityReportListActivity extends BaseActivity
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
    CustomTextViewSemiBold txtTitle;
    @BindView(R.id.edtSearch)
    CustomEditText edtSearch;
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
    AppCompatTextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    AppCompatTextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    CustomTextViewMedium tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.rvEmployeeList)
    RecyclerView rvEmployeeList;
    private boolean isStatusBarHidden = false;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<DARSummaryResponseModel.DataItem> listClientSearch = new ArrayList<DARSummaryResponseModel.DataItem>();
    private EmployeeListAdapter employeeListAdapter;
    private DataAdapter DARAdapter;
    public static Handler handler;

    ArrayList<DARSummaryResponseModel.DataItem> listDARData =new ArrayList<>();

    ActivityEmployeeListForDarBinding binding;

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

        binding = DataBindingUtil.setContentView(activity,R.layout.activity_employee_list_for_dar);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                //getAllEmployee();
                getDARSummary();
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
                            //getAllEmployee();
                            getDARSummary();
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
    private Timer timer = new Timer();
    private final long DELAY = 400;
    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("View Activity Reports");
            tvNoDataText.setText("No Employee List Found.");
            llBackNavigation.setVisibility(View.VISIBLE);
            binding.toolbar.cvCard.setVisibility(View.GONE);
            binding.toolbar.ivSerach.setVisibility(View.VISIBLE);

            /*ivHeader.setImageResource(R.drawable.img_portfolio);

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
            tvNoDataText.setText("No Employee Found.");
            linearLayoutManager = new LinearLayoutManager(activity);
            rvEmployeeList.setLayoutManager(linearLayoutManager);

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

        binding.toolbar.ivSerach.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                doSearch();
            }
        });

        binding.toolbar.ivClose.setOnClickListener(new View.OnClickListener()
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
                                            listClientSearch = new ArrayList<DARSummaryResponseModel.DataItem>();
                                            if (listDARData != null && listDARData.size() > 0)
                                            {
                                                for (int i = 0; i < listDARData.size(); i++)
                                                {
                                                    final String text = listDARData.get(i).getEmployee_name();

                                                    String text1 = AppUtils.getCapitalText(text);

                                                    String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                    if (text1.contains(cs1))
                                                    {
                                                        listClientSearch.add(listDARData.get(i));
                                                    }
                                                }

                                                if (listClientSearch.size() > 0)
                                                {
                                                    Log.e("<><> SEARCH Size: ", listClientSearch.size() + " END ");
                                                    llNoData.setVisibility(View.GONE);
                                                    DARAdapter = new DataAdapter(listClientSearch);
                                                    rvEmployeeList.setAdapter(DARAdapter);
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
                        if (listDARData.size() > 0)
                        {
                            DARAdapter = new DataAdapter(listDARData);
                            rvEmployeeList.setAdapter(DARAdapter);
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
        binding.toolbar.cvCard.setVisibility(View.GONE);
        ivSerach.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.GONE);
        //MitsUtils.hideKeyboard(activity);
        edtSearch.setText("");
    }

    private void doSearch()
    {
        edtSearch.requestFocus();
        llBackNavigation.setVisibility(View.GONE);
        txtTitle.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        edtSearch.setHint("Search Employee");
        binding.toolbar.cvCard.setVisibility(View.VISIBLE);
        ivSerach.setVisibility(View.GONE);
        ivClose.setVisibility(View.VISIBLE);
        //MitsUtils.openKeyboard(edtSearch, activity);
    }

    private void getDARSummary()
    {
        llLoading.setVisibility(View.VISIBLE);
        apiService.getDARSummaryAPI(Integer.valueOf(sessionManager.getUserId()), 0).enqueue(new Callback<DARSummaryResponseModel>()
        {
            @Override
            public void onResponse(Call<DARSummaryResponseModel> call, Response<DARSummaryResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().getData().size() > 0)
                        {
                            List<DARSummaryResponseModel.DataItem> listData = response.body().getData().getData();

                            listDARData.addAll(response.body().getData().getData());
                            listDARData.sort((o1, o2) -> String.valueOf(o1.getEmployee_name()).compareTo(o2.getEmployee_name()));
                            DARAdapter = new DataAdapter(listDARData);
                            rvEmployeeList.setAdapter(DARAdapter);

                            double currentTotalWorkingDays = 0.0;
                            double currentTotalWorkingHours = 0.0;

                            double currentTotalDays = 0.0;
                            double currentTotalHours = 0.0;

                            double lastTotalWorkingDays = 0.0;
                            double lastTotalWorkingHours = 0.0;

                            double lastTotalDays = 0.0;
                            double lastTotalHours = 0.0;

                            double pastTotalWorkingDays = 0.0;
                            double pastTotalWorkingHours = 0.0;

                            double pastTotalDays = 0.0;
                            double pastTotalHours = 0.0;

                            for (int i = 0; i < listData.size(); i++)
                            {
                                if (listData.get(i).getCurrentMonth().getWorkingDays() != 0)
                                {
                                    currentTotalWorkingDays = roundMyData(currentTotalWorkingDays + listData.get(i).getCurrentMonth().getWorkingDays(), 0);
                                    currentTotalDays = currentTotalDays + 1;
                                    listData.get(i).getCurrentMonth().setTotalWorkingDay(currentTotalWorkingDays);
                                    listData.get(i).getCurrentMonth().setTotalDay(listData.size());
                                }

                                if (listData.get(i).getCurrentMonth().getWorkingHours() != 0)
                                {
                                    currentTotalWorkingHours = currentTotalWorkingHours + listData.get(i).getCurrentMonth().getWorkingHours();
                                    currentTotalHours = currentTotalHours + 1;
                                    listData.get(i).getCurrentMonth().setTotalWorkingHour(currentTotalWorkingHours);
                                    listData.get(i).getCurrentMonth().setTotalHour(listData.size());
                                }

                                if (listData.get(i).getLastMonth().getWorkingDays() != 0)
                                {
                                    lastTotalWorkingDays = lastTotalWorkingDays + listData.get(i).getLastMonth().getWorkingDays();
                                    lastTotalDays = lastTotalDays + 1;
                                    listData.get(i).getLastMonth().setTotalWorkingDay(lastTotalWorkingDays);
                                    listData.get(i).getLastMonth().setTotalDay(listData.size());
                                }

                                if (listData.get(i).getLastMonth().getWorkingHours() != 0)
                                {
                                    lastTotalWorkingHours = lastTotalWorkingHours + listData.get(i).getLastMonth().getWorkingHours();
                                    lastTotalHours = lastTotalHours + 1;
                                    listData.get(i).getLastMonth().setTotalWorkingHour(lastTotalWorkingHours);
                                    listData.get(i).getLastMonth().setTotalHour(listData.size());
                                }

                                if (listData.get(i).getPastYearMonthDAR().getWorkingDays() != 0)
                                {
                                    pastTotalWorkingDays = pastTotalWorkingDays + listData.get(i).getPastYearMonthDAR().getWorkingDays();
                                    pastTotalDays = pastTotalDays + 1;
                                    listData.get(i).getPastYearMonthDAR().setTotalWorkingDay(pastTotalWorkingDays);
                                    listData.get(i).getPastYearMonthDAR().setTotalDay(listData.size());
                                }

                                if (listData.get(i).getPastYearMonthDAR().getWorkingHours() != 0)
                                {
                                    pastTotalWorkingHours = pastTotalWorkingHours + listData.get(i).getPastYearMonthDAR().getWorkingHours();
                                    pastTotalHours = pastTotalHours + 1;
                                    listData.get(i).getPastYearMonthDAR().setTotalWorkingHour(pastTotalWorkingHours);
                                    listData.get(i).getPastYearMonthDAR().setTotalHour(listData.size());
                                }
                            }

                            response.body().getData().getAverage().getCurrentMonthDarAverage().setWorkingDayAverage(roundMyData(currentTotalWorkingDays / currentTotalDays,2));
                            response.body().getData().getAverage().getCurrentMonthDarAverage().setWorkingHourAverage(roundMyData(currentTotalWorkingHours / currentTotalHours,2));

                            response.body().getData().getAverage().getLastMonthDarAverage().setWorkingDayAverage(roundMyData(lastTotalWorkingDays / lastTotalDays, 2));
                            response.body().getData().getAverage().getLastMonthDarAverage().setWorkingHourAverage(roundMyData(lastTotalWorkingHours / lastTotalHours, 2));

                            response.body().getData().getAverage().getPastYearMonthDARAverage().setWorkingDayAverage(roundMyData(pastTotalWorkingDays / pastTotalDays, 2));
                            response.body().getData().getAverage().getPastYearMonthDARAverage().setWorkingHourAverage(roundMyData(pastTotalWorkingHours / pastTotalHours, 2));

                            DARSummaryResponseModel.Average getSet = response.body().getData().getAverage();
                            binding.tvCurrentWorkingDay.setText(String.valueOf(getSet.getCurrentMonthDarAverage().getWorkingDayAverage()));
                            binding.tvCurrentWorkingHour.setText(String.valueOf(getSet.getCurrentMonthDarAverage().getWorkingHourAverage()));

                            binding.tvLastWorkingDay.setText(String.valueOf(getSet.getLastMonthDarAverage().getWorkingDayAverage()));
                            binding.tvLastWorkingHour.setText(String.valueOf(getSet.getLastMonthDarAverage().getWorkingHourAverage()));

                            binding.tvLast12WorkingDay.setText(String.valueOf(getSet.getPastYearMonthDARAverage().getWorkingDayAverage()));
                            binding.tvLast12WorkingHour.setText(String.valueOf(getSet.getPastYearMonthDARAverage().getWorkingHourAverage()));
                        }
                        else
                        {
                            showToast("Something went wrong!");
                        }
                    }
                }
                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DARSummaryResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
            }
        });
    }

    void sortEmployee()
    {
        try {
            Collections.sort(listDARData, (item, t1) -> {
                String s1 = item.getEmployee_name();
                String s2 = t1.getEmployee_name();
                return s1.compareToIgnoreCase(s2);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(DARAdapter!=null)
        {
            DARAdapter.notifyDataSetChanged();
        }
    }


    private void getAllEmployee()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<AllEmployeeResponse> call = apiService.getAllEmployee("", "0", "0");
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

                        if (listEmployee.size() > 0)
                        {
                            employeeListAdapter = new EmployeeListAdapter(listEmployee);
                            rvEmployeeList.setAdapter(employeeListAdapter);
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
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {

                llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
                llNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder>
    {
        ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listItems;


        public EmployeeListAdapter(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_view_dar_employee_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listItems.get(position);
                holder.tvName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));
                holder.tvEmali.setText(getSet.getEmail());

                if (getSet.getToday_activity_count() > 0)
                {
                    holder.tvToday.setText("Yes");
                }
                else
                {
                    holder.tvToday.setText("No");
                }

                if (getSet.getYesterday_activity_count() > 0)
                {
                    holder.tvYesterday.setText("Yes");
                }
                else
                {
                    holder.tvYesterday.setText("No");
                }

                if (getSet.getDay_before_yesterday_activity_count() > 0)
                {
                    holder.tvDayBeforeYesterday.setText("Yes");
                }
                else
                {
                    holder.tvDayBeforeYesterday.setText("No");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, DailyActivityReportsDetailsActivity.class);
                            intent.putExtra("EmployeeGetSet", (Parcelable) getSet);
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
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvEmali)
            TextView tvEmali;
            @BindView(R.id.tvToday)
            TextView tvToday;
            @BindView(R.id.tvYesterday)
            TextView tvYesterday;
            @BindView(R.id.tvDayBeforeYesterday)
            TextView tvDayBeforeYesterday;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
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

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
    {
        ArrayList<DARSummaryResponseModel.DataItem> listData =new ArrayList<>();
        public DataAdapter(ArrayList<DARSummaryResponseModel.DataItem> listDARData)
        {
            this.listData = listDARData;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_dar_list_for_report, parent, false);
            return new DataAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            DARSummaryResponseModel.DataItem getSet = listData.get(position);
            holder.tvCurrentWorkingDay.setText(String.valueOf(roundMyData(getSet.getCurrentMonth().getWorkingDays(),2)));
            holder.tvCurrentWorkingHour.setText(String.valueOf(roundMyData(getSet.getCurrentMonth().getTotalWorkingHour(),2)));
            holder.tvLastWorkingDay.setText(String.valueOf(roundMyData(getSet.getLastMonth().getWorkingDays(),2)));
            holder.tvLastWorkingHour.setText(String.valueOf(roundMyData(getSet.getLastMonth().getTotalWorkingHour(),2)));
            holder.tvLast12WorkingDay.setText(String.valueOf(roundMyData(getSet.getPastYearMonthDAR().getWorkingDays(),2)));
            holder.tvLast12WorkingHour.setText(String.valueOf(roundMyData(getSet.getPastYearMonthDAR().getTotalWorkingHour(),2)));
            holder.tvEmployee.setText(String.valueOf(getSet.getEmployee_name()));


            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        Gson gson = new Gson();

                        Intent intent = new Intent(activity, DailyActivityReportsDetailsActivity.class);
                        //intent.putExtra("EmployeeGetSet", (Parcelable) getSet);
                        intent.putExtra("id",getSet.getEmployee_id());
                        activity.startActivity(intent);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvCurrentWorkingDay,tvCurrentWorkingHour,tvLastWorkingDay,tvLastWorkingHour,tvLast12WorkingDay,tvLast12WorkingHour,tvEmployee;
            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvEmployee = itemView.findViewById(R.id.tvEmployee);
                tvCurrentWorkingDay = itemView.findViewById(R.id.tvCurrentWorkingDay);
                tvCurrentWorkingHour = itemView.findViewById(R.id.tvCurrentWorkingHour);
                tvLastWorkingDay = itemView.findViewById(R.id.tvLastWorkingDay);
                tvLastWorkingHour = itemView.findViewById(R.id.tvLastWorkingHour);
                tvLast12WorkingDay = itemView.findViewById(R.id.tvLast12WorkingDay);
                tvLast12WorkingHour = itemView.findViewById(R.id.tvLast12WorkingHour);
            }
        }
    }
}
