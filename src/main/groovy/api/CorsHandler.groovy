package api

import com.sun.net.httpserver.HttpExchange

class CorsHandler {
    static void addCorsHeaders(HttpExchange exchange) {
        exchange.responseHeaders.add("Access-Control-Allow-Origin", "*")
        exchange.responseHeaders.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        exchange.responseHeaders.add("Access-Control-Allow-Headers", "Content-Type, Authorization")
    }

    static boolean handleOptions(HttpExchange exchange) {
        if (exchange.requestMethod.equalsIgnoreCase("OPTIONS")) {
            addCorsHeaders(exchange)
            exchange.sendResponseHeaders(204, -1)
            exchange.close()
            return true
        }
        return false
    }
}