const ws = new WebSocket("ws://localhost:8080/websocket"); // Replace with your backend WebSocket URL
const feed = document.getElementById("feed");
const postBtn = document.getElementById("postBtn");

postBtn.addEventListener("click", () => {
  const username = document.getElementById("username").value.trim();
  const message = document.getElementById("message").value.trim();

  if (!username || !message) return alert("Name and message are required!");

  const postId = Date.now(); // Unique ID for the post
  ws.send(
    JSON.stringify({
      type: "post",
      id: postId,
      name: username,
      message: message,
    })
  );
  document.getElementById("message").value = ""; // Clear the input
});

ws.onmessage = (event) => {

  const data = JSON.parse(event.data);

  if (data.type === "post") {
    addPostToFeed(data);
  } else if (data.type === "reply") {
    addReplyToPost(data);
  }
};

function ping() {
  ws.send(
      JSON.stringify({
        type: "ping",
      })
    );
}

function addPostToFeed({ id, name, message }) {
  const postDiv = document.createElement("div");
  postDiv.className = "post";
  postDiv.id = `post-${id}`;

  postDiv.innerHTML = `
    <div class="post-header">${name}</div>
    <div class="post-content">${message}</div>
    <button class="reply-btn" onclick="showReplyForm('${id}')">Reply</button>
    <div class="reply-form" id="reply-form-${id}">
      <input type="text" placeholder="Your Name" required />
      <textarea placeholder="Write your reply"></textarea>
      <button onclick="sendReply('${id}')">Send</button>
    </div>
  `;
  feed.prepend(postDiv); // Add the post to the top of the feed
}

function showReplyForm(postId) {
  const replyForm = document.getElementById(`reply-form-${postId}`);
  replyForm.style.display = replyForm.style.display === "none" ? "block" : "none";
}

function sendReply(postId) {
  const replyForm = document.getElementById(`reply-form-${postId}`);
  const textarea = replyForm.querySelector("textarea");
  const replyMessage = textarea.value.trim();
  const username =  replyForm.querySelector("input").value.trim();

  if (!replyMessage) return alert("Reply cannot be empty!");

  ws.send(
    JSON.stringify({
      type: "reply",
      replyTo: postId,
      name: username,
      message: replyMessage,
    })
  );
  textarea.value = ""; // Clear the reply box
  replyForm.style.display = "none"; // Hide the reply form
}

function addReplyToPost({ replyTo, name, message }) {
  const postDiv = document.getElementById(`post-${replyTo}`);
  if (!postDiv) return;

  const replyDiv = document.createElement("div");
  replyDiv.className = "reply";
  replyDiv.innerHTML = `<strong>${name}:</strong> ${message}`;
  postDiv.appendChild(replyDiv);
}

function copyText(button) {
  // Find the text to copy (previous sibling of the button)
  const textToCopy = button.previousElementSibling.textContent;

  // Copy the text to clipboard
  navigator.clipboard.writeText(textToCopy).then(() => {
    showCopyMessage();
  }).catch(err => {
    console.error('Failed to copy text: ', err);
  });
}
