package com.soinsoftware.vissa.model;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.soinsoftware.vissa.bll.CashRegisterConciliationBll;
import com.soinsoftware.vissa.bll.LotBll;
import com.soinsoftware.vissa.bll.ProductBll;
import com.soinsoftware.vissa.bll.UserBll;
import com.soinsoftware.vissa.common.EComparatorType;
import com.soinsoftware.vissa.commons.CommonsConstants;
import com.soinsoftware.vissa.util.CommonsEmailService;
import com.soinsoftware.vissa.util.DateUtil;
import com.soinsoftware.vissa.util.SmsGenerator;

public class ReportManager {

	private static final Logger log = Logger.getLogger(ReportManager.class);

	/**
	 * Metodo para obtener los productos a expirar y generar reporte
	 */
	public void lotsToExpireReport() {
		String strLog = "[lotsToExpireReport] ";
		try {
			if (CommonsConstants.LOT_EXPIRATION_DAYS != null) {
				LotBll lotBll = LotBll.getInstance();

				Date expirationDate = DateUtil.addDaysToDate(DateUtil.localDateTimeToDate(DateUtil.getDefaultIniDate()),
						CommonsConstants.LOT_EXPIRATION_DAYS);
				List<Lot> lots = lotBll.select(expirationDate);
				log.info(strLog + "Cantidad de lotes por vencer: " + lots.size());

				if (lots.size() > 0) {
					String items = "";
					for (Lot lot : lots) {
						// Se validan solo los lotes con stock
						if (lot.getQuantity() > 0.0) {
							items = items + "Producto: " + lot.getProduct().getName() + ", Lote: " + lot.getCode()
									+ ", Fecha vencimiento: "
									+ DateUtil.dateToString(lot.getExpirationDate(), CommonsConstants.FORMAT_DATE)
									+ ".   ";
						}
					}

					// Enviar por SMS el mensaje
					SmsGenerator.sendSMS(CommonsConstants.LOT_EXPIRATION_MESSAGE + items);
				} else {
					log.info(strLog + "No hay lotes pr�ximos a vencerse");
				}
			} else {
				log.error(strLog + "No hay dias de validacion configurados para fecha de vencimiennto: "
						+ CommonsConstants.LOT_EXPIRATION_DAYS);
			}

		} catch (Exception e) {
			log.error(strLog + "[Excepion]" + e.getMessage());
		}
	}

	/**
	 * Metodo para obtener los productos cuyo stock se finalizar�
	 */
	public void productsToEndStockReport() {
		String strLog = "[productsToEndStockReport] ";
		try {
			if (CommonsConstants.PRODUCT_STOCK != null) {
				ProductBll productBll = ProductBll.getInstance();

				// Umbral de validaci�n de stock
				Double stock = CommonsConstants.PRODUCT_STOCK;

				// Se cosultan los productos que est�n bajo el umbral
				List<Product> products = productBll.selectByStock(stock, EComparatorType.EQ);
				log.info(strLog + "Cantidad de productos con stock a finalizar: " + products.size());

				if (products != null && !products.isEmpty()) {
					String message = "<html>" + CommonsConstants.PRODUCT_STOCK_MESSAGE;
					String items = "";
					for (Product product : products) {
						items = items + "<p>Producto: " + product.getName() + ", Stock: " + product.getStock()
								+ ". </p>";
					}

					message += message + items + "</html>";
					// Enviar por SMS el mensaje
					// SmsGenerator.sendSMS(CommonsConstants.PRODUCT_STOCK_MESSAGE + items);

					// Enviar por correo electronico el cuadre de caja para vendedor y administrador
					CommonsEmailService.send(CommonsConstants.MAIL_FROM,
							Arrays.asList(new String(CommonsConstants.MAIL_TO)),
							CommonsConstants.PRODUCT_STOCK_SUBJECT + " ", message);
					log.info(strLog + "Email enviado");
				}
			} else {
				log.error(strLog + "No hay dias de validacion configurados para stock de productos : "
						+ CommonsConstants.PRODUCT_STOCK);
			}

		} catch (Exception e) {
			log.error(strLog + "[Excepion]" + e.getMessage());
		}
	}

	/**
	 * Metodo para obtener los cuadres de caja de un dia y enviarlos por correo
	 */
	public void dailyConciliationReport() {
		String strLog = "[dailyConciliationReport] ";
		try {
			Date date = DateUtils.truncate(new Date(), Calendar.DATE);

			Date endDate = DateUtils.addHours(date, 23);
			endDate = DateUtils.addMinutes(endDate, 59);
			endDate = DateUtils.addSeconds(endDate, 59);

			CashRegisterConciliationBll conciliationBll = CashRegisterConciliationBll.getInstance();
			UserBll userBll = UserBll.getInstance();
			List<CashConciliation> conciliations = conciliationBll.select(date);
			log.info(strLog + " conciliations: " + conciliations.size());

			String message = CommonsConstants.SALESMAN_CONCILIATION_MSG;
			String subject = "";
			for (CashConciliation conciliation : conciliations) {
				Person person = conciliation.getPerson();
				User user = userBll.select(person);
				String role = user.getRole().getName();

				// Si es el cuadre del vendedor
				if (role.equals(ERole.SALESMAN.getName())) {
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

				} else if (role.equals(ERole.ADMINISTRATOR.getName())) {// Cuadre del administrador
					subject = "Administrador";
					message = CommonsConstants.ADMON_CONCILIATION_MSG;

					message = message.replace("ADMINISTRATOR",
							conciliation.getPerson().getName() + " " + conciliation.getPerson().getLastName());
					message = message.replace("BASE", String.valueOf(conciliation.getCashBase()));
					message = message.replace("SUPPLIER_PAYMENTS", String.valueOf(conciliation.getSupplierPayments()));
					message = message.replace("SUPPLIER_LOANS", String.valueOf(conciliation.getSupplierPaymentsLoan()));
					message = message.replace("BALANCE", String.valueOf(conciliation.getBalance()));

				}

				// Enviar por correo electronico el cuadre de caja para vendedor y administrador
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