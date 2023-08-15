package hu.paulolajos.rickandmortydemo.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.paulolajos.rickandmortydemo.data.local.AppDatabase
import hu.paulolajos.rickandmortydemo.data.local.CharacterDao
import hu.paulolajos.rickandmortydemo.data.remote.CharacterRemoteDataSource
import hu.paulolajos.rickandmortydemo.data.remote.CharacterService
import hu.paulolajos.rickandmortydemo.data.repository.CharacterRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson) : Retrofit = Retrofit.Builder()
        .baseUrl("https://rickandmortyapi.com/api/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCharacterService(retrofit: Retrofit): CharacterService = retrofit.create(CharacterService::class.java)

    @Singleton
    @Provides
    fun provideCharacterRemoteDataSource(characterService: CharacterService) = CharacterRemoteDataSource(characterService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) = AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.characterDao()

    @Singleton
    @Provides
    fun provideRepository(remoteDataSource: CharacterRemoteDataSource,
                          localDataSource: CharacterDao
    ) =
        CharacterRepository(remoteDataSource, localDataSource)
}