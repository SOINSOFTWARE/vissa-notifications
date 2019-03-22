package com.soinsoftware.vissa.client;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.soinsoftware.vissa.bll.CashRegisterConciliationBll;
import com.soinsoftware.vissa.bll.LotBll;
import com.soinsoftware.vissa.commons.CommonsConstants;
import com.soinsoftware.vissa.model.CashConciliation;
import com.soinsoftware.vissa.model.Lot;
import com.soinsoftware.vissa.util.CommonsEmailService;
import com.soinsoftware.vissa.util.DateUtil;
import com.soinsoftware.vissa.util.SmsGenerator;

public class VissaClient {

	protected static final Logger log = Logger.getLogger(VissaClient.class);
	private LotBll lotBll;
	private CashRegisterConciliationBll conciliationBll;

	public static void main(String[] args) {
		// new VissaClient().getProductsToExpire();
		new VissaClient().getDailyConciliation();

	}

	/**
	 * Método para obtener los productos a expirar
	 */
	private void getProductsToExpire() {
		String strLog = "[getProductsToExpire] ";
		try {
			if (CommonsConstants.EXPIRATION_DAYS != null) {
				lotBll = LotBll.getInstance();

				Date expirationDate = DateUtil.addDaysToDate(DateUtil.localDateTimeToDate(DateUtil.getDefaultIniDate()),
						CommonsConstants.EXPIRATION_DAYS);
				List<Lot> lots = lotBll.select(expirationDate);
				log.info(strLog + "Cantidad de lotes por vencer: " + lots.size());

				if (lots.size() > 0) {
					String items = "";
					for (Lot lot : lots) {
						items = items + "Producto: " + lot.getProduct().getName() + ", Lote: " + lot.getCode()
								+ ", Fecha vencimiento: "
								+ DateUtil.dateToString(lot.getExpirationDate(), CommonsConstants.FORMAT_DATE) + ".   ";

					}

					SmsGenerator.sendSMS(CommonsConstants.MESSAGE_EXPIRATION + items);
				}
			} else {
				log.error(strLog + "No hay días de validación para fecha de vencimiennto configurados: "
						+ CommonsConstants.EXPIRATION_DAYS);
			}

		} catch (Exception e) {
			log.error(strLog + "[Excepion]" + e.getMessage());
		}
	}

	/**
	 * Método para obtener los cuadres de caja de un día
	 */
	private void getDailyConciliation() {
		String strLog = "[getDailyConciliation] ";
		try {
			Date date = DateUtils.truncate(new Date(), Calendar.DATE);

			Date endDate = DateUtils.addHours(date, 23);
			endDate = DateUtils.addMinutes(endDate, 59);
			endDate = DateUtils.addSeconds(endDate, 59);

			conciliationBll = CashRegisterConciliationBll.getInstance();
			List<CashConciliation> conciliations = conciliationBll.select(date);
			log.info(strLog + " conciliations: " + conciliations.size());

			String message = CommonsConstants.SALESMAN_CONCILIATION_MSG;
			String subject = "";
			for (CashConciliation conciliation : conciliations) {

				// Si es el cuadre del vendedor
				if (conciliation.getCashRegisterNumber().equals("2")) {
					subject = "Vendedor";
					message = CommonsConstants.SALESMAN_CONCILIATION_MSG;
					message = message.replace("SALESMAN",
							conciliation.getPerson().getName() + " " + conciliation.getPerson().getLastName());
					message = message.replace("BASE", String.valueOf(conciliation.getCashBase()));
					message = message.replace("SALES", String.valueOf(conciliation.getSales()));
					message = message.replace("REMANT_SALE", String.valueOf(conciliation.getRemnantSale()));
					message = message.replace("CREDIT_COLLECTION", String.valueOf(conciliation.getCreditCollection()));
					message = message.replace("TOTAL_SALE", String.valueOf(conciliation.getTotalIngress()));
					message = message.replace("EXPENSES", String.valueOf(conciliation.getGeneralExpense()));
					message = message.replace("SUPPLIER", String.valueOf(conciliation.getSupplierPayments()));
					message = message.replace("REMANT_SALE", String.valueOf(conciliation.getRemnantSale()));
					message = message.replace("TOTAL_EGRESS", String.valueOf(conciliation.getTotalEgress()));
					message = message.replace("TOTAL_CREDIT", String.valueOf(conciliation.getTotalCredit()));
					message = message.replace("TOTAL_CASH", String.valueOf(conciliation.getTotalCash()));
				} else {// Cuadre del administrador
					subject = "Administrador";
					message = CommonsConstants.ADMON_CONCILIATION_MSG;

					message = message.replace("ADMINISTRATOR",
							conciliation.getPerson().getName() + " " + conciliation.getPerson().getLastName());
					message = message.replace("BASE", String.valueOf(conciliation.getCashBase()));
					message = message.replace("SUPPLIER_PAYMENTS", String.valueOf(conciliation.getSupplierPayments()));
					message = message.replace("SUPPLIER_LOANS", String.valueOf(conciliation.getSupplierPaymentsLoan()));
					message = message.replace("BALANCE", String.valueOf(conciliation.getBalance()));

				}

				CommonsEmailService.send(CommonsConstants.MAIL_FROM,
						Arrays.asList(new String(CommonsConstants.MAIL_TO)),
						CommonsConstants.MAIL_CONCILIATION_SUBJECT + " " + subject, message);
			}
		} catch (Exception e) {
			log.error(strLog + "[Excepion]" + e.getMessage());
			e.printStackTrace();
		}
	}

}
