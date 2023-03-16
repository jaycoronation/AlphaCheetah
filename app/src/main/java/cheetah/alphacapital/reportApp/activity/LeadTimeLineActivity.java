package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLeadTimeLineBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.CapturedLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.LstLeadStatusDetail;
import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.textutils.CustomTextViewMedium;
import cheetah.alphacapital.textutils.CustomTextViewRegular;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadTimeLineActivity extends BaseActivity {
    private ActivityLeadTimeLineBinding binding;
    ArrayList<LstLeadStatusDetail> listTimeLine = new ArrayList<>();
    private Integer position = 0;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    TimelineAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_lead_time_line);
        initView();
        onClick();
    }

    private void initView() {
        binding.toolbar.txtTitle.setText("Lead Status");
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);

        if (getIntent().hasExtra("list"))
        {
            Bundle bundle = getIntent().getExtras();
            String jsonString = bundle.getString("list");

            Gson gson = new Gson();
            Type listOfdoctorType = new TypeToken<ArrayList<LstLeadStatusDetail>>() {}.getType();
            listTimeLine = gson.fromJson(jsonString,listOfdoctorType);
        }

        if (getIntent().hasExtra("position"))
        {
            position = getIntent().getIntExtra("position",0);
        }

        dataAdapter = new  TimelineAdapter(listTimeLine,activity);
        binding.rvLeadsTimeLine.setAdapter(dataAdapter);

    }

    private void onClick() {
        binding.toolbar.llBackNavigation.setOnClickListener(view -> finish());

    }
    private class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {
        List<LstLeadStatusDetail> listItems;
        private final Activity activity;
        private final SessionManager sessionManager;

        public TimelineAdapter(List<LstLeadStatusDetail> listEmployee, Activity activity)
        {
            this.listItems = listEmployee;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public int getItemViewType(int position) {
            return TimelineView.getTimeLineViewType(position, getItemCount());
        }

        @Override
        public TimelineAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_timeline, viewGroup, false);
            return new TimelineAdapter.ViewHolder(v,i);
        }

        @Override
        public void onBindViewHolder(final TimelineAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final LstLeadStatusDetail getSet = listItems.get(position);

                holder.tvDate.setText(AppUtils.universalDateConvert(getSet.getPlannedDateFormat(),"dd/MMM/yyyy","dd MMM,yyyy"));
                holder.tvTitle.setText(getSet.getLeadStatusName());
                if (getSet.getTimeDelay() == 0)
                {
                    holder.tvReason.setText("No Delay");
                    holder.tvReason.setTextColor(ContextCompat.getColor(activity,R.color.black));
                }
                else
                {
                    holder.tvReason.setText(getSet.getTimeDelay().toString() + " Days Delay");
                    holder.tvReason.setTextColor(ContextCompat.getColor(activity,R.color.rm_gradient_start));
                }

                holder.tvDateTime.setText(AppUtils.universalDateConvert(getSet.getActualDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd/MM/yyyy hh:mm a"));

                if (getSet.getStatus())
                {
                    holder.llDate.setVisibility(View.VISIBLE);
                    holder.tvDate.setVisibility(View.VISIBLE);
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvReason.setVisibility(View.VISIBLE);
                    holder.tvMarkComplted.setVisibility(View.GONE);
                    holder.timeline.setMarker(ContextCompat.getDrawable(activity,R.drawable.ic_marker_active));
                    holder.cvMain.setCardBackgroundColor(ContextCompat.getColor(activity,R.color.green_light));
                    holder.tvMarkComplted.setVisibility(View.GONE);
                }
                else
                {
                   /* if (getSet.isButtonVisible())
                    {
                        holder.tvMarkComplted.setText("Mark as Completed");
                        holder.tvMarkComplted.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        holder.tvMarkComplted.setVisibility(View.GONE);
                    }*/
                    holder.tvMarkComplted.setText("Mark as Completed");
                    holder.tvMarkComplted.setVisibility(View.VISIBLE);
                    holder.llDate.setVisibility(View.GONE);
                    holder.tvReason.setVisibility(View.GONE);
                    holder.timeline.setMarker(ContextCompat.getDrawable(activity,R.drawable.ic_marker_inactive));
                }

                holder.tvMarkComplted.setOnClickListener(view -> openConfirmationDialog(getSet));

                holder.ivEditDate.setOnClickListener(view -> openLeadCompletionDate(getSet));

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
            private final TextView tvDate;
            private final TextView tvTitle;
            private final TextView tvReason;
            private final TextView tvDateTime;
            private final TextView tvMarkComplted;
            private final TimelineView timeline;
            private final CardView cvMain;
            private final AppCompatImageView ivEditDate;
            private final LinearLayout llDate;

            ViewHolder(View convertView, int viewType)
            {
                super(convertView);
                timeline = convertView.findViewById(R.id.timeline);
                tvDate = convertView.findViewById(R.id.tvDate);
                tvTitle = convertView.findViewById(R.id.tvTitle);
                tvReason = convertView.findViewById(R.id.tvReason);
                tvDateTime = convertView.findViewById(R.id.tvDateTime);
                tvMarkComplted = convertView.findViewById(R.id.tvMarkComplted);
                cvMain = convertView.findViewById(R.id.cvMain);
                ivEditDate = convertView.findViewById(R.id.ivEditDate);
                llDate = convertView.findViewById(R.id.llDate);
                timeline.initLine(viewType);
            }
        }
    }

    private void openLeadCompletionDate(LstLeadStatusDetail getSet){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_lead_date, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llDate;
        final CustomAppEditText edtLeadDate;
        final CustomTextViewRegular tvDescription, txtYes;
        edtLeadDate = sheetView.findViewById(R.id.edtLeadDate);
        llDate = sheetView.findViewById(R.id.llDate);

        edtLeadDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker(edtLeadDate);
            }
        });

        llDate.setOnClickListener(view -> {
            try
            {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
                updateLeadDate(getSet,AppUtils.universalDateConvert(edtLeadDate.getText().toString().trim(),"dd MMM yyyy", "dd/MM/yyyy"));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        bottomSheetDialog.show();
    }

    private void datePicker(final EditText edtTaskName)
    {
        if (edtTaskName.getText().toString().trim().length() > 0)
        {
            try
            {
                String date = AppUtils.universalDateConvert(edtTaskName.getText().toString().trim(), "dd MMM yyyy", "dd/MM/yyyy");
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
                    edtTaskName.setText(showDate);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void updateLeadDate(LstLeadStatusDetail getSet, String Date) {
        Log.e("Date", Date);
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.updateLeadDate(Integer.parseInt(getSet.getId().toString()),Date+" 12:00 AM").enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (LeadsActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 1;
                            LeadsActivity.handler.sendMessage(message);
                        }
                        showSnackBar(response.body().getMessage());
                        getAllLeads();
                    }
                    else
                    {
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        showSnackBar(response.body().getMessage());
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                AppUtils.apiFailedSnackBar(activity);
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void openConfirmationDialog(LstLeadStatusDetail getSet)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final CustomTextViewMedium tvTitle;
        final CustomTextViewRegular tvDescription, txtYes;
        tvTitle = sheetView.findViewById(R.id.tvTitle);
        tvDescription = sheetView.findViewById(R.id.tvDescription);
        txtYes = sheetView.findViewById(R.id.txtYes);
        llCancel = sheetView.findViewById(R.id.llCancel);
        llRemove = sheetView.findViewById(R.id.llRemove);

        txtYes.setText("Change");
        tvTitle.setText("Change Status");
        tvDescription.setText("Are you sure you want to change the status");


        llCancel.setOnClickListener(view -> {
            try
            {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });


        llRemove.setOnClickListener(view -> {
            try
            {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
                updateLeads(getSet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        bottomSheetDialog.show();

    }

    private void updateLeads(LstLeadStatusDetail getSet) {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.updateLeads(Integer.parseInt(getSet.getId().toString()),"").enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (LeadsActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 1;
                            LeadsActivity.handler.sendMessage(message);
                        }
                        showSnackBar(response.body().getMessage());
                        getAllLeads();
                    }
                    else
                    {
                        showSnackBar(response.body().getMessage());
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                AppUtils.apiFailedSnackBar(activity);
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getAllLeads() {
        apiService.getCaputuredLeads(Integer.parseInt(sessionManager.getUserId()),1, 9999999).enqueue(new Callback<CapturedLeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<CapturedLeadsResponseModel> call, Response<CapturedLeadsResponseModel> response)
            {

                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {

                        for (CapturedLeadsResponseModel.LeadsData e : response.body().getData())
                        {
                            String last_active_status = "";
                            for (LstLeadStatusDetail data : e.getLstLeadStatusDetails())
                            {
                                data.setButtonVisible(false);
                                data.setChecked(false);
                                if (data.getStatus())
                                {
                                    data.setChecked(true);
                                }
                                else
                                {
                                    if (last_active_status.equals(""))
                                    {
                                        last_active_status = data.get$id();
                                        data.setButtonVisible(true);
                                    }
                                }
                            }
                        }

                        listTimeLine = response.body().getData().get(position).getLstLeadStatusDetails();
                        dataAdapter = new  TimelineAdapter(listTimeLine,activity);
                        binding.rvLeadsTimeLine.setAdapter(dataAdapter);
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                    }
                    else
                    {
                        //showToast(response.body().getMessage());
                    }
                }
                else
                {
                }

            }

            @Override
            public void onFailure(Call<CapturedLeadsResponseModel> call, Throwable t)
            {
            }
        });
    }
}