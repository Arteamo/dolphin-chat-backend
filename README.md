## Dolphin Chat Backend [WIP]
WebSocket chat api with oAuth2

API: `dolphin-chat-backend.herokuapp.com`

### iOS client
https://github.com/Med1D/Dolphin

### Stack
* Scala 2.13 and sbt
* Postgres
* Slick
* Akka
* Akka-http
* Circe

### Routes
* `POST /register` - create account
* `POST /auth` - login
* `POST /logout` - logout
* `POST /chats/create` - create chat room
* `GET /chats/list` - list all rooms
* `GET /chats/find/{roomId}` - find room by id
* `GET /chats/find?title={title}` - find room by title
* `Websocket /ws/chats/{roomId}` - websocket for messaging
