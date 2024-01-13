package com.pratiksymz.tracker_data.repository

import com.pratiksymz.tracker_data.remote.OpenFoodAPI
import com.google.common.truth.Truth.assertThat
import com.pratiksymz.tracker_data.remote.malformedFoodResponse
import com.pratiksymz.tracker_data.remote.validFoodResponse
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class TrackerRepositoryImplTest {

    private lateinit var repository: TrackerRepositoryImpl
    private lateinit var mockWebServer: MockWebServer
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var api: OpenFoodAPI


    @Before
    fun setUp() {
        // Runs a local web server and provides a base url for that
        mockWebServer = MockWebServer()
        okHttpClient = OkHttpClient.Builder()
            .writeTimeout(1, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.SECONDS)
            .connectTimeout(1, TimeUnit.SECONDS)
            .build()

        api = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(mockWebServer.url("/"))
            .build()
            .create(OpenFoodAPI::class.java)

        repository = TrackerRepositoryImpl(
            dao = mockk(relaxed = true),        // No need to test rn
            api = api
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Search foods with a valid response and returns results`() = runBlocking {
        // Enqueue mock response to the web server
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(validFoodResponse)

        )
        // Make the API call
        val result = repository.searchFood(
            "banana", 1, 40
        )

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Search foods with an invalid response and returns failure`() = runBlocking {
        // Enqueue mock response to the web server
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(403)
                .setBody(validFoodResponse)

        )
        // Make the API call
        val result = repository.searchFood(
            "banana", 1, 40
        )

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Search foods with a malformed (invalid) response and returns failure`() = runBlocking {
        // Enqueue mock response to the web server
        mockWebServer.enqueue(
            MockResponse()
                .setBody(malformedFoodResponse)

        )
        // Make the API call
        val result = repository.searchFood(
            "banana", 1, 40
        )

        assertThat(result.isFailure).isTrue()
    }
}