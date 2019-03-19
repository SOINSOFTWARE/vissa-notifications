package com.soinsoftware.vissa.client;

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
			conciliationBll = CashRegisterConciliationBll.getInstance();
			List<CashConciliation> conciliations = conciliationBll.select(date);
			log.info(strLog + " conciliations: " + conciliations.size());
		} catch (Exception e) {
			log.error(strLog + "[Excepion]" + e.getMessage());
			e.printStackTrace();
		}
	}

}
