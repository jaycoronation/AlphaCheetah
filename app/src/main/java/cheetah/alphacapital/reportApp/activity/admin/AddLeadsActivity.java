package cheetah.alphacapital.reportApp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityAddLeadsBinding;
import cheetah.alphacapital.reportApp.activity.AssignedClientListActivity;
import cheetah.alphacapital.reportApp.getset.CategoryModel;
import cheetah.alphacapital.reportApp.getset.LeadsDeatilsResponseModel;
import cheetah.alphacapital.reportApp.getset.MonthYearResponse;
import cheetah.alphacapital.reportApp.getset.SaveLeadsResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLeadsActivity extends BaseActivity
{
    private ActivityAddLeadsBinding binding;
    private boolean isStatusBarHidden = false;
    private Integer YEAR, MONTH = 0;
    private ArrayList<CategoryModel> categoryArray = new ArrayList<>();
    private BottomSheetDialog listDialog;
    DialogListAdapter dialogListAdapter;
    private String selectedCategory, selectedCategoryId, currentDateTime, leadId, id = "";
    private Integer leadidInt = 0;
    private String createdDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_add_leads);
        initView();
        onClick();
    }

    private void initView()
    {
        setSupportActionBar(binding.toolbar.toolbar);

        binding.toolbar.llNotification.setVisibility(View.GONE);

        Calendar calendar = Calendar.getInstance();
        YEAR = calendar.get(Calendar.YEAR);
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        MONTH = Integer.valueOf(dateFormat.format(date));

        binding.toolbar.ivLogo.setVisibility(View.GONE);
        categoryArray.add(new CategoryModel());
        categoryArray.get(0).setCategory_Id("gold");
        categoryArray.get(0).setCategory("converted");
        categoryArray.add(new CategoryModel());
        categoryArray.get(1).setCategory_Id("a");
        categoryArray.get(1).setCategory("financial plan presented");
        categoryArray.add(new CategoryModel());
        categoryArray.get(2).setCategory_Id("b");
        categoryArray.get(2).setCategory("client interested");
        categoryArray.add(new CategoryModel());
        categoryArray.get(3).setCategory_Id("c");
        categoryArray.get(3).setCategory("no contact yet");
        categoryArray.add(new CategoryModel());
        categoryArray.get(4).setCategory_Id("d");
        categoryArray.get(4).setCategory("not right client");
        categoryArray.add(new CategoryModel());
        categoryArray.get(5).setCategory_Id("e");
        categoryArray.get(5).setCategory("client not interested");

        if (getIntent().hasExtra("gson"))
        {
            String data = getIntent().getStringExtra("gson");
            LeadsDeatilsResponseModel.Datum getSet = new Gson().fromJson(data, LeadsDeatilsResponseModel.Datum.class);
            leadId = getSet.get$id();
            id = String.valueOf(getSet.getId());
            leadidInt = getSet.getId();
            createdDate = getSet.getCreated_date();
            selectedCategory = getSet.getCategory();
            selectedCategoryId = getSet.getCategory_Id();
            currentDateTime = AppUtils.universalDateConvert(getSet.getReferenceDate(),"yyyy-MM-dd'T'hh:m:ss","dd/MM/yyyy hh:mm aa");
            Log.e("<><>currentDateTime",currentDateTime);
            binding.edtClientName.setText(getSet.getClientName());
            binding.edtCategoryId.setText(AppUtils.toDisplayCase(getSet.getCategory_Id() + " " + getSet.getCategory()));
            binding.edtReferenceFrom.setText(getSet.getReferenceFrom());
            binding.edtStatus.setText(getSet.getStatus());
            binding.toolbar.txtTitle.setText("Update Leads");
        }
        else
        {
            id = "";
            leadId = "";
            leadidInt = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
            currentDateTime = sdf.format(new Date());
            binding.toolbar.txtTitle.setText("Add Leads");
        }
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

        binding.llSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkValidation())
                {
                    if (sessionManager.isNetworkAvailable())
                    {
                        if (leadId.length() > 0)
                        {
                            callUpdateLeadsAPI();

                        }
                        else
                        {
                            callSaveLeadsAPI();
                        }

                    }
                    else
                    {
                        Toast.makeText(activity, getResources().getString(R.string.network_failed_message), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.edtCategoryId.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showListDialog();
            }
        });
    }

    private void callUpdateLeadsAPI()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getUpdateLead(MONTH, YEAR, selectedCategory, selectedCategoryId, binding.edtClientName.getText().toString().trim(), currentDateTime, binding.edtReferenceFrom.getText().toString().trim(), binding.edtStatus.getText().toString().trim(), Integer.valueOf(sessionManager.getUserId()), leadId, id, leadidInt, createdDate).enqueue(new Callback<SaveLeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<SaveLeadsResponseModel> call, Response<SaveLeadsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        showToast(response.body().getMessage());
                        finish();
                        if (LeadTrackerActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            LeadTrackerActivity.handler.sendMessage(message);
                        }
                        if (LeadsDetailsActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            LeadsDetailsActivity.handler.sendMessage(message);
                        }
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SaveLeadsResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void callSaveLeadsAPI()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getSaveLead(MONTH, YEAR, selectedCategory, selectedCategoryId, binding.edtClientName.getText().toString().trim(), currentDateTime, binding.edtReferenceFrom.getText().toString().trim(), binding.edtStatus.getText().toString().trim(), Integer.valueOf(sessionManager.getUserId()), leadId, id, leadidInt, createdDate).enqueue(new Callback<SaveLeadsResponseModel>()
        {
            @Override
            public void onResponse(Call<SaveLeadsResponseModel> call, Response<SaveLeadsResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        showToast(response.body().getMessage());
                        finish();
                        if (LeadTrackerActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            LeadTrackerActivity.handler.sendMessage(message);
                        }
                        if (LeadsDetailsActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 100;
                            LeadTrackerActivity.handler.sendMessage(message);
                        }
                    }
                    else
                    {
                        showToast(response.body().getMessage());
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SaveLeadsResponseModel> call, Throwable throwable)
            {
                apiFailedSnackBar();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkValidation()
    {
        boolean isValid = true;
        if (binding.edtClientName.getText().toString().trim().length() == 0)
        {
            binding.inputClientName.setError("Please Enter Client Name.");
            isValid = false;
        }
        else if (binding.edtCategoryId.getText().toString().length() == 0)
        {
            binding.inputCategoryId.setError("Please Enter Category Id.");
            isValid = false;
        }
        else if (binding.edtReferenceFrom.getText().toString().length() == 0)
        {
            binding.inputReferenceFrom.setError("Please Select isActive.");
            isValid = false;
        }
        else if (binding.edtStatus.getText().toString().trim().length() == 0)
        {
            binding.inputStatus.setError("Please Select Status.");
            isValid = false;
        }

        AppUtils.removeError(binding.edtClientName, binding.inputClientName);
        AppUtils.removeError(binding.edtCategoryId, binding.inputCategoryId);
        AppUtils.removeError(binding.edtReferenceFrom, binding.inputReferenceFrom);
        AppUtils.removeError(binding.edtStatus, binding.inputStatus);
        return isValid;
    }

    public void showListDialog()
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
        TextView tvTitle = listDialog.findViewById(R.id.tvTitle);
        tvTitle.setText("Select Category");
        TextView tvDone = listDialog.findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = listDialog.findViewById(R.id.rvDialog);

        dialogListAdapter = new DialogListAdapter();
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(dialogListAdapter);

        btnNo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listDialog.dismiss();
                listDialog.cancel();
            }
        });
        listDialog.show();
    }

    private class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.ViewHolder>
    {
        DialogListAdapter()
        {

        }

        @Override
        public DialogListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_common_list, parent, false);
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
            final CategoryModel getSet = categoryArray.get(position);
            holder.tvValue.setText(AppUtils.toDisplayCase(getSet.getCategory_Id() + " " + getSet.getCategory()));
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    selectedCategoryId = AppUtils.toDisplayCase(getSet.getCategory_Id());
                    selectedCategory = AppUtils.toDisplayCase(getSet.getCategory());
                    binding.edtCategoryId.setText(AppUtils.toDisplayCase(getSet.getCategory_Id() + " " + getSet.getCategory()));
                    listDialog.dismiss();
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return categoryArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            private final TextView tvValue;
            private final View viewLine;

            public ViewHolder(View itemView)
            {
                super(itemView);
                tvValue = itemView.findViewById(R.id.tvValue);
                viewLine = itemView.findViewById(R.id.viewLine);
            }
        }
    }
}