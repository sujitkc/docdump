package test;

import junit.framework.Assert;

import org.junit.Test;

import strawman.Document;


public class TestDocument {

  @Test
  public void testDocument() {
    Document doc = new Document("id");
    Assert.assertEquals(doc.getId(), "id");
  }
}
