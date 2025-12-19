package com.gustavo.vaultapp.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class VaultClient {
    private final String vaultAddr;
    private final String adminToken;
    private final ObjectMapper mapper = new ObjectMapper();

    public VaultClient(@Value("${app.vault.addr}") String vaultAddr,
                       @Value("${app.vault.adminToken:}") String adminToken) {
        this.vaultAddr = vaultAddr;
        this.adminToken = adminToken;
    }

    public VaultLoginResult loginUserpass(String username, String password) throws Exception {
        String url = vaultAddr + "/v1/auth/userpass/login/" + username;
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity("{\"password\":\"" + password + "\"}", StandardCharsets.UTF_8));

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            var resp = client.execute(post);
            var node = mapper.readTree(resp.getEntity().getContent());

            // 🔍 Log completo de la respuesta
            System.out.println("Vault raw login response for user " + username + ": " + node.toPrettyString());

            String clientToken = node.path("auth").path("client_token").asText();

            // Leer token_policies primero
            JsonNode policiesArr = node.path("auth").path("token_policies");
            if (policiesArr.isMissingNode() || policiesArr.size() == 0) {
                policiesArr = node.path("auth").path("policies");
            }

            String[] policies = new String[policiesArr.size()];
            for (int i = 0; i < policiesArr.size(); i++) {
                policies[i] = policiesArr.get(i).asText();
            }

            return new VaultLoginResult(clientToken, policies);
        }
    }
    public record VaultLoginResult(String token, String[] policies) {}
}