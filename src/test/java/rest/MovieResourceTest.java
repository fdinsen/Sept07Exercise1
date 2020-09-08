package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Movie;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Movie m1, m2, m3, m4, m5;
    
    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer(){
        //System.in.read();
         //Don't forget this, if you called its counterpart in @BeforeAll
         EMF_Creator.endREST_TestWithDB();
         httpServer.shutdownNow();
    }
    
    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        m1 = new Movie("The_Shining", "Stanley Kubrick", 146, 1980);
        m2 = new Movie("Doctor_Sleep", "Mike Flanagan", 152 , 2019);
        m3 = new Movie("The_Shining", "Mick Garris", 273 , 1997);
        m4 = new Movie("2001:_A_Space_Oddysey", "Stanley Kubrick", 164 , 1968);
        m5 = new Movie("Perfect_Blue", "Satoshi Kon", 90 , 1997);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(m1);
            em.persist(m2);
            em.persist(m3);
            em.persist(m4);
            em.persist(m5);
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void logging() {
        given().log().all().when().get("/movie/all").then().log().body();
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/movie").then().statusCode(200);
    }
   
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Hello World"));   
    }
    
    @Test
    public void testCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/movie/count").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(5));   
    }
    
    @Test
    public void testGetAllOnSize() {
        given()
         .contentType("application/json")
         .get("/movie/all").then()
         .assertThat()
         .statusCode(HttpStatus.OK_200.getStatusCode())
         .body("", hasSize(5));                        
    }
    
        
    @Test
    public void testGetAllOnContent() {
        given()
         .contentType("application/json")
         .get("/movie/all").then()
         .assertThat()
         .statusCode(HttpStatus.OK_200.getStatusCode())
         .body("title", hasItem("Perfect_Blue"));                        
    }
    
    @Test
    public void testGetMovieByTitleOnSize() {
        given()
         .contentType("application/json")
         .get("/movie/title/The_Shining").then()
         .assertThat()
         .statusCode(HttpStatus.OK_200.getStatusCode())
         .body("", hasSize(2));
    }
    
    @Test
    public void testGetMovieByTitleOnContent() {
        given()
         .contentType("application/json")
         .get("/movie/title/The_Shining").then()
         .assertThat()
         .statusCode(HttpStatus.OK_200.getStatusCode())
         .body("director", hasItem("Stanley Kubrick"));
    }
    
    @Test
    public void testGetMovieByTitleException() {
        given()
         .contentType("application/json")
         .get("/movie/title/Pet_Semetary").then()
         .assertThat()
         .body("", hasSize(0));
    }
    
    @Test
    public void testGetMovieById() {
        given()
         .contentType("application/json")
         .get("/movie/" + m5.getId()).then()
         .assertThat()
         .body("title", equalTo(m5.getTitle()));
    }
    
}
