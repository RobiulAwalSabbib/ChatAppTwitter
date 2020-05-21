package com.example.chatapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


    ListView userList;
    ArrayList<String> userArray = new ArrayList<>();
    ArrayAdapter adapter ;
    List<Object> emtyList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.tweet_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.tweet){

            Log.i("SElect","ok");
            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setTitle("Send a Tweet");

            final EditText tweetContentET = new EditText(this);
            builder.setView(tweetContentET);

            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ParseObject object = new ParseObject("Tweet");
                    object.put("username",ParseUser.getCurrentUser().getUsername());
                    object.put("tweet",tweetContentET.getText().toString());
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(getApplicationContext(),"SEnd messsage succesful",Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplicationContext(),"SEnd Faiend...!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else if(item.getItemId()==R.id.logout){
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        else if(item.getItemId()==R.id.viewFeed){
            Intent intent = new Intent(getApplicationContext(),Feed.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setTitle("UserList");



        if(ParseUser.getCurrentUser().getList("isFollowing") == null){

            emtyList = new ArrayList<>();
            ParseUser.getCurrentUser().put("isFollowing",emtyList);




            /// example ArrayList<String> aList = obj.getList("arrayName");
            //aList.add("aString")
            //obj.put("arrayName", aList);
            //obj.saveInBackground();

        }

        userList = findViewById(R.id.userListView);
        userList.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);



        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked,userArray);
        userList.setAdapter(adapter);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView)view;
                if(checkedTextView.isChecked()){

                    emtyList = ParseUser.getCurrentUser().getList("isFollowing");
                    emtyList.add(userArray.get(position));
                    ParseUser.getCurrentUser().put("isFollowing",emtyList);
                    ParseUser.getCurrentUser().saveInBackground();

//                    ParseUser.getCurrentUser().getList("isFollowing").add(userArray.get(position));
//                    ParseUser.getCurrentUser().saveInBackground();




                    Toast.makeText(getApplicationContext(),"Item selected",Toast.LENGTH_LONG).show();
                }else if (!checkedTextView.isChecked()){

//                    ParseUser.getCurrentUser().getList("isFollowing").remove(userArray.get(position));
//                    ParseUser.getCurrentUser().saveInBackground();

//                    emtyList = ParseUser.getCurrentUser().getList("isFollowing");
//                    emtyList.remove(userArray.get(position));
////                    ParseUser.getCurrentUser().put("isFollowing",emtyList);
//                    ParseUser.getCurrentUser().saveInBackground();


                    ParseUser currentUser = ParseUser.getCurrentUser();
                    emtyList = currentUser.getList("isFollowing");
                    emtyList.remove(userArray.get(position));
                    currentUser.put("isFollowing", emtyList);
                    currentUser.saveInBackground();
                    //emtyList.clear();




                    Toast.makeText(getApplicationContext(),"Item unchecked",Toast.LENGTH_LONG).show();
                }


            }
        });

        userArray.clear();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null && objects.size() > 0){
                    for(ParseUser user :objects){
                        userArray.add(user.getUsername());
                    }

                    adapter.notifyDataSetChanged();

                    for(String username : userArray){


                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(username)){

                            userList.setItemChecked(userArray.indexOf(username),true);

                        }

                    }

                }
            }
        });

    }
}
