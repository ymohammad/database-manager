/*******************************************************************************************************************************
ObjectScriptTextRepositoryImpl.java

Copyright © 2022, Triniti Corporation. All rights reserved.
The Programs (which include both the software and documentation) contain proprietary information of Triniti Corporation;
they are provided under a license agreement containing restrictions on use and disclosure and are also protected by
copyright, patent and other intellectual and industrial property law. Reverse engineering, disassembly or de-compilation of
the programs is prohibited.
Program Documentation is licensed for use solely to support the deployment of the Programs and not for any other
purpose.
The information contained in this document is subject to change without notice. If you find any problems in the
documentation, please report them to us in writing. Triniti Corporation does not warrant that this document is error free.
Except as may be expressly permitted in your license agreement for these Programs, no part of these Programs may be
reproduced or transmitted in any form or by any means, electronic or mechanical, for any purpose, without the express
written permission of Triniti Corporation.

Author : ymohammad
Date   : Jul 24, 2022

Last modified by : ymohammad
Last modified on : Jul 24, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository.script;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportOptionsModel;
import in.droidsoft.dbmanager.exportdb.rdbms.oracle.ExportScriptTextOptions;
import in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository.ObjectScriptTextRepository;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

/**
* Class ObjectScriptTextRepositoryImpl
*/
@Repository
public class ObjectScriptTextRepositoryImpl implements ObjectScriptTextRepository {

	@PersistenceContext
	private EntityManager srcEntityManager;
	
	@Override
	public void setExportProperties(ExportOptionsModel option) {
		this.setExportObjectOption(ExportScriptTextOptions.PHYSICAL_PROPS, option.isPhysicalProperties());
		this.setExportObjectOption(ExportScriptTextOptions.PRETTY_SCRIPT, option.isPretty());
		this.setExportObjectOption(ExportScriptTextOptions.SCHEMA_INFO, option.isEmitSchema());
		this.setExportObjectOption(ExportScriptTextOptions.SEGMENT_INFO, option.isSegmentAttribute());
		this.setExportObjectOption(ExportScriptTextOptions.SQL_TERMINATOR, option.isSqlTerminator());
		this.setExportObjectOption(ExportScriptTextOptions.STORAGE_INFO, option.isStorage());
		this.setExportObjectOption(ExportScriptTextOptions.TABLESPACE_INFO, option.isTableSpace());
	}

	@Override
	public String getDBObjectScriptText(String objectOwner, String objectType, String objectName) throws IOException, SQLException {
		String sqlQry = "select DBMS_METADATA.GET_DDL(:objectType, :objectName, :owner) from DUAL";
		Query ddlQry = this.srcEntityManager.createNativeQuery(sqlQry, String.class);
		Clob clob = (Clob) ddlQry.getSingleResult();
		String ddlText = AppUtils.convertClobToString(clob);
		return ddlText;
	}
	
	private void setExportObjectOption(String propertyName, boolean option) {
		String blockCode = ExportScriptTextOptions.getBlockCode(propertyName, option);
		if (AppUtils.isEmpty(blockCode)) return;
		
		int sts = this.srcEntityManager.createNativeQuery(blockCode).executeUpdate();
	}
}
