import models.Model;
import models.Sql2oModel;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.UUID;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/todolist_spark", null, null).load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "todolist_spark", null, null, new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);


        get("/", (req, res) -> {
            HashMap todo = new HashMap();
            return new ModelAndView(todo, "templates/index.vtl");
        }, new VelocityTemplateEngine());


        get("/todos", (req, res) -> {

            if(model.getAllItems().size() == 0) {
                UUID id = model.createItem("hello", "world");
            }

            HashMap todos = new HashMap();
            todos.put("todos", model.getAllItems());

            return new ModelAndView(todos, "templates/todos.vtl");
        }, new VelocityTemplateEngine());

        get("/new_todo", (req, res) -> {
            HashMap todo = new HashMap();
            return new ModelAndView(todo, "templates/new_todo.vtl");
        }, new VelocityTemplateEngine());

        post( "/todos", (req, res) -> {
            String title = req.queryParams("todo_title");
            String content = req.queryParams("todo_content");

            UUID id = model.createItem(title, content);

            res.redirect("/todos");

            return null;
        });

        //TODO Add an endpoint which receives a todo item from a form and store this in the database
    }
}
