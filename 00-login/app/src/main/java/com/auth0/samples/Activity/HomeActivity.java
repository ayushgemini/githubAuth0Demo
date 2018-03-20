package com.auth0.samples.Activity;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.SearchManager;
import android.support.v7.widget.SearchView;

import com.auth0.samples.Adapter.RecyclerViewAdapter;
import com.auth0.samples.Adapter.UserAdapter;
import com.auth0.samples.Storage.DatabaseHelper;
import com.auth0.samples.Utill.ListItemClickListener;
import com.auth0.samples.R;
import com.auth0.samples.Model.UserModel;
import com.auth0.samples.Model.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ayush on 19/3/18.
 */

public class HomeActivity  extends AppCompatActivity implements ListItemClickListener {

    private UserViewModel viewModel;
    private String TAG = "MainActivity";
    private static List<UserModel> listUsers;
    private static DatabaseHelper databaseHelper;
    private static Context context;
    private SearchView searchView;
    private static RecyclerViewAdapter userAdpater;
    private static RecyclerView recyclerView;
     static UserAdapter userUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(R.string.toolbar_title);

        recyclerView = (RecyclerView) findViewById(R.id.userList);
        databaseHelper = new DatabaseHelper(HomeActivity.this);
        HomeActivity.context = getApplicationContext();

        listUsers = new ArrayList<>();
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        viewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userUserAdapter = new UserAdapter(this);
        viewModel.userList.observe(this, pagedList -> {
            userUserAdapter.setList(pagedList);
        });
        viewModel.networkState.observe(this, networkState -> {
            userUserAdapter.setNetworkState(networkState);
            Log.d(TAG, "Network State Change");
        });
        getDataFromSQLite();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                filter(query);
                return false;
            }
        });
        return true;
    }

    void filter(String text){
        List<UserModel> temp = new ArrayList();
        if(listUsers.size()>0) {
            for (UserModel d : listUsers) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getUserName().contains(text)) {
                    temp.add(d);
                }
            }
            //update recyclerview
            userAdpater.updateList(temp);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    public static Context getAppContext() {
        return HomeActivity.context;
    }

    /**
     * This method is to fetch all user records from SQLite
     */
    public static void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                listUsers.clear();
                listUsers.addAll(databaseHelper.getAllUser());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                //notify the adapter
                if (listUsers.size()>0) {
                    userAdpater = new RecyclerViewAdapter(listUsers);
                    recyclerView.setAdapter(userAdpater);
                }
                else{
                    recyclerView.setAdapter(userUserAdapter);

                }
            }
        }.execute();
    }
    @Override
    public void onRetryClick(View view, int position) {
        Log.d(TAG, "Position " + position);
    }


}

