package com.example.falcone_finder.di.modules

import android.R
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.falcone_finder.business.data.network.implementation.FalconeNetworkDataSourceImpl
import com.example.falcone_finder.framework.datasource.remote.FalconeApiService
import com.example.falcone_finder.framework.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {


    // provide glide instance.
    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions.placeholderOf(R.drawable.alert_dark_frame)
            .error(R.drawable.alert_dark_frame)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        application: Application,
        requestOptions: RequestOptions
    ): RequestManager {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }


    @Singleton
    @Provides
    fun getFalconePreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.FalconeAppPref, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun getApiSource(retrofit: Retrofit): FalconeApiService {
        return retrofit.create(FalconeApiService::class.java)
    }
}