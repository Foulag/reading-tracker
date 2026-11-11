# Reading Tracker

Application Android personnelle de suivi de lecture, conçue pour fonctionner hors ligne et conserver les données localement.

## Stack technique
- Kotlin, Jetpack Compose et Material 3
- Room (SQLite) pour le stockage local
- Coroutines et Flow

## Démarrage
1. Ouvrir le dépôt dans Android Studio.
2. Installer un JDK 17 et le SDK Android 35.
3. Laisser Android Studio synchroniser Gradle.
4. Lancer la configuration `app` sur un émulateur ou appareil Android.

Le niveau minimal pris en charge est Android 8.0 (API 26).

## État
Socle initial : application Compose, navigation principale provisoire et première persistance Room pour les livres. Les écrans métier, le catalogue multi-sources, les annotations, les objectifs et les statistiques seront ajoutés progressivement.
