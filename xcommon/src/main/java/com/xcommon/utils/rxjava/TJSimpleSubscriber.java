package com.xcommon.utils.rxjava;

import rx.Subscriber;

/**
 * Created by Photostsrs on 2017/3/3.
 */

public abstract class TJSimpleSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public abstract void onNext(T t);
}
