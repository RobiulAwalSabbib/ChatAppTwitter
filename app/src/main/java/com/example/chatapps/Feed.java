package com.example.chatapps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feed extends AppCompatActivity {

    List<Map<String,String>> tweetData = new ArrayList<Map<String,String>>();
    ListView feedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

         feedList = (ListView)findViewById(R.id.feedList);

//
//        for(int i = 0; i <= 5; i++){
//
//            Map<String,String> tweetinfo = new HashMap<String, String>();
//            tweetinfo.put("content","Tweet content"+Integer.toString(i));
//            tweetinfo.put("username","name"+Integer.toString(i));
//            tweetData.add(tweetinfo);
//        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null && objects.size()>0){

                    for(ParseObject object : objects){


                        Map<String,String> tweetinfo = new HashMap<String, String>();
                        tweetinfo.put("content","Tweet content \n"+object.getString("tweet"));
                        tweetinfo.put("username","name \n"+object.getString("username"));
                        tweetData.add(tweetinfo);

                    }


                    SimpleAdapter simpleAdapter = new SimpleAdapter(Feed.this,tweetData,android.R.layout.simple_list_item_2,new String[]{"content","username"},new int[]{android.R.id.text1,android.R.id.text2});

                    feedList.setAdapter(simpleAdapter);
                }

            }
        });


    }
}
