package com.payulatam.prototipo;

import java.util.ArrayList;
import java.util.List;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Textbox;

import com.j_spaces.core.client.SQLQuery;
import com.payulatam.common.Constantes;
import com.payulatam.model.Customer;

public class ClienteController extends GenericForwardComposer {
	
	private static final long serialVersionUID = 6077674101236551588L;
	
	private Grid gridCustomers;
	private Textbox textboxCustomer;
	private Textbox textboxAddress;
	private Textbox textboxPhone;
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer(Constantes.JINI);
		GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
        Customer[] spaceEntries = gigaSpace.readMultiple(new Customer(), Integer.MAX_VALUE);
        setModel(spaceEntries);
        
        gridCustomers.setRowRenderer(new RowRenderer() {
            public void render(Row row, Object data) throws Exception {
                final Customer prod = (Customer)data;
                
                new Label(prod.getName()).setParent(row);
                new Label(prod.getAddress()).setParent(row);
                new Label(prod.getPhone()).setParent(row);
            }
        });
    }
	
	public void onClick$buttonSearch() {
		StringBuilder stringQuery = new StringBuilder();
		if (!"*".equals(textboxCustomer.getText()) && !textboxCustomer.getText().isEmpty()) {
			if (textboxCustomer.getText().contains("*")) {
				String toReplace = textboxCustomer.getText(); 
				toReplace = toReplace.replaceAll("\\*", "\\%");
				stringQuery.append(String.format(" name like '%s' ", toReplace));
			} else {
				stringQuery.append(String.format(" name = '%s' ", textboxCustomer.getText()));
			}
		}
		if (!"*".equals(textboxAddress.getText()) && !textboxAddress.getText().isEmpty()) {
			if (!stringQuery.toString().isEmpty()) {
				stringQuery.append(" and ");
			}
			if (textboxAddress.getText().contains("*")) {
				stringQuery.append(String.format(" address like '%s' ", textboxAddress.getText().replaceAll("\\*", "\\%")));
			} else {
				stringQuery.append(String.format(" address = '%s' ", textboxAddress.getText()));
			}
		}
		if (!"*".equals(textboxPhone.getText()) && !textboxPhone.getText().isEmpty()) {
			if (!stringQuery.toString().isEmpty()) {
				stringQuery.append(" and ");
			}
			if (textboxAddress.getText().contains("*")) {
				stringQuery.append(String.format(" phone like '%s' ", textboxPhone.getText()).replaceAll("\\*", "\\%"));
			} else {
				stringQuery.append(String.format(" phone = '%s' ", textboxPhone.getText()));
			}
		}
		SQLQuery<Customer> query = new SQLQuery<Customer>(Customer.class, stringQuery.toString());
		System.out.println(stringQuery.toString());
		
		UrlSpaceConfigurer spaceConfigurer = new UrlSpaceConfigurer(Constantes.JINI);
		GigaSpace gigaSpace = new GigaSpaceConfigurer(spaceConfigurer).gigaSpace();
		Customer[] result = gigaSpace.readMultiple(query);
		setModel(result);
	}
	
	private void setModel(Customer[] customers) {
		List<Customer> customerResult = new ArrayList<>();
		for (Customer customer : customers) {
			customerResult.add(customer);
		}
		ListModelList prodModel = new ListModelList(customerResult);
		gridCustomers.setModel(prodModel);
	}
	
}
