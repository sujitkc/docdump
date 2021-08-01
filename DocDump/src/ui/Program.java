package ui;

import docman.IDocManFactory;

public class Program {
  public static final String AppName = "DocDump";

  public static void main(String[] args) {
    IDocManFactory factory = new docmandb.DocManFactory();
    DocManFrame frame = new DocManFrame(factory);
    frame.setVisible(true);
  }
}
