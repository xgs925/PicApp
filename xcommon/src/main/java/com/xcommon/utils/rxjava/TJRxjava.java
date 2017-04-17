package com.xcommon.utils.rxjava;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Photostsrs on 2017/3/3.
 */

public class TJRxjava {
    public static <T> Observable<T> create(Observable.OnSubscribe<T> f){
        return  Observable.create(f).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
