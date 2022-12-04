# java-filmorate

Схема базы данных

![Схема базы данных](https://raw.githubusercontent.com/dev-Orlov/java-filmorate/add-database/chart_v8.png)

**Фильмы**  
1. В таблице films хранится информация о фильмах. 
Отдельно стоит обратить внимание на поле rating_id: возможные варианты рейтинга хранятся отдельно, в таблице raring.
2. Так как у фильма может быть сразу несколько жанров, информация о них хранится в отдельной таблице film_genre.
Список всех доступных жанров хранится в таблице genres.
3. Для хранения лайков служит таблица likes. Фильмам в этой таблице соответствует пользователь, поставивший 
лайк.

*Примеры запросов для операций с фильмами*

**getFilm()**  
SELECT *,  
FROM films  
WHERE film_id = 1;  

**getAll()**  
SELECT *,  
FROM film;

**Пользователи** 
1. В таблице users хранится информация о пользователях.
2. В таблице friends хранятся id друзей пользователя
Поле status таблицы friends хранит в boolean значение подтвержденной или неподтвержденной заявки в друзья.
3. В таблице likes хранятся лайки пользователя. Пользователям соответствуют понравившиеся фильмы.

*Примеры запросов для операций с пользователями*

**getUser()**  
SELECT *,  
FROM users  
WHERE user_id = 1;

**getAll()**  
SELECT *,  
FROM users;

**getFriendList()**  
SELECT *
FROM users AS u  
RIGHT JOIN friends AS f ON u.user_id = f.friend_id  
GROUP BY user_id;