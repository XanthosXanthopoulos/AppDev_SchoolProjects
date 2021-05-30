package com.example.demoapp.data;

public interface CommentLikeClickListener
{
    void sendComment(int postID, String content);

    void sendLike(int postID);
}
