package com.vream.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // View reference
    private ImageView mHomeBtn, mExploreBtn, mCaptureBtn, mMessageBtn, mProfileBtn;

    // Class reference
    private FragmentManager mFragManager = getSupportFragmentManager();
    private HomeFragment mHomeFragment = new HomeFragment();
    private ExploreFragment mExploreFragment = new ExploreFragment();
    private MessageFragment mMessageFragment = new MessageFragment();
    private ProfileFragment mProfileFragment = new ProfileFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connecting code to views
        mHomeBtn = (ImageView) findViewById(R.id.home_btn);
        mExploreBtn = (ImageView) findViewById(R.id.explore_btn);
        mCaptureBtn = (ImageView) findViewById(R.id.capture_btn);
        mMessageBtn = (ImageView) findViewById(R.id.message_btn);
        mProfileBtn = (ImageView) findViewById(R.id.profile_btn);

        mFragManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                Fragment fragment = mFragManager.findFragmentById(R.id.fragment_holder);
                String fragName = fragment.getClass().getSimpleName();

                if(fragName == HomeFragment.class.getSimpleName()){
                    unselectAllBtn();
                    mHomeBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_home_icon_selected));
                }

                if(fragName == ExploreFragment.class.getSimpleName()){
                    unselectAllBtn();
                    mExploreBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_explore_selected_icon));
                }

                if(fragName == MessageFragment.class.getSimpleName()){
                    unselectAllBtn();
                    mMessageBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_message_icon_selected));
                }

            }
        });

        if(mFragManager.findFragmentById(R.id.fragment_holder) == null){

            mFragManager.beginTransaction()
                    .add(R.id.fragment_holder, mHomeFragment)
                    .commit();

        }


        setNavClickListeners();

    }

    private void replaceFragment(Fragment fragment){

        // Clears the home frags video cache bcz it gets messy after popping back
        //FileUtils.deleteQuietly(new File(getCacheDir(), getString(R.string.app_name) + "_exo_cache"));

        if(fragment.getClass().getName() == mFragManager.findFragmentById(R.id.fragment_holder).getClass().getName()) return;

        mFragManager.beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .addToBackStack(null)
                .commit();

    }

    private void setNavClickListeners(){

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllBtn();
                mHomeBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_home_icon_selected));
                replaceFragment(mHomeFragment);
            }
        });

        mExploreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllBtn();
                mExploreBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_explore_selected_icon));
                replaceFragment(mExploreFragment);
            }
        });

        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllBtn();
                mCaptureBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_capture_icon_selected));
            }
        });

        mMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllBtn();
                mMessageBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_message_icon_selected));
                replaceFragment(mMessageFragment);
            }
        });

        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unselectAllBtn();
                mProfileBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_person_icon_selected));
                replaceFragment(mProfileFragment);
            }
        });

    }

    private void unselectAllBtn(){

        mHomeBtn.setImageDrawable(ContextCompat.getDrawable( this,R.drawable.ic_home_icon_not_selected));
        mExploreBtn.setImageDrawable(ContextCompat.getDrawable( this,R.drawable.ic_explore_not_selected_icon));
        mCaptureBtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nav_capture_icon_not_selected));
        mMessageBtn.setImageDrawable(ContextCompat.getDrawable( this,R.drawable.ic_message_icon_not_selected));
        mProfileBtn.setImageDrawable(ContextCompat.getDrawable( this,R.drawable.ic_person_icon_not_selected));

    }

}