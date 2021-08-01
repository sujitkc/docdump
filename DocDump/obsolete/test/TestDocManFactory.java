package test;

import junit.framework.Assert;

import org.junit.Test;

import strawman.DocManFactory;

import docman.IDocument;

public class TestDocManFactory {

  @Test
  public void testDocManFactory() {
    DocManFactory factory = new DocManFactory();
    IDocument doc = factory.buildDocument();
    Assert.assertNotNull(doc);
  }

}
