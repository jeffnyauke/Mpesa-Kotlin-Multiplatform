package io.piestack.multiplatform.mpesa.common.api

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpRequest
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlin.test.assertEquals

class TodoApiMockEngine {
    private lateinit var mockResponse: MockResponse
    private var lastRequest: MockHttpRequest? = null

    fun enqueueMockResponse(
        endpointSegment: String,
        responseBody: String,
        httpStatusCode: Int = 200
    ) {
        mockResponse = MockResponse(endpointSegment, responseBody, httpStatusCode)
    }

    fun get() = MockEngine {
        lastRequest = this

        when (url.encodedPath) {
            "${mockResponse.endpointSegment}" -> {
                MockHttpResponse(
                    call,
                    HttpStatusCode.fromValue(mockResponse.httpStatusCode),
                    ByteReadChannel(mockResponse.responseBody.toByteArray(Charsets.UTF_8)),
                    headersOf(HttpHeaders.ContentType to listOf(ContentType.Application.Json.toString()))
                )
            }
            else -> {
                error("Unhandled ${url.fullPath}")
            }
        }
    }

    fun verifyRequestContainsHeader(key: String, expectedValue: String) {
        val value = lastRequest!!.headers[key]
        assertEquals(expectedValue, value)
    }

    fun verifyRequestBody(addTaskRequest: String) {
        val body = (lastRequest!!.content as TextContent).text

        assertEquals(addTaskRequest, body)
    }

    fun verifyGetRequest() {
        assertEquals(HttpMethod.Get.value, lastRequest!!.method.value)
    }

    fun verifyPostRequest() {
        assertEquals(HttpMethod.Post.value, lastRequest!!.method.value)
    }

    fun verifyPutRequest() {
        assertEquals(HttpMethod.Put.value, lastRequest!!.method.value)
    }

    fun verifyDeleteRequest() {
        assertEquals(HttpMethod.Delete.value, lastRequest!!.method.value)
    }
}