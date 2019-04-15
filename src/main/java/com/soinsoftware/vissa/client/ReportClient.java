package com.soinsoftware.vissa.client;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.soinsoftware.vissa.commons.CommonsConstants;
import com.soinsoftware.vissa.model.ReportManager;
import com.soinsoftware.vissa.util.StringUtility;

public class ReportClient {

	private static final String EXPIRATION = "vencimiento";
	private static final String DAILY_CONCILIATION = "conciliacion";
	private static final String PRODUCT_STOCK = "productStock";
	private static final List<String> REPORTS = Arrays.asList(DAILY_CONCILIATION, EXPIRATION, PRODUCT_STOCK);
	private static final Logger log = Logger.getLogger(ReportClient.class);

	public static void main(String[] args) {
		if (args.length < 2) {
			log.warn("Número inválido de parámetros");
			return;
		}

		if (!REPORTS.contains(args[0])) {
			log.warn("Opcion no valida, escoja una de las siguientes: " + REPORTS);
			return;
		}
		String propFile = args[1];
		log.info(propFile);
		if (StringUtility.isNull(propFile)) {
			log.warn("Ruta de logs no valida: ");
			return;
		}
		CommonsConstants.PROPERTY_FILE_PATH = propFile;
		CommonsConstants.reloadProperties();
		generateReport(args[0]);

	}

	private static void generateReport(final String report) {
		ReportManager manager = new ReportManager();
		switch (report) {
		case DAILY_CONCILIATION:
			manager.dailyConciliationReport();
			break;
		case EXPIRATION:
			manager.lotsToExpireReport();
			break;
		case PRODUCT_STOCK:
			manager.productsToEndStockReport();
			break;
		}
	}

}