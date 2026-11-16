package app.arifnurrohim.applistbackup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Xml;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.zemna.android.app.AppItem;
import net.zemna.android.graphics.BitmapUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlSerializer;

/* JADX INFO: loaded from: classes.dex */
public class DomAppItemParser {
    public static List<AppItem> parse(String filePath) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        List<AppItem> items = new ArrayList<>();
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
                Node node = nodes.item(i);
                NodeList xxx = node.getChildNodes();
                AppItem ai = new AppItem();
                for (int j = 0; j < xxx.getLength(); j++) {
                    Node property = xxx.item(j);
                    String name = property.getNodeName();
                    if (name.equalsIgnoreCase("package_name")) {
                        ai.setPackageName(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase("version_code")) {
                        ai.setVersionCode(Integer.parseInt(property.getFirstChild().getNodeValue()));
                    } else if (name.equalsIgnoreCase("name")) {
                        ai.setAppName(property.getFirstChild().getNodeValue());
                    } else if (name.equalsIgnoreCase("is_system")) {
                        ai.setSystem(Boolean.parseBoolean(property.getFirstChild().getNodeValue()));
                    } else if (name.equalsIgnoreCase("icon")) {
                        byte[] bytes = Base64.decode(property.getFirstChild().getNodeValue(), Base64.DEFAULT);
                        ai.setIcon(new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                    }
                }
                items.add(ai);
            }
            return items;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeXml(List<AppItem> items, String filePath) throws Exception {
        FileOutputStream fos = new FileOutputStream(filePath);
        XmlSerializer serializer = Xml.newSerializer();
        try {
            try {
                serializer.setOutput(fos, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", "apps");
                for (int i = 0; i < items.size(); i++) {
                    AppItem item = items.get(i);
                    serializer.startTag("", "app");
                    serializer.startTag("", "package_name");
                    serializer.text(item.getPackageName());
                    serializer.endTag("", "package_name");
                    serializer.startTag("", "name");
                    serializer.text(item.getAppName());
                    serializer.endTag("", "name");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Bitmap bitmap = BitmapUtil.getBitmap(item.getIcon(), 48, 48);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    String imageBase64 = Base64.encodeToString(image, Base64.NO_WRAP);
                    serializer.startTag("", "Icon");
                    serializer.text(imageBase64);
                    serializer.endTag("", "Icon");
                    serializer.startTag("", "version_code");
                    serializer.text(String.valueOf(item.getVersionCode()));
                    serializer.endTag("", "version_code");
                    serializer.startTag("", "is_system");
                    serializer.text(item.isSystem() ? "true" : "false");
                    serializer.endTag("", "is_system");
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
