# CotizadorPrimaUnica
## Ejecutando la Configuración de Docker Compose

Para iniciar los contenedores como se define en tu archivo docker-compose.yml, navega al directorio que contiene el archivo y ejecuta:

```java
docker-compose up -d
```

Este comando inicia los contenedores en modo desatendido. Puedes usar el siguiente comando para detener y eliminar los contenedores, redes y volúmenes definidos en la configuración:

```java
docker-compose down -v
```  

## Ejecutar la Aplicación


Asegúrate de que MongoDB está corriendo en tu máquina local. Ahora, puedes ejecutar tu aplicación Spring Boot directamente desde tu IDE o usando Maven en la terminal:

```java
mvn spring-boot:run
```  