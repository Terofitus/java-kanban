package server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import models.SimpleTask;
import models.Status;
import models.Subtask;
import models.Task;

import java.io.IOException;
import java.util.ArrayList;

public class ArrayListOfSortedTasksAdapter extends TypeAdapter<ArrayList<Task>> {
    @Override
    public void write(JsonWriter jsonWriter, ArrayList<Task> taskArrayList) throws IOException {
        jsonWriter.beginArray();
        for (Task task : taskArrayList) {
            jsonWriter.beginObject();
            jsonWriter.name("name").value(task.getName());
            jsonWriter.name("description").value(task.getDescription());
            jsonWriter.name("status").value(String.valueOf(task.getStatus()));
            jsonWriter.name("start time").value(task.getStartTime().toString());
            jsonWriter.name("duration").value(task.getDurationOfTask());
            if (task instanceof Subtask) {
                jsonWriter.name("id of epic").value(((Subtask) task).getEpicID());
            }
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
    }

    @Override
    public ArrayList<Task> read(JsonReader jsonReader) throws IOException {
        ArrayList<Task> tasksFromJson = new ArrayList<>();
        jsonReader.beginArray();
        while (jsonReader.hasNext()) {
            jsonReader.beginObject();
            jsonReader.nextName();
            String name = jsonReader.nextString();
            jsonReader.nextName();
            String description = jsonReader.nextString();
            jsonReader.nextName();
            jsonReader.skipValue();
            jsonReader.nextName();
            Status status;
            switch (jsonReader.nextString()) {
                case "DONE":
                    status = Status.DONE;
                    break;
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                    break;
                default:
                    status = Status.NEW;
                    break;
            }
            jsonReader.nextName();
            String startTime = jsonReader.nextString();
            jsonReader.nextName();
            Integer duration = jsonReader.nextInt();
            if (jsonReader.peek() == JsonToken.STRING) {
                Integer idOfEpic = jsonReader.nextInt();
                Subtask subtask = new Subtask(name, description, idOfEpic, status, startTime, duration);
                tasksFromJson.add(subtask);
            } else {
                SimpleTask simpleTask = new SimpleTask(name, description, status, startTime, duration);
                tasksFromJson.add(simpleTask);
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();
        return tasksFromJson;
    }
}
