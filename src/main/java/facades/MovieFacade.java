package facades;

import dto.MovieDTO;
import entities.Movie;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;
    
    //Private Constructor to ensure Singleton
    private MovieFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    

    public long getMovieCount(){
        EntityManager em = emf.createEntityManager();
        try{
            long movieCount = (long)em.createQuery("SELECT COUNT(m) FROM Movie m").getSingleResult();
            return movieCount;
        }finally{  
            em.close();
        }
        
    }
    
    public List<MovieDTO> getAllMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);
            List<MovieDTO> listOfMovies = new ArrayList();
            query.getResultList().stream().forEach(m -> {
                listOfMovies.add(new MovieDTO(m));
            });
            return listOfMovies;
        }finally {
            em.close();
        }
    }
    
    public List<MovieDTO> getMoviesByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.title = :title", Movie.class);
            query.setParameter("title", title);
            List<MovieDTO> listOfMovies = new ArrayList();
            query.getResultList().stream().forEach(m -> {
                listOfMovies.add(new MovieDTO(m));
            });
            return listOfMovies;
        }finally {
            em.close();
        }
    }

    public List<MovieDTO> getMoviesByDirector(String director) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.director = :director", Movie.class);
            query.setParameter("director", director);
            List<MovieDTO> listOfMovies = new ArrayList();
            query.getResultList().stream().forEach(m -> {
                listOfMovies.add(new MovieDTO(m));
            });
            return listOfMovies;
        }finally {
            em.close();
        }
    }
    
    public List<MovieDTO> getMoviesByYear(int releaseYear) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.releaseYear = :releaseYear", Movie.class);
            query.setParameter("releaseYear", releaseYear);
            List<MovieDTO> listOfMovies = new ArrayList();
            query.getResultList().stream().forEach(m -> {
                listOfMovies.add(new MovieDTO(m));
            });
            return listOfMovies;
        }finally {
            em.close();
        }
    }
    
    public MovieDTO getMovieById(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class);
            query.setParameter("id", id);
            MovieDTO movie = new MovieDTO(query.getSingleResult());
            return movie;
        }finally {
            em.close();
        }
    }
    
    public Movie addMovie(String title, String director, int runtime, int releaseYear) {
        EntityManager em = emf.createEntityManager();
        Movie movie = new Movie(title, director, runtime, releaseYear);
        em.getTransaction().begin();
        em.persist(movie);
        em.getTransaction().commit();
        em.close();
        return movie;
    }
            
    
    
}
