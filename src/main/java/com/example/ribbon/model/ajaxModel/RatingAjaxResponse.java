package com.example.ribbon.model.ajaxModel;

import com.fasterxml.jackson.annotation.JsonView;

public class RatingAjaxResponse {

    private long postRating;

    public long getPostRating() {
        return postRating;
    }

    public void setPostRating(long postRating) {
        this.postRating = postRating;
    }
}
