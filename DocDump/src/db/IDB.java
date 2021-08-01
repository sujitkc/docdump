package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import docman.IDocument;
import docman.IDocumentMap;

//database interface
public interface IDB {
  public Set<String> getAllDocumentIds();
  public IDocument getDocument(String docId);
  public IDocumentMap getDocumentMap();
  public void writeDocumentMap(IDocumentMap map) throws FileNotFoundException, IOException;
  public void writeDocument(IDocument doc) throws IOException;
  public void deleteDocumentById(String docId) throws FileNotFoundException, IOException;
}
