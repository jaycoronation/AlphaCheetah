package cheetah.alphacapital.reportApp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityNotificationListBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.activity.admin.ManageClientActivity;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.NotificationResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends BaseActivity
{
    private ActivityNotificationListBinding binding;
    private ArrayList<NotificationResponseModel.DataItem> notificationList = new ArrayList<>();

    //paging
    int pageIndex = 0;
    boolean isLoading = false;
    boolean isLastPage = false;
    int pageResults = 25;
    private LinearLayoutManager linearLayoutManager;

    private NotificationListAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_notification_list);
        initView();
        onClick();
    }

    private void initView()
    {
        binding.toolbar.txtTitle.setVisibility(View.VISIBLE);
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Notification List");
        binding.toolbar.tvClearNotifications.setVisibility(View.VISIBLE);
        if (sessionManager.isNetworkAvailable())
        {
            getNotificationList(true);
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            linearLayoutManager = new LinearLayoutManager(activity);
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
                                if (notificationList.size() > 0)
                                {
                                    binding.llMore.setVisibility(View.VISIBLE);
                                    getNotificationList(false);
                                }
                            }
                        }
                    }
                }
            });
        }

    }

    private void onClick()
    {
        binding.toolbar.llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                finishActivityAnimation();
            }
        });

        binding.llNoInternet.llRetry.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getNotificationList(true);
            }
        });

        binding.toolbar.tvClearNotifications.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callClearNotificationListAPI();
            }
        });
    }

    private void getNotificationList(boolean isFirstTime)
    {
        binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getNotificationList(Integer.valueOf(sessionManager.getUserId()),0,15).enqueue(new Callback<NotificationResponseModel>()
        {
            @Override
            public void onResponse(Call<NotificationResponseModel> call, Response<NotificationResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        notificationList.clear();
                        notificationList.addAll(response.body().getData());
                        binding.rvNotificationList.setAdapter(new NotificationListAdapter());
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        binding.toolbar.tvClearNotifications.setVisibility(View.VISIBLE);

                        if (isFirstTime)
                        {
                            if (notificationList.size() > 0)
                            {
                                notificationList = new ArrayList<>();
                            }
                        }
                        ArrayList<NotificationResponseModel.DataItem> tempList = new ArrayList<NotificationResponseModel.DataItem>();
                        tempList.addAll(response.body().getData());
                        notificationList.addAll(tempList);

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
                            if (notificationList.size() > 0)
                            {
                                notificationAdapter = new NotificationListAdapter();
                                binding.rvNotificationList.setAdapter(notificationAdapter);
                                binding.llNoData.llNoData.setVisibility(View.GONE);
                            }
                            else
                            {
                                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            notificationAdapter.notifyDataSetChanged();
                        }
                    }
                    else
                    {
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<NotificationResponseModel> call, Throwable t)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void callClearNotificationListAPI()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.clearNotificationList(Integer.valueOf(sessionManager.getUserId())).enqueue(new Callback<CommentResponse>()
        {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                        binding.toolbar.tvClearNotifications.setVisibility(View.GONE);
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder>
    {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_notification_list, parent, false);
            return new NotificationListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            NotificationResponseModel.DataItem getSet = notificationList.get(position);
            holder.tvNotification.setText(getSet.getNotificationMessage());
            holder.tvTimestamp.setText(AppUtils.universalDateConvert(getSet.getAddedDate().trim(),"yyyy-MM-dd'T'HH:mm:ss.SSS","dd/MM/yyyy"));

            holder.itemView.setOnClickListener(v ->
            {
                Intent intent = new Intent(activity, TaskDetailsActivityNew.class);
                intent.putExtra("task_id", getSet.getTaskId());
                startActivity(intent);
            });

        }

        @Override
        public int getItemCount()
        {
            return notificationList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvNotification, tvTimestamp;
            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvNotification = itemView.findViewById(R.id.tvNotification);
                tvTimestamp = itemView.findViewById(R.id.tvTimeSpent);
            }
        }
    }
}