package cheetah.alphacapital.reportApp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.mit.mitspermissions.MitsPermissions;
import com.android.mit.mitspermissions.PermissionListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cheetah.alphacapital.classes.ImagePath;
import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.classes.SoftKeyboard;
import cheetah.alphacapital.reportApp.getset.CommentsGetSetOld;
import cheetah.alphacapital.reportApp.getset.TaskListGetSet;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.utils.AppAPIUtils;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.IOUtil;
import cheetah.alphacapital.utils.SessionManager;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AssignedClientGetSetOld;
import cheetah.alphacapital.reportApp.getset.CommentCountGetSet;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;


public class TaskDetailsActivity extends AppCompatActivity
{
    private Activity activity;
    private SessionManager sessionManager;
    private LinearLayout llMainLayout;
    private Toolbar toolbar;
    private ImageView ivHeader;
    private View viewStatusBar;
    private View emptyView;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private ImageView ivLogout;
    private LinearLayout llLoading;
    private ProgressBar pbLoading;
    private TextView txtLoading;
    private LinearLayout llNoInternet;
    private LinearLayout llRetry;
    private TextView txtRetry;
    private LinearLayout llNoData;
    private ImageView ivNoData;
    private TextView tvNoDataText;
    private LinearLayout llDetails;
    private TextView txtTaskName;
    private TextView txtShowMore;
    private TextView txtReminderDate;
    private View viewLineReminderDate;
    private LinearLayout llReasonForReassign;
    private TextView txtReasonForReassign;
    private RecyclerView rvAssignTo;
    private LinearLayout llMainComment;
    private LinearLayout llNoDataComment, llSelfTask;
    private RecyclerView rvComments;
    private LinearLayout llBottom, llAttachFile;
    private EditText edtComment;
    private ImageView ivSendComment;
    private ProgressBar progressSend;
    private final boolean isStatusBarHidden = false;
    public static Handler handler;
    SoftKeyboard softKeyboard;
    private final boolean isAction = false;
    private TaskListGetSet taskListGetSet = new TaskListGetSet();
    private ArrayList<AssignedClientGetSetOld> listEmployee_Contact = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<CommentsGetSetOld> listComment = new ArrayList<CommentsGetSetOld>();
    private CommentAdapter commentAdapter;

    public static boolean isApiCalling = false;
    public boolean isCommentCalling = false;
    private boolean isSelfTask = false;

    private final ArrayList<AssignedClientGetSetOld> listEmployee = new ArrayList<AssignedClientGetSetOld>();
    private ArrayList<AssignedClientGetSetOld> listEmployee_Search = new ArrayList<AssignedClientGetSetOld>();
    public ArrayList<AssignedClientGetSetOld> listEmployee_Selected_Edit = new ArrayList<AssignedClientGetSetOld>();

    private TextView txtAddReminderEdit;
    private HashTagHelper hashTagHelperEdit;

    private EmployeeAdpaterEdit employeeAdpaterEdit;
    public static String selectedReminderDateEdit = "", selectedReminderTimeEdit = "";
    private String finalDateTimeEdit = "";
    String date_timeEdit = "", task_id = "", task_added_by = "";
    int mYearEdit;
    int mMonthEdit;
    int mDayEdit;

    //For ProfilePic
    private final int SELECT_FILE = 1;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_FROM_GALLERY_CROP = 212;
    private String imageFilePath = "", fileExtension = "", base64File = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
      /*try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                Window w = getWindow();
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                isStatusBarHidden = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        */

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

        setContentView(R.layout.activity_task_details);

        task_id = AppUtils.getValidAPIStringResponse(getIntent().getExtras().getString("task_id"));
        task_added_by = AppUtils.getValidAPIStringResponse(getIntent().getExtras().getString("task_added_by"));
        sessionManager.setTaskId(String.valueOf(task_id));

        setupViews();

