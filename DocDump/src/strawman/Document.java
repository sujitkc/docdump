package strawman;

import java.util.Set;

import docman.IDocument;

public class Document implements IDocument {

	private final String mId;
	private String mURI;
	
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
	}

	@Override
	public boolean hasLabel(String label) {
		return this.getLabels().contains(label);
	}
}