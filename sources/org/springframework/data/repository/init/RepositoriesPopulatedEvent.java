package org.springframework.data.repository.init;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.repository.support.Repositories;
import org.springframework.util.Assert;

/* loaded from: spring-data-commons-1.13.23.RELEASE.jar:org/springframework/data/repository/init/RepositoriesPopulatedEvent.class */
public class RepositoriesPopulatedEvent extends ApplicationEvent {
    private static final long serialVersionUID = 7449982118828889097L;
    private final Repositories repositories;

    public RepositoriesPopulatedEvent(RepositoryPopulator populator, Repositories repositories) {
        super(populator);
        Assert.notNull(populator, "Populator must not be null!");
        Assert.notNull(repositories, "Repositories must not be null!");
        this.repositories = repositories;
    }

    @Override // java.util.EventObject
    public RepositoryPopulator getSource() {
        return (RepositoryPopulator) super.getSource();
    }

    public Repositories getRepositories() {
        return this.repositories;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        RepositoriesPopulatedEvent that = (RepositoriesPopulatedEvent) obj;
        return this.source.equals(that.source) && this.repositories.equals(that.repositories);
    }

    public int hashCode() {
        int result = 17 + (31 * this.source.hashCode());
        return result + (31 * this.repositories.hashCode());
    }
}
