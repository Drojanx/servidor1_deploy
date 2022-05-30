En esta rama del proyecto podemos lanzar un docker-compose para tener tanto la aplicación como la base de datos
MySql ejecutandose en local en contenedores docker:
````
docker-compose up -d
````

````
docker run -d -p 3306:3306 --name bookshelter-db -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=bsapi -e MYSQL_USER=bsuser -e MYSQL_PASSWORD=100316 mysql
````