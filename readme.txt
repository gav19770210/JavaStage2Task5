Структура проекта:
src/main/java/ru/gav19770210/stage2task05/controller - контроллеры по обработке POST запросов.
src/main/java/ru/gav19770210/stage2task05/service - сервис по созданию созданию ЭП, ПР и выбору номера счёта из пула счетов.
src/main/java/ru/gav19770210/stage2task05/entity - модель БД.
src/main/java/ru/gav19770210/stage2task05/repository - репозитории по работе с моделью БД.
src/main/java/ru/gav19770210/stage2task05/request - структуры запросов.
src/main/java/ru/gav19770210/stage2task05/response - структуры ответов.
src/main/java/ru/gav19770210/stage2task05/json - валидация запросов json на соответствие схемам.

В разработке использованы следующие зависимости:
1. lombok - для автоматической генерации стандарного кода.
2. json-schema-validator - для валидации запросов json на корректность, в том числе на наличие обазательных полей.
   Для валидации используются json схемы хранящиеся в ресурсах src/main/resources/templates
3. flyway-core - для создания на основе sql скриптов структуры базы данных и первичного наполнения её данными.
   SQL скрипты расположены в src/main/resources/db.migration
4. hamcrest + mockito-core - для создания модульных тестов.
5. io.rest-assured + org.testcontainers.postgresql - для создания интеграционных тестов.

Для тестирования используются отдельные ресурсы:
src/test/resources/json - тестовые json запросы использующиеся для тестирования.
src/test/resources/db_scripts - sql скрипты flyway для формирования тестового окружения БД.

Для проверки работы микросервиса использовался Postman.
Тестовые запросы использовались те же, что и для модульного и интеграционного тестирования.

Сервис разворачивается в докере на основе файлов docker-compose.yml и Dockerfile в корне проекта.
