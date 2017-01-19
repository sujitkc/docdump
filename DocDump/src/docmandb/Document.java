package docmandb;

import java.io.IOException;
import java.util.Set;

import xmldb.XMLDB;

import db.IDB;
import docman.IDocument;

public class Document implements IDocument {

	private final String mId;
	private String mURI;
	
	private final IDB mDB = XMLDB.getInstance();
	public Document(String id) {
		this(id, "");
	}

	public Document(String id, String uri) {
		this.mId = id;
		this.mURI = uri;
	}
	
	@Override
	public String getId() {
		return this.mId;
	}

	@Override
	public Set<String> getLabels() {
		return DocumentMap.getInstance().getLabelsByDocumentId(this.mId);
	}
	@Override
	public String getURI() {
		return this.mURI;
	}
	@Override
	public void setURI(String uri) {
		this.mURI = uri;
		try {
			this.mDB.writeDocument(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean hasLabel(String label) {
		return this.getLabels().contains(label);
	}
}