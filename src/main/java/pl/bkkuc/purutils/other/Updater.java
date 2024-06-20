package pl.bkkuc.purutils.other;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;
import pl.bkkuc.purutils.UtilPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Updater {
    private static final Logger LOGGER = LogManager.getLogger(UtilPlugin.class);
    private static final URI URI_Releases = URI.create("https://api.github.com/repos/bkkuc01/PurUtils/releases/latest");

    public static void check(final String currentVersion) {
        LOGGER.info("Checking update...");

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(URI_Releases.toString()).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    UpdateResponse lastRelease = new Gson().fromJson(response.toString(), UpdateResponse.class);

                    if (isNewVersion(currentVersion, lastRelease.getTagName())) {
                        LOGGER.info("\n---------------------------------------------------" +
                                        "\nДоступно обновление!" +
                                        "\nТекущая версия: {}" +
                                        "\nНовая версия: {}" +
                                        "\nСкачать можно тут: https://github.com/bkkuc01/PurUtils/releases/{}" +
                                        "\nСведения об обновлении: \n{}" +
                                        "\n---------------------------------------------------",
                                currentVersion,
                                lastRelease.getTagName(),
                                lastRelease.getTagName(),
                                lastRelease.getBody());
                        Bukkit.getPluginManager().disablePlugin(UtilPlugin.getInstance());
                    } else {
                        LOGGER.info("Обновления не найдены.");
                    }
                }
            } else {
                LOGGER.warn("Не удалось проверить версию | HTTP response code: {}", responseCode);
            }
        } catch (Exception e) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));
            LOGGER.debug(stringWriter.toString());
            LOGGER.error("Не удалось проверить обновление | Подробная информация в логах | {}", e.getMessage());
        }
    }

    private static boolean isNewVersion(final String currentVersion, final String lastReleaseVersion) {
        return new DefaultArtifactVersion(currentVersion)
                .compareTo(new DefaultArtifactVersion(lastReleaseVersion)) < 0;
    }

    public static class UpdateResponse {
        @SerializedName("tag_name")
        private String tagName;

        @SerializedName("body")
        private String body;

        public String getTagName() {
            return tagName;
        }

        public String getBody() {
            return body;
        }
    }
}