package cheetah.alphacapital.reportApp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityTaskSummaryDetailsBinding;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.utils.AppUtils;

public class TaskSummaryDetailsActivity extends BaseActivity
{
    private ActivityTaskSummaryDetailsBinding binding;
    TaskReportResponseModel.DataItem getSet ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding  = DataBindingUtil.setContentView(activity,R.layout.activity_task_summary_details);
        initView();
        onClick();
    }

    private void initView()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        binding.toolbar.llNotification.setVisibility(View.GONE);
       /* setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);
        int height = 56;
        binding.toolbar.viewStatusBar.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        binding.toolbar.ivHeader.setLayoutParams(params);*/
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Detailed Task Summary");

        if (getIntent().hasExtra("data"))
        {
            getSet = new Gson().fromJson(getIntent().getStringExtra("data"), TaskReportResponseModel.DataItem.class);
        }
        setupData();
    }

    private void setupData()
    {
        binding.tvEmployeeNameCurrent.setText(getSet.getEmployee_name());
        binding.tvEmployeeNameLast.setText(getSet.getEmployee_name());
        binding.tvEmployeeNameBegining.setText(getSet.getEmployee_name());

        binding.tvTotalTaskCurrent.setText(String.valueOf(getSet.getCurrentMonth().getTotalTasks()).replace(".0",""));
        binding.tvTotalTaskLast.setText(String.valueOf(getSet.getLastMonth().getTotalTasks()).replace(".0",""));
        binding.tvTotalTaskBegining.setText(String.valueOf(getSet.getSinceBeginning().getTotalTasks()).replace(".0",""));

        binding.tvTaskCloseCurrent.setText(String.valueOf(getSet.getCurrentMonth().getTasksClosed()).replace(".0",""));
        binding.tvTaskCloseLast.setText(String.valueOf(getSet.getLastMonth().getTasksClosed()).replace(".0",""));
        binding.tvTaskCloseBegining.setText(String.valueOf(getSet.getSinceBeginning().getTasksClosed()).replace(".0",""));

        binding.tvAvgDaysCurrent.setText(String.valueOf(getSet.getCurrentMonth().getAvgDaysTaken()).replace(".0",""));
        binding.tvAvgDaysLast.setText(String.valueOf(getSet.getLastMonth().getAvgDaysTaken()).replace(".0",""));
        binding.tvAvgDaysBegining.setText(String.valueOf(getSet.getSinceBeginning().getAvgDaysTaken()).replace(".0",""));

        binding.tvOpenTaskCurrent.setText(String.valueOf(getSet.getCurrentMonth().getOpenTasks()).replace(".0",""));
        binding.tvOpenTaskLast.setText(String.valueOf(getSet.getLastMonth().getOpenTasks()).replace(".0",""));
        binding.tvOpenTaskBegining.setText(String.valueOf(getSet.getSinceBeginning().getOpenTasks()).replace(".0",""));

        binding.tvAvgOpenDaysCurrent.setText(String.valueOf(getSet.getCurrentMonth().getAvgOpenDays()).replace(".0",""));
        binding.tvAvgOpenDaysLast.setText(String.valueOf(getSet.getLastMonth().getAvgOpenDays()).replace(".0",""));
        binding.tvAvgOpenDays.setText(String.valueOf(getSet.getSinceBeginning().getAvgOpenDays()).replace(".0",""));
    }

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(v ->
        {
            try
            {
                AppUtils.hideKeyboard(binding.toolbar.emptyView, activity);
                activity.finish();
                //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }
}