import { useState } from "react";
import ChatWithCanvas from "./ChatWithCanvas.js";

function RoomSelector() {
  const [selectedChat, setSelectedChat] = useState(null);
  const [roomId, setRoomId] = useState("");

  const handleChatButtonClick = (chat) => {
    setSelectedChat(chat);
  };

  return (
    <div>
      <h2>방주소를 입력해주세요</h2>

      <input
        type="text"
        value={roomId}
        onChange={(e) => setRoomId(e.target.value)}
        placeholder="방주소"
      />
      <button onClick={() => handleChatButtonClick(roomId)}>확인</button>

      {selectedChat !== null && <ChatWithCanvas roomId={selectedChat} />}
    </div>
  );
}

export default RoomSelector;
