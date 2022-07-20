function doSendMessage(socket) {
    const messageInput = document.querySelector('#messageInput');
    const messageInputValue = messageInput.value;
    const emptyMessage = messageInputValue === '';

    if(emptyMessage) {
        return;
    }
    socket.send(messageInputValue);
    messageInput.value = '';
}

function doReceiveMessage(socket, message) {
    console.debug('Received message from server: ', message)

    const messageObject = JSON.parse(message.data);

    doHandleMessage(messageObject);
}

function doHandleMessage(message) {
    const messageText = message.text;
    const nameRegex = /Your name is ([A-Za-z_\-]+)$/g;
    const matches = nameRegex.exec(message);
    if(matches) {
        userName = matches[1];
    }

    if(userName === message.senderName) {
        document.querySelector(".message-container").innerHTML += '<div class="message -own">'+
            '<div class="message__container">'+
            '<p class="message__sender">' + message.senderName + '</p>'+
            '<div class="message__text">' + message.text  + '</div>'+
            '</div><div class="profile-image"><img src="https://www.gravatar.com/avatar/'+md5(message.senderName)+'?d=identicon"></div>';
    } else {
        document.querySelector(".message-container").innerHTML += '<div class="message"><div class="profile-image"><img src="https://www.gravatar.com/avatar/'+md5(message.senderName)+'?d=identicon"></div>'+
            '<div class="message__container">'+
            '<p class="message__sender">' + message.senderName + '</p>'+
            '<div class="message__text">' + message.text  + '</div>'+
            '</div>';
    }

    setTimeout(() => {
        document.querySelector('.chat__area').scrollTo(0, 100000);
    });

}

function doHandleError(socket, error) {
    console.error("Received error from server: ", error);

    // TODO: Visualize error
}

function runApplication() {
    // Set session user name to unknown on startup. After the connection has been successfully established, the username
    // is queried with a special command to the server (/me). The answer is then parsed and saved to visualize own messages
    // in a special way (like WhatsApp & others do)
    this.userName = "Unknown";

    // Find correct domain name
    const domainName = location.host.split(':')[0];

    // Open websocket on port 8080
    const socket = new WebSocket("ws://"+domainName+":8080/chat");

    // When the connection is successful, ask the server for your given name automatically
    socket.onopen = () => socket.send("/me");
    socket.onerror = doHandleError.bind(this, socket);
    socket.onmessage = doReceiveMessage.bind(this, socket);


    const messageInput = document.querySelector('#messageInput');
    messageInput.addEventListener('keypress', (e) => {
        if(e.key !== 'Enter') {
            return;
        }

        doSendMessage(socket);
    });

    const messageInputSubmitButton = document.querySelector('#send');
    messageInputSubmitButton.addEventListener('click', () => doSendMessage(socket));
}

// When the DOM is fully loaded, run js application code
document.addEventListener('DOMContentLoaded', runApplication.bind(this));
