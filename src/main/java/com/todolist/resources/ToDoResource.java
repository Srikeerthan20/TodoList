package com.todolist.resources;

import com.todolist.core.Task;
import com.todolist.core.ToDo;
import com.todolist.db.TaskDAO;
import com.todolist.db.ToDoDAO;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/api/todos")
@Produces(MediaType.APPLICATION_JSON)
public class ToDoResource {
    private final ToDoDAO todoDAO;
    private final TaskDAO taskDAO;

    public ToDoResource(ToDoDAO todoDAO, TaskDAO taskDAO) {
        this.todoDAO = todoDAO;
        this.taskDAO = taskDAO;
    }

    @GET
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public List<ToDo> getAllToDos() {
        return todoDAO.getToDoList();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<ToDo> getToDo(@PathParam("id") Long id) {
        return todoDAO.getToDoDetails(id);
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ToDo createToDO(@NotNull @Valid ToDo todo) {
        List<Task> tasks = todo.getTasks();
        ToDo newToDo = todoDAO.createToDo(todo);
        if (tasks != null) {
            for (Task task : tasks) {
                task.setTodo(newToDo);
                taskDAO.createTask(task);
            }
        }
        return newToDo;
    }

    @PUT
    @Path("/{id}")
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Optional<ToDo> updateToDo(@PathParam("id") Long id, ToDo todo) {
        todo.setId(id);
        todoDAO.updateToDo(todo);
        for (Task task : todo.getTasks()) {
            task.setTodo(todo);
            taskDAO.updateTask(task);
        }
        return todoDAO.getToDoDetails(todo.getId());
    }

    @DELETE
    @Path("/{id}")
    @UnitOfWork
    public void deleteTask(@PathParam("id") Long id) {
        Optional<ToDo> toDoDetails = todoDAO.getToDoDetails(id);
        toDoDetails.ifPresent(todoDAO::deleteToDo);
    }

}
