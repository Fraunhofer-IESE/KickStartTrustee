//
//package de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.impl;
///* Created by chwalek on 12.08.2025 */
//
//import de.fraunhofer.iese.forgetrustee.demonstrator.backend.service.DatabaseConnection;
//
//import examples.fakeDatabases.version_FakeCarusoExampleV2.FakeDatabaseEntry;
//import examples.fakeDatabases.version_FakeCarusoExampleV2.FakeDatabaseV2;
//
//public class DatabaseConnection_FakeCarusoExampleV2 implements DatabaseConnection {
//
//  private FakeDatabaseV2 database;
//
//  public DatabaseConnection_FakeCarusoExampleV2() {
//  }
//
//  public DatabaseConnection_FakeCarusoExampleV2(FakeDatabaseV2 database) {
//    this.database = database;
//  }
//
//  @Override
//  public String getBaseModelTTL() {
//    String uriBase = this.database.getUriOfBase();
//    return this.getModelTTL(uriBase);
//  }
//
//  @Override
//  public String getModelTTL(String uri) {
//    return this.database.getDatabase().get(uri).getModelTTL();
//  }
//
//  @Override
//  public void setModelTTL(String uri, String modelTTL) {
//    this.database.getDatabase().get(uri).setModelTTL(modelTTL);
//  }
//
//  @Override
//  public void putNewEntry(String uri, Object newEntry) {
//    this.database.getDatabase().put(uri, (FakeDatabaseEntry) newEntry);
//  }
//
//  @Override
//  public void putNewEntry(String uri, String path, Object newEntry) {
//
//  }
//}
