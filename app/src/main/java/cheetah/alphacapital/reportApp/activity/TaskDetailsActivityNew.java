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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.github.dhaval2404.imagepicker.constant.ImageProvider;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.mit.mitspermissions.MitsPermissions;
import com.android.mit.mitspermissions.PermissionListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.classes.ImagePath;
import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.classes.SoftKeyboard;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.fragment.ClientDetailsToDoFragment;
import cheetah.alphacapital.reportApp.getset.CommentsGetSet;
import cheetah.alphacapital.reportApp.getset.TaskDetailsResponse;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.IOUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.CommentCountGetSet;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.SaveTaskResponse;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;


public class TaskDetailsActivityNew extends BaseActivity
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
    TextView txtTitle;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
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
    TextView txtLoading;
    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.txtRetry)
    TextView txtRetry;
    @BindView(R.id.llRetry)
    LinearLayout llRetry;
    @BindView(R.id.llNoInternet)
    LinearLayout llNoInternet;
    @BindView(R.id.ivNoData)
    ImageView ivNoData;
    @BindView(R.id.tvNoDataText)
    TextView tvNoDataText;
    @BindView(R.id.llNoData)
    LinearLayout llNoData;
    @BindView(R.id.txtTaskName)
    TextView txtTaskName;
    @BindView(R.id.txtShowMore)
    TextView txtShowMore;
    @BindView(R.id.txtReminderDate)
    TextView txtReminderDate;
    @BindView(R.id.viewLineReminderDate)
    View viewLineReminderDate;
    @BindView(R.id.rvAssignTo)
    RecyclerView rvAssignTo;
    @BindView(R.id.llDetails)
    LinearLayout llDetails;
    @BindView(R.id.llNoDataComment)
    LinearLayout llNoDataComment;
    @BindView(R.id.rvComments)
    RecyclerView rvComments;
    @BindView(R.id.edtComment)
    EditText edtComment;
    @BindView(R.id.llAttachFile)
    LinearLayout llAttachFile;
    @BindView(R.id.ivSendComment)
    ImageView ivSendComment;
    @BindView(R.id.progressSend)
    ProgressBar progressSend;
    @BindView(R.id.llBottom)
    LinearLayout llBottom;
    @BindView(R.id.llMainComment)
    LinearLayout llMainComment;
    @BindView(R.id.llSelfTask)
    LinearLayout llSelfTask;
    @BindView(R.id.llMainLayout)
    LinearLayout llMainLayout;

    @BindView(R.id.llClientName)
    LinearLayout llClientName;
    @BindView(R.id.txtClientName)
    TextView txtClientName;

    private final boolean isStatusBarHidden = false;
    public static boolean isApiCalling = false;
    public boolean isCommentCalling = false;
    private boolean isSelfTask = false;
    public static Handler handler;
    SoftKeyboard softKeyboard;
    private final boolean isAction = false;
    private boolean isFromNotification = false;

    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Contact = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<CommentsGetSet.DataBean> listComment = new ArrayList<CommentsGetSet.DataBean>();
    private CommentAdapter commentAdapter;

    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

    private TextView txtAddReminderEdit;
    private HashTagHelper hashTagHelperEdit;
    private EmployeeAdpaterEdit employeeAdpaterEdit;
    public static String selectedReminderDateEdit = "", selectedReminderTimeEdit = "";
    private String finalDateTimeEdit = "";
    String date_timeEdit = "", task_id = "", clientId = "", task_added_by = "";
    int mYearEdit;
    int mMonthEdit;
    int mDayEdit;

    //For ProfilePic
    private final int SELECT_FILE = 1;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PICK_FROM_GALLERY_CROP = 212;
    private String imageFilePath = "", fileExtension = "", base64File = "";

    private TaskDetailsResponse.DataBean.TaskDetailBean taskDetailBean = new TaskDetailsResponse.DataBean.TaskDetailBean();
    private ArrayList<TaskDetailsResponse.DataBean.EmployeeListBean> listDetailEmployee = new ArrayList<TaskDetailsResponse.DataBean.EmployeeListBean>();
    private ArrayList<TaskDetailsResponse.DataBean.ClientListBean> listDetailClient = new ArrayList<TaskDetailsResponse.DataBean.ClientListBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_details_new);
        ButterKnife.bind(this);

        task_id = AppUtils.getValidAPIStringResponse(getIntent().getExtras().getString("task_id"));

        clientId = AppUtils.getValidAPIStringResponse(getIntent().getExtras().getString("clientId"));

        task_added_by = AppUtils.getValidAPIStringResponse(getIntent().getExtras().getString("task_added_by"));

        if (getIntent().hasExtra("isFromNotification"))
        {
            isFromNotification = getIntent().getBooleanExtra("isFromNotification", false);
        }

        sessionManager.setTaskId(String.valueOf(task_id));

        setupViews();

        //AppUtils.setLightStatusBar(activity);

        onClickEvents();

        if (sessionManager.isNetworkAvailable())
        {
            llNoInternet.setVisibility(View.GONE);
            getTaskData(true);
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
                    if (msg.what == 100)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            llNoInternet.setVisibility(View.GONE);
                            getTaskData(true);
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
            ivLogout.setVisibility(View.VISIBLE);
            ivLogout.setImageResource(R.drawable.ic_dot);
            rvAssignTo.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
            rvComments.setLayoutManager(new LinearLayoutManager(activity));
           /* setSupportActionBar(toolbar);
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
                    if (isFromNotification)
                    {
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        AppUtils.hideKeyboard(toolbar, activity);
                        activity.finish();
                    }
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
                    getTaskData(true);
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

    private void getTaskData(boolean isForAllApiCall)
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<TaskDetailsResponse> call = apiService.getTaskDetailById(task_id);
        call.enqueue(new Callback<TaskDetailsResponse>()
        {
            @Override
            public void onResponse(Call<TaskDetailsResponse> call, Response<TaskDetailsResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            taskDetailBean = new TaskDetailsResponse.DataBean.TaskDetailBean();
                            listDetailEmployee = new ArrayList<TaskDetailsResponse.DataBean.EmployeeListBean>();
                            listDetailClient = new ArrayList<TaskDetailsResponse.DataBean.ClientListBean>();
                            taskDetailBean = response.body().getData().getTaskDetail();
                            listDetailEmployee.addAll(response.body().getData().getEmployeeList());
                            listDetailClient.addAll(response.body().getData().getClientList());
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

                    if (isForAllApiCall)
                    {
                        getAllComment();
                    }
                    else
                    {
                        setData();
                    }

                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                    if (isForAllApiCall)
                    {
                        getAllComment();
                    }
                    else
                    {
                        setData();
                    }
                    //llLoading.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<TaskDetailsResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);

                if (isForAllApiCall)
                {
                    getAllComment();
                }
                else
                {
                    setData();
                }
                //llLoading.setVisibility(View.GONE);

            }
        });
    }

    private void getAllComment()
    {
        Call<CommentsGetSet> call = apiService.getAllTaskMessageByTaskId(task_id);
        call.enqueue(new Callback<CommentsGetSet>()
        {
            @Override
            public void onResponse(Call<CommentsGetSet> call, Response<CommentsGetSet> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            listComment = new ArrayList<CommentsGetSet.DataBean>();
                            listComment.addAll(response.body().getData());
                            for (int i = 0; i < listComment.size(); i++)
                            {
                                listComment.get(i).setFullname(listComment.get(i).getFirst_name() + " " + listComment.get(i).getLast_name());
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        //llLoading.setVisibility(View.GONE);
                    }
                    getAllEmployee();
                }
                else
                {
                    //llLoading.setVisibility(View.GONE);
                    getAllEmployee();
                }
            }

            @Override
            public void onFailure(Call<CommentsGetSet> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                getAllEmployee();
            }
        });


    }

    private void getAllEmployee()
    {
        Call<AllEmployeeResponse> call = apiService.getAllEmployee("0", "0", "0");
        call.enqueue(new Callback<AllEmployeeResponse>()
        {
            @Override
            public void onResponse(Call<AllEmployeeResponse> call, Response<AllEmployeeResponse> response)
            {
                if (response.isSuccessful())
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
                    setData();
                }
                else
                {
                    setData();
                }

                //llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {
                setData();
            }
        });


    }


    private void setData()
    {

        if (taskDetailBean.getTask_status_id() == 2)
        {
            txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskDetailBean.getTask_message())));
            txtTaskName.setPaintFlags(txtTaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            txtTaskName.setText(Html.fromHtml(AppUtils.applyColor(taskDetailBean.getTask_message())));
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

        if (taskDetailBean.getDue_date() != null && taskDetailBean.getDue_date().length() > 0)
        {
            txtReminderDate.setVisibility(View.VISIBLE);
            viewLineReminderDate.setVisibility(View.VISIBLE);
            txtReminderDate.setText(AppUtils.universalDateConvert(taskDetailBean.getDue_date(), "dd-MM-yyyy HH:mm:ss", "dd MMM yyyy"));
        }
        else
        {
            txtReminderDate.setVisibility(View.GONE);
            viewLineReminderDate.setVisibility(View.GONE);
        }

        if (listDetailClient.size() > 0)
        {
            String name = "";
            llClientName.setVisibility(View.VISIBLE);

            for (int i = 0; i < listDetailClient.size(); i++)
            {
                if (name.length() == 0)
                {
                    name = listDetailClient.get(i).getClient_name();
                }
                else
                {
                    name = name + "," + listDetailClient.get(i).getClient_name();
                }
            }

            txtClientName.setText("Clients :" + name);
        }
        else
        {
            llClientName.setVisibility(View.GONE);
        }

        listEmployee_Contact = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
        AllEmployeeResponse.DataBean.AllEmployeeBean allEmployeeBean1 = new AllEmployeeResponse.DataBean.AllEmployeeBean();
        allEmployeeBean1.setFullname(task_added_by);
        allEmployeeBean1.setId(taskDetailBean.getEmployee_id());
        allEmployeeBean1.setIs_selected(true);
        listEmployee_Contact.add(allEmployeeBean1);


        if (listDetailEmployee.size() > 0)
        {
            llMainComment.setVisibility(View.VISIBLE);
            llSelfTask.setVisibility(View.GONE);
            isSelfTask = false;
            for (int i = 0; i < listDetailEmployee.size(); i++)
            {
                AllEmployeeResponse.DataBean.AllEmployeeBean allEmployeeBean = new AllEmployeeResponse.DataBean.AllEmployeeBean();
                allEmployeeBean.setFullname(listDetailEmployee.get(i).getEmployee_name());
                allEmployeeBean.setId(listDetailEmployee.get(i).getEmployee_id());
                allEmployeeBean.setIs_selected(false);
                listEmployee_Contact.add(allEmployeeBean);
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

        if (TaskListActivity.handler != null)
        {
            Message msgObj = Message.obtain();
            msgObj.what = 100;
            TaskListActivity.handler.sendMessage(msgObj);
        }
        llLoading.setVisibility(View.GONE);
    }

    private void saveCommentAsync(final String comment, final String imageName, final String fileExtension)
    {
        String msg_txt = "", FileExtension = "", is_image = "", img_url = "";

        if (imageName.length() > 0)
        {
            msg_txt = "";
            FileExtension = fileExtension;
            is_image = String.valueOf(true);
            img_url = imageName;
        }
        else
        {
            msg_txt = comment;
            FileExtension = "";
            is_image = String.valueOf(false);
            img_url = "";
        }


        edtComment.setText("");
        ivSendComment.setVisibility(View.GONE);
        progressSend.setVisibility(View.GONE);
        isCommentCalling = true;
        Call<CommentResponse> call = apiService.saveTaskMessage(AppUtils.getEmployeeIdForAdmin(activity), task_id, msg_txt, FileExtension, is_image, img_url);
        call.enqueue(new Callback<CommentResponse>()
        {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            CommentsGetSet.DataBean dataBean = new CommentsGetSet.DataBean();
                            dataBean.setId(response.body().getData().getId());
                            dataBean.setEmployee_id(response.body().getData().getEmployee_id());
                            dataBean.setTask_id(response.body().getData().getTask_id());
                            dataBean.setMsg_txt(response.body().getData().getMsg_txt());
                            dataBean.setImg_url(response.body().getData().getImg_url());
                            dataBean.setIs_image(response.body().getData().isIs_image());
                            dataBean.setAdded_date(response.body().getData().getAdded_date());
                            listComment.add(dataBean);

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

                            isCommentCalling = false;
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
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }

                progressSend.setVisibility(View.GONE);
                ivSendComment.setVisibility(View.VISIBLE);
                llNoDataComment.setVisibility(View.GONE);
                rvComments.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private static class EmployeeAdpater extends RecyclerView.Adapter<EmployeeAdpater.ViewHolder>
    {
        private final ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> items;

        EmployeeAdpater(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list)
        {
            this.items = list;

        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_task_details_contact_new, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
        {
            final AllEmployeeResponse.DataBean.AllEmployeeBean employeeBean = items.get(position);

            if (employeeBean.isIs_selected())
            {
                holder.txtAssignBy.setVisibility(View.VISIBLE);
                holder.viewline.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.txtAssignBy.setVisibility(View.INVISIBLE);
                holder.viewline.setVisibility(View.GONE);
            }

            if (employeeBean.getFullname().length() > 0)
            {
                String[] datearr = employeeBean.getFullname().split("\\s");

                if (datearr.length >= 1)
                {
                    holder.txtUserName.setText(AppUtils.toDisplayCase(datearr[0]));

                }
                else
                {
                    holder.txtUserName.setText(AppUtils.toDisplayCase(employeeBean.getFullname()));

                }

                // holder.txtUserName.setText(AppUtils.toDisplayCase(employeeBean.getFullname()));

                holder.txtUserSortName.setText(AppUtils.getSortName(AppUtils.toDisplayCase(employeeBean.getFullname())));
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

                txtUserSortName = convertView.findViewById(R.id.txtUserSortName);
                txtAssignBy = convertView.findViewById(R.id.txtAssignBy);
                txtUserName = convertView.findViewById(R.id.txtUserName);
                viewline = convertView.findViewById(R.id.viewline);
            }
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
    {
        private final ArrayList<CommentsGetSet.DataBean> items;

        CommentAdapter(ArrayList<CommentsGetSet.DataBean> list)
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
            final CommentsGetSet.DataBean commentsGetSet = items.get(position);

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

                holder.txtSortNameLeft.setText(AppUtils.getSortName(AppUtils.toDisplayCase(commentsGetSet.getFullname())));
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
                llLeft = convertView.findViewById(R.id.llLeft);
                txtSortNameLeft = convertView.findViewById(R.id.txtSortNameLeft);
                txtMsgLeft = convertView.findViewById(R.id.txtMsgLeft);
                imgLeft = convertView.findViewById(R.id.imgLeft);
                txtDateLeft = convertView.findViewById(R.id.txtDateLeft);
                llRight = convertView.findViewById(R.id.llRight);
                txtMsgRight = convertView.findViewById(R.id.txtMsgRight);
                imgRight = convertView.findViewById(R.id.imgRight);
                txtDateRight = convertView.findViewById(R.id.txtDateRight);
            }
        }
    }

    private void readAllComment()
    {
        Call<CommonResponse> call = apiService.markAsReadMessage(task_id, AppUtils.getEmployeeIdForAdmin(activity));
        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        try
                        {
                            if (TaskListActivity.handler != null)
                            {
                                CommentCountGetSet commentCountGetSet = new CommentCountGetSet();
                                commentCountGetSet.setTaskId(taskDetailBean.getId());
                                commentCountGetSet.setComment(0);
                                Message msgObj = Message.obtain();
                                msgObj.what = 105;
                                msgObj.obj = commentCountGetSet;
                                TaskListActivity.handler.sendMessage(msgObj);
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
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
            }
        });
    }

    public void showTaskActionDialog()
    {
        final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        dialog.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_task_action, null);
        dialog.setContentView(sheetView);
        AppUtils.configureBottomSheetBehavior(sheetView);

        AppUtils.setLightStatusBarBottomDialog(dialog, activity);

        final LinearLayout llMain;
        final LinearLayout llEdit;
        final LinearLayout llDelete;
        final LinearLayout llComplete;

        llMain = sheetView.findViewById(R.id.llMain);
        llEdit = sheetView.findViewById(R.id.llEdit);
        llDelete = sheetView.findViewById(R.id.llDelete);
        llComplete = sheetView.findViewById(R.id.llComplete);

        if (taskDetailBean.getTask_status_id() == 2)
        {
            llEdit.setVisibility(View.GONE);
            llComplete.setVisibility(View.GONE);
        }
        else
        {
            llEdit.setVisibility(View.VISIBLE);
            llComplete.setVisibility(View.VISIBLE);
        }

        if (taskDetailBean.getEmployee_id() == Integer.parseInt(sessionManager.getUserId()))
        {
            llDelete.setVisibility(View.VISIBLE);
        }
        else
        {
            llDelete.setVisibility(View.GONE);
        }


        dialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
                hideKeyboard();
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
                            if (listEmployee_Contact.get(i).getId() != taskDetailBean.getEmployee_id())
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

                    if (taskDetailBean.getDue_date() != null && taskDetailBean.getDue_date().length() > 0)
                    {
                        editTaskAsync(taskDetailBean.getTask_message().trim(), contact_id, AppUtils.universalDateConvert(taskDetailBean.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"), Integer.parseInt(task_id), "2");
                    }
                    else
                    {
                        editTaskAsync(taskDetailBean.getTask_message().trim(), contact_id, "", Integer.parseInt(task_id), "2");
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
        final EditText edtTaskName;
        final TextView txtAddTask;

        llMain = sheetView.findViewById(R.id.llMain);
        rvEmployeeList = sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = sheetView.findViewById(R.id.edtTaskName);
        txtAddReminderEdit = sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = sheetView.findViewById(R.id.txtAddTask);
        edtTaskName.requestFocus();
        rvEmployeeList.setLayoutManager(new LinearLayoutManager(activity));
        hashTagHelperEdit = HashTagHelper.Creator.create(ContextCompat.getColor(activity, R.color.blue_new), null);
        hashTagHelperEdit.handle(edtTaskName);

        edtTaskName.setText(taskDetailBean.getTask_message().trim());

        try
        {
            if (taskDetailBean.getTask_message().trim().length() > 0)
            {
                edtTaskName.setSelection(taskDetailBean.getTask_message().trim().length());
            }
        }
        catch (Exception e)
        {
            edtTaskName.setSelection(0);
            e.printStackTrace();
        }

        if (taskDetailBean.getDue_date().length() > 0)
        {
            txtAddReminderEdit.setVisibility(View.VISIBLE);
            txtAddReminderEdit.setText(AppUtils.universalDateConvert(taskDetailBean.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy hh:mm a"));
            selectedReminderDateEdit = AppUtils.universalDateConvert(taskDetailBean.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy");
            selectedReminderTimeEdit = AppUtils.universalDateConvert(taskDetailBean.getDue_date(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm a");

            finalDateTimeEdit = AppUtils.universalDateConvert(selectedReminderDateEdit.trim(), "dd MMM yyyy", "dd/MM/yyyy") + " " + selectedReminderTimeEdit.trim();

            Log.e("*** Final Date select", finalDateTimeEdit + "");
        }
        else
        {
            txtAddReminderEdit.setVisibility(View.GONE);
        }


        listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

        if (listDetailEmployee.size() > 0)
        {
            for (int i = 0; i < listDetailEmployee.size(); i++)
            {
                if (!AppUtils.getEmployeeIdForAdmin(activity).equalsIgnoreCase(String.valueOf(listDetailEmployee.get(i).getEmployee_id())))
                {
                    AllEmployeeResponse.DataBean.AllEmployeeBean assignedClientGetSetOld1 = new AllEmployeeResponse.DataBean.AllEmployeeBean();
                    String toDisplayName = AppUtils.toDisplayCase(listDetailEmployee.get(i).getEmployee_name());
                    assignedClientGetSetOld1.setFullname("@" + toDisplayName.replaceAll("\\s+", "") + " ");
                    assignedClientGetSetOld1.setId(listDetailEmployee.get(i).getEmployee_id());
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
                        return;
                    }


                    //  String str = String.valueOf(s.toString().charAt(start));

                    final String hashtag = AppUtils.getValidAPIStringResponse(AppUtils.getHashtagFromString(s.toString(), start));

                    Log.e("<><> HASHTAG LENTH :: ", hashtag + "");


                    if (hashtag.length() > 1 && hashtag.startsWith("@"))
                    {
                        listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

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
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_emplyee_to_do_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
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

        Call<SaveTaskResponse> call = apiService.updateTask(String.valueOf(id), AppUtils.getEmployeeIdForAdmin(activity), contact, task_status_id, taskName, reminderDate, clientId);

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
                            if (sessionManager.isNetworkAvailable())
                            {
                                llNoInternet.setVisibility(View.GONE);
                                getTaskData(false);
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

                            if (ClientDetailsToDoFragment.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                ClientDetailsToDoFragment.handler.sendMessage(message);
                            }

                            if (TaskListActivity.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                TaskListActivity.handler.sendMessage(message);
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
            public void onFailure(Call<SaveTaskResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });


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

        Call<CommonResponse> call = apiService.deleteTask(task_id, AppUtils.getEmployeeIdForAdmin(activity));

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
                            finish();
                            finishActivityAnimation();

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

                            if (ClientDetailsToDoFragment.handler != null)
                            {
                                Message message = Message.obtain();
                                message.what = 100;
                                ClientDetailsToDoFragment.handler.sendMessage(message);
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
            public void onFailure(Call<CommonResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
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

            items = new CharSequence[]{
                    "Take Photo",
                    "Choose From Library",
                    "Cancel"
            };

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
                        //cameraImagePicker();
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

    private void cameraImagePicker()
    {
        ImagePicker.Companion.with(activity).crop().cameraOnly().compress(1024).maxResultSize(1080, 1080).start();
    }

    private void takeCameraImage()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createImageFile();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            Log.e("<><><>PHOTO FILe", photoFile.getAbsolutePath() + " ===== ");
            Log.e("<><><>imageFilePath", imageFilePath + " ===== ");

            if (photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this, activity.getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }
        }

    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
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
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            {
                try
                {
                    tempPath = imageFilePath;

                    File fl1 = new File(tempPath);

                    tempPath = fl1.getAbsolutePath();

                    Log.v("tempPath to store", tempPath + "*");

                    File newFilePath = ImagePath.compressImage(activity,tempPath);

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
            else
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    //Image Uri will not be null for RESULT_OK
                    Uri uri = data.getData();
                    File file = new File(String.valueOf(uri));

                    Log.e("<><><><PATH",file.getAbsolutePath());

                    imageFilePath = file.getAbsolutePath();

                    byte[] byteArray = null;

                    final File fl = ImagePath.compressImage(activity, imageFilePath);

                    try
                    {
                        byteArray = IOUtil.readFile(file);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    base64File = Base64.encodeToString(byteArray, Base64.DEFAULT);
    
                    byteArray = null;

                    String[] nameArr = file.getAbsolutePath().split("/");

                    String fileName = nameArr[nameArr.length - 1];

                    fileExtension = "." + IOUtil.getExtension(fileName);

                    saveCommentAsync("", base64File, fileExtension);
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

