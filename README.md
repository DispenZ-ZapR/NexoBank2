# NexoBank2 - Инструкция по запуску

## Что это за проект?

**NexoBank2** — автоматизированная информационная система управления банковскими операциями и персоналом банка. Приложение позволяет:
- Регистрировать клиентов и сотрудников банка
- Управлять банковскими счетами (создание, блокировка, активация)
- Выполнять денежные переводы между счетами
- Обрабатывать заявки на открытие счетов
- Отслеживать историю операций и транзакций
- Управлять паспортными данными клиентов
- Аутентифицировать пользователей через JWT токены

---

## Что вам понадобится для запуска

### 1. Java 17 или выше

**Как проверить:**
```bash
java -version
```

**Если Java не установлена:**
- **Windows:** Скачайте с [oracle.com/java](https://www.oracle.com/java/technologies/downloads/) или [OpenJDK](https://adoptium.net/)
- **Linux:**
  ```bash
  sudo apt install openjdk-17-jdk
  ```
- **Mac:**
  ```bash
  brew install openjdk@17
  ```

### 2. Maven

**Как проверить:**
```bash
mvn -version
```

**Если Maven не установлен:**
- **Windows:** Скачайте с [maven.apache.org](https://maven.apache.org/download.cgi)
- **Linux:**
  ```bash
  sudo apt install maven
  ```
- **Mac:**
  ```bash
  brew install maven
  ```

### 3. PostgreSQL

Приложение использует PostgreSQL в качестве базы данных.

**Установка:**
- **Windows:** Скачайте с [postgresql.org](https://www.postgresql.org/download/)
- **Linux:**
  ```bash
  sudo apt install postgresql postgresql-contrib
  ```
- **Mac:**
  ```bash
  brew install postgresql
  ```

---

## Шаг 1: Получение проекта

### Вариант A: Клонирование через Git

```bash
git clone <repository-url>
cd NexoBank2
```

### Вариант B: Распаковка архива

1. Распакуйте архив в любую папку
2. Откройте терминал и перейдите в папку проекта:
   ```bash
   cd /путь/к/NexoBank2
   ```

---

## Шаг 2: Настройка базы данных

### 1. Создайте базу данных PostgreSQL

```bash
# Войдите в PostgreSQL
psql -U postgres

# Создайте базу данных
CREATE DATABASE nexobank2;

# Выйдите
\q
```

### 2. Настройте переменные окружения

Создайте файл `.env` в корне проекта или установите переменные окружения:

**Windows (PowerShell):**
```powershell
$env:DB_URL="jdbc:postgresql://localhost:5433/nexobank2"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="ваш_пароль"
$env:JWT_SECRET_KEY="ваш_секретный_ключ_минимум_32_символа"
$env:JWT_TOKEN_TIME="3600000"
```

**Linux/Mac:**
```bash
export DB_URL="jdbc:postgresql://localhost:5433/nexobank2"
export DB_USERNAME="postgres"
export DB_PASSWORD="ваш_пароль"
export JWT_SECRET_KEY="ваш_секретный_ключ_минимум_32_символа"
export JWT_TOKEN_TIME="3600000"
```

**Или создайте файл `.env`:**
```properties
DB_URL=jdbc:postgresql://localhost:5433/nexobank2
DB_USERNAME=postgres
DB_PASSWORD=ваш_пароль
JWT_SECRET_KEY=ваш_секретный_ключ_минимум_32_символа
JWT_TOKEN_TIME=3600000
```

---

## Шаг 3: Сборка проекта

В терминале выполните:

```bash
mvn clean compile
```

**Успешный результат:**
```
BUILD SUCCESS
```

---

## Шаг 4: Запуск приложения

```bash
mvn spring-boot:run
```

**Успешный запуск:**
Вы увидите в логах:
```
Started NexoBank2Application in X.XXX seconds
Tomcat started on port(s): 8080
```

**Важно:**
- Оставьте терминал открытым — приложение работает пока открыт терминал
- Чтобы остановить приложение, нажмите `Ctrl+C`

---

## Шаг 5: Проверка работы приложения

### Swagger UI (OpenAPI)

Откройте браузер и перейдите:
```
http://localhost:8080/swagger-ui.html
```


### Тестирование через curl

**1. Регистрация клиента:**
```bash
curl -X POST http://localhost:8080/api/client\save \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ivan@example.com",
    "phoneNumber": "+996555123456",
    "firstName": "Иван",
    "lastName": "Петров",
    "middleName": "Сергеевич",
    "dateOfBirth": "1990-01-15",
    "personalNumber": "12345678901234",
    "passportNumber": "AN1234567"
  }'
```

**Важно:** После регистрации на указанную почту придет ссылка для активации аккаунта. Перейдите по ссылке и установите пароль.

**2. Активация аккаунта (установка пароля):**
```bash
curl -X POST http://localhost:8080/api/user/verify/{token} \
  -H "Content-Type: application/json" \
  -d '{
    "password": "SecurePassword123"
  }'
```
Где `{token}` - это токен из ссылки, полученной на email.

**3. Вход в систему:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "ivan@example.com",
    "password": "SecurePassword123"
  }'
```

Ответ содержит JWT токен:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**4. Получение списка счетов (требуется токен):**
```bash
curl -X GET http://localhost:8080/api/accounts \
  -H "Authorization: Bearer ваш_токен"
```

---

## Структура проекта

```
NexoBank2/
├── src/
│   ├── main/
│   │   ├── java/com/example/nexobank2/
│   │   │   ├── config/              # Конфигурация (Security, OpenAPI)
│   │   │   ├── controller/          # REST API endpoints
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── entity/              # JPA сущности (модели БД)
│   │   │   ├── enums/               # Перечисления (статусы, типы)
│   │   │   ├── event/               # События приложения
│   │   │   ├── exception/           # Кастомные исключения
│   │   │   ├── mapper/              # MapStruct маперы
│   │   │   ├── repository/          # JPA репозитории
│   │   │   ├── security/            # JWT фильтры
│   │   │   └── service/             # Бизнес-логика
│   │   └── resources/
│   │       ├── db/migration/        # Flyway миграции
│   │       └── application.properties
│   └── test/                        # Тесты
├── pom.xml                          # Maven конфигурация
└── README.md                        # Этот файл
```

---

## Основные API Endpoints

### Аутентификация
- `POST /api/auth/login` — Вход в систему

### Клиенты
- `GET /api/clients` — Список клиентов
- `POST /api/clients` — Регистрация клиента
- `GET /api/clients/{id}` — Получить клиента
- `PUT /api/clients/{id}` — Обновить клиента
- `DELETE /api/clients/{id}` — Удалить клиента

### Счета
- `GET /api/accounts` — Список счетов
- `GET /api/accounts/{id}` — Получить счет
- `POST /api/accounts/freeze/{id}` — Заморозить счет
- `POST /api/accounts/unblock/{id}` — Разблокировать счет

### Операции
- `POST /api/operations/transfer` — Перевод между счетами
- `GET /api/operations/my` — Мои операции
- `GET /api/operations/{uuid}` — Детали операции

### Заявки на счета
- `POST /api/account-requests` — Создать заявку
- `GET /api/account-requests` — Список заявок
- `POST /api/account-requests/{id}/approve` — Одобрить заявку
- `POST /api/account-requests/{id}/reject` — Отклонить заявку

### Управление персоналом
- `GET /api/employees` — Список сотрудников
- `POST /api/employees` — Создать сотрудника
- `GET /api/employees/{id}` — Получить сотрудника

---

## Возможные проблемы и решения

### Проблема: "Порт 8080 уже занят"

**Решение:**
Измените порт в `application.properties`:
```properties
server.port=8081
```

### Проблема: "Не удается подключиться к базе данных"

**Решение:**
1. Убедитесь, что PostgreSQL запущен:
   ```bash
   # Linux/Mac
   sudo systemctl status postgresql
   
   # Windows
   # Проверьте в Services (services.msc)
   ```

2. Проверьте настройки подключения в переменных окружения
3. Убедитесь, что база данных `nexobank2` создана

### Проблема: "JWT Secret Key не установлен"

**Решение:**
Установите переменную окружения `JWT_SECRET_KEY` (минимум 32 символа):
```bash
export JWT_SECRET_KEY="your-very-long-secret-key-at-least-32-characters"
```

### Проблема: Ошибки миграций Flyway

**Решение:**
Очистите базу данных и запустите заново:
```sql
DROP DATABASE nexobank2;
CREATE DATABASE nexobank2;
```

---

## Технологический стек

- **Java 17** — язык программирования
- **Spring Boot 4.0.3** — фреймворк
- **Spring Security** — аутентификация и авторизация
- **JWT (JSON Web Tokens)** — токены доступа
- **Spring Data JPA** — работа с базой данных
- **PostgreSQL** — реляционная база данных
- **Flyway** — миграции базы данных
- **MapStruct** — маппинг объектов
- **Lombok** — уменьшение boilerplate кода
- **Springdoc OpenAPI** — документация API
- **Scalar** — альтернативная документация API

---

## Полезные ссылки

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

---

## Что дальше?

После успешного запуска вы можете:
1. Зарегистрировать клиента через API
2. Войти в систему и получить JWT токен
3. Создать заявку на открытие счета
4. Одобрить заявку (от имени сотрудника)
5. Выполнить перевод между счетами
6. Просмотреть историю операций

**Удачной работы с NexoBank2!**
