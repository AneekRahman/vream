package com.vream.app;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.util.Util;
import com.vream.app.Adapters.HomeRecyclerAdapter;
import com.vream.app.Classes.HomePost;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    // View reference
    private RecyclerView mRecyclerView;

    // Class reference
    private List<HomePost> mPostList = new ArrayList<>();
    private HomeRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public HomeFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        // Connecting view with code
        mRecyclerView = (RecyclerView) rootview.findViewById(R.id.recycler_view);

        // Setting up recycler
        mAdapter = new HomeRecyclerAdapter(mPostList, getContext());
        setupRecyclerView();

        // THIS FUCKER makes the exoplayers' adapter autoplay the player that is most visible (in percentage)
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mRecyclerView != null){
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) mRecyclerView.getLayoutManager());
                    mAdapter.checkPercentageAndStartStopPlayer(getPercentage(layoutManager));

                }
            }
        });

        testPosts();

        return rootview;


    }

    private void setupRecyclerView(){

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        // add pager behavior
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);

    }

    // Get the percentage of visibility of a home post
    private ArrayList<Integer> getPercentage(LinearLayoutManager layoutManager){

        ArrayList<Integer> arrayList = new ArrayList<Integer>();

        try {

            final int firstPosition = layoutManager.findFirstVisibleItemPosition();
            final int lastPosition = layoutManager.findLastVisibleItemPosition();

            Rect rvRect = new Rect();
            mRecyclerView.getGlobalVisibleRect(rvRect);

            for (int i = firstPosition; i <= lastPosition; i++) {
                Rect rowRect = new Rect();
                layoutManager.findViewByPosition(i).getGlobalVisibleRect(rowRect);

                int percentFirst;
                if (rowRect.right >= rvRect.right){
                    int visibleHeightFirst =rvRect.right - rowRect.left;
                    percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(i).getHeight();
                }else {
                    int visibleHeightFirst = rowRect.right - rvRect.left;
                    percentFirst = (visibleHeightFirst * 100) / layoutManager.findViewByPosition(i).getHeight();
                }

                if (percentFirst>100)
                    percentFirst = 100;


                arrayList.add(percentFirst);
            }

        }finally {

            return arrayList;

        }

    }


    //TODO remove test posts
    private void testPosts(){

        HomePost post = new HomePost(HomePost.TYPE_IMAGE,
                "aneek_rahman",
                "https://secure.parksandresorts.wdpromedia.com/media/disneyparks/blog/wp-content/uploads/2016/01/IJSS6492.jpg", null);
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_VIDEO,
                "aneek_rahman",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                "https://pre00.deviantart.net/6e53/th/pre/i/2015/193/1/0/iron_man_vs_hulk_by_bowsky-d910ley.jpg");
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_IMAGE,
                "aneek_rahman",
                "http://static.independent.co.uk/s3fs-public/thumbnails/image/2016/02/19/18/pg-11-dawn-of-justice-batman-superman.jpg", null);
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_VIDEO,
                "aneek_rahman",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "https://cdn.fstoppers.com/styles/full/s3/media/2017/07/06/fstoppers_felix_hernandez_dreamphography_cars_12.jpg");
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_VIDEO,
                "aneek_rahman",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "https://cdn.fstoppers.com/styles/full/s3/media/2017/07/06/fstoppers_felix_hernandez_dreamphography_cars_12.jpg");
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_VIDEO,
                "aneek_rahman",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "https://cdn.fstoppers.com/styles/full/s3/media/2017/07/06/fstoppers_felix_hernandez_dreamphography_cars_12.jpg");
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_IMAGE,
                "aneek_rahman",
                "http://static.independent.co.uk/s3fs-public/thumbnails/image/2016/02/19/18/pg-11-dawn-of-justice-batman-superman.jpg", null);
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_IMAGE,
                "aneek_rahman",
                "http://static.independent.co.uk/s3fs-public/thumbnails/image/2016/02/19/18/pg-11-dawn-of-justice-batman-superman.jpg", null);
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_VIDEO,
                "aneek_rahman",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "https://cdn.fstoppers.com/styles/full/s3/media/2017/07/06/fstoppers_felix_hernandez_dreamphography_cars_12.jpg");
        mPostList.add(post);

        post = new HomePost(HomePost.TYPE_IMAGE,
                "aneek_rahman",
                "http://static.independent.co.uk/s3fs-public/thumbnails/image/2016/02/19/18/pg-11-dawn-of-justice-batman-superman.jpg", null);
        mPostList.add(post);

        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            mAdapter.releaseExoPlayerFromFragment();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            mAdapter.releaseExoPlayerFromFragment();
        }
    }



}
