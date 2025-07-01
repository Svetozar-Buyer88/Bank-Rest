Вот улучшенная версия README.md, оптимизированная для отображения на GitHub:


# 🏦 Система управления банковскими картами

> **Статус проекта**: В разработке  
> **Последнее обновление**: 1 июля 2025

Микросервис для управления банковскими картами с безопасным доступом, переводами и автоматическим контролем статусов карт. Реализовано 80% функционала.

## 🚀 Запуск приложения

### Требования
- Java 17+
- PostgreSQL 14+
- Maven 3.8+

### Конфигурация
Создайте `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bankcards
spring.datasource.username=postgres
spring.datasource.password=0
spring.jpa.hibernate.ddl-auto=update

jwt.secret=your-strong-secret-key-512-bit-length
jwt.expiration-ms=900000 # 15 мин
```

### Сборка и запуск
```bash
mvn clean package
java -jar target/bankcards-0.0.1-SNAPSHOT.jar
```

## 📚 Документация API

### 🔐 Аутентификация

| Метод | Эндпоинт              | Описание                | Статус |
|-------|------------------------|-------------------------|--------|
| POST  | `/api/auth/login`      | Вход в систему          | ✅     |
| POST  | `/api/auth/register`   | Регистрация пользователя| ✅     |
| POST  | `/api/auth/register-admin` | Регистрация админа   | ✅     |

<details>
<summary>Пример запроса</summary>

```json
POST /api/auth/login
{
  "username": "user@example.com",
  "password": "password123"
}
```
</details>

### 👤 Управление пользователями (ADMIN)

| Метод | Эндпоинт              | Описание                | Статус |
|-------|------------------------|-------------------------|--------|
| GET   | `/api/users`           | Все пользователи        | ✅     |
| GET   | `/api/users/{id}`      | Получить пользователя   | ✅     |
| DELETE| `/api/users/{id}`      | Удалить пользователя    | ✅     |

### 💳 Управление картами

| Метод | Эндпоинт                     | Описание                          | Доступ | Статус |
|-------|------------------------------|-----------------------------------|--------|--------|
| POST  | `/api/cards`                 | Создать карту                    | USER   | ✅     |
| GET   | `/api/cards`                 | Все карты (ADMIN)                | ADMIN  | ✅     |
| GET   | `/api/cards/my`              | Мои карты                        | USER   | ✅     |
| GET   | `/api/cards/{id}`            | Получить карту по ID              | USER   | ✅     |
| GET   | `/api/cards/user/{userId}`   | Карты пользователя (ADMIN)       | ADMIN  | ✅     |
| DELETE| `/api/cards/{id}`            | Удалить карту                    | USER   | ✅     |
| PATCH | `/api/cards/{id}/block`      | Заблокировать карту              | ❌     | 🚧     |
| PATCH | `/api/cards/{id}/activate`   | Активировать карту               | ❌     | 🚧     |

<details>
<summary>Пример создания карты</summary>

```json
POST /api/cards
Authorization: Bearer <token>
{
  "cardNumber": "4111111111111111",
  "ownerName": "IVAN IVANOV",
  "expiryDate": "2026-12-31",
  "balance": 1500.00,
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}
```
</details>

### 🔄 Управление переводами

| Метод | Эндпоинт              | Описание                | Доступ | Статус |
|-------|------------------------|-------------------------|--------|--------|
| POST  | `/api/transfers`       | Создать перевод         | USER   | ✅     |
| GET   | `/api/transfers`       | Все переводы (ADMIN)    | ADMIN  | ✅     |
| GET   | `/api/transfers/user/{userId}` | Переводы пользователя | ADMIN  | ✅     |

<details>
<summary>Пример перевода</summary>

```json
POST /api/transfers
Authorization: Bearer <token>
{
  "fromCardId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "toCardId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "amount": 500.00
}
```
</details>

## ⚠️ Обработка ошибок
Система возвращает структурированные ответы при ошибках:

```json
{
  "message": "Card not found",
  "timestamp": "2025-07-02T12:34:56Z"
}
```

**Коды ошибок:**
| Код | Описание                  |
|-----|---------------------------|
| 400 | Bad Request - Ошибка валидации |
| 401 | Unauthorized - Не авторизован |
| 403 | Forbidden - Доступ запрещен |
| 404 | Not Found - Ресурс не найден |
| 409 | Conflict - Конфликт данных |
| 500 | Internal Server Error - Серверная ошибка |

## 📌 Текущие ограничения
1. **Не реализовано:**
    - Шифрование номеров карт в БД
    - Swagger документация
    - Liquibase миграции
    - Docker окружение
    - Блокировка/активация карт через API

2. **Частично реализовано:**
    - Автоматическое обновление статусов карт (шедулер работает, но требует оптимизации)
    - Проверка прав доступа (требует доработки для некоторых сценариев)

## 🛠 Технологический стек
- **Backend**:
    - Spring Boot 3
    - Spring Security
    - Spring Data JPA
    - Spring Scheduling
- **База данных**: PostgreSQL
- **Аутентификация**: JWT
- **Инструменты**:
    - Lombok
    - Jakarta Validation
    - MapStruct

## 📋 Следующие шаги разработки
1. [ ] Реализовать шифрование данных карт
2. [ ] Добавить Swagger документацию
3. [ ] Настроить Liquibase миграции
4. [ ] Создать Docker окружение
5. [ ] Реализовать эндпоинты блокировки/активации карт
6. [ ] Добавить интеграционные тесты

## ✅ Реализованные функции
- **Аутентификация**: JWT с ролями USER/ADMIN
- **Управление картами**: Создание, просмотр, удаление
- **Переводы**: Между своими картами
- **Автоматические проверки**:
    - Статус карты (активная/просроченная)
    - Достаточность средств
    - Права доступа
- **Пагинация**: Во всех списковых запросах
- **Валидация**: Jakarta Validation
- **Маскирование данных**: Номера карт в ответах
- **Глобальная обработка ошибок**

## 🚧 Функции в разработке
- Шедулер обновления статусов карт
- Улучшение проверки прав доступа
- Тестовое покрытие

## ❌ Не реализовано
- Шифрование чувствительных данных в БД
- Документация API (Swagger)
- Миграции баз данных (Liquibase)
- Docker окружение
- Полное покрытие тестами

## 🧭 Инструкция по использованию
1. Зарегистрируйте пользователя:
   ```bash
   POST /api/auth/register
   {
     "username": "user@example.com",
     "password": "SecurePass123!"
   }
   ```
2. Авторизуйтесь:
   ```bash
   POST /api/auth/login
   {
     "username": "user@example.com",
     "password": "SecurePass123!"
   }
   ```
3. Используйте токен в заголовках:
   ```
   Authorization: Bearer <your-token>
   ```
4. Работайте с API:
    - Создавайте карты (`POST /api/cards`)
    - Просматривайте свои карты (`GET /api/cards/my`)
    - Совершайте переводы (`POST /api/transfers`)
```

Пример создания карты:

json
POST /api/cards
Authorization: Bearer <token>
{
  "cardNumber": "4111111111111111",
  "ownerName": "IVAN IVANOV",
  "expiryDate": "2026-12-31",
  "balance": 1500.00,
  "userId": "3fa85f64-5717-4562-b3fc-2c963f66afa6"
}

Пример перевода:

json
POST /api/transfers
Authorization: Bearer <token>
{
  "fromCardId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "toCardId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
  "amount": 500.00
}
