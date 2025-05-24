(function () {
  const WS_URL = "ws://localhost:8082/ws/portfolio-updates/websocket";
  const TOKEN =
    "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBleGFtcGxlLmNvbSIsImlzcyI6ImJvcmluZyIsImV4cCI6MTc0NjYxNDY5OCwiaWF0IjoxNzQ2NjEyODk4LCJqdGkiOiI2MjdiODU3Yy1iOWMyLTRlZWUtYTQ2ZC00MzliMGY5MDRhMTcifQ.59J_dwr7zKhXlQw3CbDkY2LU8KkSz65lEquM3rNFQvk";
  const DESTINATION = "/user/topic/portfolio";

  let socket;
  let reconnectDelay = 2000;

  function connect() {
    console.log("Connecting to WebSocket...");
    socket = new WebSocket(WS_URL);

    socket.onopen = () => {
      console.log("WebSocket connection opened");

      const connectFrame =
        "CONNECT\n" +
        "accept-version:1.2\n" +
        "host:localhost\n" +
        `token:${TOKEN}\n` +
        "\n\u0000";

      socket.send(connectFrame);
    };

    socket.onmessage = (event) => {
      const message = event.data;
      console.debug("Raw message received:", message);

      if (message.startsWith("CONNECTED")) {
        console.log("STOMP connection established");

        const subscribeFrame =
          "SUBSCRIBE\n" +
          "id:sub-0\n" +
          `destination:${DESTINATION}\n` +
          "\n\u0000";

        socket.send(subscribeFrame);
      } else if (message.startsWith("MESSAGE")) {
        const bodyIndex = message.indexOf("\n\n") + 2;
        const body = message.substring(bodyIndex, message.length - 1);
        try {
          const stockPrice = JSON.parse(body);
          console.log(
            `[Update] ${stockPrice.stockSymbol}: $${stockPrice.price}`
          );
        } catch (e) {
          console.warn("Non-JSON message received:", body);
        }
      } else if (message.startsWith("ERROR")) {
        console.error("STOMP error received:", message);
      } else {
        console.debug("Unhandled message:", message);
      }
    };

    socket.onclose = (event) => {
      console.warn("WebSocket closed, attempting reconnect...", event.reason);
      setTimeout(connect, reconnectDelay);
    };

    socket.onerror = (error) => {
      console.error("WebSocket error:", error);
      socket.close();
    };
  }

  connect();
})();
