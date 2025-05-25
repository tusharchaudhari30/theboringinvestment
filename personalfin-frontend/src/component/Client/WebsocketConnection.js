import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

class WebsocketConnection {
  static url = "/portfolio";
  static stompClient = null;
  static connectWebsocket(updatePortfolio) {
    const token = localStorage.getItem("token");
    // Create a new STOMP client
    WebsocketConnection.stompClient = new Client({
      // Create the WebSocket connection using SockJS
      webSocketFactory: () => new SockJS(WebsocketConnection.url + "/ws"),
      // Optional debug log
      debug: (str) => console.log(str),
      // Authorization headers (if supported by backend)
      connectHeaders: {
        Authorization: "Bearer " + token,
      },
      // Called when the client connects
      onConnect: () => {
        console.log("Connected to WebSocket");
        WebsocketConnection.stompClient.subscribe(
          "/user/queue/portfolio",
          updatePortfolio
        );
      },
      // Called if the connection closes or fails
      onStompError: (frame) => {
        console.error("Broker error:", frame.headers["message"]);
        console.error("Details:", frame.body);
      },
      onWebSocketError: (event) => {
        console.error("WebSocket error:", event);
      },
    });
    // Activate the STOMP client
    WebsocketConnection.stompClient.activate();
  }

  static disconnectWebsocket() {
    if (
      WebsocketConnection.stompClient &&
      WebsocketConnection.stompClient.active
    ) {
      WebsocketConnection.stompClient.deactivate();
      console.log("WebSocket disconnected");
    }
  }
}

export default WebsocketConnection;
