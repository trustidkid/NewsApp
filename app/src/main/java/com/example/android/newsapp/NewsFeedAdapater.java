package com.example.android.newsapp;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsFeedAdapater extends ArrayAdapter<News> {

    /**
     * create a constructor to display the list of the earthquake
     *
     * @param context    the current activity
     * @param news array list of the earthquake
     */
    public NewsFeedAdapater(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * check if the listview exist, if not inflate one to populate the list
     *
     * @param position    position of item on the list view
     * @param convertView the current view
     * @param parent      the viewgroup that contain the listview
     * @return
     */
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_news, parent, false);
        }

        //get the position of the item
        News newsArticle = getItem(position);

        ViewHolder holder = new ViewHolder();

        //set the text value for news article title
        holder.mTextView = (TextView) listView.findViewById(R.id.web_title);
        holder.mTextView.setText(newsArticle.getTitle());

        //set the text value for news author
//        holder.mTextView = (TextView)listView.findViewById(R.id.author);
//        holder.mTextView.setText(newsArticle.getAuthor());

        //set the text value for news publsh date
        holder.mTextView = (TextView)listView.findViewById(R.id.publish_date);
        holder.mTextView.setText(newsArticle.getPublishDate());

        //set the text value for news author
        holder.mTextView = (TextView)listView.findViewById(R.id.section_name);
        holder.mTextView.setText(newsArticle.getSectionName());

        return listView;
    }
}
