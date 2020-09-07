package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import utils.EMF_Creator;
import facades.MovieFacade;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//Todo Remove or change relevant parts before ACTUAL use
@Path("movie")
public class MovieResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    
    //An alternative way to get the EntityManagerFactory, whithout having to type the details all over the code
    //EMF = EMF_Creator.createEntityManagerFactory(DbSelector.DEV, Strategy.CREATE);
    
    private static final MovieFacade FACADE =  MovieFacade.getMovieFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
            
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }
    
    @Path("/count")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getMovieCount() {
        long count = FACADE.getMovieCount();
        //System.out.println("--------------->"+count);
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
    
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMovies() {
        return Response.ok().entity(GSON.toJson(FACADE.getAllMovies())).build();
    }
    
    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(@PathParam("id") int id) {
        return Response.ok().entity(GSON.toJson(FACADE.getMovieById(id))).build();
    }
    
    @Path("/director")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByDirector(@QueryParam("d") String director) {
        return Response.ok().entity(GSON.toJson(FACADE.getMoviesByDirector(director))).build();
    }
    
    @Path("year")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByYear(@QueryParam("y") int year) {
        return Response.ok().entity(GSON.toJson(FACADE.getMoviesByYear(year))).build();
    }
    
    @Path("title")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByTitle(@QueryParam("t") String title) {
        return Response.ok().entity(GSON.toJson(FACADE.getMoviesByTitle(title))).build();
    }
    
    @Path("add")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovie(
        @QueryParam("title") String title,
        @QueryParam("director") String director,
        @QueryParam("runtime") int runtime,
        @QueryParam("year") int year) 
    {
        return Response.ok().entity(GSON.toJson(FACADE.addMovie(title, director, runtime, year))).build();
    }
    
}
