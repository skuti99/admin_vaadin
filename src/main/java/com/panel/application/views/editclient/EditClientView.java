package com.panel.application.views.editclient;

import com.panel.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

import java.util.Optional;

import com.panel.application.data.Client;
import com.panel.application.services.ClientService;

@PageTitle("Edit Client")
@Route(value = "edit-client", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class EditClientView extends Composite<VerticalLayout>        

	implements HasUrlParameter<Long>, BeforeEnterObserver{
	
	private final ClientService clientService;
	private Long clientId;
	private Client client;
	private ConfirmDialog notFoundDialog = new ConfirmDialog();
	private ConfirmDialog saveDialog = new ConfirmDialog();
	private TextField firstName = new TextField();
	private TextField lastName = new TextField();
	private DatePicker birthDate = new DatePicker();
	private TextField phoneNumber = new TextField();
	private EmailField emailField = new EmailField();
	private TextField occupation = new TextField();
	
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long clientId) {
        this.clientId=clientId;
    }    
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
     
        if (this.clientId == null) {
           openRedirectDialog(event);          

        } else {
            loadClient(this.clientId, event);
        }
    }
    
    
    

	public EditClientView(ClientService ClientService) {
		this.clientService = ClientService;
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
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
        h3.setText("Edit Personal Information");
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
        buttonPrimary.addClickListener(event -> saveClient());
        buttonSecondary.setText("Cancel");
        buttonSecondary.setWidth("min-content");
        buttonSecondary.addClickListener(event -> UI.getCurrent().navigate("/"));
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
	
	
		
    private void loadClient(Long id, BeforeEnterEvent event) {
    	this.clientId = id;  	
    	UI.getCurrent().getPage().executeJs( "console.log('Client ID:'+$0);", id.toString()) ;	
    	
    	Optional<Client> optClient = clientService.get(id);
    	
    	if(optClient.isPresent()) {
    		this.client=optClient.get();
    		firstName.setValue(client.getFirstName());
            lastName.setValue(client.getLastName());
            birthDate.setValue(client.getDateOfBirth());
            phoneNumber.setValue(client.getPhone());
            emailField.setValue(client.getEmail());
            occupation.setValue(client.getOccupation());
    	}else {
    		openRedirectDialog(event);
    	}   				
	}	
    
    
    private void openRedirectDialog(BeforeEnterEvent event1) {
    	notFoundDialog.getElement().setAttribute("aria-label", "Redirection");
    	notFoundDialog.setHeader("Client not found");
    	notFoundDialog.setText("Add new client instead");
    	notFoundDialog.setConfirmText("Ok");
    	notFoundDialog.addConfirmListener(event -> redirect(event1));
    	notFoundDialog.open();
    }    
    private void openSaveDialog(boolean saved) {
    	saveDialog.getElement().setAttribute("aria-label", "Save");
    	if(saved) {
    		saveDialog.setHeader("Saved");    	
        	saveDialog.setText("Client saved successfully");
    	}else {
    		saveDialog.setHeader("Not saved");    	
        	saveDialog.setText("Something went wrong with updating client information");
    	}    	
    	saveDialog.setConfirmText("Ok");
    	saveDialog.addConfirmListener(event -> refreshPage());    	
    	
    	saveDialog.open();
    }
    
        
    private void saveClient() {
    	client.setFirstName(firstName.getValue());
    	client.setLastName(lastName.getValue());
    	client.setDateOfBirth(birthDate.getValue());
    	client.setPhone(phoneNumber.getValue());
    	client.setEmail(emailField.getValue());
    	client.setOccupation(occupation.getValue());
    	
    	Client neww = clientService.update(client);
    	Boolean saved = false;
    	
    	if(neww!=null) {
    		saved=true;
    		client=neww;
    	}    	
    	openSaveDialog(saved);   	  	    	
    	
    }
    
    
    private void refreshPage() {
        Notification.show("Refreshing the page...");
        UI.getCurrent().getPage().reload();
    }
    private void redirect(BeforeEnterEvent event) {
    	UI.getCurrent().getPage().setLocation("add-client");
    	event.rerouteTo("add-client");    	
    }  	
	
}
