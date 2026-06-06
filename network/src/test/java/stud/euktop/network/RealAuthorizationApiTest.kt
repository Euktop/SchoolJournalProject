package stud.euktop.network

import com.schooljournal.model.LoginRequest
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import stud.euktop.network.interceptor.TokenProvider

class RealAuthorizationApiTest {
    private val baseUrl = "https://192.168.0.109:7191"
    private var token: String? = null
    private val tokenProvider = TokenProvider { token }
    private val networkConfig = NetworkConfig(baseUrl)
    private val client = NetworkClient(tokenProvider, networkConfig)

    @Test
    fun `login with correct credentials returns token`() = runBlocking {
        val request = LoginRequest("user@example.com1", "string")
        val response = client.authorizationApi().apiAuthorizationLoginPost(request)
        assertNotNull(response.token)
        assertTrue(response.token!!.isNotEmpty())
    }
}