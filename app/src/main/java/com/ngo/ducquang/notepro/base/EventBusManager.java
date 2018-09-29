package com.ngo.ducquang.notepro.base;

import org.greenrobot.eventbus.EventBus;

import java.util.logging.LogManager;

/**
 * Created by ducqu on 7/28/2018.
 */

public class EventBusManager {
    private final EventBus eventBus = EventBus.getDefault();

    private static EventBusManager instance = null;

    public static EventBusManager instance()
    {
        synchronized (EventBusManager.class)
        {
            if (instance == null)
            {
                instance = new EventBusManager();
            }

            return instance;
        }
    }

    private EventBusManager()
    {
    }

    public void post(Object object)
    {
        if(object != null && eventBus != null )
        {
            eventBus.post(object);
        }
        else
        {
        }
    }

    public void register(Object object)
    {
        if(object != null && eventBus != null && (!eventBus.isRegistered(object)))
        {
            eventBus.register(object);
        }
        else
        {
        }
    }

    public void unregister(Object object)
    {
        if(object != null && eventBus != null)
        {
            eventBus.unregister(object);
        }
        else
        {
        }
    }

    public void postSticky(Object object)
    {
        if(object != null && eventBus != null)
        {
            eventBus.postSticky(object);
        }
        else
        {
        }
    }
}
