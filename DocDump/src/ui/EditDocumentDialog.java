package ui;

import docman.IDocManFactory;
import docman.IDocument;
import docman.IDocumentMap;

public class EditDocumentDialog extends DocumentDialog {

  public EditDocumentDialog(String docid, IDocManFactory factory, boolean isStandalone) {
    super(docid, factory, isStandalone);
    this.setTitle("Edit Document - " + Program.AppName);
  }

  public EditDocumentDialog(String docid, IDocManFactory factory) {
    super(docid, factory);
    this.setTitle("Edit Document - " + Program.AppName);
  }
  // EditDocumentDialog's version of init()
  // It initialises the contents of the label-panel and search panel appropriately as follows:
  // - Search panel has the URI of the document associated with this dialog.
  // - Label panel has a list of labels. The checkboxes against the labels associated with the current
  //   document are checked. Others aren't.
  @Override
  protected void init() {
    IDocument document = this.mDocManFactory.getDocumentMap().getDocumentById(this.mDocumentId);
    this.mLabelPanel = new LabelPanel(this.getLabels(), this.getNotLabels());
    this.mSearchPanel.mFilePath.setText(document.getURI());
  }

  @Override
  protected void saveDocument() {
    IDocumentMap documentMap = this.mDocManFactory.getDocumentMap();
    IDocument document = documentMap.getDocumentById(this.mDocumentId);
    this.saveDocument(document);
  }
}
