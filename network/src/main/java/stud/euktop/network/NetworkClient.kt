package stud.euktop.network

import com.schooljournal.api.*
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.infrastructure.Serializer
import stud.euktop.network.interceptor.AuthTokenInterceptor
import stud.euktop.network.interceptor.TokenProvider
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkClient @Inject constructor(
    tokenProvider: TokenProvider,
    networkConfig: NetworkConfig
) {
    internal val moshi: Moshi = Serializer.moshiBuilder.build()

    private val okHttpClient: OkHttpClient =
        getUnsafeOkHttpClient(tokenProvider)

    private fun getUnsafeOkHttpClient(tokenProvider: TokenProvider): OkHttpClient {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory

        return OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(AuthTokenInterceptor(tokenProvider))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val baseUrl: String = networkConfig.baseUrl
    internal fun testApi(): TestApi =
        TestApi(basePath = baseUrl, client = okHttpClient)

    // внутри класса NetworkClient
    fun authorizationApi(): AuthorizationApi = AuthorizationApi(baseUrl, okHttpClient)
    fun classesApi(): ClassesApi = ClassesApi(baseUrl, okHttpClient)
    fun homeworkApi(): HomeworkApi = HomeworkApi(baseUrl, okHttpClient)
    fun lessonsApi(): LessonsApi = LessonsApi(baseUrl, okHttpClient)
    fun schoolsApi(): SchoolsApi = SchoolsApi(baseUrl, okHttpClient)
    fun studentApi(): StudentApi = StudentApi(baseUrl, okHttpClient)
    fun subjectsApi(): SubjectsApi = SubjectsApi(baseUrl, okHttpClient)
    fun teacherAssignmentsApi(): TeacherAssignmentsApi =
        TeacherAssignmentsApi(baseUrl, okHttpClient)

    fun usersApi(): UsersApi = UsersApi(baseUrl, okHttpClient)
    fun auditApi(): AuditApi = AuditApi(baseUrl, okHttpClient)
    fun rolesApi(): RolesApi = RolesApi(baseUrl, okHttpClient)
    fun absenceTypesApi(): AbsenceTypesApi = AbsenceTypesApi(baseUrl, okHttpClient)
}