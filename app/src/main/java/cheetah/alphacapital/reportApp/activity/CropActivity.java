package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cheetah.alphacapital.classes.CropImageView;
import cheetah.alphacapital.classes.ImagePath;
import cheetah.alphacapital.databinding.ActivityCropBinding;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;

public class  CropActivity extends AppCompatActivity
{
    private ActivityCropBinding binding;
    private Activity activity;
    private Toolbar toolbar;
    private CropImageView mCropView;
    private ImageView result_image;
    private Button btnCrop;
    private String imagePath = "";
    private boolean isForSave = false;
    String IMAGE_DIRECTORY_NAME = "/Android/data/cheetah.alphacapital/files/Pictures";
    private Bitmap cropped = null;
    private int outputSize = 200;
    private LinearLayout llMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activity = this;

        imagePath = AppUtils.getValidAPIStringResponse(getIntent().getStringExtra("imagePath"));
        outputSize = getIntent().getIntExtra("outputSize", 200);

        outputSize = 800;

        binding = DataBindingUtil.setContentView(activity,R.layout.activity_crop);

        // compress

        File newFilePath = ImagePath.compressImage(activity,imagePath);

        //File file = new File(imagePath);

        initView();

        initToolbar();

        onclickEvents();

        if(newFilePath == null || !newFilePath.exists())
        {
            Toast.makeText(activity, "File does not exists!", Toast.LENGTH_SHORT).show();
            finish();
            //activity.overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        }
        else
        {
            setCropImage(imagePath);
        }
    }

    private void setCropImage(final String path)
    {
        try
        {
            mCropView.setCropMode(CropImageView.CropMode.RATIO_FREE);
            mCropView.setImageBitmap(AppUtils.rotateBitmap(path));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initView()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.llNotification.setVisibility(View.GONE);
        //ivBack.setVisibility(View.GONE);

        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        result_image = (ImageView) findViewById(R.id.result_image);

        btnCrop = (Button) findViewById(R.id.btnCrop);
    }

    private void initToolbar()
    {
        try
        {
            TextView txtTitle = (TextView) findViewById(R.id.txtTitle);
            LinearLayout llBack = (LinearLayout) findViewById(R.id.llBackNavigation);
            setSupportActionBar(toolbar);
            txtTitle.setText("Crop Image");
            llBack.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        finish();
                        AppUtils.finishActivityAnimation(activity);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void onclickEvents()
    {
        btnCrop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!isForSave)
                {
                    isForSave = true;

                    cropped = mCropView.getCroppedBitmap();
                    mCropView.setVisibility(View.GONE);
                    /*btnCrop.setText("DONE");*/
                    result_image.setVisibility(View.VISIBLE);
                    result_image.setImageBitmap(cropped);
                    cropAsync();
                }
				/*else
				{
					cropAsync();
				}*/
            }
        });
    }

    private void cropAsync()
    {
        (new AsyncTask<Void, Void, Void>()
        {
            //			private ProgressDialog pd;
            private String path = "";

            @Override
            protected void onPreExecute()
            {
                try
                {
					/*pd = new ProgressDialog(activity);
	                pd.show();*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                try
                {
                    File tostoreFile = null;
                    FileOutputStream out = null;
                    try
                    {
                        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY_NAME);
                        if (!dirtostoreFile.exists())
                        {
                            dirtostoreFile.mkdirs();
                        }
                        String timestr = String.valueOf(Calendar.getInstance().getTimeInMillis());
                        tostoreFile= new File(Environment.getExternalStorageDirectory()+ IMAGE_DIRECTORY_NAME + "/IMG_" + timestr+ ".jpg");

                        out = new FileOutputStream(tostoreFile.getAbsolutePath());
                        cropped.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    } finally
                    {
                        try
                        {
                            if (out != null)
                            {
                                out.close();
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    File file = ImagePath.cropAndCompressImage(activity, tostoreFile.getAbsolutePath(), outputSize, (float) outputSize);

                    if (file != null && file.exists())
                    {
                        path = file.getAbsolutePath();
                        System.out.println(file.getAbsolutePath() + "");
                    }
                    else
                    {
                        path = "";
                        System.out.println("error saving file!!");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result)
            {
                super.onPostExecute(result);

                try
                {
                    if(path != null && path.trim().length() > 0)
                    {
                        try
                        {
                            Intent intentData = new Intent().putExtra("single_path", path);
                            setResult(RESULT_OK, intentData);
                            finish();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        //Utils.showAlertForWarning(activity, pd, "Failed", activity.getResources().getString(R.string.crop_failed_message), SweetAlertDialog.ERROR_TYPE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            AppUtils.finishActivityAnimation(activity);
        }
        return super.onKeyDown(keyCode, event);
    }
}
