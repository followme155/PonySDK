/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.core.activity;

import com.ponysdk.core.PonySession;
import com.ponysdk.core.event.BroadcastEventHandler;
import com.ponysdk.core.event.Event;
import com.ponysdk.core.event.Event.Type;
import com.ponysdk.core.event.EventBus;
import com.ponysdk.core.event.EventHandler;
import com.ponysdk.core.event.HandlerRegistration;
import com.ponysdk.core.place.Place;
import com.ponysdk.core.security.Permission;
import com.ponysdk.core.security.SecurityManager;
import com.ponysdk.ui.server.basic.PAcceptsOneWidget;

public abstract class AbstractActivity implements Activity {

    protected boolean started = false;

    protected final Permission permission;

    public AbstractActivity() {
        this.permission = Permission.ALLOWED;
    }

    public AbstractActivity(final Permission permission) {
        this.permission = permission;
    }

    @Override
    public void goTo(final Place place, final PAcceptsOneWidget world) {
        if (!SecurityManager.checkPermission(getPermission())) throw new RuntimeException("Missing permission #" + getPermission());

        if (!started) {
            start(world);
            started = true;
        }
    }

    protected abstract void start(PAcceptsOneWidget world);

    @Override
    public Permission getPermission() {
        return permission;
    }

    public EventBus getRootEventBus() {
        return PonySession.getRootEventBus();
    }

    public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
        return PonySession.addHandler(type, handler);
    }

    public <H extends EventHandler> HandlerRegistration addHandlerToSource(final Type<H> type, final Object source, final H handler) {
        return PonySession.addHandlerToSource(type, source, handler);
    }

    @SuppressWarnings("unchecked")
    public <H extends EventHandler> HandlerRegistration addHandlerToSource(final Type<H> type, final Object source) {
        return addHandlerToSource(type, source, (H) this);
    }

    public void fireEvent(final Event<?> event) {
        PonySession.fireEvent(event);
    }

    public void fireEventFromSource(final Event<?> event, final Object source) {
        PonySession.fireEventFromSource(event, source);
    }

    public void addHandler(final BroadcastEventHandler handler) {
        PonySession.addHandler(handler);
    }
}