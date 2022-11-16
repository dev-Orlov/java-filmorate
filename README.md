# java-filmorate

Схема базы данных

![Схема базы данных](https://github.com/dev-Orlov/java-filmorate/blob/main/chart_v6.png?raw=true)

**Фильмы**  
1. В таблице film хранится информация о фильмах. 
Отдельно стоит обратить внимание на поле rating: возможные варианты рейтинга хранятся отдельно, в ENUM.
2. Так как у фильма может быть сразу несколько жанров, информация о них хранится в отдельной таблице film_genre.
Поле genre_name этой таблицы в ENUM содержит возможные варианты жанров.
3. Для хранения лайков служит таблица likes. Фильмам в этой таблице соответствует пользователь, поставивший 
лайк.

*Примеры запросов для операций с фильмами*

**getFilm()**  
SELECT *,  
FROM film  
WHERE film_id = 1;  

**getAll()**  
SELECT *,  
FROM film;  

**getPopularFilms()**  
SELECT *    
FROM film AS f  
RIGHT JOIN likes AS l ON l.film_id = f.film_id  
GROUP BY film_id   
ORDER BY COUNT(film_id) DESC
LIMIT 10;

**Пользователи** 
1. В таблице user хранится информация о пользователях.
2. В таблице friends хранятся id друзей пользователя
Поле status таблицы friends хранит в ENUM значение подтвержденной или неподтвержденной заявки в друзья.
3. В таблице likes хранятся лайки пользователя. Пользователям соответствуют понравившиеся фильмы.

*Примеры запросов для операций с пользователями*

**getUser()**  
SELECT *,  
FROM user  
WHERE user_id = 1;

**getAll()**  
SELECT *,  
FROM user;

**getFriendList()**  
SELECT *
FROM user AS u  
RIGHT JOIN friends AS f ON u.user_id = f.friend_id  
GROUP BY user_id;