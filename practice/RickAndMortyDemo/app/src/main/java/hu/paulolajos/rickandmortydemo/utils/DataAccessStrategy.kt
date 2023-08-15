package hu.paulolajos.rickandmortydemo.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import hu.paulolajos.rickandmortydemo.utils.Resource as MyResource
import kotlinx.coroutines.Dispatchers

fun <T, A> performGetOperation(databaseQuery: () -> LiveData<T>,
                               networkCall: suspend () -> MyResource<A>,
                               saveCallResult: suspend (A) -> Unit): LiveData<MyResource<T>> =
    liveData(Dispatchers.IO) {
        emit(MyResource.loading())
        val source = databaseQuery.invoke().map { MyResource.success(it) }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == MyResource.Status.SUCCESS) {
            saveCallResult(responseStatus.data!!)

        } else if (responseStatus.status == MyResource.Status.ERROR) {
            emit(MyResource.error(responseStatus.message!!))
            emitSource(source)
        }
    }