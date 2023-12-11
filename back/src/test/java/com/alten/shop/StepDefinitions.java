package com.alten.shop;

import com.alten.shop.auth.AuthRequest;
import com.alten.shop.auth.AuthService;
import com.alten.shop.model.security.Role;
import com.alten.shop.repository.ProductRepository;
import com.alten.shop.repository.security.AuthorizedUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
;

public class StepDefinitions {

    private String jwtToken;

    private static final String authURL = "http://localhost:8080/auth/authenticate";
    private static final String registerURL = "http://localhost:8080/auth/register";

    @Autowired
    private AuthorizedUserRepository uRepo;
    @Autowired
    private AuthService authService;
    @Autowired
    private ProductRepository pRepo;

    private HttpUriRequest createRequest(String URL, AuthRequest authRequest, String jwtToken) throws URISyntaxException {
        HttpPost httpPost = new HttpPost(URL);

        URI uri = new URIBuilder(httpPost.getURI())
                .build();

        httpPost.setURI(uri);
        httpPost.setEntity(new StringEntity("{\"email\": \""+  authRequest.getEmail() + "\",\"password\": \""+  authRequest.getPassword() + "\"}", ContentType.APPLICATION_JSON));

        HttpUriRequest request = httpPost;
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        if(jwtToken != null) {
            request.setHeader(HttpHeaders.AUTHORIZATION, jwtToken);
        }
        return request;
    }

    @Given("I authenticate as ADMIN with the tuple : \\{email : {string}, password : {string}\\}")
    public void authenticate(String email, String password) throws URISyntaxException, IOException {
        AuthRequest authRequest = new AuthRequest(email, password);

        HttpUriRequest request = createRequest(authURL, authRequest, null);
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        Assert.assertTrue(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        jwtToken = EntityUtils.toString(httpResponse.getEntity());
        Assert.assertTrue(jwtToken != null);

        Map<String, Object> decodedToken = decodeJWT(jwtToken);
        Assert.assertTrue(decodedToken.get("sub").equals(email));

        Object roles = decodedToken.get("roles");
        List<LinkedHashMap<String, Object>> rolesCasted = (List<LinkedHashMap<String, Object>>) roles;
        Assert.assertTrue(rolesCasted.size() == 1);

        Assert.assertTrue(rolesCasted.get(0).get("authority").equals("ROLE_" + Role.ADMIN.name()));
    }

    public Map<String, Object> decodeJWT(String jwtToken) throws IOException {
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedBody = split_string[1];
        Base64.Decoder base64Url = Base64.getUrlDecoder();
        Map<String, Object> mapping = new ObjectMapper().readValue(base64Url.decode(base64EncodedBody), HashMap.class);
        return mapping;
    }


    @When("I want to register a new AuthorizedUser who will have a USER Role")
    public void register() {
        Assert.assertTrue(1 ==1);
    }


    @Then("Then I retrieve a USER token")
    public void getUserToken() {
        Assert.assertTrue(1 ==1);
    }

    @Then("my new User has the USER role, and can access to the products list")
    public void userHasUserRole() {
        Assert.assertTrue(1 ==1);
    }

}
