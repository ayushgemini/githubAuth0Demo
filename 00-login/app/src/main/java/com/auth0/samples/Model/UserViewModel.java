package com.auth0.samples.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.auth0.samples.Activity.HomeActivity;
import com.auth0.samples.Utill.GitHubUserDataSourceFactory;
import com.auth0.samples.Utill.ItemKeyedUserDataSource;
import com.auth0.samples.Utill.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ayush on 19/3/18.
 */

public class UserViewModel extends ViewModel {

    public LiveData<PagedList<User>> userList;
    public LiveData<NetworkState> networkState;
    Executor executor;
    LiveData<ItemKeyedUserDataSource> tDataSource;


    public UserViewModel() {
        executor = Executors.newFixedThreadPool(5);
        GitHubUserDataSourceFactory githubUserDataSourceFacteory = new GitHubUserDataSourceFactory(executor, HomeActivity.getAppContext());

        tDataSource = githubUserDataSourceFacteory.getMutableLiveData();

        networkState = Transformations.switchMap(githubUserDataSourceFacteory.getMutableLiveData(), dataSource -> {
            return dataSource.getNetworkState();
        });

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(190).build();

        userList = (new LivePagedListBuilder(githubUserDataSourceFacteory, pagedListConfig))
                .setBackgroundThreadExecutor(executor)
                .build();
    }
}