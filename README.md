# Пример реализации микросервисного приложения

## Общая схема архитектурного решения:

Приложение состоит из сервисов Order, Billing и Notification, Kitchen, Foodstuff
а также Spring Cloud Gateway API для роутинга запросов на эти сервисы.
Межсервисное взаимодействие осуществляется через брокер сообщений (kafka).
Пользователь взаимодействует с сервисами через вызов REST-сервисов

## Технические подробности реализации

### Установка в k8s через helm:

- Перейти в корневую директорию проекта
- Выполнить `helm install orbilnot ./helm`

### приложение состоит из следующих модулей:

#### billing - Сервис биллинга

Ключевые особенности: Rest Controller, Kafka Consumer, Kafka Producer, Scheduler

- Здесь пользователь может создать новый аккаунт и положить/снять деньги с этого аккаунта через REST
  Controller `AccountController`
- `OrderEventsListener` слушает события заказов из топика `order.event` сообщений и сохраняет их в БД со
  статусом `RECEIVED`
    - Событие состоит из номера счёта, номера заказа, и суммы заказа
- `ReceivedOrderEventsProcessingScheduler` обрабатывает все пришедшие события со статусом `RECEIVED`
    - Если денег на счёте достаточно для оплаты заказа:
        - Деньги списываются со счёта
        - Событие переходит в статус `PAYMENT_CONFIRMED`
    - Если денег на счёте не хватает:
        - Событие переходит в статус `PAYMENT_DENIED`
- `SendPaymentProcessingResultsScheduler`:
    - отправляет сообщения об успешных и неуспешных оплатах в
      топик `payment.event`
    - Переводит событие в терминальный статус `EXECUTED`

#### foodstuff service - Сервис продуктов

Ключевые особенности: Rest Controller, Kafka Consumer, Kafka Producer, Scheduler

- `FoodstuffController` позволяет
    - создать новый продукт (метод POST)
    - изменить кол-во уже созданного продукта (метод PUT). Для данной операции реализована проверка ключа
      идемпотентности.
      В теле сообщения передаётся уникальный UUID запроса (генерируется клиентом). Если запрос с таким ID уже приходил -
      будет
      выбрашено исключение. Если сообщение прошло проверку идемотентности, событие будет сохранено в БД со статусом NEW
- `ProductUpdateEventSendScheduler` вычитывает из БД все события об изменении количества продуктов со статусом NEW и
  отправляет
  для них сообщения в Кафку в топик `product.update.status.events`. После этого сообщение переводится в статус
  `AVAILABLE`.
- `ReservationEventListener` слушает события из топика `reservation.events` и пытается зарезервировать необходимое
  кол-во продуктов для конкретного заказа
    - Если резерв удался (продуктов достаточно) - в топик `reservation.status.events` отправляется событие с типом
      `SUCCESS`
    - Иначе - в топик `reservation.status.events` отправляется событие с типом `FAILED`
- `KitchenStatusEventHandler` слушает события из топика `kitchen.status.events` (статус приготовления заказа)
    - Если блюдо приготовлено (пришло событие `COOKING_COMPLETED`),
      зарезервированные продукты переводятся в статус `COOKED`, резервация переводится в статус `ARCHIVE`
    - Если пришёл статус `CANCELLED` - статус у зарезервированных продуктов меняется на `AVAILABLE` и резервацию
      переводится в статус `ARCHIVE`
- `BillingEventListener` - слушает события из топика `payment.status.events` и если пришло событие c типом
  `PAYMENT_DENIED` (платёж отклонён), меняет статус у зарезервированных продуктов на `AVAILABLE` 
  и переводит резервацию в статус `ARCHIVE`

### kitchen - Сервис кухни
Пользователи сервиса - работники кухни. Сюда приходит информация о новом заказе, который нужно приготовить.
- `KitchenOrderEventListener` - слушает сообщения из топика `kitchen.order.events` и создаёт `KitchenOrder` в статусе `NEW`
- `KitchenOrderController` - служит для изменения статуса заказа. Например, когда блюдо готово, статус заказа меняется на `COOKING_COMPLETED`
- `SendReadyOrdersScheduler` - вычитывает из БД все записи в статусе `COOKING_COMPLETED` и отправляет в топик `kitchen.status.events`, после этого  
  статус записи меняется на `READY`

