package cheetah.alphacapital.reportApp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cheetah.alphacapital.R;
import cheetah.alphacapital.databinding.ActivityLearningAndManualBinding;
import cheetah.alphacapital.reportApp.fragment.BusinessToolFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARActivityAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARClientAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDAREmployeeAnalysisFragment;
import cheetah.alphacapital.reportApp.fragment.EmpDARReportListFragment;
import cheetah.alphacapital.reportApp.fragment.FormsFragment;
import cheetah.alphacapital.reportApp.fragment.KnowledgeSessionFragment;
import cheetah.alphacapital.reportApp.fragment.LearningManualResponseModel;
import cheetah.alphacapital.reportApp.fragment.PoliciesFragment;
import cheetah.alphacapital.reportApp.fragment.VideoManualsFragment;
import cheetah.alphacapital.utils.AppUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LearningAndManualActivity extends BaseActivity
{
    private ActivityLearningAndManualBinding binding;
    private String[] tabsTitle = {"Policies", "Business Tools", "Video Manuals", "Knowledge Session", "Forms"};
    private ArrayList<LearningManualResponseModel.DataItem> listData = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_learning_and_manual);
        initView();
        onClick();
    }

    private void initView()
    {
        binding.toolbar.llNotification.setVisibility(View.GONE);
        setSupportActionBar(binding.toolbar.toolbar);
        /*binding.toolbar.ivHeader.setImageResource(R.drawable.img_portfolio);
        int height = 56;
        binding.toolbar.viewStatusBar.setVisibility(View.GONE);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) binding.toolbar.ivHeader.getLayoutParams();
        params.height = (int) AppUtils.pxFromDp(activity, height);
        binding.toolbar.ivHeader.setLayoutParams(params);*/
        binding.toolbar.ivLogo.setVisibility(View.GONE);
        binding.toolbar.txtTitle.setText("Learning Manuals");
        getLearningModualsAPI();

        binding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int i, float v, int i1)
            {}

            @Override
            public void onPageSelected(int i)
            {
                if (i == 0)
                {
                    binding.toolbar.txtTitle.setText("Learning Manuals");
                }
                else if (i == 1)
                {
                    binding.toolbar.txtTitle.setText("Learning Manuals");
                }
                else if (i == 2)
                {
                    binding.toolbar.txtTitle.setText("Learning Manuals");
                }
                else if (i == 3)
                {
                    binding.toolbar.txtTitle.setText("Learning Manuals");
                }
                else
                {
                    binding.toolbar.txtTitle.setText("Learning Manuals");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i)
            {}
        });

    }

    private void getLearningModualsAPI()
    {
        binding.llLoading.llLoading.setVisibility(View.VISIBLE);
        apiService.getLearningManualsCategory(Integer.valueOf(sessionManager.getUserId())).enqueue(new Callback<LearningManualResponseModel>()
        {
            @Override
            public void onResponse(Call<LearningManualResponseModel> call, Response<LearningManualResponseModel> response)
            {
                if (response.isSuccessful())
                {
                    if (response.body().isSuccess())
                    {
                        if (response.body().getData().size() > 0)
                        {
                            listData.addAll(response.body().getData());
                            setupPagerData();
                        }
                    }
                    else
                    {
                        showToast("Something went wrong");
                    }
                }
                binding.llLoading.llLoading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<LearningManualResponseModel> call, Throwable throwable)
            {
                binding.llLoading.llLoading.setVisibility(View.GONE);
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

    private void setupPagerData()
    {
        TabAdapter mAdapter = new TabAdapter();
        binding.pager.setAdapter(mAdapter);
        binding.tabs.setupWithViewPager(binding.pager);
        binding.pager.setOffscreenPageLimit(4);
        setupTabIcons();
    }

    private void setupTabIcons()
    {
        try
        {
            for (int i = 0; i < listData.size(); i++)
            {
                final View tab = LayoutInflater.from(activity).inflate(R.layout.rowview_tab, null);
                TextView title = (TextView) tab.findViewById(R.id.txtTab);
                title.setText(listData.get(i).getTitle());
                binding.tabs.getTabAt(i).setCustomView(tab);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class TabAdapter extends FragmentPagerAdapter
    {
        public TabAdapter()
        {
            super(getSupportFragmentManager());
        }

        @Override public Fragment getItem(int position)
        {
            if (position == 0)
            {
                return new PoliciesFragment(String.valueOf(listData.get(position).get$id()));
            }
            else if (position == 1)
            {
                return new BusinessToolFragment(String.valueOf(listData.get(position).get$id()));
            }
            else if (position == 2)
            {
                return new VideoManualsFragment(String.valueOf(listData.get(position).get$id()));
            }
            else if (position == 3)
            {
                return new KnowledgeSessionFragment(String.valueOf(listData.get(position).get$id()));
            }
            else
            {
                return new FormsFragment(String.valueOf(listData.get(position).get$id()));
            }
        }

        @Override public int getCount()
        {
            return tabsTitle.length;
        }

        @Override public CharSequence getPageTitle(int position)
        {
            return null;
        }
    }

}