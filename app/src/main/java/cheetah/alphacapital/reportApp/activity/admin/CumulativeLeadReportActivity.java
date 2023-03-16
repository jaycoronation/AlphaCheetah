package cheetah.alphacapital.reportApp.activity.admin;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityCumulativeLeadReportBinding;
import cheetah.alphacapital.databinding.RowviewListCumulativeReportBinding;
import cheetah.alphacapital.databinding.RowviewListReportBinding;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.GetAllCumulativeLeadsReportResponse;
import cheetah.alphacapital.reportApp.getset.GetAllLeadsReportResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CumulativeLeadReportActivity extends BaseActivity {

    private ActivityCumulativeLeadReportBinding binding;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private String selectedYear = "", selectedMonth = "", selectedMonthName = "", currentYear = "";
    private Integer selectedYearInt = 0, selectedMonthInt = 0;
    private List<String> listYear = new ArrayList<>();
    private List<CommonGetSet> listMonth = new ArrayList<>();
    private BottomSheetDialog listDialog;

    private LeadsReportAdapter leadsReportAdapter;

    private ArrayList<GetAllCumulativeLeadsReportResponse.LstEmpDataItem> listCumulativeLeadsReport = new ArrayList<>();
    private ArrayList<GetAllCumulativeLeadsReportResponse.LstEmpDataItem> listCumulativeLeadsReport_search = new ArrayList<>();

    private Timer timer = new Timer();
    private final long DELAY = 400;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_cumulative_lead_report);
        initView();
        onClick();
    }

    private void initView() {

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Cumulative Leads Report");
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);

        DateFormat monthFormat = new SimpleDateFormat("MM");
        DateFormat monthFormatDisplay = new SimpleDateFormat("MMMM");
        DateFormat yearFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();

        binding.tvMonth.setText(monthFormatDisplay.format(date));
        binding.tvYear.setText(yearFormat.format(date));

        currentYear = yearFormat.format(date);
        selectedYearInt = Integer.parseInt(yearFormat.format(date));
        selectedMonthInt = Integer.parseInt(monthFormat.format(date));

        if (sessionManager.isNetworkAvailable())
        {
            getMonthYearData();
            getCumulativeLeadReportList();
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void onClick() {

        binding.toolbar.llBackNavigation.setOnClickListener(v -> finish());

        binding.llSelectMonth.setOnClickListener(v -> showListDialog(MONTH));

        binding.llSelectYear.setOnClickListener(v -> showListDialog(YEAR));

        binding.toolbar.ivSerach.setOnClickListener(v -> doSearch());

        binding.toolbar.ivClose.setOnClickListener(v -> doClose());

        binding.toolbar.edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
            {
                try
                {
                    cs = binding.toolbar.edtSearch.getText().toString().trim();
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
                                    activity.runOnUiThread(() -> {
                                        listCumulativeLeadsReport_search = new ArrayList<GetAllCumulativeLeadsReportResponse.LstEmpDataItem>();
                                        if (listCumulativeLeadsReport != null && listCumulativeLeadsReport.size() > 0)
                                        {
                                            for (int i = 0; i < listCumulativeLeadsReport.size(); i++)
                                            {
                                                final String text = listCumulativeLeadsReport.get(i).getEmployeeName();

                                                String text1 = AppUtils.getCapitalText(text);

                                                String cs1 = AppUtils.getCapitalText(String.valueOf(finalCs));

                                                if (text1.contains(cs1))
                                                {
                                                    listCumulativeLeadsReport_search.add(listCumulativeLeadsReport.get(i));
                                                }
                                            }


                                            if (listCumulativeLeadsReport_search.size() > 0)
                                            {
                                                leadsReportAdapter = new LeadsReportAdapter(listCumulativeLeadsReport_search, activity);
                                                binding.rvLeads.setAdapter(leadsReportAdapter);
                                                binding.llNoData.llNoData.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
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
                        if (listCumulativeLeadsReport.size() > 0)
                        {
                            leadsReportAdapter = new LeadsReportAdapter(listCumulativeLeadsReport, activity);
                            binding.rvLeads.setAdapter(leadsReportAdapter);
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.llNoData.llNoData.setVisibility(View.VISIBLE);
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
        binding.toolbar.llBackNavigation.setVisibility(View.VISIBLE);
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.edtSearch.setVisibility(View.GONE);
        binding.toolbar.cvCard.setVisibility(View.GONE);
        binding.toolbar.ivSerach.setVisibility(View.VISIBLE);
        binding.toolbar.ivClose.setVisibility(View.GONE);
        AppUtils.hideKeyboard(binding.toolbar.edtSearch,activity);
        binding.toolbar.edtSearch.setText("");
    }

    private void doSearch()
    {
        binding.toolbar.edtSearch.requestFocus();
        binding.toolbar.llBackNavigation.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setVisibility(View.GONE);
        binding.toolbar.edtSearch.setVisibility(View.VISIBLE);
        binding.toolbar.cvCard.setVisibility(View.VISIBLE);
        binding.toolbar.ivSerach.setVisibility(View.GONE);
        binding.toolbar.ivClose.setVisibility(View.VISIBLE);
    }
    
    private void getCumulativeLeadReportList() {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.GetCumulativeLeadReport(0,selectedYearInt,selectedMonthInt).enqueue(new Callback<GetAllCumulativeLeadsReportResponse>() {
            @Override
            public void onResponse(Call<GetAllCumulativeLeadsReportResponse> call, Response<GetAllCumulativeLeadsReportResponse> response) {
                if (response.isSuccessful()) 
                {
                    if (response.isSuccessful()) 
                    {
                        if (response.body().isSuccess())
                        {
                            listCumulativeLeadsReport = new ArrayList<>();
                            listCumulativeLeadsReport.addAll(response.body().getData().getLstEmpData());

                            if (listCumulativeLeadsReport.size() > 0)
                            {
                                leadsReportAdapter = new LeadsReportAdapter(listCumulativeLeadsReport, activity);
                                binding.rvLeads.setAdapter(leadsReportAdapter);
                                binding.llNoData.llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            //showToast(response.body().getMessage());
                        }
                    } 
                    else 
                    {
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    }
                }

                    binding.llMore.setVisibility(View.GONE);
                    binding.llLoading.llLoading.setVisibility(View.GONE);
                    binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<GetAllCumulativeLeadsReportResponse> call, Throwable t) {
                    binding.llLoading.llLoading.setVisibility(View.GONE);
                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
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

   /* RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(CumulativeLeadReportActivity.this) {
        @Override protected int getVerticalSnapPreference() {
            return LinearSmoothScroller.SNAP_TO_START;
        }
    };*/

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

        if (isFor.equalsIgnoreCase(YEAR))
        {
            Integer position = 0;
            for (int i = 0; i < listYear.size(); i++)
            {
                if (listYear.get(i).equalsIgnoreCase(currentYear))
                {
                    position = i;
                }
            }

            rvListDialog.smoothScrollToPosition(position);
        }

        btnNo.setOnClickListener(v -> {
            listDialog.dismiss();
            listDialog.cancel();
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
                holder.itemView.setOnClickListener(v -> {
                    listDialog.dismiss();
                    if (!selectedMonth.equalsIgnoreCase(String.valueOf(getSet.getId())))
                    {
                        binding.tvMonth.setText(getSet.getName());
                        selectedMonthInt = getSet.getId();
                        selectedMonth = String.valueOf(getSet.getId());
                        if (sessionManager.isNetworkAvailable())
                        {
                            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                            getCumulativeLeadReportList();
                        }
                        else
                        {
                            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(YEAR))
            {
                holder.tvValue.setText(listYear.get(position).toString());
                holder.itemView.setOnClickListener(v -> {
                    listDialog.dismiss();
                    if (!selectedYear.equalsIgnoreCase(listYear.get(position).toString()))
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            selectedYear = listYear.get(position).toString();
                            binding.tvYear.setText(selectedYear);
                            selectedMonthInt = Integer.parseInt(listYear.get(position)) ;
                            if (sessionManager.isNetworkAvailable())
                            {
                                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                                getCumulativeLeadReportList();
                            }
                            else
                            {
                                binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
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

    private static class LeadsReportAdapter extends RecyclerView.Adapter<LeadsReportAdapter.ViewHolder> {
        List<GetAllCumulativeLeadsReportResponse.LstEmpDataItem> listItems;
        private Activity activity;

        public LeadsReportAdapter(ArrayList<GetAllCumulativeLeadsReportResponse.LstEmpDataItem> listEmployee, Activity activity)
        {
            this.listItems = listEmployee;
            this.activity = activity;
        }

        @NonNull
        @Override
        public LeadsReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int i)
        {
            View view = LayoutInflater.from(activity).inflate(R.layout.rowview_list_cumulative_report,parent,false);
            return new LeadsReportAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final LeadsReportAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final GetAllCumulativeLeadsReportResponse.LstEmpDataItem getSet = listItems.get(position);
                holder.binding.setDataGetSet(getSet);
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

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewListCumulativeReportBinding binding;
            public ViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }
        }

    }
    
}