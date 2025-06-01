import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

class WebsocketConnection {
  static url = "/portfolio"; // Proxy path set via Nginx

  static connectWebsocket(updatePortfolio) {
    const token = localStorage.getItem("token");
    if (!token) {
      console.error("No token found in localStorage.");
      return;
    }

    const stompClient = new Client({
      webSocketFactory: () =>
        new SockJS(`${this.url}/ws?Authorization=Bearer ${token}`),
      debug: (str) => console.log(str),
      reconnectDelay: 5000,
      onConnect: () => {
        console.log("WebSocket connected as", token);
        stompClient.subscribe("/user/queue/portfolio", (message) => {
          try {
            const data = JSON.parse(message.body);
            updatePortfolio(data); // invoke callback
          } catch (e) {
            console.error("Invalid message format", message);
          }
        });
      },
      onStompError: (frame) => {
        console.error("Broker error", frame.headers["message"], frame.body);
      },
    });

    stompClient.activate();
    return stompClient;
  }
}

export default WebsocketConnection;
