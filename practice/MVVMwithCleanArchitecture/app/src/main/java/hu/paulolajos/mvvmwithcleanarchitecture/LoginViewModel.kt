package hu.paulolajos.mvvmwithcleanarchitecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginViewModelprivate val loginUseCase: LoginUseCase) : ViewModel() {
    private val loginResult = MutableLiveData<LoginResult>()

    fun getLoginResult(): LiveData<LoginResult> = loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginUseCase.login(username, password)
            loginResult.value = result
        }
    }
}