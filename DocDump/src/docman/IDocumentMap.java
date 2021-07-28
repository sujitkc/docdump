package docman;

import java.util.Set;

public interface IDocumentMap {
	
	public void addDocument(IDocument doc);
	public void deleteDocument(String docid);
	public void addLabel(String label);
	
	// Given a label, return all the documents which are associated with that label.
	public Set<IDocument> getDocumentsByLabel(String label);
	
	// Given a label, return the ids of all the documents which are associated with that label.
	public Set<String> getDocumentIdsByLabel(String label);
	
	// Given a docid and a label, add the label to the document corresponding to docid.
	public void addLabelToDocument(String docid, String label);
	public void removeLabelFromDocument(String docid, String label);
	public IDocument getDocumentById(String label);
	public Set<String> getLabelsByDocumentId(String docid);
	public Set<String> getAllLabels();
	public boolean lookupDocumentById(String docid);
	public Set<IDocument> getAllDocuments();
	public Set<String> getAllDocumentIds();
}
