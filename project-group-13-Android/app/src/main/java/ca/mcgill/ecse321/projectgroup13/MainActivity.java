package ca.mcgill.ecse321.projectgroup13;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cz.msebera.android.httpclient.Header;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String error = null;
    private ArrayList<Artwork> artworkTitles = new ArrayList<Artwork>();
    private ArtworkListAdapter searchResultAdapter;
    private MenuItem loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        refreshErrorMessage();
        ListView listView = (ListView) findViewById(R.id.listView);
        String title = "initialized";
        double price = 60.0;
        String url = "https://placekitten.com/200/300";
        int artworkID = 1;
        Artwork art = new Artwork(title,(int) price,url,artworkID);
        artworkTitles.add(art);
        searchResultAdapter = new ArtworkListAdapter(this, R.layout.artwork_view_layout, artworkTitles);
        listView.setAdapter(searchResultAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent artDetail = new Intent(MainActivity.this, ArtworkDetail.class);
                artDetail.putExtra("artworkID",artworkTitles.get(i).getArtworkID());
                startActivity(artDetail);
            }
        });
        //System.out.println("oncreate finished");
        loadFeaturedArt();
    }
    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //check whether user is logged in. If so, change menu button to logout
        MenuItem item = menu.findItem(R.id.action_login);
        String user = isLoggedIn();
        if (user.isEmpty()) {
            item.setTitle("Login");
        }
        else{
            item.setTitle("Logout");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

//        noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent loginPage = new Intent(MainActivity.this, Login.class);
            startActivity(loginPage);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }
    }
    public void loadFeaturedArt(View v){
        loadFeaturedArt();
    }
    public void loadFeaturedArt(){
        error = "";
        final TextView tv = (TextView) findViewById(R.id.artwork_name_field);
        RequestParams params = new RequestParams();

        HttpUtils.get("artwork/onPremise", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                artworkTitles.clear();
                for( int i = 0; i < response.length(); i++){
                    try {
                        String title = response.getJSONObject(i).getString("title");
                        double price = Double.parseDouble(response.getJSONObject(i).getString("worth"));
                        String url = response.getJSONObject(i).getString("imageUrl");
                        int artworkID = Integer.parseInt(response.getJSONObject(i).getString("artworkID"));
                        Artwork art = new Artwork(title,(int) price,url,artworkID);
                        artworkTitles.add(art);
                    } catch (Exception e) {
                        error += e.getMessage();
                    }
                    refreshErrorMessage();
                }
                searchResultAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                    error += "!!!!";
                } catch (JSONException e) {
                    error += e.getMessage();
                    error += "!!!";
                }
                refreshErrorMessage();
            }
        });
    }
    public void search(View v) {
        error = "";
        final TextView tv = (TextView) findViewById(R.id.artwork_name_field);
        RequestParams params = new RequestParams();
        params.put("title",tv.getText().toString());
        HttpUtils.get("artwork/byTitle/", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                artworkTitles.clear();
                for( int i = 0; i < response.length(); i++){
                    try {
                        String title = response.getJSONObject(i).getString("title");
                        double price = Double.parseDouble(response.getJSONObject(i).getString("worth"));
                        String url = response.getJSONObject(i).getString("imageUrl");
                        int artworkID = Integer.parseInt(response.getJSONObject(i).getString("artworkID"));
                        Artwork art = new Artwork(title,(int) price,url,artworkID);
                        artworkTitles.add(art);
                    } catch (Exception e) {
                        error += e.getMessage();
                    }
                    refreshErrorMessage();
                }
                searchResultAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                    error += "!!!!";
                } catch (JSONException e) {
                    error += e.getMessage();
                    error += "!!!";
                }
                refreshErrorMessage();
            }
        });
    }

    private String isLoggedIn(){
        for (File file: this.getFilesDir().listFiles()) {
            if (file.getName().contains("token")){
                return file.getName().substring(5);
            }
        }
        return "";
    }


}