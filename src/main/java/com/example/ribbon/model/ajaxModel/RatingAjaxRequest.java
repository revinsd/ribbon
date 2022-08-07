package com.example.ribbon.model.ajaxModel;

public class RatingAjaxRequest {
    private long postId;
    private String operationType;

    public RatingAjaxRequest(long postId, String operationType) {
        this.postId = postId;
        this.operationType = operationType;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
