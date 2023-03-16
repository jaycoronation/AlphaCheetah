package cheetah.alphacapital.reportApp.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mit.mitspermissions.MitsPermissions;
import com.android.mit.mitspermissions.PermissionListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.FragmentClientDetailsDoumentBinding;
import cheetah.alphacapital.network.ApiClient;
import cheetah.alphacapital.network.ApiInterface;
import cheetah.alphacapital.reportApp.getset.CommentResponse;
import cheetah.alphacapital.reportApp.getset.GetAllDocumentResponseModel;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.utils.SessionManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ClientDetailsDocumentFragment extends Fragment
{
    FragmentClientDetailsDoumentBinding binding;
    private Activity activity;
    private SessionManager sessionManager;
    public ApiInterface apiService;
    String clientId = "";
    String SELECTED_FILE_PATH = "";
    List<GetAllDocumentResponseModel.Document> listDocument = new ArrayList<GetAllDocumentResponseModel.Document>();

    public ClientDetailsDocumentFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_client_details_doument, container, false);
        activity = getActivity();
        sessionManager = new SessionManager(activity);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        clientId = getArguments().getString("clientid");

        onClick();

        if (sessionManager.isNetworkAvailable())
        {
            getAllDocumentAPI();
        }
        return binding.getRoot();
    }

    private void onClick()
    {
        binding.ivAddDoc.setOnClickListener(v ->
        {
            openAddDialog();
        });
    }

    TextView tvFileName;
    BottomSheetDialog bottomSheetDialog;

    public void openAddDialog()
    {
        bottomSheetDialog = new BottomSheetDialog(activity, R.style.MaterialDialogSheetTemp);
        bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View sheetView = activity.getLayoutInflater().inflate(R.layout.dialog_add_document, null);
        bottomSheetDialog.setContentView(sheetView);
        AppUtils.setLightStatusBarBottomDialog(bottomSheetDialog, activity);
        final AppCompatEditText edtDocName;
        final TextInputLayout inputDocName;
        final LinearLayout llSubmit;
        final TextView tvSelectFile;
        tvFileName = sheetView.findViewById(R.id.tvFileName);
        tvSelectFile = sheetView.findViewById(R.id.tvSelectFile);
        edtDocName = sheetView.findViewById(R.id.edtDocName);
        inputDocName = sheetView.findViewById(R.id.inputDocName);
        llSubmit = sheetView.findViewById(R.id.llSubmit);

        tvSelectFile.setOnClickListener(v ->
        {
            PermissionListener permissionlistener = new PermissionListener()
            {
                @Override
                public void onPermissionGranted()
                {
                    Intent intent = new Intent(activity, FilePickerActivity.class);
                    intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                            .setCheckPermission(true)
                            .setShowImages(true)
                            .enableImageCapture(true)
                            .setMaxSelection(1)
                            .setSkipZeroSizeFiles(true)
                            .build());
                    someActivityResultLauncher.launch(intent);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions)
                {
                    Toast.makeText(activity, "Permission Denied In checkPermission ", Toast.LENGTH_SHORT).show();
                }
            };
            new MitsPermissions(activity).setPermissionListener(permissionlistener).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).check();

        });

        llSubmit.setOnClickListener(view ->
        {
            try
            {
                if (edtDocName.getText().toString().trim().isEmpty())
                {
                    inputDocName.setError("Please Enter Document Name.");
                    return;
                }
                addDocumentAPI(edtDocName.getText().toString());
                bottomSheetDialog.dismiss();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        AppUtils.removeError(edtDocName, inputDocName);

        bottomSheetDialog.show();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result ->
            {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    ArrayList<MediaFile> files = result.getData().getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
                    Log.e("<><><PATH",files.get(0).getPath());
                    SELECTED_FILE_PATH = files.get(0).getPath();

                    tvFileName.setText(getFileName(files.get(0).getPath()));
                }
            });

    public static String getFileName(String path) {

        String[] components = path.split("\\\\|/"); //$NON-NLS-1$
        return components[components.length - 1];
    }
    private static final String FILE_PATH_PATTERN = "(file://)?[a-zA-Z]:[/\\\\]([^/\\\\]+[/\\\\]?)+";

    public static boolean isLikelyFilePath(String s) {
        if (s == null || s.trim().length() == 0) {
            return false;
        }

        return Pattern.matches(FILE_PATH_PATTERN, s);
    }

    private void addDocumentAPI(String title)
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        File file = new File(SELECTED_FILE_PATH);
        MultipartBody.Part apiFile = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("*/*"), file));

        MultipartBody.Part apiClientId = MultipartBody.Part.createFormData("client_id", clientId);
        MultipartBody.Part employee_id = MultipartBody.Part.createFormData("employee_id", sessionManager.getUserId());
        MultipartBody.Part employee_id_assign = MultipartBody.Part.createFormData("employee_id_assign", sessionManager.getUserId());
        MultipartBody.Part apiTitle = MultipartBody.Part.createFormData("title", title);
        MultipartBody.Part apifromApp = MultipartBody.Part.createFormData("from_app", "true");

        apiService.addDocument(apiClientId, employee_id, employee_id_assign, apiTitle, apiFile, apifromApp).enqueue(new Callback<CommentResponse>()
        {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        bottomSheetDialog.dismiss();
                        AppUtils.showToast(activity, response.body().getMessage());
                        getAllDocumentAPI();
                    }
                    else
                    {
                        bottomSheetDialog.dismiss();
                        AppUtils.showToast(activity, response.body().getMessage());

                    }
                }

            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t)
            {
                t.printStackTrace();
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }
        });
    }

    private void getAllDocumentAPI()
    {
        apiService.getAllDocument(Integer.parseInt(clientId), 0).enqueue(new Callback<GetAllDocumentResponseModel>()
        {
            @Override
            public void onResponse(Call<GetAllDocumentResponseModel> call, Response<GetAllDocumentResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess())
                    {
                        if (response.body().getData().size() > 0)
                        {
                            listDocument.addAll(response.body().getData());
                            binding.rvGetAllDocuments.setAdapter(new DocumentAdapter());
                        }
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        binding.llNoData.llNoData.setVisibility(View.GONE);
                    }
                    else
                    {
                        binding.llLoading.llLoading.setVisibility(View.GONE);
                        binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetAllDocumentResponseModel> call, Throwable t)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
                binding.llNoData.llNoData.setVisibility(View.VISIBLE);
                AppUtils.apiFailedSnackBar(activity);
            }
        });
    }

    public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder>
    {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_document, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position)
        {
            GetAllDocumentResponseModel.Document getSet = listDocument.get(position);
            holder.tvCount.setText(String.valueOf(position + 1));
            holder.tvName.setText(getSet.getTitle());
            holder.tvDate.setText("On " + AppUtils.universalDateConvert(getSet.getCreatedDate(), "yyyy-MM-dd'T'HH:mm:ss", "MMM dd, yyyy HH:mm a"));

            holder.itemView.setOnClickListener(v ->
            {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getSet.getLink()));
                startActivity(browserIntent);
            });
        }

        @Override
        public int getItemCount()
        {
            return listDocument.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            AppCompatTextView tvCount;
            AppCompatTextView tvName;
            AppCompatTextView tvDate;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tvCount = itemView.findViewById(R.id.tvCount);
                tvName = itemView.findViewById(R.id.tvName);
                tvDate = itemView.findViewById(R.id.tvDate);
            }
        }
    }
}