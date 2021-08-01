package docman;

import java.util.Set;

public interface IDocument {
  public String getId();
  public Set<String> getLabels();
  public String getURI();
  public void setURI(String uri);
  public boolean hasLabel(String label);
}
