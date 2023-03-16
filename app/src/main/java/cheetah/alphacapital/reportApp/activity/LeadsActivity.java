package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLeadsBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.ActivityListGetSet;
import cheetah.alphacapital.reportApp.getset.AddCapturedLeadResponseModel;
import cheetah.alphacapital.reportApp.getset.CapturedLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.LstLeadStatusDetail;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadsActivity extends BaseActivity {

    private ActivityLeadsBinding binding;

    private List<CapturedLeadsResponseModel.LeadsData> listLeads = new ArrayList<>();
    private LeadsListAdapter leadsListAdapter;

    //paging
    int pageIndex = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    int pageResults = 25;

    public static Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_leads);

        handler = new Handler(message -> {
            if(message.what==1)
            {
                getAllLeads(true);
            }
            return false;
        });

        initView();
        onClick();
    }

    private void initView() {
        binding.toolbar.txtTitle.setText("Manage Leads");
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            getAllLeads(true);
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }
    }

    private void onClick() {
        binding.toolbar.llBackNavigation.setOnClickListener(view -> finish());

        binding.ivAddLeads.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddCaptureLeadsActivity.class);
            startActivity(intent);
        });

        binding.llNoInternet.llRetry.setOnClickListener(view -> {
            if (sessionManager.isNetworkAvailable())
            {
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                getAllLeads(true);
            }
            else
            {
                binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getAllLeads(boolean isFirstTime) {
        if (isFirstTime)
        {
            binding.llLoading.llLoading.setVisibility(View.VISIBLE);
            pageIndex = 1;
            isLoading = false;
            isLastPage = false;
            pageResults = 25;
        }
        apiService.getCaputuredLeads(Integer.parseInt(sessionManager.getUserId()),pageIndex, pageResults).enqueue(new Callback<CapturedLeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<CapturedLeadsResponseModel> call, Response<CapturedLeadsResponseModel> response)
            {

                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        if (isFirstTime)
                        {
                            if (listLeads.size() > 0)
                            {
                                listLeads = new ArrayList<>();
                            }
                        }
                        ArrayList<CapturedLeadsResponseModel.LeadsData> tempList = new ArrayList<>();
                        tempList.addAll(response.body().getData());
                        listLeads.addAll(tempList);

                        if (tempList.size() != 0)
                        {
                            pageIndex += 1;
                            if (tempList.size() == 0 || tempList.size() % pageResults != 0)
                            {
                                isLastPage = true;
                            }
                        }
                        isLoading = false;

                        if (isFirstTime)
                        {
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                            if (listLeads.size() > 0)
                            {
                                for (CapturedLeadsResponseModel.LeadsData e : listLeads) {
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

                                leadsListAdapter = new LeadsListAdapter(listLeads, activity);
                                binding.rvLeads.setAdapter(leadsListAdapter);
                                binding.llNoData.llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            leadsListAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        //showToast(response.body().getMessage());
                    }
                }
                else
                {
                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                }

                binding.llMore.setVisibility(View.GONE);
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CapturedLeadsResponseModel> call, Throwable t)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }
        });
    }

    private class LeadsListAdapter extends RecyclerView.Adapter<LeadsListAdapter.ViewHolder> {
        List<CapturedLeadsResponseModel.LeadsData> listItems;
        private Activity activity;
        private SessionManager sessionManager;

        public LeadsListAdapter(List<CapturedLeadsResponseModel.LeadsData> listEmployee, Activity activity)
        {
            this.listItems = listEmployee;
            this.activity = activity;
            sessionManager = new SessionManager(activity);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_leads_list, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            try
            {
                final CapturedLeadsResponseModel.LeadsData getSet = listItems.get(position);

                holder.tvLeadName.setText(getSet.getLeadName());
                holder.tvDate.setText(getSet.getAddedDateFormat());
                if (getSet.getLastLeadStatusName() != null)
                {
                    holder.tvStatus.setText(getSet.getLastLeadStatusName());
                }
                else
                {
                    holder.tvStatus.setText("Get Started");
                }

                if (getSet.getTimeDelay() != null)
                {
                    holder.tvDelay.setText(getSet.getTimeDelay() + " Days");
                }
                else
                {
                    holder.tvDelay.setText("On Track");
                }

                if (getSet.getEmployeeId().toString().equalsIgnoreCase(sessionManager.getUserId()))
                {
                    holder.ivEdit.setVisibility(View.VISIBLE);
                    holder.ivDelete.setVisibility(View.VISIBLE);
                }
                else
                {
                    holder.ivEdit.setVisibility(View.GONE);
                    holder.ivDelete.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(view -> openDetailsBottomSheet(getSet));

                holder.tvProgress.setOnClickListener(view -> {
                    Intent intent = new Intent(activity, LeadTimeLineActivity.class);
                    intent.putExtra("list",new Gson().toJson(getSet.getLstLeadStatusDetails()));
                    intent.putExtra("position",position);
                    startActivity(intent);
                });

                holder.ivDelete.setOnClickListener(view -> selectDeleteDialog(getSet));

                holder.ivEdit.setOnClickListener(view -> {
                    Intent intent = new Intent(activity, AddCaptureLeadsActivity.class);
                    intent.putExtra("data",new Gson().toJson(getSet));
                    startActivity(intent);
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
            private TextView tvLeadName,tvDate, tvStatus, tvDelay,tvProgress;
            private ImageView ivEdit, ivDelete;

            ViewHolder(View convertView)
            {
                super(convertView);
                ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
                ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
                tvProgress = (TextView) convertView.findViewById(R.id.tvProgress);
                tvLeadName = (TextView) convertView.findViewById(R.id.tvLeadName);
                tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
                tvDelay = (TextView) convertView.findViewById(R.id.tvDelay);
            }
        }
    }

    public void selectDeleteDialog(CapturedLeadsResponseModel.LeadsData getSet)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_delete_activity, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final LinearLayout llRemove, llCancel;
        final TextView tvTitle, tvDescription;
        llCancel = (LinearLayout) sheetView.findViewById(R.id.llCancel);
        llRemove = (LinearLayout) sheetView.findViewById(R.id.llRemove);
        tvTitle = (TextView) sheetView.findViewById(R.id.tvTitle);
        tvDescription = (TextView) sheetView.findViewById(R.id.tvDescription);

        tvTitle.setText("Remove Lead");
        tvDescription.setText("Are you sure you want to remove this lead");

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
                deleteLeadAPI(getSet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });


        bottomSheetDialog.show();

    }

    private void deleteLeadAPI(CapturedLeadsResponseModel.LeadsData getSet) {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.deleteLead(Integer.parseInt(sessionManager.getUserId()),Integer.parseInt(getSet.getId().toString())).enqueue(new Callback<AddCapturedLeadResponseModel>() {
            @Override
            public void onResponse(Call<AddCapturedLeadResponseModel> call, Response<AddCapturedLeadResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        showToast(response.body().getMessage());
                        getAllLeads(true);
                    }
                }
                else
                {
                    AppUtils.apiFailedSnackBar(activity);
                }
            }

            @Override
            public void onFailure(Call<AddCapturedLeadResponseModel> call, Throwable t) {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    private void openDetailsBottomSheet(CapturedLeadsResponseModel.LeadsData getSet) {
        try {
            final BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_lead_details);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);

            AppUtils.setLightStatusBarDialog(dialog, activity);

            final AppCompatTextView tvTitle = (AppCompatTextView) dialog.findViewById(R.id.tvTitle);
            final AppCompatTextView tvCapBy = (AppCompatTextView) dialog.findViewById(R.id.tvCapBy);
            final AppCompatTextView tvName = (AppCompatTextView) dialog.findViewById(R.id.tvName);
            final AppCompatTextView tvEmail = (AppCompatTextView) dialog.findViewById(R.id.tvEmail);
            final AppCompatTextView tvMobileNo = (AppCompatTextView) dialog.findViewById(R.id.tvMobileNo);
            final AppCompatTextView tvWhoGave = (AppCompatTextView) dialog.findViewById(R.id.tvWhoGave);
            final AppCompatTextView tvLeadSource = (AppCompatTextView) dialog.findViewById(R.id.tvLeadSource);

            tvCapBy.setText(getSet.getLeadOwnerEmp());
            tvTitle.setText(AppUtils.toDisplayCase(getSet.getLeadName()) +"'s Details");
            tvName.setText(getSet.getLeadName());
            tvEmail.setText(getSet.getLeadEmail());
            tvMobileNo.setText(getSet.getLeadMobile());
            tvWhoGave.setText(getSet.getNameWhoGaveLead());
            tvLeadSource.setText(getSet.getLeadSource());

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}