# Reading Tracker

Application Android personnelle de suivi de lecture, conçue pour fonctionner hors ligne et conserver les données localement.

## Stack technique

- Kotlin, Jetpack Compose et Material 3
- Room (SQLite) pour le stockage local
- Coroutines, Flow et ViewModel

## Fonctionnalités disponibles

- Bibliothèque locale persistante
- Ajout manuel d'un livre (titre, auteur, nombre de pages facultatif)
- Affichage de la progression en pages et en pourcentage
- Avancement d'une page à la fois
- Changement de statut : à lire, en cours, lu, en pause ou abandonné
- Suppression d'un livre
- Accueil avec compteurs des lectures en cours et terminées

## Démarrage

1. Ouvrir le dépôt dans Android Studio.
2. Installer un JDK 17 et le SDK Android 35.
3. Laisser Android Studio synchroniser Gradle.
4. Lancer la configuration `app` sur un émulateur ou appareil Android.

Le niveau minimal pris en charge est Android 8.0 (API 26).

## Prochaines étapes

- Saisie directe de la page courante et édition des informations d'un livre
- Modèle de lecture distinct du livre pour gérer les relectures et les dates de fin
- Carnet de notes et annotations liées aux pages
- Objectifs annuels et statistiques
- Recherche de livres via BnF, Open Library et Google Books
- Export et import des données

Le projet ne dispose pas encore de Gradle Wrapper ni de pipeline CI ; la compilation doit être vérifiée dans Android Studio.
