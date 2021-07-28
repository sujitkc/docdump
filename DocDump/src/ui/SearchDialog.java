package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import docman.IDocManFactory;
import docman.IDocument;
import docman.IDocumentMap;

//Dialog for searching and listing the books.
//There are three panels: 
//	a search panel on top with a text box, and a search button.
//	a list panel with a scrollable list of book URIs.
//	a button panel with open and delete buttons.

public class SearchDialog extends JDialog implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -854492361134266994L;

	class SearchPanel extends JPanel implements KeyListener {


		/**
		 * 
		 */
		private static final long serialVersionUID = 5004549809822852348L;

		private JTextField mTextField = new JTextField(20);
		public JButton mSearchButton = new JButton("Search");
		private IDocManFactory mFactory;

		public boolean mTextEdited = true;
		public SearchPanel(IDocManFactory mDocManFactory) {
			this.mFactory = mDocManFactory;
			this.setLayout(new FlowLayout());
			this.add(mTextField);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(mSearchButton);
			this.mTextField.addKeyListener(this);
		}
		
		// If s = s_1; s_2; ... (strings separated by semi-colons) 
		// is in the text field of the search panel, return the list of document ids which have a label l
		// such that s_i is a sub-string of l.
		public Set<String> getResult() throws Exception {
			Set<String> allLabels = this.mFactory.getDocumentMap().getAllLabels();
			if(this.mTextField.getText().equals("")) {
				return this.mFactory.getDocumentMap().getAllDocumentIds();
			}
			else {
				String[] labels = this.mTextField.getText().split(";");
				Set<Set<String>> resultSets = new HashSet<Set<String>>();
				for(String newLabel : labels) {
					String ls = newLabel.toLowerCase().trim();
					Set<String> labelResults = new HashSet<String>();
					for(String label : allLabels) {
						if(label.toLowerCase().contains(ls)) {
							labelResults.addAll(this.mFactory.getDocumentMap().getDocumentIdsByLabel(label));
						}
					}
					resultSets.add(labelResults);
				}
				if(resultSets.size() == 1) {
					
					Iterator<Set<String>> it = resultSets.iterator();
					return it.next();
				}
				else {
					return intersect(resultSets);
				}
			}
		}

		private Set<String> intersect(Set<String> a, Set<String> b) {
			Set<String> result = new HashSet<String>();
			for(String s : a) {
				if(b.contains(s)) {
					result.add(s);
				}
			}
			return result;
		}
		
		private Set<String> intersect(Set<Set<String>> sets) throws Exception {
			if(sets.size() < 2) {
				throw new Exception("Set cardinality must be at least 2 for intersection to succeed");
			}
			Iterator<Set<String>> it = sets.iterator();
			Set<String> result = it.next();
			while(it.hasNext()) {
				result = intersect(result, it.next());
			}
			return result;
		}
		private Set<String> union(Set<String> a, Set<String> b) {
			Set<String> result = new HashSet<String>();
			for(String s : a) {
				result.add(s);
			}
			for(String s : b) {
				result.add(s);
			}
			return result;
		}
		@Override
		public void keyTyped(KeyEvent e) {
			this.mTextEdited  = true;
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	}
	
	class ListPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7397043013383131873L;
		private Map<String, String> mMap = new HashMap<String, String>();
		public JList<String> mDocumentIdList;
		public ListPanel() {
			DefaultListModel<String> model = new DefaultListModel<String>();
			this.mDocumentIdList = new JList<String>(model);
			this.mDocumentIdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.add(this.mDocumentIdList);
			this.setBorder(BorderFactory.createTitledBorder("Documents"));
		}

		// Cleanup the list-view and re-populate it with the details of the set of documents provided.
		public void updateList(Set<IDocument> docSet) {
			DefaultListModel<String> model = (DefaultListModel<String>)this.mDocumentIdList.getModel();
			model.clear();
			for(IDocument doc : docSet) {
				String displayedValue = "";
				if(doc.getURI().equals("")) {
					displayedValue = doc.getId();
				}
				else {
					displayedValue = doc.getURI();
				}
				model.addElement(displayedValue);
				this.mMap.put(displayedValue, doc.getId());
			}
		}
		
		public String getDocumentIdByDisplayedValue(String uri) {
			return this.mMap.get(uri);
		}
		
		// Remove the selected item in the list from the list-view.
		// This function doesn't touch the database.
		public void deleteDocument() {
			DefaultListModel<String> model = (DefaultListModel<String>)this.mDocumentIdList.getModel();
			String selectedValue = this.mDocumentIdList.getSelectedValue();
			if(selectedValue != null) {
				model.removeElement(selectedValue);
				this.mMap.remove(selectedValue);
			}
		}
	}
	
	class ButtonPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5793220939602028556L;
		
		public JButton mOpenButton = new JButton("Open");
		public JButton mDeleteButton = new JButton("Delete");
		public ButtonPanel() {
			this.setLayout(new FlowLayout());
			this.add(this.mOpenButton);
			this.add(Box.createRigidArea(new Dimension(5,0)));
			this.add(this.mDeleteButton);
		}
	}
	
	private SearchPanel mSearchPanel;
	private ListPanel mListPanel;
	private ButtonPanel mButtonPanel;
	
	private IDocManFactory mDocManFactory;
	public SearchDialog(IDocManFactory factory) {
		this.setTitle("Search - " + Program.AppName);
		this.setSize(500, 400);
		this.mDocManFactory = factory;
		
		this.mSearchPanel = new SearchPanel(this.mDocManFactory);
		this.mSearchPanel.mTextField.addKeyListener(this);
		this.mSearchPanel. mSearchButton.addActionListener(this);
		this.getContentPane().add(BorderLayout.NORTH, this.mSearchPanel);
		
		this.mListPanel = new ListPanel();
		try {
			this.updateListPanel();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		JScrollPane sp = new JScrollPane(this.mListPanel);
		this.getContentPane().add(BorderLayout.CENTER, sp);
		
		this.mButtonPanel = new ButtonPanel();
		this.getContentPane().add(BorderLayout.SOUTH, this.mButtonPanel);
		this.mButtonPanel.mOpenButton.addActionListener(this);
		this.mButtonPanel.mDeleteButton.addActionListener(this);
	}
	
	// Updates the list of documents in the list panel in the beginning or based on a new label search.
	// Update really happens only if the text field has been edited since the last search.
	private void updateListPanel() throws Exception {
		if(this.mSearchPanel.mTextEdited == false) {
			return;
		}
		this.mSearchPanel.mTextEdited = false;
		Set<String> searchResult = this.mSearchPanel.getResult();
		if(searchResult.isEmpty()) {
			JOptionPane.showMessageDialog(this, "No document found for label '" + this.mSearchPanel.mTextField.getText() + "'.");
		}
		Set<IDocument> documents = new HashSet<IDocument>();
		for(String docid : searchResult) {
			documents.add(this.mDocManFactory.getDocumentMap().getDocumentById(docid));
		}
		this.mListPanel.updateList(documents);		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(this.mButtonPanel.mOpenButton)) {
			this.openDocument();
		}
		else if(e.getSource().equals(this.mButtonPanel.mDeleteButton)) {
			this.deleteDocument();
		}
		else if(e.getSource().equals(this.mSearchPanel.mSearchButton)) {
			try {
				this.updateListPanel();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				System.err.println(e1.getMessage());
			}
		}
	}
	
	private void openDocument() {
		String displayedValue = this.mListPanel.mDocumentIdList.getSelectedValue();
		String docid = this.mListPanel.getDocumentIdByDisplayedValue(displayedValue);
		if(!(docid == null)) {
			EditDocumentDialog editDialog = new EditDocumentDialog(docid, this.mDocManFactory);
			editDialog.setVisible(true);
		}
		else {
			JOptionPane.showMessageDialog(this, "No document selected.");
		}
	}
	
	private void deleteDocument() {
		IDocumentMap documentMap = this.mDocManFactory.getDocumentMap();
		String selectedValue = this.mListPanel.mDocumentIdList.getSelectedValue();
		if(selectedValue != null) {
			String docid = this.mListPanel.getDocumentIdByDisplayedValue(selectedValue);
			int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + selectedValue + "?");
			if(option == 0) {
				this.mListPanel.deleteDocument();
				this.paintAll(this.getGraphics());
				documentMap.deleteDocument(docid);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if(e.getSource().equals(this.mSearchPanel.mTextField)) {
			if(e.getKeyChar() == KeyEvent.VK_ENTER) {
				try {
					this.updateListPanel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.err.println(e1.getMessage());
				}
			}
		}		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Nothing. Function is here because the class implements KeyListener interface. But, currently,
		// nothing to be done on key-press.
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Nothing. Function is here because the class implements KeyListener interface. But, currently,
		// nothing to be done on key-release.
		
	}
}