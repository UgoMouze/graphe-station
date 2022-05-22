# Documentation du modèle

Le modèle est disponible sur le fichier suivant [finalstate.ttl](https://github.com/UgoMouze/graphe-station/blob/main/src/turtlefiles/finalstate.ttl)

## Structure générale
Nous avons séparé notre modèle en 4 classes comprenant plusieurs sous-classes :  
* Alert
    * Blizard
    * Canicule
    * Inondation
    * Neige
    * Visibilite
    * Vent
        * Vent Violent
* Date
    * Day
    * Hour
    * Month
    * Year
* Donnees
    * PrecipitationHoraire
    * TemperatureHoraire
    * Ville
* Mesures
    * MesureHoraire
    * MesureQuotidienne

![svg]( https://github.com/UgoMouze/graphe-station/blob/main/src/finalstate.svg "finalstate")

## Description