-- ============================================
-- СКРИПТ ПОДГОТОВКИ БД ДЛЯ ПРЕЗЕНТАЦИИ
-- NexoBank2 Demo Data
-- ============================================

-- 1. ОЧИСТКА ДАННЫХ (в правильном порядке!)
-- ============================================

-- Удаляем транзакции
TRUNCATE TABLE transaction CASCADE;

-- Удаляем операции
TRUNCATE TABLE operation CASCADE;

-- Удаляем заявки на счета
TRUNCATE TABLE account_request CASCADE;

-- Удаляем счета
TRUNCATE TABLE account CASCADE;

-- Удаляем клиентов и сотрудников
TRUNCATE TABLE client CASCADE;
TRUNCATE TABLE employee CASCADE;
TRUNCATE TABLE employee_positions_map CASCADE;

-- Удаляем пользователей и паспорта
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE passport CASCADE;

-- Сбрасываем последовательности
ALTER SEQUENCE IF EXISTS passport_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS users_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS client_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employee_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS account_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS account_request_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS transaction_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS account_type_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS account_currency_id_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS employee_position_id_seq RESTART WITH 1;

-- 2. СПРАВОЧНИКИ (если их нет)
-- ============================================

-- Типы счетов
INSERT INTO account_type (id, name) VALUES
(1, 'Текущий')
ON CONFLICT (id) DO NOTHING;

-- Синхронизируем sequence
SELECT setval('account_type_id_seq', (SELECT COALESCE(MAX(id), 1) FROM account_type));

-- Валюты
INSERT INTO account_currency (id, code, name, digital_code) VALUES
(1, 'KGS', 'Сом', '417')
ON CONFLICT (id) DO NOTHING;

-- Синхронизируем sequence
SELECT setval('account_currency_id_seq', (SELECT COALESCE(MAX(id), 1) FROM account_currency));

-- Должности
INSERT INTO employee_position (id, name) VALUES
(1, 'Менеджер'),
(2, 'Кассир'),
(3, 'Администратор')
ON CONFLICT (id) DO NOTHING;

-- Синхронизируем sequence
SELECT setval('employee_position_id_seq', (SELECT COALESCE(MAX(id), 1) FROM employee_position));

-- 3. ТЕСТОВЫЕ КЛИЕНТЫ
-- ============================================

-- Клиент 1: Иван Петров (активный)
INSERT INTO passport (id, first_name, last_name, middle_name, date_of_birth, personal_number, passport_number, is_lost)
VALUES (1, 'Иван', 'Петров', 'Сергеевич', '1990-05-15', '12345678901234', 'AN1234567', false);

INSERT INTO users (id, passport_id, password_hash, email, user_type, phone_number, created_at, deleted_at)
VALUES (1, 1, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- пароль: password123
        'ivan.petrov@example.com', 'CLIENT', '+996555123456', NOW(), NULL);

INSERT INTO client (id, user_id, created_at, credit_rating, client_status)
VALUES (1, 1, NOW(), 750, 'ACTIVE');

-- Клиент 2: Мария Сидорова (активная)
INSERT INTO passport (id, first_name, last_name, middle_name, date_of_birth, personal_number, passport_number, is_lost)
VALUES (2, 'Мария', 'Сидорова', 'Александровна', '1995-08-20', '98765432109876', 'AN9876543', false);

INSERT INTO users (id, passport_id, password_hash, email, user_type, phone_number, created_at, deleted_at)
VALUES (2, 2, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'maria.sidorova@example.com', 'CLIENT', '+996555654321', NOW(), NULL);

INSERT INTO client (id, user_id, created_at, credit_rating, client_status)
VALUES (2, 2, NOW(), 800, 'ACTIVE');

-- Клиент 3: Алексей Иванов (неактивированный)
INSERT INTO passport (id, first_name, last_name, middle_name, date_of_birth, personal_number, passport_number, is_lost)
VALUES (3, 'Алексей', 'Иванов', 'Дмитриевич', '1988-03-10', '11122233344455', 'AN1112223', false);

INSERT INTO users (id, passport_id, password_hash, email, user_type, phone_number, created_at, deleted_at, ac_token, activation_token_expires_at)
VALUES (3, 3, NULL, 'alexey.ivanov@example.com', 'CLIENT', '+996555111222', NOW(), NULL, 
        'demo-activation-token-123', NOW() + INTERVAL '24 hours');

INSERT INTO client (id, user_id, created_at, credit_rating, client_status)
VALUES (3, 3, NOW(), 0, 'UNVERIFIED');

-- Синхронизируем sequences
SELECT setval('passport_id_seq', (SELECT MAX(id) FROM passport));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('client_id_seq', (SELECT MAX(id) FROM client));

-- 4. ТЕСТОВЫЕ СОТРУДНИКИ
-- ============================================

-- Сотрудник 1: Анна Менеджерова
INSERT INTO passport (id, first_name, last_name, middle_name, date_of_birth, personal_number, passport_number, is_lost)
VALUES (4, 'Анна', 'Менеджерова', 'Викторовна', '1985-12-05', '55566677788899', 'AN5556667', false);

INSERT INTO users (id, passport_id, password_hash, email, user_type, phone_number, created_at, deleted_at)
VALUES (4, 4, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'anna.manager@nexobank.kg', 'EMPLOYEE', '+996555999888', NOW(), NULL);

