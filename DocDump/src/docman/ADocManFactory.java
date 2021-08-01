package docman;

import java.util.Random;

public abstract class ADocManFactory implements IDocManFactory {    
  protected String generateDocumentId() {
    while (true) {
      Random random = new Random ();
      int integer = random.nextInt();
      if (integer < 0) {
        integer = -1 * integer;
      }
      String id = "doc" + Integer.toString(integer);
      if (!lookupDocumentId(id)) {
        return id;
      }
    }
  }
  
  private boolean lookupDocumentId(String id) {
    return this.getDocumentMap().lookupDocumentById(id);
  }
}
