package cheetah.alphacapital.reportApp.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.ToDoListResponse;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.SaveTaskResponse;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;

@SuppressLint("NonConstantResourceId")
public class TaskListActivityNew extends BaseActivity
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
    @BindView(R.id.cvCard)
    CardView cvCard;
    @BindView(R.id.ivSerach)
    ImageView ivSerach;
    @BindView(R.id.ivClose)
    ImageView ivClose;
    @BindView(R.id.ivContactUs)
    ImageView ivContactUs;
    @BindView(R.id.llNotification)
    LinearLayout llNotification;
    @BindView(R.id.llFilter)
    LinearLayout llFilter;
    @BindView(R.id.llCalender)
    LinearLayout llCalender;
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
    @BindView(R.id.txtPinTaskCount)
    AppCompatTextView txtPinTaskCount;
    @BindView(R.id.ivPinnedTask)
    ImageView ivPinnedTask;
    @BindView(R.id.llPinnedTitle)
    LinearLayout llPinnedTitle;
    @BindView(R.id.rvPinnedTask)
    RecyclerView rvPinnedTask;
    @BindView(R.id.llPinnedTaskMain)
    LinearLayout llPinnedTaskMain;
    @BindView(R.id.viewLine)
    View viewLine;
    @BindView(R.id.txtAllTaskCount)
    AppCompatTextView txtAllTaskCount;
    @BindView(R.id.ivAllTask)
    ImageView ivAllTask;
    @BindView(R.id.llAllTitle)
    LinearLayout llAllTitle;
    @BindView(R.id.rvAllTask)
    RecyclerView rvAllTask;
    @BindView(R.id.llAllTaskMain)
    LinearLayout llAllTaskMain;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.llAddTask)
    AppCompatImageView llAddTask;
    @BindView(R.id.sfTaskList)
    SwipeRefreshLayout sfTaskList;

    private ArrayList<ToDoListResponse.DataBean.FilterEmployeeListBean> flt_Employee_list = new ArrayList<ToDoListResponse.DataBean.FilterEmployeeListBean>();
    private ArrayList<ToDoListResponse.DataBean.FilterClientListBean> flt_Client_list = new ArrayList<ToDoListResponse.DataBean.FilterClientListBean>();
    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClient = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();
    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClientSearch = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();

    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listAllTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listPinnedTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();

    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listAllTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listPinnedTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();

    private AllTaskListAdapter allTaskListAdapter;
    private PinnedTaskListAdapter pinnedTaskListAdapter;
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private EmployeeAdpater employeeAdpater;
    private BottomSheetDialog dialog_Add_Task;
    private TextView txtAddReminder, txtClientName;
    private HashTagHelper hashTagHelper;
    public static String selectedReminderDate = "", selectedReminderTime = "";
    private String finalDateTime = "";
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    private EmployeeAdpaterEdit employeeAdpaterEdit;
    private BottomSheetDialog dialog_Edit_Task;
    private TextView txtAddReminderEdit, txtClientNameEdit;
    private HashTagHelper hashTagHelperEdit;
    public static String selectedReminderDateEdit = "", selectedReminderTimeEdit = "";
    private String finalDateTimeEdit = "";
    String date_timeEdit = "";
    int mYearEdit;
    int mMonthEdit;
    int mDayEdit;

    private String flt_employeeName = "", flt_employeeIds = "", flt_clientIds = "", flt_clientName = "", flt_view_task_Name = "", flt_view_task_Ids = "";
    private String TaskStatusId = "1", add_task_clientIds = "", add_task_clientName = "";

    private boolean isStatusBarHidden = false;
    private Timer timer = new Timer();
    private final long DELAY = 400;

    public static TextView txtFromDate, txtToDate;
    private String fromdate_filter = "", todate_filter = "";
    private boolean isFilterApply = false;
    private String fromdate = "", todate = "";

    BottomSheetDialog otherFilterDialog, listDialog;
    private final String EMPLOYEE = "Employee";
    private final String CLIENT = "Client";
    private final String CLIENT_ADD = "Add Client";
    private final String CLIENT_EDIT = "Edit Client";
    private final String CLIENT_SEARCH = "Search Client";
    private final String VIEWTASKOF = "View Task Of";
    private boolean isCurrentUserTask = false;

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

        setContentView(R.layout.activity_task_list_new);

        ButterKnife.bind(this);

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                getAllTask(true);
                llNoInternet.setVisibility(View.GONE);
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
            ivLogout.setVisibility(View.VISIBLE);
            ivLogout.setImageResource(R.drawable.t_sort_green);
            ivSerach.setVisibility(View.VISIBLE);
            llCalender.setVisibility(View.VISIBLE);
            llFilter.setVisibility(View.VISIBLE);

            rvPinnedTask.setLayoutManager(new LinearLayoutManager(activity));
            rvAllTask.setLayoutManager(new LinearLayoutManager(activity));
            /*setSupportActionBar(toolbar);

            ivHeader.setImageResource(R.drawable.img_portfolio);

            int height = 56;

            if (isStatusBarHidden)
            {
                height = 56 + 25;
                viewStatusBar.setVisibility(View.INVISIBLE);
            }
            else
            {
                viewStatusBar.setVisibility(View.GONE);
            }

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
            params.height = (int) AppUtils.pxFromDp(activity, height);
            ivHeader.setLayoutParams(params);*/

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Task List");
            llBackNavigation.setVisibility(View.VISIBLE);
            tvNoDataText.setText("No Task Found.");

            flt_view_task_Ids = sessionManager.getUserId();
            isCurrentUserTask = true;
            llAddTask.setVisibility(View.VISIBLE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        sfTaskList.setOnRefreshListener(() -> getAllTask(true));

        llBackNavigation.setOnClickListener(v ->
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
        });

        llRetry.setOnClickListener(view ->
        {
            try
            {
                if (sessionManager.isNetworkAvailable())
                {
                    llNoInternet.setVisibility(View.GONE);
                    getAllTask(true);
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
        });

        llPinnedTitle.setOnClickListener(view ->
        {
            if (rvPinnedTask.getVisibility() == View.VISIBLE)
            {
                rvPinnedTask.setVisibility(View.GONE);
                ivPinnedTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_right_blue));
            }
            else
            {
                rvPinnedTask.setVisibility(View.VISIBLE);
                ivPinnedTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_down_blue));
            }
        });

        llAllTitle.setOnClickListener(view ->
        {
            if (rvAllTask.getVisibility() == View.VISIBLE)
            {
                rvAllTask.setVisibility(View.GONE);
                ivAllTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_right_blue));
            }
            else
            {
                rvAllTask.setVisibility(View.VISIBLE);
                ivAllTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_down_blue));
            }
        });

        llAddTask.setOnClickListener(v ->
        {
            add_task_clientIds = "";
            add_task_clientName = "";
            showAddTaskDialog();
        });

        ivLogout.setOnClickListener(v ->
        {
            try
            {
                showStatusFilterDialog();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llFilter.setOnClickListener(v ->
        {
            try
            {
                showOtherFilterDialog();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llCalender.setOnClickListener(view ->
        {
            try
            {
                showCalenderDialog();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        ivSerach.setOnClickListener(v -> doSearch());

        ivClose.setOnClickListener(v -> closeSearch());

        edtSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                try
                {
                    String search_Text = s.toString().trim();

                    if (search_Text.length() > 0)
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
                                                //   listTask_search = new ArrayList<>();
                                                listAllTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
                                                listPinnedTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();

                                                if (listAllTask != null && listAllTask.size() > 0)
                                                {
                                                    for (int i = 0; i < listAllTask.size(); i++)
                                                    {
                                                        final String text = AppUtils.getCapitalText(listAllTask.get(i).getTask_message());
                                                        ;
                                                        String cs1 = AppUtils.getCapitalText(String.valueOf(search_Text));

                                                        if (text.contains(cs1))
                                                        {
                                                            listAllTaskSearch.add(listAllTask.get(i));
                                                        }
                                                    }
                                                }

                                                if (listPinnedTask != null && listPinnedTask.size() > 0)
                                                {
                                                    for (int i = 0; i < listPinnedTask.size(); i++)
                                                    {
                                                        final String text = AppUtils.getCapitalText(listPinnedTask.get(i).getTask_message());
                                                        ;
                                                        String cs1 = AppUtils.getCapitalText(String.valueOf(search_Text));

                                                        if (text.contains(cs1))
                                                        {
                                                            listPinnedTaskSearch.add(listPinnedTask.get(i));
                                                        }
                                                    }
                                                }


                                                if (listPinnedTaskSearch.size() == 0 && listAllTaskSearch.size() == 0)
                                                {
                                                    llNoData.setVisibility(View.VISIBLE);
                                                }
                                                else
                                                {
                                                    llNoData.setVisibility(View.GONE);

                                                    if (listPinnedTaskSearch.size() > 0)
                                                    {
                                                        llPinnedTaskMain.setVisibility(View.VISIBLE);
                                                        pinnedTaskListAdapter = new PinnedTaskListAdapter(listPinnedTaskSearch);
                                                        rvPinnedTask.setAdapter(pinnedTaskListAdapter);
                                                    }
                                                    else
                                                    {
                                                        llPinnedTaskMain.setVisibility(View.GONE);
                                                    }

                                                    if (listAllTaskSearch.size() > 0)
                                                    {
                                                        llAllTaskMain.setVisibility(View.VISIBLE);
                                                        allTaskListAdapter = new AllTaskListAdapter(listAllTaskSearch);
                                                        rvAllTask.setAdapter(allTaskListAdapter);
                                                    }
                                                    else
                                                    {
                                                        llAllTaskMain.setVisibility(View.GONE);
                                                    }
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
                        try
                        {
                            timer.cancel();
                            timer = new Timer();

                            if (listPinnedTask.size() == 0 && listAllTask.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.GONE);

                                if (listPinnedTask.size() > 0)
                                {
                                    llPinnedTaskMain.setVisibility(View.VISIBLE);
                                    pinnedTaskListAdapter = new PinnedTaskListAdapter(listPinnedTask);
                                    rvPinnedTask.setAdapter(pinnedTaskListAdapter);
                                }
                                else
                                {
                                    llPinnedTaskMain.setVisibility(View.GONE);
                                }

                                if (listAllTask.size() > 0)
                                {
                                    llAllTaskMain.setVisibility(View.VISIBLE);
                                    allTaskListAdapter = new AllTaskListAdapter(listAllTask);
                                    rvAllTask.setAdapter(allTaskListAdapter);
                                }
                                else
                                {
                                    llAllTaskMain.setVisibility(View.GONE);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {
            }
        });
    }

    private void doSearch()
    {
        txtTitle.setVisibility(View.GONE);
        llCalender.setVisibility(View.GONE);
        llFilter.setVisibility(View.GONE);
        ivSerach.setVisibility(View.GONE);
        ivLogout.setVisibility(View.GONE);
        edtSearch.setVisibility(View.VISIBLE);
        cvCard.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.VISIBLE);
        //MitsUtils.openKeyboard(edtSearch, activity);
    }

    private void closeSearch()
    {
        edtSearch.setText("");
        txtTitle.setVisibility(View.VISIBLE);
        llCalender.setVisibility(View.VISIBLE);
        llFilter.setVisibility(View.VISIBLE);
        ivLogout.setVisibility(View.VISIBLE);
        ivSerach.setVisibility(View.VISIBLE);
        edtSearch.setVisibility(View.GONE);
        cvCard.setVisibility(View.GONE);
        ivClose.setVisibility(View.GONE);
        //MitsUtils.hideKeyboard(activity);
    }

    private void getAllTask(boolean isFirstTime)
    {
        if (isFirstTime)
        {
            llLoading.setVisibility(View.VISIBLE);
        }

        if (DashboardActivity.handler != null)
        {
            Message message = Message.obtain();
            message.what = 13;
            DashboardActivity.handler.sendMessage(message);
        }

        String finalFromDate = "", finalToDate = "";
        if (fromdate_filter.length() > 0 && todate_filter.length() > 0)
        {
            finalFromDate = fromdate_filter + " 12:00 AM";
            finalToDate = todate_filter + " 12:00 AM";
        }

        Call<ToDoListResponse> call = apiService.getAllTaskFromApi(TaskStatusId, flt_view_task_Ids, flt_clientIds, flt_employeeIds, finalFromDate, finalToDate, "0", "1000");

        call.enqueue(new Callback<ToDoListResponse>()
        {
            @Override
            public void onResponse(Call<ToDoListResponse> call, Response<ToDoListResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            listPinnedTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
                            listAllTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
                            flt_Employee_list = new ArrayList<ToDoListResponse.DataBean.FilterEmployeeListBean>();
                            flt_Client_list = new ArrayList<ToDoListResponse.DataBean.FilterClientListBean>();
                            listPinnedTask.addAll(response.body().getData().getPinnedTaskList());
                            listAllTask.addAll(response.body().getData().getTaskList());

                            if (listPinnedTask.size() > 0)
                            {
                                for (int i = 0; i < listPinnedTask.size(); i++)
                                {
                                    if (listPinnedTask.get(i).getLstclient().size() > 0)
                                    {
                                        listPinnedTask.get(i).setAll_Client_Name(AppUtils.arrayToString(listPinnedTask.get(i).getLstclient()));
                                    }

                                }
                            }

                            if (listAllTask.size() > 0)
                            {
                                for (int i = 0; i < listAllTask.size(); i++)
                                {
                                    if (listAllTask.get(i).getLstclient().size() > 0)
                                    {
                                        listAllTask.get(i).setAll_Client_Name(AppUtils.arrayToString(listAllTask.get(i).getLstclient()));
                                    }
                                }
                            }

                            flt_Employee_list.addAll(response.body().getData().getFilterEmployeeList());
                            flt_Client_list.addAll(response.body().getData().getFilterClientList());

                            if (listPinnedTask.size() == 0 && listAllTask.size() == 0)
                            {
                                llNoData.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                llNoData.setVisibility(View.GONE);

                                if (listPinnedTask.size() > 0)
                                {
                                    llPinnedTaskMain.setVisibility(View.VISIBLE);
                                    pinnedTaskListAdapter = new PinnedTaskListAdapter(listPinnedTask);
                                    rvPinnedTask.setAdapter(pinnedTaskListAdapter);
                                }
                                else
                                {
                                    llPinnedTaskMain.setVisibility(View.GONE);
                                }

                                if (listAllTask.size() > 0)
                                {
                                    llAllTaskMain.setVisibility(View.VISIBLE);
                                    allTaskListAdapter = new AllTaskListAdapter(listAllTask);
                                    rvAllTask.setAdapter(allTaskListAdapter);
                                }
                                else
                                {
                                    llAllTaskMain.setVisibility(View.GONE);
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        if (listEmployee.size() > 0)
                        {
                            if (sessionManager.getClientList().length() == 0)
                            {
                                getAllClientByEmployeeFromApi();
                            }
                            else
                            {
                                llLoading.setVisibility(View.GONE);
                                if (sessionManager.getClientList().length() > 0)
                                {
                                    listClient = new Gson().fromJson(sessionManager.getClientList(), new TypeToken<List<NewClientListResponse.DataBean.AllClientByEmployeeBean>>()
                                    {
                                    }.getType());
                                    Log.e("<><><>", "getDataFromSession: " + listClient.size());
                                }
                            }
                        }
                        else
                        {
                            getAllEmployee();
                        }
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
                        llLoading.setVisibility(View.GONE);
                    }
                }
                else
                {
                    llNoData.setVisibility(View.VISIBLE);
                    AppUtils.apiFailedSnackBar(activity);
                    llLoading.setVisibility(View.GONE);
                }
                if (sfTaskList.isRefreshing())
                {
                    sfTaskList.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ToDoListResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void getAllEmployee()
    {
        Call<AllEmployeeResponse> call = apiService.getAllEmployee(flt_clientIds, "0", "0");
        call.enqueue(new Callback<AllEmployeeResponse>()
        {
            @Override
            public void onResponse(Call<AllEmployeeResponse> call, Response<AllEmployeeResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
                            listEmployee.addAll(response.body().getData().getAllEmployee());

                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                String toDisplayName = AppUtils.toDisplayCase(listEmployee.get(i).getFirst_name() + "" + listEmployee.get(i).getLast_name());
                                listEmployee.get(i).setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                            }
                        }

                        if (sessionManager.getClientList().length() == 0)
                        {
                            getAllClientByEmployeeFromApi();
                        }
                        else
                        {
                            llLoading.setVisibility(View.GONE);
                            if (sessionManager.getClientList().length() > 0)
                            {
                                listClient = new Gson().fromJson(sessionManager.getClientList(), new TypeToken<List<NewClientListResponse.DataBean.AllClientByEmployeeBean>>()
                                {
                                }.getType());
                                Log.e("<><><>", "getDataFromSession: " + listClient.size());
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
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void getAllClientByEmployeeFromApi()
    {
        Call<NewClientListResponse> call = apiService.getAllClientByEmployee_new("0", "0", sessionManager.getUserId());
        call.enqueue(new Callback<NewClientListResponse>()
        {
            @Override
            public void onResponse(Call<NewClientListResponse> call, Response<NewClientListResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            listClient = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();
                            listClient.addAll(response.body().getData().getAllClientByEmployee());
                            sessionManager.setClientList(new Gson().toJson(listClient));
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NewClientListResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
                llLoading.setVisibility(View.GONE);
            }
        });
    }

    public class PinnedTaskListAdapter extends RecyclerView.Adapter<PinnedTaskListAdapter.ViewHolder>
    {

        private ArrayList<ToDoListResponse.DataBean.TaskListBean> items;

        PinnedTaskListAdapter(ArrayList<ToDoListResponse.DataBean.TaskListBean> list)
        {
            this.items = list;
        }

        @Override
        public PinnedTaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_list_for_client_details, viewGroup, false);
            return new PinnedTaskListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final PinnedTaskListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ToDoListResponse.DataBean.TaskListBean taskListGetSet = items.get(position);

                if (listAllTask.size() > 0)
                {
                    if (position == items.size() - 1)
                    {
                        holder.viewLine.setVisibility(View.GONE);
                    }
                    else
                    {
                        holder.viewLine.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                if (taskListGetSet.getAll_Client_Name().length() > 0)
                {
                    if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " for " + taskListGetSet.getAll_Client_Name() + " is due on " + AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "dd-MM-yyyy HH:mm:ss", "dd MMM,yyyy"))));
                    }
                    else
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " for " + taskListGetSet.getAll_Client_Name())));
                    }
                }
                else
                {
                    if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " is due on " + AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "dd-MM-yyyy HH:mm:ss", "dd MMM,yyyy"))));
                    }
                    else
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message())));
                    }
                }

                if (taskListGetSet.getTask_status_id() == 2)
                {
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                if (isCurrentUserTask)
                {
                    holder.ivAction.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivAction.setVisibility(View.GONE);
                }

                holder.ivAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showTaskActionDialog(taskListGetSet, position);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, TaskDetailsActivityNew.class);
                            intent.putExtra("task_id", String.valueOf(taskListGetSet.getId()));
                            intent.putExtra("task_added_by", taskListGetSet.getTask_added_by());
                            startActivity(intent);
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
            return items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.txtTaskName)
            TextView txtTaskName;
            @BindView(R.id.txtUnReadCount)
            TextView txtUnReadCount;
            @BindView(R.id.ivPinTask)
            ImageView ivPinTask;
            @BindView(R.id.ivAction)
            ImageView ivAction;
            @BindView(R.id.viewLine)
            View viewLine;
            @BindView(R.id.llMain)
            LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
                //  ivPinTask.setVisibility(View.VISIBLE);
            }
        }
    }

    public class AllTaskListAdapter extends RecyclerView.Adapter<AllTaskListAdapter.ViewHolder>
    {
        private ArrayList<ToDoListResponse.DataBean.TaskListBean> items;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

        AllTaskListAdapter(ArrayList<ToDoListResponse.DataBean.TaskListBean> list)
        {
            this.items = list;
            viewBinderHelper.setOpenOnlyOne(true);
        }

        @Override
        public AllTaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_list_for_client_details, viewGroup, false);
            return new AllTaskListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final AllTaskListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final ToDoListResponse.DataBean.TaskListBean taskListGetSet = items.get(position);

                if (taskListGetSet.getAll_Client_Name().length() > 0)
                {
                    if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " for " + taskListGetSet.getAll_Client_Name() + " is due on " + AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "MM/dd/yyyy HH:mm:ss a", "dd MMM,yyyy"))));
                    }
                    else
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " for " + taskListGetSet.getAll_Client_Name())));
                    }
                }
                else
                {
                    if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message() + " is due on " + AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "dd-MM-yyyy HH:mm:ss", "dd MMM,yyyy"))));
                    }
                    else
                    {
                        holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message())));
                    }
                }

                if (taskListGetSet.getTask_status_id() == 2)
                {
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }

                if (isCurrentUserTask)
                {
                    holder.ivAction.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivAction.setVisibility(View.GONE);
                }

                holder.ivAction.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showTaskActionDialog(taskListGetSet, position);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            Intent intent = new Intent(activity, TaskDetailsActivityNew.class);
                            intent.putExtra("task_id", String.valueOf(taskListGetSet.getId()));
                            intent.putExtra("task_added_by", taskListGetSet.getTask_added_by());
                            startActivity(intent);
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
            return items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.txtTaskName)
            TextView txtTaskName;
            @BindView(R.id.txtUnReadCount)
            TextView txtUnReadCount;
            @BindView(R.id.ivPinTask)
            ImageView ivPinTask;
            @BindView(R.id.ivAction)
            ImageView ivAction;
            @BindView(R.id.viewLine)
            View viewLine;
            @BindView(R.id.llMain)
            LinearLayout llMain;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    public void showAddTaskDialog()
    {
        dialog_Add_Task = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        dialog_Add_Task.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog_Add_Task.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_edit_task, null);
        dialog_Add_Task.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);
        AppUtils.setLightStatusBarBottomDialog(dialog_Add_Task, activity);
        final LinearLayout llMain;
        final RecyclerView rvEmployeeList;
        final ImageView ivSetReminder, ivAddClient;
        final CustomEditText edtTaskName;
        final TextView txtAddTask;

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        rvEmployeeList = (RecyclerView) sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = (ImageView) sheetView.findViewById(R.id.ivSetReminder);

        edtTaskName = (CustomEditText) sheetView.findViewById(R.id.edtTaskName);
        txtAddReminder = (TextView) sheetView.findViewById(R.id.txtAddReminder);
        txtClientName = (TextView) sheetView.findViewById(R.id.txtClientName);
        ivAddClient = (ImageView) sheetView.findViewById(R.id.ivAddClient);
        txtAddTask = (TextView) sheetView.findViewById(R.id.txtAddTask);
        ivAddClient.setVisibility(View.VISIBLE);

        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        hashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(activity, R.color.colorAccent), null);
        hashTagHelper.handle(edtTaskName);

        for (int i = 0; i < listClient.size(); i++)
        {
            listClient.get(i).setSelected(false);
        }

        dialog_Add_Task.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
            }
        });

        edtTaskName.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, int count)
            {
                try
                {
                    if (s.toString().trim().length() == 0)
                    {
                        rvEmployeeList.setVisibility(View.GONE);
                        txtAddReminder.setVisibility(View.GONE);
                        txtAddReminder.setText("");
                        finalDateTime = "";
                        selectedReminderDate = "";
                        selectedReminderTime = "";
                        return;
                    }

                    final String hashtag = AppUtils.getValidAPIStringResponse(AppUtils.getHashtagFromString(s.toString(), start));

                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

                        if (listEmployee != null && listEmployee.size() > 0)
                        {
                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                final String text = listEmployee.get(i).getFullname();

                                String text1 = AppUtils.getCapitalText(text);

                                String cs1 = AppUtils.getCapitalText(String.valueOf(hashtag));

                                if (text1.contains(cs1))
                                {
                                    listEmployee_Search.add(listEmployee.get(i));
                                }
                            }

                            try
                            {
                                if (listEmployee_Search.size() > 0)
                                {
                                    employeeAdpater = new EmployeeAdpater(listEmployee_Search, hashtag, start, edtTaskName, rvEmployeeList);
                                    rvEmployeeList.setAdapter(employeeAdpater);
                                    rvEmployeeList.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    rvEmployeeList.setVisibility(View.GONE);
                                }
                            }
                            catch (Exception e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            rvEmployeeList.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        rvEmployeeList.setVisibility(View.GONE);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                /*  if (editable.toString().startsWith("#"))
                {
                    String result = editable.toString().replaceAll(" " ,"");
                    if(!editable.toString().equals(result))
                    {
                        edtAssignedToAndCategory.setText(result);
                        edtAssignedToAndCategory.setSelection(result.length());
                    }
                }*/
            }
        });

        ivSetReminder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                datePicker(edtTaskName);
            }
        });

        ivAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(CLIENT_ADD);
            }
        });

        txtAddTask.setOnClickListener(view ->
        {
            try
            {
                AppUtils.hideKeyboard(edtTaskName, activity);

                dialog_Add_Task.dismiss();

                if (sessionManager.isNetworkAvailable())
                {
                    if (edtTaskName.getText().toString().trim().length() > 0)
                    {
                        ToDoListResponse.DataBean.TaskListBean getSet = new ToDoListResponse.DataBean.TaskListBean();
                        getSet.setTask_message(edtTaskName.getText().toString().trim());
                        getSet.setDue_date(finalDateTime);
                        getSet.setEmployee_id(Integer.valueOf(sessionManager.getUserId()));
                        listAllTask.add(0,getSet);
                        allTaskListAdapter.notifyDataSetChanged();
                        rvEmployeeList.setVisibility(View.GONE);

                        String contact = getAllContact(false);

                        if (contact.startsWith(","))
                        {
                            contact = contact.substring(1);
                        }

                        Log.e("*** contact", contact);

                        saveTask(edtTaskName.getText().toString().trim(), contact, finalDateTime);

                        edtTaskName.setText("");
                        txtAddReminder.setVisibility(View.GONE);
                        finalDateTime = "";
                        selectedReminderDate = "";
                        selectedReminderTime = "";
                        txtAddReminder.setText("");
                        txtClientName.setText("");
                        add_task_clientIds = "";
                        add_task_clientName = "";
                    }
                    else
                    {
                        Toast.makeText(activity, "Please enter add task.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        edtTaskName.requestFocus();

        dialog_Add_Task.show();
    }

    private class EmployeeAdpater extends RecyclerView.Adapter<EmployeeAdpater.ViewHolder>
    {
        private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> items;
        private String hashtag = "";
        private int end = 0;
        private EditText editText;
        private RecyclerView recyclerView;

        EmployeeAdpater(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list, final String hashtag, final int start, final EditText editText, final RecyclerView recyclerView)
        {
            this.items = list;
            this.hashtag = hashtag;
            this.end = start;
            this.editText = editText;
            this.recyclerView = recyclerView;
        }

        @Override
        public EmployeeAdpater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_emplyee_to_do_list, viewGroup, false);
            return new EmployeeAdpater.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EmployeeAdpater.ViewHolder holder, final int position)
        {
            final AllEmployeeResponse.DataBean.AllEmployeeBean employeeBean = items.get(position);
            try
            {
                holder.txtName.setText(AppUtils.toDisplayCase(employeeBean.getFirst_name()) + " " + AppUtils.toDisplayCase(employeeBean.getLast_name()));

                if (employeeBean.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(employeeBean.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(employeeBean.getFirst_name() + " " + employeeBean.getLast_name())));

                if (position == items.size() - 1)
                {
                    holder.viewline.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewline.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        String edtTextString = editText.getText().toString();
                        int start = end - (hashtag.length() - 1);
                        System.out.println(start);
                        String startText = edtTextString.substring(0, start);
                        String endText = edtTextString.substring(end + 1, edtTextString.length());
                        System.out.println("start : " + startText);
                        System.out.println("end : " + endText);
                        String finaltext = startText + employeeBean.getFullname() + endText;
                        System.out.println(" " + finaltext);
                        int cursorposition = start + employeeBean.getFullname().length();
                        editText.setText(finaltext);
                        editText.setSelection(cursorposition);

                        AllEmployeeResponse.DataBean.AllEmployeeBean allEmployeeBean = new AllEmployeeResponse.DataBean.AllEmployeeBean();
                        allEmployeeBean.setFullname(employeeBean.getFullname());
                        allEmployeeBean.setFirst_name(employeeBean.getFirst_name());
                        allEmployeeBean.setLast_name(employeeBean.getLast_name());
                        allEmployeeBean.setEmail(employeeBean.getEmail());
                        allEmployeeBean.setId(employeeBean.getId());
                        listEmployee_Selected.add(allEmployeeBean);
                        recyclerView.setVisibility(View.GONE);
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
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView txtName;
            final TextView txtUserSortName;
            final TextView txtEmail;
            final View viewline;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                txtUserSortName = (TextView) convertView.findViewById(R.id.txtUserSortName);
                txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
                viewline = (View) convertView.findViewById(R.id.viewline);
            }
        }
    }

    private void datePicker(final EditText edtTaskName)
    {
        Log.e("showDate ReminderDate:", selectedReminderDate.toString());

        if (selectedReminderDate.length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(selectedReminderDate.toString(), "dd MMM yyyy", "dd/MM/yyyy");
                String[] datearr = date.split("/");
                mDay = Integer.parseInt(datearr[0]);
                mMonth = Integer.parseInt(datearr[1]);
                mMonth = mMonth - 1;
                mYear = Integer.parseInt(datearr[2]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                view.setMinDate(new Date().getTime());

                date_time = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat df = new SimpleDateFormat("dd MMM yyyy");
                Date convertedDate2 = new Date();
                try
                {
                    convertedDate2 = dateFormat.parse(date_time);
                    String showDate = df.format(convertedDate2);
                    Log.e("showDate", showDate.toString());
                    selectedReminderDate = showDate;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                showTimePickerDialog(edtTaskName);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void showTimePickerDialog(final EditText edtTaskName)
    {
        try
        {
            final Calendar mcurrentTime = Calendar.getInstance();

            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);

            int minute = mcurrentTime.get(Calendar.MINUTE);

            if (!selectedReminderTime.toString().equals(""))
            {
                try
                {
                    String time = selectedReminderTime.toString().trim();
                    boolean isPm = false;

                    if (time.contains("AM"))
                    {
                        time = time.replace("AM", "").trim();
                        isPm = false;
                    }
                    else if (time.contains("PM"))
                    {
                        time = time.replace("PM", "").trim();
                        isPm = true;
                    }

                    String[] splitedTime = time.split(":");

                    hour = Integer.parseInt(splitedTime[0]);
                    minute = Integer.parseInt(splitedTime[1]);

                    Log.e("selectedHour before ", hour + "");

                    if (isPm)
                    {
                        if (hour != 12 && hour < 12)
                        {
                            hour = hour + 12;
                        }
                        else
                        {
                            hour = hour;
                        }
                    }
                    else
                    {
                        hour = hour;
                    }

                    Log.e("selectedHour after ", hour + "");
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                }
            }
            else
            {
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
            }

            final RangeTimePickerDialog mTimePicker;

            mTimePicker = new RangeTimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                {

                    String AM_PM;

                    Log.e("selectedHour select", selectedHour + "");

                    if (selectedHour < 12)
                    {
                        AM_PM = "AM";
                    }
                    else
                    {
                        AM_PM = "PM";

                        selectedHour = selectedHour - 12;

                        if (selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                    }

                    String newHour = "", newMinute = "";

                    if (selectedHour <= 9)
                    {
                        newHour = "0" + selectedHour;
                    }
                    else
                    {
                        newHour = String.valueOf(selectedHour);
                    }

                    if (selectedMinute <= 9)
                    {
                        newMinute = "0" + selectedMinute;
                    }
                    else
                    {
                        newMinute = String.valueOf(selectedMinute);
                    }

                    String selectedTime = newHour + ":" + newMinute + " " + AM_PM;

                    selectedReminderTime = selectedTime;

                    finalDateTime = AppUtils.universalDateConvert(selectedReminderDate.toString().trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTime.toString().trim();

                    Log.e("*** Final Date select", finalDateTime + "");

                    if (finalDateTime.length() > 0)
                    {
                        txtAddReminder.setVisibility(View.VISIBLE);
                        txtAddReminder.setText(selectedReminderDate + " " + selectedReminderTime);
                        AppUtils.showKeyboard(edtTaskName, activity);
                    }

                }
            }, hour, minute, false);

            //true = 24 hour time

            mTimePicker.setTitle("Select Time");

            mTimePicker.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private String getAllContact(boolean isForDialog)
    {
        List<String> allHashTags = null;

        allHashTags = hashTagHelper.getAllContact();

        String hashtags = "", contactFinal = "", contactSelected = "";

        try
        {
            for (int i = 0; i < allHashTags.size(); i++)
            {
                if (!allHashTags.get(i).startsWith("@"))
                {
                    // ignore if doesn't start with hash
                    continue;
                }

                if (hashtags.contains(allHashTags.get(i)))
                {
                    // ignore if duplicate
                    continue;
                }
                if (i == 0)
                {
                    hashtags = "" + AppUtils.getValidAPIStringResponse(allHashTags.get(i));
                }
                else
                {
                    hashtags = hashtags + "," + AppUtils.getValidAPIStringResponse(allHashTags.get(i));
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            List<String> items = Arrays.asList(hashtags.split("\\s*,\\s*"));

            for (int i = 0; i < items.size(); i++)
            {
                Log.e("<><> Check Du : ", items.get(i).toString());
            }


            if (listEmployee_Selected.size() > 0 && items.size() > 0)
            {
                for (int i = 0; i < listEmployee_Selected.size(); i++)
                {
                    for (int j = 0; j < items.size(); j++)
                    {
                        if (listEmployee_Selected.get(i).getFullname().trim().equalsIgnoreCase(items.get(j).trim()))
                        {
                            if (contactFinal.length() == 0)
                            {
                                contactFinal = String.valueOf(listEmployee_Selected.get(i).getId());
                            }
                            else
                            {
                                contactFinal = contactFinal + "," + listEmployee_Selected.get(i).getId() + "";
                            }
                        }
                    }
                }
            }

            if (contactFinal.length() > 0)
            {
                List<String> items_contact = Arrays.asList(contactFinal.split("\\s*,\\s*"));

                for (int i = 0; i < items_contact.size(); i++)
                {
                    if (contactSelected.length() > 0)
                    {
                        if (!contactSelected.contains(items_contact.get(i)))
                        {
                            contactSelected = contactSelected + "," + items_contact.get(i).toString().trim();
                        }
                    }
                    else
                    {
                        contactSelected = items_contact.get(i).toString().trim();
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return contactSelected;
    }

    private void saveTask(String taskName, String contact, String finalDateTime)
    {
        Call<SaveTaskResponse> call = apiService.addTask(AppUtils.getEmployeeIdForAdmin(activity), contact, "1", taskName, finalDateTime, add_task_clientIds);

        call.enqueue(new Callback<SaveTaskResponse>()
        {
            @Override
            public void onResponse(Call<SaveTaskResponse> call, Response<SaveTaskResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            getAllTask(false);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<SaveTaskResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public void showEditTaskDialog(final ToDoListResponse.DataBean.TaskListBean getSet, int pos)
    {
        dialog_Edit_Task = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        dialog_Edit_Task.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog_Edit_Task.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_edit_task, null);
        dialog_Edit_Task.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);
        AppUtils.setLightStatusBarBottomDialog(dialog_Edit_Task, activity);
        final LinearLayout llMain;
        final RecyclerView rvEmployeeList;
        final ImageView ivSetReminder, ivAddClient;
        final CustomEditText edtTaskName;
        final TextView txtAddTask;

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        rvEmployeeList = (RecyclerView) sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = (ImageView) sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = (CustomEditText) sheetView.findViewById(R.id.edtTaskName);
        txtAddReminderEdit = (TextView) sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = (TextView) sheetView.findViewById(R.id.txtAddTask);
        txtClientNameEdit = (TextView) sheetView.findViewById(R.id.txtClientName);
        ivAddClient = (ImageView) sheetView.findViewById(R.id.ivAddClient);
        ivAddClient.setVisibility(View.VISIBLE);


        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        hashTagHelperEdit = HashTagHelper.Creator.create(ContextCompat.getColor(activity, R.color.colorAccent), null);
        hashTagHelperEdit.handle(edtTaskName);

        edtTaskName.setText(getSet.getTask_message().toString().trim());

        try
        {
            if (getSet.getTask_message().toString().trim().length() > 0)
            {
                edtTaskName.setSelection(getSet.getTask_message().toString().trim().length());
            }
        }
        catch (Exception e)
        {
            edtTaskName.setSelection(0);
            e.printStackTrace();
        }

        if (getSet.getDue_date() != null && getSet.getDue_date().length() > 0)
        {
            txtAddReminderEdit.setVisibility(View.VISIBLE);
            txtAddReminderEdit.setText(AppUtils.universalDateConvert(getSet.getDue_date(), "dd-MM-yyyy hh:mm:ss", "dd MMM yyyy hh:mm a"));
            selectedReminderDateEdit = AppUtils.universalDateConvert(getSet.getDue_date(), "dd-MM-yyyy hh:mm:ss", "dd MMM yyyy");
            selectedReminderTimeEdit = AppUtils.universalDateConvert(getSet.getDue_date(), "dd-MM-yyyy hh:mm:ss", "hh:mm a");
            finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.toString().trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.toString().trim();
            Log.e("*** Final Date select", finalDateTimeEdit + "");
        }
        else
        {
            txtAddReminderEdit.setVisibility(View.GONE);
        }


        Log.e("<><> Client List : ", getSet.getLstclient().size() + " ");

        for (int i = 0; i < listClient.size(); i++)
        {
            for (int j = 0; j < getSet.getLstclient().size(); j++)
            {
                if (listClient.get(i).getId() == getSet.getLstclient().get(j).getClient_id())
                {
                    Log.e("<><> Client List : ", listClient.get(i).getId() + " == " + getSet.getLstclient().get(j).getClient_id());
                    listClient.get(i).setSelected(true);
                }
            }
        }

        if (getSet.getAll_Client_Name().length() > 0)
        {
            txtClientNameEdit.setVisibility(View.VISIBLE);
            txtClientNameEdit.setText(getSet.getAll_Client_Name());
        }
        else
        {
            txtClientNameEdit.setVisibility(View.GONE);
        }

        listEmployee_Selected_Edit = new ArrayList<>();
        if (getSet.getLstemployee().size() > 0)
        {
            for (int i = 0; i < getSet.getLstemployee().size(); i++)
            {
                if (!AppUtils.getEmployeeIdForAdmin(activity).equalsIgnoreCase(String.valueOf(getSet.getLstemployee().get(i).getEmployee_id())))
                {
                    AllEmployeeResponse.DataBean.AllEmployeeBean assignedClientGetSetOld1 = new AllEmployeeResponse.DataBean.AllEmployeeBean();
                    String toDisplayName = AppUtils.toDisplayCase(getSet.getLstemployee().get(i).getEmployee_name());
                    assignedClientGetSetOld1.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                    assignedClientGetSetOld1.setId(getSet.getLstemployee().get(i).getEmployee_id());
                    listEmployee_Selected_Edit.add(assignedClientGetSetOld1);
                }
            }
        }


        dialog_Edit_Task.setOnDismissListener(dialogInterface ->
        {
            //MitsUtils.hideKeyboard(activity);
             AppUtils.hideKeyboard(edtTaskName, activity);
        });


        ivAddClient.setOnClickListener(v -> showListDialog(CLIENT_EDIT));

        edtTaskName.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, int count)
            {
                try
                {
                    if (s.toString().trim().length() == 0)
                    {
                        rvEmployeeList.setVisibility(View.GONE);
                        return;
                    }


                    //  String str = String.valueOf(s.toString().charAt(start));

                    final String hashtag = AppUtils.getValidAPIStringResponse(AppUtils.getHashtagFromString(s.toString(), start));

                    Log.e("<><> HASHTAG LENTH :: ", hashtag.toString() + "");


                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

                        if (listEmployee != null && listEmployee.size() > 0)
                        {
                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                final String text = listEmployee.get(i).getFullname();

                                String text1 = AppUtils.getCapitalText(text);

                                String cs1 = AppUtils.getCapitalText(String.valueOf(hashtag));

                                if (text1.contains(cs1))
                                {
                                    listEmployee_Search.add(listEmployee.get(i));
                                }
                            }

                            try
                            {
                                if (listEmployee_Search.size() > 0)
                                {
                                    employeeAdpaterEdit = new EmployeeAdpaterEdit(listEmployee_Search, hashtag, start, edtTaskName, rvEmployeeList);
                                    rvEmployeeList.setAdapter(employeeAdpaterEdit);
                                    rvEmployeeList.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    rvEmployeeList.setVisibility(View.GONE);
                                }
                            }
                            catch (Exception e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            rvEmployeeList.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        rvEmployeeList.setVisibility(View.GONE);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                /*  if (editable.toString().startsWith("#"))
                {
                    String result = editable.toString().replaceAll(" " ,"");
                    if(!editable.toString().equals(result))
                    {
                        edtAssignedToAndCategory.setText(result);
                        edtAssignedToAndCategory.setSelection(result.length());
                    }
                }*/
            }
        });

        ivSetReminder.setOnClickListener(view -> datePickerEdit(edtTaskName));

        txtAddTask.setOnClickListener(view ->
        {
            try
            {
                AppUtils.hideKeyboard(edtTaskName, activity);

                dialog_Edit_Task.dismiss();

                if (sessionManager.isNetworkAvailable())
                {
                    if (edtTaskName.getText().toString().trim().length() > 0)
                    {
                        listAllTask.get(pos).setTask_message(edtTaskName.getText().toString().trim());
                        listAllTask.get(pos).setDue_date(finalDateTime);
                        listAllTask.get(pos).setEmployee_id(Integer.valueOf(sessionManager.getUserId()));
                        listAllTask.get(pos).setId(getSet.getId());
                        allTaskListAdapter.notifyDataSetChanged();

                        rvEmployeeList.setVisibility(View.GONE);

                        String contact = getAllContactEdit(false);

                        if (contact.startsWith(","))
                        {
                            contact = contact.substring(1);
                        }

                        Log.e("*** contact", contact);

                        updateTask(edtTaskName.getText().toString().trim(), contact, finalDateTimeEdit, getSet.getId(), "1", add_task_clientIds);

                        edtTaskName.setText("");
                        txtAddReminderEdit.setVisibility(View.GONE);
                        finalDateTimeEdit = "";
                        selectedReminderDateEdit = "";
                        selectedReminderTimeEdit = "";
                        txtAddReminderEdit.setText("");
                    }
                    else
                    {
                        Toast.makeText(activity, "Please enter add task.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        dialog_Edit_Task.show();

        edtTaskName.requestFocus();
    }

    private class EmployeeAdpaterEdit extends RecyclerView.Adapter<EmployeeAdpaterEdit.ViewHolder>
    {
        private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> items;
        private String hashtag = "";
        private int end = 0;
        private EditText editText;
        private RecyclerView recyclerView;

        EmployeeAdpaterEdit(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list, final String hashtag, final int start, final EditText editText, final RecyclerView recyclerView)
        {
            this.items = list;
            this.hashtag = hashtag;
            this.end = start;
            this.editText = editText;
            this.recyclerView = recyclerView;
        }

        @Override
        public EmployeeAdpaterEdit.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_emplyee_to_do_list, viewGroup, false);
            return new EmployeeAdpaterEdit.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final EmployeeAdpaterEdit.ViewHolder holder, final int position)
        {
            final AllEmployeeResponse.DataBean.AllEmployeeBean assignedClientGetSetOld = items.get(position);
            try
            {
                holder.txtName.setText(AppUtils.toDisplayCase(assignedClientGetSetOld.getFirst_name()) + " " + AppUtils.toDisplayCase(assignedClientGetSetOld.getLast_name()));

                if (assignedClientGetSetOld.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(assignedClientGetSetOld.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(assignedClientGetSetOld.getFirst_name() + " " + assignedClientGetSetOld.getLast_name())));

                if (position == items.size() - 1)
                {
                    holder.viewline.setVisibility(View.GONE);
                }
                else
                {
                    holder.viewline.setVisibility(View.VISIBLE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        String edtTextString = editText.getText().toString();
                        int start = end - (hashtag.length() - 1);
                        System.out.println(start);
                        String startText = edtTextString.substring(0, start);
                        String endText = edtTextString.substring(end + 1, edtTextString.length());
                        System.out.println("start : " + startText);
                        System.out.println("end : " + endText);
                        String finaltext = startText + assignedClientGetSetOld.getFullname() + endText;
                        System.out.println(" " + finaltext);
                        int cursorposition = start + assignedClientGetSetOld.getFullname().length();
                        editText.setText(finaltext);
                        editText.setSelection(cursorposition);

                        AllEmployeeResponse.DataBean.AllEmployeeBean assignedClientGetSetOld1 = new AllEmployeeResponse.DataBean.AllEmployeeBean();
                        assignedClientGetSetOld1.setFullname(assignedClientGetSetOld.getFullname());
                        assignedClientGetSetOld1.setFirst_name(assignedClientGetSetOld.getFirst_name());
                        assignedClientGetSetOld1.setLast_name(assignedClientGetSetOld.getLast_name());
                        assignedClientGetSetOld1.setEmail(assignedClientGetSetOld.getEmail());
                        assignedClientGetSetOld1.setId(assignedClientGetSetOld.getId());
                        listEmployee_Selected_Edit.add(assignedClientGetSetOld1);
                        recyclerView.setVisibility(View.GONE);

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
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView txtName;
            final TextView txtUserSortName;
            final TextView txtEmail;
            final View viewline;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                txtUserSortName = (TextView) convertView.findViewById(R.id.txtUserSortName);
                txtEmail = (TextView) convertView.findViewById(R.id.txtEmail);
                viewline = (View) convertView.findViewById(R.id.viewline);
            }
        }
    }

    private String getAllContactEdit(boolean isForDialog)
    {
        List<String> allHashTags = null;

        allHashTags = hashTagHelperEdit.getAllContact();

        String hashtags = "", contactFinal = "", contactSelected = "";

        try
        {
            for (int i = 0; i < allHashTags.size(); i++)
            {
                if (!allHashTags.get(i).startsWith("@"))
                {
                    // ignore if doesn't start with hash
                    continue;
                }

                if (hashtags.contains(allHashTags.get(i)))
                {
                    // ignore if duplicate
                    continue;
                }
                if (i == 0)
                {
                    hashtags = "" + AppUtils.getValidAPIStringResponse(allHashTags.get(i));
                }
                else
                {
                    hashtags = hashtags + "," + AppUtils.getValidAPIStringResponse(allHashTags.get(i));
                }
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            List<String> items = Arrays.asList(hashtags.split("\\s*,\\s*"));

            for (int i = 0; i < items.size(); i++)
            {
                Log.e("<><> Check Du : ", items.get(i).toString());
            }


            if (listEmployee_Selected_Edit.size() > 0 && items.size() > 0)
            {
                for (int i = 0; i < listEmployee_Selected_Edit.size(); i++)
                {
                    for (int j = 0; j < items.size(); j++)
                    {
                        if (listEmployee_Selected_Edit.get(i).getFullname().trim().equalsIgnoreCase(items.get(j).trim()))
                        {
                            if (contactFinal.length() == 0)
                            {
                                contactFinal = String.valueOf(listEmployee_Selected_Edit.get(i).getId());
                            }
                            else
                            {
                                contactFinal = contactFinal + "," + listEmployee_Selected_Edit.get(i).getId() + "";
                            }
                        }
                    }
                }
            }

            if (contactFinal.length() > 0)
            {
                List<String> items_contact = Arrays.asList(contactFinal.split("\\s*,\\s*"));

                for (int i = 0; i < items_contact.size(); i++)
                {
                    if (contactSelected.length() > 0)
                    {
                        if (!contactSelected.contains(items_contact.get(i)))
                        {
                            contactSelected = contactSelected + "," + items_contact.get(i).toString().trim();
                        }
                    }
                    else
                    {
                        contactSelected = items_contact.get(i).toString().trim();
                    }

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return contactSelected;
    }

    private void datePickerEdit(final EditText edtTaskName)
    {

        Log.e("showDate ReminderDate:", selectedReminderDateEdit.toString());

        if (selectedReminderDateEdit.length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(selectedReminderDateEdit.toString(), "dd MMM yyyy", "dd/MM/yyyy");
                String[] datearr = date.split("/");
                mDayEdit = Integer.parseInt(datearr[0]);
                mMonthEdit = Integer.parseInt(datearr[1]);
                mMonthEdit = mMonthEdit - 1;
                mYearEdit = Integer.parseInt(datearr[2]);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                final Calendar c = Calendar.getInstance();
                mYearEdit = c.get(Calendar.YEAR);
                mMonthEdit = c.get(Calendar.MONTH);
                mDayEdit = c.get(Calendar.DAY_OF_MONTH);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        // Get Current Date
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                view.setMinDate(new Date().getTime());

                date_timeEdit = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat df = new SimpleDateFormat("dd MMM yyyy");
                Date convertedDate2 = new Date();
                try
                {
                    convertedDate2 = dateFormat.parse(date_timeEdit);
                    String showDate = df.format(convertedDate2);
                    Log.e("showDate", showDate.toString());
                    selectedReminderDateEdit = showDate;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                showTimePickerDialogEdit(edtTaskName);
            }
        }, mYearEdit, mMonthEdit, mDayEdit);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    public void showTimePickerDialogEdit(final EditText edtTaskName)
    {
        try
        {

            final Calendar mcurrentTime = Calendar.getInstance();

            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);

            int minute = mcurrentTime.get(Calendar.MINUTE);

            if (!selectedReminderTimeEdit.toString().equals(""))
            {
                try
                {
                    String time = selectedReminderTimeEdit.toString().trim();
                    boolean isPm = false;

                    if (time.contains("AM"))
                    {
                        time = time.replace("AM", "").trim();
                        isPm = false;
                    }
                    else if (time.contains("PM"))
                    {
                        time = time.replace("PM", "").trim();
                        isPm = true;
                    }

                    String[] splitedTime = time.split(":");

                    hour = Integer.parseInt(splitedTime[0]);
                    minute = Integer.parseInt(splitedTime[1]);

                    Log.e("selectedHour before ", hour + "");

                    if (isPm)
                    {
                        if (hour != 12 && hour < 12)
                        {
                            hour = hour + 12;
                        }
                        else
                        {
                            hour = hour;
                        }
                    }
                    else
                    {
                        hour = hour;
                    }

                    Log.e("selectedHour after ", hour + "");
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                }
            }
            else
            {
                hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
            }

            final RangeTimePickerDialog mTimePicker;

            mTimePicker = new RangeTimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener()
            {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
                {

                    String AM_PM;

                    Log.e("selectedHour select", selectedHour + "");

                    if (selectedHour < 12)
                    {
                        AM_PM = "AM";
                    }
                    else
                    {
                        AM_PM = "PM";

                        selectedHour = selectedHour - 12;

                        if (selectedHour == 0)
                        {
                            selectedHour = 12;
                        }
                    }

                    String newHour = "", newMinute = "";

                    if (selectedHour <= 9)
                    {
                        newHour = "0" + selectedHour;
                    }
                    else
                    {
                        newHour = String.valueOf(selectedHour);
                    }

                    if (selectedMinute <= 9)
                    {
                        newMinute = "0" + selectedMinute;
                    }
                    else
                    {
                        newMinute = String.valueOf(selectedMinute);
                    }

                    String selectedTime = newHour + ":" + newMinute + " " + AM_PM;

                    selectedReminderTimeEdit = selectedTime;

                    finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.toString().trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.toString().trim();

                    Log.e("*** Final Date select", finalDateTimeEdit + "");

                    if (finalDateTimeEdit.length() > 0)
                    {
                        txtAddReminderEdit.setVisibility(View.VISIBLE);
                        txtAddReminderEdit.setText(selectedReminderDateEdit + " " + selectedReminderTimeEdit);

                        AppUtils.showKeyboard(edtTaskName, activity);
                    }

                }
            }, hour, minute, false);

            //true = 24 hour time

            mTimePicker.setTitle("Select Time");

            mTimePicker.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateTask(String taskName, String contact, String finalDateTime, final int id, final String task_status_id, final String add_task_clientIds_param)
    {
        Call<SaveTaskResponse> call = apiService.updateTask(String.valueOf(id), AppUtils.getEmployeeIdForAdmin(activity), contact, task_status_id, taskName, finalDateTime, add_task_clientIds_param);

        call.enqueue(new Callback<SaveTaskResponse>()
        {
            @Override
            public void onResponse(Call<SaveTaskResponse> call, Response<SaveTaskResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            getAllTask(false);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<SaveTaskResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public void selectDeleteDialog(final ToDoListResponse.DataBean.TaskListBean getSet, final int pos)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView txtNo;
        final TextView txtYes;
        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);
        tvTitle = (TextView) sheetView.findViewById(R.id.tvTitle);
        tvDescription = (TextView) sheetView.findViewById(R.id.tvDescription);
        txtNo = (TextView) sheetView.findViewById(R.id.txtNo);
        txtYes = (TextView) sheetView.findViewById(R.id.txtYes);
        tvTitle.setText("Delete Task");
        tvDescription.setText("Are you sure want to remove this task?");


        llCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        llRemove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    bottomSheetDialog.dismiss();
                    bottomSheetDialog.cancel();
                    listAllTask.remove(getSet);
                    allTaskListAdapter.notifyDataSetChanged();
                    removeTask(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeTask(final ToDoListResponse.DataBean.TaskListBean taskListGetSet, final int pos)
    {

        Call<CommonResponse> call = apiService.deleteTask(String.valueOf(taskListGetSet.getId()), AppUtils.getEmployeeIdForAdmin(activity));

        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            getAllTask(false);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void pinTask(final ToDoListResponse.DataBean.TaskListBean taskListGetSet, final int pos)
    {
        String isPin = "";

        if (taskListGetSet.isIsPinedTask())
        {
            isPin = String.valueOf(false);
        }
        else
        {
            isPin = String.valueOf(true);
        }

        Call<SaveTaskResponse> call = apiService.pinTask("0", String.valueOf(taskListGetSet.getId()), AppUtils.getEmployeeIdForAdmin(activity), isPin);

        call.enqueue(new Callback<SaveTaskResponse>()
        {
            @Override
            public void onResponse(Call<SaveTaskResponse> call, Response<SaveTaskResponse> response)
            {
                if (response.isSuccessful())
                {
                    try
                    {
                        if (response.body().isSuccess())
                        {
                            getAllTask(true);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<SaveTaskResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public void showStatusFilterDialog()
    {
        BottomSheetDialog dialog_filter = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        dialog_filter.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog_filter.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_filter_task_new, null);
        dialog_filter.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);
        AppUtils.setLightStatusBarBottomDialog(dialog_filter, activity);
        TextView txtAll;
        ImageView ivAll;
        TextView txtOpen;
        ImageView ivOpen;
        TextView txtCompleted;
        ImageView ivCompleted;

        txtAll = (TextView) sheetView.findViewById(R.id.txtAll);
        ivAll = (ImageView) sheetView.findViewById(R.id.ivAll);

        txtOpen = (TextView) sheetView.findViewById(R.id.txtOpen);
        ivOpen = (ImageView) sheetView.findViewById(R.id.ivOpen);
        txtCompleted = (TextView) sheetView.findViewById(R.id.txtCompleted);
        ivCompleted = (ImageView) sheetView.findViewById(R.id.ivCompleted);


        if (TaskStatusId.equalsIgnoreCase("0"))
        {
            ivAll.setVisibility(View.VISIBLE);
            ivOpen.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.GONE);
        }
        else if (TaskStatusId.equalsIgnoreCase("1"))
        {
            ivAll.setVisibility(View.GONE);
            ivOpen.setVisibility(View.VISIBLE);
            ivCompleted.setVisibility(View.GONE);
        }
        else if (TaskStatusId.equalsIgnoreCase("2"))
        {
            ivAll.setVisibility(View.GONE);
            ivOpen.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.VISIBLE);
        }
        else
        {
            ivAll.setVisibility(View.GONE);
            ivOpen.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.GONE);
        }

        dialog_filter.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
            }
        });

        txtAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    TaskStatusId = "0";
                    dialog_filter.dismiss();
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true);

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
        });

        txtOpen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {

                    TaskStatusId = "1";
                    dialog_filter.dismiss();
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true);
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
        });

        txtCompleted.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    TaskStatusId = "2";
                    dialog_filter.dismiss();
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true);
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
        });


        dialog_filter.show();
    }

    public void showTaskActionDialog(ToDoListResponse.DataBean.TaskListBean taskListGetSet, final int pos)
    {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_task_action, null);
        dialog.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);

        AppUtils.setLightStatusBarBottomDialog(dialog, activity);

        final LinearLayout llMain;
        final LinearLayout llEdit;
        final LinearLayout llDelete;
        final LinearLayout llComplete;
        final LinearLayout llPin;
        final TextView txtPin;
        final ImageView ivPin;

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        llEdit = (LinearLayout) sheetView.findViewById(R.id.llEdit);
        llDelete = (LinearLayout) sheetView.findViewById(R.id.llDelete);
        llComplete = (LinearLayout) sheetView.findViewById(R.id.llComplete);
        llPin = (LinearLayout) sheetView.findViewById(R.id.llPin);
        ivPin = (ImageView) sheetView.findViewById(R.id.ivPin);
        txtPin = (TextView) sheetView.findViewById(R.id.txtPin);
        llPin.setVisibility(View.VISIBLE);


        if (taskListGetSet.getTask_status_id() == 2)
        {
            llPin.setVisibility(View.VISIBLE);
            llEdit.setVisibility(View.GONE);
            llComplete.setVisibility(View.GONE);
        }
        else
        {
            llPin.setVisibility(View.VISIBLE);
            llEdit.setVisibility(View.VISIBLE);
            llComplete.setVisibility(View.VISIBLE);
        }

        if (taskListGetSet.getEmployee_id() == Integer.parseInt(sessionManager.getUserId()))
        {
            llDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            llDelete.setVisibility(View.GONE);
        }

        if (taskListGetSet.isIsPinedTask())
        {
            txtPin.setText("UnPin Task");
            ivPin.setImageResource(R.drawable.t_unpin_white);
        }
        else
        {
            txtPin.setText("Pin Task");
            ivPin.setImageResource(R.drawable.t_pin_white);
        }

        dialog.setOnDismissListener(dialogInterface -> AppUtils.hideKeyboard(edtSearch,activity));

        llEdit.setOnClickListener(v ->
        {
            try
            {
                dialog.dismiss();
                showEditTaskDialog(taskListGetSet,pos);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    dialog.dismiss();
                    selectDeleteDialog(taskListGetSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llPin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                pinTask(taskListGetSet, pos);
            }
        });

        llComplete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();

                String add_task_client = "";

                for (int i = 0; i < taskListGetSet.getLstclient().size(); i++)
                {
                    if (add_task_client.length() == 0)
                    {
                        add_task_client = String.valueOf(taskListGetSet.getLstclient().get(i).getClient_id());
                    }
                    else
                    {
                        add_task_client = add_task_client + "," + String.valueOf(taskListGetSet.getLstclient().get(i).getClient_id());
                    }
                }

                if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                {
                    updateTask(taskListGetSet.getTask_message().toString().trim(), taskListGetSet.getAll_employee_ids(), AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"), taskListGetSet.getId(), "2", add_task_client);
                }
                else
                {
                    updateTask(taskListGetSet.getTask_message().toString().trim(), taskListGetSet.getAll_employee_ids(), "", taskListGetSet.getId(), "2", add_task_client);
                }
            }
        });


        dialog.show();
    }

    TextView tvClient, tvEmployee, tvViewTaskOf;
    LinearLayout llSelectClient, llSelectEmployee, llSelectViewTaskOf, llClear, llApply;

    private void showOtherFilterDialog()
    {
        otherFilterDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        otherFilterDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_dar_report_filter, null);
        otherFilterDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(otherFilterDialog, activity);


        LinearLayout llSelectActivityType = sheetView.findViewById(R.id.llSelectActivityType);
        llSelectActivityType.setVisibility(View.GONE);

        tvClient = sheetView.findViewById(R.id.tvClient);
        tvEmployee = sheetView.findViewById(R.id.tvEmployee);
        tvViewTaskOf = sheetView.findViewById(R.id.tvViewTaskOf);
        llSelectClient = sheetView.findViewById(R.id.llSelectClient);
        llSelectEmployee = sheetView.findViewById(R.id.llSelectEmployee);
        llSelectViewTaskOf = sheetView.findViewById(R.id.llSelectViewTaskOf);
        llClear = sheetView.findViewById(R.id.llClear);
        llApply = sheetView.findViewById(R.id.llApply);

        if (listEmployee.size() > 0)
        {
            llSelectViewTaskOf.setVisibility(View.VISIBLE);
        }
        else
        {
            llSelectViewTaskOf.setVisibility(View.GONE);
        }

        if (flt_Employee_list.size() > 0)
        {
            llSelectEmployee.setVisibility(View.VISIBLE);
        }
        else
        {
            llSelectEmployee.setVisibility(View.GONE);
        }

        if (flt_Client_list.size() > 0)
        {
            llSelectClient.setVisibility(View.VISIBLE);
        }
        else
        {
            llSelectClient.setVisibility(View.GONE);
        }

        if (flt_employeeName.length() > 0)
        {
            tvEmployee.setText(flt_employeeName);
        }

        if (flt_clientName.length() > 0)
        {
            tvClient.setText(flt_clientName);
        }

        if (flt_view_task_Name.length() > 0)
        {
            tvViewTaskOf.setText(flt_view_task_Name);
        }

        llSelectEmployee.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showListDialog(EMPLOYEE);
            }
        });

        llSelectClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showListDialog(CLIENT);
            }
        });

        llSelectViewTaskOf.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showListDialog(VIEWTASKOF);
            }
        });

        llApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.e("<><> Filter flt_employeeName ", flt_employeeName + " End");
                Log.e("<><> Filter flt_employeeIds ", flt_employeeIds + " End");
                Log.e("<><> Filter flt_clientIds ", flt_clientIds + " End");
                Log.e("<><> Filter flt_clientName ", flt_clientName + " End");
                Log.e("<><> Filter flt_view_task_Name ", flt_view_task_Name + " End");
                Log.e("<><> Filter flt_view_task_Ids ", flt_view_task_Ids + " End");

                getAllTask(true);

                otherFilterDialog.dismiss();
            }
        });

        llClear.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                flt_employeeName = "";
                flt_employeeIds = "";
                flt_clientIds = "";
                flt_clientName = "";
                flt_view_task_Name = "";
                flt_view_task_Ids = sessionManager.getUserId();
                isCurrentUserTask = true;
                llAddTask.setVisibility(View.VISIBLE);
                getAllTask(true);

                otherFilterDialog.dismiss();
            }
        });

        otherFilterDialog.show();
    }

    DialogListAdapter dialogListAdapter;
    DialogListForViewTaskAdapter dialogListForViewTaskAdapter;

    public void showListDialog(final String isFor)
    {
        listDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
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
        TextView btnYes = (TextView) listDialog.findViewById(R.id.btnYes);
        TextView tvTitle = (TextView) listDialog.findViewById(R.id.tvTitle);
        EditText edtSearch = (EditText) listDialog.findViewById(R.id.edtSearch);
        TextView tvDone = (TextView) listDialog.findViewById(R.id.tvDone);
        final RecyclerView rvListDialog = (RecyclerView) listDialog.findViewById(R.id.rvDialog);
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        tvDone.setVisibility(View.GONE);
        tvTitle.setText("Select " + isFor);
        btnNo.setText("Clear");


        if (isFor.equals(CLIENT_ADD) || isFor.equals(CLIENT_EDIT))
        {
            edtSearch.setVisibility(View.VISIBLE);
            edtSearch.addTextChangedListener(new TextWatcher()
            {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    String serachText = AppUtils.getCapitalText(cs.toString().trim());
                    if (serachText.length() > 0)
                    {
                        listClientSearch = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();
                        for (int i = 0; i < listClient.size(); i++)
                        {
                            String name = AppUtils.getCapitalText(listClient.get(i).getFirst_name()) + " " + AppUtils.getCapitalText(listClient.get(i).getLast_name());
                            if (name.contains(serachText))
                            {
                                listClientSearch.add(listClient.get(i));
                            }

                            if (listClientSearch.size() > 0)
                            {
                                dialogListAdapter = new DialogListAdapter(CLIENT_SEARCH);
                                rvListDialog.setAdapter(dialogListAdapter);
                            }
                        }
                    }
                    else
                    {
                        dialogListAdapter = new DialogListAdapter(isFor);
                        rvListDialog.setAdapter(dialogListAdapter);
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
        else
        {
            edtSearch.setVisibility(View.GONE);
        }

        if (isFor.equals(EMPLOYEE) || isFor.equals(CLIENT) || isFor.equals(CLIENT_ADD) || isFor.equals(CLIENT_EDIT))
        {
            btnYes.setVisibility(View.VISIBLE);
        }
        else
        {
            btnYes.setVisibility(View.GONE);
        }

        if (isFor.equals(EMPLOYEE) || isFor.equals(CLIENT) || isFor.equals(CLIENT_ADD) || isFor.equals(CLIENT_EDIT))
        {
            dialogListAdapter = new DialogListAdapter(isFor);
            rvListDialog.setAdapter(dialogListAdapter);
        }
        else
        {
            dialogListForViewTaskAdapter = new DialogListForViewTaskAdapter(isFor);
            rvListDialog.setAdapter(dialogListForViewTaskAdapter);
        }

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();


                if (isFor.equals(EMPLOYEE))
                {
                    flt_employeeIds = "";
                    flt_employeeName = "";
                    tvEmployee.setText("Select Employee");
                    for (int i = 0; i < flt_Employee_list.size(); i++)
                    {
                        flt_Employee_list.get(i).setSelected(false);
                    }
                }
                else if (isFor.equals(CLIENT))
                {
                    flt_clientIds = "";
                    flt_clientName = "";
                    tvClient.setText("Select Client");
                    for (int i = 0; i < flt_Client_list.size(); i++)
                    {
                        flt_Client_list.get(i).setSelected(false);
                    }
                }
                else if (isFor.equals(CLIENT_ADD))
                {
                    add_task_clientIds = "";
                    add_task_clientName = "";
                    txtClientName.setVisibility(View.GONE);
                    txtClientName.setText("");

                    for (int i = 0; i < listClient.size(); i++)
                    {
                        if (listClient.get(i).isSelected())
                        {
                            listClient.get(i).setSelected(false);
                        }
                    }
                }

            }

        });

        btnYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                try
                {
                    if (isFor.equals(EMPLOYEE))
                    {
                        flt_employeeIds = "";
                        flt_employeeName = "";
                        for (int i = 0; i < flt_Employee_list.size(); i++)
                        {
                            if (flt_Employee_list.get(i).isSelected())
                            {
                                if (flt_employeeIds.length() == 0)
                                {
                                    flt_employeeIds = String.valueOf(flt_Employee_list.get(i).getEmployee_id());
                                    flt_employeeName = flt_Employee_list.get(i).getEmployee_name();
                                }
                                else
                                {
                                    flt_employeeIds = flt_employeeIds + "," + String.valueOf(flt_Employee_list.get(i).getEmployee_id());
                                    flt_employeeName = flt_employeeName + "," + flt_Employee_list.get(i).getEmployee_name();
                                }
                            }
                        }

                        if (flt_employeeIds.length() > 0)
                        {
                            tvEmployee.setText(flt_employeeName);
                        }
                        else
                        {
                            tvEmployee.setText("Select Employee");
                            flt_employeeIds = "";
                            flt_employeeName = "";
                        }
                    }
                    else if (isFor.equals(CLIENT))
                    {
                        flt_clientIds = "";
                        flt_clientName = "";
                        for (int i = 0; i < flt_Client_list.size(); i++)
                        {
                            if (flt_Client_list.get(i).isSelected())
                            {
                                if (flt_clientIds.length() == 0)
                                {
                                    flt_clientIds = String.valueOf(flt_Client_list.get(i).getClient_id());
                                    flt_clientName = flt_Client_list.get(i).getClient_name();
                                }
                                else
                                {
                                    flt_clientIds = flt_clientIds + "," + String.valueOf(flt_Client_list.get(i).getClient_id());
                                    flt_clientName = flt_clientName + "," + flt_Client_list.get(i).getClient_name();
                                }
                            }
                        }

                        if (flt_clientIds.length() > 0)
                        {
                            tvClient.setText(flt_clientName);
                        }
                        else
                        {
                            tvClient.setText("Select Client");
                            flt_clientIds = "";
                            flt_clientName = "";
                        }
                    }
                    else if (isFor.equals(CLIENT_ADD))
                    {
                        edtSearch.setText("");
                        add_task_clientIds = "";
                        add_task_clientName = "";

                        for (int i = 0; i < listClient.size(); i++)
                        {
                            if (listClient.get(i).isSelected())
                            {
                                if (add_task_clientIds.length() == 0)
                                {
                                    add_task_clientIds = String.valueOf(listClient.get(i).getId());
                                    add_task_clientName = listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();
                                }
                                else
                                {
                                    add_task_clientIds = add_task_clientIds + "," + String.valueOf(listClient.get(i).getId());
                                    add_task_clientName = add_task_clientName + "," + listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();
                                }
                            }
                        }

                        if (add_task_clientIds.length() > 0)
                        {
                            txtClientName.setVisibility(View.VISIBLE);
                            txtClientName.setText(add_task_clientName);
                        }
                        else
                        {
                            txtClientName.setVisibility(View.GONE);
                        }
                    }
                    else if (isFor.equals(CLIENT_EDIT))
                    {
                        edtSearch.setText("");
                        add_task_clientIds = "";
                        add_task_clientName = "";

                        for (int i = 0; i < listClient.size(); i++)
                        {
                            if (listClient.get(i).isSelected())
                            {
                                if (add_task_clientIds.length() == 0)
                                {
                                    add_task_clientIds = String.valueOf(listClient.get(i).getId());
                                    add_task_clientName = listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();
                                }
                                else
                                {
                                    add_task_clientIds = add_task_clientIds + "," + String.valueOf(listClient.get(i).getId());
                                    add_task_clientName = add_task_clientName + "," + listClient.get(i).getFirst_name() + " " + listClient.get(i).getLast_name();
                                }
                            }
                        }

                        if (add_task_clientIds.length() > 0)
                        {
                            txtClientNameEdit.setVisibility(View.VISIBLE);
                            txtClientNameEdit.setText(add_task_clientName);
                        }
                        else
                        {
                            txtClientNameEdit.setVisibility(View.GONE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_to_do_filter, parent, false);
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

            if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                ToDoListResponse.DataBean.FilterEmployeeListBean filterEmployeeListBean = flt_Employee_list.get(position);
                holder.txtName.setText(AppUtils.toDisplayCase(filterEmployeeListBean.getEmployee_name()));

                if (filterEmployeeListBean.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(filterEmployeeListBean.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(filterEmployeeListBean.getEmployee_name())));

                if (filterEmployeeListBean.isSelected())
                {
                    holder.checkBox.setChecked(true);

                }
                else
                {
                    holder.checkBox.setChecked(false);
                }


                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // listDialog.dismiss();

                        if (filterEmployeeListBean.isSelected())
                        {
                            holder.checkBox.setChecked(false);
                            filterEmployeeListBean.setSelected(false);

                        }
                        else
                        {
                            holder.checkBox.setChecked(true);
                            filterEmployeeListBean.setSelected(true);
                        }

                       /* if (filterEmployeeListBean.getEmployee_id() != 0)
                        {
                            tvEmployee.setText(filterEmployeeListBean.getEmployee_name());
                            flt_employeeIds = String.valueOf(filterEmployeeListBean.getEmployee_id());
                            flt_employeeName = filterEmployeeListBean.getEmployee_name();
                        }
                        else
                        {
                            tvEmployee.setText(filterEmployeeListBean.getEmployee_name());
                            flt_employeeIds = "";
                            flt_employeeName = "";
                        }*/
                    }
                });
            }
            else if (isFor.equalsIgnoreCase(CLIENT))
            {
                final ToDoListResponse.DataBean.FilterClientListBean getSet = flt_Client_list.get(position);
                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getClient_name()));

                if (getSet.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getClient_name())));

                if (getSet.isSelected())
                {
                    holder.checkBox.setChecked(true);

                }
                else
                {
                    holder.checkBox.setChecked(false);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                     /*   listDialog.dismiss();
                        if (getSet.getClient_id() != 0)
                        {
                            tvClient.setText(getSet.getClient_name());
                            flt_clientIds = String.valueOf(getSet.getClient_id());
                            flt_clientName = getSet.getClient_name();
                        }
                        else
                        {
                            tvClient.setText(getSet.getClient_name());
                            flt_clientIds = "";
                            flt_clientName = "";
                        }*/

                        if (getSet.isSelected())
                        {
                            holder.checkBox.setChecked(false);
                            getSet.setSelected(false);

                        }
                        else
                        {
                            holder.checkBox.setChecked(true);
                            getSet.setSelected(true);
                        }
                    }
                });
            }
            else if (isFor.equals(CLIENT_ADD))
            {
                final NewClientListResponse.DataBean.AllClientByEmployeeBean getSet = listClient.get(position);
                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));


                if (getSet.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()) + " " + AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()))));

                if (getSet.isSelected())
                {
                    holder.checkBox.setChecked(true);
                }
                else
                {
                    holder.checkBox.setChecked(false);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (getSet.isSelected())
                        {
                            holder.checkBox.setChecked(false);
                            getSet.setSelected(false);
                        }
                        else
                        {
                            holder.checkBox.setChecked(true);
                            getSet.setSelected(true);
                        }
                    }
                });
            }
            else if (isFor.equals(CLIENT_EDIT))
            {
                final NewClientListResponse.DataBean.AllClientByEmployeeBean getSet = listClient.get(position);
                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));

                if (getSet.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()) + " " + AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()))));

                Log.e("<><> Selected : ", getSet.getId() + " == " + getSet.isSelected() + "");

                if (getSet.isSelected())
                {

                    holder.checkBox.setChecked(true);
                }
                else
                {
                    holder.checkBox.setChecked(false);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (getSet.isSelected())
                        {
                            holder.checkBox.setChecked(false);
                            getSet.setSelected(false);
                        }
                        else
                        {
                            holder.checkBox.setChecked(true);
                            getSet.setSelected(true);
                        }
                    }
                });
            }
            else if (isFor.equals(CLIENT_SEARCH))
            {
                final NewClientListResponse.DataBean.AllClientByEmployeeBean getSet = listClientSearch.get(position);
                holder.txtName.setText(AppUtils.toDisplayCase(getSet.getFirst_name() + " " + getSet.getLast_name()));


                if (getSet.getEmail().length() > 0)
                {
                    holder.txtEmail.setVisibility(View.VISIBLE);
                    holder.txtEmail.setText(getSet.getEmail());
                }
                else
                {
                    holder.txtEmail.setVisibility(View.GONE);
                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()) + " " + AppUtils.getSortName(AppUtils.toDisplayCase(getSet.getFirst_name()))));

                if (getSet.isSelected())
                {
                    holder.checkBox.setChecked(true);
                }
                else
                {
                    holder.checkBox.setChecked(false);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (getSet.isSelected())
                        {
                            holder.checkBox.setChecked(false);
                            getSet.setSelected(false);
                        }
                        else
                        {
                            holder.checkBox.setChecked(true);
                            getSet.setSelected(true);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount()
        {
            if (isFor.equalsIgnoreCase(EMPLOYEE))
            {
                return flt_Employee_list.size();
            }
            else if (isFor.equalsIgnoreCase(CLIENT))
            {
                return flt_Client_list.size();
            }
            else if (isFor.equalsIgnoreCase(CLIENT_SEARCH))
            {
                return listClientSearch.size();
            }
            else
            {
                return listClient.size();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView txtName;
            final TextView txtUserSortName;
            final TextView txtEmail;
            final View viewLine;
            final LinearLayout llMain;
            final CheckBox checkBox;

            public ViewHolder(View itemView)
            {
                super(itemView);
                llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
                txtName = (TextView) itemView.findViewById(R.id.txtName);
                txtUserSortName = (TextView) itemView.findViewById(R.id.txtUserSortName);
                txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
                viewLine = (View) itemView.findViewById(R.id.viewLine);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            }
        }
    }

    private class DialogListForViewTaskAdapter extends RecyclerView.Adapter<DialogListForViewTaskAdapter.ViewHolder>
    {
        String isFor = "";

        DialogListForViewTaskAdapter(String isFor)
        {
            this.isFor = isFor;
        }

        @Override
        public DialogListForViewTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
            return new DialogListForViewTaskAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(DialogListForViewTaskAdapter.ViewHolder holder, final int position)
        {
            if (position == getItemCount() - 1)
            {
                holder.viewLine.setVisibility(View.GONE);
            }
            else
            {
                holder.viewLine.setVisibility(View.VISIBLE);
            }

            AllEmployeeResponse.DataBean.AllEmployeeBean getset = listEmployee.get(position);
            holder.tvValue.setText(getset.getFirst_name() + " " + getset.getLast_name());
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listDialog.dismiss();
                    if (getset.getId() != Integer.parseInt(sessionManager.getUserId()))
                    {
                        tvViewTaskOf.setText(getset.getFirst_name() + " " + getset.getLast_name());
                        flt_view_task_Ids = String.valueOf(getset.getId());
                        isCurrentUserTask = false;
                        llAddTask.setVisibility(View.GONE);
                        flt_view_task_Name = getset.getFirst_name() + " " + getset.getLast_name();
                    }
                    else
                    {
                        tvViewTaskOf.setText(getset.getFirst_name() + " " + getset.getLast_name());
                        flt_view_task_Ids = sessionManager.getUserId();
                        isCurrentUserTask = true;
                        llAddTask.setVisibility(View.VISIBLE);
                        flt_view_task_Name = "";
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listEmployee.size();
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

    public void showCalenderDialog()
    {
        final Dialog dialog = new Dialog(activity, R.style.BaseBottomSheetDialog);
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCanceledOnTouchOutside(true);

        final TextView txtApply, txtClearFilter;
        txtFromDate = (TextView) dialog.findViewById(R.id.txtFromDate);
        txtToDate = (TextView) dialog.findViewById(R.id.txtToDate);
        txtApply = (TextView) dialog.findViewById(R.id.txtApply);
        txtClearFilter = (TextView) dialog.findViewById(R.id.txtClearFilter);

        if (isFilterApply)
        {
            if (fromdate_filter.length() > 0)
            {
                txtFromDate.setText(AppUtils.universalDateConvert(fromdate_filter, "dd/MM/yyyy", "dd MMM yyyy"));
                txtToDate.setText(AppUtils.universalDateConvert(todate_filter, "dd/MM/yyyy", "dd MMM yyyy"));
            }
        }

        txtFromDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showDatePickerDialog(txtFromDate, true);
            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (!txtFromDate.getText().toString().equalsIgnoreCase("From Date"))
                {
                    showDatePickerDialog(txtToDate, false);
                }
                else
                {
                    AppUtils.showToast(activity, "Please select from date");
                }

            }
        });

        txtClearFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    isFilterApply = false;
                    fromdate_filter = "";
                    todate_filter = "";
                    fromdate = "";
                    todate = "";
                    dialog.dismiss();
                    dialog.cancel();

                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true);

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
        });

        txtApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    isFilterApply = true;

                    if (txtFromDate.getText().toString().length() > 0 && !txtFromDate.getText().toString().equalsIgnoreCase("From Date"))
                    {
                        fromdate_filter = AppUtils.universalDateConvert(txtFromDate.getText().toString().trim(), "dd MMM yyyy", "dd/MM/yyyy");
                        todate_filter = AppUtils.universalDateConvert(txtToDate.getText().toString().trim(), "dd MMM yyyy", "dd/MM/yyyy");
                    }

                    Log.e("<><> DATS : ", fromdate_filter + " gg " + todate_filter);

                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true);
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

                dialog.dismiss();

                dialog.cancel();
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog(TextView textView, boolean isForFromDate)
    {
        try
        {
            DialogFragment newFragment = new SelectDateFragment(textView, isForFromDate);
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
    {
        TextView textView;
        boolean isForFromDate = false;

        public SelectDateFragment(TextView textView, boolean isForFromDate)
        {
            this.textView = textView;
            this.isForFromDate = isForFromDate;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            DatePickerDialog datepicker = null;

            if (textView.getText().length() > 0 && !textView.getText().toString().equalsIgnoreCase("From Date") && !textView.getText().toString().equalsIgnoreCase("To Date"))
            {
                try
                {
                    String date = AppUtils.universalDateConvert(textView.getText().toString(), "dd MMM yyyy", "dd/MM/yyyy");
                    String[] datearr = date.split("/");
                    int dateint = Integer.parseInt(datearr[0]);
                    int monthint = Integer.parseInt(datearr[1]);
                    int yearint = Integer.parseInt(datearr[2]);
                    datepicker = new DatePickerDialog(getActivity(), this, yearint, monthint - 1, dateint);

                    if (isForFromDate)
                    {
                        final Calendar calendar = Calendar.getInstance();
                        int yy = calendar.get(Calendar.YEAR);
                        int mm = calendar.get(Calendar.MONTH);
                        int dd = calendar.get(Calendar.DAY_OF_MONTH);
                        calendar.set(yy, mm, dd - 1);//Year,Mounth -1,Day
                        datepicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    }
                    else
                    {
                        String[] datearr1 = AppUtils.universalDateConvert(txtFromDate.getText().toString(), "dd MMM yyyy", "dd/MM/yyyy").split("/");

                        int dateint1 = Integer.parseInt(datearr1[0]);
                        int monthint1 = Integer.parseInt(datearr1[1]);
                        int yearint1 = Integer.parseInt(datearr1[2]);
                        Calendar c = Calendar.getInstance();
                        c.set(yearint1, monthint1 - 1, dateint1 + 1);//Year,Mounth -1,Day
                        datepicker.getDatePicker().setMinDate(c.getTimeInMillis());
                        datepicker.getDatePicker().setMaxDate(new Date().getTime());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                try
                {
                    final Calendar calendar = Calendar.getInstance();
                    int yy = calendar.get(Calendar.YEAR);
                    int mm = calendar.get(Calendar.MONTH);
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    datepicker = new DatePickerDialog(getActivity(), this, yy, mm, dd);

                    if (isForFromDate)
                    {
                        calendar.set(yy, mm, dd - 1);//Year,Mounth -1,Day
                        datepicker.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                    }
                    else
                    {

                        String[] datearr1 = AppUtils.universalDateConvert(txtFromDate.getText().toString(), "dd MMM yyyy", "dd/MM/yyyy").split("/");
                        int dateint = Integer.parseInt(datearr1[0]);
                        int monthint = Integer.parseInt(datearr1[1]);
                        int yearint = Integer.parseInt(datearr1[2]);
                        Calendar c = Calendar.getInstance();
                        c.set(yearint, monthint, dateint + 1);//Year,Mounth -1,Day
                        datepicker.getDatePicker().setMinDate(c.getTimeInMillis());
                        datepicker.getDatePicker().setMaxDate(new Date().getTime());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            return datepicker;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd)
        {
            populateSetDate(yy, mm + 1, dd, textView, isForFromDate);
        }

    }

    public static void populateSetDate(int year, int month, int day, TextView textView, boolean isForFromDate)
    {
        String Selected_date = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        Date convertedDate2 = new Date();
        try
        {
            if (isForFromDate)
            {
                convertedDate2 = dateFormat.parse(Selected_date);
                String showDate = df.format(convertedDate2);
                Log.e("showDate", showDate.toString());
                textView.setText(showDate);

                String Selected_date_todate = String.valueOf(day + 1) + "/" + String.valueOf(month) + "/" + String.valueOf(year);
                SimpleDateFormat dateFormat_todate = new SimpleDateFormat("dd/MM/yyyy");
                DateFormat df_todate = new SimpleDateFormat("dd MMM yyyy");
                Date convertedDate_todate = new Date();
                convertedDate_todate = dateFormat.parse(Selected_date_todate);
                txtToDate.setText(df_todate.format(convertedDate_todate).toString());
            }
            else
            {
                convertedDate2 = dateFormat.parse(Selected_date);
                String showDate = df.format(convertedDate2);
                Log.e("showDate", showDate.toString());
                textView.setText(showDate);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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

