package xmldb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import db.IDB;
import docman.IDocument;
import docman.IDocumentMap;

public class XMLDB implements IDB {

	private String mDBLocation;
	private String mLabelFileName;
	private static IDB mInstance = new XMLDB();

	private XMLDB() {
		try {
			this.mDBLocation = this.readDBLocation();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		this.mLabelFileName = this.mDBLocation + "/labels.xml";
	}
	
	private String readDBLocation () throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(System.getenv("HOME") + "/docdump/DocDumpConfig.xml");
		NodeList list = document.getDocumentElement().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item (i).getNodeName().equals("DBLocation")) {
				return list.item(i).getTextContent();
			}
		}
		throw new Exception ("Configuration file is corrupted.");
	}

	public static IDB getInstance() {
		return XMLDB.mInstance;
	}
	
	@Override
	public IDocument getDocument(String docId) {
		File file = new File(this.mDBLocation + '/' + docId + ".xml");
		if(!file.isFile()) {
			return null;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(this.mDBLocation + '/' + docId + ".xml");
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		NodeList list = document.getDocumentElement().getChildNodes();
		
		String id = "";
		String uri = "";
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName() == "id") {
				id = list.item(i).getTextContent();
			}
			if (list.item(i).getNodeName() == "uri") {
				uri = list.item(i).getTextContent();
			}
		}
		return new docmandb.Document(id, uri);
	}

	@Override
	public Set<String> getAllDocumentIds() {
		File dbDir = new File(this.mDBLocation);
		File[] listofFiles = dbDir.listFiles();
		Set<String> ids = new HashSet<String>();
		for(File f : listofFiles) {
			String fileName = f.getName();
			if(fileName.startsWith("doc") && fileName.endsWith(".xml")) {
				ids.add(fileName.substring(0, fileName.length() - ".xml".length()));
			}
		}
		return ids;
	}

	@Override
	public IDocumentMap getDocumentMap() {
		return docmandb.DocumentMap.getInstance();
	}

	public Map<String, Set<String>> getMap() {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}

		Document document = null;
		try {
			document = builder.parse(this.mLabelFileName);
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		NodeList list = document.getDocumentElement().getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("label")) {
				Node labelNode = list.item(i);
				NodeList docNodeList = labelNode.getChildNodes();
				String id = null;
				Set<String> docs = new HashSet<String>();
				for(int j = 0; j < docNodeList.getLength(); j++) {
					if(docNodeList.item(j).getNodeName().equals("id")) {
						Node idNode = docNodeList.item(j);
						id = idNode.getTextContent();
					}
					if(docNodeList.item(j).getNodeName().equals("document")) {
						Node documentNode = docNodeList.item(j);
						String docid = documentNode.getTextContent();
						docs.add(docid);
					}
				}
				if(id != null) {
					map.put(id, docs);
				}
			}
		}
		return map;
	}
	
	@Override
	public void writeDocumentMap(IDocumentMap map) throws IOException {
		Document xmldoc = new DocumentImpl();
		Element root = xmldoc.createElement("labels");
		xmldoc.appendChild(root);
		
		for(String label : map.getAllLabels()) {
			Element labelNode = xmldoc.createElement("label");
			root.appendChild(labelNode);
			
			Element idNode = xmldoc.createElement("id");
			idNode.appendChild(xmldoc.createTextNode(label));
			labelNode.appendChild(idNode);
			for(String docid : map.getDocumentIdsByLabel(label)) {
				Element docNode = xmldoc.createElement("document");
				docNode.appendChild(xmldoc.createTextNode(docid));
				labelNode.appendChild(docNode);
			}
		}
		this.writeToFile(this.mDBLocation + '/' + "labels.xml", xmldoc);
	}

	@Override
	public void writeDocument(IDocument doc) throws IOException, DOMException {
		Document xmldoc = new DocumentImpl();
		Element root = xmldoc.createElement("document");
		xmldoc.appendChild(root);
		
		Element idNode = xmldoc.createElement("id");
		idNode.appendChild(xmldoc.createTextNode(doc.getId()));
		root.appendChild(idNode);
		
		Element uriNode = xmldoc.createElement("uri");
		uriNode.appendChild(xmldoc.createTextNode(doc.getURI()));
		root.appendChild(uriNode);
		
		String documentFileName = this.mDBLocation + '/' + doc.getId() + ".xml"; 
		this.writeToFile(documentFileName, xmldoc);

	}
	
	private void writeToFile (String fileName, Document xmldoc) throws IOException {
		Element element = xmldoc.getDocumentElement();
		FileOutputStream fos = new FileOutputStream(fileName);
		
		OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
		of.setIndent(1);
		of.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer(fos,of);
				
		serializer.asDOMSerializer();
		serializer.serialize( element);
		fos.close();
	}

	@Override
	public void deleteDocumentById(String docId) throws FileNotFoundException,
			IOException {
		String documentFileName = this.mDBLocation + '/' + docId + ".xml";
		File documentFile = new File(documentFileName);
		if(documentFile.exists()) {
			documentFile.delete();
		}
		
	}
}
