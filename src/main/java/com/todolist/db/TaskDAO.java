package com.todolist.db;

import com.todolist.core.Task;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Optional;

public class TaskDAO extends AbstractDAO<Task> {

    public TaskDAO(SessionFactory factory) {
        super(factory);
    }

    public void createTask(Task task) {
        persist(task);
    }

    public Optional<Task> getTaskDetails(Long taskID) {
        return Optional.ofNullable(get(taskID));
    }

    public List<Task> getTasksList() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<Task> criteria = builder.createQuery(Task.class);
        criteria.from(Task.class);
        return currentSession().createQuery(criteria).getResultList();
    }

    public void updateTask(Task task){
        this.currentSession().update(Task.class.getName(), task);
    }

    public void deleteTask(Task task) {
        this.currentSession().delete(Task.class.getName(), task);
    }
}
