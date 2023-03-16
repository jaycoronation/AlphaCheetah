package cheetah.alphacapital.reportApp.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentEmpNotesBinding;
import cheetah.alphacapital.databinding.RowviewNotesBinding;
import cheetah.alphacapital.reportApp.getset.AddNoteResponse;
import cheetah.alphacapital.reportApp.getset.CommonResponse;
import cheetah.alphacapital.reportApp.getset.NotesResponse;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class EmpNotesFragment extends Fragment
{
    private Activity activity;
    private SessionManager sessionManager;
    private ApiInterface apiService;
    private FragmentEmpNotesBinding binding;


    private int employeeId = 0;
    private List<NotesResponse.DataBean> listReport = new ArrayList<>();

    private ReportAdapter reportAdapter;


    @SuppressLint("ValidFragment")
    public EmpNotesFragment(int employeeId)
    {
        this.employeeId = employeeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_emp_notes, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        initView();
        getData();
        return binding.getRoot();
    }

    private void initView()
    {
        binding.noData.tvNoDataText.setText("Sorry, We could not find any Notes for you, Click + button to add your note.");
        binding.ivAddNote.setOnClickListener(view -> addNoteDialog("add", 0, new NotesResponse.DataBean()));
    }

    void getData()
    {
        if (sessionManager.isNetworkAvailable())
        {
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.getAllNotesForEmployee(String.valueOf(employeeId)).enqueue(new Callback<NotesResponse>()
            {
                @Override
                public void onResponse(Call<NotesResponse> call, Response<NotesResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        if (response.body().isSuccess())
                        {
                            binding.noData.llNoData.setVisibility(View.GONE);
                            listReport = response.body().getData();

                            if (listReport.size() > 0)
                            {
                                binding.noData.llNoData.setVisibility(View.GONE);
                                setAdapter();
                            }
                            else
                            {
                                binding.noData.llNoData.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        binding.noData.llNoData.setVisibility(View.VISIBLE);
                    }
                    binding.loading.llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<NotesResponse> call, Throwable t)
                {
                    binding.loading.llLoading.setVisibility(View.GONE);
                    binding.noData.llNoData.setVisibility(View.VISIBLE);
                    Log.e("<><><>", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    void setAdapter()
    {
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(activity));
        reportAdapter = new ReportAdapter();
        binding.rvNotes.setAdapter(reportAdapter);
    }

    public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>
    {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rowview_notes, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position)
        {
            NotesResponse.DataBean bean = listReport.get(position);
            holder.binding.tvNote.setText(bean.getTitle());
            holder.binding.tvDateTime.setText(AppUtils.universalDateConvert(bean.getCreated_date().replace("T", " "), "yyyy-MM-dd hh:mm:ss.SSS", "MMM dd, yyyy HH:mm:aa"));
            holder.binding.ivEdit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    showOptionDialog(position, bean);
                }
            });

        }

        @Override
        public int getItemCount()
        {
            return listReport.size();
        }

        @SuppressWarnings("unused")
        public class ViewHolder extends RecyclerView.ViewHolder
        {
            RowviewNotesBinding binding;

            ViewHolder(View convertView)
            {
                super(convertView);
                binding = DataBindingUtil.bind(convertView);
            }
        }
    }

    public void showOptionDialog(int position, NotesResponse.DataBean bean)
    {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.BaseBottomSheetDialog);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_option_edit_delete, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);

        TextView tvEdit, tvDelete;
        tvEdit = sheetView.findViewById(R.id.tvEdit);
        tvDelete = sheetView.findViewById(R.id.tvDelete);

        tvEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                bottomSheetDialog.dismiss();
                addNoteDialog("edit", position, bean);
            }
        });

        tvDelete.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            showDeleteDialog(position);
        });

        bottomSheetDialog.show();

    }

    void showDeleteDialog(int position)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_logout, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final TextView txtMsg, tvTitle, tvLogout;
        final LinearLayout llCancel;
        final LinearLayout llLogout;

        tvLogout = sheetView.findViewById(R.id.tvLogout);
        tvLogout.setText("Yes");
        tvTitle = sheetView.findViewById(R.id.tvTitle);
        tvTitle.setText("Delete");
        txtMsg = sheetView.findViewById(R.id.txtMsg);
        txtMsg.setText("Are you sure you want to delete this note?");
        llCancel = sheetView.findViewById(R.id.llCancel);
        llLogout = sheetView.findViewById(R.id.llLogout);


        llCancel.setOnClickListener(view -> bottomSheetDialog.dismiss());

        llLogout.setOnClickListener(view ->
        {
            bottomSheetDialog.dismiss();
            binding.loading.llLoading.setVisibility(View.VISIBLE);
            apiService.deleteNote(String.valueOf(listReport.get(position).getId())).enqueue(new Callback<CommonResponse>()
            {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response)
                {
                    if (response.isSuccessful())
                    {
                        listReport.remove(position);

                        if (listReport.size() == 0)
                        {
                            binding.noData.llNoData.setVisibility(View.VISIBLE);
                        }

                        if (reportAdapter != null)
                        {
                            reportAdapter.notifyDataSetChanged();
                        }
                    }
                    binding.loading.llLoading.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t)
                {
                    binding.loading.llLoading.setVisibility(View.GONE);
                }
            });


        });


        bottomSheetDialog.show();
    }

    void addNoteDialog(String isFor, int position, NotesResponse.DataBean bean)
    {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_notes, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);

        final EditText edtNote;
        final ImageView ivAddNotes;

        edtNote = sheetView.findViewById(R.id.edtNote);

        if (isFor.equals("edit"))
        {
            Log.e("<><> NOTE ID : ", String.valueOf(bean.getId() + " ++++"));
            edtNote.setText(bean.getTitle());
        }

        ivAddNotes = sheetView.findViewById(R.id.ivAddNotes);

        edtNote.requestFocus();

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                //MitsUtils.hideKeyboard(activity);
            }
        });

        ivAddNotes.setOnClickListener(view ->
        {

            AppUtils.hideKeyboard(edtNote, activity);
            bottomSheetDialog.dismiss();

            if (edtNote.getText().toString().trim().equals(""))
            {
                AppUtils.showToast(activity, "Please enter your note.");
            }
            else
            {
                if (sessionManager.isNetworkAvailable())
                {
                    binding.loading.llLoading.setVisibility(View.VISIBLE);
                    Call<AddNoteResponse> call;
                    if (isFor.equals("add"))
                    {
                        call = apiService.addNote("0", "", sessionManager.getUserId(), String.valueOf(employeeId), edtNote.getText().toString().trim());
                    }
                    else
                    {
                        call = apiService.updateNote(String.valueOf(bean.getId()), String.valueOf(bean.getClient_id()), "", sessionManager.getUserId(), String.valueOf(employeeId), edtNote.getText().toString().trim());
                    }

                    call.enqueue(new Callback<AddNoteResponse>()
                    {
                        @Override
                        public void onResponse(Call<AddNoteResponse> call, Response<AddNoteResponse> response)
                        {
                            if (response.isSuccessful())
                            {
                                if (response.body().isSuccess())
                                {
                                    if (response.body().getMessage() != null)
                                    {
                                        AppUtils.showToast(activity, response.body().getMessage());

                                        if (isFor.equals("add"))
                                        {
                                            NotesResponse.DataBean bean = new NotesResponse.DataBean();
                                            bean.setId(response.body().getData().getId());
                                            bean.setClient_id(response.body().getData().getClient_id());
                                            bean.setCreated_date(response.body().getData().getCreated_date());
                                            bean.setTitle(response.body().getData().getTitle());
                                            bean.setDescription(response.body().getData().getDescription());
                                            bean.setEmployee_id(response.body().getData().getEmployee_id());
                                            bean.setEmployee_id_assign(response.body().getData().getEmployee_id_assign());
                                            bean.setUpdated_date(response.body().getData().getUpdated_date());
                                            listReport.add(bean);
                                        }
                                        else
                                        {
                                            listReport.get(position).setTitle(response.body().getData().getTitle());
                                        }

                                        binding.noData.llNoData.setVisibility(View.GONE);

                                        if (reportAdapter != null)
                                        {
                                            reportAdapter.notifyDataSetChanged();
                                        }
                                        else
                                        {
                                            setAdapter();
                                        }
                                    }
                                }
                            }

                            binding.loading.llLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<AddNoteResponse> call, Throwable t)
                        {
                            binding.loading.llLoading.setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    AppUtils.showToast(activity, activity.getString(R.string.network_failed_message));
                }
            }


        });

        bottomSheetDialog.show();
    }
}
