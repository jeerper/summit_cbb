package com.summit.service;

import com.summit.entity.LockInfo;
import com.summit.entity.LockRequest;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Query;
import rx.Observable;

public interface UBLockService {
    @POST("unlock.jhtml")
    Observable<LockInfo> unLock(@Body LockRequest lockRequest);


    @POST("lockstatus.jhtml")
    Observable<LockInfo> queryLockStatus(@Body LockRequest lockRequest);


}