        AppUtils.setLightStatusBar(activity);

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getTaskDetails(true);
            readAllComment();
        }
        else
        {
            llNoInternet.setVisibility(View.VISIBLE);
        }

        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message msg)
            {
                try
                {
                    //Reload All Feeds
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getTaskDetails(true);
                        }
                        else
                        {
                            llNoInternet.setVisibility(View.VISIBLE);
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                return false;
            }
        });

    }

    private void setupViews()
    {
        try
        {
            llMainLayout = (LinearLayout) findViewById(R.id.llMainLayout);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            ivHeader = (ImageView) findViewById(R.id.ivHeader);
            viewStatusBar = (View) findViewById(R.id.viewStatusBar);
            emptyView = (View) findViewById(R.id.emptyView);
            llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
            ivLogo = (ImageView) findViewById(R.id.ivLogo);
            ivIcon = (ImageView) findViewById(R.id.ivIcon);
            txtTitle = (TextView) findViewById(R.id.txtTitle);
            ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
            llNotification = (LinearLayout) findViewById(R.id.llNotification);
            ivLogout = (ImageView) findViewById(R.id.ivLogout);
            ivLogout.setVisibility(View.VISIBLE);
            ivLogout.setImageResource(R.drawable.ic_info_gray);
            llLoading = (LinearLayout) findViewById(R.id.llLoading);
            pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
            txtLoading = (TextView) findViewById(R.id.txtLoading);
            llNoInternet = (LinearLayout) findViewById(R.id.llNoInternet);
            llRetry = (LinearLayout) findViewById(R.id.llRetry);
            txtRetry = (TextView) findViewById(R.id.txtRetry);
            llNoData = (LinearLayout) findViewById(R.id.llNoData);
            ivNoData = (ImageView) findViewById(R.id.ivNoData);
            tvNoDataText = (TextView) findViewById(R.id.tvNoDataText);
            llDetails = (LinearLayout) findViewById(R.id.llDetails);
            txtTaskName = (TextView) findViewById(R.id.txtTaskName);
            txtShowMore = (TextView) findViewById(R.id.txtShowMore);
            txtReminderDate = (TextView) findViewById(R.id.txtReminderDate);
            viewLineReminderDate = (View) findViewById(R.id.viewLineReminderDate);
            llReasonForReassign = (LinearLayout) findViewById(R.id.llReasonForReassign);
            txtReasonForReassign = (TextView) findViewById(R.id.txtReasonForReassign);
            rvAssignTo = (RecyclerView) findViewById(R.id.rvAssignTo);
            rvAssignTo.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            llMainComment = (LinearLayout) findViewById(R.id.llMainComment);
            llNoDataComment = (LinearLayout) findViewById(R.id.llNoDataComment);
            llSelfTask = (LinearLayout) findViewById(R.id.llSelfTask);
            rvComments = (RecyclerView) findViewById(R.id.rvComments);
            llBottom = (LinearLayout) findViewById(R.id.llBottom);
            llAttachFile = (LinearLayout) findViewById(R.id.llAttachFile);
            edtComment = (EditText) findViewById(R.id.edtComment);
            ivSendComment = (ImageView) findViewById(R.id.ivSendComment);
            progressSend = (ProgressBar) findViewById(R.id.progressSend);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
            rvComments.setLayoutManager(linearLayoutManager);
            /*setSupportActionBar(toolbar);
            ImageView ivHeader = findViewById(R.id.ivHeader);
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
            txtTitle.setText("Task Details");
            llBackNavigation.setVisibility(View.VISIBLE);

            InputMethodManager im = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);

            softKeyboard = new SoftKeyboard(llMainLayout, im);

            try
            {
                softKeyboard.setSoftKeyboardCallback(new SoftKeyboard.SoftKeyboardChanged()
                {
                    @Override
                    public void onSoftKeyboardHide()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    llDetails.setVisibility(View.VISIBLE);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onSoftKeyboardShow()
                    {
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try
                                {
                                    llDetails.setVisibility(View.GONE);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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
                if (sessionManager.isNetworkAvailable())
                {
                    llNoInternet.setVisibility(View.GONE);
                    getTaskDetails(true);
                }
                else
                {
                    llNoInternet.setVisibility(View.VISIBLE);
                }
            }
        });

        edtComment.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                try
                {
                    if (s.toString().trim().length() == 0)
                    {
                        ivSendComment.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        ivSendComment.setVisibility(View.VISIBLE);
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
            public void afterTextChanged(Editable s)
            {
            }
        });

        ivSendComment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (edtComment.getText().toString().trim().length() > 0)
                    {
                        if (!isCommentCalling)
                        {
                            saveCommentAsync(edtComment.getText().toString().trim(), "", "");
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, "Please add comment.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        txtShowMore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    if (txtShowMore.getText().toString().equalsIgnoreCase("Show more"))
                    {
                        txtTaskName.setMaxLines(Integer.MAX_VALUE);
                        txtShowMore.setText("Show less");
                    }
                    else
                    {
                        txtTaskName.setMaxLines(3);
                        txtShowMore.setText("Show more");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showTaskActionDialog();
            }
        });

        llAttachFile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    checkPermission();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTaskDetails(final boolean isForAllData)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message_employee = "", message_details = "";

                private boolean success_comment = false, is_success_Employee = false, is_success_details = false;

                @Override
                protected void onPreExecute()
                {
                    llLoading.setVisibility(View.VISIBLE);
                    isApiCalling = true;
                    taskListGetSet = new TaskListGetSet();
                    listComment = new ArrayList<CommentsGetSetOld>();
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {

                    if (isForAllData)
                    {
                        getAllTaskDetails();
                        getAllComment();
                        getAllEmployee();
                    }
                    else
                    {
                        getAllTaskDetails();
                    }

                    return null;
                }


                private void getAllTaskDetails()
                {
                    try
                    {
                        String response = "";

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("task_id", task_id);

                        AppUtils.printLog(activity, "GET_TASK_DETAILBYID Request ", hashMap.toString());

                        response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.GET_TASK_DETAILBYID, hashMap);

                        AppUtils.printLog(activity, "GET_TASK_DETAILBYID Response ", response);

                        JSONObject jsonObject = new JSONObject(response);

                        is_success_details = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message_details = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                        if (is_success_details)
                        {

                            if (jsonObject.has("data"))
                            {
                                JSONObject dataObj = jsonObject.getJSONObject("data");

                                if (dataObj.has("TaskDetail"))
                                {
                                    JSONObject dataTaskDetail = dataObj.getJSONObject("TaskDetail");
                                    taskListGetSet.setId(AppUtils.getValidAPIIntegerResponseHas(dataTaskDetail, "id"));
                                    taskListGetSet.setEmployee_id(AppUtils.getValidAPIIntegerResponseHas(dataTaskDetail, "employee_id"));
                                    taskListGetSet.setTask_status_id(AppUtils.getValidAPIIntegerResponseHas(dataTaskDetail, "task_status_id"));
                                    taskListGetSet.setTask_message(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "task_message"));
                                    taskListGetSet.setDue_date(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "due_date"));
                                    taskListGetSet.setCreated_date(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "created_date"));
                                    taskListGetSet.setFormated_created_date(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "formated_created_date"));
                                    taskListGetSet.setTask_added_by(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "task_added_by"));
                                    taskListGetSet.setTask_status(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "task_status"));
                                    taskListGetSet.setAll_employee_ids(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "all_employee_ids"));
                                    taskListGetSet.setAll_employee_name(AppUtils.getValidAPIStringResponseHas(dataTaskDetail, "all_employee_name"));
                                    taskListGetSet.setPinedTask(AppUtils.getValidAPIBooleanResponseHas(dataTaskDetail, "IsPinedTask"));
                                    taskListGetSet.setUnReadCount(AppUtils.getValidAPIIntegerResponseHas(dataTaskDetail, "unReadCount"));
                                }

                                if (dataObj.has("EmployeeList"))
                                {
                                    ArrayList<AssignedClientGetSetOld> list_Emp = new ArrayList<AssignedClientGetSetOld>();
                                    JSONArray dataListEmployee = dataObj.getJSONArray("EmployeeList");

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


                            }
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                private void getAllComment()
                {
                    try
                    {
                        String URL = AppAPIUtils.GETALLTASK_MESSAGE_BYTASKID;

                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("task_id", String.valueOf(task_id));
                        hashMap.put("PagIndex", "0");
                        hashMap.put("PageSize", "0");
                        //  hashMap.put("LastCommentId", "0");

                        AppUtils.printLog(activity, "<><> GETALLTASK_MESSAGE_BYTASKID PARAM:", hashMap.toString());
                        String response = AppUtils.readJSONServiceUsingPOST(URL, hashMap);
                        AppUtils.printLog(activity, "<><> GETALLTASK_MESSAGE_BYTASKID RES:", response);


                        JSONObject jsonObject = new JSONObject(response);

                        success_comment = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        if (success_comment)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONArray commentsArray = jsonObject.getJSONArray("data");

                                if (commentsArray.length() > 0)
                                {
                                    for (int i = 0; i < commentsArray.length(); i++)
                                    {
                                        JSONObject commentsObj = commentsArray.getJSONObject(i);
                                        CommentsGetSetOld commentsGetSet = new CommentsGetSetOld();
                                        commentsGetSet.setId(AppUtils.getValidAPIStringResponseHas(commentsObj, "id"));
                                        commentsGetSet.setEmployee_id(AppUtils.getValidAPIStringResponseHas(commentsObj, "employee_id"));
                                        commentsGetSet.setTask_id(AppUtils.getValidAPIStringResponseHas(commentsObj, "task_id"));
                                        commentsGetSet.setMsg_txt(AppUtils.getValidAPIStringResponseHas(commentsObj, "msg_txt"));
                                        commentsGetSet.setImg_url(AppUtils.getValidAPIStringResponseHas(commentsObj, "img_url"));
                                        commentsGetSet.setIs_image(AppUtils.getValidAPIBooleanResponseHas(commentsObj, "is_image"));
                                        commentsGetSet.setAdded_date(AppUtils.getValidAPIStringResponseHas(commentsObj, "added_date"));
                                        commentsGetSet.setFull_name(AppUtils.getValidAPIStringResponseHas(commentsObj, "first_name") + " " + AppUtils.getValidAPIStringResponseHas(commentsObj, "last_name"));
                                        listComment.add(commentsGetSet);
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

                        AppUtils.printLog(activity, "Get_All_Employee Response ", response);

                        JSONObject jsonObject = new JSONObject(response);

                        is_success_Employee = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message_employee = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                        if (is_success_Employee)
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

                    if (isForAllData)
                    {
                        if (is_success_details)
                        {
                            setTaskDetailsData();
                        }

                        if (success_comment)
                        {
                            isApiCalling = false;

                            try
                            {
                                if (listComment.size() > 0)
                                {
                                    commentAdapter = new CommentAdapter(listComment);
                                    rvComments.setAdapter(commentAdapter);
                                    new CountDownTimer(300, 1000)
                                    {
                                        @Override
                                        public void onTick(long millisUntilFinished)
                                        {
                                        }

                                        @Override
                                        public void onFinish()
                                        {
                                            try
                                            {
                                                if (listComment.size() > 0)
                                                {
                                                    rvComments.scrollToPosition(listComment.size() - 1);
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();

                                    rvComments.setVisibility(View.VISIBLE);
                                    llNoDataComment.setVisibility(View.GONE);

                                    if (listComment.size() > 0)
                                    {
                                        sessionManager.setLastCommentId(String.valueOf(listComment.get(listComment.size() - 1).getId()));
                                    }
                                }
                                else
                                {
                                    llNoDataComment.setVisibility(View.VISIBLE);
                                    rvComments.setVisibility(View.GONE);
                                }
                            }
                            catch (Exception e1)
                            {
                                e1.printStackTrace();
                            }
                        }
                        else
                        {
                            llNoDataComment.setVisibility(View.VISIBLE);
                            rvComments.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        if (is_success_details)
                        {
                            setTaskDetailsData();
                        }
                    }


                    if (TaskListActivity.handler != null)
                    {
                        Message msgObj = Message.obtain();
                        msgObj.what = 100;
                        TaskListActivity.handler.sendMessage(msgObj);
                    }

                    llLoading.setVisibility(View.GONE);
                }

            }.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setTaskDetailsData()
    {
        if (taskListGetSet.getTask_status_id() == 2)
        {
            txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message())));
            txtTaskName.setPaintFlags(txtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskListGetSet.getTask_message())));
            txtTaskName.setPaintFlags(txtTaskName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        int lines = txtTaskName.getLineCount();

        if (lines > 3)
        {
            txtTaskName.setMaxLines(3);
            txtShowMore.setVisibility(View.VISIBLE);
        }
        else
        {
            txtShowMore.setVisibility(View.GONE);
        }

        if (taskListGetSet.getDue_date().length() > 0)
        {
            txtReminderDate.setVisibility(View.VISIBLE);
            viewLineReminderDate.setVisibility(View.VISIBLE);
            txtReminderDate.setText(AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy hh:mm a"));
        }
        else
        {
            txtReminderDate.setVisibility(View.GONE);
            viewLineReminderDate.setVisibility(View.GONE);
        }

        listEmployee_Contact = new ArrayList<AssignedClientGetSetOld>();

        AssignedClientGetSetOld assignedClientGetSetOld1 = new AssignedClientGetSetOld();
        assignedClientGetSetOld1.setName(task_added_by);
        assignedClientGetSetOld1.setId(taskListGetSet.getEmployee_id());
        assignedClientGetSetOld1.setIs_selected(true);
        listEmployee_Contact.add(assignedClientGetSetOld1);


        if (taskListGetSet.getListEmployee().size() > 0)
        {
            llMainComment.setVisibility(View.VISIBLE);
            llSelfTask.setVisibility(View.GONE);
            isSelfTask = false;
            for (int i = 0; i < taskListGetSet.getListEmployee().size(); i++)
            {
                AssignedClientGetSetOld assignedClientGetSetOld = new AssignedClientGetSetOld();
                assignedClientGetSetOld.setName(taskListGetSet.getListEmployee().get(i).getName());
                assignedClientGetSetOld.setId(taskListGetSet.getListEmployee().get(i).getId());
                assignedClientGetSetOld.setIs_selected(false);
                listEmployee_Contact.add(assignedClientGetSetOld);
            }
            Log.e("<><> ListEmployee Size ", listEmployee_Contact.size() + " ===");
        }
        else
        {
            isSelfTask = true;
            llMainComment.setVisibility(View.GONE);
            llSelfTask.setVisibility(View.VISIBLE);
        }

        if (listEmployee_Contact.size() > 0)
        {
            rvAssignTo.setVisibility(View.VISIBLE);
            EmployeeAdpater employeeAdpater = new EmployeeAdpater(listEmployee_Contact);
            rvAssignTo.setAdapter(employeeAdpater);
        }
        else
        {
            rvAssignTo.setVisibility(View.GONE);
        }
    }

    private void saveCommentAsync(final String comment, final String imageName, final String fileExtension)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private boolean success = false;
                private final int commentId = 0;

                @Override
                protected void onPreExecute()
                {
                    edtComment.setText("");
                    ivSendComment.setVisibility(View.GONE);
                    progressSend.setVisibility(View.VISIBLE);
                    isCommentCalling = true;
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... params)
                {
                    try
                    {
                        String URL = AppAPIUtils.SAVE_TASK_MESSAGE;
                        HashMap<String, String> hashMap = new HashMap<String, String>();
                        hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                        hashMap.put("task_id", String.valueOf(task_id));

                        if (imageName.length() > 0)
                        {
                            hashMap.put("msg_txt", "");
                            hashMap.put("FileExtension", fileExtension);
                            hashMap.put("is_image", String.valueOf(true));
                            hashMap.put("img_url", imageName);
                        }
                        else
                        {
                            hashMap.put("msg_txt", comment);
                            hashMap.put("img_url", "");
                            hashMap.put("FileExtension", "");
                            hashMap.put("is_image", String.valueOf(false));
                        }

                        Log.e("<><> employee_id ", AppUtils.getEmployeeIdForAdmin(activity) + " " + task_id + fileExtension);

                        AppUtils.printLog(activity, "<><> SAVE_COMMENT PARAM:", hashMap.toString());

                        String response = AppUtils.readJSONServiceUsingPOST(URL, hashMap);

                        AppUtils.printLog(activity, "<><> SAVE_COMMENT RES :", response);

                        JSONObject jsonObject = new JSONObject(response);

                        success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");

                        message = AppUtils.getValidAPIStringResponseHas(jsonObject, "message");

                        if (success)
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONObject commentsObj = jsonObject.getJSONObject("data");
                                CommentsGetSetOld commentsGetSet = new CommentsGetSetOld();
                                commentsGetSet.setId(AppUtils.getValidAPIStringResponseHas(commentsObj, "Id"));
                                commentsGetSet.setEmployee_id(AppUtils.getValidAPIStringResponseHas(commentsObj, "employee_id"));
                                commentsGetSet.setTask_id(AppUtils.getValidAPIStringResponseHas(commentsObj, "task_id"));
                                commentsGetSet.setMsg_txt(AppUtils.getValidAPIStringResponseHas(commentsObj, "msg_txt"));
                                commentsGetSet.setImg_url(AppUtils.getValidAPIStringResponseHas(commentsObj, "img_url"));
                                commentsGetSet.setIs_image(AppUtils.getValidAPIBooleanResponseHas(commentsObj, "is_image"));
                                commentsGetSet.setAdded_date(AppUtils.getValidAPIStringResponseHas(commentsObj, "added_date"));
                                commentsGetSet.setFull_name(sessionManager.getUserName());
                                listComment.add(commentsGetSet);
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

                    try
                    {
                        progressSend.setVisibility(View.GONE);
                        ivSendComment.setVisibility(View.VISIBLE);

                        if (success)
                        {
                            llNoDataComment.setVisibility(View.GONE);

                            rvComments.setVisibility(View.VISIBLE);

                            if (listComment.size() > 2)
                            {
                                commentAdapter.notifyDataSetChanged();
                                rvComments.scrollToPosition(listComment.size() - 1);
                            }
                            else
                            {
                                commentAdapter = new CommentAdapter(listComment);
                                rvComments.setAdapter(commentAdapter);
                                new CountDownTimer(300, 1000)
                                {
                                    @Override
                                    public void onTick(long millisUntilFinished)
                                    {
                                    }

                                    @Override
                                    public void onFinish()
                                    {
                                        try
                                        {
                                            if (listComment.size() > 0)
                                            {
                                                rvComments.scrollToPosition(listComment.size() - 1);
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                }.start();
                            }

                            if (listComment.size() > 0)
                            {
                                sessionManager.setLastCommentId(String.valueOf(listComment.get(listComment.size() - 1).getId()));
                            }

                            readAllComment();
                        }
                        else
                        {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }

                        isCommentCalling = false;
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

    private class EmployeeAdpater extends RecyclerView.Adapter<EmployeeAdpater.ViewHolder>
    {
        private final ArrayList<AssignedClientGetSetOld> items;

        EmployeeAdpater(ArrayList<AssignedClientGetSetOld> list)
        {
            this.items = list;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_details_contact_new, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final AssignedClientGetSetOld assignedClientGetSetOld = items.get(position);

            if (assignedClientGetSetOld.isIs_selected())
            {
                holder.txtAssignBy.setVisibility(View.VISIBLE);
                holder.viewline.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.txtAssignBy.setVisibility(View.INVISIBLE);
                holder.viewline.setVisibility(View.GONE);
            }

            if (assignedClientGetSetOld.getName().length() > 0)
            {
                String[] datearr = assignedClientGetSetOld.getName().split("\\s");

                if (datearr.length >= 1)
                {
                    holder.txtUserName.setText(AppUtils.toDisplayCase(datearr[0]));

                }
                else
                {
                    holder.txtUserName.setText(AppUtils.toDisplayCase(assignedClientGetSetOld.getName()));

                }

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(assignedClientGetSetOld.getName())));
            }


        }

        @Override
        public int getItemCount()
        {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView txtUserSortName, txtAssignBy, txtUserName;
            final View viewline;

            ViewHolder(View convertView)
            {
                super(convertView);

                txtUserSortName = (TextView) convertView.findViewById(R.id.txtUserSortName);
                txtAssignBy = (TextView) convertView.findViewById(R.id.txtAssignBy);
                txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
                viewline = (View) convertView.findViewById(R.id.viewline);
            }
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
    {
        private final ArrayList<CommentsGetSetOld> items;

        CommentAdapter(ArrayList<CommentsGetSetOld> list)
        {
            this.items = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_comments_new, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            final CommentsGetSetOld commentsGetSet = items.get(position);

            if (AppUtils.getEmployeeIdForAdmin(activity).trim().equalsIgnoreCase(String.valueOf(commentsGetSet.getEmployee_id())))
            {
                holder.llLeft.setVisibility(View.GONE);
                holder.llRight.setVisibility(View.VISIBLE);

                if (commentsGetSet.isIs_image())
                {
                    holder.txtMsgRight.setVisibility(View.GONE);
                    holder.imgRight.setVisibility(View.VISIBLE);
                    if (commentsGetSet.getImg_url() != null && commentsGetSet.getImg_url().length() > 0)
                    {
                        Glide.with(activity).load(commentsGetSet.getImg_url().trim()).placeholder(R.color.blue_new).into(holder.imgRight);
                    }
                }
                else
                {
                    holder.txtMsgRight.setVisibility(View.VISIBLE);
                    holder.imgRight.setVisibility(View.GONE);
                    holder.txtMsgRight.setText(commentsGetSet.getMsg_txt());
                }


                if (commentsGetSet.getAdded_date().trim().length() > 0)
                {
                    holder.txtDateRight.setVisibility(View.VISIBLE);
                    holder.txtDateRight.setText(AppUtils.getRelativeDateTimeForChat(AppUtils.universalDateConvert(commentsGetSet.getAdded_date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyy hh:mm a")));
                }
                else
                {
                    holder.txtDateRight.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.llLeft.setVisibility(View.VISIBLE);
                holder.llRight.setVisibility(View.GONE);
                if (commentsGetSet.isIs_image())
                {
                    holder.txtMsgLeft.setVisibility(View.GONE);
                    holder.imgLeft.setVisibility(View.VISIBLE);
                    if (commentsGetSet.getImg_url() != null && commentsGetSet.getImg_url().length() > 0)
                    {
                        Glide.with(activity).load(commentsGetSet.getImg_url().trim()).placeholder(R.color.blue_new).into(holder.imgLeft);
                    }
                }
                else
                {
                    holder.txtMsgLeft.setVisibility(View.VISIBLE);
                    holder.imgLeft.setVisibility(View.GONE);
                    holder.txtMsgLeft.setText(commentsGetSet.getMsg_txt());
                }

                if (commentsGetSet.getAdded_date().trim().length() > 0)
                {
                    holder.txtDateLeft.setVisibility(View.VISIBLE);
                    holder.txtDateLeft.setText(AppUtils.getRelativeDateTimeForChat(AppUtils.universalDateConvert(commentsGetSet.getAdded_date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "dd/MM/yyy hh:mm a")));
                }
                else
                {
                    holder.txtDateLeft.setVisibility(View.GONE);
                }

                holder.txtSortNameLeft.setText(AppUtils.getSortName(AppUtils.toDisplayCase(commentsGetSet.getFull_name())));
            }


            holder.imgLeft.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(activity, ImageSliderActivity.class);
                    intent.putExtra("url", commentsGetSet.getImg_url());
                    activity.startActivity(intent);
                }
            });

            holder.imgRight.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(activity, ImageSliderActivity.class);
                    intent.putExtra("url", commentsGetSet.getImg_url());
                    activity.startActivity(intent);
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
            private final LinearLayout llLeft;
            private final TextView txtSortNameLeft;
            private final TextView txtMsgLeft;
            private final ImageView imgLeft;
            private final TextView txtDateLeft;
            private final LinearLayout llRight;
            private final TextView txtMsgRight;
            private final ImageView imgRight;
            private final TextView txtDateRight;

            ViewHolder(View convertView)
            {
                super(convertView);
                llLeft = (LinearLayout) convertView.findViewById(R.id.llLeft);
                txtSortNameLeft = (TextView) convertView.findViewById(R.id.txtSortNameLeft);
                txtMsgLeft = (TextView) convertView.findViewById(R.id.txtMsgLeft);
                imgLeft = (ImageView) convertView.findViewById(R.id.imgLeft);
                txtDateLeft = (TextView) convertView.findViewById(R.id.txtDateLeft);
                llRight = (LinearLayout) convertView.findViewById(R.id.llRight);
                txtMsgRight = (TextView) convertView.findViewById(R.id.txtMsgRight);
                imgRight = (ImageView) convertView.findViewById(R.id.imgRight);
                txtDateRight = (TextView) convertView.findViewById(R.id.txtDateRight);
            }
        }
    }

    private void readAllComment()
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private boolean success = false;

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
                        String URL = AppAPIUtils.MARK_AS_READ_MESSAGE;
                        HashMap<String, String> hashmap = new HashMap<String, String>();
                        hashmap.put("task_id", String.valueOf(task_id));
                        hashmap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));

                        AppUtils.printLog(activity, "MARK_AS_READ_MESSAGE PARA", hashmap.toString());
                        String serverResponse = AppUtils.readJSONServiceUsingPOST(URL, hashmap);
                        AppUtils.printLog(activity, "MARK_AS_READ_MESSAGE Response", serverResponse);

                        JSONObject jsonObject = new JSONObject(serverResponse);
                        success = AppUtils.getValidAPIBooleanResponseHas(jsonObject, "success");
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

                    if (success)
                    {
                        if (TaskListActivity.handler != null)
                        {
                            CommentCountGetSet commentCountGetSet = new CommentCountGetSet();
                            commentCountGetSet.setTaskId(taskListGetSet.getId());
                            commentCountGetSet.setComment(0);
                            Message msgObj = Message.obtain();
                            msgObj.what = 105;
                            msgObj.obj = commentCountGetSet;
                            TaskListActivity.handler.sendMessage(msgObj);
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                    }

                    //llLoading.setVisibility(View.GONE);
                }

            }.execute();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showTaskActionDialog()
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

        llMain = (LinearLayout) sheetView.findViewById(R.id.llMain);
        llEdit = (LinearLayout) sheetView.findViewById(R.id.llEdit);
        llDelete = (LinearLayout) sheetView.findViewById(R.id.llDelete);
        llComplete = (LinearLayout) sheetView.findViewById(R.id.llComplete);

        if (taskListGetSet.getTask_status_id() == 2)
        {
            llEdit.setVisibility(View.GONE);
            llDelete.setVisibility(View.VISIBLE);
            llComplete.setVisibility(View.GONE);
        }
        else
        {
            llEdit.setVisibility(View.VISIBLE);
            llDelete.setVisibility(View.VISIBLE);
            llComplete.setVisibility(View.VISIBLE);
        }


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                //hideKeyboard();
            }
        });

        llEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    dialog.dismiss();
                    showEditTaskDialog();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
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
                    selectDeleteDialog();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        llComplete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    dialog.dismiss();

                    String contact_id = "";

                    if (listEmployee_Contact.size() > 0)
                    {
                        for (int i = 0; i < listEmployee_Contact.size(); i++)
                        {
                            if (listEmployee_Contact.get(i).getId() != taskListGetSet.getEmployee_id())
                            {
                                if (contact_id.length() == 0)
                                {
                                    contact_id = String.valueOf(listEmployee_Contact.get(i).getId());
                                }
                                else
                                {
                                    contact_id = contact_id + "," + listEmployee_Contact.get(i).getId() + "";
                                }
                            }
                        }
                    }

                    if (taskListGetSet.getDue_date().length() > 0)
                    {
                        editTaskAsync(taskListGetSet.getTask_message().trim(), contact_id, AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"), Integer.parseInt(task_id), "2");
                    }
                    else
                    {
                        editTaskAsync(taskListGetSet.getTask_message().trim(), contact_id, "", Integer.parseInt(task_id), "2");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        dialog.show();
    }

    public void showEditTaskDialog()
    {
        final BottomSheetDialog dialog_Edit_Task = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
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

        edtTaskName.setText(taskListGetSet.getTask_message().trim());

        try
        {
            if (taskListGetSet.getTask_message().trim().length() > 0)
            {
                edtTaskName.setSelection(taskListGetSet.getTask_message().trim().length());
            }
        }
        catch (Exception e)
        {
            edtTaskName.setSelection(0);
            e.printStackTrace();
        }

        if (taskListGetSet.getDue_date().length() > 0)
        {
            txtAddReminderEdit.setVisibility(View.VISIBLE);
            txtAddReminderEdit.setText(AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy hh:mm a"));
            selectedReminderDateEdit = AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy");
            selectedReminderTimeEdit = AppUtils.universalDateConvert(taskListGetSet.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm a");

            finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.trim();

            Log.e("*** Final Date select", finalDateTimeEdit + "");
        }
        else
        {
            txtAddReminderEdit.setVisibility(View.GONE);
        }


        listEmployee_Selected_Edit = new ArrayList<AssignedClientGetSetOld>();

        if (taskListGetSet.getListEmployee().size() > 0)
        {
            for (int i = 0; i < taskListGetSet.getListEmployee().size(); i++)
            {
                if (!AppUtils.getEmployeeIdForAdmin(activity).equalsIgnoreCase(String.valueOf(taskListGetSet.getListEmployee().get(i).getId())))
                {
                    AssignedClientGetSetOld assignedClientGetSetOld1 = new AssignedClientGetSetOld();
                    String toDisplayName = AppUtils.toDisplayCase(taskListGetSet.getListEmployee().get(i).getName());
                    assignedClientGetSetOld1.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                    assignedClientGetSetOld1.setId(taskListGetSet.getListEmployee().get(i).getId());
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
                AppUtils.hideKeyboard(edtTaskName,activity);
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
                        listEmployee_Search = new ArrayList<AssignedClientGetSetOld>();

                        if (listEmployee != null && listEmployee.size() > 0)
                        {
                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                final String text = listEmployee.get(i).getFullname();

                                String text1 = AppUtils.getCapitalText(text);

                                String cs1 = AppUtils.getCapitalText(hashtag);

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

                            editTaskAsync(edtTaskName.getText().toString().trim(), contact, finalDateTimeEdit, Integer.parseInt(task_id), "1");

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
        private final ArrayList<AssignedClientGetSetOld> items;
        private String hashtag = "";
        private int end = 0;
        private final EditText editText;
        private final RecyclerView recyclerView;

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
                        String endText = edtTextString.substring(end + 1);
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

    private void editTaskAsync(final String taskName, final String contact, final String reminderDate, final int id, final String task_status_id)
    {
        try
        {
            new AsyncTask<Void, Void, Void>()
            {
                private String message = "";
                private final String status = "";
                private final int taskId = 0;
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
                            getTaskDetails(false);
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

                        if (TaskListActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            TaskListActivity.handler.sendMessage(message);
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

    public void selectDeleteDialog()
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
                    removeTask();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bottomSheetDialog.show();

    }

    private void removeTask()
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
                    hashMap.put("task_id", task_id);
                    hashMap.put("employee_id", AppUtils.getEmployeeIdForAdmin(activity));
                    AppUtils.printLog(activity, "DELETE_TASK Request ", hashMap.toString());

                    response = AppUtils.readJSONServiceUsingPOST(AppAPIUtils.DELETE_TASK, hashMap);

                    AppUtils.printLog(activity, "DELETE_TASK Response ", response);

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
                        finish();

                        if (TaskListActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            TaskListActivity.handler.sendMessage(message);
                        }

                        if (DashboardActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
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

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //softKeyboard.unRegisterSoftKeyboardCallback();
    }

    private void checkPermission()
    {
        try
        {
            PermissionListener permissionlistener = new PermissionListener()
            {
                @Override
                public void onPermissionGranted()
                {
                    try
                    {
                        selectImage();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions)
                {

                    Toast.makeText(activity, "Permission Denied In checkPermission ", Toast.LENGTH_SHORT).show();

                }
            };
            new MitsPermissions(activity).setPermissionListener(permissionlistener).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).check();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void selectImage()
    {
        try
        {
            CharSequence[] items = new CharSequence[]{};

            items = new CharSequence[]{"Take Photo", "Choose From Library", "Cancel"};


            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Add Photo!");
            final CharSequence[] finalItems = items;
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int item)
                {
                    if (finalItems[item].equals("Take Photo"))
                    {
                        takeCameraImage();
                    }
                    else if (finalItems[item].equals("Choose From Library"))
                    {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    }
                    else if (finalItems[item].equals("Cancel"))
                    {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void takeCameraImage()
    {
        try
        {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (pictureIntent.resolveActivity(activity.getPackageManager()) != null)
            {
                File photoFile = null;

                try
                {
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    String imageFileName = "IMG_" + timeStamp + "_";
                    File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
                    imageFilePath = photoFile.getAbsolutePath();
                    Uri photoUri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".provider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(pictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    private String getPath(Uri uri, Activity activity)
    {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    String tempPath = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            if (requestCode ==  CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            {
                try
                {
                    tempPath = imageFilePath;

                    File fl1 = new File(tempPath);

                    tempPath = fl1.getAbsolutePath();

                    Log.v("tempPath to store", tempPath + "*");

                    Intent i = new Intent(activity, CropActivity.class);
                    i.putExtra("imagePath", tempPath);
                    i.putExtra("outputSize", 400);
                    startActivityForResult(i, PICK_FROM_GALLERY_CROP);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK)
            {
                try
                {
                    tempPath = getPath(data.getData(), activity);

                    File fl1 = new File(tempPath);

                    tempPath = fl1.getAbsolutePath();

                    Log.v("tempPath to store", tempPath + "*");

                    Intent i = new Intent(activity, CropActivity.class);
                    i.putExtra("imagePath", tempPath);
                    i.putExtra("outputSize", 400);
                    startActivityForResult(i, PICK_FROM_GALLERY_CROP);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if (requestCode == PICK_FROM_GALLERY_CROP)
            {
                try
                {
                    String path = data.getStringExtra("single_path");

                    File fl1 = new File(path);

                    tempPath = fl1.getAbsolutePath();

                   /* if (tempPath.length() > 0)
                    {
                        llSortName.setVisibility(View.GONE);

                        ivUserImage.setVisibility(View.VISIBLE);

                        try
                        {
                            Picasso.get().load(fl1).placeholder(R.drawable.ic_user_light_gray).into(ivUserImage);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        llSortName.setVisibility(View.VISIBLE);
                        ivUserImage.setVisibility(View.GONE);
                    }*/

                    Log.v("tempPath to store Crop : ", tempPath + "*");

                    byte[] byteArray = null;

                    final File fl = ImagePath.compressImage(activity, tempPath);

                    try
                    {
                        byteArray = IOUtil.readFile(fl);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    base64File = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    byteArray = null;

                    String[] nameArr = fl.getAbsolutePath().split("/");

                    String fileName = nameArr[nameArr.length - 1];

                    fileExtension = "." + IOUtil.getExtension(fileName);


                    saveCommentAsync("", base64File, fileExtension);

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
}

