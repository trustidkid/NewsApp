package com.example.android.newsapp;

public class News {

    //private String mAuthor;
    private String mSectionName;
    private String mPublishDate;
    private String mTitle;
    private String mURL;

    public News(String sectionName, String publishDate, String title, String url){
        //mAuthor = author;
        mSectionName=sectionName;
        mPublishDate=publishDate;
        mTitle=title;
        mURL=url;
    }
    public String getURL()
    {return mURL;}
    //public String getAuthor(){
//        return mAuthor;
//    }
    public String getSectionName(){
        return mSectionName;
    }
    public String getPublishDate(){
        return mPublishDate;
    }
    public String getTitle(){
        return mTitle;
    }
}
