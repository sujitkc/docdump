package docmandb;

import docman.ADocManFactory;
import docman.IDocManFactory;
import docman.IDocument;
import docman.IDocumentMap;

public class DocManFactory extends ADocManFactory implements IDocManFactory {

	@Override
	public IDocumentMap getDocumentMap() {
		return DocumentMap.getInstance();
	}

	@Override
	public IDocument buildDocument() {
		String docid = this.generateDocumentId();
		IDocument doc = new Document(docid);
		return doc;
	}
}