INSERT INTO employee (id, user_id, salary, hired_at, employee_status)
VALUES (1, 4, 50000.00, '2023-01-15', 'ACTIVE');

INSERT INTO employee_positions_map (employee_id, position_id)
VALUES (1, 1); -- Менеджер

-- Синхронизируем sequences
SELECT setval('passport_id_seq', (SELECT MAX(id) FROM passport));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('employee_id_seq', (SELECT MAX(id) FROM employee));

-- 5. БАНКОВСКИЕ СЧЕТА
-- ============================================

-- Счета Ивана Петрова
INSERT INTO account (id, date_created, account_type_id, balance, account_number, currency_id, status, client_id)
VALUES 
(1, NOW(), 1, 50000.00, '20250115000000001', 1, 'ACTIVE', 1), -- Текущий KGS
(2, NOW(), 1, 100000.00, '20250115000000002', 1, 'ACTIVE', 1); -- Текущий KGS

-- Счета Марии Сидоровой
INSERT INTO account (id, date_created, account_type_id, balance, account_number, currency_id, status, client_id)
VALUES 
(3, NOW(), 1, 75000.00, '20250115000000003', 1, 'ACTIVE', 2), -- Текущий KGS
(4, NOW(), 1, 50000.00, '20250115000000004', 1, 'ACTIVE', 2), -- Текущий KGS
(5, NOW(), 1, 25000.00, '20250115000000005', 1, 'FROZEN', 2); -- Заблокированный KGS

-- Синхронизируем sequence
SELECT setval('account_id_seq', (SELECT MAX(id) FROM account));

-- 6. ОПЕРАЦИИ И ТРАНЗАКЦИИ
-- ============================================

-- Успешная операция: Иван → Мария (5000 KGS)
INSERT INTO operation (id, initiator_id, channel, status, reason, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440001', 1, 'WEB', 'SUCCESSFULLY', 'Оплата за услуги', NOW() - INTERVAL '2 days');

INSERT INTO transaction (id, account_id, operation_id, transaction_type, amount, transaction_after, transaction_date)
VALUES 
(1, 1, '550e8400-e29b-41d4-a716-446655440001', 'DEBIT', 5000.00, 45000.00, NOW() - INTERVAL '2 days'),
(2, 3, '550e8400-e29b-41d4-a716-446655440001', 'CREDIT', 5000.00, 80000.00, NOW() - INTERVAL '2 days');

-- Неудачная операция: Иван пытался перевести больше, чем есть
INSERT INTO operation (id, initiator_id, channel, status, reason, created_at)
VALUES ('550e8400-e29b-41d4-a716-446655440002', 1, 'WEB', 'FAILED', 'Перевод другу', NOW() - INTERVAL '1 day');

-- Синхронизируем sequence
SELECT setval('transaction_id_seq', (SELECT MAX(id) FROM transaction));

-- 7. ЗАЯВКИ НА СЧЕТА
-- ============================================

-- Одобренная заявка (уже создан счет)
INSERT INTO account_request (id, client_id, account_type_id, currency_id, status, requested_at, processed_at, approved_by_employee_id)
VALUES (1, 1, 1, 1, 'APPROVED', NOW() - INTERVAL '5 days', NOW() - INTERVAL '4 days', 1);

-- Ожидающая заявка (для демонстрации одобрения)
INSERT INTO account_request (id, client_id, account_type_id, currency_id, status, requested_at)
VALUES (2, 2, 1, 1, 'PENDING', NOW() - INTERVAL '1 hour');

-- Отклоненная заявка
INSERT INTO account_request (id, client_id, account_type_id, currency_id, status, requested_at, processed_at, approved_by_employee_id, rejection_reason)
VALUES (3, 1, 1, 1, 'REJECTED', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days', 1, 'Недостаточный кредитный рейтинг');

-- Синхронизируем sequence
SELECT setval('account_request_id_seq', (SELECT MAX(id) FROM account_request));

-- ============================================
-- ГОТОВО! БД подготовлена для презентации
-- ============================================

-- Проверка данных
SELECT 'Клиенты:' as info, COUNT(*) as count FROM client
UNION ALL
SELECT 'Сотрудники:', COUNT(*) FROM employee
UNION ALL
SELECT 'Счета:', COUNT(*) FROM account
UNION ALL
SELECT 'Операции:', COUNT(*) FROM operation
UNION ALL
SELECT 'Заявки:', COUNT(*) FROM account_request;

-- ============================================
-- УЧЕТНЫЕ ДАННЫЕ ДЛЯ ВХОДА
-- ============================================
-- 
-- КЛИЕНТЫ:
-- 1. Иван Петров
--    Email: ivan.petrov@example.com
--    Пароль: password123
--    Счета: 2 (KGS: 50,000, KGS: 100,000)
--
-- 2. Мария Сидорова
--    Email: maria.sidorova@example.com
--    Пароль: password123
--    Счета: 3 (KGS: 75,000, KGS: 50,000, KGS заблокирован: 25,000)
--
-- 3. Алексей Иванов (неактивированный)
--    Email: alexey.ivanov@example.com
--    Статус: UNVERIFIED
--
-- СОТРУДНИКИ:
-- 1. Анна Менеджерова
--    Email: anna.manager@nexobank.kg
--    Пароль: password123
--    Должность: Менеджер
-- ============================================
