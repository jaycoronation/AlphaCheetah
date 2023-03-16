package cheetah.alphacapital.reportApp.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheetah.alphacapital.classes.RangeTimePickerDialog;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.activity.AddAUMActivity;
import cheetah.alphacapital.reportApp.activity.AddClientActivity;
import cheetah.alphacapital.reportApp.activity.admin.AddDarEntryActivity;
import cheetah.alphacapital.reportApp.getset.AUMListGetSet;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.ClientDetailByIdResponse;
import cheetah.alphacapital.reportApp.getset.SaveTaskResponse;
import cheetah.alphacapital.reportApp.hashtag.HashTagHelper;
import cheetah.alphacapital.textutils.CustomEditText;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.reportApp.getset.CommonGetSet;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.NoteActionResponse;

public class ClientOverViewFragment extends Fragment
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
    @BindView(R.id.txtFirstName)
    TextView txtFirstName;
    @BindView(R.id.txtLastName)
    TextView txtLastName;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtMobileNo)
    TextView txtMobileNo;
    @BindView(R.id.txtOrganization)
    TextView txtOrganization;
    @BindView(R.id.txtStatus)
    TextView txtStatus;
    @BindView(R.id.txtTotalMeeting)
    TextView txtTotalMeeting;
    @BindView(R.id.txtAUM)
    TextView txtAUM;
    @BindView(R.id.txtSIP)
    TextView txtSIP;
    @BindView(R.id.txtCollaborativeEmployee)
    TextView txtCollaborativeEmployee;
    @BindView(R.id.txtAddress)
    TextView txtAddress;
    @BindView(R.id.txtAddNote)
    AppCompatTextView txtAddNote;
    @BindView(R.id.txtNoNotes)
    AppCompatTextView txtNoNotes;
    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    @BindView(R.id.cardNotes)
    CardView cardNotes;
    @BindView(R.id.tvNote)
    AppCompatTextView tvNote;
    @BindView(R.id.tvAddDar)
    AppCompatTextView tvAddDar;
    @BindView(R.id.tvAddTask)
    AppCompatTextView tvAddTask;
    @BindView(R.id.tvAddAum)
    AppCompatTextView tvAddAum;
    @BindView(R.id.fabAddNote)
    FloatingActionButton fabAddNote;
    @BindView(R.id.fabAddAUM)
    FloatingActionButton fabAddAUM;
    @BindView(R.id.fabAddDAR)
    FloatingActionButton fabAddDAR;
    @BindView(R.id.fabAddTask)
    FloatingActionButton fabAddTask;
    @BindView(R.id.ivEdit)
    AppCompatImageView ivEdit;

    private String clientid = "";
    private Boolean isFABOpen = false;
    ArrayList<ClientDetailByIdResponse.DataBean.NotesBean> listNote = new ArrayList<ClientDetailByIdResponse.DataBean.NotesBean>();
    ClientDetailByIdResponse.DataBean.ClientDetailBean clientDetailBean = new ClientDetailByIdResponse.DataBean.ClientDetailBean();

    private NotesAdpater notesAdpater;
    private BottomSheetDialog dialog_Add_Notes;
    private LinearLayout fab1, fab2, fab3, fab4;
    private FloatingActionButton fab;

    public ClientOverViewFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_client_overview, container, false);

        activity = getActivity();

        sessionManager = new SessionManager(activity);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        ButterKnife.bind(this, rootView);

        if (getArguments().containsKey("clientid"))
        {
            clientid = getArguments().getString("clientid");
        }

        setupViews(rootView);

        onClicks();

        return rootView;
    }

    private void setupViews(View rootView)
    {
        rvNotes.setLayoutManager(new LinearLayoutManager(activity));
        try
        {
            if (sessionManager.isNetworkAvailable())
            {
                llNoInternet.setVisibility(View.GONE);
                getDetailsFromApi();
                getAllEmployee();
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

        fab = rootView.findViewById(R.id.fab);
        fab1 = rootView.findViewById(R.id.fab1);
        fab2 = rootView.findViewById(R.id.fab2);
        fab3 = rootView.findViewById(R.id.fab3);
        fab4 = rootView.findViewById(R.id.fab4);

        fab.setOnClickListener(view ->
        {
            if (!isFABOpen)
            {
                showFABMenu();
            }
            else
            {
                closeFABMenu();
            }
        });
    }

    private void showFABMenu()
    {
        isFABOpen = true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        fab4.animate().translationY(-getResources().getDimension(R.dimen.standard_205));
        tvNote.setVisibility(View.VISIBLE);
        tvAddAum.setVisibility(View.VISIBLE);
        tvAddDar.setVisibility(View.VISIBLE);
        tvAddTask.setVisibility(View.VISIBLE);
    }

    private void closeFABMenu()
    {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab4.animate().translationY(0);
        tvNote.setVisibility(View.GONE);
        tvAddAum.setVisibility(View.GONE);
        tvAddDar.setVisibility(View.GONE);
        tvAddTask.setVisibility(View.GONE);
        fab.setExpanded(false);
    }

    private BottomSheetDialog dialog_Add_Task;

    private void onClicks()
    {
        txtAddNote.setOnClickListener(v -> showAddTaskDialog(false, new ClientDetailByIdResponse.DataBean.NotesBean(), 0));

        txtMobileNo.setOnClickListener(v ->
        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + clientDetailBean.getContact_no()));
            try
            {
                startActivity(intent);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        });

        txtEmail.setOnClickListener(v ->
        {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, clientDetailBean.getEmail());
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        fabAddNote.setOnClickListener(v ->
        {
            showAddTaskDialog(false, new ClientDetailByIdResponse.DataBean.NotesBean(), 0);
            closeFABMenu();
        });

        fabAddAUM.setOnClickListener(v ->
        {
            Intent intent = new Intent(activity, AddAUMActivity.class);
            intent.putExtra("clientId", clientid);
            intent.putExtra("isFor", "add");
            intent.putExtra("AUMListGetSet", (Parcelable) new AUMListGetSet.DataBean());
            startActivity(intent);
        });

        fabAddDAR.setOnClickListener(v ->
        {
            Intent intent = new Intent(activity, AddDarEntryActivity.class);
            intent.putExtra("isFor", "add");
            intent.putExtra("isEditClient", false);
            intent.putExtra("clientId", clientid);
            startActivity(intent);
        });

        fabAddTask.setOnClickListener(v -> showAddTaskDialog());

        ivEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Gson gson = new Gson();
                Intent intent = new Intent(activity, AddClientActivity.class);
                intent.putExtra("isFor", "edit");
                intent.putExtra("clientDetailBean", gson.toJson(clientDetailBean));
                startActivity(intent);
            }
        });
    }

    private TextView txtAddReminder;
    private HashTagHelper hashTagHelper;
    public static String selectedReminderDate = "", selectedReminderTime = "";
    private String finalDateTime = "";
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    public ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee_Selected_Edit = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private EmployeeAdpater employeeAdpater;

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

        llMain = sheetView.findViewById(R.id.llMain);
        rvEmployeeList = sheetView.findViewById(R.id.rvEmployeeList);
        ivSetReminder = sheetView.findViewById(R.id.ivSetReminder);
        edtTaskName = sheetView.findViewById(R.id.edtTaskName);
        txtAddReminder = sheetView.findViewById(R.id.txtAddReminder);
        txtAddTask = sheetView.findViewById(R.id.txtAddTask);

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

                            saveTask(edtTaskName.getText().toString().trim(), contact, finalDateTime);

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
        Log.e("showDate ReminderDate:", selectedReminderDate);

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
        Call<SaveTaskResponse> call = apiService.addTask(AppUtils.getEmployeeIdForAdmin(activity), contact, "1", taskName, finalDateTime, clientid);

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
                            if (ClientDetailsToDoFragment.handler != null)
                            {
                                Message msgObj = Message.obtain();
                                msgObj.what = 1;
                                ClientDetailsToDoFragment.handler.sendMessage(msgObj);
                            }
                            Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getDetailsFromApi()
    {
        llLoading.setVisibility(View.VISIBLE);
        Call<ClientDetailByIdResponse> call = apiService.getClientDetailById(AppUtils.getEmployeeIdForAdmin(activity), clientid);
        call.enqueue(new Callback<ClientDetailByIdResponse>()
        {
            @Override
            public void onResponse(Call<ClientDetailByIdResponse> call, Response<ClientDetailByIdResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        listNote = new ArrayList<ClientDetailByIdResponse.DataBean.NotesBean>();
                        listNote.addAll(response.body().getData().getNotes());
                        clientDetailBean = response.body().getData().getClientDetail();
                        setData();
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }

                llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ClientDetailByIdResponse> call, Throwable t)
            {
                llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void getAllEmployee()
    {
        Call<AllEmployeeResponse> call = apiService.getAllEmployee(clientid, "0", "0");
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

    private void setData()
    {
        try
        {
            txtFirstName.setText(AppUtils.toDisplayCase(clientDetailBean.getFirst_name()));
            txtLastName.setText(AppUtils.toDisplayCase(clientDetailBean.getLast_name()));
            txtEmail.setText(clientDetailBean.getEmail());
            txtMobileNo.setText(clientDetailBean.getContact_no());

            if (clientDetailBean.isIs_active())
            {
                txtStatus.setText("Active");
            }
            else
            {
                txtStatus.setText("InActive");
            }

            if (clientDetailBean.getTotalMeeting() > 0)
            {
                txtTotalMeeting.setText(String.valueOf(clientDetailBean.getTotalMeeting()));
            }
            else
            {
                txtTotalMeeting.setText("N.A");
            }

            if (clientDetailBean.getMonth_End_AUM() > 0)
            {
                txtAUM.setText(activity.getResources().getString(R.string.ruppe) + AppUtils.convertToDecimalValueForListing(String.valueOf(clientDetailBean.getMonth_End_AUM())) + "");
            }
            else
            {
                txtAUM.setText("N.A");
            }
            if (clientDetailBean.getSIP() > 0)
            {
                txtSIP.setText(activity.getResources().getString(R.string.ruppe) + AppUtils.convertToDecimalValueForListing(String.valueOf(clientDetailBean.getSIP())) + "");
            }
            else
            {
                txtSIP.setText("N.A");
            }


            txtOrganization.setText(clientDetailBean.getOrganization());

            txtAddress.setText(clientDetailBean.getAddress() + ", " + clientDetailBean.getCity_name() + ", " + clientDetailBean.getState_name() + ", " + clientDetailBean.getCountry_name());


            String employeeName = "";

            if (clientDetailBean.getLstCollaborativeEmployee().size() > 0)
            {
                for (int j = 0; j < clientDetailBean.getLstCollaborativeEmployee().size(); j++)
                {
                    CommonGetSet commonGetSet = new CommonGetSet();
                    commonGetSet.setName(clientDetailBean.getLstCollaborativeEmployee().get(j).getFirst_name() + " " + clientDetailBean.getLstCollaborativeEmployee().get(j).getLast_name());
                    if (j == clientDetailBean.getLstCollaborativeEmployee().size() - 1)
                    {
                        employeeName = employeeName + commonGetSet.getName();
                    }
                    else
                    {
                        employeeName = employeeName + commonGetSet.getName() + ",";
                    }
                }
            }

            if (employeeName.length() > 0)
            {
                txtCollaborativeEmployee.setText(employeeName);
            }
            else
            {
                txtCollaborativeEmployee.setText("N.A");
            }

            if (listNote.size() > 0)
            {
                cardNotes.setVisibility(View.VISIBLE);
                txtNoNotes.setVisibility(View.GONE);
                notesAdpater = new NotesAdpater(listNote);
                rvNotes.setAdapter(notesAdpater);
            }
            else
            {
                cardNotes.setVisibility(View.GONE);
                txtNoNotes.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class NotesAdpater extends RecyclerView.Adapter<NotesAdpater.ViewHolder>
    {
        ArrayList<ClientDetailByIdResponse.DataBean.NotesBean> listItems;


        public NotesAdpater(ArrayList<ClientDetailByIdResponse.DataBean.NotesBean> listClient)
        {
            this.listItems = listClient;
        }

        @Override
        public NotesAdpater.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_notes_list, viewGroup, false);
            return new NotesAdpater.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final NotesAdpater.ViewHolder holder, final int position)
        {
            try
            {
                final ClientDetailByIdResponse.DataBean.NotesBean getSet = listItems.get(position);

                holder.tvNotes.setText(String.valueOf(getSet.getTitle()));
                holder.tvdate.setText(AppUtils.universalDateConvert(getSet.getCreated_date(), "yyyy-MM-dd'T'HH:mm:ss.SSS", "MMM dd,yyyy hh:mm a"));

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showAddTaskDialog(true, getSet, position);
                    }
                });

                holder.ivEdit.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showAddTaskDialog(true, getSet, position);
                    }
                });

                holder.ivDelete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        deleteNote(getSet, position);
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
            @BindView(R.id.tvNotes)
            TextView tvNotes;
            @BindView(R.id.tvdate)
            TextView tvdate;
            @BindView(R.id.ivDelete)
            ImageView ivDelete;
            @BindView(R.id.ivEdit)
            ImageView ivEdit;
            @BindView(R.id.viewline)
            View viewline;

            ViewHolder(View convertView)
            {
                super(convertView);
                ButterKnife.bind(this, convertView);
            }
        }
    }

    public void showAddTaskDialog(boolean isForEdit, ClientDetailByIdResponse.DataBean.NotesBean getset, int pos)
    {
        dialog_Add_Notes = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        dialog_Add_Notes.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog_Add_Notes.setCanceledOnTouchOutside(true);
        final View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_add_notes, null);
        dialog_Add_Notes.setContentView(sheetView);

        AppUtils.setLightStatusBarBottomDialog(dialog_Add_Notes, activity);

        final LinearLayout llMain;
        final ImageView ivAddNotes;
        final EditText edtNotes;

        llMain = sheetView.findViewById(R.id.llMain);
        ivAddNotes = sheetView.findViewById(R.id.ivAddNotes);
        edtNotes = sheetView.findViewById(R.id.edtNotes);
        edtNotes.requestFocus();


        if (isForEdit)
        {
            edtNotes.setText(getset.getTitle());
            edtNotes.setSelection(getset.getTitle().length());
        }

        dialog_Add_Notes.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
            }
        });


        ivAddNotes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                try
                {
                    AppUtils.hideKeyboard(edtNotes, activity);

                    dialog_Add_Notes.dismiss();

                    if (edtNotes.getText().toString().trim().length() > 0)
                    {
                        if (sessionManager.isNetworkAvailable())
                        {
                            if (isForEdit)
                            {
                                editNote(edtNotes.getText().toString().trim(), getset, pos);
                            }
                            else
                            {
                                addNote(edtNotes.getText().toString().trim());
                            }

                            edtNotes.setText("");
                        }
                        else
                        {
                            Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(activity, "Please enter note.", Toast.LENGTH_SHORT).show();
                    }


                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        dialog_Add_Notes.show();
    }

    private void addNote(String notes)
    {
        Call<NoteActionResponse> call = apiService.addNewNote(AppUtils.getEmployeeIdForAdmin(activity), clientid, notes, "");
        call.enqueue(new Callback<NoteActionResponse>()
        {
            @Override
            public void onResponse(Call<NoteActionResponse> call, Response<NoteActionResponse> response)
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            ClientDetailByIdResponse.DataBean.NotesBean notesBean = new ClientDetailByIdResponse.DataBean.NotesBean();
                            notesBean.setId(response.body().getData().getId());
                            notesBean.setClient_id(response.body().getData().getClient_id());
                            notesBean.setEmployee_id(response.body().getData().getEmployee_id());
                            notesBean.setEmployee_id_assign(response.body().getData().getEmployee_id_assign());
                            notesBean.setTitle(response.body().getData().getTitle());
                            notesBean.setDescription(response.body().getData().getDescription());
                            notesBean.setCreated_date(response.body().getData().getCreated_date());
                            notesBean.setUpdated_date(response.body().getData().getUpdated_date());
                            notesBean.setEmployee_f_name(response.body().getData().getEmployee_f_name());
                            notesBean.setEmployee_l_name(response.body().getData().getEmployee_l_name());
                            listNote.add(notesBean);

                            if (listNote.size() == 1)
                            {
                                notesAdpater = new NotesAdpater(listNote);
                                rvNotes.setAdapter(notesAdpater);
                            }
                            else
                            {
                                notesAdpater.notifyDataSetChanged();
                            }

                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<NoteActionResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void editNote(String notes, ClientDetailByIdResponse.DataBean.NotesBean getset, int pos)
    {
        Call<NoteActionResponse> call = apiService.updateNote(AppUtils.getEmployeeIdForAdmin(activity), clientid, notes, "", String.valueOf(getset.getId()));
        call.enqueue(new Callback<NoteActionResponse>()
        {
            @Override
            public void onResponse(Call<NoteActionResponse> call, Response<NoteActionResponse> response)
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            ClientDetailByIdResponse.DataBean.NotesBean notesBean = new ClientDetailByIdResponse.DataBean.NotesBean();
                            notesBean.setId(response.body().getData().getId());
                            notesBean.setClient_id(response.body().getData().getClient_id());
                            notesBean.setEmployee_id(response.body().getData().getEmployee_id());
                            notesBean.setEmployee_id_assign(response.body().getData().getEmployee_id_assign());
                            notesBean.setTitle(response.body().getData().getTitle());
                            notesBean.setDescription(response.body().getData().getDescription());
                            notesBean.setCreated_date(response.body().getData().getCreated_date());
                            notesBean.setUpdated_date(response.body().getData().getUpdated_date());
                            notesBean.setEmployee_f_name(response.body().getData().getEmployee_f_name());
                            notesBean.setEmployee_l_name(response.body().getData().getEmployee_l_name());

                            listNote.set(pos, notesBean);
                            notesAdpater.notifyItemChanged(pos);
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<NoteActionResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void deleteNote(ClientDetailByIdResponse.DataBean.NotesBean getset, int pos)
    {
        Call<CommonResponse> call = apiService.deleteNote(String.valueOf(getset.getId()));
        call.enqueue(new Callback<CommonResponse>()
        {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            listNote.remove(pos);
                            notesAdpater.notifyDataSetChanged();

                            if (listNote.size() > 0)
                            {
                                cardNotes.setVisibility(View.VISIBLE);
                                txtNoNotes.setVisibility(View.GONE);
                            }
                            else
                            {
                                cardNotes.setVisibility(View.GONE);
                                txtNoNotes.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else
                    {
                        AppUtils.apiFailedSnackBar(activity);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
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
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

    }
}
