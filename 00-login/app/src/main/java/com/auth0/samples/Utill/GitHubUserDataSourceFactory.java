package com.auth0.samples.Utill;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.content.Context;

import java.util.concurrent.Executor;

/**
 * Created by ayush on 19/3/18.
 */

public class GitHubUserDataSourceFactory implements DataSource.Factory {

    MutableLiveData<ItemKeyedUserDataSource> mutableLiveData;
    ItemKeyedUserDataSource itemKeyedUserDataSource;
    Executor executor;
    Context context;

    public GitHubUserDataSourceFactory(Executor executor, Context context) {
        this.mutableLiveData = new MutableLiveData<ItemKeyedUserDataSource>();
        this.executor = executor;
        this.context = context;
    }

    @Override
    public DataSource create() {
        itemKeyedUserDataSource = new ItemKeyedUserDataSource(executor,context);
        mutableLiveData.postValue(itemKeyedUserDataSource);
        return itemKeyedUserDataSource;
    }

    public MutableLiveData<ItemKeyedUserDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

}
