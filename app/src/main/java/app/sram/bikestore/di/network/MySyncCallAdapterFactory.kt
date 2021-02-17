package app.sram.bikestore.di.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class MySyncCallAdapterFactory @Inject constructor() : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<out Annotation>, retrofit: Retrofit): CallAdapter<Any, Any> {
        return MyCallAdapter(returnType)
    }
}

class MyCallAdapter(private val returnType: Type) : CallAdapter<Any, Any> {
    override fun responseType(): Type {
        return returnType
    }

    override fun adapt(call: Call<Any>): Any? {
        return call.execute().body()
    }
}
