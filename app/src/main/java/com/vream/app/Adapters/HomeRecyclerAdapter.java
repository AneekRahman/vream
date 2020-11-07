package com.vream.app.Adapters;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.vream.app.Classes.CacheDataSourceFactory;
import com.vream.app.Classes.HomePost;
import com.vream.app.R;

import java.util.ArrayList;
import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter {

    private List<HomePost> mPostList;
    Context mContext;

    // View holders to control exoplayers
    private RecyclerView.ViewHolder previousHolder, afterHolder, thirdHolder;

    // Image view holder

    public class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mMainHolder;
        CardView mContentHolder;
        ImageView mPostImageView, mUserDp, mHeartButton;

        public ImageTypeViewHolder(View itemView) {
            super(itemView);

            mMainHolder = (LinearLayout) itemView.findViewById(R.id.main_holder);
            mContentHolder = (CardView) itemView.findViewById(R.id.post_content_holder);
            mPostImageView = (ImageView) itemView.findViewById(R.id.post_image);
            mUserDp = (ImageView) itemView.findViewById(R.id.user_dp);
            mHeartButton = (ImageView) itemView.findViewById(R.id.heart_button);

        }

        public void setOnClickListeners(){

            mHeartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mHeartButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_heart_filled_red));

                }
            });

        }

        public void setMainHolderWidth(){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int widthPixels = displayMetrics.widthPixels;

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) (24 * scale + 0.5f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels - pixels, ViewGroup.LayoutParams.WRAP_CONTENT);
            mMainHolder.setLayoutParams(params);

        }

    }

    // Video view holder

    public class VideoTypeViewHolder extends RecyclerView.ViewHolder {

        CardView mMainHolder;
        CardView mContentHolder;
        ImageView mUninitializedOverlay, mUserDp, mHeartButton, mOpenPlayerActivityBtn;
        PlayerView mPlayerView;

        // Exoplayer declarations
        private String vidAddress;
        private ExoPlayer mExoPlayer;
        private MediaSource mMediaSource;
        private int mExoPlayerWindowIndex = 0;
        private long mPlaybackPosition = 0;

        public VideoTypeViewHolder(View itemView) {
            super(itemView);

            mMainHolder = (CardView) itemView.findViewById(R.id.main_holder);
            mContentHolder = (CardView) itemView.findViewById(R.id.post_content_holder);
            mUninitializedOverlay = (ImageView) itemView.findViewById(R.id.post_video_uninitialized_overlay);
            mPlayerView = (PlayerView) itemView.findViewById(R.id.player_view);
            mUserDp = (ImageView) itemView.findViewById(R.id.user_dp);
            mHeartButton = (ImageView) itemView.findViewById(R.id.heart_button);
            mOpenPlayerActivityBtn = (ImageView) itemView.findViewById(R.id.player_activity_btn);

        }

        public void setMainHolderWidth(){

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int widthPixels = displayMetrics.widthPixels;

            final float scale = mContext.getResources().getDisplayMetrics().density;
            int pixels = (int) (24 * scale + 0.5f);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPixels - pixels, ViewGroup.LayoutParams.WRAP_CONTENT);
            mMainHolder.setLayoutParams(params);

        }

        public void setOnClickListeners(){

            mHeartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mHeartButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_heart_filled_red));

                }
            });

            mOpenPlayerActivityBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    /* TODO player activity
                    Intent intent = new Intent(mContext, PlayerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("vidAddress", vidAddress);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    */

                }
            });

        }

        public void setVidAdress(String vidAdress){

            this.vidAddress = vidAdress;

        }

        // Initializing the posts exoplayer
        public void initializeExoPlayer(){

            if(mExoPlayer == null){

                mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(mContext),
                        new DefaultTrackSelector(),
                        new DefaultLoadControl());

                mPlayerView.setPlayer(mExoPlayer);

                mExoPlayer.setPlayWhenReady(true);
                mExoPlayer.seekTo(mExoPlayerWindowIndex, mPlaybackPosition);
                mExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);

                prepareExoPlayer();
            }

        }

        // Preparing the exoplayer with the video address
        private void prepareExoPlayer(){

            Uri uri = Uri.parse(vidAddress);
            mMediaSource = buildMediaSource(uri);
            mExoPlayer.prepare(mMediaSource, false, false);

            mExoPlayer.addListener(mExoPlayerEventListener);

        }

        // Building mediasource for exoplayer
        private MediaSource buildCacheMediaSource(Uri uri) {

            return new ExtractorMediaSource(uri,
                    new CacheDataSourceFactory(mContext, 100 * 1024 * 1024, 5 * 1024 * 1024),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

        }

        // Building mediasource for exoplayer
        private MediaSource buildMediaSource(Uri uri) {
            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("vream-exo")).createMediaSource(uri);
        }

        private void fadeInOutOverlay(boolean fadeIn){

            if(fadeIn){

                mUninitializedOverlay.animate()
                        .alpha(1.0f)
                        .setDuration(200)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationEnd(Animator animator) {
                                releasePlayer();
                            }
                            @Override public void onAnimationStart(Animator animator) {}
                            @Override public void onAnimationCancel(Animator animator) {}
                            @Override public void onAnimationRepeat(Animator animator) {}
                        });
            }else{

                mUninitializedOverlay.animate()
                        .alpha(0.0f)
                        .setDuration(200)
                        .setListener(null);
            }

        }

        // Exoplayer events listener
        Player.EventListener mExoPlayerEventListener = new Player.EventListener() {

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                // Giving Player state response to user
                switch (playbackState){

                    case ExoPlayer.STATE_READY: {
                        fadeInOutOverlay(false);
                        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    break;
                    case ExoPlayer.STATE_BUFFERING: {}
                    case ExoPlayer.STATE_ENDED: {
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    case ExoPlayer.STATE_IDLE: {
                        ((Activity) mContext).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    }
                    break;
                }

            }

            // On error keep on trying to ready the player on and on (This happens when internet connection unintentionally goes)
            @Override
            public void onPlayerError(ExoPlaybackException error) {

                mExoPlayer.prepare(mMediaSource, false, false);

            }
            // Not needed default methods
            @Override public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {}
            @Override public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}
            @Override public void onLoadingChanged(boolean isLoading) {}
            @Override public void onPositionDiscontinuity(int reason) {}
            @Override public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
            @Override public void onSeekProcessed() {}
            @Override public void onRepeatModeChanged(int repeatMode) {}
            @Override public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {}
        };

        // Releasing the exoplayer manually ( When home fragment gets paused or stopped )
        // Gets called after overlay ends
        public void releasePlayer() {

            if(mExoPlayer != null) {

                mPlaybackPosition = mExoPlayer.getCurrentPosition();
                mExoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
                mExoPlayer.removeListener(mExoPlayerEventListener);
                mExoPlayer.release();
                mExoPlayer = null;
            }

        }

        public void releaseExoPlayer(){

            fadeInOutOverlay(true);

        }

    }

    public HomeRecyclerAdapter(List<HomePost>data, Context context) {
        this.mPostList = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case HomePost.TYPE_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_video_post_layout, parent, false);
                return new VideoTypeViewHolder(view);
            case HomePost.TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_image_post_layout, parent, false);
                return new ImageTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (mPostList.get(position).getPostType()) {
            case HomePost.TYPE_VIDEO:
                return HomePost.TYPE_VIDEO;
            case HomePost.TYPE_IMAGE:
                return HomePost.TYPE_IMAGE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        HomePost post = mPostList.get(listPosition);

        if (post != null) {

            if(post.getPostType() == HomePost.TYPE_VIDEO){

                // VIDEO POST

                VideoTypeViewHolder postHolder = ((VideoTypeViewHolder) holder);

                // User dp
                Glide.with(mContext)
                        .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSStSkWpZMKQkWpCwN_2i2g03JYYxB8Iolxg-_NAs-1a40hTDX5")
                        .apply(RequestOptions.circleCropTransform())
                        .into(postHolder.mUserDp);

                // Uninitialized overlay
                Glide.with(mContext)
                        .load(post.getVidThumbnail())
                        .apply(RequestOptions.centerCropTransform())
                        .into(postHolder.mUninitializedOverlay);

                postHolder.setVidAdress(post.getContentUrl());
                postHolder.setMainHolderWidth();
                setVideoHolderHeight(holder);
                postHolder.setOnClickListeners();

            }else{

                // IMAGE POST

                ImageTypeViewHolder postHolder = ((ImageTypeViewHolder) holder);

                // User dp
                Glide.with(mContext)
                        .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSStSkWpZMKQkWpCwN_2i2g03JYYxB8Iolxg-_NAs-1a40hTDX5")
                        .apply(RequestOptions.circleCropTransform())
                        .into(postHolder.mUserDp);

                // Post image
                Glide.with(mContext)
                        .load(post.getContentUrl())
                        .apply(RequestOptions.centerCropTransform())
                        .into(postHolder.mPostImageView);

                postHolder.setMainHolderWidth();
                //setImageHolderHeight(holder);
                postHolder.setOnClickListeners();

            }

        }
    }

    // Setting a fixed height for the root view
    private void setImageHolderHeight(RecyclerView.ViewHolder holder){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (8 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, widthPixels - pixels);
        ((ImageTypeViewHolder) holder).mContentHolder.setLayoutParams(params);

    }

    // Setting a fixed height for the root view
    private void setVideoHolderHeight(RecyclerView.ViewHolder holder){

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthPixels = displayMetrics.widthPixels;

        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (120 * scale + 0.5f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, widthPixels - pixels);
        ((VideoTypeViewHolder) holder).mContentHolder.setLayoutParams(params);

    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        if(previousHolder == null){
            previousHolder = holder;
            return;
        }
        if(holder.getAdapterPosition() == previousHolder.getAdapterPosition() + 1){
            afterHolder = holder;
        }else if(holder.getAdapterPosition() == previousHolder.getAdapterPosition() + 2){
            thirdHolder = holder;
        }else if(holder.getAdapterPosition() == previousHolder.getAdapterPosition() - 1){
            thirdHolder = afterHolder;
            afterHolder = previousHolder;
            previousHolder = holder;
        }


    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        if(holder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
            ((VideoTypeViewHolder)holder).releaseExoPlayer();

        if(holder.getAdapterPosition() == previousHolder.getAdapterPosition()){
            previousHolder = afterHolder;
            afterHolder = thirdHolder;
            thirdHolder = null;
        }

    }

    // Checks the visibility percentage of a post on user screen
    public void checkPercentageAndStartStopPlayer(ArrayList<Integer> arrayList){

        if(arrayList.size() == 2){

            Integer previousPer = arrayList.get(0);
            Integer afterPer = arrayList.get(1);

            if(previousPer < 50 && afterPer > 50){
                if(previousHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                    ((VideoTypeViewHolder)previousHolder).releaseExoPlayer();

                if(afterHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                    ((VideoTypeViewHolder)afterHolder).initializeExoPlayer();

            }else if(previousPer > 50 && afterPer < 50){
                if(previousHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                    ((VideoTypeViewHolder)previousHolder).initializeExoPlayer();

                if(afterHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                    ((VideoTypeViewHolder)afterHolder).releaseExoPlayer();

            }

        }else if(arrayList.size() == 3){

            if(previousHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                ((VideoTypeViewHolder)previousHolder).releaseExoPlayer();

            if(afterHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                ((VideoTypeViewHolder)afterHolder).initializeExoPlayer();

            if(thirdHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
                ((VideoTypeViewHolder)thirdHolder).releaseExoPlayer();

        }

    }

    // Releases all viewholders' exoplayers manually (When home fragment gets resumed or started)
    public void releaseExoPlayerFromFragment(){

        if(previousHolder != null && previousHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
            ((VideoTypeViewHolder)previousHolder).releaseExoPlayer();

        if(afterHolder != null && afterHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
            ((VideoTypeViewHolder)afterHolder).releaseExoPlayer();

        if(thirdHolder != null && thirdHolder.getClass().getSimpleName() == VideoTypeViewHolder.class.getSimpleName())
            ((VideoTypeViewHolder)thirdHolder).releaseExoPlayer();


    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }
}