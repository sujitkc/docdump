package strawman;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import docman.IDocument;
import docman.IDocumentMap;

public class DocumentMap implements IDocumentMap {

	private Map<String, Set<IDocument>> mMap = new HashMap<String, Set<IDocument>>();
	private Set<IDocument> mDocuments = new HashSet<IDocument>();
	protected static IDocumentMap mInstance = null;

	//protected constructor for implementation of singleton design-pattern.
	protected DocumentMap() {
/*		this.mDocuments.add(new Document("1", "sadfh"));
		this.mDocuments.add(new Document("2", "skdfhsfd"));
		this.mDocuments.add(new Document("3", "sdfn"));
		this.mDocuments.add(new Document("4", "ldkfg"));
		this.mDocuments.add(new Document("5", "hlkjdgf"));
		this.mDocuments.add(new Document("6", "mfghdfk"));
		this.mDocuments.add(new Document("7", " nvbrilt"));
		this.mDocuments.add(new Document("8", "dkflhg"));
		this.mDocuments.add(new Document("9", "khjb"));
		this.mDocuments.add(new Document("10", "kdsfg"));
		
		this.addLabelToDocument("1", "l1");
		this.addLabelToDocument("2", "l2");
		this.addLabelToDocument("3", "l3");
		this.addLabelToDocument("4", "l4");
		this.addLabelToDocument("5", "l5");
		this.addLabelToDocument("6", "l1");
		this.addLabelToDocument("7", "l2");
		this.addLabelToDocument("8", "l3");
		this.addLabelToDocument("9", "l4");
		this.addLabelToDocument("10", "l5");
		this.addLabelToDocument("1", "l1");
		this.addLabelToDocument("2", "l2");
		this.addLabelToDocument("3", "l3");
*/	}
	
	// singleton design pattern
	public static IDocumentMap getInstance() {
		if(DocumentMap.mInstance == null) {
			DocumentMap.mInstance = new DocumentMap();
		}
		return DocumentMap.mInstance;
	}
	
	@Override
	public void addDocument(IDocument doc) {
		this.mDocuments.add(doc);
	}

	@Override
	public void deleteDocument(String docid) {
		IDocument doc = this.getDocumentById(docid);
		if(doc != null) {
			Set<String> labels = doc.getLabels();
			for(String label : labels) {
				Set<IDocument> docs = this.mMap.get(label);
				docs.remove(doc);
			}
			this.mDocuments.remove(doc);
		}
	}

	@Override
	public void addLabel(String label) {
		if(!this.mMap.containsKey(label)) {
			this.mMap.put(label, new HashSet<IDocument>());
		}
	}

	@Override
	public Set<IDocument> getDocumentsByLabel(String label) {
		return this.mMap.get(label);
	}

	@Override
	public Set<String> getDocumentIdsByLabel(String label) {
		Set<IDocument> docs = this.mMap.get(label);
		Set<String> ids = new HashSet<String>();
		for(IDocument doc : docs) {
			ids.add(doc.getId());
		}
		return ids;
	}

	@Override
	public void addLabelToDocument(String docid, String label) {
		this.addLabel(label);
		IDocument doc = this.getDocumentById(docid);
		if(doc != null) {
			this.mMap.get(label).add(doc);
		}
	}

	@Override
	public void removeLabelFromDocument(String docid, String label) {
		if(!this.mMap.containsKey(label)) {
			return;
		}
		Set<IDocument> docs = this.mMap.get(label);
		IDocument doc = this.getDocumentById(docid);
		if(doc != null) {
			docs.remove(doc);
		}
	}

	@Override
	public IDocument getDocumentById(String id) {
		for(IDocument doc : this.mDocuments) {
			if(doc.getId().equals(id)) {
				return doc;
			}
		}
		return null;
	}

	// look for the document for with given id. If not found, return an
	// empty set. If found, look for the document in the map against each label. 
	// Return the set of labels against which the document is found.
	@Override
	public Set<String> getLabelsByDocumentId(String docid) {
		Set<String> labels = new HashSet<String>();
		IDocument doc = this.getDocumentById(docid);
		if(doc != null) {
			for(String label : this.mMap.keySet()) {
				if(this.mMap.get(label).contains(doc)) {
					labels.add(label);
				}
			}
		}
		return labels;
	}

	@Override
	public boolean lookupDocumentById(String docid) {
		if(this.getDocumentById(docid) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Set<String> getAllLabels() {
		return this.mMap.keySet();
	}

	@Override
	public Set<IDocument> getAllDocuments() {
		return this.mDocuments;
	}
	
	public String toString() {
		String s = "";
		for(String label : this.mMap.keySet()) {
			s = s + label + ": ";
			for(IDocument doc : this.mMap.get(label)) {
				s = s + doc.getId() + ", ";
			}
			s = s + "\n";
		}
		return s;
	}
}
