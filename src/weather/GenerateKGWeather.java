package weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateKGWeather extends GenerateKG {
    /**
     * Main execution method
     */

    public static void main(String[] args) {
        boolean jour2 = false;
        boolean mois2 = false;
        String jour;
        Integer mois;
        String ville;
        String name = "src/turtlefiles/test.ttl";
        // Create a new KG generator
        GenerateKG gen = new GenerateKG();
        // Set the base URI to a custom value
        gen.setBase("http://mydomain.org/weatherkg/");
        // Define an additional prefix for the ontology namespace
        gen.setNsPrefix("onto", "http://mydomain.org/ontology/");

        try {
            gen.serialise(new FileOutputStream(name));
        } catch (FileNotFoundException ex) {
            File file = new File(name);
        }

        try {
            String url = "https://www.meteociel.fr/temps-reel/obs_villes.php?affint=0&code2=7475&jour2=1&mois2=3&annee2=2021";
            Document doc = Jsoup.connect(url).get();
            Elements elts = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/center[2]/table/tbody/tr");
            Elements loc = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/h1/center");
            ville = loc.get(0).text().substring(loc.get(0).text().indexOf("pour")+5, loc.get(0).text().indexOf(" ("));
            System.out.println(ville);

            jour2 = url.substring(url.indexOf("jour2")+7, url.indexOf("jour2")+8) == "&";
            if (!jour2){
                jour = url.substring(url.indexOf("jour2")+6,url.indexOf("jour2")+7);
            }
            else{
                jour = url.substring(url.indexOf("jour2")+6,url.indexOf("jour2")+8);
            }

            mois2 = url.substring(url.indexOf("mois2")+7, url.indexOf("mois2")+8) == "&";
            if(!mois2){
                mois = Integer.parseInt(url.substring(url.indexOf("mois2")+6,url.indexOf("mois2")+7))+1;
            }
            else{
                mois = Integer.parseInt(url.substring(url.indexOf("mois2")+6,url.indexOf("mois2")+8))+1;
            }
            String Mois[] = {"null","Janvier","Février","Mars","Avril","Mai","Juin","Juillet","Août","Septembre","Octobre","Novemebre","Décembre"};
            String annee = url.substring(url.indexOf("annee2")+7,url.indexOf("annee2")+11);

            gen.addTriples(
                    "<"+ville+"> a onto:City ;"
                            +" onto:Departement "+loc.get(0).text().substring(loc.get(0).text().indexOf("(")+1, loc.get(0).text().indexOf(")"))+" ."
            );
            gen.addTriples(
                    "<Rapports" + annee + ">" +
                            " a onto:Year ;"
                            + " xsd:gYear \""+annee+"\" .");
            String m;
            if(mois>=10){
                m=mois.toString();
            }
            else{
                m="0"+mois;
            }
            gen.addTriples(
                    "<Rapports" + Mois[mois] + annee + ">" +
                            " a onto:Month ;"
                            +"onto:quand <Rapports" + annee + "> ;"
                            + " xsd:gYearMonth \""+annee + "-" + m + "\" .");

            gen.addTriples(
                    "<Rapports" + jour + Mois[mois] + annee + ">" +
                            " a onto:Day ;"
                            +"onto:quand <Rapports" + Mois[mois] + annee + "> ;"
                            + " xsd:date \""+annee + "-" + m + "-"+jour +"\" .");
            for (Element elt : elts) {
                // on selectionne l'heure
                String time = elt.selectXpath("tr/td[1]").first().text().replaceAll("\\s", "");
                String temp = elt.selectXpath("tr/td[5]").first().text().replaceAll("\\s", "");
                System.out.println(temp);


                gen.addTriples(
                        "<Rapports" +time.substring(0, time.indexOf("h")+1)+ jour + Mois[mois] + annee + ">" +
                                " a onto:Hour ;"
                                +"onto:quand <Rapports" + jour + Mois[mois] + annee + "> ;"
                                + " xsd:datetime \""+annee + "-" + m + "-"+jour +"\" .");

                gen.addTriples(
                        "<MesureHoraire"+time.substring(0, time.indexOf("h")+1)+ jour + Mois[mois] + annee + ville+">" +
                                "a onto:MesureHoraire ; " +
                                "onto:quand Rapports" + time.substring(0, time.indexOf("h")+1)+ jour + Mois[mois] + annee +"; " +
                                "onto:ou " + ville+" ; " +
                                "onto:temperature AZE ; " +
                                "onto:precipitation AZE ."
                );
            }

            gen.serialise();







        } catch (IOException e) {
        }
    }
}
