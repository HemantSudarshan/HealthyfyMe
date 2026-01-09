# 🏥 HealthyfyMe

<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android"/>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/Room-4285F4?style=for-the-badge&logo=android&logoColor=white" alt="Room Database"/>
  <img src="https://img.shields.io/badge/Material%20Design-757575?style=for-the-badge&logo=material-design&logoColor=white" alt="Material Design"/>
</p>

<p align="center">
  <b>A comprehensive healthcare Android application for medicine purchases, lab test bookings, and doctor appointments</b>
</p>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Security](#-security)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

**HealthyfyMe** is a modern Android healthcare application that provides users with a one-stop solution for their medical needs. The app enables users to:

- 💊 **Purchase medicines** with doorstep delivery
- 🧪 **Book lab tests** with home sample collection
- 👨‍⚕️ **Find doctors** and schedule appointments
- 📰 **Read health articles** to stay informed

Built with modern Android development practices including **Room Database**, **Repository Pattern**, and **ViewBinding** for a robust and maintainable codebase.

---

## ✨ Features

### 🔐 User Management
- Secure user registration with password validation
- SHA-256 password hashing for secure storage
- Session management with SharedPreferences

### 💊 Medicine Store
- Browse and purchase medicines
- Add items to cart
- Schedule delivery date
- Order tracking and history

### 🧪 Lab Tests
- Browse available lab tests
- Book tests with home sample collection
- Select date and time slots
- View booking history

### 👨‍⚕️ Find Doctors
- Search for doctors by specialty
- View doctor profiles
- Book appointments

### 📦 Order Management
- View all orders in one place
- Track order status
- Order history with delivery details

### 📰 Health Articles
- Read curated health articles
- Stay informed about health topics

---

## 📱 Screenshots

| Login | Home | Buy Medicine |
|:-----:|:----:|:------------:|
| Login Screen | Dashboard | Medicine Store |

| Lab Tests | Cart | Order Details |
|:---------:|:----:|:-------------:|
| Lab Test Booking | Shopping Cart | Order History |

---

## 🛠 Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java** | Primary programming language |
| **Android SDK 32** | Target platform |
| **Room Database** | Local data persistence with SQLite abstraction |
| **LiveData** | Observable data holder for lifecycle-aware components |
| **ViewBinding** | Type-safe view access |
| **Material Design** | Modern UI components |
| **SharedPreferences** | Session management |

### Dependencies

```gradle
// AndroidX
implementation 'androidx.appcompat:appcompat:1.5.1'
implementation 'com.google.android.material:material:1.7.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// Room Database
implementation "androidx.room:room-runtime:2.5.0"
annotationProcessor "androidx.room:room-compiler:2.5.0"

// Lifecycle
implementation "androidx.lifecycle:lifecycle-livedata:2.5.1"
implementation "androidx.lifecycle:lifecycle-viewmodel:2.5.1"
```

---

## 🏗 Architecture

The application follows **Clean Architecture** principles with a clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                      UI Layer                                │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐│
│  │ Activities  │ │  Adapters   │ │      ViewBinding        ││
│  └──────┬──────┘ └─────────────┘ └─────────────────────────┘│
│         │                                                    │
├─────────┼────────────────────────────────────────────────────┤
│         ▼           Repository Layer                         │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              HealthifyRepository                        │ │
│  │  • Async operations with Executor                       │ │
│  │  • Callback-based results                               │ │
│  │  • Password hashing                                     │ │
│  └──────────────────────┬─────────────────────────────────┘ │
│                         │                                    │
├─────────────────────────┼────────────────────────────────────┤
│                         ▼         Data Layer                 │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐│
│  │ AppDatabase │ │ HealthifyDao│ │    Entity Classes       ││
│  │   (Room)    │ │   (DAO)     │ │ User, CartItem, Order   ││
│  └─────────────┘ └─────────────┘ └─────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
```

### Key Architectural Decisions

| Decision | Rationale |
|----------|-----------|
| **Repository Pattern** | Abstracts data sources, enables easy testing |
| **Background Threading** | Prevents UI freezing, improves responsiveness |
| **ViewBinding** | Type-safe view access, eliminates `findViewById` |
| **Callbacks** | Async operation results without blocking UI |

---

## 📁 Project Structure

```
HealthyfyMe/
├── src/main/java/com/labs/healthify/
│   │
│   ├── 📂 dao/
│   │   └── HealthifyDao.java         # Data Access Object
│   │
│   ├── 📂 db/
│   │   └── AppDatabase.java          # Room Database singleton
│   │
│   ├── 📂 models/
│   │   ├── User.java                 # User entity
│   │   ├── CartItem.java             # Cart entity
│   │   └── Order.java                # Order entity
│   │
│   ├── 📂 repository/
│   │   └── HealthifyRepository.java  # Data repository
│   │
│   ├── 📄 Activities
│   │   ├── LoginActivity.java
│   │   ├── RegisterActivity.java
│   │   ├── HomeActivity.java
│   │   ├── BuyMedActivity.java
│   │   ├── CartBuyMedActivity.java
│   │   ├── LabTestActivity.java
│   │   ├── CartLabActivity.java
│   │   ├── OrderDetailsActivity.java
│   │   └── ...more activities
│   │
│   └── Database.java                 # Legacy database helper
│
├── src/main/res/
│   ├── layout/                       # XML layouts
│   ├── values/
│   │   ├── strings.xml               # String resources
│   │   ├── colors.xml                # Color definitions
│   │   └── themes.xml                # App themes
│   └── drawable/                     # Image resources
│
└── build.gradle                      # Dependencies & config
```

---

## 🚀 Getting Started

### Prerequisites

- **Android Studio** Arctic Fox or later
- **JDK 8** or higher
- **Android SDK 23** (minimum) to **SDK 32** (target)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/HemantSudarshan/HealthyfyMe.git
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the HealthyfyMe folder
   ```

3. **Sync Gradle**
   ```
   Android Studio will automatically sync dependencies
   ```

4. **Run the app**
   ```
   Click the Run button or press Shift + F10
   ```

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test
```

---

## 🔒 Security

### Password Security

- Passwords are **hashed using SHA-256** before storage
- Plain text passwords are **never stored** in the database
- Secure comparison using hashed values

```java
// Password hashing implementation
private String hashPassword(String password) {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
    // Convert to hex string
}
```

### Data Protection

- All database operations run on **background threads**
- User sessions managed via encrypted SharedPreferences
- Input validation on all user inputs

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Code Style

- Follow **Java naming conventions**
- Use **meaningful variable names**
- Add **comments** for complex logic
- Use **string resources** instead of hardcoded strings

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Hemant Sudarshan**

[![GitHub](https://img.shields.io/badge/GitHub-HemantSudarshan-181717?style=flat-square&logo=github)](https://github.com/HemantSudarshan)

---

<p align="center">
  Made with ❤️ for better healthcare access
</p>

<p align="center">
  ⭐ Star this repo if you find it helpful!
</p>
