# RegistreNumérique

**RegistreNumérique** est une application Android moderne développée avec **Jetpack Compose** permettant de gérer un registre de vendeurs. L'application utilise **Supabase** comme backend pour le stockage des données et des images.

## 🚀 Fonctionnalités

- **Gestion des vendeurs** : Ajouter, modifier et supprimer des vendeurs.
- **Recherche en temps réel** : Filtrer les vendeurs par nom ou numéro de table.
- **Stockage d'images** : Téléchargement et affichage des photos des vendeurs via Supabase Storage.
- **Synchronisation en temps réel** : Utilisation de Supabase Realtime pour mettre à jour la liste instantanément.
- **Interface Moderne** : UI construite entièrement avec Jetpack Compose et Material3.

## 🛠️ Stack Technique

- **Langage** : Kotlin
- **UI** : Jetpack Compose
- **Architecture** : MVVM (Model-View-ViewModel)
- **Backend** : Supabase (Postgrest, Storage, Realtime)
- **Networking** : Ktor (Moteur CIO)
- **Injection de dépendances** : Centralisation via Gradle Version Catalog (libs.versions.toml)
- **Chargement d'images** : Coil

## 📦 Installation et Configuration

1. **Cloner le projet** :
   ```bash
   git clone https://github.com/FatumaDivine/RegistreNumerique.git
   ```

2. **Configuration Supabase** :
   - Créez un projet sur [Supabase](https://supabase.com/).
   - Créez une table `sellers` avec les colonnes appropriées (`id`, `name`, `first_name`, `table_number`, `category`, `contact`, `image_url`).
   - Configurez les politiques RLS pour autoriser les accès.
   - Mettez à jour vos identifiants dans `SupabaseConfig.kt`.

3. **Build** :
   - Ouvrez le projet dans Android Studio (Ladybug ou plus récent).
   - Synchronisez le projet avec les fichiers Gradle.
   - Lancez l'application sur un émulateur ou un appareil physique.

## 📄 Licence

Ce projet est sous licence MIT.
