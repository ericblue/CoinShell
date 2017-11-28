package io.coinshell.rest;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.body.RequestBodyEntity;
import io.coinshell.exception.RestClientException;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Data
public class RestClient {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private static Format DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private HttpClient httpClient;
    private ObjectMapper mapper;
    private String baseUrl;
    private String password;
    private String userHash;
    private String username;


    public RestClient(RestClientOptions options) {


        // If we want to proxy set our own custom HTTP client for UniRest to use
        if (options.getUseProxy()) {

            if (isProxyAvailable(options.getProxyHost(), options.getProxyPort())) {
                HttpHost proxy = new HttpHost(options.getProxyHost(), options.getProxyPort());
                this.httpClient = HttpClientBuilder.create().setProxy(proxy).build();
                Unirest.setHttpClient(httpClient);
            }
        }

        // Setup Jackson to know how to serialize custom objects/modules
        this.mapper = new ObjectMapper();
        //this.mapper.registerModule(DateTimeModule.create());


        this.username = options.getUsername();
        this.password = options.getPassword();
        this.userHash = options.getUserHash();

        if (StringUtils.isNotBlank(options.getBaseUrl())) {
            this.baseUrl = options.getBaseUrl();
        } else {
            this.baseUrl = new String();
        }

    }

    private Boolean isProxyAvailable(String host, Integer port) {

        Socket socket = null;

        try {
            socket = new Socket(host, port);
        } catch (UnknownHostException e) {
            logger.error("is Proxy Available error: " + e);
            return false;
        } catch (IOException e2) {
            logger.error("is Proxy Available error: " + e2);
            return false;
        }

        try {
            socket.close();
        } catch (IOException e) {
            logger.error("is Proxy Available error: " + e);
            return false;
        }

        return true;


    }


    public Object makeRequest(RestOperation op, String url, Object requestObject, Object responseObjectType) throws RestClientException {

        Object responseData = null;
        HttpResponse<String> body = null;
        RequestBodyEntity postResponse;
        GetRequest getResponse;


        String requestUrl = this.baseUrl + url;

        try {

            if (op == RestOperation.GET) {

                // Don't use basic auth if values are NULL

                if (StringUtils.isNotEmpty(this.username)) {

                    getResponse = Unirest.get(requestUrl)
                            .basicAuth(this.username, this.password)
                            .header("Accept-Encoding", "");

                } else {
                    getResponse = Unirest.get(requestUrl)
                            .header("Accept-Encoding", "");
                }


                body = getResponse.asString();


            } else if (op == RestOperation.POST) {


                if (StringUtils.isNotEmpty(this.username)) {
                    postResponse = (RequestBodyEntity) Unirest.post(requestUrl)

                            .header("Content-Type", "application/json")
                            .header("Accept-Encoding", "")
                            .basicAuth(this.username, this.password)
                            .body(objectToJson(requestObject));

                } else {
                    postResponse = (RequestBodyEntity) Unirest.post(requestUrl)

                            .header("Content-Type", "application/json")
                            .header("Accept-Encoding", "")
                            .body(objectToJson(requestObject));
                }


                body = postResponse.asString();

            } else {
                throw new RestClientException("Unsupported operation");
            }


            try {

                String json = IOUtils.toString(body.getRawBody(), "UTF-8");
                logger.debug("Successfully recieved json = " + json);

                if (body.getStatus() != 200) {
                    HTTPStatus status = HTTPStatus.fromCode(body.getStatus());
                    throw new RestClientException("Error: Expected status 200, got " + status + "(" + status.getCode() + "), Json = " + json);
                }


                // Dynamcially handle response in a number of ways
                // If TypeReference, use Jackson to serialize as a complex HashMap or ArrayList
                // If Class, serialize as the POJO/domain object
                // If String, return raw JSON string to be processed manually with something like JSONPath

                if (responseObjectType != null) {

                    if (responseObjectType instanceof TypeReference) {
                        responseData = mapper.readValue(json, (TypeReference<?>) responseObjectType);

                    } else if (responseObjectType instanceof Class) {
                        responseData = mapper.readValue(json, (Class<?>) responseObjectType);
                    } else if (responseObjectType instanceof String) {
                        responseData = json;
                    } else {
                        throw new RestClientException(
                                "Return data expected object must be Class or TypeReference for Lists");
                    }

                }


            } catch (IOException e) {
                throw new RestClientException(e);
            }

        } catch (UnirestException e) {
            throw new RestClientException(e);
        }

        return responseData;

    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void handleError(String json) throws RestClientException {

        logger.debug("Handling error");

        Map<String, Map> map = new HashMap<String, Map>();

        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(json, HashMap.class);
            Map<String, String> errorMessage = map.get("error");
            throw new RestClientException("Rest Client error: " + errorMessage.get("message"));
        } catch (JsonParseException e) {
            throw new RestClientException(e);
        } catch (JsonMappingException e) {
            throw new RestClientException(e);
        } catch (IOException e) {
            throw new RestClientException(e);
        }

    }

    private static String objectToJson(Object obj) {

        ObjectMapper mapper = new ObjectMapper();
        //mapper.registerModule(DateTimeModule.create());

        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage());
        } catch (JsonMappingException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return json;

    }
}
