package com.example.android.movienightvolley.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movienightvolley.Data.Review;
import com.example.android.movienightvolley.R;

import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<Review> {

    private TextView mAuthorTextView;
    private TextView mContentTextView;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View reviewItemView = convertView;
        if (reviewItemView == null){
            reviewItemView = LayoutInflater.from(getContext()).inflate(R.layout.reviews_layout, parent, false);
        }
        Review currentReview = getItem(position);
        mAuthorTextView = reviewItemView.findViewById(R.id.review_author_text_view);
        mContentTextView = reviewItemView.findViewById(R.id.review_content_text_view);
        mAuthorTextView.setText(currentReview.getAuthor());
        mContentTextView.setText(currentReview.getContent());
        return reviewItemView;
    }
}
