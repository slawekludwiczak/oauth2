package com.ludigi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class ProductController {
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ProductController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @GetMapping("/add-product")
    String addProduct() {
        return "add-product";
    }

    @PostMapping("/add-product")
    String addProduct(AddProductCommand addProductCommand,
                      @RegisteredOAuth2AuthorizedClient("kc-client") OAuth2AuthorizedClient authorizedClient) throws URISyntaxException, IOException, InterruptedException {
        String jsonCommand = objectMapper.writeValueAsString(addProductCommand);
        HttpRequest request = HttpRequest.newBuilder(new URI("http://products:8081/api/products"))
                .header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(jsonCommand))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 201) {
            return "redirect:/";
        } else {
            return "redirect:/add-product";
        }
    }

    @GetMapping("/products")
    @ResponseBody
    public String products(@RegisteredOAuth2AuthorizedClient("kc-client") OAuth2AuthorizedClient authorizedClient) {
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("http://products:8081/api/products"))
                    .header("Authorization", "Bearer " + authorizedClient.getAccessToken().getTokenValue())
                    .GET()
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            return "error when requesting products from resource server";
        }
        return response.statusCode() == 200 ? response.body() : String.valueOf(response.statusCode());
    }

    record AddProductCommand(String name, double price){}
}
