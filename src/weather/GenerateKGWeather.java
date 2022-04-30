package weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;

public class GenerateKGWeather extends GenerateKG{
    /**
     * Main execution method
     */
    public static void main(String[] args) {

        String name = "src/turtlefiles/test.ttl";
        // Create a new KG generator
        GenerateKG gen = new GenerateKG();

        // Set the base URI to a custom value
        gen.setBase("http://mydomain.org/weatherkg/");
        // Define an additional prefix for the ontology namespace
        gen.setNsPrefix("onto", "http://mydomain.org/ontology/");
        // Add a single triple
        gen.addTriples("<> a onto:Entity .");
        // Add a bunch of triples at once
        gen.addTriples("<> rdfs:label \"Test entity\"@en;"
                + "  rdfs:commnt \"This is a test\"@en .");
        gen.serialise();
        try{
            gen.serialise(new FileOutputStream(name));
        }
        catch(FileNotFoundException ex)
        {
            File file = new File(name);
        }



/*
        try {
            String url = "https://www.meteociel.fr/temps-reel/obs_villes.php?affint=1&code2=7475&jour2=1&mois2=3&annee2=2022";
            Document doc = Jsoup.connect(url).get();
            Elements elts = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/center[2]/table[2]/tbody/tr");

            for (Element elt : elts) {
                // on selectionne l'heure
                String time = elt.selectXpath("tr/td[1]").first().text();
                System.out.println(time);
            }
        }
        catch (IOException e){

        }*/
    }
}
