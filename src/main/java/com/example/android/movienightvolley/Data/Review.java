package com.example.android.movienightvolley.Data;

public class Review {

    private String mAuthor;
    private String mCOntent;

    public Review(String mAuthor, String mCOntent) {
        this.mAuthor = mAuthor;
        this.mCOntent = mCOntent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getContent() {
        return mCOntent;
    }
}
