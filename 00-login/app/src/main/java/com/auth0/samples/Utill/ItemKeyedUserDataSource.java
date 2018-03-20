package com.auth0.samples.Utill;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.auth0.samples.Activity.HomeActivity;
import com.auth0.samples.Api.GitHubApi;
import com.auth0.samples.Api.GitHubService;
import com.auth0.samples.Model.User;
import com.auth0.samples.Model.UserModel;
import com.auth0.samples.Storage.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ayush on 19/3/18.
 */

public class ItemKeyedUserDataSource extends ItemKeyedDataSource<Long, User> {
    public static final String TAG = "ItemKeyedUserDataSource";
    GitHubService gitHubService;
    ItemKeyedDataSource.LoadInitialParams<Long> initialParams;
    LoadParams<Long> afterParams;
    private MutableLiveData networkState;
    private MutableLiveData initialLoading;
    private Executor retryExecutor;
    private UserModel user;
    private DatabaseHelper databaseHelper;
    private Context context;
    public static int count;

    public ItemKeyedUserDataSource(Executor retryExecutor, Context context) {
        gitHubService = GitHubApi.createGitHubService();
        networkState = new MutableLiveData();
        initialLoading = new MutableLiveData();
        this.context = context;
        this.retryExecutor = retryExecutor;
    }


    public MutableLiveData getNetworkState() {
        return networkState;
    }

    public MutableLiveData getInitialLoading() {
        return initialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<User> callback) {
        Log.i(TAG, "Loading Range initial " + 1 + " Count " + params.requestedLoadSize+ "count"+count);
        List<User> gitHubUser = new ArrayList();
        databaseHelper = new DatabaseHelper(context);
        initialParams = params;
        initialLoading.postValue(NetworkState.LOADING);
        networkState.postValue(NetworkState.LOADING);
        gitHubService.getUser(1, params.requestedLoadSize).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    gitHubUser.addAll(response.body());
                    count=count+gitHubUser.size();
                    user = new UserModel();
                    for(int i=0;i<gitHubUser.size();i++){
                        Log.d("add initial_______" , gitHubUser.get(i).firstName);
                        user.setUserName(gitHubUser.get(i).firstName);
                        user.setUserCode(gitHubUser.get(i).userId);
                        databaseHelper.addUser(user);
                    }
                    callback.onResult(gitHubUser);
                    initialLoading.postValue(NetworkState.LOADED);
                    networkState.postValue(NetworkState.LOADED);
                    initialParams = null;
                } else {
                    Log.e("API CALL", response.message());
                    initialLoading.postValue(new NetworkState(Status.FAILED, response.message()));
                    networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                String errorMessage;
                errorMessage = t.getMessage();
                if (t == null) {
                    errorMessage = "unknown error";
                }
                networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
            }
        });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {
        Log.i(TAG, "Loading Range " + params.key + " Count " + params.requestedLoadSize+ "count"+count);
        if(count<201) {
            List<User> gitHubUser = new ArrayList();
            databaseHelper = new DatabaseHelper(context);
            afterParams = params;

            networkState.postValue(NetworkState.LOADING);
            gitHubService.getUser(Math.toIntExact(params.key), params.requestedLoadSize).enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (response.isSuccessful()) {
                        gitHubUser.addAll(response.body());
                        count = count+gitHubUser.size();
                        user = new UserModel();
                        for (int i = 0; i < gitHubUser.size(); i++) {
                            Log.d("add after_______", gitHubUser.get(i).firstName);
                            user.setUserName(gitHubUser.get(i).firstName);
                            user.setUserCode(gitHubUser.get(i).userId);
                            databaseHelper.addUser(user);
                        }
                        callback.onResult(gitHubUser);
                        networkState.postValue(NetworkState.LOADED);
                        afterParams = null;
                    } else {
                        networkState.postValue(new NetworkState(Status.FAILED, response.message()));
                        Log.e("API CALL", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    String errorMessage;
                    errorMessage = t.getMessage();
                    if (t == null) {
                        errorMessage = "unknown error";
                    }
                    networkState.postValue(new NetworkState(Status.FAILED, errorMessage));
                }
            });
        }
        else{

            HomeActivity.getDataFromSQLite();

        }

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<User> callback) {

    }
    @NonNull
    @Override
    public Long getKey(@NonNull User item) {
        return item.userId;
    }

}
