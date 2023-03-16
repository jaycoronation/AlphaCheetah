package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.admin.AddDarEntryActivity;
import cheetah.alphacapital.reportApp.getset.DARResponse;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpDarreportListBinding;
import cheetah.alphacapital.databinding.RowviewDarListBinding;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.DARDetailsReportListResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpDARReportListFragment extends Fragment {

    private Activity activity;
    private SessionManager sessionManager;
    private FragmentEmpDarreportListBinding binding;
    private ApiInterface apiService;

    private String YEAR = "Year", MONTH = "Month", TYPE = "Activity Type", EMPLOYEE = "Employee", CLIENT = "Client";
    private String selectedYear = "", selectedMonth = "", selectedEmployeeName = "Select Employee", selectedTypeName = "Select Activity Type", selectedClientName = "Select Client", monthName = "";
    private int employeeId, currentMonth = 0, currentYear = 0, selectedClientId = 0, selectedEmployeeId = 0, selectedTypeId = 0;

    private List<String> listYear = new ArrayList<>();
    private List<CommonGetSet> listMonth = new ArrayList<>();
    private List<DARDetailsReportListResponse.DataBean> listReport = new ArrayList<>();
    private List<DARDetailsReportListResponse.DataBean> listReportAll = new ArrayList<>();
    private List<DARDetailsReportListResponse.DataBean> listReportSearch = new ArrayList<>();
    private List<CommonGetSet> listType = new ArrayList<>();
    private List<CommonGetSet> listEmployee = new ArrayList<>();
    private List<CommonGetSet> listClient = new ArrayList<>();

    private BottomSheetDialog listDialog;

    public static Handler handler;

    @SuppressLint("ValidFragment")
    public EmpDARReportListFragment(int employeeId, int currentMonth, int currentYear, String monthName)
    {
        this.employeeId = employeeId;
        this.currentMonth = currentMonth;
        this.currentYear = currentYear;
        this.monthName = monthName;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_darreport_list, container, false);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initViews();
        selectedMonth = String.valueOf(currentMonth);
        selectedYear = String.valueOf(currentYear);
        //getClientData();
        getYearData();

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message)
            {
                if (message.what == 1)
                {
                    String searchText = (String) message.obj;
                    if (searchText.length() > 0)
                    {
                        try
                        {
                            listReportSearch = new ArrayList<>();
                            if (listReport != null && listReport.size() > 0)
                            {
                                for (int i = 0; i < listReport.size(); i++)
                                {
                                    final String text = listReport.get(i).getDar_message().toLowerCase();
                                    if (text.contains(searchText.toLowerCase()))
                                    {
                                        listReportSearch.add(listReport.get(i));
                                    }
                                }

                                if (listReportSearch.size() > 0)
                                {
                                    binding.noData.llNoData.setVisibility(View.GONE);
                                    listReport.clear();
                                    listReport.addAll(listReportSearch);
                                    setAdapter();
                                }
                                else
                                {
                                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        listReport.clear();
                        listReport.addAll(listReportAll);
                        setAdapter();
                    }
                }
                else if (message.what == 2)
                {
                    AppUtils.hideKeyboard(binding.llFilter, activity);
                }
                else if (message.what == 222)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getYearData();
                    }
                    else
                    {
                        AppUtils.noInternetSnackBar(activity);
                    }
                }
                return false;
            }
        });

        return binding.getRoot();
    }

    private void initViews()
    {
        binding.llSelectYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showListDialog(YEAR);
            }
        });
        binding.llSelectMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showListDialog(MONTH);
            }
        });
        binding.llFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showFilterDialog();
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
                                        listYear.add(yearArray.getString(i));
                                    }

                                    if (listYear.size() > 0)
                                    {
                                      //  selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
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
                                        /*if (getSet.getId() == Integer.parseInt(selectedMonth))
                                        {
                                            break;
                                        }*/
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
                    binding.tvMonth.setText(monthName);
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

    void fillFilterData()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {

                listType.clear();
                listEmployee.clear();
                listClient.clear();

                for (int i = 0; i < listReportAll.size(); i++)
                {
                    CommonGetSet bean = new CommonGetSet();
                    bean.setName(listReportAll.get(i).getFirst_name() + " " + listReportAll.get(i).getLast_name());
                    bean.setId(listReportAll.get(i).getEmployee_id());
                    if (!listEmployee.contains(bean))
                    {
                        listEmployee.add(bean);
                    }

                    CommonGetSet beanType = new CommonGetSet();
                    beanType.setName(listReportAll.get(i).getActivity_type_name());
                    beanType.setId(listReportAll.get(i).getActivity_type_id());
                    if (!listType.contains(beanType))
                    {
                        listType.add(beanType);
                    }

                    CommonGetSet beanClient = new CommonGetSet();
                    beanClient.setName(listReportAll.get(i).getC_first_name() + " " + listReportAll.get(i).getC_last_name());
                    beanClient.setId(listReportAll.get(i).getClient_id());
                    if (beanClient.getId() != 0 && !listClient.contains(beanClient))
                    {
                        listClient.add(beanClient);
                    }
                }

                try
                {
                    if (listType.size() > 0)
                    {
                        Set<CommonGetSet> s = new TreeSet<CommonGetSet>(new Comparator<CommonGetSet>() {
                            @Override
                            public int compare(CommonGetSet o1, CommonGetSet o2)
                            {
                                if (o1.getId() == o2.getId())
                                {
                                    return 0;
                                }
                                else
                                {
                                    return 1;
                                }
                            }

                        });
                        s.addAll(listType);
                        listType = new ArrayList<>(s);
                    }

                    if (listEmployee.size() > 0)
                    {
                        Set<CommonGetSet> emp = new TreeSet<CommonGetSet>(new Comparator<CommonGetSet>() {
                            @Override
                            public int compare(CommonGetSet o1, CommonGetSet o2)
                            {
                                if (o1.getId() == o2.getId())
                                {
                                    return 0;
                                }
                                else
                                {
                                    return 1;
                                }
                            }

                        });
                        emp.addAll(listEmployee);
                        listEmployee = new ArrayList<>(emp);
                    }


                    if (listClient.size() > 0)
                    {
                        Set<CommonGetSet> emp = new TreeSet<CommonGetSet>(new Comparator<CommonGetSet>() {
                            @Override
                            public int compare(CommonGetSet o1, CommonGetSet o2)
                            {
                                if (o1.getId() == o2.getId())
                                {
                                    return 0;
                                }
                                else
                                {
                                    return 1;
                                }
                            }

                        });
                        emp.addAll(listClient);
                        listClient = new ArrayList<>(emp);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                CommonGetSet getSet = new CommonGetSet();
                getSet.setId(0);
                getSet.setName("Select Activity Type");
                listType.add(0, getSet);

                getSet = new CommonGetSet();
                getSet.setId(0);
                getSet.setName("Select Employee");
                listEmployee.add(0, getSet);

                getSet = new CommonGetSet();
                getSet.setId(0);
                getSet.setName("Select Client");
                listClient.add(0, getSet);


                activity.runOnUiThread(() -> {
                    if (listClient.size() <= 1 && listEmployee.size() <= 1 && listType.size() <= 1)
                    {
                        binding.llFilter.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.llFilter.setVisibility(View.VISIBLE);
                    }
                });

            }
        }).start();
    }

    void applyTypeFilter(int typeID)
    {
        listReport.clear();

        for (int i = 0; i < listReportAll.size(); i++)
        {
            if (listReportAll.get(i).getActivity_type_id() == typeID)
            {
                listReport.add(listReportAll.get(i));
            }
        }

        if (listReport.size() > 0)
        {
            setAdapter();
        }
    }

    void clearTypeFilter()
    {
        listReport.clear();
        listReport.addAll(listReportAll);
        if (selectedEmployeeId != 0)
        {
            applyEmployeeFilter(selectedEmployeeId);
        }
        else
        {
            setAdapter();
        }
    }

    void clearEmployeeFilter()
    {
        listReport.clear();
        listReport.addAll(listReportAll);
        if (selectedTypeId != 0)
        {
            applyTypeFilter(selectedTypeId);
        }
        else
        {
            setAdapter();
        }
    }

    void applyEmployeeFilter(int typeID)
    {
        listReport.clear();

        for (int i = 0; i < listReportAll.size(); i++)
        {
            if (listReportAll.get(i).getEmployee_id() == typeID)
            {
                listReport.add(listReportAll.get(i));
            }
        }

        if (listReport.size() > 0)
        {
            setAdapter();
        }
    }

    void getData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getMonthWiseDARReportByEmployee(String.valueOf(selectedMonth),
                    String.valueOf(selectedYear),
                    "",
                    "",
                    String.valueOf(selectedClientId),
                    String.valueOf(employeeId))
                    .enqueue(new Callback<DARDetailsReportListResponse>() {
                        @Override
                        public void onResponse(Call<DARDetailsReportListResponse> call, Response<DARDetailsReportListResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if (response.body().isSuccess())
                                {
                                    listReportAll.clear();
                                    listReportAll.addAll(response.body().getData());
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

                                fillFilterData();
                            }
                            else
                            {
                                binding.noData.llNoData.setVisibility(View.VISIBLE);
                            }
                            binding.loading.llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<DARDetailsReportListResponse> call, Throwable t)
                        {

                        }
                    });
        }
    }


    void setAdapter()
    {
        binding.rvDAR.setLayoutManager(new LinearLayoutManager(activity));
        ReportAdapter reportAdapter = new ReportAdapter();
        binding.rvDAR.setAdapter(reportAdapter);
        calculateTotalHoursAndMinutes();
    }

    void calculateTotalHoursAndMinutes()
    {
        if (listReport.size() > 0)
        {
            try
            {
                int total = (sumHours() * 60) + sumMins();
                int hours = total / 60;
                int minutes = total % 60;
                binding.llBottomSummary.setVisibility(View.VISIBLE);
                binding.tvTotalTimespent.setText(hours + " Hours, " + minutes + " Minutes");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                binding.llBottomSummary.setVisibility(View.GONE);
            }
        }
    }

    public int sumHours()
    {
        int sum = 0;
        for (DARDetailsReportListResponse.DataBean bean : listReport)
            sum = sum + bean.getTimeSpent();
        return sum;
    }

    public int sumMins()
    {
        int sum = 0;
        for (DARDetailsReportListResponse.DataBean bean : listReport)
            sum = sum + bean.getTimeSpent_Min();
        return sum;
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

            DARDetailsReportListResponse.DataBean bean = listReport.get(position);
            holder.binding.tvMessage.setText(bean.getDar_message());
            holder.binding.tvClient.setText(bean.getC_first_name() != null && bean.getC_last_name() != null ?
                    bean.getC_first_name() + " " + bean.getC_last_name() : "-");
            holder.binding.tvActivityType.setText(bean.getActivity_type_name());
            holder.binding.tvDate.setText(AppUtils.universalDateConvert(bean.getReportDate().replace("T", " "), "yyyy-MM-dd hh:mm:ss.SSS", "MMM dd, yyyy"));
            holder.binding.tvDateTime.setText(bean.getTimeSpent() + " Hours, " + bean.getTimeSpent_Min() + " Minutes");
            holder.binding.tvEmployee.setText(bean.getFirst_name() + " " + bean.getLast_name());
            holder.binding.tvRemark.setText(bean.getRemarksComment());

            if (bean.getEmployee_id() == Integer.parseInt(sessionManager.getUserId()))
            {
                holder.binding.ivEdit.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.binding.ivEdit.setVisibility(View.GONE);
            }

            holder.binding.ivEdit.setOnClickListener(v ->
            {
                try
                {
                    final DARResponse.DataBean darDataBean = new DARResponse.DataBean();
                    darDataBean.setId(bean.getId());
                    darDataBean.setEmployee_id(bean.getEmployee_id());
                    darDataBean.setClient_id(bean.getClient_id());
                    darDataBean.setActivity_type_id(bean.getActivity_type_id());
                    darDataBean.setActivity_type_name(bean.getActivity_type_name());
                    darDataBean.setDar_message(bean.getDar_message());
                    darDataBean.setRMName(bean.getRMName());
                    darDataBean.setTimeSpent(bean.getTimeSpent());
                    darDataBean.setTimeSpent_Min(bean.getTimeSpent_Min());
                    darDataBean.setRemarksComment(bean.getRemarksComment());
                    darDataBean.setReportDate(bean.getReportDate());
                    darDataBean.setCreated_date(bean.getCreated_date());
                    darDataBean.setC_first_name(bean.getC_first_name());

                    Intent intent = new Intent(activity, AddDarEntryActivity.class);
                    intent.putExtra("isFor", "edit");
                    intent.putExtra("isEditClient", false);
                    intent.putExtra("clientId",String.valueOf(bean.getClient_id()));
                    intent.putExtra("data", (Parcelable) darDataBean);
                    startActivity(intent);
                    AppUtils.startActivityAnimation(activity);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listReport.size();
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
                                if (!selectedMonth.equals(""))
                                {
                                    getData();

                                    if (EmpDAREmployeeAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = 0;
                                        message.arg2 = Integer.parseInt(selectedYear);
                                        EmpDAREmployeeAnalysisFragment.handler.sendMessage(message);

                                    }
                                    if (EmpDARActivityAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = 0;
                                        message.arg2 = Integer.parseInt(selectedYear);
                                        EmpDARActivityAnalysisFragment.handler.sendMessage(message);

                                    }
                                    if (EmpDARClientAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = 0;
                                        message.arg2 = Integer.parseInt(selectedYear);
                                        EmpDARClientAnalysisFragment.handler.sendMessage(message);

                                    }
                                }
                            }
                        }

                    }
                });
            }
            else if (isFor.equalsIgnoreCase(MONTH))
            {

                holder.tvValue.setText(listMonth.get(position).getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (!selectedMonth.equalsIgnoreCase(String.valueOf(listMonth.get(position).getId())))
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedMonth = String.valueOf(listMonth.get(position).getId());
                                monthName = listMonth.get(position).getName();
                                binding.tvMonth.setText(monthName);
                                if (!selectedYear.equals(""))
                                {
                                    getData();

                                    if (EmpDAREmployeeAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = Integer.parseInt(selectedMonth);
                                        message.arg2 = 0;
                                        EmpDAREmployeeAnalysisFragment.handler.sendMessage(message);

                                    }
                                    if (EmpDARActivityAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = Integer.parseInt(selectedMonth);
                                        message.arg2 = 0;
                                        EmpDARActivityAnalysisFragment.handler.sendMessage(message);

                                    }
                                    if (EmpDARClientAnalysisFragment.handler != null)
                                    {
                                        Message message = Message.obtain();
                                        message.what = 1;
                                        message.arg1 = Integer.parseInt(selectedMonth);
                                        message.arg2 = 0;
                                        EmpDARClientAnalysisFragment.handler.sendMessage(message);

                                    }
                                }
                            }
                        }

                    }
                });
            }
            else if (isFor.equalsIgnoreCase(TYPE))
            {
                holder.tvValue.setText(listType.get(position).getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (selectedTypeId != listType.get(position).getId())
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                selectedTypeId = listType.get(position).getId();
                                selectedTypeName = listType.get(position).getName();
                                tvActivityType.setText(listType.get(position).getName());
                            }
                        }

                    }
                });
            }
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                holder.tvValue.setText(listEmployee.get(position).getName());
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
                                selectedEmployeeName = listEmployee.get(position).getName();
                                tvEmployee.setText(listEmployee.get(position).getName());
                            }
                        }

                    }
                });
            }
            else if (isFor.equalsIgnoreCase(CLIENT))
            {

                holder.tvValue.setText(listClient.get(position).getName() != null && listClient.get(position).getName().length() > 0 ?
                        listClient.get(position).getName() : "");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (selectedClientId != listClient.get(position).getId())
                        {
                            if (sessionManager.isNetworkAvailable())
                            {
                                if (listDialog != null)
                                {
                                    listDialog.dismiss();
                                }

                                selectedClientId = listClient.get(position).getId();
                                selectedClientName = listClient.get(position).getName();
                                tvClient.setText(listClient.get(position).getName());
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
            else if (isFor.equalsIgnoreCase(MONTH))
            {
                return listMonth.size();
            }
            else if (isFor.equalsIgnoreCase(TYPE))
            {
                return listType.size();
            }
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                return listEmployee.size();
            }
            else if (isFor.equalsIgnoreCase(CLIENT))
            {
                return listClient.size();
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

    void applyFilter()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                listReport.clear();

                if (selectedEmployeeId != 0 && selectedClientId != 0 && selectedTypeId != 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getEmployee_id() == selectedEmployeeId &&
                                listReportAll.get(i).getClient_id() == selectedClientId &&
                                listReportAll.get(i).getActivity_type_id() == selectedTypeId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId != 0 && selectedClientId != 0 && selectedTypeId == 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getEmployee_id() == selectedEmployeeId &&
                                listReportAll.get(i).getClient_id() == selectedClientId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId != 0 && selectedClientId == 0 && selectedTypeId == 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getEmployee_id() == selectedEmployeeId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId == 0 && selectedClientId != 0 && selectedTypeId != 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getClient_id() == selectedClientId &&
                                listReportAll.get(i).getActivity_type_id() == selectedTypeId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId == 0 && selectedClientId == 0 && selectedTypeId != 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getActivity_type_id() == selectedTypeId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId == 0 && selectedClientId != 0 && selectedTypeId == 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        if (listReportAll.get(i).getClient_id() == selectedClientId)
                        {
                            listReport.add(listReportAll.get(i));
                        }
                    }
                }
                else if (selectedEmployeeId == 0 && selectedClientId == 0 && selectedTypeId == 0)
                {
                    for (int i = 0; i < listReportAll.size(); i++)
                    {
                        listReport.add(listReportAll.get(i));
                    }
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        setAdapter();
                    }
                });
            }
        }).start();
    }

    TextView tvClient, tvActivityType, tvEmployee;
    LinearLayout llSelectClient, llSelectActivityType, llSelectEmployee, llClear, llApply;
    BottomSheetDialog filterDialog;

    private void showFilterDialog()
    {
        filterDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        filterDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_dar_report_filter, null);
        filterDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(filterDialog, activity);

        tvClient = sheetView.findViewById(R.id.tvClient);
        tvActivityType = sheetView.findViewById(R.id.tvActivityType);
        tvEmployee = sheetView.findViewById(R.id.tvEmployee);

        tvActivityType.setText(selectedTypeName);
        tvEmployee.setText(selectedEmployeeName);
        tvClient.setText(selectedClientName);

        llSelectClient = sheetView.findViewById(R.id.llSelectClient);
        llSelectActivityType = sheetView.findViewById(R.id.llSelectActivityType);
        llSelectEmployee = sheetView.findViewById(R.id.llSelectEmployee);
        llClear = sheetView.findViewById(R.id.llClear);
        llApply = sheetView.findViewById(R.id.llApply);

        llSelectClient.setVisibility(listClient.size() > 1 ? View.VISIBLE : View.GONE);
        llSelectActivityType.setVisibility(listType.size() > 1 ? View.VISIBLE : View.GONE);
        llSelectEmployee.setVisibility(listEmployee.size() > 1 ? View.VISIBLE : View.GONE);

        llSelectClient.setOnClickListener(view -> showListDialog(CLIENT));

        llSelectActivityType.setOnClickListener(view -> showListDialog(TYPE));

        llSelectEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                showListDialog(EMPLOYEE);
            }
        });

        llApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                filterDialog.dismiss();
                applyFilter();
            }
        });

        llClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                filterDialog.dismiss();
                selectedClientId = 0;
                selectedClientName = "Select Client";
                selectedEmployeeId = 0;
                selectedEmployeeName = "Select Employee";
                selectedTypeId = 0;
                selectedTypeName = "Select Activity Type";
                applyFilter();
            }
        });

        filterDialog.show();
    }
}
