package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.reportApp.draganddrop.ItemMoveCallback;
import cheetah.alphacapital.reportApp.draganddrop.ItemOffsetDecoration;
import cheetah.alphacapital.reportApp.draganddrop.StartDragListener;
import cheetah.alphacapital.reportApp.getset.TaskListGetSet;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommentCountGetSet;


public class TaskListActivity extends AppCompatActivity implements StartDragListener
{
    private Activity activity;
    private SessionManager sessionManager;
    private Toolbar toolbar;
    private View viewStatusBar;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon, ivLogout;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private LinearLayout llLoading, llLoadingMore;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private LinearLayout llNoData, llAddTask;
    private ImageView ivNoData;
    private RecyclerView rvNormalTaskList;
    private LinearLayoutManager linearLayoutManager;
    private boolean isStatusBarHidden = false;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private ArrayList<TaskListGetSet> listTask = new ArrayList<TaskListGetSet>();
    private ArrayList<AssignedClientGetSetOld> listEmployee = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<AssignedClientGetSetOld> listEmployee_Search = new ArrayList<AssignedClientGetSetOld>();
    public ArrayList<AssignedClientGetSetOld> listEmployee_Selected = new ArrayList<AssignedClientGetSetOld>();
    public ArrayList<AssignedClientGetSetOld> listEmployee_Selected_Edit = new ArrayList<AssignedClientGetSetOld>();
    private TaskListAdapter taskListAdapter;
    public static Handler handler;

    private BottomSheetDialog dialog_Add_Task;
    private BottomSheetDialog dialog_Edit_Task;
    private TextView txtAddReminder;
    private TextView txtAddReminderEdit;
    private HashTagHelper hashTagHelper;
    private HashTagHelper hashTagHelperEdit;
    public static String selectedReminderDate = "", selectedReminderTime = "";
    private String finalDateTime = "";
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;

    public static String selectedReminderDateEdit = "", selectedReminderTimeEdit = "";
    private String finalDateTimeEdit = "";
    String date_timeEdit = "";
    int mYearEdit;
    int mMonthEdit;
    int mDayEdit;

    private String TaskStatusId = "0";

    private EmployeeAdpater employeeAdpater;
    private EmployeeAdpaterEdit employeeAdpaterEdit;

    private ItemTouchHelper touchHelper;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";

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

        setContentView(R.layout.activity_task_list);

        AppUtils.setLightStatusBar(activity);

        mSharedPreferences = this.getApplicationContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

        mEditor = mSharedPreferences.edit();

        setupViews();

        onClickEvents();

        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getAllTask(true, true);
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


