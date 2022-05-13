package weather;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class GenerateKGWeather {
    /**
     * Main execution method
     */

    public static void main(String[] args) {
        String name = "src/turtlefiles/stationmeteo.ttl";
        // Create a new KG generator
        GenerateKG gen = new GenerateKG();
        // Set the base URI to a custom value
        gen.setBase("http://mydomain.org/weatherkg/");
        // Define an additional prefix for the ontology namespace
        gen.setNsPrefix("onto", "http://mydomain.org/ontology/");


        try {
            boolean jour2;
            boolean mois2;
            String jour;
            Integer mois;
            String ville;
            float visimini = 10;
            float nivprecip = 0;
            float tempmax = 0;
            float tempmin = 10;

            String url = "https://www.meteociel.fr/temps-reel/obs_villes.php?affint=1&code2=7475&jour2=20&mois2=3&annee2=2021";
            Document doc = Jsoup.connect(url).get();
            Elements elts = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/center[2]/table/tbody/tr");
            Elements loc = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/h1/center");
            ville = loc.get(0).text().substring(loc.get(0).text().indexOf("pour") + 5, loc.get(0).text().indexOf(" ("));
            Elements eltsvent = doc.selectXpath("/html/body/table[1]/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/table/tbody/tr/td/center[2]/table[1]/tbody/tr[2]/td[3]");
            Float rafale = Float.valueOf(eltsvent.first().text().replaceAll("\\s", "")
                    .substring(0, eltsvent.first().text().replaceAll("\\s", "").indexOf("km/h"))).floatValue();
            jour2 = url.substring(url.indexOf("jour2") + 7, url.indexOf("jour2") + 8) == "&";
            if (!jour2) {
                jour = url.substring(url.indexOf("jour2") + 6, url.indexOf("jour2") + 7);
            } else {
                jour = url.substring(url.indexOf("jour2") + 6, url.indexOf("jour2") + 8);
            }

            mois2 = url.substring(url.indexOf("mois2") + 7, url.indexOf("mois2") + 8) == "&";
            if (!mois2) {
                mois = Integer.parseInt(url.substring(url.indexOf("mois2") + 6, url.indexOf("mois2") + 7)) + 1;
            } else {
                mois = Integer.parseInt(url.substring(url.indexOf("mois2") + 6, url.indexOf("mois2") + 8)) + 1;
            }
            String Mois[] = {"null", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novemebre", "Décembre"};
            String annee = url.substring(url.indexOf("annee2") + 7, url.indexOf("annee2") + 11);

            gen.addTriples(
                    "<" + ville + "> a onto:Ville ;"
                            + " onto:Departement " + loc.get(0).text().substring(loc.get(0).text().indexOf("(") + 1, loc.get(0).text().indexOf(")")) + " ."
            );
            gen.addTriples(
                    "<Rapports" + annee + ">" +
                            " a onto:Year ;"
                            + " xsd:gYear \"" + annee + "\" .");
            String m;
            if (mois >= 10) {
                m = mois.toString();
            } else {
                m = "0" + mois;
            }
            gen.addTriples(
                    "<Rapports" + Mois[mois] + annee + ">" +
                            " a onto:Month ;"
                            + "onto:Year <Rapports" + annee + "> ;"
                            + " xsd:gYearMonth \"" + annee + "-" + m + "\" .");

            gen.addTriples(
                    "<Rapports" + jour + Mois[mois] + annee + ">" +
                            " a onto:Day ;"
                            + "onto:Month <Rapports" + Mois[mois] + annee + "> ;"
                            + " xsd:date \"" + annee + "-" + m + "-" + jour + "\" .");
            // On enlève les éléments inutilisables
            for (int i = 0; i < 3; i++) {
                elts.remove(0);
            }
            boolean changementHeure;
            float temperature = 0;
            float precipitation = 0;
            int n = 0;
            String hour = "";
            for (Element elt : elts) {

                // on selectionne l'heure
                String time = elt.selectXpath("tr/td[1]").first().text().replaceAll("\\s", "");
                Float temp = Float.valueOf(elt.selectXpath("tr/td[5]").first().text().replaceAll("\\s", "").substring(0, elt.selectXpath("tr/td[5]").first().text().replaceAll("\\s", "").indexOf("°C"))).floatValue();
                String visib = elt.selectXpath("tr/td[4]").first().text();
                if (visib.compareTo("")!=0){
                    Float visi = Float.valueOf(elt.selectXpath("tr/td[4]").first().text().replaceAll("\\s", "")
                            .substring(0, elt.selectXpath("tr/td[4]").first().text().replaceAll("\\s", "").indexOf("km"))).floatValue();
                    if (visi < visimini){
                        visimini = visi;
                    }
                }
                String precip = elt.selectXpath("tr/td[12]").first().text().replaceAll("\\s", "");


                // initialisation
                if (hour.compareTo("") == 0) {
                    hour = time.substring(0, time.indexOf("h") + 1);
                }
                changementHeure = time.substring(0, time.indexOf("h") + 1).compareTo(hour) != 0;

                if (changementHeure) {
                    nivprecip += precipitation;
                    temperature = temperature / n;
                    gen.addTriples(
                            "<TemperatureMoyenneHoraire" + hour + jour + Mois[mois] + annee + ville + "> " +
                                    "a onto:TemperatureMoyenneHoraire ; " +
                                    "onto:valeur \"" + temperature + "\" ; " +
                                    "xsd:unit \"Celsius\" ."
                    );
                    gen.addTriples(
                            "<PrecipitationHoraire" + hour + jour + Mois[mois] + annee + ville + "> " +
                                    "a onto:PrecipitationHoraire ;" +
                                    "onto:valeur \"" + precipitation + "mm\" ."
                    );

                    gen.addTriples(
                            "<Rapports" + hour + jour + Mois[mois] + annee + ">" +
                                    " a onto:Hour ;"
                                    + "onto:Day <Rapports" + jour + Mois[mois] + annee + "> ;"
                                    + " xsd:datetime \"" + annee + "-" + m + "-" + jour + "T" + hour + "\" .");

                    gen.addTriples(
                            "<MesureHoraire" + hour + jour + Mois[mois] + annee + ville + ">" +
                                    "a onto:MesureHoraire ; " +
                                    "onto:MesureQuotidienne <MesureQuotidienne" + hour + jour + Mois[mois] + annee + ville + "> ; "+
                                    "onto:Hour <Rapports" + hour + jour + Mois[mois] + annee + "> ; " +
                                    "onto:Ville <" + ville + "> ; " +
                                    "onto:TemperatureMoyenneHoraire <TemperatureMoyenneHoraire" + hour + jour + Mois[mois] + annee + ville + "> ; " +
                                    "onto:PrecipitationHoraire <PrecipitationHoraire" + hour + jour + Mois[mois] + annee + ville + "> ."
                    );



                    hour = time.substring(0, time.indexOf("h") + 1);
                    n = 0;
                    temperature = 0;
                    precipitation = 0;
                } else {
                    temperature += temp;
                    if (tempmin > temp){
                        tempmin = temp;
                    }
                    if (tempmax<temp){
                        tempmax= temp;
                    }
                    if (precip.indexOf("aucune") != 0) {
                        precipitation += Float.valueOf(precip.substring(0, precip.indexOf("mm")));
                    }
                    n++;
                }
            }
            gen.addTriples(
                    "<MesureQuotidienne" + hour + jour + Mois[mois] + annee + ville + ">" +
                            "a onto:MesureQuotidienne ; " +
                            "onto:Day <Rapports" + jour + Mois[mois] + annee + "> ; " +
                            "onto:Ville <" + ville + "> ; " +
                            "onto:Alert <Alert" + hour + jour + Mois[mois] + annee + ville + "> ."
            );

            gen.addTriples(
                    "<Alert" + jour + Mois[mois] + annee + ville + "> " +
                            "a onto:Alert ; " +
                            "onto:Visibilite <Visibilite" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Inondation <Inondation" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Neige <Neige" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Verglas <Verglas" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Vents_Violents <Vents_Violents" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Blizzard <Blizzard" + jour + Mois[mois] + annee + ville + "> ; "+
                            "onto:Canicule <Canicule" + jour + Mois[mois] + annee + ville + "> . "
            );

            if (visimini < 1){
                gen.addTriples(
                        "<Visibilite" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Visibilite ;" +
                                "xsd:string \"Pas de visibilité\" . "
                );
            }

            if (nivprecip > 500){
                gen.addTriples(
                        "<Inondation" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Inondation ;" +
                                "xsd:string \"Risque d'inondation\" . "
                );
            }

            if (nivprecip > 10 && tempmin < 0){
                gen.addTriples(
                        "<Neige" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Neige ;" +
                                "xsd:string \"Risque de neige\" . "
                );
            }

            if (rafale > 100){
                gen.addTriples(
                        "<Vents_Violents" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Vents_Violents ;" +
                                "xsd:string \"Risque de vents violents\" . "
                );
            } else if (rafale >50 && tempmin < 0 && nivprecip > 10) {
                gen.addTriples(
                        "<Blizzard" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Blizzard ;" +
                                "xsd:string \"Risque de blizzard\" . "
                );
            }

            if (tempmax > 35){
                gen.addTriples(
                        "<Canicule" + jour + Mois[mois] + annee + ville + "> " +
                                "a onto:Canicule ;" +
                                "xsd:string \"Risque de canicule\" . "
                );
            }

            gen.serialise();
            try {
                gen.serialise(new FileOutputStream(name));
            } catch (FileNotFoundException ex) {
                new File(name);
            }
        } catch (IOException e) {

        }
    }
}
