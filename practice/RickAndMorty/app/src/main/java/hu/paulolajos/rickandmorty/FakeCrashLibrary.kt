package hu.paulolajos.rickandmorty

class FakeCrashLibrary {
    companion object {
        fun log(priority: Int, tag: String?, message: String?) {
            // TODO add log entry to circular buffer.
        }

        fun logWarning(t: Throwable?) {
            // TODO report non-fatal warning.
        }

        fun logError(t: Throwable?) {
            // TODO report non-fatal error.
        }

        private fun FakeCrashLibrary(): Unit = throw AssertionError("No instances.")
    }
}
