package com.vream.app.Classes;

public class HomePost {

    public static final int TYPE_VIDEO = 0;
    public static final int TYPE_IMAGE = 1;

    int postType;
    String username, contentUrl, vidThumbnail;

    public HomePost(int postType, String username, String contentUrl, String vidThumbnail){

        this.postType = postType;
        this.username = username;
        this.contentUrl = contentUrl;
        this.vidThumbnail = vidThumbnail;

    }

    public int getPostType() {
        return postType;
    }

    public String getUsername() {
        return username;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getVidThumbnail() {
        return vidThumbnail;
    }
}
