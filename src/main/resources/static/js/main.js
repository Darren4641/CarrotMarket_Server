var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#ff9800', '#39bbb0'
]
var roomId = null;
function connect(myId, title) {
    // username = document.querySelector('#name').value.trim();
    username = myId;
    setRoomId(title);
    console.log("@@");
    console.log(roomId);
    localStorage.setItem('chatId', username);
    console.log(username);
    if(username) {
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws/chat');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    //event.preventDefault();
}

function setRoomId(title) {
    this.roomId = title;
}

function getRoomId() {
    return this.roomId;
}

function onConnected() {
    stompClient.subscribe('/topic/chat/room/'+ getRoomId(), onMessageReceived);
    stompClient.send("/app/chat/enter",
        {},
        JSON.stringify({sender: username, type: 'ENTER', content: "", roomId: getRoomId()})
    )
    connectingElement.classList.add('hidden');
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.color = 'red';
}

function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'TALK',
            roomId: getRoomId()
        };
        stompClient.send("/app/chat/message", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    console.log("@@");
    console.log(payload);
    var message = JSON.parse(payload.body);
    var messageDiv = document.createElement("div");
    var messageElement = document.createElement('li');

    if(message.type === 'ENTER') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
    }else if(message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';
    }else {
        messageDiv.classList.add('chat-messageParent');
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);




        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        if(message.sender === username) {
            messageDiv.style['text-align'] = 'right';
            avatarElement.style['position'] = 'relative';
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
            messageElement.appendChild(avatarElement);
        }else {
            avatarElement.style['position'] = 'absolute';
            messageElement.appendChild(avatarElement);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageDiv.appendChild(messageElement);


    messageArea.appendChild(messageDiv);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for( var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

//usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);