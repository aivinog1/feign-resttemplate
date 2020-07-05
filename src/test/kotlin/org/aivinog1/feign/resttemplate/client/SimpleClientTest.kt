package org.aivinog1.feign.resttemplate.client

import feign.Client
import feign.Feign
import feign.Param
import feign.Request
import feign.RequestLine
import feign.Response
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import java.io.IOException

/**
 * @todo #8 Remove this test when starting to implement the main feature (RestTemplate client for Feign).
 *  For now, this is a naive implementation of this
 */
@Disabled
class SimpleClientTest {
    @Test
    fun shouldWorkWithClient() {
        val testService = Feign.builder()
            .decoder(JacksonDecoder())
            .client(RestTemplateFeignClient())
            .target(
                TestService::class.java,
                "http://70739.mocklab.io"
            )
        val testResult = testService.test("54", "45")
        println(testResult)
    }
}

internal class RestTemplateFeignClient : Client {
    @Throws(IOException::class)
    override fun execute(request: Request, options: Request.Options): Response {
//        return new Client.Default(null, null).execute(request, options);
        val result = RestTemplate().exchange(
            request.url(),
            HttpMethod.GET,
            null,
            ByteArray::class.java
        )
        return Response
            .builder()
            .body(result.body)
            .status(result.statusCodeValue)
            .request(request)
            .build()
    }
}

internal interface TestService {
    @RequestLine("GET /coordinates?latitude={latitude}&longitude={longitude}")
    fun test(
        @Param("latitude") latitude: String?,
        @Param("longitude") longitude: String?
    ): Map<String, Any>
}