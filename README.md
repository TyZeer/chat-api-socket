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

# Создание чатов
## /api/chat-rooms
* `POST` /group
```json
{
  "name": "Рабочая группа",
  "memberIds": [1, 2, 3, 4]
}

```
* `POST` /private
```json
{
    "name" : "Name",
    "user1Id" : 1,
    "user2Id" : 2
}

```
* `POST` /{chatID}/add-user?userId=100 (Добавление пользователя)
* `GET` /my (Вернет список чатов пользователя)
# Сбор всех сообщений из чата
* `GET` /api/chat-messages/{chatRoomId} (Вернет список всех сообщений в чате)
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
http://minio-server:9000/chat-bucket/filename.jpg
```
* `POST` /{filename} (Пример скрипта для получения файла Используется для отображения/скачивания файла из чата)
```js
fetch('/имя-файла.ext')
  .then(response => {
    if (!response.ok) throw new Error('File not found');
    return response.blob(); // или .text() для текстовых файлов
  })
  .then(blob => {
    // Обработка файла
    const url = URL.createObjectURL(blob);
    window.open(url); // Открыть в новой вкладке
    // или скачать:
    const a = document.createElement('a');
    a.href = url;
    a.download = 'filename.ext';
    a.click();
  })
  .catch(error => console.error('Error:', error));
```
## Получение списка пользователей
* `GET` /api/users (Вернет список пользователей)

# WEBSOCKET
# Тут я просто вставлю пример того как я отправил сообщение, никнейм пользователя надо запоминать при успешном логине или регистрации
Обязательно надо добавлять jwt в хедере, иначе нельзя отправлять и получать сообщения
```js
const WebSocket = require('ws');
const Stomp = require('stompjs');
const fs = require('fs');
const axios = require('axios');
const FormData = require('form-data');

const socket = new WebSocket("ws://localhost:8080/ws");
const stompClient = Stomp.over(socket);

const delay = ms => new Promise(res => setTimeout(res, ms));

const jwtToken = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOjEsImVtYWlsIjoiZW1haWxAZ21haWwuY29tIiwic3ViIjoiVHl6ZXIiLCJpYXQiOjE3NDM4NTg0NDksImV4cCI6MTc0NDcyMjQ0OX0.yEVVSyLJZyRsApX-h7jEYIDK6KNbghx06mKSj0j7HQr9V-6qb7nUkUbeB0S8LQ0iT4dK8cux7rMEomlLYWydGQ";
const chatId = 1;
const sender = "Tyzer";

// Указанный тобой путь к файлу
const filePath = "C:\\Users\\Dmitriy Pavlov\\OneDrive\\Рабочий стол\\test-photo.jpg";

// Функция загрузки файла в MinIO через API
async function uploadFile(filePath) {
    const formData = new FormData();
    formData.append("file", fs.createReadStream(filePath));

    try {
        const response = await axios.post("http://localhost:8080/api/files/upload", formData, {
            headers: {
                ...formData.getHeaders(),
                Authorization: `Bearer ${jwtToken}` // Добавляем токен в заголовки
            },
        });
        return response.data; // URL загруженного файла
    } catch (error) {
        console.error("Ошибка загрузки файла:", error.response?.data || error.message);
        return null;
    }
}

stompClient.connect({ Authorization: `Bearer ${jwtToken}` }, async function (frame) {
    console.log("Connected to WebSocket");

    // Загружаем фото и получаем ссылку
    const fileUrl = await uploadFile(filePath);
    if (!fileUrl) {
        console.error("Не удалось загрузить файл, сообщение не отправлено");
        return;
    }

    let message = {
        sender: sender,
        content: "Вот фото!",
        fileUrl: fileUrl, // Используем загруженный URL
        chatId: chatId
    };

    await delay(2000);

    stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(message));
    console.log("Message sent with image:", fileUrl);
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



