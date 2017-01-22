package com.lpmas.erp.factory;

import java.sql.SQLException;

import com.lpmas.erp.config.ErpDBConfig;
import com.lpmas.framework.db.DBExecutor;
import com.lpmas.framework.db.DBFactory;
import com.lpmas.framework.db.DBObject;
import com.lpmas.framework.db.MysqlDBExecutor;
import com.lpmas.framework.db.MysqlDBObject;

public class ErpDBFactory extends DBFactory {

	public DBObject getDBObjectR() throws SQLException {
		return new MysqlDBObject(ErpDBConfig.DB_LINK_ERP_R);
	}

	public DBObject getDBObjectW() throws SQLException {
		return new MysqlDBObject(ErpDBConfig.DB_LINK_ERP_W);
	}

	@Override
	public DBExecutor getDBExecutor() {
		return new MysqlDBExecutor();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub

	}
}
