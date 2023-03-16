package cheetah.alphacapital.reportApp.activity;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityAddCaptureLeadsBinding;
import cheetah.alphacapital.reportApp.activity.admin.BaseActivity;
import cheetah.alphacapital.reportApp.getset.AddCapturedLeadResponseModel;
import cheetah.alphacapital.reportApp.getset.AllEmployeeResponse;
import cheetah.alphacapital.reportApp.getset.CapturedLeadsResponseModel;
import cheetah.alphacapital.reportApp.getset.CategoryModel;
import cheetah.alphacapital.reportApp.getset.ClientListResponse;
import cheetah.alphacapital.textutils.CustomAppEditText;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCaptureLeadsActivity extends BaseActivity {

    private ActivityAddCaptureLeadsBinding binding;
    private ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listEmployee = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();
    private CommonListAdapter commonListAdapter;
    private String empTypeName = "",empTypeId = "";
    private String rmTypeName = "",rmTypeId = "", leadId = "";
    private ArrayList<CategoryModel> categoryArray = new ArrayList<>();
    private BottomSheetDialog listDialog;
    DialogListAdapter dialogListAdapter;
    private String EMPLOYEE = "employee";
    private String RM = "rm";
    int mYear;
    int mMonth;
    int mDay;
    String date_time = "";

    private CapturedLeadsResponseModel.LeadsData getSet = new CapturedLeadsResponseModel.LeadsData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_add_capture_leads);
        initView();
        onClick();
    }

    private void initView() {
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Add Lead");
        binding.edtLeadBy.setText(sessionManager.getUserName());
        empTypeId = sessionManager.getUserId();

        if (sessionManager.isNetworkAvailable())
        {
            getAllEmployee();
            binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
        }
        else
        {
            binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra("data"))
        {
            getSet = new Gson().fromJson(getIntent().getStringExtra("data"), CapturedLeadsResponseModel.LeadsData.class);

            binding.edtAssignRM.setText(getSet.getLeadRMEmp());
            binding.edtLeadBy.setText(getSet.getLeadOwnerEmp());
            binding.edtLeadName.setText(getSet.getLeadName());
            binding.edtLeadEmail.setText(getSet.getLeadEmail());
            binding.edtLeadMobile.setText(getSet.getLeadMobile());
            binding.edtWhoGave.setText(getSet.getNameWhoGaveLead());
            binding.edtLeadSource.setText(getSet.getLeadSource());
            binding.edtLeadDate.setText(AppUtils.universalDateConvert(getSet.getActualDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd MMM yyyy"));
            empTypeId = getSet.getEmployeeId().toString();
            rmTypeId = getSet.getRmEmployeeId().toString();
            leadId = getSet.getId().toString();

            binding.llDate.setVisibility(View.VISIBLE);
        }


        categoryArray.add(new CategoryModel());
        categoryArray.get(0).setCategory("Existing Client");
        categoryArray.add(new CategoryModel());
        categoryArray.get(1).setCategory("Social Media");
        categoryArray.add(new CategoryModel());
        categoryArray.get(2).setCategory("Alpha Colleague");
        categoryArray.add(new CategoryModel());
        categoryArray.get(3).setCategory("Friend");
        categoryArray.add(new CategoryModel());
        categoryArray.get(4).setCategory("Direct Office Visit");
        categoryArray.add(new CategoryModel());
        categoryArray.get(5).setCategory("Direct Call");
        categoryArray.add(new CategoryModel());
        categoryArray.get(6).setCategory("Direct Mail");
        categoryArray.add(new CategoryModel());
        categoryArray.get(7).setCategory("Other");
    }

    private void onClick() {
        binding.toolbar.llBackNavigation.setOnClickListener(view -> finish());

        binding.llNoInternet.llRetry.setOnClickListener(view -> {
            if (sessionManager.isNetworkAvailable())
            {
                getAllEmployee();
                binding.llNoInternet.llNoInternet.setVisibility(View.GONE);
            }
            else
            {
                binding.llNoInternet.llNoInternet.setVisibility(View.VISIBLE);
            }
        });

        binding.edtLeadBy.setOnClickListener(view -> showDialog(EMPLOYEE));

        binding.edtAssignRM.setOnClickListener(view -> showDialog(RM));

        binding.edtLeadSource.setOnClickListener(view -> showListDialog());

        binding.edtLeadDate.setOnClickListener(view -> datePicker(binding.edtLeadDate));

        binding.llSubmit.setOnClickListener(view -> {
            if (binding.edtLeadName.getText().toString().trim().isEmpty())
            {
                showToast("Please enter lead name");
            }
            else if (binding.edtLeadEmail.getText().toString().trim().isEmpty())
            {
                showToast("Please enter lead email");
            }
            else if (binding.edtLeadMobile.getText().toString().trim().isEmpty())
            {
                showToast("Please enter lead mobile no.");
            }
            else if (binding.edtWhoGave.getText().toString().trim().isEmpty())
            {
                showToast("Please enter who gave the lead to you");
            }
            else if (binding.edtLeadSource.getText().toString().trim().isEmpty())
            {
                showToast("Please enter lead source");
            }
            else
            {
                callSaveLeadAPI();
            }
        });
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

    private void callSaveLeadAPI() {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.addLeads(
                Integer.parseInt(leadId),
                Integer.parseInt(empTypeId),
                Integer.parseInt(rmTypeId),
                binding.edtLeadEmail.getText().toString().trim(),
                binding.edtLeadMobile.getText().toString().trim(),
                binding.edtLeadName.getText().toString().trim(),
                binding.edtLeadSource.getText().toString().trim(),
                binding.edtWhoGave.getText().toString().trim(),
                AppUtils.universalDateConvert(binding.edtLeadDate.getText().toString(),"dd MMM yyyy", "dd/MM/yyyy hh:mm a")
                ).enqueue(new Callback<AddCapturedLeadResponseModel>() {
            @Override
            public void onResponse(Call<AddCapturedLeadResponseModel> call, Response<AddCapturedLeadResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        if (LeadsActivity.handler != null)
                        {
                            Message message = Message.obtain();
                            message.what = 1;
                            LeadsActivity.handler.sendMessage(message);
                        }
                        showSnackBar(response.body().getMessage());
                        finish();
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
            public void onFailure(Call<AddCapturedLeadResponseModel> call, Throwable t) {
                AppUtils.apiFailedSnackBar(activity);
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getAllEmployee()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        Call<AllEmployeeResponse> call = apiService.getAllEmployee("", "0", "0");
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
                            listEmployee = new ArrayList<>();
                            listEmployee.addAll(response.body().getData().getAllEmployee());

                            for (int i = 0; i < listEmployee.size(); i++)
                            {
                                listEmployee.get(i).setFullname(listEmployee.get(i).getFirst_name() + " " + listEmployee.get(i).getLast_name());
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
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<AllEmployeeResponse> call, Throwable t)
            {
                AppUtils.apiFailedSnackBar(activity);
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void showDialog(String isForm)
    {
        try
        {
            final Dialog dialog = new Dialog(activity, R.style.MaterialDialogSheetTemp);
            dialog.setContentView(R.layout.dialog_chooser);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.TOP);
            dialog.setCanceledOnTouchOutside(true);

            AppUtils.setLightStatusBarDialog(dialog, activity);
            final RecyclerView rvList = (RecyclerView) dialog.findViewById(R.id.rvList);
            final EditText edtSearch = (EditText) dialog.findViewById(R.id.edtSearch);
            final TextView txtTitle = (TextView) dialog.findViewById(R.id.txtTitle);
            final ImageView ivBack = (ImageView) dialog.findViewById(R.id.ivBack);
            rvList.setLayoutManager(new LinearLayoutManager(activity));

            AppUtils.showKeyboard(edtSearch, activity);

            ivBack.setOnClickListener(v -> {
                AppUtils.hideKeyboard(edtSearch, activity);
                try
                {
                    dialog.dismiss();
                    dialog.cancel();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            });

            rvList.setOnTouchListener((v, event) -> {
                AppUtils.hideKeyboard(edtSearch, activity);
                return false;
            });

            AppendList(listEmployee, rvList, dialog, isForm);

            edtSearch.addTextChangedListener(new TextWatcher()
            {
                @SuppressLint("DefaultLocale")
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3)
                {
                    int textlength = edtSearch.getText().length();

                    ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list_search = new ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean>();

                    for (int i = 0; i < listEmployee.size(); i++)
                    {
                        if (textlength <= listEmployee.get(i).getFullname().length())
                        {
                            if (listEmployee.get(i).getFullname().toLowerCase(Locale.getDefault()).contains(edtSearch.getText().toString().toLowerCase().trim()))
                            {
                                list_search.add(listEmployee.get(i));
                            }
                        }
                    }

                    commonListAdapter = new CommonListAdapter(list_search, dialog, isForm);
                    rvList.setAdapter(commonListAdapter);

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
                {
                }

                @Override
                public void afterTextChanged(Editable arg0)
                {
                }
            });

            dialog.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public class CommonListAdapter extends RecyclerView.Adapter<CommonListAdapter.ViewHolder>
    {
        ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listItems;
        private Dialog dialog;
        private String isForm;

        public CommonListAdapter(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> list, Dialog dialog, String isForm)
        {
            this.listItems = list;
            this.dialog = dialog;
            this.isForm = isForm;
        }

        @Override
        public CommonListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_country_state_city, viewGroup, false);
            return new CommonListAdapter.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final CommonListAdapter.ViewHolder holder, final int position)
        {
            try
            {
                final AllEmployeeResponse.DataBean.AllEmployeeBean getSet = listItems.get(position);
                if (position == (listItems.size() - 1))
                {
                    holder.viewLine.setVisibility(View.INVISIBLE);
                }
                else
                {
                    holder.viewLine.setVisibility(View.VISIBLE);
                }

                holder.txtName.setText(getSet.getFullname());

                holder.itemView.setOnClickListener(v -> {
                    AppUtils.hideKeyboard(binding.edtLeadBy, activity);
                    try
                    {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    if (isForm.equalsIgnoreCase(EMPLOYEE))
                    {
                        empTypeId = String.valueOf(getSet.getId());
                        empTypeName = getSet.getFullname();
                        binding.edtLeadBy.setText(empTypeName);
                    }
                    else
                    {
                        rmTypeId = String.valueOf(getSet.getId());
                        rmTypeName = getSet.getFullname();
                        binding.edtAssignRM.setText(rmTypeName);
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
            private TextView txtName;
            private View viewLine;

            ViewHolder(View convertView)
            {
                super(convertView);
                txtName = (TextView) convertView.findViewById(R.id.txtName);
                viewLine = (View) convertView.findViewById(R.id.viewLine);
            }
        }
    }

    private void AppendList(ArrayList<AllEmployeeResponse.DataBean.AllEmployeeBean> listItem, RecyclerView recyclerView, Dialog dialog, String isFrom)
    {
        commonListAdapter = new CommonListAdapter(listItem, dialog, isFrom);
        recyclerView.setAdapter(commonListAdapter);
        commonListAdapter.notifyDataSetChanged();
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
        tvTitle.setText("Select Source");
        TextView tvDone = listDialog.findViewById(R.id.tvDone);
        tvDone.setVisibility(View.GONE);

        final RecyclerView rvListDialog = listDialog.findViewById(R.id.rvDialog);

        dialogListAdapter = new DialogListAdapter();
        rvListDialog.setLayoutManager(new LinearLayoutManager(activity));
        rvListDialog.setAdapter(dialogListAdapter);

        btnNo.setOnClickListener(v -> {
            listDialog.dismiss();
            listDialog.cancel();
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
            holder.tvValue.setText(AppUtils.toDisplayCase( getSet.getCategory()));
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    binding.edtLeadSource.setText(AppUtils.toDisplayCase(getSet.getCategory()));
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