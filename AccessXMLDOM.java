
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import java.util.*;
import java.io.*;
import static javax.management.Query.lt;

public class AccessXMLDOM {

    Document doc;

    public int abriXMLaDOM(File f) {
        try {

            System.out.println("Abriendo archivo XML file y generando DOM");
            //Creamos el documentBuilder al que le añadimos la variable factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //Ignoramos los comentarios y espacios en blanco
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            //Utilizamos el método parse que es el que genera DOM en memoria

            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
            //Ahora el documento apunta al arbol DOM y podemos recorrerlo
            System.out.println("DOM creado con éxito");
            return 0;//si el método funciona devuelve 0
        } catch (Exception e) {
            System.out.println(e);
            return -1;//si el metodo falla en algún momento devuelve -1
        }
    }

    public void recorreDOMyMuestra(File f) {

        String[] datos = new String[3];//Lo usamos para la información de cada libro
        //Creamos los nodos y el nodo raíz lo inicializamos
        Node nodo;
        Node root = doc.getFirstChild();
        NodeList nodelist = root.getChildNodes(); //Vemos el dibujo del árbol y cogemos los nodos
        //recorreremos el árbol DOM
        for (int i = 0; i < nodelist.getLength(); i++) {
            nodo = nodelist.item(i);//tomamos el valor de los hijos del elemento raíz
            if (nodo.getNodeType() == Node.ELEMENT_NODE) {//miramos nodos de tipo Element
                Node ntemp = null;
                int contador = 1;
//sacamos el valor del atributo publicado
                datos[0] = nodo.getAttributes().item(0).getNodeValue();
//sacamos los valores de los hijos de nodo, Titulo y Autor
                NodeList nl2 = nodo.getChildNodes();//obtenemos lista de hijos 
                for (int j = 0; j < nl2.getLength(); j++) {//Repetimos el proceso para coger el titulo y el autor en esa lista
                    ntemp = nl2.item(j);
                    if (ntemp.getNodeType() == Node.ELEMENT_NODE) {

                        if (ntemp.getNodeType() == Node.ELEMENT_NODE) {

                            datos[contador] = ntemp.getChildNodes().item(0).getNodeValue();

                            contador++;

                        }
                    }
                    //Mostramos los datos
                    System.out.println(datos[0] + "--" + datos[2] + "--" + datos[1]);
                }//
            }
        }
    }

    public int insertarLibroEnDOM(String titulo, String autor, String fecha) {
        try {

            System.out.println(" " + "Añadir libro al árbol DOM" + titulo + " " + autor + fecha);

        //creamos los nodos y los añadimos al padre desde las hojas a la raíz
        
            Node ntitulo = doc.createElement("Titulo");//crea etiquetas

            Node ntitulo_text = doc.createTextNode(titulo);//crea el nodo texto
            
            //Repetimos proceso para el autor
            Node nautor = doc.createElement("Autor");
            Node nautor_text = doc.createTextNode(autor);
            nautor.appendChild(nautor_text);
            //CREA LIBRO, con atributo y nodos Título y Autor
            Node nLibro = doc.createElement("Libro");
            ((Element) nLibro).setAttribute("publicado", fecha);
            nLibro.appendChild(ntitulo);
            nLibro.appendChild(nautor);

            nLibro.appendChild(doc.createTextNode("\n "));//para insertar saltos de línea
            //Hacemos el nodo raíz
            Node raiz = doc.getFirstChild();
            raiz.appendChild(nLibro);
            System.out.println("Libro insertado en DOM.");
            return 0;//Devuelve 0 si se añadimos el libro y si hay algún error devolverá -1
        } catch (Exception e) {
            System.out.println(e);
            return -1;
        }

    }

    public void guardarDOMcomoArchivo(String nuevoArchivo) {
        try {

            Source src = new DOMSource(doc); // Definimos el origen
            StreamResult rst = new StreamResult(new File(nuevoArchivo));//Genera el archivo
            //Creamos el transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            //Hace que haya sangría(Que el código este bien identado)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            //Crea el archivo DOM en el archivo
            transformer.transform(src, (javax.xml.transform.Result) rst);
            System.out.println("Archivo creado del DOM con éxito\n");
            //Cogemos cualquier error
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
