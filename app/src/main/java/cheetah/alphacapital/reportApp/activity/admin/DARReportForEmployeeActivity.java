package cheetah.alphacapital.reportApp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityDarreportForEmployeeBinding;
import cheetah.alphacapital.databinding.RowviewEmpDarReportBinding;
import cheetah.alphacapital.reportApp.getset.EmpDARReportListResponse;

public class DARReportForEmployeeActivity extends BaseActivity {
    private ActivityDarreportForEmployeeBinding binding;

    private String YEAR = "Year";
    private String selectedYear = "";

    private List<String> listYear = new ArrayList<>();
    private List<EmpDARReportListResponse.DataBean> listReport = new ArrayList<>();

    private BottomSheetDialog listDialog;

    private boolean isStatusBarHidden = false;
    private int employeeId = 0;

    public static Handler handler;

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
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_darreport_for_employee);
        employeeId = getIntent().getIntExtra("employeeId", 0);
        initViews();
        getYearData();

        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg)
            {

                if (msg.what == 222)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getData();
                    }
                    else
                    {
                        noInternetSnackBar();
                    }
                }
            }
        };
    }

    private void initViews()
    {

/*
        int height = 56;
        if (isStatusBarHidden)
        {
            height = 56 + 25;
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
        }
        else
        {
            binding.toolbar.toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        binding.toolbar.ivHeader.setLayoutParams(params);
        binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);*/

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("DAR Report");
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);

        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                finish();
                finishActivityAnimation();
            }
        });


        binding.noData.tvNoDataText.setText("Sorry, We could not find DAR Report.");
        binding.ivAddDAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              /*  Intent intent = new Intent(activity, AddDARNewActivity.class);
                activity.startActivity(intent);
                AppUtils.startActivityAnimation(activity);*/
                Intent intent = new Intent(activity, AddDarEntryActivity.class);
                intent.putExtra("isFor","add");
                intent.putExtra("isEditClient",true);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            }
        });
        binding.llSelectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showListDialog(YEAR);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    void getYearData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.noInternet.llNoInternet.setVisibility(View.GONE);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();

                    binding.loading.llLoading.setVisibility(View.VISIBLE);
                }

                @Override
                protected Void doInBackground(Void... voids)
                {
                    getMonthYearData();
                    return null;
                }

                private void getMonthYearData()
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
                                    listYear.clear();

                                    for (int i = 0; i < yearArray.length(); i++)
                                    {
                                        if (Integer.parseInt(yearArray.getString(i)) <= Calendar.getInstance().get(Calendar.YEAR))
                                        {
                                            listYear.add(yearArray.getString(i));
                                        }
                                    }

                                    if (listYear.size() > 0)
                                    {
                                        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
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

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);
                    if (listYear.size() > 0)
                    {
                        binding.tvYear.setText(selectedYear);
                    }
                    binding.loading.llLoading.setVisibility(View.GONE);
                    getData();
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void getData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getAUMByEmployee(String.valueOf(employeeId), selectedYear)
                    .enqueue(new Callback<EmpDARReportListResponse>() {
                        @Override
                        public void onResponse(Call<EmpDARReportListResponse> call, Response<EmpDARReportListResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if (response.body().isSuccess())
                                {
                                    binding.noData.llNoData.setVisibility(View.GONE);
                                    listReport = response.body().getData();

                                    if (listReport.size() > 0)
                                    {
                                        binding.noData.llNoData.setVisibility(View.GONE);
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
                        public void onFailure(Call<EmpDARReportListResponse> call, Throwable t)
                        {
                            binding.loading.llLoading.setVisibility(View.GONE);
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                            Log.e("<><><>", "onFailure: " + t.getMessage());
                        }
                    });
        }
    }

    void setAdapter()
    {
        binding.rvDAR.setLayoutManager(new LinearLayoutManager(activity));
        ReportAdapter reportAdapter = new ReportAdapter();
        binding.rvDAR.setAdapter(reportAdapter);
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        @Override
        public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_emp_dar_report, viewGroup, false);
            return new ReportAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ReportAdapter.ViewHolder holder, final int position)
        {
            EmpDARReportListResponse.DataBean bean = listReport.get(position);
            holder.binding.tvMonth.setText(bean.getFull_month());
            holder.binding.tvTotalDays.setText(String.valueOf(bean.getTotalDays()));
            holder.binding.tvWorkingDays.setText(String.valueOf(bean.getWorkingdays()));
            holder.binding.tvTotalHours.setText(String.valueOf(bean.getTotalHours()));
            holder.binding.tvWorkingHours.setText(String.valueOf(bean.getWorkingHours()));

            holder.itemView.setOnClickListener(view -> {
                Intent intent = new Intent(activity, EmpDARReportDetailsActivity.class);
                intent.putExtra("employeeId", employeeId);
                intent.putExtra("currentYear", Integer.parseInt(selectedYear));
                intent.putExtra("currentMonth", bean.getMonth());
                intent.putExtra("monthName", bean.getFull_month());
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            });
        }

        @Override
        public int getItemCount()
        {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            RowviewEmpDarReportBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
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
        listDialog.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
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

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });

        listDialog.show();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder> {
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
            if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedYear = listYear.get(position);
                                binding.tvYear.setText(selectedYear);
                                getData();
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(YEAR))
            {
                return listYear.size();
            }
            else
            {
                return 0;
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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
}
