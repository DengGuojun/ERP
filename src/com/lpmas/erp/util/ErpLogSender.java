package com.lpmas.erp.util;

import com.lpmas.erp.config.ErpConsoleConfig;
import com.lpmas.log.business.DataLogSender;

public class ErpLogSender extends DataLogSender {

	public void sendAddLog(Object bean, Integer infoType, int infoId1, String field1) {
		super.sendAddLog(bean, infoType, infoId1, 0, 0, field1, "", "", ErpConsoleConfig.APP_ID);
	}

	public void sendUpdateLog(Object originalBean, Object currentBean, Integer infoType, int infoId1, String field1) {
		super.sendUpdateLog(originalBean, currentBean, infoType, infoId1, 0, 0, field1, "", "", ErpConsoleConfig.APP_ID);
	}

	public void sendRemoveLog(Object originalBean, Integer infoType, int infoId1, String field1) {
		super.sendRemoveLog(originalBean, infoType, infoId1, 0, 0, field1, "", "", ErpConsoleConfig.APP_ID);
	}

	public void sendAddLog(Object bean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1) {
		super.sendAddLog(bean, infoType, infoId1, infoId2, infoId3, field1, "", "", ErpConsoleConfig.APP_ID);
	}

	public void sendUpdateLog(Object originalBean, Object currentBean, Integer infoType, int infoId1, int infoId2, int infoId3,
			String field1) {
		super.sendUpdateLog(originalBean, currentBean, infoType, infoId1, infoId2, infoId3, field1, "", "", ErpConsoleConfig.APP_ID);
	}

	public void sendRemoveLog(Object originalBean, Integer infoType, int infoId1, int infoId2, int infoId3, String field1) {
		super.sendRemoveLog(originalBean, infoType, infoId1, infoId2, infoId3, field1, "", "", ErpConsoleConfig.APP_ID);
	}

}
