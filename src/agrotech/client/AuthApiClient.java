package agrotech.client;

import agrotech.model.LoginResponse;
import agrotech.model.Role;
import agrotech.model.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthApiClient {
    private static final String BASE_URL = "http://a2001678-f14b-4a5a-8211.04a74656c25a.bastion.elmeuescriptori.cat:80";

    public LoginResponse login(String username, String password) {
        HttpURLConnection connection = null;

        try {
            // URL de l'endpoint de login
            URL url = new URL(BASE_URL + "/api/auth/login");

            // Obrim la connexió HTTP
            connection = (HttpURLConnection) url.openConnection();

            // Indiquem que serà una petició POST
            connection.setRequestMethod("POST");

            // Indiquem que enviarem dades al body
            connection.setDoOutput(true);

            // Indiquem que el body serà JSON
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            // Construïm el JSON manualment
            String jsonBody = "{"
                    + "\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\""
                    + "}";

            // Enviem el JSON al servidor
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Llegim el codi de resposta HTTP
            int statusCode = connection.getResponseCode();

            // Llegim el header Authorization si existeix
            String authHeader = connection.getHeaderField("Authorization");

            // Llegim el body de la resposta
            InputStream responseStream;

            if (statusCode >= 200 && statusCode < 400) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            String responseBody = readStream(responseStream);

            // Mostrem la resposta a consola per comprovar què retorna el servidor
            System.out.println("=== RESPOSTA SERVIDOR LOGIN ===");
            System.out.println("Status code: " + statusCode);
            System.out.println("Authorization header: " + authHeader);
            System.out.println("Body: " + responseBody);
            System.out.println("================================");

            // Si el login és correcte, llegim el token i el rol reals
            if (statusCode == 200) {
                String token = extractToken(authHeader);
                String rolText = extractJsonValue(responseBody, "rol");
                Role role = mapRole(rolText);

                User user = new User(1, username, role);

                return new LoginResponse(true, "Login correcte.", user, token);
            }

            if (statusCode == 401) {
                return new LoginResponse(false, "Contrasenya incorrecta.", null, null);
            }

            if (statusCode == 404) {
                return new LoginResponse(false, "Usuari no trobat.", null, null);
            }

            return new LoginResponse(false, "Error del servidor. Codi HTTP: " + statusCode, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse(false, "No s'ha pogut connectar amb el servidor.", null, null);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public boolean logout(String token) {
        HttpURLConnection connection = null;

        try {
            // URL de l'endpoint de logout
            URL url = new URL(BASE_URL + "/api/auth/logout");

            // Obrim la connexió HTTP
            connection = (HttpURLConnection) url.openConnection();

            // Indiquem que serà una petició POST
            connection.setRequestMethod("POST");

            // Indiquem que no enviarem body, només headers
            connection.setDoOutput(false);

            // Enviem el token al header Authorization
            connection.setRequestProperty("Authorization", "Bearer " + token);

            // Llegim el codi de resposta HTTP
            int statusCode = connection.getResponseCode();

            // Llegim el body per depuració
            InputStream responseStream;

            if (statusCode >= 200 && statusCode < 400) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            String responseBody = readStream(responseStream);

            // Mostrem la resposta a consola per comprovar què retorna el servidor
            System.out.println("=== RESPOSTA SERVIDOR LOGOUT ===");
            System.out.println("Status code: " + statusCode);
            System.out.println("Body: " + responseBody);
            System.out.println("=================================");

            // Si retorna 200, considerem que el logout ha anat bé
            return statusCode == 200;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Llegeix tot el contingut d'un InputStream i el converteix en String
    private String readStream(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }

        return result.toString();
    }

    // Treu el prefix "Bearer " del header Authorization
    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    // Extreu un valor simple d'un JSON molt petit com {"rol":"user","status":200}
    private String extractJsonValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);

        if (start == -1) {
            return null;
        }

        start += search.length();

        while (start < json.length() && Character.isWhitespace(json.charAt(start))) {
            start++;
        }

        if (start < json.length() && json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);

            if (end != -1) {
                return json.substring(start, end);
            }
        }

        return null;
    }

    // Converteix el rol del servidor al rol del client
    private Role mapRole(String rolText) {
        if (rolText == null) {
            return Role.CLIENT;
        }

        if (rolText.equalsIgnoreCase("admin")) {
            return Role.ADMIN;
        }

        if (rolText.equalsIgnoreCase("user")) {
            return Role.CLIENT;
        }

        return Role.CLIENT;
    }
}
