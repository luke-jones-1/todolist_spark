package models;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;
import java.util.UUID;

public class Sql2oModel implements Model {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;

    }

    @Override
    public UUID createItem(String title, String content) {
        try (Connection conn = sql2o.beginTransaction()) {
            UUID itemUuid = UUID.randomUUID();
            conn.createQuery("insert into todos(item_id, title, item) VALUES (:item_id, :title, :content)")
                    .addParameter("item_id", itemUuid)
                    .addParameter("title", title)
                    .addParameter("content", content)
                    .executeUpdate();
            conn.commit();
            return itemUuid;
        }
    }

    @Override
    public List<TodoItem> getAllItems() {
        try (Connection conn = sql2o.open()) {
            List<TodoItem> posts = conn.createQuery("select * from todos;")
                    .executeAndFetch(TodoItem.class);
            return posts;
        }
    }


    @Override
    public boolean existItem(UUID item) {
        try (Connection conn = sql2o.open()) {
            List<TodoItem> posts = conn.createQuery("select * from todos where item_uuid=:item")
                    .addParameter("item", item)
                    .executeAndFetch(TodoItem.class);
            return posts.size() > 0;
        }
    }

}