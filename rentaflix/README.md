

Avance Final Portafolio de Evidencias — Programación Orientada a Objetos

---



## Compilar y ejecutar

```bash
# Compilar y empaquetar
mvn clean package -q

# Ejecutar
java -jar target/rentaflix-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Estructura del proyecto

```
src/main/java/cr/cenfotec/rentaflix/
├── Main.java                         
├── model/
│   ├── Persona.java                 
│   ├── Cliente.java                 
│   ├── Direccion.java                
│   ├── Pelicula.java                 
│   ├── Renta.java                   
│   └── Genero.java                   
├── exception/
│   ├── RentaException.java          
│   ├── PeliculaNoDisponibleException.java
│   ├── PeliculaNotFoundException.java
│   ├── ClienteNotFoundException.java
│   └── DAOException.java
├── dao/
│   ├── GenericDAO.java             
│   ├── PeliculaDAO.java
│   ├── ClienteDAO.java
│   ├── RentaDAO.java
│   └── impl/
│       ├── PeliculaDAOImpl.java     
│       ├── ClienteDAOImpl.java
│       └── RentaDAOImpl.java
├── controller/
│   └── RentaController.java         
├── view/
│   └── ConsoleView.java              
├── pattern/observer/
│   ├── RentaObserver.java
│   ├── EventoRenta.java
│   └── NotificadorConsola.java
└── util/
    ├── DatabaseConnection.java        
    └── DatabaseInitializer.java
