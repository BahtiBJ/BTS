package com.bb.bts.ui;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.bb.bts.db.RepositoryBalance;
import com.bb.bts.models.UIBalance;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainViewModel extends ViewModel {

    public MainViewModel(
            RepositoryBalance myRepository
    ) {
        repository = myRepository;
    }

    static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class,
            creationExtras -> {
                Application app = creationExtras.get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY);
                assert app != null;
                return new MainViewModel(RepositoryBalance.getInstance(app));
            }
    );


    private MutableLiveData<UIBalance> liveBalanceList = new MutableLiveData();
    private RepositoryBalance repository;
    private ExecutorService executorService = Executors.newFixedThreadPool(2);


    public LiveData<UIBalance> getLiveBalanceList() {
        return liveBalanceList;
    }

    public void getBalance() {
        executorService.execute(() -> {
            liveBalanceList.postValue(repository.getBalance());
        });
    }

    public void updateBalance(UIBalance uiBalance){
        executorService.execute(() -> {
            repository.update(uiBalance);
        });
    }

    @Override
    protected void onCleared() {
        executorService.shutdown();
        super.onCleared();
    }
}
