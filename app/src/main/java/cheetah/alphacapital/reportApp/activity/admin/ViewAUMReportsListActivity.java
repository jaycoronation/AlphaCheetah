package cheetah.alphacapital.reportApp.activity.admin;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatTextView;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;

public class ViewAUMReportsListActivity extends BaseActivity
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
    AppCompatTextView  txtRetry;
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
    @BindView(R.id.rvAUMReports)
    RecyclerView rvAUMReports;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private EmployeeListAdapter employeeListAdapter;
    private String clientid = "";

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

        setContentView(R.layout.activity_view_aum_reports_type);

        ButterKnife.bind(this);

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
    }

    private void setupViews()
    {
        try
        {
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("View AUM Reports");
            tvNoDataText.setText("No Employee List Found.");
            llBackNavigation.setVisibility(View.VISIBLE);
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

                        if (listEmployee.size() > 0)
                        {
                            employeeListAdapter = new EmployeeListAdapter(listEmployee);
                            rvAUMReports.setAdapter(employeeListAdapter);
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
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_view_aum_reports_employee_list, viewGroup, false);
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

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(activity, AumEmployeeYearlySummeryActivity.class);
                        intent.putExtra("selectedEmployee", String.valueOf(getSet.getId()));
                        intent.putExtra("selectedEmployeeName", getSet.getFirst_name() + " " + getSet.getLast_name());
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
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvEmali)
            TextView tvEmali;

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
}
