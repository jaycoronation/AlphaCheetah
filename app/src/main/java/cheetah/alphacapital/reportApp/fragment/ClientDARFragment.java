package cheetah.alphacapital.reportApp.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.admin.AddDarEntryActivity;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.DARResponse;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientDARFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private View rootView;
    public ApiInterface apiService;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.tvMonth)
    TextView tvMonth;
    @BindView(R.id.llSelectMonth)
    LinearLayout llSelectMonth;
    @BindView(R.id.tvYear)
    TextView tvYear;
    @BindView(R.id.llSelectYear)
    LinearLayout llSelectYear;
    @BindView(R.id.tvSelectedDate)
    TextView tvSelectedDate;
    @BindView(R.id.llSelectDate)
    LinearLayout llSelectDate;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.rvDAR)
    RecyclerView rvDAR;
    @BindView(R.id.imgFilter)
    ImageView imgFilter;
    @BindView(R.id.tvTotalHour)
    TextView tvTotalHour;
    @BindView(R.id.ivAddDAR)
    ImageView ivAddDAR;


    public ArrayList<DARResponse.DataBean> listDAR = new ArrayList<DARResponse.DataBean>();
    public ArrayList<DARResponse.DataBean> listDARFilter = new ArrayList<DARResponse.DataBean>();
    public ArrayList<DARResponse.DataBean> listDARSearch = new ArrayList<DARResponse.DataBean>();
    private DARAdapter darAdapter;
    private BottomSheetDialog listDialog;
    private List<String> listYear = new ArrayList<>();
    private ArrayList<MonthYearResponse.DataBean.MonthBean> listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private final String TYPE = "Type";
    private final String EMPLOYEE = "Employee";
    private String selectedYear = "", selectedMonth = "", selectedTypeName = "", selectedTypeID = "", selectedEmployeeName = "", selectedEmployeeId = "";
    private String clientId;
    private ArrayList<CommonGetSet> listActivityType = new ArrayList<CommonGetSet>();
    private ArrayList<CommonGetSet> listEmployee = new ArrayList<CommonGetSet>();
    BottomSheetDialog filterdialog;
    public static Handler handler;
    private Timer timer = new Timer();
    private final long DELAY = 400;

    public ClientDARFragment()
    {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_client_dar, container, false);
        activity = getActivity();
        ButterKnife.bind(this, rootView);
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        clientId = getArguments().getString("clientid");

        Log.d("<><> clientid :" ,clientId);
        setupViews(rootView);
        onClicks();

        handler = new Handler(Looper.getMainLooper())
        {
            public void handleMessage(Message msg)
            {
                if (msg.what == 111 && msg.obj != null)
                {
                    try
                    {
                        String aResponse = (String) msg.obj;

                        if (!aResponse.equalsIgnoreCase(""))
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
                                                try
                                                {
                                                    listDARSearch = new ArrayList<DARResponse.DataBean>();

                                                    if (selectedTypeID.length() > 0 || selectedEmployeeId.length() > 0)
                                                    {
                                                        for (int i = 0; i < listDARFilter.size(); i++)
                                                        {
                                                            String employeeName = listDARFilter.get(i).getDar_message();

                                                            if (employeeName.toLowerCase(Locale.getDefault()).contains(aResponse))
                                                            {
                                                                listDARSearch.add(listDARFilter.get(i));
                                                            }
                                                        }

                                                        calculateTotalHoursAndMinutes(listDARSearch);
                                                    }
                                                    else
                                                    {
                                                        for (int i = 0; i < listDAR.size(); i++)
                                                        {
                                                            String employeeName = listDAR.get(i).getDar_message();

                                                            if (employeeName.toLowerCase(Locale.getDefault()).contains(aResponse))
                                                            {
                                                                listDARSearch.add(listDAR.get(i));
                                                            }
                                                        }

                                                        calculateTotalHoursAndMinutes(listDARSearch);
                                                    }

                                                    if (listDARSearch.size() > 0)
                                                    {
                                                        llNoData.setVisibility(View.GONE);
                                                        darAdapter = new DARAdapter(listDARSearch);
                                                        rvDAR.setAdapter(darAdapter);
                                                    }
                                                    else
                                                    {
                                                        llNoData.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
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
                            if (selectedTypeID.length() > 0 || selectedEmployeeId.length() > 0)
                            {
                                if (listDARFilter.size() > 0)
                                {
                                    llNoData.setVisibility(View.GONE);
                                    darAdapter = new DARAdapter(listDARFilter);
                                    rvDAR.setAdapter(darAdapter);
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }

                                calculateTotalHoursAndMinutes(listDARFilter);

                            }
                            else
                            {
                                if (listDAR.size() > 0)
                                {
                                    llNoData.setVisibility(View.GONE);
                                    darAdapter = new DARAdapter(listDAR);
                                    rvDAR.setAdapter(darAdapter);
                                }
                                else
                                {
                                    llNoData.setVisibility(View.VISIBLE);
                                }

                                calculateTotalHoursAndMinutes(listDAR);

                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (msg.what == 222)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getDARDetails();
                        llNoInternet.setVisibility(View.GONE);
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        return rootView;
    }

    private void setupViews(View rootView)
    {
        rvDAR.setLayoutManager(new LinearLayoutManager(activity));
        tvNoDataText.setText("No DAR Data Found.");
        selectedMonth = AppUtils.getCurrentMonth();
        selectedYear = AppUtils.getCurrentYear();

        if (sessionManager.isNetworkAvailable())
        {
            getMonthYearData();
            getDARDetails();
            llNoInternet.setVisibility(View.GONE);
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }
        Log.i("***********", "setupViews: " + selectedMonth + "    " + selectedYear);
    }

    private void onClicks()
    {
        llSelectMonth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(MONTH);
            }
        });

        llSelectYear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(YEAR);
            }
        });

        imgFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFilterDialog();
            }
        });

        ivAddDAR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* Intent intent = new Intent(activity, AddDARNewActivity.class);
                activity.startActivity(intent);
                AppUtils.startActivityAnimation(activity);*/

                Log.d("<><> clientid :" ,clientId);
                Intent intent = new Intent(activity, AddDarEntryActivity.class);
                intent.putExtra("isFor","add");
                intent.putExtra("isEditClient",false);
                intent.putExtra("clientId",clientId);
                startActivity(intent);
                AppUtils.startActivityAnimation(activity);
            }
        });
    }

    private void getDARDetails()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<DARResponse> call = apiService.getMonthWiseDarReportByEmployee(
                "0",
                "0",
                selectedMonth,
                selectedYear,
                clientId,
                "0");  //emp id 0 for gettting all list
        call.enqueue(new Callback<DARResponse>()
        {
            @Override
            public void onResponse(Call<DARResponse> call, Response<DARResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listDAR = new ArrayList<DARResponse.DataBean>();
                        listDAR.addAll(response.body().getData());

                        if (listDAR.size() > 0)
                        {
                            imgFilter.setVisibility(View.VISIBLE);
                            llNoData.setVisibility(View.GONE);
                            darAdapter = new DARAdapter(listDAR);
                            rvDAR.setAdapter(darAdapter);

                            listActivityType = new ArrayList<CommonGetSet>();
                            listEmployee = new ArrayList<CommonGetSet>();

                            listActivityType.add(new CommonGetSet(0, "Select Activity Type"));
                            listEmployee.add(new CommonGetSet(0, "Select Employee"));

                            for (int i = 0; i < listDAR.size(); i++)
                            {
                                listActivityType.add(new CommonGetSet(listDAR.get(i).getActivity_type_id(), listDAR.get(i).getActivity_type_name()));
                                listEmployee.add(new CommonGetSet(listDAR.get(i).getEmployee_id(), listDAR.get(i).getFirst_name() + " " + listDAR.get(i).getLast_name()));
                            }

                            calculateTotalHoursAndMinutes(listDAR);
                            removeDuplicateValue();
                        }
                        else
                        {
                            imgFilter.setVisibility(View.GONE);
                            llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        imgFilter.setVisibility(View.GONE);
                        llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    imgFilter.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DARResponse> call, Throwable t)
            {
                imgFilter.setVisibility(View.GONE);
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void calculateTotalHoursAndMinutes(ArrayList<DARResponse.DataBean> list)
    {
        if (list.size() > 0)
        {
            try
            {
                int total = (sumHours(list) * 60) + sumMins(list);
                int hours = total / 60;
                int minutes = total % 60;

                tvTotalHour.setText("Total - " + hours + " Hours," + minutes + " Minutes");
                tvTotalHour.setVisibility(View.VISIBLE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                tvTotalHour.setVisibility(View.GONE);
            }
        }
        else
        {
            tvTotalHour.setVisibility(View.GONE);
        }
    }

    public int sumHours(ArrayList<DARResponse.DataBean> list)
    {
        int sum = 0;

        for (DARResponse.DataBean bean : list)
            sum = sum + bean.getTimeSpent();
        return sum;
    }

    public int sumMins(ArrayList<DARResponse.DataBean> list)
    {
        int sum = 0;
        for (DARResponse.DataBean bean : list)
            sum = sum + bean.getTimeSpent_Min();
        return sum;
    }


    private void removeDuplicateValue()
    {
        try
        {
            Set<CommonGetSet> s = new TreeSet<CommonGetSet>(new Comparator<CommonGetSet>()
            {
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
            s.addAll(listActivityType);
            listActivityType = new ArrayList<>(s);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            Set<CommonGetSet> emp = new TreeSet<CommonGetSet>(new Comparator<CommonGetSet>()
            {
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
                                }
                            }
                        }

                        if (response.body().getData().getMonth().size() > 0)
                        {
                            listMonth = new ArrayList<MonthYearResponse.DataBean.MonthBean>();

                            for (int i = 0; i < response.body().getData().getMonth().size(); i++)
                            {
                                listMonth.add(response.body().getData().getMonth().get(i));
                                if (response.body().getData().getMonth().get(i).getId() == Integer.parseInt(selectedMonth))
                                {
                                    tvMonth.setText(listMonth.get(i).getName());
                                }
                            }
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<MonthYearResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public class DARAdapter extends RecyclerView.Adapter<DARAdapter.ViewHolder>
    {
        List<DARResponse.DataBean> listItems;

        public DARAdapter(List<DARResponse.DataBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_dar_list_for_client_details, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final DARResponse.DataBean getSet = listItems.get(position);
                holder.tvEmployee.setText(getSet.getFirst_name() + " " + getSet.getLast_name());
                holder.tvTimeSpent.setText(getSet.getTimeSpent() + " Hours, " +getSet.getTimeSpent_Min() + " Minutes");
                holder.tvActivityType.setText(getSet.getActivity_type_name());
                holder.tvDARMessage.setText(getSet.getDar_message());
                holder.tvRemarks.setText(getSet.getRemarksComment());

                if(getSet.getReportDate() !=null && getSet.getReportDate().length()>0)
                {
                    holder.tvDate.setVisibility(View.VISIBLE);
                    holder.tvDate.setText(AppUtils.universalDateConvert(getSet.getReportDate(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "MMM dd,yyyy"));
                }
                else
                {
                    holder.tvDate.setVisibility(View.GONE);
                }

                if (getSet.getEmployee_id() == Integer.parseInt(sessionManager.getUserId()))
                {
                    holder.ivEdit.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivEdit.setVisibility(View.GONE);
                }

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, AddDarEntryActivity.class);
                            intent.putExtra("isFor","edit");
                            intent.putExtra("isEditClient",false);
                            intent.putExtra("clientId",clientId);
                            intent.putExtra("data", (Parcelable) getSet);
                            startActivity(intent);
                            AppUtils.startActivityAnimation(activity);
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
            @BindView(R.id.tvEmployee)
            TextView tvEmployee;
            @BindView(R.id.tvActivityType)
            TextView tvActivityType;
            @BindView(R.id.tvDate)
            TextView tvDate;
            @BindView(R.id.tvTimeSpent)
            TextView tvTimeSpent;
            @BindView(R.id.tvRemarks)
            TextView tvRemarks;
            @BindView(R.id.tvDARMessage)
            TextView tvDARMessage;
            @BindView(R.id.ivEdit)
            ImageView ivEdit;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    TextView tvClient, tvActivityType, tvEmployee;
    LinearLayout llSelectClient, llSelectActivityType, llSelectEmployee, llClear, llApply;

    private void showFilterDialog()
    {
        filterdialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        filterdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_dar_report_filter, null);
        filterdialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(filterdialog, activity);

        tvClient = sheetView.findViewById(R.id.tvClient);
        tvActivityType = sheetView.findViewById(R.id.tvActivityType);
        tvEmployee = sheetView.findViewById(R.id.tvEmployee);

        llSelectClient = sheetView.findViewById(R.id.llSelectClient);
        llSelectActivityType = sheetView.findViewById(R.id.llSelectActivityType);
        llSelectEmployee = sheetView.findViewById(R.id.llSelectEmployee);

        llClear = sheetView.findViewById(R.id.llClear);
        llApply = sheetView.findViewById(R.id.llApply);

        llSelectClient.setVisibility(View.GONE);

        if (selectedEmployeeName.length() > 0)
        {
            tvEmployee.setText(selectedEmployeeName);
        }

        if (selectedTypeName.length() > 0)
        {
            tvActivityType.setText(selectedTypeName);
        }

        llSelectActivityType.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showListDialog(TYPE);
            }
        });

        llSelectEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showListDialog(EMPLOYEE);
            }
        });

        llApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                listDARFilter = new ArrayList<DARResponse.DataBean>();

                try
                {

                    if (selectedTypeID.length() > 0 && selectedEmployeeId.length() > 0)
                    {
                        if (listDAR.size() > 0)
                        {
                            for (int i = 0; i < listDAR.size(); i++)
                            {
                                if (String.valueOf(listDAR.get(i).getActivity_type_id()).equals(selectedTypeID) && String.valueOf(listDAR.get(i).getEmployee_id()).equals(selectedEmployeeId))
                                {
                                    listDARFilter.add(listDAR.get(i));
                                }
                            }
                        }

                        if (listDARFilter.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            darAdapter = new DARAdapter(listDARFilter);
                            rvDAR.setAdapter(darAdapter);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                        calculateTotalHoursAndMinutes(listDARFilter);
                    }
                    else if (selectedTypeID.length() > 0)
                    {
                        if (listDAR.size() > 0)
                        {
                            for (int i = 0; i < listDAR.size(); i++)
                            {
                                if (String.valueOf(listDAR.get(i).getActivity_type_id()).equals(selectedTypeID))
                                {
                                    listDARFilter.add(listDAR.get(i));
                                }
                            }
                        }

                        if (listDARFilter.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            darAdapter = new DARAdapter(listDARFilter);
                            rvDAR.setAdapter(darAdapter);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }

                        calculateTotalHoursAndMinutes(listDARFilter);
                    }
                    else if (selectedEmployeeId.length() > 0)
                    {
                        if (listDAR.size() > 0)
                        {
                            for (int i = 0; i < listDAR.size(); i++)
                            {
                                if (String.valueOf(listDAR.get(i).getEmployee_id()).equals(selectedEmployeeId))
                                {
                                    listDARFilter.add(listDAR.get(i));
                                }
                            }
                        }


                        if (listDARFilter.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            darAdapter = new DARAdapter(listDARFilter);
                            rvDAR.setAdapter(darAdapter);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }

                        calculateTotalHoursAndMinutes(listDARFilter);
                    }
                    else
                    {
                        if (listDAR.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                            darAdapter = new DARAdapter(listDAR);
                            rvDAR.setAdapter(darAdapter);
                            calculateTotalHoursAndMinutes(listDAR);
                            selectedEmployeeId = "";
                            selectedEmployeeName = "";
                            selectedTypeID = "";
                            selectedTypeName = "";
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

                filterdialog.dismiss();
            }
        });

        llClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listDARFilter = new ArrayList<DARResponse.DataBean>();

                if (listDAR.size() > 0)
                {
                    llNoData.setVisibility(View.GONE);
                    darAdapter = new DARAdapter(listDAR);
                    rvDAR.setAdapter(darAdapter);
                    calculateTotalHoursAndMinutes(listDAR);
                    selectedEmployeeId = "";
                    selectedEmployeeName = "";
                    selectedTypeID = "";
                    selectedTypeName = "";
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                }

                filterdialog.dismiss();
            }
        });

        filterdialog.show();

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
                                getDARDetails();
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
                                getDARDetails();
                            }
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(TYPE))
            {
                final CommonGetSet getSet = listActivityType.get(position);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (getSet.getId() != 0)
                        {
                            tvActivityType.setText(getSet.getName());
                            selectedTypeID = String.valueOf(getSet.getId());
                            selectedTypeName = getSet.getName();
                        }
                        else
                        {
                            tvActivityType.setText(getSet.getName());
                            selectedTypeName = "";
                            selectedTypeID = "";
                        }
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                final CommonGetSet getSet = listEmployee.get(position);
                holder.tvValue.setText(getSet.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listDialog.dismiss();
                        if (getSet.getId() != 0)
                        {
                            tvEmployee.setText(getSet.getName());
                            selectedEmployeeId = String.valueOf(getSet.getId());
                            selectedEmployeeName = getSet.getName();
                        }
                        else
                        {
                            tvEmployee.setText(getSet.getName());
                            selectedEmployeeId = "";
                            selectedEmployeeName = "";
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
            else if (isFor.equalsIgnoreCase(TYPE))
            {
                return listActivityType.size();
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
    public void onDestroy()
    {
        super.onDestroy();
    }
}
