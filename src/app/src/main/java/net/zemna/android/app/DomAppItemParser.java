package net.zemna.android.app;

import android.util.Xml;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: classes.dex */
public class DomAppItemParser {
    public static List<String> parse(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<String> items = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.parse(new File(filePath));
            Element root = dom.getDocumentElement();
            NodeList nodes = root.getElementsByTagName("app");
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i).getFirstChild();
                items.add(node.getNodeValue());
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeXml(Iterator<AppItem> items, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        XmlSerializer serializer = Xml.newSerializer();
        try {
            try {
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "apps");
                while (items.hasNext()) {
                    AppItem iAppItem = items.next();
                    serializer.startTag("", "app");
                    serializer.text(iAppItem.getPackageName());
                    serializer.endTag("", "app");
                }
                serializer.endTag("", "apps");
                serializer.endDocument();
                fos.write("".getBytes());
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Throwable th) {
            fos.close();
            throw th;
        }
    }
}
