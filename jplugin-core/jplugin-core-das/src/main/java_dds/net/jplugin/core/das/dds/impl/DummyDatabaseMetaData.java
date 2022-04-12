package net.jplugin.core.das.dds.impl;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.RowIdLifetime;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import net.jplugin.core.das.dds.impl.kits.NoDataResultSet;


public class DummyDatabaseMetaData implements DatabaseMetaData {

	public static final DatabaseMetaData INSTANCE = new DummyDatabaseMetaData();
	
//	private DatabaseMetaData inner ;
	public DummyDatabaseMetaData()  {
		boolean b=false;
//		try {
//			b = ThreadLocalContextManager.instance.createContextIfNotExists();
//			inner = DataSourceFactory.getDataSource("database-1").getConnection().getMetaData();
//		} catch (SQLException e) {
//			throw new RuntimeException(e);
//		}finally {
//			if (b) 
//				ThreadLocalContextManager.instance.releaseContext();
//		}
	}
	public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types)
			throws SQLException {
		return NoDataResultSet.INSTANCE;
	}
	public int getDatabaseMajorVersion() throws SQLException {
		return 5;
//		return inner.getDatabaseMajorVersion();
	}
	public int getDatabaseMinorVersion() throws SQLException {
		return 6;
//		return inner.getDatabaseMinorVersion();
	}
	public String getDatabaseProductVersion() throws SQLException {
		return "5.6.22-log";
//		return inner.getDatabaseProductVersion();
	}
	public String getDatabaseProductName() throws SQLException {
		return "MySQL";
//		return inner.getDatabaseProductName();
	}
	public boolean supportsGetGeneratedKeys() throws SQLException {
//		return inner.supportsGetGeneratedKeys();
		return true;
	}
	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
//		return inner.dataDefinitionIgnoredInTransactions();
		return false;
	}
	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
