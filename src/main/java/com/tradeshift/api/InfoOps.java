package com.tradeshift.api;

import com.tradeshift.client.TradeshiftRestClient;

import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.UUID;

/**
 * API Operations about public information, that can be retrieved without authenticating.
 */
public class InfoOps {
    private final TradeshiftRestClient client;

    protected InfoOps(TradeshiftRestClient client) {
        this.client = client;
    }

    public static InfoOps on(TradeshiftRestClient client) {
        return new InfoOps(client);
    }

    /**
     * Returns the "cluster ID" of the Tradeshift instance this REST client is talking to. The Cluster ID uniquely
     * identifies a Tradeshift environment, e.g. production, sandbox, or others.
     */
    public UUID getClusterId() {
        HashMap status = client.target()
                .path("rest/external/info/status")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(HashMap.class);
        return UUID.fromString((String) status.get("ClusterId"));
    }
}
