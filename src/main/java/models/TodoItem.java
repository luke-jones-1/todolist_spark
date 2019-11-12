package models;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;
@Data
public class TodoItem {
    private UUID item_id;
    private String title;
    private String item;

}