//		return inner.dataDefinitionCausesTransactionCommit();
		return true;
	}
	public boolean supportsResultSetType(int type) throws SQLException {
		return true;
//		return inner.supportsResultSetType(type);
	}
	public boolean supportsBatchUpdates() throws SQLException {
//		return inner.supportsBatchUpdates();
		return true;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean allProceduresAreCallable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean allTablesAreSelectable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getURL() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getUserName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean nullsAreSortedHigh() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean nullsAreSortedLow() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean nullsAreSortedAtStart() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean nullsAreSortedAtEnd() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getDriverName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getDriverVersion() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getDriverMajorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getDriverMinorVersion() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean usesLocalFiles() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean usesLocalFilePerTable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesUpperCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesLowerCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesMixedCaseIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getIdentifierQuoteString() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSQLKeywords() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getNumericFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getStringFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSystemFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getTimeDateFunctions() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getSearchStringEscape() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getExtraNameCharacters() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsColumnAliasing() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean nullPlusNonNullIsNull() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsConvert() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsConvert(int fromType, int toType) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsTableCorrelationNames() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsExpressionsInOrderBy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOrderByUnrelated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsGroupBy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsGroupByUnrelated() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsGroupByBeyondSelect() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsLikeEscapeClause() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMultipleResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMultipleTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsNonNullableColumns() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMinimumSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCoreSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsExtendedSQLGrammar() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsANSI92FullSQL() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsFullOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsLimitedOuterJoins() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getSchemaTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getProcedureTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getCatalogTerm() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isCatalogAtStart() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public String getCatalogSeparator() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsSchemasInDataManipulation() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsPositionedDelete() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsPositionedUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSelectForUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsStoredProcedures() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSubqueriesInComparisons() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSubqueriesInExists() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSubqueriesInIns() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsCorrelatedSubqueries() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsUnion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsUnionAll() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getMaxBinaryLiteralLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxCharLiteralLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnsInGroupBy() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnsInIndex() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnsInOrderBy() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnsInSelect() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxColumnsInTable() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxConnections() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxCursorNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxIndexLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxSchemaNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxProcedureNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxCatalogNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxRowSize() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getMaxStatementLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxStatements() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxTableNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxTablesInSelect() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getMaxUserNameLength() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getDefaultTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean supportsTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
			String columnNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getSchemas() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getCatalogs() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getTableTypes() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
			String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getTypeInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean ownDeletesAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean ownInsertsAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean othersDeletesAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean othersInsertsAreVisible(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean updatesAreDetected(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean deletesAreDetected(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean insertsAreDetected(int type) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsSavepoints() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsNamedParameters() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsMultipleOpenResults() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
			String attributeNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getResultSetHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getJDBCMajorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getJDBCMinorVersion() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public int getSQLStateType() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public boolean locatorsUpdateCopy() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean supportsStatementPooling() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public RowIdLifetime getRowIdLifetime() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public ResultSet getClientInfoProperties() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
			String columnNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern,
			String columnNamePattern) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean generatedKeyAlwaysReturned() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	
//	
//	public <T> T unwrap(Class<T> iface) throws SQLException {
//		return inner.unwrap(iface);
//	}
//	public boolean isWrapperFor(Class<?> iface) throws SQLException {
//		return inner.isWrapperFor(iface);
//	}
//	public boolean allProceduresAreCallable() throws SQLException {
//		return inner.allProceduresAreCallable();
//	}
//	public boolean allTablesAreSelectable() throws SQLException {
//		return inner.allTablesAreSelectable();
//	}
//	public String getURL() throws SQLException {
//		return inner.getURL();
//	}
//	public String getUserName() throws SQLException {
//		return inner.getUserName();
//	}
//	public boolean isReadOnly() throws SQLException {
//		return inner.isReadOnly();
//	}
//	public boolean nullsAreSortedHigh() throws SQLException {
//		return inner.nullsAreSortedHigh();
//	}
//	public boolean nullsAreSortedLow() throws SQLException {
//		return inner.nullsAreSortedLow();
//	}
//	public boolean nullsAreSortedAtStart() throws SQLException {
//		return inner.nullsAreSortedAtStart();
//	}
//	public boolean nullsAreSortedAtEnd() throws SQLException {
//		return inner.nullsAreSortedAtEnd();
//	}
//	
//	
//	public String getDriverName() throws SQLException {
//		return inner.getDriverName();
//	}
//	public String getDriverVersion() throws SQLException {
//		return inner.getDriverVersion();
//	}
//	public int getDriverMajorVersion() {
//		return inner.getDriverMajorVersion();
//	}
//	public int getDriverMinorVersion() {
//		return inner.getDriverMinorVersion();
//	}
//	public boolean usesLocalFiles() throws SQLException {
//		return inner.usesLocalFiles();
//	}
//	public boolean usesLocalFilePerTable() throws SQLException {
//		return inner.usesLocalFilePerTable();
//	}
//	public boolean supportsMixedCaseIdentifiers() throws SQLException {
//		return inner.supportsMixedCaseIdentifiers();
//	}
//	public boolean storesUpperCaseIdentifiers() throws SQLException {
//		return inner.storesUpperCaseIdentifiers();
//	}
//	public boolean storesLowerCaseIdentifiers() throws SQLException {
//		return inner.storesLowerCaseIdentifiers();
//	}
//	public boolean storesMixedCaseIdentifiers() throws SQLException {
//		return inner.storesMixedCaseIdentifiers();
//	}
//	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
//		return inner.supportsMixedCaseQuotedIdentifiers();
//	}
//	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
//		return inner.storesUpperCaseQuotedIdentifiers();
//	}
//	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
//		return inner.storesLowerCaseQuotedIdentifiers();
//	}
//	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
//		return inner.storesMixedCaseQuotedIdentifiers();
//	}
//	public String getIdentifierQuoteString() throws SQLException {
//		return inner.getIdentifierQuoteString();
//	}
//	public String getSQLKeywords() throws SQLException {
//		return inner.getSQLKeywords();
//	}
//	public String getNumericFunctions() throws SQLException {
//		return inner.getNumericFunctions();
//	}
//	public String getStringFunctions() throws SQLException {
//		return inner.getStringFunctions();
//	}
//	public String getSystemFunctions() throws SQLException {
//		return inner.getSystemFunctions();
//	}
//	public String getTimeDateFunctions() throws SQLException {
//		return inner.getTimeDateFunctions();
//	}
//	public String getSearchStringEscape() throws SQLException {
//		return inner.getSearchStringEscape();
//	}
//	public String getExtraNameCharacters() throws SQLException {
//		return inner.getExtraNameCharacters();
//	}
//	public boolean supportsAlterTableWithAddColumn() throws SQLException {
//		return inner.supportsAlterTableWithAddColumn();
//	}
//	public boolean supportsAlterTableWithDropColumn() throws SQLException {
//		return inner.supportsAlterTableWithDropColumn();
//	}
//	public boolean supportsColumnAliasing() throws SQLException {
//		return inner.supportsColumnAliasing();
//	}
//	public boolean nullPlusNonNullIsNull() throws SQLException {
//		return inner.nullPlusNonNullIsNull();
//	}
//	public boolean supportsConvert() throws SQLException {
//		return inner.supportsConvert();
//	}
//	public boolean supportsConvert(int fromType, int toType) throws SQLException {
//		return inner.supportsConvert(fromType, toType);
//	}
//	public boolean supportsTableCorrelationNames() throws SQLException {
//		return inner.supportsTableCorrelationNames();
//	}
//	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
//		return inner.supportsDifferentTableCorrelationNames();
//	}
//	public boolean supportsExpressionsInOrderBy() throws SQLException {
//		return inner.supportsExpressionsInOrderBy();
//	}
//	public boolean supportsOrderByUnrelated() throws SQLException {
//		return inner.supportsOrderByUnrelated();
//	}
//	public boolean supportsGroupBy() throws SQLException {
//		return inner.supportsGroupBy();
//	}
//	public boolean supportsGroupByUnrelated() throws SQLException {
//		return inner.supportsGroupByUnrelated();
//	}
//	public boolean supportsGroupByBeyondSelect() throws SQLException {
//		return inner.supportsGroupByBeyondSelect();
//	}
//	public boolean supportsLikeEscapeClause() throws SQLException {
//		return inner.supportsLikeEscapeClause();
//	}
//	public boolean supportsMultipleResultSets() throws SQLException {
//		return inner.supportsMultipleResultSets();
//	}
//	public boolean supportsMultipleTransactions() throws SQLException {
//		return inner.supportsMultipleTransactions();
//	}
//	public boolean supportsNonNullableColumns() throws SQLException {
//		return inner.supportsNonNullableColumns();
//	}
//	public boolean supportsMinimumSQLGrammar() throws SQLException {
//		return inner.supportsMinimumSQLGrammar();
//	}
//	public boolean supportsCoreSQLGrammar() throws SQLException {
//		return inner.supportsCoreSQLGrammar();
//	}
//	public boolean supportsExtendedSQLGrammar() throws SQLException {
//		return inner.supportsExtendedSQLGrammar();
//	}
//	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
//		return inner.supportsANSI92EntryLevelSQL();
//	}
//	public boolean supportsANSI92IntermediateSQL() throws SQLException {
//		return inner.supportsANSI92IntermediateSQL();
//	}
//	public boolean supportsANSI92FullSQL() throws SQLException {
//		return inner.supportsANSI92FullSQL();
//	}
//	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
//		return inner.supportsIntegrityEnhancementFacility();
//	}
//	public boolean supportsOuterJoins() throws SQLException {
//		return inner.supportsOuterJoins();
//	}
//	public boolean supportsFullOuterJoins() throws SQLException {
//		return inner.supportsFullOuterJoins();
//	}
//	public boolean supportsLimitedOuterJoins() throws SQLException {
//		return inner.supportsLimitedOuterJoins();
//	}
//	public String getSchemaTerm() throws SQLException {
//		return inner.getSchemaTerm();
//	}
//	public String getProcedureTerm() throws SQLException {
//		return inner.getProcedureTerm();
//	}
//	public String getCatalogTerm() throws SQLException {
//		return inner.getCatalogTerm();
//	}
//	public boolean isCatalogAtStart() throws SQLException {
//		return inner.isCatalogAtStart();
//	}
//	public String getCatalogSeparator() throws SQLException {
//		return inner.getCatalogSeparator();
//	}
//	public boolean supportsSchemasInDataManipulation() throws SQLException {
//		return inner.supportsSchemasInDataManipulation();
//	}
//	public boolean supportsSchemasInProcedureCalls() throws SQLException {
//		return inner.supportsSchemasInProcedureCalls();
//	}
//	public boolean supportsSchemasInTableDefinitions() throws SQLException {
//		return inner.supportsSchemasInTableDefinitions();
//	}
//	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
//		return inner.supportsSchemasInIndexDefinitions();
//	}
//	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
//		return inner.supportsSchemasInPrivilegeDefinitions();
//	}
//	public boolean supportsCatalogsInDataManipulation() throws SQLException {
//		return inner.supportsCatalogsInDataManipulation();
//	}
//	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
//		return inner.supportsCatalogsInProcedureCalls();
//	}
//	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
//		return inner.supportsCatalogsInTableDefinitions();
//	}
//	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
//		return inner.supportsCatalogsInIndexDefinitions();
//	}
//	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
//		return inner.supportsCatalogsInPrivilegeDefinitions();
//	}
//	public boolean supportsPositionedDelete() throws SQLException {
//		return inner.supportsPositionedDelete();
//	}
//	public boolean supportsPositionedUpdate() throws SQLException {
//		return inner.supportsPositionedUpdate();
//	}
//	public boolean supportsSelectForUpdate() throws SQLException {
//		return inner.supportsSelectForUpdate();
//	}
//	public boolean supportsStoredProcedures() throws SQLException {
//		return inner.supportsStoredProcedures();
//	}
//	public boolean supportsSubqueriesInComparisons() throws SQLException {
//		return inner.supportsSubqueriesInComparisons();
//	}
//	public boolean supportsSubqueriesInExists() throws SQLException {
//		return inner.supportsSubqueriesInExists();
//	}
//	public boolean supportsSubqueriesInIns() throws SQLException {
//		return inner.supportsSubqueriesInIns();
//	}
//	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
//		return inner.supportsSubqueriesInQuantifieds();
//	}
//	public boolean supportsCorrelatedSubqueries() throws SQLException {
//		return inner.supportsCorrelatedSubqueries();
//	}
//	public boolean supportsUnion() throws SQLException {
//		return inner.supportsUnion();
//	}
//	public boolean supportsUnionAll() throws SQLException {
//		return inner.supportsUnionAll();
//	}
//	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
//		return inner.supportsOpenCursorsAcrossCommit();
//	}
//	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
//		return inner.supportsOpenCursorsAcrossRollback();
//	}
//	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
//		return inner.supportsOpenStatementsAcrossCommit();
//	}
//	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
//		return inner.supportsOpenStatementsAcrossRollback();
//	}
//	public int getMaxBinaryLiteralLength() throws SQLException {
//		return inner.getMaxBinaryLiteralLength();
//	}
//	public int getMaxCharLiteralLength() throws SQLException {
//		return inner.getMaxCharLiteralLength();
//	}
//	public int getMaxColumnNameLength() throws SQLException {
//		return inner.getMaxColumnNameLength();
//	}
//	public int getMaxColumnsInGroupBy() throws SQLException {
//		return inner.getMaxColumnsInGroupBy();
//	}
//	public int getMaxColumnsInIndex() throws SQLException {
//		return inner.getMaxColumnsInIndex();
//	}
//	public int getMaxColumnsInOrderBy() throws SQLException {
//		return inner.getMaxColumnsInOrderBy();
//	}
//	public int getMaxColumnsInSelect() throws SQLException {
//		return inner.getMaxColumnsInSelect();
//	}
//	public int getMaxColumnsInTable() throws SQLException {
//		return inner.getMaxColumnsInTable();
//	}
//	public int getMaxConnections() throws SQLException {
//		return inner.getMaxConnections();
//	}
//	public int getMaxCursorNameLength() throws SQLException {
//		return inner.getMaxCursorNameLength();
//	}
//	public int getMaxIndexLength() throws SQLException {
//		return inner.getMaxIndexLength();
//	}
//	public int getMaxSchemaNameLength() throws SQLException {
//		return inner.getMaxSchemaNameLength();
//	}
//	public int getMaxProcedureNameLength() throws SQLException {
//		return inner.getMaxProcedureNameLength();
//	}
//	public int getMaxCatalogNameLength() throws SQLException {
//		return inner.getMaxCatalogNameLength();
//	}
//	public int getMaxRowSize() throws SQLException {
//		return inner.getMaxRowSize();
//	}
//	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
//		return inner.doesMaxRowSizeIncludeBlobs();
//	}
//	public int getMaxStatementLength() throws SQLException {
//		return inner.getMaxStatementLength();
//	}
//	public int getMaxStatements() throws SQLException {
//		return inner.getMaxStatements();
//	}
//	public int getMaxTableNameLength() throws SQLException {
//		return inner.getMaxTableNameLength();
//	}
//	public int getMaxTablesInSelect() throws SQLException {
//		return inner.getMaxTablesInSelect();
//	}
//	public int getMaxUserNameLength() throws SQLException {
//		return inner.getMaxUserNameLength();
//	}
//	public int getDefaultTransactionIsolation() throws SQLException {
//		return inner.getDefaultTransactionIsolation();
//	}
//	public boolean supportsTransactions() throws SQLException {
//		return inner.supportsTransactions();
//	}
//	public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
//		return inner.supportsTransactionIsolationLevel(level);
//	}
//	public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
//		return inner.supportsDataDefinitionAndDataManipulationTransactions();
//	}
//	public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
//		return inner.supportsDataManipulationTransactionsOnly();
//	}
//
//
//	public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern)
//			throws SQLException {
//		return inner.getProcedures(catalog, schemaPattern, procedureNamePattern);
//	}
//	public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern,
//			String columnNamePattern) throws SQLException {
//		return inner.getProcedureColumns(catalog, schemaPattern, procedureNamePattern, columnNamePattern);
//	}
//	public ResultSet getSchemas() throws SQLException {
//		return inner.getSchemas();
//	}
//	public ResultSet getCatalogs() throws SQLException {
//		return inner.getCatalogs();
//	}
//	public ResultSet getTableTypes() throws SQLException {
//		return inner.getTableTypes();
//	}
//	public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern)
//			throws SQLException {
//		return inner.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
//	}
//	public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern)
//			throws SQLException {
//		return inner.getColumnPrivileges(catalog, schema, table, columnNamePattern);
//	}
//	public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern)
//			throws SQLException {
//		return inner.getTablePrivileges(catalog, schemaPattern, tableNamePattern);
//	}
//	public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable)
//			throws SQLException {
//		return inner.getBestRowIdentifier(catalog, schema, table, scope, nullable);
//	}
//	public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
//		return inner.getVersionColumns(catalog, schema, table);
//	}
//	public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
//		return inner.getPrimaryKeys(catalog, schema, table);
//	}
//	public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
//		return inner.getImportedKeys(catalog, schema, table);
//	}
//	public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
//		return inner.getExportedKeys(catalog, schema, table);
//	}
//	public ResultSet getCrossReference(String parentCatalog, String parentSchema, String parentTable,
//			String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
//		return inner.getCrossReference(parentCatalog, parentSchema, parentTable, foreignCatalog, foreignSchema,
//				foreignTable);
//	}
//	public ResultSet getTypeInfo() throws SQLException {
//		return inner.getTypeInfo();
//	}
//	public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate)
//			throws SQLException {
//		return inner.getIndexInfo(catalog, schema, table, unique, approximate);
//	}
//
//	public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
//		return inner.supportsResultSetConcurrency(type, concurrency);
//	}
//	public boolean ownUpdatesAreVisible(int type) throws SQLException {
//		return inner.ownUpdatesAreVisible(type);
//	}
//	public boolean ownDeletesAreVisible(int type) throws SQLException {
//		return inner.ownDeletesAreVisible(type);
//	}
//	public boolean ownInsertsAreVisible(int type) throws SQLException {
//		return inner.ownInsertsAreVisible(type);
//	}
//	public boolean othersUpdatesAreVisible(int type) throws SQLException {
//		return inner.othersUpdatesAreVisible(type);
//	}
//	public boolean othersDeletesAreVisible(int type) throws SQLException {
//		return inner.othersDeletesAreVisible(type);
//	}
//	public boolean othersInsertsAreVisible(int type) throws SQLException {
//		return inner.othersInsertsAreVisible(type);
//	}
//	public boolean updatesAreDetected(int type) throws SQLException {
//		return inner.updatesAreDetected(type);
//	}
//	public boolean deletesAreDetected(int type) throws SQLException {
//		return inner.deletesAreDetected(type);
//	}
//	public boolean insertsAreDetected(int type) throws SQLException {
//		return inner.insertsAreDetected(type);
//	}
//
//	public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types)
//			throws SQLException {
//		return inner.getUDTs(catalog, schemaPattern, typeNamePattern, types);
//	}
//	public Connection getConnection() throws SQLException {
//		return inner.getConnection();
//	}
//	public boolean supportsSavepoints() throws SQLException {
//		return inner.supportsSavepoints();
//	}
//	public boolean supportsNamedParameters() throws SQLException {
//		return inner.supportsNamedParameters();
//	}
//	public boolean supportsMultipleOpenResults() throws SQLException {
//		return inner.supportsMultipleOpenResults();
//	}
//
//	public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
//		return inner.getSuperTypes(catalog, schemaPattern, typeNamePattern);
//	}
//	public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
//		return inner.getSuperTables(catalog, schemaPattern, tableNamePattern);
//	}
//	public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern,
//			String attributeNamePattern) throws SQLException {
//		return inner.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
//	}
//	public boolean supportsResultSetHoldability(int holdability) throws SQLException {
//		return inner.supportsResultSetHoldability(holdability);
//	}
//	public int getResultSetHoldability() throws SQLException {
//		return inner.getResultSetHoldability();
//	}
//
//	public int getJDBCMajorVersion() throws SQLException {
//		return inner.getJDBCMajorVersion();
//	}
//	public int getJDBCMinorVersion() throws SQLException {
//		return inner.getJDBCMinorVersion();
//	}
//	public int getSQLStateType() throws SQLException {
//		return inner.getSQLStateType();
//	}
//	public boolean locatorsUpdateCopy() throws SQLException {
//		return inner.locatorsUpdateCopy();
//	}
//	public boolean supportsStatementPooling() throws SQLException {
//		return inner.supportsStatementPooling();
//	}
//	public RowIdLifetime getRowIdLifetime() throws SQLException {
//		return inner.getRowIdLifetime();
//	}
//	public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
//		return inner.getSchemas(catalog, schemaPattern);
//	}
//	public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
//		return inner.supportsStoredFunctionsUsingCallSyntax();
//	}
//	public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
//		return inner.autoCommitFailureClosesAllResultSets();
//	}
//	public ResultSet getClientInfoProperties() throws SQLException {
//		return inner.getClientInfoProperties();
//	}
//	public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern)
//			throws SQLException {
//		return inner.getFunctions(catalog, schemaPattern, functionNamePattern);
//	}
//	public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern,
//			String columnNamePattern) throws SQLException {
//		return inner.getFunctionColumns(catalog, schemaPattern, functionNamePattern, columnNamePattern);
//	}
//	public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern,
//			String columnNamePattern) throws SQLException {
//		return inner.getPseudoColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern);
//	}
//	public boolean generatedKeyAlwaysReturned() throws SQLException {
//		return inner.generatedKeyAlwaysReturned();
//	}
//	public  long getMaxLogicalLobSize() throws SQLException {
//		return inner.getMaxLogicalLobSize();
//	}
//	public  boolean supportsRefCursors() throws SQLException {
//		return inner.supportsRefCursors();
//	}

}
