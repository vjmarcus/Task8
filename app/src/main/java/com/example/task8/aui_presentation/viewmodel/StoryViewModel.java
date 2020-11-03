package com.example.task8.aui_presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.task8.R;
import com.example.task8.aui_presentation.view.MainActivity;
import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.model.Source;
import com.example.task8.data.model.Story;
import com.example.task8.data.model.StoryResponse;
import com.example.task8.utils.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "MyApp";
    private StoryInteractor storyInteractor;
    private MutableLiveData<List<Story>> viewModelLiveData = new MutableLiveData<>();
    private String searchKey;
    private List<Story> storyList = new ArrayList<>();
    private CompositeDisposable disposable;


    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
        getDataFromInter(searchKey);
    }

    public void getDataFromInter(String searchKey) {
        storyList.clear();
        storyInteractor.getDataFromRepo(searchKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Story>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<Story> stories) {
                        viewModelLiveData.setValue(stories);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public LiveData<List<Story>> getViewModelLiveData() {
        return viewModelLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        // Disposable.dispose();
        // Отписать Rx
    }

    //> К интерактору, он возвращает Обсерваил лист стори, и тут я Обсервлю обсервл
    // Все что в репозитории, делаю в ВьюМодел
    // Паблик метод ГетЛайвДата, который возвращает пустой геттре, саму лайвДату
    // Данные которые в Юай неизменны должны быть
    // А данные в лайвДату писать вторым методом,
    // 1. Репозиторий - в методе проверяет, прошла ли минута. Проверили. Тогда идем в сеть с ретро
    // Если не прошла - идем в рум.
    // 2. Результат в репозитории Обсервабл или Сингл лист стори.
    // 3. Возвращаем в интерактор простым методом. гет результат.
    // 4. ВьюМодель запрашивает данные у Интерактора через метод гетНьюсБайКей *(с парпамеипрм)
    // 5. Применяем операторы в Интеракторе, в методе он некст сет велью, все что пришло - в лайДату
    // 6. Активити в креате дергает вьюМодел, гет лайв дата, обсерв, нью обсервер, стандартный метод
    // 7. подписка на лайвДату должна быть одна, а запрос данных где угодно, запрос новых новостей
    // 8. Отписаться от обсервалб , во вьюмодел метод ОнДейстрой, чтобы отписаться, добавить в диспозабл
    // или компос диспозабл, и отписывается в дестрое
}


