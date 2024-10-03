package com.panel.application.views.addclient;

import com.panel.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import com.panel.application.data.Client;
import com.panel.application.services.ClientService;

@PageTitle("Add Client")
@Route(value = "add-client", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AddClientView extends Composite<VerticalLayout> {
	
	private final ClientService clientService;
	
    public AddClientView(ClientService ClientService) {
    	this.clientService = ClientService;
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        TextField firstName = new TextField();
        TextField lastName = new TextField();
        DatePicker birthDate = new DatePicker();
        TextField phoneNumber = new TextField();
        EmailField emailField = new EmailField();
        TextField occupation = new TextField();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Personal Information");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        firstName.setLabel("First Name");
        lastName.setLabel("Last Name");
        birthDate.setLabel("Birthday");
        phoneNumber.setLabel("Phone Number");
        emailField.setLabel("Email");
        occupation.setLabel("Occupation");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        buttonPrimary.setText("Save");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(event -> addClient(firstName, lastName, birthDate, phoneNumber, emailField, occupation));
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        buttonSecondary.addClickListener(event -> refreshPage());
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(firstName);
        formLayout2Col.add(lastName);
        formLayout2Col.add(birthDate);
        formLayout2Col.add(phoneNumber);
        formLayout2Col.add(emailField);
        formLayout2Col.add(occupation);
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
    }
    
    
    public void addClient(TextField firstName, TextField lastName, DatePicker birthDate, TextField phoneNumber, EmailField emailField, TextField occupation) {
    	final Client client = new Client();
    	
    	client.setFirstName(firstName.getValue());
    	client.setLastName(lastName.getValue());    	
    	client.setDateOfBirth(birthDate.getValue());
    	client.setPhone(phoneNumber.getValue());
    	client.setEmail(emailField.getValue());
    	client.setOccupation(occupation.getValue());
    	client.setImportant(false);
    	
    	if(client.getFirstName() == "" || client.getLastName() == "" || client.getPhone() == "" || client.getOccupation() == ""){
    		Notification notification = new Notification("Please provide all required data!", 3000, Notification.Position.MIDDLE);
            notification.open();
            return;    		
    	}  			
    	
    	Client newClient = clientService.add(client);
    	
    	if(newClient != null) {
    		Notification notification = new Notification("Client added successfully!", 3000, Notification.Position.MIDDLE);
            notification.open();
    	}else {
    		Notification notification = new Notification("Something went wrong with adding new client!", 3000, Notification.Position.MIDDLE);
            notification.open();    		
    	}  	    	   	
        
    }
    
    private void refreshPage() {
        Notification.show("Refreshing the page...");
        UI.getCurrent().getPage().reload();
    }
    
        
}
