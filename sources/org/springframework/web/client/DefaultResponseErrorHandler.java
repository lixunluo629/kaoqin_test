package org.springframework.web.client;

import java.io.IOException;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.FileCopyUtils;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/DefaultResponseErrorHandler.class */
public class DefaultResponseErrorHandler implements ResponseErrorHandler {
    @Override // org.springframework.web.client.ResponseErrorHandler
    public boolean hasError(ClientHttpResponse response) throws IOException {
        int rawStatusCode = response.getRawStatusCode();
        for (HttpStatus statusCode : HttpStatus.values()) {
            if (statusCode.value() == rawStatusCode) {
                return hasError(statusCode);
            }
        }
        return hasError(rawStatusCode);
    }

    protected boolean hasError(HttpStatus statusCode) {
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    protected boolean hasError(int unknownStatusCode) {
        int seriesCode = unknownStatusCode / 100;
        return seriesCode == HttpStatus.Series.CLIENT_ERROR.value() || seriesCode == HttpStatus.Series.SERVER_ERROR.value();
    }

    @Override // org.springframework.web.client.ResponseErrorHandler
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = getHttpStatusCode(response);
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw new HttpClientErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
            case SERVER_ERROR:
                throw new HttpServerErrorException(statusCode, response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
            default:
                throw new UnknownHttpStatusCodeException(statusCode.value(), response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
        }
    }

    protected HttpStatus getHttpStatusCode(ClientHttpResponse response) throws IOException {
        try {
            return response.getStatusCode();
        } catch (IllegalArgumentException e) {
            throw new UnknownHttpStatusCodeException(response.getRawStatusCode(), response.getStatusText(), response.getHeaders(), getResponseBody(response), getCharset(response));
        }
    }

    protected byte[] getResponseBody(ClientHttpResponse response) {
        try {
            return FileCopyUtils.copyToByteArray(response.getBody());
        } catch (IOException e) {
            return new byte[0];
        }
    }

    protected Charset getCharset(ClientHttpResponse response) {
        HttpHeaders headers = response.getHeaders();
        MediaType contentType = headers.getContentType();
        if (contentType != null) {
            return contentType.getCharset();
        }
        return null;
    }
}
