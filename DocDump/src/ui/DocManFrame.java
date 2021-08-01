package ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import docman.IDocManFactory;

public class DocManFrame extends JFrame implements ActionListener {

  /**
   * 
   */
  private static final long serialVersionUID = 4965429427299910727L;
  
  IDocManFactory mDocManFactory;
  
  JButton mSearchButton = new JButton("Search");
  JButton mAddButton = new JButton("Add Document");

   public DocManFrame(IDocManFactory factory) {
    this.mDocManFactory = factory;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(400, 125);
    this.setTitle(Program.AppName);
    
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(this.mAddButton);
    buttonPanel.add(Box.createHorizontalGlue());
    buttonPanel.add(this.mSearchButton);
    buttonPanel.add(Box.createHorizontalGlue());
    
    this.getContentPane().add(buttonPanel, BorderLayout.CENTER);

    this.mAddButton.addActionListener(this);
    this.mSearchButton.addActionListener(this);
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource().equals(this.mAddButton)) {
      DocumentDialog documentDialog = new AddDocumentDialog(this.mDocManFactory);
      documentDialog.setVisible(true);
    }
    else if(e.getSource().equals(this.mSearchButton)) {
      SearchDialog searchDialog = new SearchDialog(this.mDocManFactory);
      searchDialog.setVisible(true);
    }
  }

}
