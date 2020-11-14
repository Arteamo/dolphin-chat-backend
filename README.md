## Dolphin Chat Backend
WebSocket chat api with oAuth2

API: `dolphin-chat-backend.herokuapp.com`

### Stack
* Scala 2.13 and sbt
* Postgres
* Slick
* Akka-HTTP
* Circe
* Swagger

### Routes
* `POST /register`
* `POST /auth`
* `POST /logout`
* `POST /chats/create`
* `GET /chats/list`
* `GET /chats/find/{roomId}`
* `Websocket /ws/chats/{roomId}`
