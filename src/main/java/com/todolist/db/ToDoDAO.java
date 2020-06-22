package com.todolist.db;

import com.todolist.core.ToDo;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

public class ToDoDAO extends AbstractDAO<ToDo> {
    public ToDoDAO(SessionFactory factory) {
        super(factory);
    }

    public ToDo createToDo(ToDo toDo) {
        return persist(toDo);
    }

    public void updateToDo(ToDo toDo) {
        this.currentSession().update(ToDo.class.getName(), toDo);
    }

    public void deleteToDo(ToDo todo) {
        this.currentSession().delete(ToDo.class.getName(), todo);
    }

    public List<ToDo> getToDoList() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<ToDo> criteria = builder.createQuery(ToDo.class);
        criteria.from(ToDo.class);
        return currentSession().createQuery(criteria).getResultList();
    }

    public Optional<ToDo> getToDoDetails(long toDoID) {
        return Optional.ofNullable(get(toDoID));
    }
}
