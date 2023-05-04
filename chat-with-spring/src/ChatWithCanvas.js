import React, { useState, useEffect, useRef } from "react";
import { w3cwebsocket as W3CWebSocket } from "websocket";

// const client = new W3CWebSocket("ws://localhost:8080/chat/chat1");

function ChatWithCanvas(props) {
  const [client, setClient] = useState(null);
  useEffect(() => {
    if (props.roomId) {
      const newClient = new W3CWebSocket(
        `ws://localhost:8080/chat/${props.roomId}`
      );
      setClient(newClient);
    }
  }, [props.roomId]);

  // console.log(a);
  // const client = new W3CWebSocket(`ws://localhost:8080/chat/${a}`);
  //chat
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState("");
  const [clientId, setClientId] = useState("");

  //canvas
  const [isDrawing, setIsDrawing] = useState(false);
  const [lastX, setLastX] = useState(0);
  const [lastY, setLastY] = useState(0);
  const canvasRef = useRef(null);

  //chat
  useEffect(() => {
    if (client) {
      client.onopen = () => {
        console.log("WebSocket Client Connected");
      };
      client.onmessage = (message) => {
        const data = JSON.parse(message.data);
        if (data.type === "draw") {
          const canvas = canvasRef.current;
          const context = canvas.getContext("2d");
          context.beginPath();
          context.moveTo(data.lastX, data.lastY);
          context.lineTo(data.currentX, data.currentY);
          context.stroke();
        } else {
          setMessages((prevMessages) => [...prevMessages, data]);
        }
      };
      client.onclose = () => {
        console.log("WebSocket Client Disconnected");
      };
    }
  }, [client]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const message = { clientId, content: inputValue };
    client.send(JSON.stringify(message));
    setInputValue("");
  };

  //canvas
  useEffect(() => {
    const canvas = canvasRef.current;
    const context = canvas.getContext("2d");
    context.lineWidth = 2;
    context.lineCap = "round";
    context.strokeStyle = "red";
  }, []);

  function sendDrawingData(data) {
    if (client.readyState === client.OPEN) {
      client.send(JSON.stringify(data));
    }
  }

  function handleMouseDown(e) {
    setIsDrawing(true);
    setLastX(e.nativeEvent.offsetX);
    setLastY(e.nativeEvent.offsetY);
  }

  function handleMouseMove(e) {
    if (!isDrawing) return;
    const canvas = canvasRef.current;
    const context = canvas.getContext("2d");
    const currentX = e.nativeEvent.offsetX;
    const currentY = e.nativeEvent.offsetY;
    context.beginPath();
    context.moveTo(lastX, lastY);
    context.lineTo(currentX, currentY);
    context.stroke();
    sendDrawingData({
      type: "draw",
      lastX: lastX,
      lastY: lastY,
      currentX: currentX,
      currentY: currentY,
    });
    setLastX(currentX);
    setLastY(currentY);
  }

  function handleMouseUp() {
    setIsDrawing(false);
  }

  return (
    <div>
      <form>
        <input value={clientId} onChange={(e) => setClientId(e.target.value)} />
        <button type="submit">닉네임 입력</button>
      </form>

      <div
        style={{ height: "400px", border: "1px solid black", overflow: "auto" }}
      >
        {messages.map((message, index) => (
          <div
            key={index}
            style={{
              textAlign: message.clientId === clientId ? "right" : "left",
              backgroundColor:
                message.clientId === clientId ? "yellow" : "transparent",
              padding: "0.5rem",
              borderRadius: "0.5rem",
              marginBottom: "0.5rem",
              maxWidth: "80%",
            }}
          >
            {message.clientId === clientId ? "You: " : `${message.clientId}: `}
            {message.content}
          </div>
        ))}
      </div>
      <form onSubmit={handleSubmit}>
        <input
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button type="submit">Send</button>
      </form>

      <canvas
        ref={canvasRef}
        width={500}
        height={500}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        style={{ border: "1px solid black" }}
      />
    </div>
  );
}

export default ChatWithCanvas;
