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

/**
 * Client HTTP encarregat de comunicar el client escriptori amb el servidor per
 * a les operacions d'autenticació.
 *
 * <p>
 * Actualment implementa les peticions de login i logout mitjançant HTTP.</p>
 *
 * @author Javier Santana Coronado
 */
public class AuthApiClient {

    /**
     * URL base del servidor d'autenticació.
     */
    private static final String BASE_URL = "http://a2001678-f14b-4a5a-8211.04a74656c25a.bastion.elmeuescriptori.cat:80";

    /**
     * Envia una petició de login al servidor amb l'usuari i la contrasenya.
     *
     * @param username nom d'usuari introduït al formulari
     * @param password contrasenya introduïda al formulari
     * @return resposta del login amb l'estat, el missatge, l'usuari i el token
     */
    public LoginResponse login(String username, String password) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(BASE_URL + "/api/auth/login");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String jsonBody = "{"
                    + "\"username\":\"" + username + "\","
                    + "\"password\":\"" + password + "\""
                    + "}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int statusCode = connection.getResponseCode();
            String authHeader = connection.getHeaderField("Authorization");

            InputStream responseStream;
            if (statusCode >= 200 && statusCode < 400) {
                responseStream = connection.getInputStream();
            } else {
                responseStream = connection.getErrorStream();
            }

            String responseBody = readStream(responseStream);

            System.out.println("=== RESPOSTA SERVIDOR LOGIN ===");
            System.out.println("Status code: " + statusCode);
            System.out.println("Authorization header: " + authHeader);
            System.out.println("Body: " + responseBody);
            System.out.println("================================");

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

    /**
     * Envia una petició de logout al servidor amb el token de sessió.
     *
     * @param token token de la sessió activa
     * @return {@code true} si el servidor confirma el logout; {@code false} en
     * cas contrari
     */
    public boolean logout(String token) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(BASE_URL + "/api/auth/logout");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(false);
            connection.setRequestProperty("Authorization", "Bearer " + token);

            int statusCode = connection.getResponseCode();

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

    /**
     * Llegeix el contingut complet d'un {@link InputStream} i el retorna com a
     * text.
     *
     * @param inputStream flux d'entrada a llegir
     * @return contingut del flux en format {@link String}
     * @throws Exception si es produeix un error de lectura
     */
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

    /**
     * Extreu el token del header {@code Authorization}.
     *
     * @param authHeader valor del header {@code Authorization}
     * @return token sense el prefix {@code Bearer }, o {@code null} si no
     * existeix
     */
    private String extractToken(String authHeader) {
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }

        if (authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return authHeader;
    }

    /**
     * Extreu el valor d'una clau dins d'un JSON simple.
     *
     * @param json text JSON rebut
     * @param key clau que es vol cercar
     * @return valor associat a la clau, o {@code null} si no es troba
     */
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

    /**
     * Converteix el rol textual retornat pel servidor al tipus {@link Role} del
     * client.
     *
     * @param rolText rol rebut del servidor
     * @return rol equivalent del client
     */
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
