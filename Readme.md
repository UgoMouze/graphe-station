# Documentation du modèle

Le modèle est disponible sur le fichier suivant [finalstate.ttl](https://github.com/UgoMouze/graphe-station/blob/main/src/turtlefiles/finalstate.ttl)

## **Structure générale**
Nous avons séparé notre modèle en 4 classes comprenant plusieurs sous-classes pouvant être observées sur le graphique suivant :  
![getting image]( https://github.com/UgoMouze/graphe-station/blob/main/src/finalstate.svg "finalstate")


## **Description**
### **Alertes**
***Canicule***  
La canicule est un phénomène météorologique caractérisé par de fortes températures durant 3 jours d'affilé. Néanmoins, nous avons décidé de caractériser ce phénomène par une `température maximale de la journée à 35°C`.  
  
***Inondation***  
Une inondation est une submersion temporaire, naturelle ou artificielle, d'un espace par de l'eau liquide. Nous avons décidé que cela correspond à des précipitations totales de `500mm sur une journée`.  
  
***Neige***  
Nous avond décidé que l'alerte Neige correspond à une `température inférieure à 0°C` et à des `précipitations suppérieures à 10mm sur une journée`.  
  
***Vent***  
La classe vent est une classe servant à classer les phénomènes.  
  
***Vents violents***  
Nous avons considéré que ce phénomène est observé avec des rafales à plus de `100km/h`.  
  
***Visibilité***  
Nous avons noté des problèmes de visibilité avec une visibilité inférieure à `1km` à tout moment de la journée.  
  
***Blizzard***  
Pour le phénomène de Blizzard, nous avons considéré que les rafales de vents soient à plus de `50km/h`, une température inférieure à `0°C` et des précipitations supérieures à `10mm sur une journée`.  
### Date
