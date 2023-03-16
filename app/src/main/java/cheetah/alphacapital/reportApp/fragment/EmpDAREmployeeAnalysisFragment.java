package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpDaremployeeAnalysisBinding;
import cheetah.alphacapital.databinding.RowviewAnalysisBinding;
import cheetah.alphacapital.reportApp.getset.DARFilterResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpDAREmployeeAnalysisFragment extends Fragment {

    private Activity activity;
    private SessionManager sessionManager;
    private FragmentEmpDaremployeeAnalysisBinding binding;
    private ApiInterface apiService;

    private int employeeId = 0,currentMonth = 0,currentYear = 0;
    private List<DARFilterResponse.DataBean.EmployeeWiseBean> listReport = new ArrayList<>();
    private ReportAdapter reportAdapter;

    public static Handler handler;

    @SuppressLint("ValidFragment")
    public EmpDAREmployeeAnalysisFragment(int employeeId, int currentMonth, int currentYear) {
        this.employeeId = employeeId;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_emp_daremployee_analysis, container, false);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        initViews();

        getReportData();

        handler = new Handler(message -> {
            if(message.what==1)
            {
                if(message.arg1!=0)
                {
                    currentMonth = message.arg1;
                }
                if(message.arg2!=0)
                {
                    currentYear = message.arg2;
                }
                getReportData();
            }
            return false;
        });

        return binding.getRoot();
    }

    private void initViews() {
        binding.noData.tvNoDataText.setText("Sorry, We could't find Employee Analysis Report data.");
        binding.rvReport.setLayoutManager(new LinearLayoutManager(activity));
        binding.llSelectSort.setOnClickListener(view -> showSortingDialog());
    }

    void getReportData()
    {
        if(sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getDARFilters("",
                    "",
                    String.valueOf(currentMonth),
                    String.valueOf(currentYear),
                    "",
                    String.valueOf(employeeId))
                    .enqueue(new Callback<DARFilterResponse>() {
                        @Override
                        public void onResponse(Call<DARFilterResponse> call, Response<DARFilterResponse> response) {
                            if(response.isSuccessful())
                            {
                                if(response.body().isSuccess())
                                {
                                    listReport = response.body().getData().getEmployeeWise();

                                    if(listReport.size()>0)
                                    {
                                        binding.noData.llNoData.setVisibility(View.GONE);
                                        binding.llTopSection.setVisibility(View.VISIBLE);
                                        setAdapter();
                                    }
                                    else
                                    {
                                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                                    }
                                }
                                else
                                {
                                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                            else
                            {
                                binding.noData.llNoData.setVisibility(View.VISIBLE);
                            }

                            binding.loading.llLoading.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<DARFilterResponse> call, Throwable t) {
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                            binding.loading.llLoading.setVisibility(View.GONE);
                        }
                    });
        }
    }

    void setAdapter()
    {
        reportAdapter = new ReportAdapter();
        binding.rvReport.setAdapter(reportAdapter);

        calculateTotalHoursAndMinutes();
    }

    void calculateTotalHoursAndMinutes()
    {
        if(listReport.size()>0)
        {
            binding.llBottomSummary.setVisibility(View.VISIBLE);
            binding.tvTotalPercentage.setText(sumPercentage() +" %");
            try
            {

                binding.tvTotalTimespent.setText(sumHours() + " Hours");
                binding.tvTotalTimespent.setVisibility(View.VISIBLE);

            }
            catch (Exception e)
            {
                binding.tvTotalTimespent.setVisibility(View.GONE);
                e.printStackTrace();
            }


        }
    }

    public double sumHours() {
        double sum = 0;
        for (DARFilterResponse.DataBean.EmployeeWiseBean bean: listReport)
        {
            sum = sum + bean.getTimeSpent_total();

        }
        DecimalFormat df2 = new DecimalFormat("#.##");
        return Double.parseDouble(df2.format(sum));
    }

    public double sumPercentage() {
        double sum = 0;
        for (DARFilterResponse.DataBean.EmployeeWiseBean bean: listReport)
        {
            sum = sum + bean.getPercentage();
        }
        DecimalFormat df2 = new DecimalFormat("#.##");
        return Double.parseDouble(df2.format(sum));
    }

    private class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.rowview_analysis,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            DARFilterResponse.DataBean.EmployeeWiseBean bean = listReport.get(position);
            holder.binding.tvTitle.setText(bean.getName());
            holder.binding.tvTotalTimespent.setText(String.valueOf(bean.getTimeSpent_total()) + " Hours");
            holder.binding.tvPercentageTimespent.setText(String.valueOf(bean.getPercentage()) + " %");
        }

        @Override
        public int getItemCount() {
            return listReport.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewAnalysisBinding binding;
            public ViewHolder(View itemView) {
                super(itemView);

                binding = DataBindingUtil.bind(itemView);
            }
        }
    }

    public void showSortingDialog() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_sort_dar, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);

        TextView tvEmployee, tvTotalTimespent, tvPercentageTimespent;
        tvEmployee = sheetView.findViewById(R.id.tvEmployee);
        tvTotalTimespent = sheetView.findViewById(R.id.tvTotalTimespent);
        tvPercentageTimespent = sheetView.findViewById(R.id.tvPercentageTimespent);

        tvEmployee.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            sortEmployee();
        });

        tvTotalTimespent.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
            sortTotal();
        });

        tvPercentageTimespent.setOnClickListener(view ->{
            bottomSheetDialog.dismiss();
            sortPercent();
        });

        bottomSheetDialog.show();
    }

    boolean isEmpSorted = true;
    void sortEmployee()
    {
        try {
            Collections.sort(listReport, (item, t1) -> {
                String s1 = item.getName();
                String s2 = t1.getName();
                return isEmpSorted ? s1.compareToIgnoreCase(s2) : s2.compareToIgnoreCase(s1);
            });

            isEmpSorted = !isEmpSorted;

            binding.tvSort.setText("Employee");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(reportAdapter!=null)
        {
            reportAdapter.notifyDataSetChanged();
        }
    }

    boolean isTotalSorted = true;
    void sortTotal()
    {
        try {
            Collections.sort(listReport, (item, t1) -> {
                double s1 = item.getTimeSpent_total();
                double s2 = t1.getTimeSpent_total();
                return (int) (isTotalSorted ? s1-s2 : s2-s1);
            });

            isTotalSorted = !isTotalSorted;

            binding.tvSort.setText("Total Timespent");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(reportAdapter!=null)
        {
            reportAdapter.notifyDataSetChanged();
        }
    }

    boolean isPercentSorted = true;
    void sortPercent()
    {
        try {
            Collections.sort(listReport, (item, t1) -> {
                double s1 = item.getPercentage();
                double s2 = t1.getPercentage();
                return (int) (isPercentSorted ? s1-s2 : s2-s1);
            });

            isPercentSorted = !isPercentSorted;

            binding.tvSort.setText("Percentage Timespent");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(reportAdapter!=null)
        {
            reportAdapter.notifyDataSetChanged();
        }
    }
}
