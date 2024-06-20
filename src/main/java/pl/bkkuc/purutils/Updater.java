package pl.bkkuc.purutils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.bukkit.Bukkit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.Optional;

public class Updater {
    private static final Logger LOGGER = LogManager.getLogger(UtilPlugin.class);
    private static final URI URI_Releases = URI.create("https://api.github.com/repos/bkkuc01/PurUtils/releases/latest");

    public static void check(final String currentVersion) {
        LOGGER.info("Проверка обновления...");

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpClient.execute(
                    RequestBuilder.get()
                            .setUri(URI_Releases)
                            .build()
            )) {
                HttpEntity entity = response.getEntity();
                if (Optional.ofNullable(entity).isPresent()) {
                    UpdateResponse lastRelease = new Gson().fromJson(
                            EntityUtils.toString(entity),
                            UpdateResponse.class);

                    if (isNewVersion(currentVersion, lastRelease.getTagName())) {
                        LOGGER.info("\n---------------------------------------------------" +
                                        "\nДоступно обновление!" +
                                        "\nТекущая версия: {}" +
                                        "\nНовая версия: {}" +
                                        "\nСкачать можно тут: https://github.com/bkkuc01/PurUti;s/releases/{}" +
                                        "\nСведения об обновлении: \n{}" +
                                        "\n---------------------------------------------------",
                                currentVersion,
                                lastRelease.getTagName(),
                                lastRelease.getTagName(),
                                lastRelease.getBody());
                        Bukkit.getPluginManager().disablePlugin(UtilPlugin.getInstance());

                    } else LOGGER.info("Обновления не найдены.");
                } else LOGGER.warn("Не удалось проверить версию | {}", response);
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