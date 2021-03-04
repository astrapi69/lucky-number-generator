package de.alpharogroup.android.lucky_number_generator.extensions

import de.alpharogroup.random.SecureRandomBean
import de.alpharogroup.random.SecureRandomBuilder
import java.security.Provider
import java.security.SecureRandom
import java.security.Security
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Extensions {

    fun getProviderName(): String {
        val sha1prng = "SecureRandom.SHA1PRNG"
        val secureRandomProviders: Array<Provider> = Security.getProviders(sha1prng)
        if(!secureRandomProviders.isEmpty()) {
            return secureRandomProviders[0].name
        }
        return "AndroidOpenSSL"
    }

    fun newSecureRandom(currentDrawDate: Date): SecureRandom {
        val providerName = getProviderName()
        val secureRandom: SecureRandom = SecureRandomBuilder
            .getInstance(
                SecureRandomBean.DEFAULT_ALGORITHM,
                providerName,
                currentDrawDate.time
            ).build()
        return secureRandom
    }

    @Throws(ParseException::class)
    fun parseToDate(date: String, format: String): Date {
        return SimpleDateFormat(format).parse(date)
    }
}