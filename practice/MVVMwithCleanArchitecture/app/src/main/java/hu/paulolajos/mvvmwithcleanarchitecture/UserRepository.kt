package hu.paulolajos.mvvmwithcleanarchitecture

class UserRepository(private val userRepository: UserRepository) {
    suspend fun login(username: String, password: String): LoginResult {
        // Perform business logic and data operations
        val user = userRepository.getUserByUsername(username)

        return if (user != null && user.password == password) {
            LoginResult(success = true, user)
        } else {
            LoginResult(success = false, error = "Invalid username or password")
        }
    }
}