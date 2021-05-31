package com.example.demoapp.data.model;

import java.util.Date;
import java.util.Deque;
import java.util.LinkedList;

public class Post extends Item
{
    private int postID;
    private String profileImageID;
    private String thumbnailImageID;
    private String countrySummary;
    private String username;
    private String title;
    private String description;
    private Date date;
    private int likes;
    private boolean liked;

    private LinkedList<Activity> activities;
    private LinkedList<String> images;
    private LinkedList<Comment> comments;

    public Post()
    {
        super(ContentType.POST);

        this.activities = new LinkedList<>();
        this.images = new LinkedList<>();
        this.comments = new LinkedList<>();
    }

    public Post(String username, String description, Date date)
    {
        super(ContentType.POST);

        this.username = username;
        this.description = description;
        this.date = date;
        this.activities = new LinkedList<>();
        this.images = new LinkedList<>();
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getProfileImageID()
    {
        return profileImageID;
    }

    public void setProfileImageID(String profileImageID)
    {
        this.profileImageID = profileImageID;
    }

    public String getThumbnailImageID()
    {
        return thumbnailImageID;
    }

    public void setThumbnailImageID(String thumbnailImageID)
    {
        this.thumbnailImageID = thumbnailImageID;
    }

    public LinkedList<Activity> getActivities()
    {
        return activities;
    }

    public void setActivities(LinkedList<Activity> activities)
    {
        this.activities = activities;
    }

    public LinkedList<String> getImages()
    {
        return images;
    }

    public void setImages(LinkedList<String> images)
    {
        this.images = images;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public int getPostID()
    {
        return postID;
    }

    public void setPostID(int postID)
    {
        this.postID = postID;
    }

    public String getCountrySummary()
    {
        return countrySummary;
    }

    public void setCountrySummary(String countrySummary)
    {
        this.countrySummary = countrySummary;
    }

    public int getLikes()
    {
        return likes;
    }

    public void setLikes(int likes)
    {
        this.likes = likes;
    }

    public LinkedList<Comment> getComments()
    {
        return comments;
    }

    public void setComments(LinkedList<Comment> comments)
    {
        this.comments = comments;
    }

    public boolean isLiked()
    {
        return liked;
    }

    public void setLiked(boolean liked)
    {
        this.liked = liked;
    }
}
