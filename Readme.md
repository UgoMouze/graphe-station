# Documentation du modèle

Le modèle est disponible sur le fichier suivant [finalstate.ttl](https://github.com/UgoMouze/graphe-station/blob/main/src/turtlefiles/finalstate.ttl)

## **Structure générale**
Nous avons séparé notre modèle en 4 classes comprenant plusieurs sous-classes pouvant être observées sur le graphique suivant :  
![getting image]( https://github.com/UgoMouze/graphe-station/blob/main/finalstate.svg "finalstate")


## **Description**
### **Alertes**
Les alertes permettent de qualifier les risques associés à une situation météorologique.  

***Canicule***  
La canicule est un phénomène météorologique caractérisé par de fortes températures durant 3 jours d'affilé. Néanmoins, nous avons décidé de caractériser ce phénomène par une `température maximale de la journée à 35°C`.  
  
***Inondation***  
Une inondation est une submersion temporaire, naturelle ou artificielle, d'un espace par de l'eau liquide. Nous avons décidé que cela correspond à des précipitations totales de `500mm sur une journée`.  
  
***Neige***  
Nous avond décidé que l'alerte Neige correspond à une `température minimale inférieure à 0°C` et à des `précipitations suppérieures à 10mm sur une journée`.  
  
***Vent***  
La classe vent est une classe servant à classer les phénomènes.  
  
***Vents violents***  
Nous avons considéré que ce phénomène est observé avec des rafales à plus de `100km/h`.  
  
***Visibilité***  
Nous avons noté des problèmes de visibilité avec une visibilité inférieure à `1km` à tout moment de la journée.  
  
***Blizzard***  
Pour le phénomène de Blizzard, nous avons considéré que les rafales de vents soient à plus de `50km/h`, une température inférieure à `0°C` et des précipitations supérieures à `10mm sur une journée`.
### **Date**
Les dates permettent de séparer les différentes mesures.  
  
***Day***  
La façon dont nous avons pris les mesures fait que les mesures sont regroupées par jour. Ainsi la mesure de base est la mesure correspondant au jour.  
  
***Hour***  
Il était également possible de prendre des mesures toutes les heures, ou plus. Il était donc naturel de conserver cette fréquence de mesure.  

***Month***  
Pour les mois, nous aurions pu faire des mesures pour beaucoup de jours, mais dans un objectif descriptif du rendu, il était préférable de se limiter aux valeurs sur une journée.  
  
***Year***  
Le commentaire est le même que celui du mois. Il était possible de trouver des formules pour faire des mesures correctes sur une année, mais il n'est pas recommendé de le faire.  
### **Donnees**
Les données correspondent aux valeurs quantitatives pouvant être retrouvées et exploitées.  
  
***PrecipitationHoraire***  
Les valeurs de précipitation horaires peuvent être reprises. Elles sont retranscrise en `mm/h` dans cet exemple.  
  
***TemperatureMoyenneHoraire***  
Pour la température, nous avons choisi de prendre la température moyenne sur une heure afin de quantifier cet évènement.  
  
***Ville***  
Il n'était pas possible d'extraire la `station météo` avec notre méthode. Nous avons donc choisi de qualifier nos mesures avec la ville ratachée à la station.
### **Mesure**
***MesureHoraire***  
Nous avons séparé les mesures à chaque heure de la journée. Ainsi nous pouvions évaluer l'évolution des phénomènes au cours de la journée.  
  
***MesureQuotidienne***  
Enfin, nous pouvons moyenner nos valeurs afin d'obtenir des relevé quotidien afin de relever l'évolution des phénomènes au cours d'un mois ou d'une année.
## **Exemples**
Nous avons pris pour exemple la situation du `1er avril 2022` dans notre [fichier turtle](https://github.com/UgoMouze/graphe-station/blob/main/src/turtlefiles/finalstate.ttl). Il avait neigé ce jour là, cependant notre modèle ne correspondait pas aux faits qui se sont produits.  
C'est à dire que la classe `Neige` n'est pas présente dans le fichier, ce qui est attendu puisque les précipitations totales sont inférieures à `10mm` cette journée.  
Il est possible de voir toutes les `mesures horaires` de la journée sur le fichier précédent. En effet, on voit que les mesures horaires pointent sur les mesures quotidiennes ainsi que sur l'heure qui leur est associée.  
Enfin, les valeurs sont regroupées pour faire une `mesure quotidienne`.  
Nous avons également décidé de regrouper les valeurs par temps afin de faire des recherches plus facilement et de possiblement implémenter une moyenne sur une zone géographique recherchée.