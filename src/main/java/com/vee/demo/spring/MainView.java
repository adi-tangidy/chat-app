package com.vee.demo.spring;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

@Route
@Push
@StyleSheet("frontend://styles/styles.css")
public class MainView extends VerticalLayout {

    private String username;
    private UnicastProcessor<ChatMessage> publisher;
    private Flux<ChatMessage> messages;

    public MainView(UnicastProcessor<ChatMessage> publisher, Flux<ChatMessage> messages) {
        this.publisher = publisher;
        this.messages = messages;
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        setClassName("main-view");
        
        H1 header = new H1("Vaadin Chat"); 
        header.getElement().getThemeList().add("dark");
        add(header);
        
        askUsername();
    }

    private void askUsername() {
        HorizontalLayout usernameLayout = new HorizontalLayout();
        TextField txtUsername = new TextField();
        Button btnUsername = new Button("Start Chat");
        btnUsername.addClickListener(click -> {
           username = txtUsername.getValue();
           remove(usernameLayout);
           showChat();
        });
        usernameLayout.add(txtUsername, btnUsername);
        add(usernameLayout);
    }

    private void showChat() {
        MessageList msgList = new MessageList();
        add(msgList, createInputLayout());
        expand(msgList);
        
        messages.subscribe(message -> {
            getUI().ifPresent(ui -> ui.access(()->{
                msgList.add(new Paragraph(
                        message.getFrom()+ ": " + message.getMessage()
                ));
            }));
        });
    }

    private Component createInputLayout() {
        HorizontalLayout inputLayout = new HorizontalLayout();
        inputLayout.setWidth("100%");
        TextField msgField = new TextField();
        Button sendButton = new Button("Chat");
        sendButton.addClickListener(click -> {
           publisher.onNext(new ChatMessage(this.username, msgField.getValue()));
           msgField.clear();
           msgField.focus();
        });
        msgField.focus();
        sendButton.getElement().getThemeList().add("primary");
        inputLayout.add(msgField, sendButton);
        inputLayout.expand(msgField);
        return inputLayout;
    }

}
