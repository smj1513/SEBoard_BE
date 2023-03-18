package com.seproject.seboard.application;

public class CommentAppService {

    public void createNamedComment(Long userId, Long postId, String contents){
    }

    public void createUnnamedComment(Long postId, String contents, String password){
    }

    public void createNamedReply(Long userId, Long postId, Long commentId, String contents){
    }

    public void createUnnamedReply(Long postId, Long commentId, String contents, String password){
    }

    // Retrieve -> Comment만
    //TODO : void -> 변경필요
    public void retrieveCommentList(Long postId){
        //TODO : pagination 고려
    }

    public void changeContentsOfNamed(Long commentId, Long userId, String contents){
    }

    public void changeContentsOfUnnamed(Long commentId, String password, String contents){
    }

    public void deleteNamedComment(Long commentId, Long userId){
    }

    public void deleteUnnamedComment(Long commentId, String password){
    }
    
}