#### order - Сервис заказов

Ключевые особенности: Rest Controller, Kafka Consumer, Kafka Producer, Scheduler
- `MenuController` 
  - GET - получить список всех пунктов меню (menuItems)
  - POST - создать новый пункт меню
- `OrdersController` - создать заказ и посмотреть список заказов
- Новый заказ создаётся со статусом `NEW` и содержит параметры: номер счёта, номер заказа, сумма заказа
- `SendOrderEventMessagesToTopicScheduler`:
    - берёт все заказы в статусе `NEW`
    - для каждого из заказов производится отправка события заказа в топик `order.event`
    - Переводит заказ в статус `SENT`
- `PaymentEventListener` слушает события об успешных и неуспешных оплатах из топика `payment.event`:
    - Если получено сообщение об успешной оплате, заказ переходит в статус `CONFIRMED`
    - Если получено сообщение о НЕуспешной оплате, заказ переходит в статус `CANCELLED`
- `KitchenStatusEventListener` слушает события топика `kitchen.status.events`
  - Если пришло сообщение со статусом `COOKING_COMPLETED`, запись `Order` меняется на `READY` и запас продуктов переводится в статус `COOKED`
  - Если пришло сообщение со статусом `CANCELLED`, запись `Order` меняется на `CANCELLED` и запас продуктов переводится в статус `ARCHIVE`


#### notification - Сервис уведомлений

Ключевые особенности: Rest Controller, Kafka Consumer
осуществляет рассылку уведомлений пользователям
(в текущей реализации происходит сохранение уведомлений в БД)

- Пользователь может посмотреть список всех своих уведомлений через REST Controller `NotificationsController`
- `PaymentEventListener` слушает события об успешных и неуспешных оплатах из топика `payment.event`:
    - Если пришло сообщение об успешной оплате, создаётся уведомление об успешной оплате
    - Если пришло сообщение об неуспешной оплате, создаётся уведомление о неуспешной оплате

#### gateway - Маршрутизатор запросов

Ключевые особенности: Spring Cloud Gateway Api

Осуществляет роутинг запросов

- Запросы, содержащие путь `/account` роутятся в сервис биллинга
- Запросы, содержащие путь `/notifications` роутятся в сервис нотификаций
- Запросы, содержащие путь `/order` роутятся в сервис заказов
- Запросы, содержащие путь `/kitchen` роутятся в сервис кухни
- Запросы, содержащие путь `/foodstuff` роутятся в сервис продуктов
- Запросы, содержащие путь `/auth` роутятся в сервис авторизации (keycloak)

#### model

Общие классы, используемые в других модулях, в частности DTO для обмена сообщениями

#### migration

Содержит скрипты инициализации БД и последующей миграции данных

- Скрипты содержатся в папке `sql/scripts`

### Брокер сообщений, БД, Миграция

- Для обмена сообщениями между сервисами используется Кафка
- В качестве БД используется postgresql
- Кафка и Postgres устанавливаются вместе с основными сервисами и не требуют дополнительной настройки
- Миграция данных осуществляется с помощью k8s job: orbilnot-migration-job
    - Джоба вызывает liquibase.maven.plugin
    - Поды `billing`, `order`, `notification`, `kitchen`, `foodstuff` ждут, когда джоба завершится и только после этого поднимают свои основные
      контейнеры

## Описание сценариев использования:

### Billing Service

- Создать пользователя (аккаунт в биллинге).
- Положить деньги на счет пользователя в сервисе биллинга.
- Посмотреть состояние счёта

### Foodstuff service (сервис продуктов):

- Создать новый продукт (например, "котлета")
- Изменить кол-во продуктов

### Order Service

- Создать блюдо (например, двойной гамбургер)
- Создать заказ
- Посмотреть список заказов

### Kitchen Service

- Поменять статус заказа (например, на "приготовлено")

### Notification Service

- Отправить нотификацию пользователю
