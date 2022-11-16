# java-filmorate

Схема базы данных

![Схема базы данных](https://github.com/dev-Orlov/java-filmorate/blob/main/chart.png?raw=true)

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
2. Та