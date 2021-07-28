package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import docman.IDocManFactory;
import docman.IDocument;
import docman.IDocumentMap;


// The dialog for viewing/editing the details of a particular document.
// There are four panels:
//	A Details panel contains the URI.
//	A label panel contains one list of pairs of a label and a checkbox.
//  
//	The third panel, called new label panel, contains:
//			a text field to enter a new label.
//			a 'add' button to add the new label to the labels associated with 
//			the current document. If the label already exists, this should flash
//			a message informing the user of this fact.
//	
//	The fourth panel is the button panel which has two buttons:
//		cancel button closes the dialog without saving the changes made to the
//		document details.
//		Save button saves the data as currently reflected on the dialog. It
//		closes the dialog.
public abstract class DocumentDialog extends JDialog implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5445883183893516029L;

	class SearchPanel extends JPanel implements ActionListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public final JTextField mFilePath = new JTextField(20);
		public final JButton mSearchButton = new JButton("Search");
		public final JButton mOpenButton = new JButton("Open");
		public SearchPanel() {
			this.setLayout(new FlowLayout());
			this.add(this.mFilePath);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(this.mSearchButton);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(this.mOpenButton);
			
			this.mSearchButton.addActionListener(this);
			this.mOpenButton.addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(this.mSearchButton)) {
				File file = new File("");
				JFileChooser chooser = new JFileChooser(".");
				int retval = chooser.showOpenDialog(null);
				if(retval == JFileChooser.APPROVE_OPTION) {
					file = chooser.getSelectedFile().getAbsoluteFile();
					this.mFilePath.setText(file.getAbsolutePath());
				}
			}
			if(e.getSource().equals(this.mOpenButton)) {
				String docName = this.mFilePath.getText();
				try {
					DocumentOpener opener = DocumentOpener.getInstance();
					opener.openDocument(docName);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(this, "Couldn't open file " + docName);
				}
			}
		}
	}
	
	class LabelPanel extends JPanel implements ItemListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Map<String, JCheckBox> mMap = new HashMap<String, JCheckBox>();
		public Set<String> mRemovedLabels = new HashSet<String>();
		public Set<String> mAddedLabels = new HashSet<String>();
		
		public LabelPanel(Set<String> labels, Set<String> notlabels) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			for(String label : labels) {
				this.addLabel(label, true);
			}
			for(String label: notlabels) {
				this.addLabel(label, false);
			}
			this.setBorder(BorderFactory.createTitledBorder("Labels"));
		}
		
		private String lookupLabel(String label) {
			for(String key : this.mMap.keySet()) {
				if(key.toLowerCase().equals(label.toLowerCase())) {
					return key;
				}
			}
			return null;
		}
		
		public boolean addLabel(String label, boolean state) {
			String key = this.lookupLabel(label); 
			if(key != null) {
				JCheckBox checkBox = this.mMap.get(key);
				if(checkBox.isSelected()) {
					return false;
				}
				else {
					checkBox.setSelected(true);
					return true;
				}
			}
			JPanel row = new JPanel();
			row.setLayout(new FlowLayout());
			JLabel labelLabel = new JLabel(label);
			labelLabel.setPreferredSize(new Dimension(200, labelLabel.getPreferredSize().height));
			row.add(labelLabel, FlowLayout.LEFT);
			JCheckBox checkBox = new JCheckBox();
			checkBox.setSelected(state);
			checkBox.addItemListener(this);
			this.mMap.put(label, checkBox);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			row.add(checkBox);
			this.add(row);
			return true;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			for(String label : this.mMap.keySet()) {
				if(e.getSource().equals(this.mMap.get(label))) {
					if(e.getStateChange() == ItemEvent.DESELECTED) {
						if(this.mAddedLabels.contains(label)) {
							this.mAddedLabels.remove(label);
						}
						else {
							this.mRemovedLabels.add(label);
						}
					}
					else {
						if(this.mRemovedLabels.contains(label)) {
							this.mRemovedLabels.remove(label);
						}
						else {
							this.mAddedLabels.add(label);
						}
					}
					break;
				}
			}
		}
	}

	class NewLabelPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8287526177677324997L;

		public JTextField mNewLabel = new JTextField(20);
		public JButton mAddButton = new JButton("Add");
		public NewLabelPanel() {
			this.setLayout(new FlowLayout());
			this.add(mNewLabel);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(mAddButton);
			this.setBorder(BorderFactory.createTitledBorder("New Label"));
		}
	}

	class ButtonPanel extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public final JButton mSaveButton = new JButton("Save");
		public final JButton mCancelButton = new JButton("Cancel");
		
		public ButtonPanel() {
			this.setLayout(new FlowLayout());
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(this.mSaveButton);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(this.mCancelButton);
		}
	}

	protected IDocManFactory mDocManFactory;
	protected IDocumentMap mDocumentMap;
	protected String mDocumentId;
	
	protected SearchPanel mSearchPanel     = new SearchPanel();
	protected LabelPanel mLabelPanel;
	protected ButtonPanel mButtonPanel     = new ButtonPanel();
	protected NewLabelPanel mNewLabelPanel = new NewLabelPanel();
	
	protected abstract void init();
	
	public DocumentDialog(String id, IDocManFactory factory) {
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.mDocManFactory = factory;
		this.mDocumentMap = this.mDocManFactory.getDocumentMap();
		this.mDocumentId = id;
		this.init();
		
		this.setSize(500, 450);
		
		this.add(this.mSearchPanel);
		this.add(new JScrollPane(this.mLabelPanel));
		this.add(this.mNewLabelPanel);
		this.add(this.mButtonPanel);
		
		this.mNewLabelPanel.mAddButton.addActionListener(this);
		this.mNewLabelPanel.mNewLabel.addKeyListener(this);
		this.mButtonPanel.mCancelButton.addActionListener(this);
		this.mButtonPanel.mSaveButton.addActionListener(this);
	}

	protected Set<String> getLabels() {
		IDocument document = this.mDocumentMap.getDocumentById(this.mDocumentId);
		if(document != null) {
			return document.getLabels();			
		}
		return new HashSet<String>();
	}

	protected Set<String> getNotLabels() {
		Set<String> allLabels = this.mDocumentMap.getAllLabels();
		Set<String> labels = this.getLabels();
		Set<String> notlabels = new HashSet<String>();
		for(String label : allLabels) {
			if(!labels.contains(label)) {
				notlabels.add(label);
			}
		}
		return notlabels;
	}

	protected abstract void saveDocument();
	
	private void handleAddLabel() {
		String[] newLabels = this.mNewLabelPanel.mNewLabel.getText().split(";");
		for(String newLabel : newLabels) {
			if(newLabel.equals("")) {
				JOptionPane.showMessageDialog(this, "Empty label: can't add.");
				return;
			}
			boolean result = this.mLabelPanel.addLabel(newLabel, true);
			if(result == true) {
				if(DocumentDialog.stringSetContainsCaseInsensitive(this.mLabelPanel.mAddedLabels, newLabel) == false) {
					this.mLabelPanel.mAddedLabels.add(newLabel);
					this.paintAll(this.getGraphics());
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "Label already exists.");
			}
		}
		this.mNewLabelPanel.mNewLabel.setText("");
	}
	
	private static boolean stringSetContainsCaseInsensitive(Set<String> set, String s) {
		for(String e : set) {
			if(e.toLowerCase().equals(s.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.mButtonPanel.mCancelButton)) {
			this.setVisible(false);
		}
		else if(e.getSource().equals(this.mNewLabelPanel.mAddButton)) {
			this.handleAddLabel();
		}
		else if(e.getSource().equals(this.mButtonPanel.mSaveButton)) {
			this.saveDocument();
			this.setVisible(false);
		}
	}

	// Implement the functionality that the label addition happens when the user presses
	// ENTER while typing the label.
	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getSource().equals(this.mNewLabelPanel.mNewLabel)) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				this.handleAddLabel();
			}
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	protected void saveDocument(IDocument document) {
		document.setURI(this.mSearchPanel.mFilePath.getText());
		IDocumentMap documentMap = this.mDocManFactory.getDocumentMap();
		for(String label : this.mLabelPanel.mAddedLabels) {
			documentMap.addLabelToDocument(this.mDocumentId, label);
		}
		for(String label : this.mLabelPanel.mRemovedLabels) {
			documentMap.removeLabelFromDocument(this.mDocumentId, label);
		}
	}
}