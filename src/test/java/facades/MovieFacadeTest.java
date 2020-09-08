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
    
    private static Movie m1;
    private static Movie m2;
    private static Movie m3;
    private static Movie m4;
    private static Movie m5;

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
        m1 = new Movie("The Shining", "Stanley Kubrick", 146, 1980);
        m2 = new Movie("Doctor Sleep", "Mike Flanagan", 152 , 2019);
        m3 = new Movie("The Shining", "Mick Garris", 273 , 1997);
        m4 = new Movie("2001: A Space Oddysey", "Stanley Kubrick", 164 , 1968);
        m5 = new Movie("Perfect Blue", "Satoshi Kon", 90 , 1997);
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testGetMovieById() {
        String expectedTitle = m5.getTitle();
        String expectedDirector = m5.getDirector();
        int expectedRuntime = m5.getRuntime();
        int expectedYear = m5.getReleaseYear();
        
        MovieDTO movie = facade.getMovieById(m5.getId());
        
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
       
        MovieDTO movie = facade.getMoviesByDirector("David Lynch").get(0); 
        
        assertEquals(title, movie.getTitle());
        assertEquals(director, movie.getDirector());
        assertEquals(runtime, movie.getRuntime());
        assertEquals(year, movie.getReleaseYear());
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
    public void testGetAllMoviesOnAllElements() {
        //Rigtig grim løsning, men nu gider jeg ikke tænke mere over det.
        for(MovieDTO movie : facade.getAllMovies()) {
            switch(movie.getRuntime()) {
                case 146:
                    assertEquals(m1.getTitle(), movie.getTitle());
                    assertEquals(m1.getDirector(), movie.getDirector());
                    assertEquals(m1.getRuntime(), movie.getRuntime());
                    assertEquals(m1.getReleaseYear(), movie.getReleaseYear());
                    break;
                case 152:
                    assertEquals(m2.getTitle(), movie.getTitle());
                    assertEquals(m2.getDirector(), movie.getDirector());
                    assertEquals(m2.getRuntime(), movie.getRuntime());
                    assertEquals(m2.getReleaseYear(), movie.getReleaseYear());
                    break;
                case 273:
                    assertEquals(m3.getTitle(), movie.getTitle());
                    assertEquals(m3.getDirector(), movie.getDirector());
                    assertEquals(m3.getRuntime(), movie.getRuntime());
                    assertEquals(m3.getReleaseYear(), movie.getReleaseYear());
                    break;
                case 164:
                    assertEquals(m4.getTitle(), movie.getTitle());
                    assertEquals(m4.getDirector(), movie.getDirector());
                    assertEquals(m4.getRuntime(), movie.getRuntime());
                    assertEquals(m4.getReleaseYear(), movie.getReleaseYear());
                    break;
                case 90:
                    assertEquals(m5.getTitle(), movie.getTitle());
                    assertEquals(m5.getDirector(), movie.getDirector());
                    assertEquals(m5.getRuntime(), movie.getRuntime());
                    assertEquals(m5.getReleaseYear(), movie.getReleaseYear());
                    break;
            }
        }
        
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
}
