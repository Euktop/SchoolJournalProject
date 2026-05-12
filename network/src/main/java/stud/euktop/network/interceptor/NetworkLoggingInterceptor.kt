package stud.euktop.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import stud.euktop.network.util.logger
import java.io.IOException

class NetworkLoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        // Логируем запрос
        logRequest(request)

        val response = try {
            chain.proceed(request)
        } catch (e: IOException) {
            logError(request, e)
            throw e
        }

        val durationMs = (System.nanoTime() - startNs) / 1_000_000
        logResponse(response, durationMs)

        return response
    }

    private fun logRequest(request: okhttp3.Request) {
        val method = request.method
        val url = request.url
        val headers = request.headers
        val body = request.body?.let { bodyToString(request) } ?: ""

        logger?.d(
            tag = "NETWORK_REQUEST",
            action = method,
            data = "URL: $url\nHeaders: $headers\nBody: $body"
        )
    }

    private fun logResponse(response: okhttp3.Response, durationMs: Long) {
        val code = response.code
        val message = response.message
        val url = response.request.url
        val body = response.peekBody(Long.MAX_VALUE).string()

        val data = "URL: $url\nCode: $code ($message)\nDuration: ${durationMs}ms\nBody: $body"
        if (code in 200..299) {
            logger?.i(tag = "NETWORK_RESPONSE", action = "SUCCESS", data = data)
        } else {
            logger?.e(tag = "NETWORK_RESPONSE", action = "ERROR", data = data)
        }
    }

    private fun logError(request: okhttp3.Request, exception: IOException) {
        logger?.e(
            tag = "NETWORK_ERROR",
            action = request.method,
            throwable = exception,
            data = "URL: ${request.url}"
        )
    }

    private fun bodyToString(request: okhttp3.Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = okio.Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "Failed to log request body: ${e.message}"
        }
    }
}