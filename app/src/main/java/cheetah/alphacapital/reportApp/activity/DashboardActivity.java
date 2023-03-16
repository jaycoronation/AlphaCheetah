package cheetah.alphacapital.reportApp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.classes.CirclePageIndicator;
import cheetah.alphacapital.classes.EnhancedWrapContentViewPager;
import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.databinding.ActivityDashboardBinding;
import cheetah.alphacapital.reportApp.activity.admin.AddEmployeeActivity;
import cheetah.alphacapital.reportApp.activity.admin.AumEmployeeYearlySummeryActivity;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.activity.admin.CumulativeLeadReportActivity;
import cheetah.alphacapital.reportApp.activity.admin.LeadReportActivity;
import cheetah.alphacapital.reportApp.activity.admin.LeadTrackerActivity;
import cheetah.alphacapital.reportApp.activity.admin.LearningAndManualActivity;
import cheetah.alphacapital.reportApp.activity.admin.ManageEmployeeActivity;
import cheetah.alphacapital.reportApp.activity.admin.TaskSummaryActivity;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.DARSummaryResponseModel;
import cheetah.alphacapital.reportApp.getset.DashboardDARSummaryModel;
import cheetah.alphacapital.reportApp.getset.DashboardPagerGetSet;
import cheetah.alphacapital.reportApp.getset.DashboardTaskSummaryModel;
import cheetah.alphacapital.reportApp.getset.MenuGetSet;
import cheetah.alphacapital.reportApp.getset.NewClientListResponse;
import cheetah.alphacapital.reportApp.getset.SaveTaskResponse;
import cheetah.alphacapital.reportApp.getset.TaskReportResponseModel;
import cheetah.alphacapital.reportApp.getset.ToDoListResponse;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.textutils.CustomTextInputLayout;
import cheetah.alphacapital.textutils.CustomTextViewSemiBold;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.activity.admin.ActivityTypeListActivity;
import cheetah.alphacapital.reportApp.activity.admin.DARReportForEmployeeActivity;
import cheetah.alphacapital.reportApp.activity.admin.EmployeeDetailsActivity;
import cheetah.alphacapital.reportApp.activity.admin.ViewDailyActivityReportListActivity;
import cheetah.alphacapital.reportApp.activity.admin.ManageClientActivity;
import cheetah.alphacapital.reportApp.activity.admin.ManageDailyActivityReportActivity;
import cheetah.alphacapital.reportApp.activity.admin.ManageSchemeActivity;
import cheetah.alphacapital.reportApp.activity.admin.ViewAUMReportsListActivity;
import cheetah.alphacapital.reportApp.activity.admin.WorkingDaysActivity;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardActivity extends BaseActivity
{
    private ActivityDashboardBinding binding;
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private CustomTextViewSemiBold txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading, llNoData;
    private ProgressBar pbLoading;
    private AppCompatTextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private AppCompatTextView txtRetry;
    private DrawerLayout drawerLayout;
    private TextView tvMonth, tvYear, tvNoDataText, tvEmployee, txtUserName, txtUserEmail;
    private TextView tvClient1, tvClient2, tvReference1, tvReference2, tvExMeeting1, tvExMeeting2, tvNewMeeting1, tvNewMeeting2, tvToDo1, tvToDo2;
    private LinearLayout llClients, llSelectMonth, llSelectYear, llSelectEmployee, llToDoList, llAddDAR, llOther;
    private LinearLayout llBelow, llTop;
    private LinearLayout llViewPager;
    private RecyclerView rvTaskSummary;
    RecyclerView rvMenu, rvMenuDashBoard;
    private EnhancedWrapContentViewPager viewpager;
    private CirclePageIndicator circlePageIndicator;
    private ArcProgress arc_progress;
    private SwipeRefreshLayout sfDashboard;
    private CardView cvTaskMain;

    private final List<String> listYear = new ArrayList<>();
    private final List<CommonGetSet> listMonth = new ArrayList<>();
    private ArrayList<AssignedClientGetSetOld> listEmployee = new ArrayList<AssignedClientGetSetOld>();

    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee2 = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

    private BottomSheetDialog listDialog;
    private final String MONTH = "Month";
    private final String YEAR = "Year";
    private final String EMPLOYEE = "Employee";
    private String selectedYear = "", selectedMonth = "", selectedEmployee = "";

    private ArrayList<MenuGetSet> listMenu = new ArrayList<MenuGetSet>();
    private ArrayList<MenuGetSet> listMenuDashBoard = new ArrayList<MenuGetSet>();
    public static Handler handler;

    private final ArrayList<TaskReportResponseModel.DataItem> listData = new ArrayList<>();

    //For TODO List
    private ArrayList<ToDoListResponse.DataBean.FilterEmployeeListBean> flt_Employee_list = new ArrayList<ToDoListResponse.DataBean.FilterEmployeeListBean>();
    private ArrayList<ToDoListResponse.DataBean.FilterClientListBean> flt_Client_list = new ArrayList<ToDoListResponse.DataBean.FilterClientListBean>();
    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClient = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();
    private ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean> listClientSearch = new ArrayList<NewClientListResponse.DataBean.AllClientByEmployeeBean>();

    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listAllTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
    private ArrayList<ToDoListResponse.DataBean.TaskListBean> listPinnedTask = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();

    private final ArrayList<ToDoListResponse.DataBean.TaskListBean> listAllTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
    private final ArrayList<ToDoListResponse.DataBean.TaskListBean> listPinnedTaskSearch = new ArrayList<ToDoListResponse.DataBean.TaskListBean>();
    private AllTaskListAdapter allTaskListAdapter;

    String CurrentMonth = "Current Month";
    String LastMonth = "Last Month";
    String SinceBeginnings = "Since Beginning";

    private final String TaskStatusId = "1";
    private String add_task_clientIds = "";
    private String add_task_clientName = "";
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

        activity = this;

        sessionManager = new SessionManager(activity);

        binding = DataBindingUtil.setContentView(activity, R.layout.activity_dashboard);

        setupViews();

        onClickEvents();

        getFirebaseToken();

        if (sessionManager.isAdmin())
        {
            //  llAdminAction.setVisibility(View.VISIBLE);
            if (sessionManager.isNetworkAvailable())
            {
                getEmployeeDataForAdmin();
                getTaskSummary();
                getDARSummary();
                getAllTask(true);
                getAllEmployee();
                llNoInternet.setVisibility(View.GONE);
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
                txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            // llAdminAction.setVisibility(View.GONE);
            if (sessionManager.isNetworkAvailable())
            {
                getDashboardData(true);
                getTaskSummary();
                getDARSummary();
                getAllTask(true);
                getAllEmployee();
                llNoInternet.setVisibility(View.GONE);
            }
            else
            {
                llNoInternet.setVisibility(View.VISIBLE);
                txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                pbLoading.setVisibility(View.GONE);
                llRetry.setVisibility(View.VISIBLE);
            }
        }

        handler = new Handler(msg ->
        {
            try
            {
                if (msg.what == 100)
                {
                    if (sessionManager.isAdmin())
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            getEmployeeDataForAdmin();
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                            pbLoading.setVisibility(View.GONE);
                            llRetry.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            getDashboardData(true);
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                            txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                            pbLoading.setVisibility(View.GONE);
                            llRetry.setVisibility(View.VISIBLE);
                        }
                    }
                }
                if (msg.what == 110)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getDashboardData(false);
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                        txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                        pbLoading.setVisibility(View.GONE);
                        llRetry.setVisibility(View.VISIBLE);
                    }
                }
                if (msg.what == 13)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        getAllTask(true);
                    }
                    else
                    {
                        llNoInternet.setVisibility(View.VISIBLE);
                        txtLoading.setText(activity.getResources().getString(R.string.network_failed_message));
                        pbLoading.setVisibility(View.GONE);
                        llRetry.setVisibility(View.VISIBLE);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        });
    }

    private void setupViews()
    {
        try
        {
            toolbar = findViewById(R.id.toolbar);
            sfDashboard = findViewById(R.id.sfDashboard);
            cvTaskMain = findViewById(R.id.cvTaskMain);
            drawerLayout = findViewById(R.id.drawerLayout);
            llBelow = findViewById(R.id.llBelow);
            llTop = findViewById(R.id.llTop);
            llBackNavigation = findViewById(R.id.llBackNavigation);
            ivLogo = findViewById(R.id.ivLogo);
            ivIcon = findViewById(R.id.ivIcon);
            txtTitle = findViewById(R.id.txtTitle);
            txtUserName = findViewById(R.id.txtUserName);
            txtUserEmail = findViewById(R.id.txtUserEmail);
            ivContactUs = findViewById(R.id.ivContactUs);
            llNotification = findViewById(R.id.llNotification);
            llLoading = findViewById(R.id.llLoading);
            pbLoading = findViewById(R.id.pbLoading);
            txtLoading = findViewById(R.id.txtLoading);
            llNoInternet = findViewById(R.id.llNoInternet);
            llRetry = findViewById(R.id.llRetry);
            txtRetry = findViewById(R.id.txtRetry);
            llNoData = findViewById(R.id.llNoData);
            tvNoDataText = findViewById(R.id.tvNoDataText);
            llToDoList = findViewById(R.id.llToDoList);
            llAddDAR = findViewById(R.id.llAddDAR);
            llOther = findViewById(R.id.llOther);
            llViewPager = findViewById(R.id.llViewPager);
            rvMenu = findViewById(R.id.rvMenu);
            rvMenuDashBoard = findViewById(R.id.rvMenuDashBoard);
            View emptyView = findViewById(R.id.emptyView);
            tvMonth = findViewById(R.id.tvMonth);
            tvYear = findViewById(R.id.tvYear);
            tvEmployee = findViewById(R.id.tvEmployee);
            tvClient1 = findViewById(R.id.tvClient1);
            tvClient2 = findViewById(R.id.tvClient2);
            tvReference1 = findViewById(R.id.tvReference1);
            tvReference2 = findViewById(R.id.tvReference2);
            tvExMeeting1 = findViewById(R.id.tvExMeeting1);
            tvExMeeting2 = findViewById(R.id.tvExMeeting2);
            tvNewMeeting1 = findViewById(R.id.tvNewMeeting1);
            tvNewMeeting2 = findViewById(R.id.tvNewMeeting2);
            tvToDo1 = findViewById(R.id.tvToDo1);
            tvToDo2 = findViewById(R.id.tvToDo2);
            llClients = findViewById(R.id.llClients);
            llSelectYear = findViewById(R.id.llSelectYear);
            llSelectMonth = findViewById(R.id.llSelectMonth);
            llSelectEmployee = findViewById(R.id.llSelectEmployee);
            viewpager = findViewById(R.id.viewpager);
            circlePageIndicator = findViewById(R.id.pageIndicator);
            arc_progress = findViewById(R.id.arc_progress);
            rvTaskSummary = findViewById(R.id.rvTaskSummary);

            //setSupportActionBar(toolbar);
            txtTitle.setText("Dashboard");
            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.VISIBLE);
            llBackNavigation.setVisibility(View.GONE);
            rvMenu.setLayoutManager(new LinearLayoutManager(activity));
            rvMenuDashBoard.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            emptyView.setVisibility(View.GONE);

            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setImageResource(R.drawable.ic_menu);

            selectedMonth = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            Log.e("<><> Month : ", selectedMonth + " Year " + selectedYear);

            isCurrentUserTask = true;

            if (sessionManager.isAdmin())
            {
                llSelectEmployee.setVisibility(View.VISIBLE);
                llOther.setVisibility(View.GONE);
                txtUserName.setText(sessionManager.getUserName());
                txtUserEmail.setText(sessionManager.getEmail());
                tvNoDataText.setText("No Data Found For Dashboard.");
            }
            else
            {
                llOther.setVisibility(View.VISIBLE);
                llSelectEmployee.setVisibility(View.GONE);
                selectedEmployee = sessionManager.getUserId();
            }

            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener()
            {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset)
                {
                }

                @Override
                public void onDrawerOpened(View drawerView)
                {
                    try
                    {
                        //MitsUtils.hideKeyboard(activity);
                        hideKeyboard();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDrawerClosed(View drawerView)
                {
                }

                @Override
                public void onDrawerStateChanged(int newState)
                {
                }
            });

            setMenuAdapter();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onClickEvents()
    {
        sfDashboard.setOnRefreshListener(() ->
        {
            getEmployeeDataForAdmin();
            getTaskSummary();
            getDARSummary();
            getAllTask(true);
            getAllEmployee();
        });

        llNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(activity, NotificationListActivity.class);
                startActivity(intent);
            }
        });

        llBackNavigation.setOnClickListener(v ->
        {
            try
            {
                AppUtils.hideKeyboard(toolbar, activity);
                activity.finishAffinity();
                //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llClients.setOnClickListener(v ->
        {
            if (sessionManager.isAdmin())
            {
                Intent intent = new Intent(activity, ManageClientActivity.class);
                intent.putExtra("selectedMonth", selectedMonth);
                intent.putExtra("selectedMonthName", tvMonth.getText().toString().trim());
                intent.putExtra("selectedYear", selectedYear);
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(activity, AssignedClientListActivity.class);
                intent.putExtra("selectedMonth", selectedMonth);
                intent.putExtra("selectedMonthName", tvMonth.getText().toString().trim());
                intent.putExtra("selectedYear", selectedYear);
                startActivity(intent);
            }
        });

        llToDoList.setOnClickListener(v -> startActivity(new Intent(activity, TaskListActivityNew.class)));

        llAddDAR.setOnClickListener(v ->
        {
            try
            {
                Intent intent = new Intent(activity, ManageDailyActivityReportActivity.class);
                intent.putExtra("ID", AppUtils.getEmployeeIdForAdmin(activity));
                startActivity(intent);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llSelectMonth.setOnClickListener(v -> showListDialog(MONTH));

        llSelectYear.setOnClickListener(v -> showListDialog(YEAR));

        llSelectEmployee.setOnClickListener(v -> showListDialog(EMPLOYEE));

        ivIcon.setOnClickListener(v ->
        {
            try
            {
                AppUtils.openDrawerLeft(drawerLayout);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        llBelow.setOnClickListener(v ->
        {
        });

        llTop.setOnClickListener(v ->
        {
            try
            {
                AppUtils.openDrawerLeft(drawerLayout);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llCurrentMonth.setOnClickListener(v ->
        {
            try
            {
                binding.txtCurrentMonth.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineCurrentMonth.setVisibility(View.GONE);

                binding.txtLastMonth.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLastMonth.setVisibility(View.GONE);

                binding.tvSinceBegining.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineSinceBegining.setVisibility(View.GONE);

                getTaskData(CurrentMonth, taskData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llLastMonth.setOnClickListener(v ->
        {
            try
            {
                binding.txtCurrentMonth.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineCurrentMonth.setVisibility(View.GONE);

                binding.txtLastMonth.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineLastMonth.setVisibility(View.GONE);

                binding.tvSinceBegining.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineSinceBegining.setVisibility(View.GONE);

                getTaskData(LastMonth, taskData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llSinceBegining.setOnClickListener(v ->
        {
            try
            {
                binding.txtCurrentMonth.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineCurrentMonth.setVisibility(View.GONE);

                binding.txtLastMonth.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLastMonth.setVisibility(View.GONE);

                binding.tvSinceBegining.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineSinceBegining.setVisibility(View.GONE);

                getTaskData(SinceBeginnings, taskData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llAllTitle.setOnClickListener(view ->
        {
            if (binding.rvAllTask.getVisibility() == View.VISIBLE)
            {
                binding.rvAllTask.setVisibility(View.GONE);
                binding.rvPinnedTask.setVisibility(View.GONE);
                binding.ivAllTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_right_blue));
            }
            else
            {
                binding.rvAllTask.setVisibility(View.VISIBLE);
                binding.rvPinnedTask.setVisibility(View.VISIBLE);
                binding.ivAllTask.setImageDrawable(getResources().getDrawable(R.drawable.t_arrow_down_blue));
            }
        });

        binding.llCurrentMonthDAR.setOnClickListener(v ->
        {
            try
            {
                binding.tvCurrentMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineCurrentMonthDAR.setVisibility(View.GONE);

                binding.tvLastMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLastMonthDAR.setVisibility(View.GONE);

                binding.tvLast12MonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLast12MonthDAR.setVisibility(View.GONE);

                getDARData(CurrentMonth, DARData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llLastMonthDAR.setOnClickListener(v ->
        {
            try
            {
                binding.tvCurrentMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineCurrentMonthDAR.setVisibility(View.GONE);

                binding.tvLastMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineLastMonthDAR.setVisibility(View.GONE);

                binding.tvLast12MonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLast12MonthDAR.setVisibility(View.GONE);

                getDARData(LastMonth, DARData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.llLast12MonthDAR.setOnClickListener(v ->
        {
            try
            {
                binding.tvCurrentMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineCurrentMonthDAR.setVisibility(View.GONE);

                binding.tvLastMonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.black));
                binding.viewLineLastMonthDAR.setVisibility(View.GONE);

                binding.tvLast12MonthDAR.setTextColor(ContextCompat.getColor(activity, R.color.blue_new));
                binding.viewLineLast12MonthDAR.setVisibility(View.GONE);

                getDARData(SinceBeginnings, DARData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        binding.ivAddTask.setOnClickListener(v ->
        {
            add_task_clientIds = "";
            add_task_clientName = "";
            showAddTaskDialog();
        });

        binding.tvViewAll.setOnClickListener(v ->
        {
            startActivity(new Intent(activity, TaskListActivityNew.class));
        });
    }

    private void getFirebaseToken()
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task ->
                {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    // Get new FCM registration token
                    String token = task.getResult();
                    sessionManager.saveTokenId(token);
                    sendRegistrationToServer(token);
                    Log.d("FCM TOKEN", token);
                });
    }

    private void sendRegistrationToServer(String token)
    {
        apiService.updateDeviceToken(Integer.valueOf(sessionManager.getUserId()),token,"true").enqueue(new Callback<CommentResponse>()
        {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response)
            {

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t)
            {

            }
        });
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

        llMain = sheetView.findViewById(R.id.llMain);
        rvEmployeeList = sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = sheetView.findViewById(R.id.ivSetReminder);

        edtTaskName = sheetView.findViewById(R.id.edtTaskName);
        txtAddReminder = sheetView.findViewById(R.id.txtAddReminder);
        txtClientName = sheetView.findViewById(R.id.txtClientName);
        ivAddClient = sheetView.findViewById(R.id.ivAddClient);
        txtAddTask = sheetView.findViewById(R.id.txtAddTask);
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
                hideKeyboard();
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
                        Log.e("<><>listEmployee2", String.valueOf(listEmployee2.size()));
                        if (listEmployee2 != null && listEmployee2.size() > 0)
                        {
                            for (int i = 0; i < listEmployee2.size(); i++)
                            {
                                final String text = listEmployee2.get(i).getFullname();

                                String text1 = AppUtils.getCapitalText(text);

                                String cs1 = AppUtils.getCapitalText(hashtag);

                                if (text1.contains(cs1))
                                {
                                    listEmployee_Search.add(listEmployee2.get(i));
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

        txtAddTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
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
            }
        });

        edtTaskName.requestFocus();

        dialog_Add_Task.show();
    }

    private class EmployeeAdpater extends RecyclerView.Adapter<EmployeeAdpater.ViewHolder>
    {
        private final ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> items;
        private String hashtag = "";
        private int end = 0;
        private final EditText editText;
        private final RecyclerView recyclerView;

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
                        String endText = edtTextString.substring(end + 1);
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
                txtName = convertView.findViewById(R.id.txtName);
                txtUserSortName = convertView.findViewById(R.id.txtUserSortName);
                txtEmail = convertView.findViewById(R.id.txtEmail);
                viewline = convertView.findViewById(R.id.viewline);
            }
        }
    }

    private void datePicker(final EditText edtTaskName)
    {
        if (selectedReminderDate.length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(selectedReminderDate, "dd MMM yyyy", "dd/MM/yyyy");
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
                    Log.e("showDate", showDate);
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

            if (!selectedReminderTime.equals(""))
            {
                try
                {
                    String time = selectedReminderTime.trim();
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

                    finalDateTime = AppUtils.universalDateConvert(selectedReminderDate.trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTime.trim();

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
                Log.e("<><> Check Du : ", items.get(i));
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
                            contactSelected = contactSelected + "," + items_contact.get(i).trim();
                        }
                    }
                    else
                    {
                        contactSelected = items_contact.get(i).trim();
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

    private String flt_employeeName = "";
    private String flt_employeeIds = "";
    private String flt_clientIds = "";
    private String flt_clientName = "";
    private final String flt_view_task_Name = "";
    private final String flt_view_task_Ids = "";

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
                            listEmployee2 = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
                            listEmployee2.addAll(response.body().getData().getAllEmployee());

                            for (int i = 0; i < listEmployee2.size(); i++)
                            {
                                String toDisplayName = AppUtils.toDisplayCase(listEmployee2.get(i).getFirst_name() + "" + listEmployee2.get(i).getLast_name());
                                listEmployee2.get(i).setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
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

    TaskReportResponseModel.Data taskData = new TaskReportResponseModel.Data();

    private void getTaskSummary()
    {
        binding.pbTask.setVisibility(View.VISIBLE);
        apiService.getTaskReportAPI(Integer.valueOf(sessionManager.getUserId()), 0).enqueue(new Callback<TaskReportResponseModel>()
        {
            @Override
            public void onResponse(Call<TaskReportResponseModel> call, Response<TaskReportResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().getData().size() > 0)
                        {
                            getTaskData(CurrentMonth, response.body().getData());
                            taskData = response.body().getData();
                            listData.addAll(response.body().getData().getData());
                        }
                        else
                        {
                        }
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                if (sfDashboard.isRefreshing())
                {
                    sfDashboard.setRefreshing(false);
                }
                binding.pbTask.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<TaskReportResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
                binding.pbTask.setVisibility(View.GONE);
            }
        });
    }

    DARSummaryResponseModel.Data DARData = new DARSummaryResponseModel.Data();

    private void getDARSummary()
    {
        binding.pbDARSummary.setVisibility(View.VISIBLE);
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

                            response.body().getData().getAverage().getCurrentMonthDarAverage().setWorkingDayAverage(roundMyData(currentTotalWorkingDays / currentTotalDays, 2));
                            response.body().getData().getAverage().getCurrentMonthDarAverage().setWorkingHourAverage(roundMyData(currentTotalWorkingHours / currentTotalHours, 2));

                            response.body().getData().getAverage().getLastMonthDarAverage().setWorkingDayAverage(roundMyData(lastTotalWorkingDays / lastTotalDays, 2));
                            response.body().getData().getAverage().getLastMonthDarAverage().setWorkingHourAverage(roundMyData(lastTotalWorkingHours / lastTotalHours, 2));

                            response.body().getData().getAverage().getPastYearMonthDARAverage().setWorkingDayAverage(roundMyData(pastTotalWorkingDays / pastTotalDays, 2));
                            response.body().getData().getAverage().getPastYearMonthDARAverage().setWorkingHourAverage(roundMyData(pastTotalWorkingHours / pastTotalHours, 2));

                            DARData = response.body().getData();
                            getDARData(CurrentMonth, DARData);
                        }
                        else
                        {
                            showToast("Something wents Wrong");
                        }
                    }
                }

                if (sfDashboard.isRefreshing())
                {
                    sfDashboard.setRefreshing(false);
                }

                binding.pbDARSummary.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DARSummaryResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
                binding.pbDARSummary.setVisibility(View.GONE);
            }
        });
    }

    private void getDARData(String isFor, DARSummaryResponseModel.Data data)
    {
        ArrayList<DashboardDARSummaryModel> listData = new ArrayList<>();

        DashboardDARSummaryModel titlesGetSet = new DashboardDARSummaryModel();
        titlesGetSet.setTitle("");
        titlesGetSet.setWorking_days("Working Day");
        titlesGetSet.setWorking_hours("Working Hours");
        listData.add(titlesGetSet);

        if (isFor.equals(CurrentMonth))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardDARSummaryModel dataGetSet = new DashboardDARSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setWorking_days(String.valueOf(roundMyData(data.getData().get(i).getCurrentMonth().getWorkingDays(), 2)));
                    dataGetSet.setWorking_hours(String.valueOf(roundMyData(data.getData().get(i).getCurrentMonth().getWorkingHours(), 2)));
                    listData.add(dataGetSet);
                }
            }

            DashboardDARSummaryModel avgGetSet = new DashboardDARSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setWorking_days(String.valueOf(data.getAverage().getCurrentMonthDarAverage().getWorkingDayAverage()));
            avgGetSet.setWorking_hours(String.valueOf(data.getAverage().getCurrentMonthDarAverage().getWorkingHourAverage()));
            listData.add(avgGetSet);
        }
        else if (isFor.equals(LastMonth))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardDARSummaryModel dataGetSet = new DashboardDARSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setWorking_days(String.valueOf(roundMyData(data.getData().get(i).getLastMonth().getWorkingDays(), 2)));
                    dataGetSet.setWorking_hours(String.valueOf(roundMyData(data.getData().get(i).getLastMonth().getWorkingHours(), 2)));
                    listData.add(dataGetSet);
                }
            }

            DashboardDARSummaryModel avgGetSet = new DashboardDARSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setWorking_days(String.valueOf(data.getAverage().getLastMonthDarAverage().getWorkingDayAverage()));
            avgGetSet.setWorking_hours(String.valueOf(data.getAverage().getLastMonthDarAverage().getWorkingHourAverage()));
            listData.add(avgGetSet);
        }
        else if (isFor.equalsIgnoreCase(SinceBeginnings))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardDARSummaryModel dataGetSet = new DashboardDARSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setWorking_days(String.valueOf(roundMyData(data.getData().get(i).getPastYearMonthDAR().getWorkingDays(), 2)));
                    dataGetSet.setWorking_hours(String.valueOf(roundMyData(data.getData().get(i).getPastYearMonthDAR().getWorkingHours(), 2)));
                    listData.add(dataGetSet);
                }
            }

            DashboardDARSummaryModel avgGetSet = new DashboardDARSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setWorking_days(String.valueOf(data.getAverage().getPastYearMonthDARAverage().getWorkingDayAverage()));
            avgGetSet.setWorking_hours(String.valueOf(data.getAverage().getPastYearMonthDARAverage().getWorkingHourAverage()));
            listData.add(avgGetSet);
        }

        if (listData.size() > 1)
        {
            binding.rvDARSummary.setAdapter(new DARSummaryAdapter(listData));
        }
    }

    private void getTaskData(String isFor, TaskReportResponseModel.Data data)
    {
        ArrayList<DashboardTaskSummaryModel> listData = new ArrayList<>();

        DashboardTaskSummaryModel titlesGetSet = new DashboardTaskSummaryModel();
        titlesGetSet.setTitle("");
        titlesGetSet.setTotal_tasks("Total Tasks");
        titlesGetSet.setTask_closed("Tasks Closed");
        titlesGetSet.setAvg_days_taken("Avg Days Taken");
        titlesGetSet.setOpen_task("Open Tasks");
        titlesGetSet.setAvg_days_open("Avg Open Days");
        listData.add(titlesGetSet);

        if (isFor.equals(CurrentMonth))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardTaskSummaryModel dataGetSet = new DashboardTaskSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setTotal_tasks(String.valueOf(data.getData().get(i).getCurrentMonth().getTotalTasks()));
                    dataGetSet.setTask_closed(String.valueOf(data.getData().get(i).getCurrentMonth().getTasksClosed()));
                    dataGetSet.setAvg_days_taken(String.valueOf(data.getData().get(i).getCurrentMonth().getAvgDaysTaken()));
                    dataGetSet.setOpen_task(String.valueOf(data.getData().get(i).getCurrentMonth().getOpenTasks()));
                    dataGetSet.setAvg_days_open(String.valueOf(data.getData().get(i).getCurrentMonth().getAvgOpenDays()));
                    listData.add(dataGetSet);
                }
            }

            DashboardTaskSummaryModel avgGetSet = new DashboardTaskSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setTotal_tasks(String.valueOf(data.getAverage().getCurrentMonthTaskAverage().getTotalTasks()));
            avgGetSet.setTask_closed(String.valueOf(data.getAverage().getCurrentMonthTaskAverage().getTasksClosed()));
            avgGetSet.setAvg_days_taken(String.valueOf(data.getAverage().getCurrentMonthTaskAverage().getAvgDaysTaken()));
            avgGetSet.setOpen_task(String.valueOf(data.getAverage().getCurrentMonthTaskAverage().getOpenTasks()));
            avgGetSet.setAvg_days_open(String.valueOf(data.getAverage().getCurrentMonthTaskAverage().getAvgOpenDays()));
            listData.add(avgGetSet);
        }
        else if (isFor.equals(LastMonth))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardTaskSummaryModel dataGetSet = new DashboardTaskSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setTotal_tasks(String.valueOf(data.getData().get(i).getLastMonth().getTotalTasks()));
                    dataGetSet.setTask_closed(String.valueOf(data.getData().get(i).getLastMonth().getTasksClosed()));
                    dataGetSet.setAvg_days_taken(String.valueOf(data.getData().get(i).getLastMonth().getAvgDaysTaken()));
                    dataGetSet.setOpen_task(String.valueOf(data.getData().get(i).getLastMonth().getOpenTasks()));
                    dataGetSet.setAvg_days_open(String.valueOf(data.getData().get(i).getLastMonth().getAvgOpenDays()));
                    listData.add(dataGetSet);
                }
            }

            DashboardTaskSummaryModel avgGetSet = new DashboardTaskSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setTotal_tasks(String.valueOf(data.getAverage().getLastMonthTaskAverage().getTotalTasks()));
            avgGetSet.setTask_closed(String.valueOf(data.getAverage().getLastMonthTaskAverage().getTasksClosed()));
            avgGetSet.setAvg_days_taken(String.valueOf(data.getAverage().getLastMonthTaskAverage().getAvgDaysTaken()));
            avgGetSet.setOpen_task(String.valueOf(data.getAverage().getLastMonthTaskAverage().getOpenTasks()));
            avgGetSet.setAvg_days_open(String.valueOf(data.getAverage().getLastMonthTaskAverage().getAvgOpenDays()));
            listData.add(avgGetSet);
        }
        else if (isFor.equalsIgnoreCase(SinceBeginnings))
        {
            for (int i = 0; i < data.getData().size(); i++)
            {
                if (String.valueOf(data.getData().get(i).getEmployee_id()).equalsIgnoreCase(sessionManager.getUserId()))
                {
                    DashboardTaskSummaryModel dataGetSet = new DashboardTaskSummaryModel();
                    dataGetSet.setTitle("Your Score");
                    dataGetSet.setTotal_tasks(String.valueOf(data.getData().get(i).getSinceBeginning().getTotalTasks()));
                    dataGetSet.setTask_closed(String.valueOf(data.getData().get(i).getSinceBeginning().getTasksClosed()));
                    dataGetSet.setAvg_days_taken(String.valueOf(data.getData().get(i).getSinceBeginning().getAvgDaysTaken()));
                    dataGetSet.setOpen_task(String.valueOf(data.getData().get(i).getSinceBeginning().getOpenTasks()));
                    dataGetSet.setAvg_days_open(String.valueOf(data.getData().get(i).getSinceBeginning().getAvgOpenDays()));
                    listData.add(dataGetSet);
                }
            }

            DashboardTaskSummaryModel avgGetSet = new DashboardTaskSummaryModel();
            avgGetSet.setTitle("Company Average");
            avgGetSet.setTotal_tasks(String.valueOf(data.getAverage().getSinceBeginningTaskAverage().getTotalTasks()));
            avgGetSet.setTask_closed(String.valueOf(data.getAverage().getSinceBeginningTaskAverage().getTasksClosed()));
            avgGetSet.setAvg_days_taken(String.valueOf(data.getAverage().getSinceBeginningTaskAverage().getAvgDaysTaken()));
            avgGetSet.setOpen_task(String.valueOf(data.getAverage().getSinceBeginningTaskAverage().getOpenTasks()));
            avgGetSet.setAvg_days_open(String.valueOf(data.getAverage().getSinceBeginningTaskAverage().getAvgOpenDays()));
            listData.add(avgGetSet);
        }

        if (listData.size() > 1)
        {
            binding.rvTaskSummary.setAdapter(new DataAdapter(listData));
        }
    }

    private PinnedTaskListAdapter pinnedTaskListAdapter;

    private void getAllTask(boolean isFirstTime)
    {
        if (isFirstTime)
        {
            binding.pbTask.setVisibility(View.VISIBLE);
        }
        String finalFromDate = "", finalToDate = "";

        Call<ToDoListResponse> call = apiService.getAllTaskFromApi("1", sessionManager.getUserId(), "", "", finalFromDate, finalToDate, "0", "5");
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
                            }
                            else
                            {

                                if (listPinnedTask.size() > 0)
                                {
                                    binding.rvPinnedTask.setVisibility(View.VISIBLE);
                                    pinnedTaskListAdapter = new PinnedTaskListAdapter(listPinnedTask);
                                    binding.rvPinnedTask.setAdapter(pinnedTaskListAdapter);
                                }
                                else
                                {
                                    binding.rvPinnedTask.setVisibility(View.GONE);
                                }

                                if (listAllTask.size() > 0)
                                {
                                    Log.e("<><><>IS TASK LIST FUll", String.valueOf(listAllTask.size()));
                                    binding.llAllTaskMain.setVisibility(View.VISIBLE);
                                    allTaskListAdapter = new AllTaskListAdapter(listAllTask);
                                    binding.rvAllTask.setAdapter(allTaskListAdapter);
                                    cvTaskMain.setVisibility(View.VISIBLE);
                                }
                                else
                                {
                                    binding.llAllTaskMain.setVisibility(View.GONE);
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
                        cvTaskMain.setVisibility(View.GONE);
                        binding.pbTask.setVisibility(View.GONE);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                    binding.pbTask.setVisibility(View.GONE);
                }
                if (sfDashboard.isRefreshing())
                {
                    sfDashboard.setRefreshing(false);
                }
                binding.pbTask.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ToDoListResponse> call, Throwable t)
            {
                binding.pbTask.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public class PinnedTaskListAdapter extends RecyclerView.Adapter<PinnedTaskListAdapter.ViewHolder>
    {
        private final ArrayList<ToDoListResponse.DataBean.TaskListBean> items;

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
        private final ArrayList<ToDoListResponse.DataBean.TaskListBean> items;
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
        public void onBindViewHolder(final AllTaskListAdapter.ViewHolder holder, int position)
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
            if (items.size() < 5)
            {
                return items.size();
            }
            else
            {
                return 5;
            }

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

        llMain = sheetView.findViewById(R.id.llMain);
        llEdit = sheetView.findViewById(R.id.llEdit);
        llDelete = sheetView.findViewById(R.id.llDelete);
        llComplete = sheetView.findViewById(R.id.llComplete);
        llPin = sheetView.findViewById(R.id.llPin);
        ivPin = sheetView.findViewById(R.id.ivPin);
        txtPin = sheetView.findViewById(R.id.txtPin);
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


        dialog.setOnDismissListener(dialogInterface -> AppUtils.hideKeyboard(llEdit,activity));

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
                        add_task_client = add_task_client + "," + taskListGetSet.getLstclient().get(i).getClient_id();
                    }
                }

                if (taskListGetSet.getDue_date() != null && taskListGetSet.getDue_date().length() > 0)
                {
                    updateTask(taskListGetSet.getTask_message().trim(), taskListGetSet.getAll_employee_ids(), AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"), taskListGetSet.getId(), "2", add_task_client);
                }
                else
                {
                    updateTask(taskListGetSet.getTask_message().trim(), taskListGetSet.getAll_employee_ids(), "", taskListGetSet.getId(), "2", add_task_client);
                }
            }
        });

        dialog.show();
    }

    private BottomSheetDialog dialog_Edit_Task;
    private TextView txtAddReminderEdit, txtClientNameEdit;
    private HashTagHelper hashTagHelperEdit;

    public static String selectedReminderDateEdit = "", selectedReminderTimeEdit = "";
    private String finalDateTimeEdit = "";
    String date_timeEdit = "";
    int mYearEdit;
    int mMonthEdit;
    int mDayEdit;

    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

    private final String CLIENT = "Client";
    private final String CLIENT_ADD = "Add Client";
    private final String CLIENT_EDIT = "Edit Client";
    private final String CLIENT_SEARCH = "Search Client";
    private final String VIEWTASKOF = "View Task Of";
    private boolean isCurrentUserTask = false;
    private EmployeeAdpaterEdit employeeAdpaterEdit;

    public void showEditTaskDialog(final ToDoListResponse.DataBean.TaskListBean getSet, int pos)
    {
        dialog_Edit_Task = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
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

        llMain = sheetView.findViewById(R.id.llMain);
        rvEmployeeList = sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = sheetView.findViewById(R.id.edtTaskName);
        txtAddReminderEdit = sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = sheetView.findViewById(R.id.txtAddTask);
        txtClientNameEdit = sheetView.findViewById(R.id.txtClientName);
        ivAddClient = sheetView.findViewById(R.id.ivAddClient);
        ivAddClient.setVisibility(View.VISIBLE);


        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        hashTagHelperEdit = HashTagHelper.Creator.create(ContextCompat.getColor(activity, R.color.colorAccent), null);
        hashTagHelperEdit.handle(edtTaskName);

        edtTaskName.setText(getSet.getTask_message().trim());

        try
        {
            if (getSet.getTask_message().trim().length() > 0)
            {
                edtTaskName.setSelection(getSet.getTask_message().trim().length());
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
            finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.trim();
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

        listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
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


        dialog_Edit_Task.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                hideKeyboard();
                // AppUtils.hideKeyboard(edtTaskName, activity);
            }
        });


        ivAddClient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog(CLIENT_EDIT);
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
                        return;
                    }


                    //  String str = String.valueOf(s.toString().charAt(start));

                    final String hashtag = AppUtils.getValidAPIStringResponse(AppUtils.getHashtagFromString(s.toString(), start));

                    Log.e("<><> HASHTAG LENTH :: ", hashtag + "");


                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

                        if (listEmployee2 != null && listEmployee2.size() > 0)
                        {
                            for (int i = 0; i < listEmployee2.size(); i++)
                            {
                                final String text = listEmployee2.get(i).getFullname();

                                String text1 = AppUtils.getCapitalText(text);

                                String cs1 = AppUtils.getCapitalText(hashtag);

                                if (text1.contains(cs1))
                                {
                                    listEmployee_Search.add(listEmployee2.get(i));
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

        ivSetReminder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                datePickerEdit(edtTaskName);
            }
        });

        txtAddTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
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

                            updateTask(edtTaskName.getText().toString().trim(), contact, finalDateTimeEdit, getSet.getId(), "1", "");

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
            }
        });

        dialog_Edit_Task.show();

        edtTaskName.requestFocus();
    }

    private class EmployeeAdpaterEdit extends RecyclerView.Adapter<EmployeeAdpaterEdit.ViewHolder>
    {
        private final ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> items;
        private String hashtag = "";
        private int end = 0;
        private final EditText editText;
        private final RecyclerView recyclerView;

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
                        String endText = edtTextString.substring(end + 1);
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
                txtName = convertView.findViewById(R.id.txtName);
                txtUserSortName = convertView.findViewById(R.id.txtUserSortName);
                txtEmail = convertView.findViewById(R.id.txtEmail);
                viewline = convertView.findViewById(R.id.viewline);
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
                Log.e("<><> Check Du : ", items.get(i));
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
                            contactSelected = contactSelected + "," + items_contact.get(i).trim();
                        }
                    }
                    else
                    {
                        contactSelected = items_contact.get(i).trim();
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

        Log.e("showDate ReminderDate:", selectedReminderDateEdit);

        if (selectedReminderDateEdit.length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(selectedReminderDateEdit, "dd MMM yyyy", "dd/MM/yyyy");
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
                    Log.e("showDate", showDate);
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

            if (!selectedReminderTimeEdit.equals(""))
            {
                try
                {
                    String time = selectedReminderTimeEdit.trim();
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

                    finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.trim();

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
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView txtNo;
        final TextView txtYes;
        llCancel = sheetView.findViewById(R.id.llCancel);
        llRemove = sheetView.findViewById(R.id.llRemove);
        tvTitle = sheetView.findViewById(R.id.tvTitle);
        tvDescription = sheetView.findViewById(R.id.tvDescription);
        txtNo = sheetView.findViewById(R.id.txtNo);
        txtYes = sheetView.findViewById(R.id.txtYes);
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

    private void setMenuAdapter()
    {
        listMenu = new ArrayList<MenuGetSet>();
        listMenuDashBoard = new ArrayList<MenuGetSet>();

        if (sessionManager.isAdmin())
        {
            listMenu.add(new MenuGetSet("15", "Profile", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenu.add(new MenuGetSet("1", "Client List", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenu.add(new MenuGetSet("2", "Employee List", getResources().getDrawable(R.drawable.ic_employee_list_navigation)));
            listMenu.add(new MenuGetSet("10", "To Do List", getResources().getDrawable(R.drawable.ic_to_do_list_navigatoin)));
            listMenu.add(new MenuGetSet("17", "DAR Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("3", "Working Days", getResources().getDrawable(R.drawable.ic_working_day_navigation)));
            listMenu.add(new MenuGetSet("4", "AUM Report", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenu.add(new MenuGetSet("5", "Manage Scheme", getResources().getDrawable(R.drawable.ic_manage_scheme_navigation)));
            listMenu.add(new MenuGetSet("6", "Manage Activity Type", getResources().getDrawable(R.drawable.ic_manage_activity_navigation)));
            listMenu.add(new MenuGetSet("7", "View Daily Activity Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("20", "Leads Tracker", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("21", "Learning & Manuals", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("22", "Task Summary", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("25", "Leads", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("8", "Change Password", getResources().getDrawable(R.drawable.ic_change_password_new)));
            listMenu.add(new MenuGetSet("9", "Logout", getResources().getDrawable(R.drawable.ic_logout_temp)));
        }
        else
        {
            listMenu.add(new MenuGetSet("15", "Profile", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenu.add(new MenuGetSet("16", "Client List", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenu.add(new MenuGetSet("10", "To Do List", getResources().getDrawable(R.drawable.ic_to_do_list_navigatoin)));
            listMenu.add(new MenuGetSet("17", "DAR Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("18", "AUM Data Entry", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenu.add(new MenuGetSet("19", "AUM Report", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenu.add(new MenuGetSet("21", "Learning & Manuals", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("24", "Monthly Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("25", "Leads", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenu.add(new MenuGetSet("8", "Change Password", getResources().getDrawable(R.drawable.ic_change_password_new)));
            listMenu.add(new MenuGetSet("9", "Logout", getResources().getDrawable(R.drawable.ic_logout_temp)));
        }

        if (sessionManager.isAdmin())
        {
            listMenuDashBoard.add(new MenuGetSet("1", "Client List", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("2", "Employee List", getResources().getDrawable(R.drawable.ic_employee_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("10", "To Do List", getResources().getDrawable(R.drawable.ic_to_do_list_navigatoin)));
            listMenuDashBoard.add(new MenuGetSet("17", "DAR Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            //listMenuDashBoard.add(new MenuGetSet("3", "Working Days", getResources().getDrawable(R.drawable.ic_working_day_navigation)));
            listMenuDashBoard.add(new MenuGetSet("4", "AUM Report", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenuDashBoard.add(new MenuGetSet("20", "Leads Tracker", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenuDashBoard.add(new MenuGetSet("21", "Learning & Manuals", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            //listMenuDashBoard.add(new MenuGetSet("5", "Manage Scheme", getResources().getDrawable(R.drawable.ic_manage_scheme_navigation)));
            //listMenuDashBoard.add(new MenuGetSet("6", "Manage Activity Type", getResources().getDrawable(R.drawable.ic_manage_activity_navigation)));
            listMenuDashBoard.add(new MenuGetSet("25", "Leads", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("26", "Leads\nReport", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("27", "Cumulative Leads Report", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("7", "View Daily Activity Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
        }
        else
        {
            listMenuDashBoard.add(new MenuGetSet("15", "Profile", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("16", "Client List", getResources().getDrawable(R.drawable.ic_client_list_navigation)));
            listMenuDashBoard.add(new MenuGetSet("10", "To Do List", getResources().getDrawable(R.drawable.ic_to_do_list_navigatoin)));
            listMenuDashBoard.add(new MenuGetSet("17", "DAR Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenuDashBoard.add(new MenuGetSet("18", "AUM Data Entry", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenuDashBoard.add(new MenuGetSet("19", "AUM Report", getResources().getDrawable(R.drawable.ic_aum_reports_navigation)));
            listMenuDashBoard.add(new MenuGetSet("21", "Learning & Manuals", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenuDashBoard.add(new MenuGetSet("24", "Monthly Report", getResources().getDrawable(R.drawable.ic_view_daily_activity_navigation)));
            listMenuDashBoard.add(new MenuGetSet("25", "Leads", getResources().getDrawable(R.drawable.ic_client_list_navigation)));

        }

        MenuAdapter menuAdapter = new MenuAdapter(listMenu, 1);
        rvMenu.setAdapter(menuAdapter);

        MenuAdapter menuAdapter1 = new MenuAdapter(listMenuDashBoard, 2);
        rvMenuDashBoard.setAdapter(menuAdapter1);
    }

    private void getEmployeeDataForAdmin()
    {
        new AsyncTask<Void, Void, Void>()
        {
            private String message = "";

            private boolean is_success = false;

            @Override
            protected void onPreExecute()
            {
                try
                {
                    llLoading.setVisibility(View.VISIBLE);
                    listEmployee = new ArrayList<AssignedClientGetSetOld>();
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
                getAllEmployee();
                return null;
            }

            private void getAllEmployee()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("clientid", "0");

                    AppUtils.printLog(activity, "Get_All_Employee Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.Get_All_Employee, hashMap);

                    AppUtils.printLog(activity, "Get_All_Employee Response ", response);

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            if (dataObj.has("AllEmployee"))
                            {
                                JSONArray dataArray = dataObj.getJSONArray("AllEmployee");

                                if (dataArray.length() > 0)
                                {
                                    for (int i = 0; i < dataArray.length(); i++)
                                    {
                                        AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                        JSONObject dataObject = (JSONObject) dataArray.get(i);
                                        assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                        assignedClientGetSetOld.setFirst_name(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name"));
                                        assignedClientGetSetOld.setLast_name(AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                        assignedClientGetSetOld.setContact_no(AppUtils.getValidAPIStringResponseHas(dataObject, "contact_no"));
                                        assignedClientGetSetOld.setEmail(AppUtils.getValidAPIStringResponseHas(dataObject, "email"));
                                        assignedClientGetSetOld.setAddress(AppUtils.getValidAPIStringResponseHas(dataObject, "address"));
                                        assignedClientGetSetOld.setCountry_name(AppUtils.getValidAPIStringResponseHas(dataObject, "country_name"));
                                        assignedClientGetSetOld.setState_name(AppUtils.getValidAPIStringResponseHas(dataObject, "state_name"));
                                        assignedClientGetSetOld.setCity_name(AppUtils.getValidAPIStringResponseHas(dataObject, "city_name"));
                                        assignedClientGetSetOld.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                        assignedClientGetSetOld.setCountry_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "country_id"));
                                        assignedClientGetSetOld.setState_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "state_id"));
                                        assignedClientGetSetOld.setIs_active(AppUtils.getValidAPIBooleanResponseHas(dataObject, "is_active"));
                                        listEmployee.add(assignedClientGetSetOld);
                                    }
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
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);

                try
                {
                    llLoading.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        if (listEmployee.size() > 0)
                        {

                            tvEmployee.setText(listEmployee.get(0).getFirst_name() + " " + listEmployee.get(0).getLast_name());
                            selectedEmployee = String.valueOf(listEmployee.get(0).getId());
                            // sessionManager.setEmpIdForAdmin(selectedEmployee);

                            getDashboardData(true);
                        }
                        else
                        {
                            Toast.makeText(activity, "No Employee found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e1)
                {
                    e1.printStackTrace();
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void getDashboardData(final boolean isForMonthYear)
    {
        new AsyncTask<Void, Void, Void>()
        {
            boolean isSuccessful = false;

            double Target_meetings_exisiting = 0;
            double Target_meetings_new = 0;
            double Target_reference_client = 0;
            double Target_fresh_aum = 0;
            double Target_sip = 0;
            final double Target_new_clients_converted = 0;
            double Target_self_acquired_aum = 0;
            double TotalClient = 0;
            double MeetingWithClient = 0;
            double Actual_Reference = 0;
            double Actual_SIP = 0;
            double Actual_fresh_aum = 0;
            double Actual_fresh_aum_TillDate = 0;
            double Actual_New_Meeting = 0;
            double Actual_Existing_Meeting = 0;
            double Actual_NewClientsConverted = 0;
            double Actual_SelfAcquiredAUM = 0;
            double OverallProgress = 0;
            double TotalTask = 0;
            double TotalCompletedTask = 0;
            double TotalEmployeeForAdmin = 0;
            double TotalClientForAdmin = 0;
            double TotalSchemeForAdmin = 0;

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
                llRetry.setVisibility(View.GONE);
                //llLoading.setVisibility(View.VISIBLE);
                txtLoading.setText(activity.getResources().getString(R.string.loading_progress_message));
                pbLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                if (isForMonthYear)
                {
                    getMonthYearData();
                }
                getDashboardData();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                super.onPostExecute(aVoid);
                llLoading.setVisibility(View.GONE);

                if (isSuccessful)
                {
                    llViewPager.setVisibility(View.GONE);
                    circlePageIndicator.setVisibility(View.VISIBLE);
                    //arc_progress.setProgress((int) OverallProgress);
                    try
                    {
                        ProgressBarAnimation animation = new ProgressBarAnimation(arc_progress, 0, (int) OverallProgress);
                        animation.setDuration(1500);
                        arc_progress.startAnimation(animation);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    tvClient1.setText(AppUtils.formatDoubleIntoString(Actual_NewClientsConverted));
                    tvClient2.setText(AppUtils.formatDoubleIntoString(TotalClient));
                    tvReference1.setText(AppUtils.formatDoubleIntoString(Actual_Reference));
                    tvReference2.setText(AppUtils.formatDoubleIntoString(Target_reference_client));
                    tvExMeeting1.setText(AppUtils.formatDoubleIntoString(Actual_Existing_Meeting));
                    tvExMeeting2.setText(AppUtils.formatDoubleIntoString(Target_meetings_exisiting));
                    tvNewMeeting1.setText(AppUtils.formatDoubleIntoString(Actual_New_Meeting));
                    tvNewMeeting2.setText(AppUtils.formatDoubleIntoString(Target_meetings_new));
                    tvToDo1.setText(AppUtils.formatDoubleIntoString(TotalCompletedTask));
                    tvToDo2.setText(AppUtils.formatDoubleIntoString(TotalTask));


                    if (listMonth != null && listMonth.size() > 0)
                    {
                        for (int i = 0; i < listMonth.size(); i++)
                        {
                            if (selectedMonth.equals(String.valueOf(listMonth.get(i).getId())))
                            {
                                tvMonth.setText(listMonth.get(i).getName());
                            }
                        }
                    }

                    if (listYear != null && listYear.size() > 0)
                    {
                        for (int i = 0; i < listYear.size(); i++)
                        {
                            if (selectedYear.equals(String.valueOf(listYear.get(i))))
                            {
                                tvYear.setText(listYear.get(i).trim());
                            }
                        }
                    }

                    List<DashboardPagerGetSet> list = new ArrayList<>();
                    DashboardPagerGetSet getSet = new DashboardPagerGetSet("SIP", AppUtils.formatDoubleIntoString(Actual_SIP), AppUtils.formatDoubleIntoString(Target_sip));
                    list.add(getSet);
                    getSet = new DashboardPagerGetSet("Fresh AUM", AppUtils.formatDoubleIntoString(Actual_fresh_aum), AppUtils.formatDoubleIntoString(Target_fresh_aum));
                    list.add(getSet);
                    getSet = new DashboardPagerGetSet("Self Acquired AUM", AppUtils.formatDoubleIntoString(Actual_SelfAcquiredAUM), AppUtils.formatDoubleIntoString(Target_self_acquired_aum));
                    list.add(getSet);

                    setPagerData(list);
                }
                else
                {
                    llViewPager.setVisibility(View.GONE);
                    circlePageIndicator.setVisibility(View.GONE);

                    tvClient1.setText("0");
                    tvClient2.setText("0");
                    tvReference1.setText("0");
                    tvReference2.setText("0");
                    tvExMeeting1.setText("0");
                    tvExMeeting2.setText("0");
                    tvNewMeeting1.setText("0");
                    tvNewMeeting2.setText("0");
                }

            }

            private void getDashboardData()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("CurrentYear", selectedYear);
                    hashMap.put("CurrentMonth", selectedMonth);
                    hashMap.put("employee_id", selectedEmployee);
                    AppUtils.printLog(activity, "Dashboard Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DASHBOARD_DATA, hashMap);

                    AppUtils.printLog(activity, "Dashboard Response ", response);

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccessful = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    if (isSuccessful)
                    {
                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObjMain = jsonObject.getJSONObject("data");

                            if (dataObjMain.has("DashboardData"))
                            {
                                JSONObject dataObj = dataObjMain.getJSONObject("DashboardData");
                                Target_meetings_exisiting = dataObj.optDouble("Target_meetings_exisiting");
                                Target_meetings_new = dataObj.optDouble("Target_meetings_new");
                                Target_reference_client = dataObj.optDouble("Target_reference_client");
                                Target_fresh_aum = dataObj.optDouble("Target_fresh_aum");
                                Target_self_acquired_aum = dataObj.optDouble("Target_self_acquired_aum");
                                TotalClient = dataObj.optDouble("TotalClient");
                                MeetingWithClient = dataObj.optDouble("MeetingWithClient");
                                Actual_Reference = dataObj.optDouble("Actual_Reference");
                                Actual_SIP = dataObj.optDouble("Actual_SIP");
                                Target_sip = dataObj.optDouble("Target_sip");
                                Actual_fresh_aum = dataObj.optDouble("Actual_fresh_aum");
                                Actual_fresh_aum_TillDate = dataObj.optDouble("Actual_fresh_aum_TillDate");
                                Actual_New_Meeting = dataObj.optDouble("Actual_New_Meeting");
                                Actual_Existing_Meeting = dataObj.optDouble("Actual_Existing_Meeting");
                                Actual_NewClientsConverted = dataObj.optDouble("Actual_NewClientsConverted");
                                Actual_SelfAcquiredAUM = dataObj.optDouble("Actual_SelfAcquiredAUM");
                                OverallProgress = dataObj.optDouble("OverallProgress");
                            }

                            if (dataObjMain.has("DashboardDataForAdmin"))
                            {
                                JSONObject dataObjForAdmin = dataObjMain.getJSONObject("DashboardDataForAdmin");
                                TotalTask = dataObjForAdmin.optDouble("TotalTask");
                                TotalCompletedTask = dataObjForAdmin.optDouble("TotalCompletedTask");
                                TotalEmployeeForAdmin = dataObjForAdmin.optDouble("TotalEmployeeForAdmin");
                                TotalClientForAdmin = dataObjForAdmin.optDouble("TotalClientForAdmin");
                                TotalSchemeForAdmin = dataObjForAdmin.optDouble("TotalSchemeForAdmin");
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            private void getMonthYearData()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    AppUtils.printLog(activity, "month year Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.MONTH_YEAR, hashMap);

                    AppUtils.printLog(activity, "month year Response ", response);

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
                                    if (yearArray.get(i).equals(selectedYear))
                                    {
                                        break;
                                    }
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
                                    if (getSet.getId() == Integer.parseInt(selectedMonth))
                                    {
                                        break;
                                    }
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

        }.execute();
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

        TextView btnNo = listDialog.findViewById(R.id.btnNo);
        TextView btnYes = listDialog.findViewById(R.id.btnYes);
        TextView tvTitle = listDialog.findViewById(R.id.tvTitle);
        EditText edtSearch = listDialog.findViewById(R.id.edtSearch);
        TextView tvDone = listDialog.findViewById(R.id.tvDone);
        final RecyclerView rvListDialog = listDialog.findViewById(R.id.rvDialog);
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
                    //tvClient.setText("Select Client");
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
                                    flt_employeeIds = flt_employeeIds + "," + flt_Employee_list.get(i).getEmployee_id();
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
                                    flt_clientIds = flt_clientIds + "," + flt_Client_list.get(i).getClient_id();
                                    flt_clientName = flt_clientName + "," + flt_Client_list.get(i).getClient_name();
                                }
                            }
                        }

                        if (flt_clientIds.length() > 0)
                        {
                            //tvClient.setText(flt_clientName);
                        }
                        else
                        {
                            //tvClient.setText("Select Client");
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
                                    add_task_clientIds = add_task_clientIds + "," + listClient.get(i).getId();
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
                                    add_task_clientIds = add_task_clientIds + "," + listClient.get(i).getId();
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

                holder.checkBox.setChecked(filterEmployeeListBean.isSelected());


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

                holder.checkBox.setChecked(getSet.isSelected());

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

                holder.checkBox.setChecked(getSet.isSelected());

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

                holder.checkBox.setChecked(getSet.isSelected());

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

                holder.checkBox.setChecked(getSet.isSelected());

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
                llMain = itemView.findViewById(R.id.llMain);
                txtName = itemView.findViewById(R.id.txtName);
                txtUserSortName = itemView.findViewById(R.id.txtUserSortName);
                txtEmail = itemView.findViewById(R.id.txtEmail);
                viewLine = itemView.findViewById(R.id.viewLine);
                checkBox = itemView.findViewById(R.id.checkBox);
            }
        }
    }

    public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>
    {
        ArrayList<MenuGetSet> listItems;
        int isFor = 0;

        MenuAdapter(ArrayList<MenuGetSet> listItems, int isFor)
        {
            this.listItems = listItems;
            this.isFor = isFor;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            if (isFor == 1)
            {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_menu_new, viewGroup, false);
                return new ViewHolder(v);
            }
            else
            {
                View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_menu_grid, viewGroup, false);
                return new ViewHolder(v);
            }
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final MenuGetSet getSet = listItems.get(position);

            holder.txtMenu.setText(getSet.getMenuName());

            holder.imgMenu.setImageDrawable(getSet.getDrawable());

            if (getSet.getMenuId().equalsIgnoreCase("1"))
            {
                //  holder.txtMenuCount.setText("(" + AppUtils.formatDoubleIntoString(TotalClientForAdmin) + ")");
            }
            else if (getSet.getMenuId().equalsIgnoreCase("2"))
            {
                // holder.txtMenuCount.setText("(" + AppUtils.formatDoubleIntoString(TotalEmployeeForAdmin) + ")");
            }
            else if (getSet.getMenuId().equalsIgnoreCase("5"))
            {
                //  holder.txtMenuCount.setText("(" + AppUtils.formatDoubleIntoString(TotalSchemeForAdmin) + ")");
            }

            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    try
                    {
                        if (isFor == 1)
                        {
                            AppUtils.openDrawerLeft(drawerLayout);
                        }

                        if (getSet.getMenuId().equalsIgnoreCase("1"))
                        {
                            Intent intent = new Intent(activity, ManageClientActivity.class);
                            intent.putExtra("selectedMonth", selectedMonth);
                            intent.putExtra("selectedMonthName", tvMonth.getText().toString().trim());
                            intent.putExtra("selectedYear", selectedYear);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("2"))
                        {
                            startActivity(new Intent(activity, ManageEmployeeActivity.class));
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("3"))
                        {
                            Intent intent = new Intent(activity, WorkingDaysActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("4"))
                        {
                            Intent intent = new Intent(activity, ViewAUMReportsListActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("5"))
                        {
                            startActivity(new Intent(activity, ManageSchemeActivity.class));
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("6"))
                        {
                            Intent intent = new Intent(activity, ActivityTypeListActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("7"))
                        {
                            startActivity(new Intent(activity, ViewDailyActivityReportListActivity.class));
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("8"))
                        {
                            showChangePasswordDialog();
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("9"))
                        {
                            if (sessionManager.isLoggedIn())
                            {
                                AppUtils.showLogoutDialog(activity, sessionManager);
                            }
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("10"))
                        {
                            startActivity(new Intent(activity, TaskListActivityNew.class));
                            AppUtils.startActivityAnimation(activity);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("15"))
                        {
                            Intent intent = new Intent(activity, EmployeeDetailsActivity.class);
                            intent.putExtra("employeeId", Integer.parseInt(sessionManager.getUserId()));
                            intent.putExtra("employeeName", sessionManager.getUserName());
                            intent.putExtra("isForEmployeeLogin", true);
                            intent.putExtra("pos", 1);
                            startActivity(intent);
                            AppUtils.startActivityAnimation(activity);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("16"))
                        {
                            Intent intent = new Intent(activity, AssignedClientListActivity.class);
                            intent.putExtra("selectedMonth", selectedMonth);
                            intent.putExtra("selectedMonthName", tvMonth.getText().toString().trim());
                            intent.putExtra("selectedYear", selectedYear);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("17"))
                        {
                            Intent intent = new Intent(activity, DARReportForEmployeeActivity.class);
                            intent.putExtra("employeeId", Integer.parseInt(sessionManager.getUserId()));
                            startActivity(intent);
                            AppUtils.startActivityAnimation(activity);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("18"))
                        {
                            Intent intent = new Intent(activity, AddAUMActivity.class);
                            intent.putExtra("clientId", "0");
                            intent.putExtra("isFor", "add");
                            intent.putExtra("AUMListGetSet", (Parcelable) new AUMListGetSet.DataBean());
                            startActivity(intent);

                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("19"))
                        {
                            Intent intent = new Intent(activity, AumEmployeeYearlySummeryActivity.class);
                            intent.putExtra("selectedEmployee", sessionManager.getUserId());
                            intent.putExtra("selectedEmployeeName", sessionManager.getUserName());
                            intent.putExtra("isForEmployeeAUM", true);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("20"))
                        {
                            Intent intent = new Intent(activity, LeadTrackerActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("21"))
                        {
                            Intent intent = new Intent(activity, LearningAndManualActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("22"))
                        {
                            Intent intent = new Intent(activity, TaskSummaryActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("23"))
                        {
                            Intent intent = new Intent(activity, AddEmployeeActivity.class);
                            intent.putExtra("isFor", "edit");
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("24"))
                        {
                            Intent intent = new Intent(activity, MonthlyReportActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("25"))
                        {
                            Intent intent = new Intent(activity, LeadsActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("26"))
                        {
                            Intent intent = new Intent(activity, LeadReportActivity.class);
                            startActivity(intent);
                        }
                        else if (getSet.getMenuId().equalsIgnoreCase("27"))
                        {
                            Intent intent = new Intent(activity, CumulativeLeadReportActivity.class);
                            startActivity(intent);
                        }
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
            return listItems.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            @BindView(R.id.imgMenu)
            ImageView imgMenu;
            @BindView(R.id.txtMenu)
            AppCompatTextView txtMenu;
            @BindView(R.id.txtMenuCount)
            AppCompatTextView txtMenuCount;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    public void showChangePasswordDialog()
    {
        BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        dialog.setContentView(sheetView);
        //AppUtils.configureBottomSheetBehavior(sheetView);
        //AppUtils.setLightStatusBarBottomDialog(dialog, activity);

        final TextView tvDialogHeader;
        final CustomTextInputLayout inputOldPassword;
        final EditText edtOldPassword;
        final CustomTextInputLayout inputNewPassword;
        final EditText edtNewPassword;
        final CustomTextInputLayout inputConfirmPassword;
        final EditText edtConfirmPassword;
        final LinearLayout llDialogYes, llLoadingChange;

        tvDialogHeader = sheetView.findViewById(R.id.tvDialogHeader);
        inputOldPassword = sheetView.findViewById(R.id.inputOldPassword);
        edtOldPassword = sheetView.findViewById(R.id.edtOldPassword);
        inputNewPassword = sheetView.findViewById(R.id.inputNewPassword);
        edtNewPassword = sheetView.findViewById(R.id.edtNewPassword);
        inputConfirmPassword = sheetView.findViewById(R.id.inputConfirmPassword);
        edtConfirmPassword = sheetView.findViewById(R.id.edtConfirmPassword);
        llDialogYes = sheetView.findViewById(R.id.llDialogYes);
        llLoadingChange = sheetView.findViewById(R.id.llLoadingChange);
        llDialogYes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    boolean isValid = true;

                    if (edtOldPassword.getText().toString().length() == 0)
                    {
                        inputOldPassword.setError("Please Enter Your Old Password.");
                        isValid = false;
                    }
                    else if (edtNewPassword.getText().toString().length() == 0)
                    {
                        inputNewPassword.setError("Please Enter Your New Password.");
                        isValid = false;
                    }
                    else if (edtConfirmPassword.getText().toString().length() == 0)
                    {
                        inputConfirmPassword.setError("Please re-enter your Password.");
                        isValid = false;
                    }
                    else if (!edtNewPassword.getText().toString().trim().equals(edtConfirmPassword.getText().toString().trim()))
                    {
                        inputConfirmPassword.setError("Password did not matched");
                        isValid = false;
                    }

                    AppUtils.removeError(edtOldPassword, inputOldPassword);
                    AppUtils.removeError(edtNewPassword, inputNewPassword);
                    AppUtils.removeError(edtConfirmPassword, inputConfirmPassword);

                    if (isValid)
                    {

                        //MitsUtils.hideKeyboard(activity);
                        hideKeyboard();
                        AppUtils.hideKeyboard(edtConfirmPassword, activity);
                        //Forgot Password api will call here
                        changePasswordAsync(edtOldPassword.getText().toString().trim(), edtConfirmPassword.getText().toString().trim(), llLoadingChange, dialog);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                hideKeyboard();
                // AppUtils.hideKeyboard(edtTaskName, activity);
            }
        });

        dialog.show();

    }

    private void changePasswordAsync(final String oldPassword, final String newPassword, final LinearLayout llLoadingChange, final BottomSheetDialog dialog)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    try
                    {
                        llLoadingChange.setVisibility(View.VISIBLE);
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
                        hashMap.put("user_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("oldpassword", oldPassword);
                        hashMap.put("newpassword", newPassword);

                        AppUtils.printLog(activity, "CHANGE_PASSWORD Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.CHANGE_PASSWORD, hashMap);

                        AppUtils.printLog(activity, "CHANGE_PASSWORD Response ", response);

                        JSONObject jsonObject = new JSONObject(response);

                        is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");
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
                    try
                    {
                        llLoadingChange.setVisibility(View.GONE);
                        if (is_success)
                        {
                            AppUtils.showToast(activity, message);
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        else
                        {
                            AppUtils.showToast(activity, message);
                        }
                    }
                    catch (Exception e1)
                    {
                        e1.printStackTrace();
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setPagerData(List<DashboardPagerGetSet> list)
    {
        CustomPageAdapter customPageAdapter = new CustomPageAdapter(activity, list);
        viewpager.setAdapter(customPageAdapter);
        circlePageIndicator.setViewPager(viewpager);
        circlePageIndicator.setFillColor(ContextCompat.getColor(activity, R.color.dash_text_blue));
        circlePageIndicator.setRadius(12);
        circlePageIndicator.setPageColor(ContextCompat.getColor(activity, R.color.dash_text_seperator));
    }

    public class CustomPageAdapter extends PagerAdapter
    {
        private final Context context;
        private final List<DashboardPagerGetSet> dataObjectList;
        private final LayoutInflater layoutInflater;

        public CustomPageAdapter(Context context, List<DashboardPagerGetSet> dataObjectList)
        {
            this.context = context;
            this.layoutInflater = (LayoutInflater) this.context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.dataObjectList = dataObjectList;
        }

        @Override
        public int getCount()
        {
            return dataObjectList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            View view = this.layoutInflater.inflate(R.layout.pager_dashboard, container, false);

            TextView tvTitle = view.findViewById(R.id.tvTitle);
            TextView tvRemaining = view.findViewById(R.id.tvRemaining);
            TextView tvTotal = view.findViewById(R.id.tvTotal);

            ImageView ivAdd = view.findViewById(R.id.ivAdd);
            if (position == 2)
            {
                ivAdd.setVisibility(View.VISIBLE);
            }
            else
            {
                ivAdd.setVisibility(View.GONE);
            }

            DashboardPagerGetSet getSet = dataObjectList.get(position);

            tvTitle.setText(getSet.getTitle());
            //tvRemaining.setText(activity.getString(R.string.ruppe)+""+getSet.getRemaining());
            //tvTotal.setText(activity.getString(R.string.ruppe)+""+getSet.getTotal());

            try
            {
                int remaining = Integer.parseInt(getSet.getRemaining());
                tvRemaining.setText(activity.getString(R.string.ruppe) + NumberFormat.getNumberInstance(Locale.getDefault()).format(remaining));
                int total = Integer.parseInt(getSet.getTotal());
                tvTotal.setText(activity.getString(R.string.ruppe) + NumberFormat.getNumberInstance(Locale.getDefault()).format(total));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            ivAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(activity, AddSelfAcquiredAumActivity.class);
                    intent.putExtra("isFor", "add");
                    intent.putExtra("position", 0);
                    startActivity(intent);
                }
            });

            view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (position == 2)
                    {
                        Intent intent = new Intent(activity, SelfAcquiredListActivity.class);
                        startActivity(intent);
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    public static class ProgressBarAnimation extends Animation
    {
        private final ArcProgress progressBar;
        private final float from;
        private final float to;

        public ProgressBarAnimation(ArcProgress progressBar, float from, float to)
        {
            super();
            this.progressBar = progressBar;
            this.from = from;
            this.to = to;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t)
        {
            super.applyTransformation(interpolatedTime, t);
            float value = from + (to - from) * interpolatedTime;
            progressBar.setProgress((int) value);
        }

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            try
            {
                if (doubleBackToExitPressedOnce)
                {
                    activity.finishAffinity();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                else
                {
                    doubleBackToExitPressedOnce = true;

                    Toast.makeText(this, "Press back again to exit the app", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);

                    return false;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public static class DataAdapter extends RecyclerView.Adapter<DataAdapter.Viewholder>
    {
        ArrayList<DashboardTaskSummaryModel> listData;

        public DataAdapter(ArrayList<DashboardTaskSummaryModel> listData)
        {
            this.listData = listData;
        }

        @NonNull
        @Override
        public DataAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int postition)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_task_summary_dashboard, parent, false);
            return new DataAdapter.Viewholder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.Viewholder holder, int position)
        {
            DashboardTaskSummaryModel getSet = listData.get(position);

            holder.tvTitle.setText(getSet.getTitle());
            holder.tvTotalTask.setText(getSet.getTotal_tasks());
            holder.tvClosedTask.setText(getSet.getTask_closed());
            holder.tvAvgDaysTaken.setText(getSet.getAvg_days_taken());
            holder.tvAvgDaysOpen.setText(getSet.getAvg_days_open());
            holder.tvOpenTask.setText(getSet.getOpen_task());
        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvTitle, tvTotalTask, tvClosedTask, tvAvgDaysTaken, tvOpenTask, tvAvgDaysOpen;

            public Viewholder(View itemView)
            {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvTotalTask = itemView.findViewById(R.id.tvTotalTask);
                tvClosedTask = itemView.findViewById(R.id.tvClosedTask);
                tvAvgDaysTaken = itemView.findViewById(R.id.tvAvgDaysTaken);
                tvOpenTask = itemView.findViewById(R.id.tvOpenTask);
                tvAvgDaysOpen = itemView.findViewById(R.id.tvAvgDaysOpen);
            }
        }
    }

    public static class DARSummaryAdapter extends RecyclerView.Adapter<DARSummaryAdapter.ViewHolder>
    {
        ArrayList<DashboardDARSummaryModel> listData;

        public DARSummaryAdapter(ArrayList<DashboardDARSummaryModel> listData)
        {
            this.listData = listData;
        }

        @NonNull
        @Override
        public DARSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_dar_summary_dashboard, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            DashboardDARSummaryModel getSet = listData.get(position);

            holder.tvTitle.setText(getSet.getTitle());
            holder.tvWorkingDays.setText(getSet.getWorking_days());
            holder.tvWorkingHours.setText(getSet.getWorking_hours());
        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvTitle, tvWorkingDays, tvWorkingHours;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvWorkingDays = itemView.findViewById(R.id.tvWorkingDays);
                tvWorkingHours = itemView.findViewById(R.id.tvWorkingHours);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        sendRegistrationToServer(sessionManager.getTokenId());
    }
}

