package cheetah.alphacapital.reportApp.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLeadsDetailsBinding;
import cheetah.alphacapital.reportApp.getset.LeadsDeatilsResponseModel;
import cheetah.alphacapital.reportApp.getset.SaveLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.ToDoListResponse;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadsDetailsActivity extends BaseActivity
{
    private ActivityLeadsDetailsBinding binding;

    //paging
    int pageIndex = 1;
    boolean isLoading = false;
    boolean isLastPage = false;
    boolean isStatusBarHidden = false;
    int pageResults = 25;

    String Month , Year = "";

    private ArrayList<LeadsDeatilsResponseModel.Datum> listData = new ArrayList<>();
    private LeadsDeatilsAdapter dataAdapter;

    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_leads_details);
        handler = new Handler(msg ->
        {
            try
            {
                if (msg.what == 100)
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                        callLeadsDetailsAPI(true);
                    }
                    else
                    {
                        binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return false;
        });

        initView();
        onClick();
    }

    private void initView()
    {
        if (getIntent().hasExtra("month"))
        {
            Month = getIntent().getStringExtra("month");
        }
        if (getIntent().hasExtra("year"))
        {
            Year = getIntent().getStringExtra("year");
        }

        String Title = "";
        if (getIntent().hasExtra("month_title"))
        {
            Title = getIntent().getStringExtra("month_title");
        }

        binding.toolbar.llNotification.setVisibility(View.GONE);
        if (sessionManager.isNetworkAvailable())
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            callLeadsDetailsAPI(true);
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText(Title + " Leads");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        binding.nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
        {
            if (v.getChildAt(v.getChildCount() - 1) != null)
            {
                if (scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight() && scrollY > oldScrollY)
                {
                    Integer visibleItemCount = linearLayoutManager.getChildCount();
                    Integer totalItemCount = linearLayoutManager.getItemCount();
                    Integer firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    if (!isLoading && !isLastPage)
                    {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0)
                        {
                            if (listData.size() > 0)
                            {
                                binding.llMore.setVisibility(View.VISIBLE);
                                callLeadsDetailsAPI(false);
                            }
                        }
                    }
                }
            }
        });

    }

    private void callLeadsDetailsAPI(boolean isFirstTime)
    {
        if (isFirstTime)
        {
            binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        }
        apiService.getLeadsDetails(Year,Month,pageIndex,pageResults,sessionManager.getUserId() ).enqueue(new Callback<LeadsDeatilsResponseModel>()
        {
            @Override
            public void onResponse(Call<LeadsDeatilsResponseModel> call, Response<LeadsDeatilsResponseModel> response)
            {
                if (response.isSuccessful())
                {

                    if (isFirstTime)
                    {
                        if (listData.size() > 0)
                        {
                            listData = new ArrayList<>();
                        }
                    }
                    ArrayList<LeadsDeatilsResponseModel.Datum> tempList = new ArrayList<>();
                    if (response.body().getData() != null)
                    {
                        tempList.addAll(response.body().getData());
                        listData.addAll(tempList);
                    }

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
                        if (listData.size() > 0)
                        {
                            dataAdapter = new LeadsDeatilsAdapter(listData, activity);
                            binding.rvLeadsList.setAdapter(dataAdapter);
                            binding.llNoData.llNoData.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        dataAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<LeadsDeatilsResponseModel> call, Throwable t)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
                apiFailedSnackBar();
            }
        });
    }

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    AppUtils.hideKeyboard(binding.toolbar.emptyView, activity);
                    activity.finish();
                    //activity.overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    private class LeadsDeatilsAdapter extends RecyclerView.Adapter<LeadsDeatilsAdapter.ViewHolder>
    {
        public LeadsDeatilsAdapter(ArrayList<LeadsDeatilsResponseModel.Datum> listData, Activity activity)
        {
        }

        @NonNull
        @Override
        public LeadsDeatilsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_leads_details, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull LeadsDeatilsAdapter.ViewHolder holder, int i)
        {
            final LeadsDeatilsResponseModel.Datum getSet = listData.get(i);

            holder.tvGoldTag.setText("Client Name");
            holder.tvGold.setText(AppUtils.toDisplayCase(getSet.getClientName()));

            holder.tvATag.setText("Category");
            holder.tvFinancialPlan.setText(AppUtils.toDisplayCase(getSet.getCategory()));

            holder.tvBTag.setText("Category Id");
            holder.tvClient.setText(AppUtils.toDisplayCase(getSet.getCategory_Id()));

            holder.tvCTag.setText("Reference Date");
            holder.tvNoContact.setText(AppUtils.toDisplayCase(getSet.getReferenceDate_Format()));

            holder.tvDTag.setText("Reference From");
            holder.tvNotRightClient.setText(AppUtils.toDisplayCase(getSet.getReferenceFrom()));

            holder.tvETag.setText("Status");
            holder.tvNotInterestedClient.setText(AppUtils.toDisplayCase(getSet.getStatus()));

            holder.llLast.setVisibility(View.GONE);
            holder.tvMonth.setVisibility(View.GONE);
            if (getSet.getEmployee_id().equals(Integer.valueOf(sessionManager.getUserId())))
            {
                holder.ivDelete.setVisibility(View.VISIBLE);
            }

            holder.ivDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectDeleteDialog(getSet,i);
                }
            });

            holder.itemView.setOnClickListener(v ->
            {
                String data = new Gson().toJson(getSet);

                Intent intent =new Intent(activity,AddLeadsActivity.class);
                intent.putExtra("gson", data);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvGold, tvFinancialPlan, tvClient, tvNoContact, tvNotRightClient, tvNotInterestedClient, tvTotal, tvMonth;
            AppCompatTextView tvGoldTag, tvATag, tvBTag, tvCTag, tvDTag, tvETag, tvTotalTag;
            LinearLayout llView,llLast;
            ImageView ivDelete;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvGoldTag = itemView.findViewById(R.id.tvGoldTag);
                tvATag = itemView.findViewById(R.id.tvATag);
                tvBTag = itemView.findViewById(R.id.tvBTag);
                tvCTag = itemView.findViewById(R.id.tvCTag);
                tvDTag = itemView.findViewById(R.id.tvDTag);
                tvETag = itemView.findViewById(R.id.tvETag);
                tvTotalTag = itemView.findViewById(R.id.tvTotalTag);
                tvMonth = itemView.findViewById(R.id.tvMonth);
                tvGold = itemView.findViewById(R.id.tvGold);
                tvFinancialPlan = itemView.findViewById(R.id.tvFinancialPlan);
                tvClient = itemView.findViewById(R.id.tvClient);
                tvNoContact = itemView.findViewById(R.id.tvNoContact);
                tvNotRightClient = itemView.findViewById(R.id.tvNotRightClient);
                tvNotInterestedClient = itemView.findViewById(R.id.tvNotInterestedClient);
                tvTotal = itemView.findViewById(R.id.tvTotal);
                llView = itemView.findViewById(R.id.llView);
                llLast = itemView.findViewById(R.id.llLast);
                ivDelete = itemView.findViewById(R.id.ivDelete);
            }
        }
    }

    public void selectDeleteDialog(final LeadsDeatilsResponseModel.Datum getSet, final int pos)
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
        tvTitle.setText("Delete Lead");
        tvDescription.setText("Are you sure want to remove this Lead?");

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
                    callDeleteLeadAPI(getSet, pos);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void callDeleteLeadAPI(LeadsDeatilsResponseModel.Datum getSet, int pos)
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.deleteLeadsAPI(getSet.getId()).enqueue(new Callback<SaveLeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<SaveLeadsResponseModel> call, Response<SaveLeadsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        callLeadsDetailsAPI(true);
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SaveLeadsResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
            }
        });
    }
}