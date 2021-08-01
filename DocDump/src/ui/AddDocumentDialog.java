package ui;

import java.util.HashSet;
import java.util.Set;

import docman.IDocManFactory;
import docman.IDocument;

public class AddDocumentDialog extends DocumentDialog {
  /**
   * 
   */
  private static final long serialVersionUID = -5597155924783525025L;

  public AddDocumentDialog(IDocManFactory factory) {
    super(null, factory);
    this.setTitle("Add Document - " + Program.AppName);
  }

  @Override
  protected void init() {
    Set<String> labels = new HashSet<String>();
    Set<String> notlabels = this.mDocumentMap.getAllLabels();
    this.mLabelPanel = new LabelPanel(labels, notlabels);
  }

  @Override
  protected void saveDocument() {
    IDocument document = this.mDocManFactory.buildDocument();
    this.mDocumentId = document.getId();
    this.mDocManFactory.getDocumentMap().addDocument(document);
    this.saveDocument(document);
  }
}