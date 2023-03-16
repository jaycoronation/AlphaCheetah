package cheetah.alphacapital.reportApp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentKnowledgeSessionBinding;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.AllLearningManualResponseModel;
import cheetah.alphacapital.reportApp.getset.DeleteLearningModuleResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KnowledgeSessionFragment extends Fragment
{
    private FragmentKnowledgeSessionBinding binding;
    private String modualId = "";
    private FragmentActivity activity;
    private SessionManager sessionManager;
    public ApiInterface apiService;
    private ArrayList<AllLearningManualResponseModel.DataItem> listData = new ArrayList<>();
    private DataAdapter viewAdapter;

    public KnowledgeSessionFragment(String id)
    {
        this.modualId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_knowledge_session, container, false);
        activity = requireActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        getDataAPI();
        onClick();
        return binding.getRoot();
    }

    private void onClick()
    {
        binding.ivAddScheme.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openAddDialog(new AllLearningManualResponseModel.DataItem());
            }
        });

        if (sessionManager.isAdmin())
        {
            binding.ivAddScheme.setVisibility(View.VISIBLE);
        }
        else
        {
            binding.ivAddScheme.setVisibility(View.GONE);
        }
    }

    public void openAddDialog(AllLearningManualResponseModel.DataItem getSet)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_add_learning_manuals, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final AppCompatEditText edtTitle, edtLink;
        final TextInputLayout inputTitle, inputLink;
        final LinearLayout llSubmit;
        final TextView tvDescription;
        final TextView txtNo;
        final TextView txtYes;
        final TextView tvTitle,tvSave;
        edtLink = sheetView.findViewById(R.id.edtLink);
        inputTitle = sheetView.findViewById(R.id.inputTitle);
        edtTitle = sheetView.findViewById(R.id.edtTitle);
        inputLink = sheetView.findViewById(R.id.inputLink);
        llSubmit = sheetView.findViewById(R.id.llSubmit);
        tvSave = sheetView.findViewById(R.id.tvSave);
        tvTitle = sheetView.findViewById(R.id.tvTitle);

        if (!getSet.getId().isEmpty())
        {
            tvTitle.setText("Update Learning Manuals");
            tvSave.setText("Update");
            edtTitle.setText(getSet.getTitle());
            edtLink.setText(getSet.getLink());
        }

        llSubmit.setOnClickListener(view ->
        {
            try
            {
                if (edtTitle.getText().toString().trim().isEmpty())
                {
                    inputTitle.setError("Please Enter Title.");
                    return;
                }
                else if (edtLink.getText().toString().trim().isEmpty())
                {
                    inputLink.setError("Please Enter Link.");
                    return;
                }
                else if (!AppUtils.validateWebUrl(edtLink.getText().toString().trim()))
                {
                    inputLink.setError("Please Enter Valid Link.");
                    return;
                }

                if (getSet.getId().isEmpty())
                {
                    callAddLearningManualAPI(edtTitle.getText().toString().trim(),edtLink.getText().toString().trim());
                }
                else
                {
                    callUpdateLearningManualAPI(getSet,edtTitle.getText().toString().trim(),edtLink.getText().toString().trim());
                }
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        AppUtils.removeError(edtLink,inputLink);
        AppUtils.removeError(edtTitle,inputTitle);

        bottomSheetDialog.show();
    }

    private void callAddLearningManualAPI(String title, String Link)
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getAddLearningManualApi(modualId, Integer.valueOf(sessionManager.getUserId()),"{}",Link,title).enqueue(new Callback<DeleteLearningModuleResponseModel>()
        {
            @Override
            public void onResponse(Call<DeleteLearningModuleResponseModel> call, Response<DeleteLearningModuleResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        getDataAPI();
                    }
                    Toast.makeText(activity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DeleteLearningModuleResponseModel> call, Throwable throwable)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void callUpdateLearningManualAPI(AllLearningManualResponseModel.DataItem getSet, String title, String Link)
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getUpdateLearningMaualAPI(Integer.valueOf(modualId), Integer.valueOf(sessionManager.getUserId()),getSet.get$id(),Link,title).enqueue(new Callback<DeleteLearningModuleResponseModel>()
        {
            @Override
            public void onResponse(Call<DeleteLearningModuleResponseModel> call, Response<DeleteLearningModuleResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        getDataAPI();
                    }
                    Toast.makeText(activity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DeleteLearningModuleResponseModel> call, Throwable throwable)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getDataAPI()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getAllLearningManual(modualId).enqueue(new Callback<AllLearningManualResponseModel>()
        {
            @Override
            public void onResponse(Call<AllLearningManualResponseModel> call, Response<AllLearningManualResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().size() > 0)
                        {
                            listData.clear();
                            listData.addAll(response.body().getData());
                            viewAdapter = new DataAdapter();
                            binding.rvList.setAdapter(viewAdapter);
                        }
                    }
                    else
                    {
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllLearningManualResponseModel> call, Throwable throwable)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });

    }

    private class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder>
    {
        @NonNull
        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_learning_module, viewGroup, false);
            return new DataAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull DataAdapter.ViewHolder holder, int i)
        {
            AllLearningManualResponseModel.DataItem getSet = listData.get(i);
            holder.tvFile.setText(getSet.getTitle());

            if (sessionManager.isAdmin())
            {
                holder.ivDelete.setVisibility(View.VISIBLE);
                holder.ivEdit.setVisibility(View.VISIBLE);
                holder.view.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.ivDelete.setVisibility(View.GONE);
                holder.ivEdit.setVisibility(View.GONE);
                holder.view.setVisibility(View.GONE);
            }

            holder.tvFile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(getSet.getLink()));
                    startActivity(i);
                }
            });

            holder.ivDelete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectDeleteDialog(getSet);
                }
            });

            holder.ivEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    openAddDialog(getSet);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvFile;
            AppCompatImageView ivEdit, ivDelete;
            View view;
            public ViewHolder(View itemView)
            {
                super(itemView);
                view = itemView.findViewById(R.id.view);
                tvFile = itemView.findViewById(R.id.tvFile);
                ivDelete = itemView.findViewById(R.id.ivDelete);
                ivEdit = itemView.findViewById(R.id.ivEdit);
            }
        }
    }

    public void selectDeleteDialog(final AllLearningManualResponseModel.DataItem getSet)
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
        tvTitle.setText("Delete File");
        tvDescription.setText("Are you sure want to remove this File?");


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


        llRemove.setOnClickListener(view ->
        {
            try
            {
                bottomSheetDialog.dismiss();
                bottomSheetDialog.cancel();
                callDeleteLeadAPI(getSet);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        bottomSheetDialog.show();
    }

    private void callDeleteLeadAPI(AllLearningManualResponseModel.DataItem getSet)
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.deleteFileAPI(getSet.get$id()).enqueue(new Callback<DeleteLearningModuleResponseModel>()
        {
            @Override
            public void onResponse(Call<DeleteLearningModuleResponseModel> call, Response<DeleteLearningModuleResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        getDataAPI();
                        Toast.makeText(activity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(activity,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<DeleteLearningModuleResponseModel> call, Throwable throwable)
            {
                Toast.makeText(activity,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
}