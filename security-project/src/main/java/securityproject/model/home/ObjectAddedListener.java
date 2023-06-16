package securityproject.model.home;


import org.drools.core.event.DefaultAgendaEventListener;
import org.kie.api.event.rule.AfterMatchFiredEvent;

import java.util.ArrayList;
import java.util.List;

public class ObjectAddedListener implements WorkingMemoryEventListener {

    private List<Object> insertedObjects = new ArrayList<>();

    @Override
    public void objectInserted(ObjectInsertedEvent event) {
        Object insertedObject = event.getObject();
        insertedObjects.add(insertedObject);
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent event) {
        // Ignore object update events
    }

    @Override
    public void objectDeleted(ObjectDeletedEvent event) {
        // Ignore object deletion events
    }

    public List<Object> getInsertedObjects() {
        return insertedObjects;
    }
}