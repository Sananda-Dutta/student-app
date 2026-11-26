package com.example.student;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/** Minimal Java web app: serves the registration form and handles the submission. */
public class App {

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", App::handleForm);
        server.createContext("/register", App::handleRegister);
        server.start();
        System.out.println("Student app running at http://localhost:" + port);
    }

    private static void handleForm(HttpExchange ex) throws IOException {
        if (!ex.getRequestURI().getPath().equals("/")) {
            send(ex, 404, "<h1>404 Not Found</h1>");
            return;
        }
        try (InputStream in = App.class.getResourceAsStream("/index.html")) {
            if (in == null) {
                send(ex, 500, "<h1>index.html missing</h1>");
                return;
            }
            send(ex, 200, new String(in.readAllBytes(), StandardCharsets.UTF_8));
        }
    }

    private static void handleRegister(HttpExchange ex) throws IOException {
        if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
            send(ex, 405, "<h1>405 Use the form to submit</h1>");
            return;
        }
        String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> f = parseForm(body);
        String html = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Registered</title></head>"
                + "<body style='font-family:Arial;padding:30px'>"
                + "<h1>Registration Successful!</h1>"
                + "<p><b>Student Name:</b> " + escape(f.get("name")) + "</p>"
                + "<p><b>Roll Number:</b> " + escape(f.get("roll")) + "</p>"
                + "<p><b>Email:</b> " + escape(f.get("email")) + "</p>"
                + "<p><b>Course:</b> " + escape(f.get("course")) + "</p>"
                + "<p><a href='/'>Register another student</a></p></body></html>";
        send(ex, 200, html);
    }

    /** Parses an application/x-www-form-urlencoded body into a map. */
    static Map<String, String> parseForm(String body) {
        Map<String, String> map = new HashMap<>();
        if (body == null || body.isEmpty()) return map;
        for (String pair : body.split("&")) {
            int i = pair.indexOf('=');
            String k = i < 0 ? pair : pair.substring(0, i);
            String v = i < 0 ? "" : pair.substring(i + 1);
            map.put(URLDecoder.decode(k, StandardCharsets.UTF_8),
                    URLDecoder.decode(v, StandardCharsets.UTF_8));
        }
        return map;
    }

    /** Escapes HTML special characters to prevent script injection. */
    static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private static void send(HttpExchange ex, int code, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        ex.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }
}
