package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RuntimeConfig {
    private static final String CONFIG_FILE = "src/Configurations.properties";
    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("^\\$\\{([^}]+)}$");
    private static final Pattern UNRESOLVED_TOKEN_PATTERN = Pattern.compile("^<[^>]+>$");
    private static final Properties PROPERTIES = loadProperties();

    private RuntimeConfig() {
    }

    public static String get(String key, String defaultValue) {
        String systemValue = System.getProperty(key);
        if (isProvided(systemValue)) {
            return systemValue.trim();
        }

        String envValue = System.getenv(toEnvKey(key));
        if (isProvided(envValue)) {
            return envValue.trim();
        }

        String fileValue = PROPERTIES.getProperty(key);
        if (isProvided(fileValue)) {
            return fileValue.trim();
        }

        return defaultValue;
    }

    public static String getRequired(String key) {
        String value = get(key, "");
        if (!isProvided(value) || isTemplateToken(value)) {
            throw new IllegalStateException("Required runtime value is missing for key: " + key);
        }
        return value;
    }

    public static String resolveScenarioValue(String rawValue) {
        if (!isProvided(rawValue)) {
            return rawValue;
        }

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(rawValue.trim());
        if (matcher.matches()) {
            return get(matcher.group(1), "");
        }

        return rawValue.trim();
    }

    private static boolean isTemplateToken(String value) {
        return UNRESOLVED_TOKEN_PATTERN.matcher(value).matches()
                || PLACEHOLDER_PATTERN.matcher(value).matches();
    }

    private static boolean isProvided(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static String toEnvKey(String key) {
        return key.replaceAll("([a-z])([A-Z])", "$1_$2")
                .replace('.', '_')
                .toUpperCase(Locale.ROOT);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (IOException ignored) {
            // Missing local config should not fail compile-time usage.
        }
        return properties;
    }
}
