import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

/**
 * Keycloak image does not provide curl or wget
 * so this is the most readable option to provide healthcheck for docker compose
 */
public class HealthCheck {

  public static void main(String[] args) throws InterruptedException, IOException {
    var client = HttpClient.newHttpClient();
    var request = HttpRequest.newBuilder()
        .uri(URI.create(args[0]))
        .header("accept", "application/json")
        .build();

    var response = client.send(request, BodyHandlers.ofString());

    if (response.statusCode() != 200 || !response.body().contains("UP")) {
      throw new RuntimeException("Healthcheck failed");
    }
  }
}
