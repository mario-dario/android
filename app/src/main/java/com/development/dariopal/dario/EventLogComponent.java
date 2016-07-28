package com.labstyle.darioandroid.hackathon;

import com.labstyle.darioandroid.dagger2.components.DarioAppGlobalComponents;
import com.labstyle.darioandroid.dagger2.scope.UserScope;

import dagger.Component;

/**
 * Created by mario on 27/07/16.
 */

@UserScope
@Component(dependencies = DarioAppGlobalComponents.class)
public interface EventLogComponent
{
    DataBroadcasterManager getEventBroadcaster();

}
