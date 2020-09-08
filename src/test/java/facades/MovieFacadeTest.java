package facades;

import dto.MovieDTO;
import utils.EMF_Creator;
import entities.Movie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class MovieFacadeTest {

    private static EntityManagerFactory emf;
    private static MovieFacade facade;

    public MovieFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = MovieFacade.getMovieFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Movie.deleteAllRows").executeUpdate();
            em.persist(new Movie("The Shining", "Stanley Kubrick", 146, 1980));
            em.persist(new Movie("Doctor Sleep", "Mike Flanagan", 152 , 2019));
            em.persist(new Movie("The Shining", "Mick Garris", 273 , 1997));
            em.persist(new Movie("2001: A Space Oddysey", "Stanley Kubrick", 164 , 1968));
            
            //Making sure this'll be the last movie, makes database more predictable
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(new Movie("Perfect Blue", "Satoshi Kon", 90 , 1997));

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testNumberOfMovies() {
        assertEquals(5, facade.getMovieCount(), "Expects five rows in the database");
    }

    @Test
    public void testGetAllMoviesBySize() {
        int expectedSize = 5;
        int actualSize;
        
        actualSize = facade.getAllMovies().size();
        
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetAllMoviesByLastElement() {
        String expectedTitle = "Perfect Blue";
        String expectedDirector = "Satoshi Kon";
        int expectedRuntime = 90;
        int expectedYear = 1997;
        
        MovieDTO actualLastElement = facade.getAllMovies().get(4);
        
        assertEquals(expectedTitle, actualLastElement.getTitle());
        assertEquals(expectedDirector, actualLastElement.getDirector());
        assertEquals(expectedRuntime, actualLastElement.getRuntime());
        assertEquals(expectedYear, actualLastElement.getReleaseYear());
    }
    
    @Test
    public void testGetMoviesByTitleBySize() {
        String titleToGet = "The Shining";
        int expectedSize = 2;
        
        int actualSize = facade.getMoviesByTitle(titleToGet).size();
        
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetMoviesByTitle() {
        String titleToGet = "The Shining";
        
        List<MovieDTO> actualList = facade.getMoviesByTitle(titleToGet);
        
        assertEquals(titleToGet, actualList.get(0).getTitle());
        assertEquals(titleToGet, actualList.get(1).getTitle());
    }
    
    @Test
    public void testGetMoviesByDirector() {
        String directorToGet = "Stanley Kubrick";
        
        List<MovieDTO> actualList = facade.getMoviesByDirector(directorToGet);
        
        assertEquals(directorToGet, actualList.get(0).getDirector());
        assertEquals(directorToGet, actualList.get(1).getDirector());
    }
    
    @Test
    public void testGetMoviesByDirectorBySize() {
        String directorToGet = "Stanley Kubrick";
        int expectedSize = 2;
        
        int actualSize = facade.getMoviesByDirector(directorToGet).size();
        
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetMoviesByYear() {
        int yearToGet = 1997;
        
        List<MovieDTO> actualList = facade.getMoviesByYear(yearToGet);
        
        assertEquals(yearToGet, actualList.get(0).getReleaseYear());
        assertEquals(yearToGet, actualList.get(1).getReleaseYear());
    }
    
    @Test
    public void testGetMoviesByYearBySize() {
        int yearToGet = 1997;
        int expectedSize = 2;
        
        int actualSize = facade.getMoviesByYear(yearToGet).size();
        
        assertEquals(expectedSize, actualSize);
    }
    
    @Test
    public void testGetMovieById() {
        int idToCheck = 5;
        String expectedTitle = "Perfect Blue";
        String expectedDirector = "Satoshi Kon";
        int expectedRuntime = 90;
        int expectedYear = 1997;
        
        MovieDTO movie = facade.getMovieById(idToCheck);
        
        assertEquals(expectedTitle, movie.getTitle());
        assertEquals(expectedDirector, movie.getDirector());
        assertEquals(expectedRuntime, movie.getRuntime());
        assertEquals(expectedYear, movie.getReleaseYear());
    }
    
    @Test
    public void testAddMovie() {
        String title = "Twin Peaks: Fire Walk With Me";
        String director = "David Lynch";
        int runtime = 135;
        int year = 1992;
        
        facade.addMovie(title, director, runtime, year);
        
        //At this point in the tests, the database will have been cleaned and 
        // refilled multiple times, so the ids have progressed quite far
        MovieDTO movie = facade.getMovieById(21); 
        
        assertEquals(title, movie.getTitle());
        assertEquals(director, movie.getDirector());
        assertEquals(runtime, movie.getRuntime());
        assertEquals(year, movie.getReleaseYear());
    }
}
