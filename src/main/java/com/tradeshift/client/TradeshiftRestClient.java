package com.tradeshift.client;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Entry point for creating Tradeshift REST client instances.
 */
public class TradeshiftRestClient {

    private final String userAgent;
    private final Supplier<UUID> requestIdSupplier;
    protected String entryPoint;
    protected ClientFactory clientFactory;
    private Object access_token;

    protected TradeshiftRestClient(ClientFactory clientFactory, String entryPoint, final String userAgent, final Supplier<UUID> requestIdSupplier) {
        this.entryPoint = entryPoint;
        this.clientFactory = clientFactory;
        this.userAgent = userAgent;
        this.requestIdSupplier = requestIdSupplier;
    }

    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift production server.
     *
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient production(ClientFactory client, String userAgent) {
        return new TradeshiftRestClient(client, "https://api.tradeshift.com/tradeshift", userAgent, UUID::randomUUID);
    }

    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift sandbox server.
     *
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient sandbox(ClientFactory client, String userAgent) {
        return new TradeshiftRestClient(client, "https://api-sandbox.tradeshift.com/tradeshift", userAgent, UUID::randomUUID);
    }

    /**
     * Creates a new TradeshiftRestClient for accessing the Tradeshift development server.
     *
     * @param userAgent The HTTP User-Agent to use. Must be uniquely identifying you as an API user.
     */
    public static TradeshiftRestClient dev(ClientFactory client, String userAgent, String serverUrl) {
        return new TradeshiftRestClient(client, serverUrl, userAgent, UUID::randomUUID);
    }

    public WebTarget target() {
        return withHeaderProcessor(clientFactory).target(entryPoint);
    }

    private Client withHeaderProcessor(ClientFactory clientFactory) {
        return clientFactory.withJsonMapping().build().register(new HeaderProcessor());
    }

    public TradeshiftRestClient withPasswordGrant(String clientId, String clientPassword) {
        Form params = new Form("grant_type", "password")
                .param("client_id", "Tradeshift.MobileAccess");

        ClientFactory clientFactory = this.clientFactory.withBasicAuth(clientId, clientPassword);

        Client client = withHeaderProcessor(clientFactory);
        Response response = client.target(entryPoint).path("auth/token")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(params, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != 200) {
            String error = response.readEntity(String.class);
            throw new RuntimeException(error);
        }
        HashMap entity = response.readEntity(HashMap.class);
        access_token = entity.get("token_type") + " " + entity.get("access_token");
        return this;
    }

    public TradeshiftRestClient withClientCredentialsGrant(String clientId, String clientPassword) {
        Form params = new Form("grant_type", "client_credentials")
                .param("client_id", clientId);

        Response response = target().path("auth/token")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(params, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        if (response.getStatus() != 200) {
            String error = response.readEntity(String.class);
            throw new RuntimeException(error);
        }
        HashMap entity = response.readEntity(HashMap.class);
        access_token = entity.get("token_type") + " " + entity.get("access_token");
        return this;

    }

    @Provider
    private class HeaderProcessor implements ClientRequestFilter {

        @Override
        public void filter(ClientRequestContext requestContext) throws IOException {
            MultivaluedMap<String, Object> headers = requestContext.getHeaders();
            headers.putSingle("User-Agent", userAgent);
            if (requestIdSupplier != null) {
                UUID id = requestIdSupplier.get();
                if (id != null) {
                    headers.putSingle("X-Tradeshift-RequestId", id.toString());
                }
            }
            if (access_token != null) {
                headers.putSingle(HttpHeaders.AUTHORIZATION, access_token);
            }
        }
    }
}
