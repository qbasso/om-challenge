package pl.qbasso.omisechallenge.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pl.qbasso.omisechallenge.BuildConfig
import pl.qbasso.omisechallenge.api.ChallengeApi
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.CertificateException
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
class NetworkModule {

    private val unsafeOkHttpClient: OkHttpClient
        get() {
            try {
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) = Unit

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) = Unit

                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> = arrayOf()
                })

                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                val sslSocketFactory = sslContext.socketFactory

                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier { _, _ -> true }
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
                builder.retryOnConnectionFailure(true)
                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }

    @Provides
    @Singleton
    internal fun provideGsonConverter(): Converter.Factory {
        val gson = GsonBuilder().create()
        return GsonConverterFactory
                .create(gson)
    }

    @Provides
    @Singleton
    internal fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.BASIC
        return logging
    }

    @Provides
    @Singleton
    internal fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            unsafeOkHttpClient
        } else {
            val httpClient = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
            httpClient.build()
        }
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(okHttpClient: OkHttpClient,
                                 converter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.API_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(converter)
                .build()
    }

    @Provides
    @Singleton
    internal fun provideApi(retrofit: Retrofit): ChallengeApi = retrofit.create(ChallengeApi::class.java)
}
