package org.springframework.web.client;

import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;

/* loaded from: spring-web-4.3.25.RELEASE.jar:org/springframework/web/client/AsyncRestOperations.class */
public interface AsyncRestOperations {
    RestOperations getRestOperations();

    <T> ListenableFuture<ResponseEntity<T>> getForEntity(String str, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> getForEntity(String str, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> getForEntity(URI uri, Class<T> cls) throws RestClientException;

    ListenableFuture<HttpHeaders> headForHeaders(String str, Object... objArr) throws RestClientException;

    ListenableFuture<HttpHeaders> headForHeaders(String str, Map<String, ?> map) throws RestClientException;

    ListenableFuture<HttpHeaders> headForHeaders(URI uri) throws RestClientException;

    ListenableFuture<URI> postForLocation(String str, HttpEntity<?> httpEntity, Object... objArr) throws RestClientException;

    ListenableFuture<URI> postForLocation(String str, HttpEntity<?> httpEntity, Map<String, ?> map) throws RestClientException;

    ListenableFuture<URI> postForLocation(URI uri, HttpEntity<?> httpEntity) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> postForEntity(String str, HttpEntity<?> httpEntity, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> postForEntity(String str, HttpEntity<?> httpEntity, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> postForEntity(URI uri, HttpEntity<?> httpEntity, Class<T> cls) throws RestClientException;

    ListenableFuture<?> put(String str, HttpEntity<?> httpEntity, Object... objArr) throws RestClientException;

    ListenableFuture<?> put(String str, HttpEntity<?> httpEntity, Map<String, ?> map) throws RestClientException;

    ListenableFuture<?> put(URI uri, HttpEntity<?> httpEntity) throws RestClientException;

    ListenableFuture<?> delete(String str, Object... objArr) throws RestClientException;

    ListenableFuture<?> delete(String str, Map<String, ?> map) throws RestClientException;

    ListenableFuture<?> delete(URI uri) throws RestClientException;

    ListenableFuture<Set<HttpMethod>> optionsForAllow(String str, Object... objArr) throws RestClientException;

    ListenableFuture<Set<HttpMethod>> optionsForAllow(String str, Map<String, ?> map) throws RestClientException;

    ListenableFuture<Set<HttpMethod>> optionsForAllow(URI uri) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls, Object... objArr) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls, Map<String, ?> map) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> cls) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Object... objArr) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(String str, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference, Map<String, ?> map) throws RestClientException;

    <T> ListenableFuture<ResponseEntity<T>> exchange(URI uri, HttpMethod httpMethod, HttpEntity<?> httpEntity, ParameterizedTypeReference<T> parameterizedTypeReference) throws RestClientException;

    <T> ListenableFuture<T> execute(String str, HttpMethod httpMethod, AsyncRequestCallback asyncRequestCallback, ResponseExtractor<T> responseExtractor, Object... objArr) throws RestClientException;

    <T> ListenableFuture<T> execute(String str, HttpMethod httpMethod, AsyncRequestCallback asyncRequestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> map) throws RestClientException;

    <T> ListenableFuture<T> execute(URI uri, HttpMethod httpMethod, AsyncRequestCallback asyncRequestCallback, ResponseExtractor<T> responseExtractor) throws RestClientException;
}
