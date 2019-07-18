package com.summit.service.impl;

import com.summit.entity.LockInfo;
import com.summit.entity.LockRequest;
import com.summit.util.HttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import retrofit2.HttpException;
import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

@Slf4j
@Component
public class NBLockServiceImpl {
    @Autowired
    private HttpClient httpClient;

    public LockInfo toUnLock(LockRequest lockRequest) {
        final LockInfo[] backLockInfo = {null};
        httpClient.ubLockService.unLock(lockRequest)
                .flatMap(new Func1<LockInfo, Observable<LockInfo>>() {
                    @Override
                    public Observable<LockInfo> call(LockInfo lockInfo) {
                        log.info("{}" ,lockInfo);
                        backLockInfo[0] = lockInfo;
                        return null;
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<LockInfo>>() {
            @Override
            public Observable<LockInfo> call(Throwable throwable) {
                if (throwable instanceof HttpException) {
                    log.error("httpException");
                    HttpException httpException = (HttpException) throwable;
                    if (httpException.code() == HttpStatus.NOT_FOUND.value()) {
                        log.error("404");
                        return null;
                    }
                }
                return Observable.error(throwable);
            }
        }).subscribe(new Observer<LockInfo>() {
            //最后的业务逻辑
            @Override
            public void onCompleted() {
                log.info("请求完成");
            }
            @Override
            public void onError(Throwable e) {
                log.error("请求失败", e);
            }
            @Override
            public void onNext(LockInfo lockInfo) {
                log.debug("onNext");
            }
        });
        return backLockInfo[0];
    }

    public LockInfo toQueryLockStatus(LockRequest lockRequest) {
        final LockInfo[] queryLockInfo = {null};
//        CountDownLatch count = new CountDownLatch(1);
        httpClient.ubLockService.queryLockStatus(lockRequest)
                .flatMap(new Func1<LockInfo, Observable<LockInfo>>() {
                    @Override
                    public Observable<LockInfo> call(LockInfo lockInfo) {
                        log.info("{}" ,lockInfo);
                        queryLockInfo[0] = lockInfo;
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        return null;
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<LockInfo>>() {
            @Override
            public Observable<LockInfo> call(Throwable throwable) {
                if (throwable instanceof HttpException) {
                    log.error("httpException");
                    HttpException httpException = (HttpException) throwable;
                    if (httpException.code() == HttpStatus.NOT_FOUND.value()) {
                        log.error("404");
                        return null;
                    }
                }
                return Observable.error(throwable);
            }
        }).subscribe(new Observer<LockInfo>() {
            @Override
            public void onCompleted() {
                log.info("请求完成");
            }
            @Override
            public void onError(Throwable e) {
                log.error("请求失败", e);
            }
            @Override
            public void onNext(LockInfo lockInfo) {
                log.debug("onNext");
            }
        });
        return queryLockInfo[0];
    }
}
