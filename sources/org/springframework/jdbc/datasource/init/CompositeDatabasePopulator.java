package org.springframework.jdbc.datasource.init;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/* loaded from: spring-jdbc-4.3.25.RELEASE.jar:org/springframework/jdbc/datasource/init/CompositeDatabasePopulator.class */
public class CompositeDatabasePopulator implements DatabasePopulator {
    private final List<DatabasePopulator> populators = new ArrayList(4);

    public CompositeDatabasePopulator() {
    }

    public CompositeDatabasePopulator(Collection<DatabasePopulator> populators) {
        this.populators.addAll(populators);
    }

    public CompositeDatabasePopulator(DatabasePopulator... populators) {
        this.populators.addAll(Arrays.asList(populators));
    }

    public void setPopulators(DatabasePopulator... populators) {
        this.populators.clear();
        this.populators.addAll(Arrays.asList(populators));
    }

    public void addPopulators(DatabasePopulator... populators) {
        this.populators.addAll(Arrays.asList(populators));
    }

    @Override // org.springframework.jdbc.datasource.init.DatabasePopulator
    public void populate(Connection connection) throws SQLException, ScriptException {
        for (DatabasePopulator populator : this.populators) {
            populator.populate(connection);
        }
    }
}
