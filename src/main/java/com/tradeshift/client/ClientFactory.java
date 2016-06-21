package com.tradeshift.client;

import javax.ws.rs.client.Client;

public interface ClientFactory {

    /**
     * Create factory which produce client with basic authentication set.
     *
     * @param login    user login
     * @param password user password
     * @return new factory
     */
    ClientFactory withBasicAuth(String login, String password);

    /**
     * Create factory which produce client with json mapping capabilities
     *
     * @return new factory
     */
    ClientFactory withJsonMapping();

    /**
     * Create new client.
     *
     * @return configured client
     */
    Client build();

}
