package cheetah.alphacapital.reportApp.activity;

import android.app.Activity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cheetah.alphacapital.classes.TouchImageView;
import cheetah.alphacapital.utils.AppUtils;
import cheetah.alphacapital.R;


public class ImageSliderActivity extends AppCompatActivity
{
    private Activity activity;
    private Toolbar toolbar;
    private ImageView ivHeader;
    private View viewStatusBar;
    private View emptyView;
    private LinearLayout llBackNavigation;
    private ImageView ivLogo;
    private ImageView ivIcon;
    private TextView txtTitle;
    private ImageView ivContactUs;
    private LinearLayout llNotification;
    private ImageView ivLogout;
    private int position = 0;
    private boolean isStatusBarHidden = false;
    private TouchImageView ivRowViewProduct;
    private ProgressBar pbImage;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_slider);
        activity = ImageSliderActivity.this;
        AppUtils.setLightStatusBar(activity);
        url = getIntent().getExtras().getString("url");
        setUpViews();
    }

    private void setUpViews()
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivHeader = (ImageView) findViewById(R.id.ivHeader);
        viewStatusBar = (View) findViewById(R.id.viewStatusBar);
        emptyView = (View) findViewById(R.id.emptyView);
        llBackNavigation = (LinearLayout) findViewById(R.id.llBackNavigation);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivIcon = (ImageView) findViewById(R.id.ivIcon);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        ivContactUs = (ImageView) findViewById(R.id.ivContactUs);
        llNotification = (LinearLayout) findViewById(R.id.llNotification);
        ivLogout = (ImageView) findViewById(R.id.ivLogout);

        /*setSupportActionBar(toolbar);
        ImageView ivHeader = findViewById(R.id.ivHeader);
        ivHeader.setImageResource(R.drawable.img_portfolio);

        int height = 56;
        if (isStatusBarHidden)
        {
            height = 56 + 25;
            toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.INVISIBLE);
        }
        else
        {
            toolbar.findViewById(R.id.viewStatusBar).setVisibility(View.GONE);
        }

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        ivHeader.setLayoutParams(params);*/

        ivLogo.setVisibility(View.GONE);
        llNotification.setVisibility(View.GONE);
        txtTitle.setText("View Image");
        llBackNavigation.setVisibility(View.VISIBLE);
        ivRowViewProduct = (TouchImageView) findViewById(R.id.ivRowView_Product);
        pbImage = (ProgressBar) findViewById(R.id.pbImage);

        setSupportActionBar(toolbar);

        llBackNavigation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    finish();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        try
        {
           // pbImage.setVisibility(View.VISIBLE);

          /*  Picasso.get().load(url.toString().trim().replace(" ", "%20")).into(ivRowViewProduct, new Callback()
            {
                @Override
                public void onSuccess()
                {
                    pbImage.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e)
                {

                }
            });*/

          /*  Glide.with(activity).load(url).listener(new RequestListener<String, GlideDrawable>()
            {
                @Override
                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b)
                {
                    pbImage.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1)
                {
                    pbImage.setVisibility(View.GONE);
                    return false;
                }
            }).into(ivRowViewProduct);*/

            Glide.with(activity).load(url.toString().trim()).placeholder(R.color.blue_new).into(ivRowViewProduct);



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed()
    {
        activity.finish();

        super.onBackPressed();
    }

}
