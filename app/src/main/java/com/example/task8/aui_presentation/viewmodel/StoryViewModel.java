package com.example.task8.aui_presentation.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.task8.R;
import com.example.task8.business_domain.StoryInteractor;
import com.example.task8.data.model.Story;

import java.util.List;

public class StoryViewModel extends ViewModel {

    private static final String TAG = "MyApp";
    private StoryInteractor storyInteractor;
    private LiveData<List<Story>> viewModelLiveData;

    public StoryViewModel(StoryInteractor storyInteractor) {
        this.storyInteractor = storyInteractor;
        viewModelLiveData  = getStoryFromInter("software");
    }

    public LiveData<List<Story>> getStoryFromInter(String key) {
        return storyInteractor.getInterLiveData();
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


