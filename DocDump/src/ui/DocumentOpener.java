package ui;

import java.util.HashMap;
import java.util.Map;

public class DocumentOpener {
  private static Map<String, String> mReaderMap = new HashMap<String, String>();
  private static DocumentOpener mOpener = new DocumentOpener();
  private static char[] mEscapists = {
    ' ', '(', ')'
  };
  
  private DocumentOpener() {
    DocumentOpener.mReaderMap.put(".pdf" , "evince"     );
    DocumentOpener.mReaderMap.put(".txt" , "gedit"      );
    DocumentOpener.mReaderMap.put(".docx", "libreoffice");
    DocumentOpener.mReaderMap.put(".c"   , "gvim"       );
    DocumentOpener.mReaderMap.put(".h"   , "gvim"       );
    DocumentOpener.mReaderMap.put(".cpp" , "gvim"       );
    DocumentOpener.mReaderMap.put(".jpg" , "eog"        );
    DocumentOpener.mReaderMap.put(".java", "gvim"       );
    DocumentOpener.mReaderMap.put(".ml"  , "gvim"       );
  }
  
  public static DocumentOpener getInstance() {
    return DocumentOpener.mOpener;
  }
  
  public void openDocument(String fileName) throws Exception {
    String s = DocumentOpener.getSuffix(fileName);
    String appName = DocumentOpener.mReaderMap.get(s);

    try {
      String command = appName + " " + DocumentOpener.adjustString(fileName);
      System.out.println("command = " + command);
      Runtime.getRuntime().exec(command);
      return;
    }
    catch(Exception e) {
    }
    throw new Exception("Unknown document type");
  }

  private static boolean isEscapist(char c) {
    for(char ch : DocumentOpener.mEscapists) {
      if(ch == c) {
        return true;
      }
    }
    return false;
  }
  
  private static String adjustString(String fileName) {
    String newFileName = "";
    for(int i = 0; i < fileName.length(); i++) {
      char c = fileName.charAt(i);
      if(DocumentOpener.isEscapist(c)) {
        newFileName = newFileName + '\\'; 
      }
      newFileName = newFileName + c;
    }
    return newFileName;
  }
  
  private static String getSuffix(String fileName) {
    for(String suffix : DocumentOpener.mReaderMap.keySet()) {
      if(fileName.endsWith(suffix)) {
        return suffix;
      }
    }
    return "";
  }
}
