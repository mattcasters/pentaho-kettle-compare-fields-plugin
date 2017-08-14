package org.pentaho.di.trans.steps.comparefields;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.injection.Injection;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.w3c.dom.Node;

public class CompareField {
  private static Class<?> PKG = CompareField.class; // for i18n purposes, needed by Translator2!!

  public static final String XML_TAG = "field";
  public static final String CODE_REFERENCE_FIELD = "reference_field";
  public static final String CODE_COMPARE_FIELD = "compare_field";
  public static final String CODE_IGNORE_CASE = "ignore_case";

  @Injection( name = "REFERENCE_FIELD", group = "COMPARE_FIELDS" )
  private String referenceFieldname;

  @Injection( name = "COMPARE_FIELD", group = "COMPARE_FIELDS" )
  private String compareFieldname;

  public CompareField() {
    super();
  }

  public CompareField( String referenceFieldname, String compareFieldname, boolean ignoringCase ) {
    super();
    this.referenceFieldname = referenceFieldname;
    this.compareFieldname = compareFieldname;
  }

  public String getXML() {
    StringBuilder xml = new StringBuilder();
    xml.append( XMLHandler.openTag( XML_TAG ) );
    xml.append( XMLHandler.addTagValue( CODE_REFERENCE_FIELD, referenceFieldname ) );
    xml.append( XMLHandler.addTagValue( CODE_COMPARE_FIELD, compareFieldname ) );
    xml.append( XMLHandler.closeTag( XML_TAG ) );
    return xml.toString();
  }

  public CompareField( Node fieldNode ) {
    referenceFieldname = XMLHandler.getTagValue( fieldNode, CODE_REFERENCE_FIELD );
    compareFieldname = XMLHandler.getTagValue( fieldNode, CODE_COMPARE_FIELD );
  }

  public void saveRep( Repository repository, ObjectId transformationId, ObjectId stepId, int fieldNr )
    throws KettleException {
    repository.saveStepAttribute( transformationId, stepId, fieldNr, CODE_REFERENCE_FIELD, referenceFieldname );
    repository.saveStepAttribute( transformationId, stepId, fieldNr, CODE_COMPARE_FIELD, compareFieldname );
  }

  public CompareField( Repository repository, ObjectId stepId, int fieldNr ) throws KettleException {
    referenceFieldname = repository.getStepAttributeString( stepId, fieldNr, CODE_REFERENCE_FIELD );
    compareFieldname = repository.getStepAttributeString( stepId, fieldNr, CODE_COMPARE_FIELD );
  }

  private volatile int referenceFieldIndex;
  private volatile int compareFieldIndex;

  public void index( RowMetaInterface rowMeta ) throws KettleException {

    if ( Const.isEmpty( referenceFieldname ) ) {
      throw new KettleException( BaseMessages.getString( PKG, "CompareField.Error.EmptyReferenceField" ) );
    }
    referenceFieldIndex = rowMeta.indexOfValue( referenceFieldname );
    if ( referenceFieldIndex < 0 ) {
      throw new KettleException(
        BaseMessages.getString( PKG, "CompareField.Error.ReferenceFieldNotFound", referenceFieldname ) );
    }
    if ( Const.isEmpty( compareFieldname ) ) {
      throw new KettleException(
        BaseMessages.getString( PKG, "CompareField.Error.CompareFieldEmpty", referenceFieldname ) );
    }
    compareFieldIndex = rowMeta.indexOfValue( compareFieldname );
    if ( compareFieldIndex < 0 ) {
      throw new KettleException(
        BaseMessages.getString( PKG, "CompareField.Error.CompareFieldNotFound", compareFieldname ) );
    }
  }

  public String getReferenceFieldname() {
    return referenceFieldname;
  }

  public void setReferenceFieldname( String referenceFieldname ) {
    this.referenceFieldname = referenceFieldname;
  }

  public String getCompareFieldname() {
    return compareFieldname;
  }

  public void setCompareFieldname( String compareFieldname ) {
    this.compareFieldname = compareFieldname;
  }

  public int getReferenceFieldIndex() {
    return referenceFieldIndex;
  }

  public void setReferenceFieldIndex( int referenceFieldIndex ) {
    this.referenceFieldIndex = referenceFieldIndex;
  }

  public int getCompareFieldIndex() {
    return compareFieldIndex;
  }

  public void setCompareFieldIndex( int compareFieldIndex ) {
    this.compareFieldIndex = compareFieldIndex;
  }
}
