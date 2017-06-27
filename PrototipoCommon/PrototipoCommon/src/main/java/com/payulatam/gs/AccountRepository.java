package com.payulatam.gs;

import java.math.BigDecimal;

import org.openspaces.core.GigaSpace;

import com.payulatam.model.Account;

/**
 * Implementation of repository by GigaSpace for Account
 * @author John Quiroga
 *
 * @param <BaseEntity>
 */
public class AccountRepository<J extends Account> extends AbstractRepository<Account> implements IAccountRepository {

	public AccountRepository(GigaSpace gigaSpace) {
		super.gigaSpace = gigaSpace;
	}

	@Override
	public Account[] serach(String customerId, String accountNumber, BigDecimal balance) {
		String queryString = generateStringQuery(customerId, accountNumber, balance);
		Account[] result = findByCriteria(queryString);
		return result;
	}
	
	public String generateStringQuery(String customerId, String accountNumber, BigDecimal balance) {
		StringBuilder query = new StringBuilder();
		if (customerId != null && !"*".equals(customerId)) {
			query.append("customerId = '" + customerId + "'");
		}
		
		if (!"*".equals(accountNumber) && !accountNumber.isEmpty()) {
			if (!query.toString().isEmpty()) {
				query.append(" and ");
			}
			if (accountNumber.contains("*")) {
				String toReplace = accountNumber; 
				toReplace = toReplace.replaceAll("\\*", "\\%");
				query.append(String.format(" number like '%s' ", toReplace));
			} else {
				query.append(String.format(" number = '%s' ", accountNumber));
			}
		}
		if (balance != null) {
			if (!query.toString().isEmpty()) {
				query.append(" and ");
			}
			query.append(String.format(" balance = %s ", balance));
		}
		
		return query.toString();
	}
	
}
