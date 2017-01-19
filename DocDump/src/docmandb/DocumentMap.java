package docmandb;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import xmldb.XMLDB;
import db.IDB;
import docman.IDocument;
import docman.IDocumentMap;

public class DocumentMap implements IDocumentMap {

	private static IDocumentMap mInstance;
	private Map<String, Set<String>> mMap = new HashMap<String, Set<String>>();
	private Set<String> mDocumentIds = new HashSet<String>();
	private IDB mDB = XMLDB.getInstance();
	
	private DocumentMap() {
		this.mDocumentIds = this.mDB.getAllDocumentIds();
		XMLDB db = (XMLDB)this.mDB;
		this.mMap = db.getMap();
	}
	
	// singleton design pattern
	public static IDocumentMap getInstance() {
		if(DocumentMap.mInstance == null) {
			DocumentMap.mInstance = new DocumentMap();
		}
		return DocumentMap.mInstance;
	}

	@Override
	public void addDocument(IDocument doc) {
		this.mDocumentIds.add(doc.getId());
		try {
			this.mDB.writeDocument(doc);
			this.mDB.writeDocumentMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteDocument(String docid) {
		IDocument doc = this.getDocumentById(docid);
		if(doc != null) {
			Set<String> labels = doc.getLabels();
			for(String label : labels) {
				Set<String> ids = this.mMap.get(label);
				ids.remove(docid);
			}
			this.mDocumentIds.remove(doc.getId());
		}
		try {
			this.mDB.deleteDocumentById(doc.getId());
			this.mDB.writeDocumentMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addLabel(String label) {
		if(!this.mMap.containsKey(label)) {
			this.mMap.put(label, new HashSet<String>());
		}
		try {
			this.mDB.writeDocumentMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addLabelToDocument(String docid, String label) {
		this.addLabel(label);
		if(this.mDocumentIds.contains(docid)) {
			this.mMap.get(label).add(docid);
		}
		try {
			this.mDB.writeDocument(this.getDocumentById(docid));
			this.mDB.writeDocumentMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeLabelFromDocument(String docid, String label) {
		if(!this.mMap.containsKey(label)) {
			return;
		}
		Set<String> docids = this.mMap.get(label);
		if(docids.contains(docid)) {
			docids.remove(docid);
		}
		try {
			this.mDB.writeDocumentMap(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Set<IDocument> getDocumentsByLabel(String label) {
		if(!this.mMap.containsKey(label)) {
			return null;
		}
		Set<String> ids = this.mMap.get(label);
		Set<IDocument> documents = new HashSet<IDocument>();
		for(String id : ids) {
			documents.add(this.mDB.getDocument(id));
		}
		return documents;
	}

	@Override
	public IDocument getDocumentById(String id) {
		return this.mDB.getDocument(id);
	}

	@Override
	public Set<String> getDocumentIdsByLabel(String label) {
		return this.mMap.get(label);
	}

	@Override
	public Set<String> getLabelsByDocumentId(String docid) {
		Set<String> labels = new HashSet<String>();
		for(String label : this.mMap.keySet()) {
			if(this.mMap.get(label).contains(docid)) {
				labels.add(label);
			}
		}
		return labels;
	}

	@Override
	public Set<String> getAllLabels() {
		return this.mMap.keySet();
	}

	@Override
	public boolean lookupDocumentById(String docid) {
		if(this.getDocumentById(docid) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Set<IDocument> getAllDocuments() {
		Set<IDocument> documents = new HashSet<IDocument>();
		for(String id : this.mDocumentIds) {
			documents.add(this.mDB.getDocument(id));
		}
		return documents;
	}

	@Override
	public Set<String> getAllDocumentIds() {
		return this.mDocumentIds;
	}
}