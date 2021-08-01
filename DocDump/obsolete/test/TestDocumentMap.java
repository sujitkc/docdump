package test;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import strawman.Document;
import strawman.DocumentMap;

import docman.IDocument;
import docman.IDocumentMap;

public class TestDocumentMap {

  @Test
  public void testDocumentMap() {
    IDocumentMap map = DocumentMap.getInstance();
    Assert.assertNotNull(map);
  }
  
  @Test
  public void testAddDocument() {
    IDocumentMap map = DocumentMap.getInstance();
    Set<IDocument> docs = map.getAllDocuments();

    Assert.assertEquals(docs.size(), 0);
    
    IDocument doc = new Document("1");
    map.addDocument(doc);
    Assert.assertEquals(docs.size(), 1);
  }

  @Test
  public void testAddLabel() {
    
  }

  @Test
  public void testAddLabelToDocument() {
    
  }

  @Test
  public void testDeleteDocument() {
    
  }

  @Test
  public void testGetDocumentById() {
    
  }

  @Test
  public void testGetDocumentsByLabel() {
    
  }

  @Test
  public void testGetLabelsByDocumentId() {
    
  }

  @Test
  public void testLookupDocumentById() {
    
  }

  @Test
  public void testRemoveLabelFromDocument() {
    
  }

}