        handler = new Handler()
        {
            public void handleMessage(Message msg)
            {
                if (msg.what == 105 && msg.obj != null)
                {
                    try
                    {
                        CommentCountGetSet commentCountGetSet = (CommentCountGetSet) msg.obj;

                        if (listTask.size() > 0 && listTask != null)
                        {
                            for (int i = 0; i < listTask.size(); i++)
                            {
                                //   Log.e("Task Id For ::", listTask.get(i).getUserTaskId() + " === " + commentCountGetSet.getTaskId() + "");

                                if (listTask.get(i).getId() == commentCountGetSet.getTaskId())
                                {
                                    //    Log.e("Task Id In Comment Count ::", commentCountGetSet.getComment() + " Task ID " + commentCountGetSet.getTaskId() + " USER TASK ID :: " + listTask.get(i).getUserTaskId() + "");
                                    TaskListGetSet taskListGetSet = listTask.get(i);
                                    taskListGetSet.setUnReadCount(commentCountGetSet.getComment());
                                    listTask.set(i, taskListGetSet);
                                    taskListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                if (msg.what == 100)
                {
                    try
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getAllTask(false, false);
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


            }
        };
    }

    private void setupViews()
    {
        try
        {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            viewStatusBar = (View) findViewById(R.id.viewStatusBar);
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            ivLogout = (ImageView) findViewById(R.id.ivLogout);
            ivLogout.setImageResource(R.drawable.t_sort);
            ivLogout.setVisibility(View.VISIBLE);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            llLoadingMore = (LinearLayout) findViewById(R.id.llLoadingMore);
            pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
            txtLoading = (TextView) findViewById(R.id.txtLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            llNoData = (LinearLayout) findViewById(R.id.llNoData);
            ivNoData = (ImageView) findViewById(R.id.ivNoData);
            llAddTask = (LinearLayout) findViewById(R.id.llAddTask);
            rvNormalTaskList = (RecyclerView) findViewById(R.id.rvNormalTaskList);
            linearLayoutManager = new LinearLayoutManager(activity);
            rvNormalTaskList.setLayoutManager(linearLayoutManager);
            setSupportActionBar(toolbar);

            /*ImageView ivHeader = findViewById(R.id.ivHeader);
            ivHeader.setImageResource(R.drawable.img_portfolio);

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

            ivLogo.setVisibility(View.GONE);
            llNotification.setVisibility(View.GONE);
            txtTitle.setText("Task List");
            llBackNavigation.setVisibility(View.VISIBLE);


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

        llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true, true);
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

        llAddTask.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showAddTaskDialog();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    showFilterDialog();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getAllTask(final boolean isLoadingVisible, final boolean isgetAllEmployee)
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
                    isLoading = true;

                    listTask =new ArrayList<TaskListGetSet>();
                    if (isLoadingVisible)
                    {
                        llLoading.setVisibility(View.VISIBLE);
                    }
                    if (isgetAllEmployee)
                    {
                        listEmployee = new ArrayList<AssignedClientGetSetOld>();
                    }

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
                getAllTaskFromApi();

                if (isgetAllEmployee)
                {
                    getAllEmployee();
                }


                return null;
            }

            private void getAllTaskFromApi()
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("employeeid", AppUtils.getEmployeeIdForAdmin(activity));
                    hashMap.put("pageindex", "0");
                    hashMap.put("pagesize", "0");
                    hashMap.put("TaskStatusId", TaskStatusId);
                    hashMap.put("SearchText", "");

                    AppUtils.printLog(activity, "GET_ALL_TASK Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_ALL_TASK, hashMap);

                    AppUtils.printLog(activity, "GET_ALL_TASK Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    is_success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                    message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                    if (is_success)
                    {

                        if (jsonObject.has("data"))
                        {
                            JSONObject dataObj = jsonObject.getJSONObject("data");

                            if (dataObj.has("TaskList"))
                            {
                                JSONArray dataTaskListArray = dataObj.getJSONArray("TaskList");


                                if (dataObj.has("PinnedTaskList"))
                                {
                                    JSONArray dataPinnedTaskListArray = dataObj.getJSONArray("PinnedTaskList");

                                    if (dataPinnedTaskListArray.length() > 0)
                                    {
                                        for (int i = 0; i < dataPinnedTaskListArray.length(); i++)
                                        {
                                            TaskListGetSet taskListGetSet = new TaskListGetSet();
                                            JSONObject dataObject = (JSONObject) dataPinnedTaskListArray.get(i);
                                            taskListGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                            taskListGetSet.setEmployee_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "employee_id"));
                                            taskListGetSet.setTask_status_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "task_status_id"));
                                            taskListGetSet.setTask_message(AppUtils.getValidAPIStringResponseHas(dataObject, "task_message"));
                                            taskListGetSet.setDue_date(AppUtils.getValidAPIStringResponseHas(dataObject, "due_date"));
                                            taskListGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                            taskListGetSet.setFormated_created_date(AppUtils.getValidAPIStringResponseHas(dataObject, "formated_created_date"));
                                            taskListGetSet.setTask_added_by(AppUtils.getValidAPIStringResponseHas(dataObject, "task_added_by"));
                                            taskListGetSet.setTask_status(AppUtils.getValidAPIStringResponseHas(dataObject, "task_status"));
                                            taskListGetSet.setAll_employee_ids(AppUtils.getValidAPIStringResponseHas(dataObject, "all_employee_ids"));
                                            taskListGetSet.setAll_employee_name(AppUtils.getValidAPIStringResponseHas(dataObject, "all_employee_name"));
                                            taskListGetSet.setPinedTask(AppUtils.getValidAPIBooleanResponseHas(dataObject, "IsPinedTask"));
                                            taskListGetSet.setUnReadCount(AppUtils.getValidAPIIntegerResponseHas(dataObject, "unReadCount"));

                                            if (dataObject.has("lstemployee"))
                                            {
                                                ArrayList<AssignedClientGetSetOld> list_Emp = new ArrayList<AssignedClientGetSetOld>();
                                                JSONArray dataListEmployee = dataObject.getJSONArray("lstemployee");

                                                if (dataListEmployee.length() > 0)
                                                {
                                                    for (int j = 0; j < dataListEmployee.length(); j++)
                                                    {
                                                        JSONObject dataEmployee = (JSONObject) dataListEmployee.get(j);
                                                        AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                                        assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataEmployee, "employee_id"));
                                                        assignedClientGetSetOld.setName(AppUtils.getValidAPIStringResponseHas(dataEmployee, "employee_name"));
                                                        list_Emp.add(assignedClientGetSetOld);
                                                    }
                                                }

                                                taskListGetSet.setListEmployee(list_Emp);
                                            }

                                            listTask.add(taskListGetSet);
                                        }
                                    }
                                }

                                if (dataTaskListArray.length() > 0)
                                {
                                    for (int i = 0; i < dataTaskListArray.length(); i++)
                                    {
                                        TaskListGetSet taskListGetSet = new TaskListGetSet();
                                        JSONObject dataObject = (JSONObject) dataTaskListArray.get(i);
                                        taskListGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataObject, "id"));
                                        taskListGetSet.setEmployee_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "employee_id"));
                                        taskListGetSet.setTask_status_id(AppUtils.getValidAPIIntegerResponseHas(dataObject, "task_status_id"));
                                        taskListGetSet.setTask_message(AppUtils.getValidAPIStringResponseHas(dataObject, "task_message"));
                                        taskListGetSet.setDue_date(AppUtils.getValidAPIStringResponseHas(dataObject, "due_date"));
                                        taskListGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataObject, "created_date"));
                                        taskListGetSet.setFormated_created_date(AppUtils.getValidAPIStringResponseHas(dataObject, "formated_created_date"));
                                        taskListGetSet.setTask_added_by(AppUtils.getValidAPIStringResponseHas(dataObject, "task_added_by"));
                                        taskListGetSet.setTask_status(AppUtils.getValidAPIStringResponseHas(dataObject, "task_status"));
                                        taskListGetSet.setAll_employee_ids(AppUtils.getValidAPIStringResponseHas(dataObject, "all_employee_ids"));
                                        taskListGetSet.setAll_employee_name(AppUtils.getValidAPIStringResponseHas(dataObject, "all_employee_name"));
                                        taskListGetSet.setPinedTask(AppUtils.getValidAPIBooleanResponseHas(dataObject, "IsPinedTask"));
                                        taskListGetSet.setUnReadCount(AppUtils.getValidAPIIntegerResponseHas(dataObject, "unReadCount"));

                                        if (dataObject.has("lstemployee"))
                                        {
                                            ArrayList<AssignedClientGetSetOld> list_Emp = new ArrayList<AssignedClientGetSetOld>();
                                            JSONArray dataListEmployee = dataObject.getJSONArray("lstemployee");

                                            if (dataListEmployee.length() > 0)
                                            {
                                                for (int j = 0; j < dataListEmployee.length(); j++)
                                                {
                                                    JSONObject dataEmployee = (JSONObject) dataListEmployee.get(j);
                                                    AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                                                    assignedClientGetSetOld.setId(AppUtils.getValidAPIIntegerResponseHas(dataEmployee, "employee_id"));
                                                    assignedClientGetSetOld.setName(AppUtils.getValidAPIStringResponseHas(dataEmployee, "employee_name"));
                                                    list_Emp.add(assignedClientGetSetOld);
                                                }
                                            }

                                            taskListGetSet.setListEmployee(list_Emp);
                                        }

                                        listTask.add(taskListGetSet);
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

                    AppUtils.printLog(activity, "Get_All_Employee Response ", response.toString());

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
                                        String toDisplayName = AppUtils.toDisplayCase(AppUtils.getValidAPIStringResponseHas(dataObject, "first_name") + "" + AppUtils.getValidAPIStringResponseHas(dataObject, "last_name"));
                                        assignedClientGetSetOld.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
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
                    isLoading = false;
                    llLoading.setVisibility(View.GONE);
                    llLoadingMore.setVisibility(View.GONE);
                    llNoInternet.setVisibility(View.GONE);

                    if (is_success)
                    {
                        if (listTask.size() == 0 && listTask.size() == 0)
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.GONE);

                            if (listTask.size() > 0)
                            {
                                rvNormalTaskList.setVisibility(View.VISIBLE);
                                setNormalListAdapter(getSortTaskList(listTask));
                            }
                            else
                            {
                                rvNormalTaskList.setVisibility(View.GONE);
                            }
                        }
                    }
                    else
                    {
                        llNoData.setVisibility(View.VISIBLE);
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

    private void setNormalListAdapter(ArrayList<TaskListGetSet> adapterList)
    {
        try
        {
            final int spacing = getResources().getDimensionPixelOffset(R.dimen.default_spacing_small);
            taskListAdapter = new TaskListAdapter(adapterList, activity, this);
            ItemTouchHelper.Callback callback = new ItemMoveCallback(taskListAdapter);
            touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(rvNormalTaskList);
            rvNormalTaskList.setAdapter(taskListAdapter);
            ViewCompat.setNestedScrollingEnabled(rvNormalTaskList, false);
            rvNormalTaskList.addItemDecoration(new ItemOffsetDecoration(spacing));
            //  runLayoutAnimation(rvNormalTaskList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private ArrayList<TaskListGetSet> getSortTaskList(ArrayList<TaskListGetSet> apiList)
    {
        //  Log.e("*** API LIST SIZE ", apiList.size() + " abcd ");
        final List<TaskListGetSet> sortedList = new ArrayList<TaskListGetSet>();
        final ArrayList<TaskListGetSet> adapterList = new ArrayList<TaskListGetSet>();
        adapterList.addAll(apiList);

        //get the JSON array of the ordered of sorted customers
        String jsonListOfSortedCustomerId = mSharedPreferences.getString(LIST_OF_SORTED_DATA_ID, "");

        //check for null
        if (!jsonListOfSortedCustomerId.isEmpty())
        {
            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<String> dbList = gson.fromJson(jsonListOfSortedCustomerId, new TypeToken<List<String>>()
            {
            }.getType());

            // Log.e("*** STORE LIST SIZE ", dbList.size() + " abcd ");

            final ArrayList<TaskListGetSet> actionList = new ArrayList<TaskListGetSet>();

            actionList.addAll(adapterList);

            adapterList.clear();

            // Log.e("*** STORE LIST SIZE ", dbList.size() + " abcd ");


            //build sorted list
            if (dbList != null && dbList.size() > 0)
            {
                for (String id : dbList)
                {
                    for (TaskListGetSet taskListGetSet : actionList)
                    {
                        if (String.valueOf(taskListGetSet.getId()).equalsIgnoreCase(String.valueOf(id)))
                        {
                            sortedList.add(taskListGetSet);
                            actionList.remove(taskListGetSet);
                            break;
                        }
                    }
                }
            }

            //  Log.e("*** STORE ActionList SIZE ", actionList.size() + " abcd ");

            //  Log.e("*** STORE SortedList SIZE ", sortedList.size() + " abcd ");

            List<TaskListGetSet> formatList = new ArrayList<TaskListGetSet>();

            if (actionList.size() > 0)
            {
                formatList.addAll(actionList);
            }

            formatList.addAll(sortedList);

            adapterList.addAll(formatList);

            // Log.e("*** AFTER SORT LIST SIZE", adapterList.size() + " abcd ");

            return adapterList;
        }
        else
        {

            //  Log.e("*** STORE LIST SIZE ELSE", adapterList.size() + " abcd ");

            return adapterList;
        }
    }

    public void onNoteListChanged(List<TaskListGetSet> taskList)
    {
        List<String> listOfSortedCustomerId = new ArrayList<String>();

        for (TaskListGetSet taskListGetSet : taskList)
        {
            listOfSortedCustomerId.add(String.valueOf(taskListGetSet.getId()));
        }

        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedCustomerId);

        //save to SharedPreference
        mEditor.putString(LIST_OF_SORTED_DATA_ID, jsonListOfSortedCustomerIds).commit();
        mEditor.commit();

    }

    private void runLayoutAnimation(final RecyclerView recyclerView)
    {
        final Context context = recyclerView.getContext();

        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder)
    {
        touchHelper.startDrag(viewHolder);
    }

    public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> implements ItemMoveCallback.ItemTouchHelperContract
    {
        ArrayList<TaskListGetSet> listItems;
        private Activity activity;
        private SessionManager sessionManager;
        private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
        private final StartDragListener mStartDragListener;

        public TaskListAdapter(ArrayList<TaskListGetSet> listClient, Activity activity, StartDragListener startDragListener)
        {
            this.listItems = listClient;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
            viewBinderHelper.setOpenOnlyOne(true);
            mStartDragListener = startDragListener;
        }

        @Override
        public TaskListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_list, viewGroup, false);
            return new TaskListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final TaskListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final TaskListGetSet getSet = listItems.get(position);
                viewBinderHelper.bind(holder.swipeLayout, String.valueOf(getSet.getId()));
                holder.txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(getSet.getTask_message())));

                if (getSet.getUnReadCount() > 0)
                {
                    holder.txtUnReadCount.setVisibility(View.VISIBLE);
                    holder.txtUnReadCount.setText(String.valueOf(getSet.getUnReadCount()));
                }
                else
                {
                    holder.txtUnReadCount.setVisibility(View.GONE);
                }

                if(getSet.isPinedTask())
                {
                    holder.ivPinTask.setVisibility(View.VISIBLE);

                    holder.txtPinUnPin.setText("UnPin Task");

                    holder.ivPinUnPin.setImageResource(R.drawable.t_unpin_white);

                }
                else
                {
                    holder.ivPinTask.setVisibility(View.GONE);

                    holder.txtPinUnPin.setText("Pin Task");

                    holder.ivPinUnPin.setImageResource(R.drawable.t_pin_white);
                }

                if (getSet.getTask_status_id() == 2)
                {
                    holder.llDelete.setVisibility(View.VISIBLE);
                    holder.llEdit.setVisibility(View.GONE);
                    holder.llComplete.setVisibility(View.GONE);
                    holder.llPinUnPin.setVisibility(View.GONE);
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else
                {
                    holder.llDelete.setVisibility(View.VISIBLE);
                    holder.llEdit.setVisibility(View.VISIBLE);
                    holder.llComplete.setVisibility(View.VISIBLE);
                    holder.llPinUnPin.setVisibility(View.VISIBLE);
                    holder.txtTaskName.setPaintFlags(holder.txtTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }


                holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener()
                {
                    @Override
                    public boolean onLongClick(View view)
                    {
                        if(getSet.isPinedTask())
                        {
                            AppUtils.showToast(activity,"You can't swipe Pin task.");
                        }
                        else 
                        {
                            if (listItems.size() > 1)
                            {
                                mStartDragListener.requestDrag(holder);
                            }
                        }
                      
                        return false;
                    }
                });

                holder.llEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            holder.swipeLayout.close(true);
                            showEditTaskDialog(getSet);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.llDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            holder.swipeLayout.close(true);
                            selectDeleteDialog(getSet, position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.llPinUnPin.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            holder.swipeLayout.close(true);
                            pinTask(getSet, position);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                holder.llComplete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        holder.swipeLayout.close(true);

                        if (getSet.getDue_date().length() > 0)
                        {
                            editTaskAsync(getSet.getTask_message().toString().trim(), getSet.getAll_employee_ids(), AppUtils.universalDateConvert(getSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"), getSet.getId(), "2");
                        }
                        else
                        {
                            editTaskAsync(getSet.getTask_message().toString().trim(), getSet.getAll_employee_ids(), "", getSet.getId(), "2");
                        }
                    }
                });

                holder.mainLayout.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(activity, TaskDetailsActivity.class);
                        intent.putExtra("task_id", String.valueOf(getSet.getId()));
                        intent.putExtra("task_added_by", getSet.getTask_added_by());
                        startActivity(intent);
                    }
                });

                setViewHeight(holder.mainLayout, holder.layoutAction);

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

        @Override
        public void onRowMoved(int fromPosition, int toPosition)
        {
            try
            {
                if(listItems.get(toPosition).isPinedTask() || toPosition ==0)
                {
                    AppUtils.showToast(activity,"");
                    AppUtils.showToast(activity,"You can't swipe task over the pin task.");
                }
                else
                {
                    Collections.swap(listItems, fromPosition, toPosition);
                    onNoteListChanged(listItems);
                    notifyItemMoved(fromPosition, toPosition);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        public void onRowSelected(ViewHolder myViewHolder)
        {
            try
            {
                myViewHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.demoo2));
            }
            catch (Exception e)
            {
                myViewHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                e.printStackTrace();
            }
        }

        @Override
        public void onRowClear(ViewHolder myViewHolder)
        {
            try
            {
                int pos = myViewHolder.getAdapterPosition();
                // myViewHolder.mainLayout.setBackgroundColor(Color.parseColor(listItems.get(pos).getBackColor()));

                myViewHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
            }
            catch (Exception e)
            {
                myViewHolder.mainLayout.setBackgroundColor(ContextCompat.getColor(activity, R.color.white));
                e.printStackTrace();
            }
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private SwipeRevealLayout swipeLayout;
            private FrameLayout layoutAction;
            private LinearLayout llDelete, llActionMain;
            private LinearLayout llEdit;
            private LinearLayout llComplete;
            private FrameLayout mainLayout;
            private LinearLayout llMain, llPinUnPin;
            private TextView txtTaskName, txtPinUnPin, txtUnReadCount;
            private View viewLine;
            private ImageView ivPinTask, ivPinUnPin;

            ViewHolder(View convertView)
            {
                super(convertView);
                swipeLayout = (SwipeRevealLayout) convertView.findViewById(R.id.swipe_layout);
                layoutAction = (FrameLayout) convertView.findViewById(R.id.layout_action);
                llActionMain = (LinearLayout) convertView.findViewById(R.id.llActionMain);
                llDelete = (LinearLayout) convertView.findViewById(R.id.llDelete);
                llEdit = (LinearLayout) convertView.findViewById(R.id.llEdit);
                llComplete = (LinearLayout) convertView.findViewById(R.id.llComplete);
                mainLayout = (FrameLayout) convertView.findViewById(R.id.main_layout);
                llMain = (LinearLayout) convertView.findViewById(R.id.llMain);
                ivPinTask = (ImageView) convertView.findViewById(R.id.ivPinTask);
                txtTaskName = (TextView) convertView.findViewById(R.id.txtTaskName);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
                llPinUnPin = (LinearLayout) convertView.findViewById(R.id.llPinUnPin);
                ivPinUnPin = (ImageView) convertView.findViewById(R.id.ivPinUnPin);
                txtPinUnPin = (TextView) convertView.findViewById(R.id.txtPinUnPin);
                txtUnReadCount = (TextView) convertView.findViewById(R.id.txtUnReadCount);
            }
        }
    }

    private void setViewHeight(final FrameLayout mainLayout, final FrameLayout layoutAction)
    {
        ViewTreeObserver vto = mainLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
        {
            @Override
            public void onGlobalLayout()
            {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                {
                    mainLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                else
                {
                    mainLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = mainLayout.getMeasuredWidth();
                int height = mainLayout.getMeasuredHeight();
                //  Log.e("<><> Hight ", String.valueOf(height) + " " + String.valueOf(position));

                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, height);
                layoutAction.setLayoutParams(lp);
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
        final ImageView ivSetReminder;
        final CustomEditText edtTaskName;
        final TextView txtAddTask;

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        rvEmployeeList = (RecyclerView) sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = (ImageView) sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = (CustomEditText) sheetView.findViewById(R.id.edtTaskName);
        txtAddReminder = (TextView) sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = (TextView) sheetView.findViewById(R.id.txtAddTask);

        edtTaskName.requestFocus();

        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        hashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(activity, R.color.colorAccent), null);
        hashTagHelper.handle(edtTaskName);

        dialog_Add_Task.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();
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

                    if (s.toString().trim().length() > 0)
                    {
                        // ivAddTask.setVisibility(View.VISIBLE);
                        // txtAddReminder.setVisibility(View.VISIBLE);
                    }


                    //String str = String.valueOf(s.toString().charAt(start));

                    final String hashtag = AppUtils.getValidAPIStringResponse(AppUtils.getHashtagFromString(s.toString(), start));

                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AssignedClientGetSetOld>();

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

                            rvEmployeeList.setVisibility(View.GONE);

                            String contact = getAllContact(false);

                            if (contact.startsWith(","))
                            {
                                contact = contact.substring(1);
                            }

                            Log.e("*** contact", contact);

                            saveTaskAsync(edtTaskName.getText().toString().trim(), contact, finalDateTime);

                            edtTaskName.setText("");
                            txtAddReminder.setVisibility(View.GONE);
                            finalDateTime = "";
                            selectedReminderDate = "";
                            selectedReminderTime = "";
                            txtAddReminder.setText("");
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

        dialog_Add_Task.show();
    }

    private void saveTaskAsync(final String taskName, final String contact, final String reminderDate)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "", status = "";
                private int taskId = 0;
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    //  llLoading.setVisibility(View.VISIBLE);
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    String URL = AppAPIUtils.ADD_TASK;
                    try
                    {
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashmap.put("assign_employee_ids", contact);
                        hashmap.put("task_status_id", "1");
                        hashmap.put("task_message", taskName);
                        hashmap.put("due_date", reminderDate);

                        Log.e("SAVE_TASK PARA", hashmap.toString());

                        String serverResponse = AppUtils.readJSONServiceUsingPOST(URL, hashmap);

                        Log.e("SAVE_TASK Response", serverResponse + "");

                        JSONObject jsonObject = new JSONObject(serverResponse);

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

                    if (is_success)
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getAllTask(false, false);
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                        }

                        if (DashboardActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            DashboardActivity.handler.sendMessage(message);
                        }

                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }

                }

            }.execute();
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

    private class EmployeeAdpater extends RecyclerView.Adapter<EmployeeAdpater.ViewHolder>
    {
        private ArrayList<AssignedClientGetSetOld> items;
        private String hashtag = "";
        private int end = 0;
        private EditText editText;
        private RecyclerView recyclerView;

        EmployeeAdpater(ArrayList<AssignedClientGetSetOld> list, final String hashtag, final int start, final EditText editText, final RecyclerView recyclerView)
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
            final AssignedClientGetSetOld assignedClientGetSetOld = items.get(position);
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

                        AssignedClientGetSetOld assignedClientGetSetOld1 = new AssignedClientGetSetOld();
                        assignedClientGetSetOld1.setFullname(assignedClientGetSetOld.getFullname());
                        assignedClientGetSetOld1.setFirst_name(assignedClientGetSetOld.getFirst_name());
                        assignedClientGetSetOld1.setLast_name(assignedClientGetSetOld.getLast_name());
                        assignedClientGetSetOld1.setEmail(assignedClientGetSetOld.getEmail());
                        assignedClientGetSetOld1.setId(assignedClientGetSetOld.getId());
                        listEmployee_Selected.add(assignedClientGetSetOld1);
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

    ///Edit Task
    public void showEditTaskDialog(final TaskListGetSet getSet)
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
        final ImageView ivSetReminder;
        final CustomEditText edtTaskName;
        final TextView txtAddTask;

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        rvEmployeeList = (RecyclerView) sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = (ImageView) sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = (CustomEditText) sheetView.findViewById(R.id.edtTaskName);
        txtAddReminderEdit = (TextView) sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = (TextView) sheetView.findViewById(R.id.txtAddTask);
        edtTaskName.requestFocus();
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


        if (getSet.getDue_date().length() > 0)
        {
            txtAddReminderEdit.setVisibility(View.VISIBLE);
            txtAddReminderEdit.setText(AppUtils.universalDateConvert(getSet.getDue_date(), "MM/dd/yyyy hh:mm:ss a", "dd MMM yyyy hh:mm a"));
            selectedReminderDateEdit = AppUtils.universalDateConvert(getSet.getDue_date(), "MM/dd/yyyy hh:mm:ss a", "dd MMM yyyy");
            selectedReminderTimeEdit = AppUtils.universalDateConvert(getSet.getDue_date(), "MM/dd/yyyy hh:mm:ss a", "hh:mm a");


            finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.toString().trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.toString().trim();

            Log.e("*** Final Date select", finalDateTimeEdit + "");
        }
        else
        {
            txtAddReminderEdit.setVisibility(View.GONE);
        }


        listEmployee_Selected_Edit = new ArrayList<AssignedClientGetSetOld>();

       /* List<String> items_id = Arrays.asList(getSet.getAll_employee_ids().split("\\s*,\\s*"));

        List<String> items_name = Arrays.asList(getSet.getAll_employee_name().split("\\s*,\\s*"));

        if (items_id.size() > 0)
        {
            for (int i = 0; i < items_id.size(); i++)
            {
                if (!AppUtils.getEmployeeIdForAdmin(activity).equalsIgnoreCase(items_id.get(i)))
                {
                    AssignedClientGetSetOld assignedClientGetSet1 = new AssignedClientGetSetOld();
                    String toDisplayName = AppUtils.toDisplayCase(items_name.get(i).toString());
                    assignedClientGetSet1.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                    assignedClientGetSet1.setId(Integer.parseInt(items_id.get(i).toString().trim()));
                    listEmployee_Selected_Edit.add(assignedClientGetSet1);
                }

            }
        }*/

        if (getSet.getListEmployee().size() > 0)
        {
            for (int i = 0; i < getSet.getListEmployee().size(); i++)
            {
                if (!AppUtils.getEmployeeIdForAdmin(activity).equalsIgnoreCase(String.valueOf(getSet.getListEmployee().get(i).getId())))
                {
                    AssignedClientGetSetOld assignedClientGetSetOld1 = new AssignedClientGetSetOld();
                    String toDisplayName = AppUtils.toDisplayCase(getSet.getListEmployee().get(i).getName());
                    assignedClientGetSetOld1.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                    assignedClientGetSetOld1.setId(getSet.getListEmployee().get(i).getId());
                    listEmployee_Selected_Edit.add(assignedClientGetSetOld1);

                    //  Log.e("<><>listEmployee_Edit:", getSet.getListEmployee().get(i).getId() + " ++ "+getSet.getListEmployee().get(i).getName() + "");
                }

            }
        }


        dialog_Edit_Task.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();
                // AppUtils.hideKeyboard(edtTaskName, activity);
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

                    Log.e("<><> HASHTAG LENTH :: ", hashtag.toString() + "");


                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AssignedClientGetSetOld>();

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
                            rvEmployeeList.setVisibility(View.GONE);

                            String contact = getAllContactEdit(false);

                            if (contact.startsWith(","))
                            {
                                contact = contact.substring(1);
                            }

                            Log.e("*** contact", contact);

                            editTaskAsync(edtTaskName.getText().toString().trim(), contact, finalDateTimeEdit, getSet.getId(), "1");

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
    }

    private class EmployeeAdpaterEdit extends RecyclerView.Adapter<EmployeeAdpaterEdit.ViewHolder>
    {
        private ArrayList<AssignedClientGetSetOld> items;
        private String hashtag = "";
        private int end = 0;
        private EditText editText;
        private RecyclerView recyclerView;

        EmployeeAdpaterEdit(ArrayList<AssignedClientGetSetOld> list, final String hashtag, final int start, final EditText editText, final RecyclerView recyclerView)
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
            final AssignedClientGetSetOld assignedClientGetSetOld = items.get(position);
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

                        AssignedClientGetSetOld assignedClientGetSetOld1 = new AssignedClientGetSetOld();
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

    private void editTaskAsync(final String taskName, final String contact, final String reminderDate, final int id, final String task_status_id)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "", status = "";
                private int taskId = 0;
                private boolean is_success = false;

                @Override
                protected void onPreExecute()
                {
                    //  llLoading.setVisibility(View.VISIBLE);
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    String URL = AppAPIUtils.UPDATE_TASK;
                    try
                    {
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put("Id", String.valueOf(id));
                        hashmap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashmap.put("assign_employee_ids", contact);
                        hashmap.put("task_status_id", task_status_id);
                        hashmap.put("task_message", taskName);
                        hashmap.put("due_date", reminderDate);

                        Log.e("UPDATE_TASK PARA", hashmap.toString());

                        String serverResponse = AppUtils.readJSONServiceUsingPOST(URL, hashmap);

                        Log.e("UPDATE_TASK Response", serverResponse + "");

                        JSONObject jsonObject = new JSONObject(serverResponse);

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

                    if (is_success)
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getAllTask(false, false);
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                        }

                        if (task_status_id.equalsIgnoreCase("2"))
                        {
                            if (DashboardActivity.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                DashboardActivity.handler.sendMessage(message);
                            }
                        }
                    }
                    else
                    {
                        llLoading.setVisibility(View.GONE);
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }

                }

            }.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void selectDeleteDialog(final TaskListGetSet getSet, final int pos)
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

    private void removeTask(final TaskListGetSet taskListGetSet, final int pos)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;
            private String message = "";

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("task_id", String.valueOf(taskListGetSet.getId()));
                    hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                    AppUtils.printLog(activity, "DELETE_TASK Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_TASK, hashMap);

                    AppUtils.printLog(activity, "DELETE_TASK Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

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

                try
                {
                    if (isSuccess)
                    {
                        getAllTask(false, false);

                      /*  listNormalTask.remove(taskListGetSet);
                        taskListAdapter.notifyDataSetChanged();
                        if (listNormalTask.size() > 0)
                        {
                            llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            llNoData.setVisibility(View.VISIBLE);
                        }*/

                        if (DashboardActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 110;
                            DashboardActivity.handler.sendMessage(message);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    private void pinTask(final TaskListGetSet taskListGetSet, final int pos)
    {
        new AsyncTask<Void, Void, Void>()
        {
            private boolean isSuccess = false;
            private String message = "";

            @Override
            protected void onPreExecute()
            {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    String response = "";

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("Id", "0");
                    hashMap.put("task_id", String.valueOf(taskListGetSet.getId()));
                    hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));

                    if (taskListGetSet.isPinedTask())
                    {
                        hashMap.put("IsPin", String.valueOf(false));
                    }
                    else
                    {
                        hashMap.put("IsPin", String.valueOf(true));
                    }


                    AppUtils.printLog(activity, "PIN_TASK Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.PIN_TASK, hashMap);

                    AppUtils.printLog(activity, "PIN_TASK Response ", response.toString());

                    JSONObject jsonObject = new JSONObject(response);

                    isSuccess = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

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

                try
                {
                    if (isSuccess)
                    {
                        getAllTask(false, false);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPostExecute(result);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
    }

    public void showFilterDialog()
    {
        BottomSheetDialog dialog_filter = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        dialog_filter.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog_filter.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_filter_task, null);
        dialog_filter.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);
        AppUtils.setLightStatusBarBottomDialog(dialog_filter, activity);
        TextView txtAll;
        ImageView ivAll;
        TextView txtPending;
        ImageView ivPending;
        TextView txtCompleted;
        ImageView ivCompleted;

        txtAll = (TextView) sheetView.findViewById(R.id.txtAll);
        ivAll = (ImageView) sheetView.findViewById(R.id.ivAll);

        txtPending = (TextView) sheetView.findViewById(R.id.txtPending);
        ivPending = (ImageView) sheetView.findViewById(R.id.ivPending);
        txtCompleted = (TextView) sheetView.findViewById(R.id.txtCompleted);
        ivCompleted = (ImageView) sheetView.findViewById(R.id.ivCompleted);


        if (TaskStatusId.equalsIgnoreCase("0"))
        {
            ivAll.setVisibility(View.VISIBLE);
            ivPending.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.GONE);
            ivLogout.setImageResource(R.drawable.t_sort);
        }
        else if (TaskStatusId.equalsIgnoreCase("1"))
        {
            ivAll.setVisibility(View.GONE);
            ivPending.setVisibility(View.VISIBLE);
            ivCompleted.setVisibility(View.GONE);
            ivLogout.setImageResource(R.drawable.t_sort);
        }
        else if (TaskStatusId.equalsIgnoreCase("2"))
        {
            ivAll.setVisibility(View.GONE);
            ivPending.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.VISIBLE);
            ivLogout.setImageResource(R.drawable.t_sort_green);
        }
        else
        {
            ivAll.setVisibility(View.GONE);
            ivPending.setVisibility(View.GONE);
            ivCompleted.setVisibility(View.GONE);
            ivLogout.setImageResource(R.drawable.t_sort);
        }

        dialog_filter.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();
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
                    ivLogout.setImageResource(R.drawable.t_sort);
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true, false);

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

        txtPending.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {

                    TaskStatusId = "1";
                    dialog_filter.dismiss();
                    ivLogout.setImageResource(R.drawable.t_sort);
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true, false);
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
                    ivLogout.setImageResource(R.drawable.t_sort_green);
                    if (sessionManager.isNetworkAvailable())
                    {
                        llNoInternet.setVisibility(View.GONE);
                        getAllTask(true, false);
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

