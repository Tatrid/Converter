package sample;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ComboBox ComboBox1, ComboBox2;
    public TextField TextField1, TextField2;

    String url = "https://www.bnm.md/ro/official_exchange_rates?get_xml=1&date=27.05.2021";

    public void Convert_Clicked() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new URL(url).openStream());
            doc.getDocumentElement().normalize();
            doc.getDocumentElement().getNodeName();
            NodeList nl = doc.getElementsByTagName("Valute");
            float ValueTo =1, ValueFrom = 1;
            for (int i = 0; i < nl.getLength(); i++) {
                Node nNode = nl.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element el = (Element) nNode;

                    String valute = el.getElementsByTagName("CharCode").item(0).getTextContent();
                    float value = Float.parseFloat(el.getElementsByTagName("Value").item(0).getTextContent());
                    System.out.println(ComboBox1.getValue());
                    if (ComboBox1.getValue().equals( valute)) {

                        ValueFrom = value;

                    }
                    if (ComboBox2.getValue().equals(valute)){
                        ValueTo = value;
                    }
                }
            }
            TextField2.setText(String.valueOf(ValueFrom * Float.parseFloat(TextField1.getText())/ValueTo));



        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }


    //only digit in TextField
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChangeListener<String> textListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> state, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {

                    Platform.runLater(() -> {
                        StringProperty textProperty = (StringProperty) state ;
                        TextField textField = (TextField) textProperty.getBean();
                        textField.setText(newValue.replaceAll("[^\\d.]", ""));

                    });

                }
            }
        };
        this.TextField1.textProperty().addListener(textListener);
        this.TextField2.textProperty().addListener(textListener);
    }
}
