package com.vee.demo.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;

public class MessageList extends Div {

    public MessageList() {
        addClassName("message-list");
    }
    
    @Override
    public void add(Component... arg0) {
        super.add(arg0);
        
        arg0[arg0.length-1].getElement().callFunction("scrollIntoView");
    }
    
}
