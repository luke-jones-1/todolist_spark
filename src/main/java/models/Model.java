package models;


import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface Model {
    UUID createItem(String title, String content);
    List getAllItems();
    boolean existItem(UUID item);
}


