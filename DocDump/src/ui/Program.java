package ui;

import java.util.Set;
import docman.IDocManFactory;
import db.IDB;
import docman.IDocumentMap;
import docman.IDocument;

public class Program {
  public static final String AppName = "DocDump";

  public static void main(String[] args) {

    IDocManFactory factory = new docmandb.DocManFactory();
    if(args.length == 0) {
      DocManFrame frame = new DocManFrame(factory);
      frame.setVisible(true);
    }
    else {
      System.out.println(args.length + " arguments.");
      for(String arg : args) {
        System.out.println("argument = " + arg);
      }
      IDocumentMap documentMap = factory.getDocumentMap();
      Set<IDocument> allDocuments = documentMap.getAllDocuments();      
      IDocument doc = null;
      for(IDocument d : allDocuments) {
        if(d.getURI().equals(args[0])) {
          doc = d;
          break;
        }
      }
      if(doc != null) {
        DocumentDialog dialog = new EditDocumentDialog(doc.getId(), factory, true);
        dialog.mSearchPanel.mFilePath.setText(args[0]);
        dialog.show();
      }
      else {
        DocumentDialog dialog = new AddDocumentDialog(factory, true);
        dialog.mSearchPanel.mFilePath.setText(args[0]);
        dialog.show();
      }
    }
  }
}
