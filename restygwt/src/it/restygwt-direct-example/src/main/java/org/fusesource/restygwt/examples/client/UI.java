/**
 * Copyright (C) 2009-2012 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.restygwt.examples.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.text.shared.Renderer;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.OverlayCallback;
import org.fusesource.restygwt.client.REST;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.RestServiceProxy;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 *
 * @author <a href="http://hiramchirino.com">Hiram Chirino</a>
 */
public class UI implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get().add(new Label("Name:"));
        final TextBox nameInput = new TextBox();
        RootPanel.get().add(nameInput);
        nameInput.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                getCustomGreeting(nameInput.getValue());
            }
        });
        nameInput.setValue("ping", true);
    }

    private void getCustomGreeting(String name) {
        GreetingService service = GWT.create(GreetingService.class);
        Resource resource = new Resource(GWT.getModuleBaseURL() + "greeting-service");
        ((RestServiceProxy) service).setResource(resource);

        Overlay overlay = (Overlay) JavaScriptObject.createObject();
        overlay.setStr(name);
        getCall(service, "overlays", new AbstractRenderer<Overlay>() {
            public String render(Overlay object) {
                return object.getStr();
            }
        }).overlay(overlay);

        Interop interop = createInterop();
        interop.setStr(name);
        getCall(service, "interop", new AbstractRenderer<Interop>() {
            public String render(Interop object) {
                return object.getStr();
            }
        }).interop(interop);

        Pojo pojo = new Pojo();
        pojo.setStr(name);
        getCall(service, "pojo", new AbstractRenderer<Pojo>() {
            public String render(Pojo object) {
                return object.getStr();
            }
        }).pojo(pojo);
    }

    private <T> GreetingService getCall(GreetingService service, final String container, final Renderer<T> render) {
        return REST.withCallback(new OverlayCallback<T>() {
            public void onSuccess(Method method, T greeting) {
                RootPanel.get().add(new Label("server said using " + container + ": " +  render.render(greeting)));
            }

            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error: " + exception);
            }
        }).call(service);
    }

    static  native Interop createInterop() /*-{ return {}; }-*/;
}
