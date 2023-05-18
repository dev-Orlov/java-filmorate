Filmorate
========================
Сервис, который даёт возможность пользователям выбирать наиболее популярные фильмы, а также оценивать и комментировать их.
***

Функции
-------------------------
* Настроено управление пользователями и фильмами
* Доступен поиск наиболее популярных фильмов
* Создан механизм комментирования и оценки фильмов
* Настроен механизм добавления пользователей в друзья

Схема базы данных
-------------------------

![Схема базы данных](https://raw.githubusercontent.com/dev-Orlov/java-filmorate/add-database/chart_v8.png)

**Фильмы**

1. В таблице films хранится информация о фильмах.
   Отдельно стоит обратить внимание на поле rating_id: возможные варианты рейтинга хранятся отдельно, в таблице raring.
2. Так как у фильма может быть сразу несколько жанров, информация о них хранится в отдельной таблице film_genre.
   Список всех доступных жанров хранится в таблице genres.
3. Для хранения лайков служит таблица likes. Фильмам в этой таблице соответствует пользователь, поставивший
   лайк.

<details>
<summary>Примеры запросов для операций с фильмами</summary>

**getFilm()**
```SQL 
SELECT * 
FROM films 
WHERE film_id = 1;
```

**getAll()** 
```SQL 
SELECT *
FROM film;
```
</details>

**Пользователи**
1. В таблице users хранится информация о пользователях.
2. В таблице friends хранятся id друзей пользователя
   Поле status таблицы friends хранит в boolean значение подтвержденной или неподтвержденной заявки в друзья.
3. В таблице likes хранятся лайки пользователя. Пользователям соответствуют понравившиеся фильмы.

<details>
<summary>Примеры запросов для операций с пользователями</summary>

**getUser()** 

```SQL 
SELECT *
FROM users
WHERE user_id = 1;
```

**getAll()** 

```SQL 
SELECT *
FROM users;
```

**getFriendList()** 

```SQL 
SELECT * 
FROM users AS u 
RIGHT JOIN friends AS f ON u.user_id = f.friend_id 
GROUP BY user_id;
```
</details>

Технологический стек
-------------------------
Java 11, Spring Boot, Maven, REST, Lombok, SQL, Postgres, JDBC, JPA, Junit.

Требования
-------------------------
* JDK 11
* Apache Maven 3.6.0

Установка
-------------------------
Сборка:
>mvn clean package

Запуск:
> mvn spring-boot:run