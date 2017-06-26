package com.payulatam.prototipo.movement;

import java.math.BigDecimal;
import java.util.Date;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Decimalbox;

import com.j_spaces.core.client.SQLQuery;
import com.payulatam.common.GigaSpaceController;
import com.payulatam.enums.MovementType;
import com.payulatam.model.Account;
import com.payulatam.model.Movement;

public class MovementDetailController extends GenericForwardComposer {
	
	private static final long serialVersionUID = 6077674101236551588L;
	
	private Movement actualMovement;
	
	private Button buttonNew;
	
	private Combobox comboboxAccount;
	private Combobox comboboxType;
	private Datebox dateboxDate;
	private Decimalbox decimalboxValue;
	
	@Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        SQLQuery<Account> query = new SQLQuery<>(Account.class, "");
        Account[] accounts = GigaSpaceController.getGigaSpace().readMultiple(query);
        for (int i = 0; i < accounts.length; i++) {
        	Comboitem comboitem = new Comboitem(accounts[i].getNumber());
        	comboitem.setValue(accounts[i].getId());
        	comboitem.setParent(comboboxAccount);
		}
        
        if (accounts != null && accounts.length > 0) {
        	comboboxAccount.setSelectedIndex(0);
        }
        
        Comboitem comboitemDebit = new Comboitem(MovementType.DEBIT.toString());
        comboitemDebit.setValue(MovementType.DEBIT.toString());
        comboitemDebit.setParent(comboboxType);
        
        Comboitem comboitemCredit = new Comboitem(MovementType.CREDIT.toString());
        comboitemCredit.setValue(MovementType.CREDIT.toString());
        comboitemCredit.setParent(comboboxType);
        
        comboboxType.setSelectedIndex(0);
        
        dateboxDate.setValue(new Date());
        decimalboxValue.setValue(new BigDecimal(0));
        
        buttonNew.setVisible(true);
    }
	
	public void onClick$buttonNew() {
		actualMovement = new Movement();
		actualMovement.setAccountId("" + comboboxAccount.getSelectedItem().getValue());
		actualMovement.setType("" + comboboxType.getSelectedItem().getValue());
		actualMovement.setDate(dateboxDate.getValue());
		actualMovement.setValue(decimalboxValue.getValue());
		actualMovement.setProcessed(false);
		actualMovement.setSpacerouting(1);
		GigaSpaceController.getGigaSpace().write(actualMovement);
		
		Executions.sendRedirect("/pages/movement/movement.zul");
	}
	
}