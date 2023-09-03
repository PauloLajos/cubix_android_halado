package hu.paulolajos.mvvmwithcleanarchitecture

class AppModule {
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }

    @Provides
    fun provideLoginUseCase(userRepository: UserRepository): LoginUseCase {
        return LoginUseCase(userRepository)
    }
}

// AppComponent.kt
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(loginActivity: LoginActivity)
}