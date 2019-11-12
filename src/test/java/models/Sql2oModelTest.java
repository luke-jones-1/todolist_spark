package models;

import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oModelTest {


    Sql2o sql2o = new Sql2o("jdbc:postgresql://localhost:5432/" + "todolist-spark-test",
            null, null, new PostgresQuirks() {
        {
            // make sure we use default UUID converter.
            converters.put(UUID.class, new UUIDConverter());
        }
    });

    UUID id = UUID.fromString("49921d6e-e210-4f68-ad7a-afac266278cb");

    @BeforeAll
    static void setUpClass() {
        BasicConfigurator.configure();
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost:5432/todolist-spark-test", null, null).load();
        flyway.migrate();

    }
    @BeforeEach
    void setUp() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("insert into todos(item_id, title, item) VALUES (:item_id, 'Things', 'Buy stamps')")
                .addParameter("item_id", id)
                .executeUpdate();
        System.out.println(id);
        conn.commit();
    }

    @AfterEach
    void tearDown() {
        Connection conn = sql2o.beginTransaction();
        conn.createQuery("TRUNCATE TABLE todos")
                .executeUpdate();
        conn.commit();
    }

    @Test
    void createItem() {

    }

    @Test
    void getAllItems() {
        Model model = new Sql2oModel(sql2o);
        List<TodoItem> items =  new ArrayList<TodoItem>();
        items.add(new TodoItem(id, "Things", "Buy stamps"));
        assertEquals(model.getAllItems(), items);
    }


}