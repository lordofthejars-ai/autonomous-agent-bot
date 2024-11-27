document.addEventListener("DOMContentLoaded", () => {
  const chatBox = document.getElementById("chatBox");
  const messageInput = document.getElementById("messageInput");
  const sendMessageBtn = document.getElementById("sendMessageBtn");
  const defaultMessageBtn = document.getElementById("defaultMessageBtn");

  const API_URL = "/createCampaign"; // Replace with your actual backend REST API URL

  // Send a message to the server
  sendMessageBtn.addEventListener("click", () => {
    const message = messageInput.value.trim();
    if (!message) {
      alert("Message cannot be empty!");
      return;
    }

    sendMessageToServer(message);
  });

  // Insert a default message into the text area
  defaultMessageBtn.addEventListener("click", () => {
    messageInput.value = "Hello, this is a default message!";
  });

  // Function to send message to server
  function sendMessageToServer(message) {
    fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ message }),
    })
      .then((response) => response.json())
      .then((data) => {
        addMessageToChatBox(`You: ${message}`);
        if (data.response) {
          addMessageToChatBox(`Server: ${data.response}`);
        }
      })
      .catch((error) => {
        console.error("Error sending message:", error);
        addMessageToChatBox("Error: Could not send the message.");
      })
      .finally(() => {
        messageInput.value = ""; // Clear the input
      });
  }

  // Function to add a message to the chat box
  function addMessageToChatBox(message) {
    const messageDiv = document.createElement("div");
    messageDiv.textContent = message;
    chatBox.appendChild(messageDiv);
    chatBox.scrollTop = chatBox.scrollHeight; // Scroll to the latest message
  }
});
