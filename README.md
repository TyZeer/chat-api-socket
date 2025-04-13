# CHAT_API_SOCKET
АПИ для создания и общения в чатах с применением SPRING BOOT RESTfull api и WebSocket
## Запуск проекта
Для запуска проекта необходимо запустить Docker Desktop, далее ввести команду
в корне проекта
```bash
docker compose up -d
```
После ввода команды необходимо перейти по адресу [`http://localhost:9001`]
логин пароль - minioadmin:minioadmin, далее нажать создать bucket и
создать ведро с названием <b>chat-bucket</b>

# API Endpoint : localhost:8080

# API
## АВТОРИЗАЦИЯ
#### /auth/sign-up
,
* `POST`
``` json
Request body
{
    "email": "example@email.com",
    "username": "username",
    "password": "Password1234"
}
```
Response
```json
{
  "token":"new_token"
}
```
#### /auth/sign-in

* `POST`
``` json
Request body
{
    "username": "username",
    "password": "Password1234"
}
```
Response
```json
{
  "token":"new_token"
}
```
#### /auth/check-auth
* `POST`
``` json
Request body
{
    "token": "token",
    "username": "username"
}
```
Response
```
  true
```
#### /auth/regenerate-token

* `POST`
``` json
Request body
{
    "token": "token",
    "username": "username"
}
```
Response
```json
{
  "token":"new_token"
}
```
* `POST` /auth/who-am-i (Только для авторизованных пользователей)
```json
Response
{
  "id": 1,
  "username": "Tyzer"
}
```

# Создание чатов
## /api/chat-rooms
* `POST` /group (Свой id не надо указывать)
```json
{
  "name": "Рабочая группа",
  "memberIds": [1, 2, 3, 4]
}

```
* `POST` /private (Свой id не надо указывать)
```json
{
    "name" : "Name",
    "user_id" : "1"
}

```
* `POST` /{chatID}/add-user?userId=100 (Добавление пользователя)
* `GET` /my (Вернет список чатов пользователя)
## Сбор всех сообщений из чата й c примером запроса с пагинацией сообщений и сортировкой
* `GET` /api/chat-messages/{chatRoomId}?page (api/chat-messages/1?page=0&size=20&sort=timestamp,desc)
## Взаимодействие с сообщениями отредактировать и удалить
* `DELETE` /api/chat-messages/{chatMessageId} (Удаление сообщения из чата (Может только отправитель))
* `PUT` /api/chat-messages/{chatMessageId} (Редактирование сообщения)
```json
Request body
{
  "content" : "New message content"
}
```
```json
Response
{
  "id": 2,
  "sender": {
    "id": 1,
    "username": "Tyzer"
  },
  "content": "new_content",
  "fileUrl": "http://localhost:9000/chat-bucket/f6b6e445-c107-4e05-bc85-74269200c647-test-photo.jpg",
  "timestamp": "2025-03-29T11:43:59.391122"
}
```
# Работа с файлами
## /api/files Общее начало
* `POST` /upload (Запрос выглядит как-то так Отправляется перед отправкой сообщения с файлом, для сохранения его в бд)
```js
const formData = new FormData();
formData.append('file', fileInput.files[0]);

fetch('/api/files/upload', {
  method: 'POST',
  body: formData
})
.then(response => response.text())
.then(fileUrl => console.log('File URL:', fileUrl))
.catch(error => console.error('Error:', error));
```
Response
```string
filename.jpg
```
* `GET` /{filename} (Вернет готовую ссылку на скачивание проекта)

## Получение списка пользователей
* `GET` /api/users?username=z (Вернет список пользователей для поиска есть параметр username он не обязательный)

# WEBSOCKET
# Тут я просто вставлю пример того как я отправил сообщение, никнейм пользователя надо запоминать при успешном логине или регистрации
Обязательно надо добавлять jwt в хедере, иначе нельзя отправлять и получать сообщения
```js
const WebSocket = require('ws');
const Stomp = require('stompjs');
const fs = require('fs');
const axios = require('axios');
const FormData = require('form-data');

const socket = new WebSocket("ws:localhost:8081/ws");
const stompClient = Stomp.over(socket);

const delay = ms => new Promise(res => setTimeout(res, ms));

const jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOjEsImVtYWlsIjoiZW1haWxAZ21haWwuY29tIiwic3ViIjoiVHl6ZXIiLCJpYXQiOjE3NDQzMTE4NzQsImV4cCI6MTc0NTE3NTg3NH0.uWI0TIgCUtXqbQeP4J3czGF-i5xitjsymimpnZdUM9BPcKeltDQkgtgZQrXkXDk4w4ZciMywXeITLnHw3pHPog";
const chatId = 1;
const sender = "test1";

// Указанный тобой путь к файлу
const filePath = "C:\\Users\\Dmitriy Pavlov\\OneDrive\\Рабочий стол\\test-photo.jpg";

// Функция загрузки файла в MinIO через API
// async function uploadFile(filePath) {
//     const formData = new FormData();
//     formData.append("file", fs.createReadStream(filePath));

//     try {
//         const response = await axios.post("http://localhost:8080/api/files/upload", formData, {
//             headers: {
//                 ...formData.getHeaders(),
//                 Authorization: `Bearer ${jwtToken}` // Добавляем токен в заголовки
//             },
//         });
//         return response.data; // URL загруженного файла
//     } catch (error) {
//         console.error("Ошибка загрузки файла:", error.response?.data || error.message);
//         return null;
//     }
// }

stompClient.connect({ Authorization: `Bearer ${jwtToken}` }, async function (frame) {
    console.log("Connected to WebSocket");

    // Загружаем фото и получаем ссылку
    // const fileUrl = await uploadFile(filePath);
    // if (!fileUrl) {
    //     console.error("Не удалось загрузить файл, сообщение не отправлено");
    //     return;
    // }

    let message = {
        content: "Ахуеееееееееееееееееееть как же тут дохуя текста, как же меня это все заебало, простоо невозможно блять, эти ебучие слова писать пДЫВЛраФХЩШГГЫВРТЪЩ0РОШФВЫАЭШЩЗО",
        fileUrl: "", // Используем загруженный URL
        chatId: chatId
    };

    await delay(2000);

    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
    console.log("Message sent with image:", "fileUrl");
});

stompClient.onmessage = function (message) {
    console.log("Received:", message.body);
};

stompClient.onerror = function (error) {
    console.log("WebSocket Error:", error);
};

stompClient.onclose = function () {
    console.log("Connection closed");
};


```



