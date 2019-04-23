package com.soinsoftware.vissa.client;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import com.soinsoftware.vissa.commons.CommonsConstants;
import com.soinsoftware.vissa.model.ReportManager;

public class ReportClient {

	private static final String EXPIRATION = "vencimiento";
	private static final String DAILY_CONCILIATION = "conciliacion";
	private static final String PRODUCT_STOCK = "productStock";
	private static final String PURCHASE_PENDING = "comprasPendientes";
	private static final String SALE_PENDING = "ventasPendientes";
	private static final List<String> REPORTS = Arrays.asList(DAILY_CONCILIATION, EXPIRATION, PRODUCT_STOCK,
			PURCHASE_PENDING, SALE_PENDING);
	private static final Logger log = Logger.getLogger(ReportClient.class);

	public static void main(String[] args) {
		if (args.length < 2) {
			log.warn("Número inválido de parámetros");
			return;
		}
		// Primer parámetro: ruta de archivo de propiedades
		String propFile = args[0];
		log.info(propFile);
		if (StringUtils.isBlank(propFile)) {
			log.warn("Ruta de logs no valida: ");
			return;
		}

		// Segundo parámetro: tipo de reporte
		String report = args[1];
		if (!REPORTS.contains(report)) {
			log.warn("Opcion no valida, escoja una de las siguientes: " + REPORTS);
			return;
		}

		// Terecer parámetro: parámetro requerido de acuerdo al tipo de reporte. No es obligatorio
		String reportParam = args[2];
		
		CommonsConstants.PROPERTY_FILE_PATH = propFile;
		CommonsConstants.reloadProperties();
		generateReport(report, reportParam);

	}

	private static void generateReport(final String report, String reportParam) {
		String strLog = "[generateReport] ";
		try {
			log.info(strLog + "[parameters] report: " + report + ", reportParam: " + reportParam);
			ReportManager manager = new ReportManager();
			switch (report) {
			case DAILY_CONCILIATION:
				String[] formats = {CommonsConstants.FORMAT_DATE};
				Date reportDate = DateUtils.parseDate(reportParam, formats);
				manager.dailyConciliationReport(reportDate);
				break;
			case EXPIRATION:
				manager.lotsToExpireReport();
				break;
			case PRODUCT_STOCK:
				manager.productsToEndStockReport();
				break;
			case PURCHASE_PENDING:
				manager.purchasePending();
				break;
			case SALE_PENDING:
				manager.salePending();
				break;
			}

		} catch (Exception e) {
			log.error(strLog + "[Exception] " + e.getMessage());
			e.printStackTrace();
		}
	}

}