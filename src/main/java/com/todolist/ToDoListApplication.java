package com.todolist;

import com.todolist.core.Task;
import com.todolist.core.ToDo;
import com.todolist.db.TaskDAO;
import com.todolist.db.ToDoDAO;
import com.todolist.resources.ToDoResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class ToDoListApplication extends Application<ToDoListConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ToDoListApplication().run(args);
    }

    private final HibernateBundle<ToDoListConfiguration> hibernateBundle =
            new HibernateBundle<ToDoListConfiguration>(ToDo.class, Task.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(ToDoListConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "ToDoList";
    }

    @Override
    public void initialize(final Bootstrap<ToDoListConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(ToDoListConfiguration configuration, Environment environment) {
        final ToDoDAO toDoDAO = new ToDoDAO(hibernateBundle.getSessionFactory());
        final TaskDAO taskDAO = new TaskDAO(hibernateBundle.getSessionFactory());

        environment.jersey().register(new ToDoResource(toDoDAO, taskDAO));
    }

}
