package weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class GenerateKGWeather extends GenerateKG{

    public static void main(String[] args) {
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

        }
    }
}
