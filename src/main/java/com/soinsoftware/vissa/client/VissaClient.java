package com.soinsoftware.vissa.client;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.soinsoftware.vissa.bll.LotBll;
import com.soinsoftware.vissa.model.Lot;
import com.soinsoftware.vissa.util.DateUtil;

public class VissaClient {

	protected static final Logger log = Logger.getLogger(VissaClient.class);
	private LotBll lotBll;

	public static void main(String[] args) {
		new VissaClient().getProductsToExpire();

	}

	private void getProductsToExpire() {
		try {
			lotBll = LotBll.getInstance();

			Date expirationDate = DateUtil.addDaysToDate(DateUtil.localDateTimeToDate(DateUtil.getDefaultIniDate()), 4);
			List<Lot> lots = lotBll.select(expirationDate);
			log.info("Lotes por vencer: " + lots.size());

		} catch (Exception e) {

		}
	}

}
