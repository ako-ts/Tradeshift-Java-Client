package com.tradeshift.client;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class JerseyClientFactory implements ClientFactory {


    @Override
    public ClientFactory withBasicAuth(String login, String password) {
        return new JerseyClientFactory() {
            @Override
            protected Client configure(Client client) {
                Client c = super.configure(client);
                HttpAuthenticationFeature authenticationFeature = HttpAuthenticationFeature.basic(login, password);
                c.register(authenticationFeature);
                return c;
            }
        };
    }

    @Override
    public ClientFactory withJsonMapping() {
        return new JerseyClientFactory() {
            @Override
            protected Client configure(Client client) {
                Client c = JerseyClientFactory.this.configure(client);
                c.register(JacksonJsonProvider.class);
                return c;
            }
        };
    }

    @Override
    public Client build() {
        return configure(null);
    }

    protected Client configure(Client client) {
        if (client == null) {
            ClientConfig configuration = new ClientConfig();
// uncomment to enable detailed request logging
//            configuration.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL, Level.INFO.getName());
//            configuration.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY, LoggingFeature.Verbosity.PAYLOAD_ANY);
            client = ClientBuilder.newClient(configuration);
        }
        return client;
    }
}
