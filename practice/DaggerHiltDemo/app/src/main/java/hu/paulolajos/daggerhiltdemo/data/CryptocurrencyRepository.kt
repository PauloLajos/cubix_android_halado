package hu.paulolajos.daggerhiltdemo.data

interface CryptocurrencyRepository {
    fun getCryptoCurrency(): List<Cryptocurrency>
}