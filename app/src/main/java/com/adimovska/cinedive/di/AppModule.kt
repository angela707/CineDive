package com.adimovska.cinedive.di

import android.content.Context
import androidx.room.Room
import com.adimovska.cinedive.data.local.MovieDatabase
import com.adimovska.cinedive.data.remote.AppConfig.BASE_URL
import com.adimovska.cinedive.data.remote.AuthInterceptor
import com.adimovska.cinedive.data.remote.MovieApi
import com.adimovska.cinedive.data.repository.MediaRepositoryImpl
import com.adimovska.cinedive.domain.repository.MediaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor =
            HttpLoggingInterceptor { message -> Timber.d("HttpCall $message") }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun okHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MovieApi {
        return retrofit.create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMediaRepository(
        movieApi: MovieApi,
        movieDatabase: MovieDatabase,
    ): MediaRepository {
        return MediaRepositoryImpl(
            api = movieApi,
            movieDatabase = movieDatabase
        )
    }
}