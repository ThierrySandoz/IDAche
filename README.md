# IDAche

- [ ] Application
  - [ ] General
    - [x] Navigation entre les activités
    - [x] Implémentations des toolbars
    - [ ] Implémentation de notifications (?)
    - [x] Gestion de la langue francais & anglais
  - [x] Splash Screen
  - [x] Page de profile
    - [x] Stockage des informations de l'utilisateur (date de naissance, genre, taille, poids) dans les shared preferences
    - [x] Vérification des champs entrés par l'utilisateur dans la création de son profil
  - [ ] Page principale
    - [x] Création de la vue permettant de visualiser les différentes données provenant des capteurs
    - [x] Création de la vue permettant de visualiser les données météorologiques
    - [x] Création de la vue permettant de visualiser les données des activités sportives
    - [x] Création de la vue permettant de visualiser les données de sommeil
    - [ ] Affichage des valeurs obtenues depuis la montre sur le téléphone (rythme cardiaque + acceleromètre) => implémenté, mais pas pu tester
    - [x] Affichage des données météorologiques en fonction de la position de l'utilisateur
    - [ ] Affichage des données relatives au sommeil de l'utilisateur
    - [ ] Affichage des données relatives à l'activité physique de l'utilisateur
  - [ ] Page d'historique
    - [x] Création du fragment contenant le slider permettant de naviguer dans l'historique
    - [ ] Création du fragment récapitulatif de la journée sélectionnée  -> Fait, mais 2-3 corrections à faire
    - [x] Implémentation de l'outil de recherche dans l'historique
  - [ ] Page de déclaration de maux de tête
    - [x] Implémentation du fragment permettant de sélectionner les symptômes liés au mal de tête
    - [x] Implémentation du fragment contenant le slider permettant de sélectionner le niveau de mal à la tête
    - [ ] Implémentation du fragment contenant le visualisation
    - [ ] Implémentation des aides aux utilisateurs
  - [ ] Capteurs
  - [x] Obtention des données météorologiques depuis l'API (weatherstack) / openweathermap
  
- [ ] Serveur
  - [ ] Stockage des données
  - [ ] Machine learning

- [ ] Bracelet
  - [ ] Capteurs 
    - [x] Obtention des données du capteur cardiaque
    - [x] Obtention des données de l'accéléromètre
    - [ ] Simulation de récéption du capteur cardiaque

- [ ] Communication
  - [ ] Bracelet - Application
    - [x] Transmission des données obtenues depuis la montre sur le téléphone
  - [ ] Application - Serveur
    - [ ] Transmission/Récupération des données
