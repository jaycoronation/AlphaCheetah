package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.EmployeeListResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpOverviewBinding;
import cheetah.alphacapital.databinding.RowviewDarListBinding;
import cheetah.alphacapital.reportApp.getset.AUMReportResponse;
import cheetah.alphacapital.reportApp.getset.DARByEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.EmpDashboardResponse;
import cheetah.alphacapital.reportApp.getset.EmployeeDetailResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpOverviewFragment extends Fragment {
    private Activity activity;
    private SessionManager sessionManager;
    private FragmentEmpOverviewBinding binding;
    private ApiInterface apiService;

    private BottomSheetDialog listDialog;

    private List<String> listFilterType = new ArrayList<>();
    private List<AUMReportResponse.DataBean.AumEmployeeYearlySummeryResultBean> listChart = new ArrayList<>();
    private List<EmployeeListResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<EmployeeListResponse.DataBean.AllEmployeeBean>();
    private List<DARByEmployeeResponse.DataBean> listDAR = new ArrayList<>();

    final String EMPLOYEE = "Employee";
    final String TYPE = "Type";

    private String selectedFilterType = "AUM";
    private int selectedEmployeeId = 0;
    private String selectedEmployeeName = "";

    private int employeeId = 0;
    private boolean isForEmployeeLogin;

    private boolean isLoading = false;
    private int pageIndex = 1;
    private String pagesize = "20";
    private boolean isLastPage = false;
    ReportAdapter reportAdapter;

    @SuppressLint("ValidFragment")
    public EmpOverviewFragment(int employeeId, final String selectedEmployeeName, final boolean isForEmployeeLogin)
    {
        this.employeeId = employeeId;
        this.selectedEmployeeName = selectedEmployeeName;
        this.isForEmployeeLogin = isForEmployeeLogin;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_overview, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initView();
        getEmployeeDetails();
        return binding.getRoot();
    }

    private void initView()
    {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.rvDAR.setLayoutManager(linearLayoutManager);

        selectedEmployeeId = employeeId;
        binding.tvSelectedEmployee.setText(selectedEmployeeName);

        fillFilterTypeList();

        //binding.chart.setBackgroundColor(Color.rgb(104, 241, 175));
        binding.chart.setDrawGridBackground(false);
        binding.chart.getAxisLeft().setDrawGridLines(false);
        binding.chart.getXAxis().setDrawGridLines(true);
        binding.chart.getDescription().setEnabled(false);

        binding.llSelectEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (!isForEmployeeLogin)
                {
                    showListDialog(EMPLOYEE);
                }
            }
        });

        binding.llSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showListDialog(TYPE);
            }
        });

        /*binding.rvDAR.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager)
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
                            getDARFromEmployee(false);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });*/

        binding.nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged()
            {
                View view = (View) binding.nestedScrollView.getChildAt(binding.nestedScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (binding.nestedScrollView.getHeight() + binding.nestedScrollView.getScrollY()));

                if (diff == 0)
                {

                    try
                    {
                        if (!isLoading && !isLastPage)
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                getDARFromEmployee(false);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void fillFilterTypeList()
    {
        listFilterType.add("AUM");
        listFilterType.add("New Meetings");
        listFilterType.add("Existing Meetings");
        listFilterType.add("Inflow / Outflow");
        listFilterType.add("SIP");
        listFilterType.add("Client Reference");
        listFilterType.add("Summary Mail");
        listFilterType.add("Day Forward Mail");
        listFilterType.add("New Client Converted");
        listFilterType.add("Self Acquired AUM");
        listFilterType.add("DAR");
    }

    void getChartData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            listChart.clear();
            binding.noInternet.llNoInternet.setVisibility(View.GONE);
            apiService.getAUMYearWise(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), String.valueOf(employeeId)).enqueue(new Callback<AUMReportResponse>() {
                @Override
                public void onResponse(Call<AUMReportResponse> call, Response<AUMReportResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            listChart = response.body().getData().getAumEmployeeYearlySummeryResult();
                            drawChart(selectedFilterType);

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
                    if (!isForEmployeeLogin)
                    {
                        getAllEmployee();
                    }
                    else
                    {
                        getDashboardData();
                    }
                }

                @Override
                public void onFailure(Call<AUMReportResponse> call, Throwable t)
                {
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    if (!isForEmployeeLogin)
                    {
                        getAllEmployee();
                    }
                    else
                    {
                        getDashboardData();
                    }
                }
            });
        }
        else
        {
            binding.noInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    void getAllEmployee()
    {
        listEmployee.clear();
        apiService.getAllEmployee(0, 0,"").enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response)
            {
                if (response.isSuccessful())
                {
                    binding.noData.llNoData.setVisibility(View.GONE);
                    listEmployee = response.body().getData().getAllEmployee();
                }
                else
                {

                }

                getDashboardData();
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t)
            {
                getDashboardData();
            }
        });

    }

    void getEmployeeDetails()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getEmployeeDetails(String.valueOf(selectedEmployeeId)).enqueue(new Callback<EmployeeDetailResponse>() {
                @Override
                public void onResponse(Call<EmployeeDetailResponse> call, Response<EmployeeDetailResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            binding.tvEmployee.setText(AppUtils.toDisplayCase(response.body().getData().getFirst_name() + " " + response.body().getData().getLast_name()));
                            binding.tvDesignation.setVisibility(response.body().getData().getEmp_type() != null && response.body().getData().getEmp_type().length() > 0 ? View.VISIBLE : View.GONE);
                            binding.tvDesignation.setText(response.body().getData().getEmp_type());
                            binding.tvEmpShortName.setText(response.body().getData().getFirst_name().toUpperCase().charAt(0) + " " + response.body().getData().getLast_name().toUpperCase().charAt(0));
                            binding.tvDateOfJoin.setText(AppUtils.universalDateConvert(response.body().getData().getJoining_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM, yyyy"));
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
                    getChartData();
                }

                @Override
                public void onFailure(Call<EmployeeDetailResponse> call, Throwable t)
                {
                    AppUtils.showToast(activity, "Error while getting Employee details");
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    getChartData();
                }
            });
        }
        else
        {
            AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
        }
    }

    void getDashboardData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            apiService.getDashboardResponse(String.valueOf(selectedEmployeeId), String.valueOf(currentMonth), String.valueOf(currentYear)).enqueue(new Callback<EmpDashboardResponse>() {
                @Override
                public void onResponse(Call<EmpDashboardResponse> call, Response<EmpDashboardResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {

                            binding.tvAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getMonth_End_AUM())));
                            binding.tvSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_SIP())));
                            binding.tvMeetingNew.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_New_Meeting())));
                            binding.tvMeetingExisting.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_Existing_Meeting())));
                            binding.tvInflowOutflow.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getInflow_Outfolw())));
                            binding.tvClientReference.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_Reference())));
                            binding.tvSummaryMail.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getSummery_mail())));
                            binding.tvDayForwardMail.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getDay_forward_mail())));
                            binding.tvNewClientConverted.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_NewClientsConverted())));
                        }
                    }
                    getDashboardDataForLastMonth();
                }

                @Override
                public void onFailure(Call<EmpDashboardResponse> call, Throwable t)
                {
                    getDashboardDataForLastMonth();
                }
            });
        }
    }

    void getDashboardDataForLastMonth()
    {
        if (sessionManager.isNetworkAvailable())
        {
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);

            int lastMonth = 0;
            if (currentMonth != 0)
            {
                lastMonth = currentMonth - 1;
            }

            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getDashboardResponse(String.valueOf(selectedEmployeeId), String.valueOf(lastMonth), String.valueOf(currentYear)).enqueue(new Callback<EmpDashboardResponse>() {
                @Override
                public void onResponse(Call<EmpDashboardResponse> call, Response<EmpDashboardResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.tvLastMonthAUM.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getMonth_End_AUM())));
                            binding.tvLastMonthSip.setText(AppUtils.convertToDecimalValueForListing(String.valueOf(response.body().getData().getDashboardData().getActual_SIP())));
                        }
                    }
                    pageIndex = 1;
                    getDARFromEmployee(true);
                }

                @Override
                public void onFailure(Call<EmpDashboardResponse> call, Throwable t)
                {
                    pageIndex = 1;
                    getDARFromEmployee(true);
                }
            });
        }
    }

    void getDARFromEmployee(boolean isFirstTime)
    {
        if (sessionManager.isNetworkAvailable())
        {
            if (isFirstTime)
            {
                binding.loading.llLoading.setVisibility(View.VISIBLE);
                listDAR.clear();
            }
            else
            {
                binding.loadMore.llLoadingMore.setVisibility(View.VISIBLE);
            }
            apiService.getDARbyEmployee(String.valueOf(pageIndex), "20", String.valueOf(selectedEmployeeId)).enqueue(new Callback<DARByEmployeeResponse>() {
                @Override
                public void onResponse(Call<DARByEmployeeResponse> call, Response<DARByEmployeeResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            listDAR.addAll(response.body().getData());

                            pageIndex = pageIndex + 1;

                            if (response.body().getData().size() == 0 || response.body().getData().size() % 20 != 0)
                            {
                                isLastPage = true;
                            }

                            if (listDAR.size() > 0)
                            {
                                binding.tvDARTitle.setVisibility(View.VISIBLE);
                                if (isFirstTime)
                                {
                                    reportAdapter = new ReportAdapter();
                                    binding.rvDAR.setAdapter(reportAdapter);
                                }
                                else
                                {
                                    reportAdapter.notifyDataSetChanged();
                                }
                            }
                            else
                            {
                                binding.tvDARTitle.setVisibility(View.GONE);
                            }
                        }
                    }
                    isLoading = false;
                    binding.loading.llLoading.setVisibility(View.GONE);
                    binding.loadMore.llLoadingMore.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<DARByEmployeeResponse> call, Throwable t)
                {
                    binding.loading.llLoading.setVisibility(View.GONE);
                    binding.tvDARTitle.setVisibility(View.GONE);
                    binding.loadMore.llLoadingMore.setVisibility(View.GONE);
                    isLoading = false;
                }
            });
        }
    }

    void drawChart(String type)
    {
        LineDataSet set = new LineDataSet(getDataSet(type), type);
        set.setDrawFilled(true);
        set.setCubicIntensity(0.1f);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        LineData data = new LineData(set);
        binding.chart.setData(data);
        XAxis xAxis = binding.chart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        binding.chart.invalidate();
    }

    private ArrayList<String> getXAxisValues()
    {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEP");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }

    private ArrayList<Entry> getDataSet(String type)
    {
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        valueSet1.add(getMonthWiseData(1, type));
        valueSet1.add(getMonthWiseData(2, type));
        valueSet1.add(getMonthWiseData(3, type));
        valueSet1.add(getMonthWiseData(4, type));
        valueSet1.add(getMonthWiseData(5, type));
        valueSet1.add(getMonthWiseData(6, type));
        valueSet1.add(getMonthWiseData(7, type));
        valueSet1.add(getMonthWiseData(8, type));
        valueSet1.add(getMonthWiseData(9, type));
        valueSet1.add(getMonthWiseData(10, type));
        valueSet1.add(getMonthWiseData(11, type));
        valueSet1.add(getMonthWiseData(12, type));

        return valueSet1;
    }

    Entry getMonthWiseData(int month, String type)
    {
        Entry entry = null;
        for (int i = 0; i < listChart.size(); i++)
        {
            if (listChart.get(i).getMonth() == month)
            {
                if (type.equalsIgnoreCase("AUM"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getMonth_End_AUM());
                }
                else if (type.equalsIgnoreCase("New Meetings"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getMeetings_New());
                }
                else if (type.equalsIgnoreCase("Existing Meetings"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getMeetings_Existing());
                }
                else if (type.equalsIgnoreCase("Inflow / Outflow"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getInflow_Outfolw());
                }
                else if (type.equalsIgnoreCase("SIP"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getSip());
                }
                else if (type.equalsIgnoreCase("Client Reference"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getReferences());
                }
                else if (type.equalsIgnoreCase("Summary Mail"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getSummery_mail());
                }
                else if (type.equalsIgnoreCase("Day Forward Mail"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getDay_forward_mail());
                }
                else if (type.equalsIgnoreCase("New Client Converted"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getNew_Clients_Convered());
                }
                else if (type.equalsIgnoreCase("Self Acquired AUM"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getSelf_Acquired_AUM());
                }
                else if (type.equalsIgnoreCase("DAR"))
                {
                    entry = new Entry(month - 1, (float) listChart.get(i).getDAR());
                }
            }
        }
        return entry;
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
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position)
        {
            if (position == getItemCount() - 1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }
            if (isFor.equalsIgnoreCase(TYPE))
            {
                holder.tvValue.setText(listFilterType.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedFilterType.equals(listFilterType.get(position)))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedFilterType = listFilterType.get(position);
                                binding.tvType.setText(selectedFilterType);

                                drawChart(selectedFilterType);
                            }
                        }

                    }
                });
            }
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                holder.tvValue.setText(listEmployee.get(position).getFirst_name() + " " + listEmployee.get(position).getLast_name());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (selectedEmployeeId != listEmployee.get(position).getId())
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedEmployeeId = listEmployee.get(position).getId();
                                selectedEmployeeName = listEmployee.get(position).getFirst_name() + " " + listEmployee.get(position).getLast_name();
                                binding.tvSelectedEmployee.setText(selectedEmployeeName);
                                employeeId = selectedEmployeeId;

                                getEmployeeDetails();

                                if (EmpProfileFragment.handler != null)
                                {
                                    Message message = Message.obtain();
                                    message.what = 1;
                                    message.obj = selectedEmployeeId;
                                    EmpProfileFragment.handler.sendMessage(message);
                                }

                                /*getChartData();
                                getAllEmployee();
                                getDashboardData();
                                getDashboardDataForLastMonth();
                                getDARFromEmployee();*/
                            }
                        }

                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(TYPE))
            {
                return listFilterType.size();
            }
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                return listEmployee.size();
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

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_dar_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {

            DARByEmployeeResponse.DataBean bean = listDAR.get(position);
            holder.binding.tvMessage.setText(bean.getDar_message());
            holder.binding.tvClient.setText(bean.getC_first_name() + " " + bean.getC_last_name());
            holder.binding.tvActivityType.setText(bean.getActivity_type_name());
            if (!bean.getCreated_date().equals("") && bean.getCreated_date().contains("T"))
            {
                holder.binding.tvDate.setText(AppUtils.universalDateConvert(bean.getReportDate().replace("T", " "), "yyyy-MM-dd hh:mm:ss.SSS", "MMM dd, yyyy"));
            }
            else
            {
                holder.binding.tvDate.setText("-");
            }
            holder.binding.tvDateTime.setText(bean.getTimeSpent() + " Hours, " + bean.getTimeSpent_Min() + " Minutes");
            holder.binding.tvEmployee.setText(bean.getFirst_name() + " " + bean.getLast_name());
            holder.binding.tvRemark.setText(bean.getRemarksComment());
        }

        @Override
        public int getItemCount()
        {
            return listDAR.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder {
            RowviewDarListBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
            }
        }
    }
}
