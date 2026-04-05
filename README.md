# Student Course Registration System

A robust, Java-based desktop application designed to streamline the academic course enrollment process for universities and colleges. The system provides secure, role-based access for both Administrators and Students and is backed by a MySQL database for persistent data storage.

---

## 🌟 Key Features

### 🧑‍🎓 Student Features
- **Secure Authentication:** Mandatory password update on first login to ensure data privacy.
- **Dynamic Course Browsing:** View currently available courses specifically tailored to your enrolled semester.
- **Easy Enrollment:** Simple interface to view available seats and register for classes.
- **Registration Receipts:** Export your finalized course schedule as a secure, uneditable PDF receipt.
- **Dashboard:** A personalized view displaying all currently enrolled courses.

### 🔐 Administrator Features
- **User Management:** Automatically generate secure credentials (e.g., `Name@RollNo`) for new student accounts.
- **Curriculum Management:** Add, update, or remove courses to ensure the curriculum stays current.
- **System Settings:** Easily oversee and manage the overall database.

---

## 🛠️ Technology Stack
- **Frontend/UI:** Java Swing (Desktop GUI)
- **Backend/Logic:** Java (JDK 8+)
- **Database:** MySQL
- **Database Connector:** JDBC (`mysql-connector-j-9.6.0.jar`)

---

## 🚀 Getting Started

### Prerequisites
Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK):** Version 8 or higher.
- **MySQL Server:** Running locally or remotely.

### Installation & Setup

1. **Clone or Download the Repository:**
   Save the project locally on your machine.

2. **Database Setup:**
   - Open your MySQL client (e.g., MySQL Workbench or Command Line).
   - Execute the SQL script provided in the root folder (`setup.sql`) to create the required schemas and tables.
   - Run the `SetupDB.java` and `PopulateData.java` files if you want to initialize the database with placeholder data.

3. **Check Database Credentials:**
   Ensure your database connection details (Username, Password, Port) match your local MySQL setup. These are usually located in the `util/DBConnection.java` file.

4. **Run the Application:**
   Windows users can simply double-click the `run.bat` file in the root directory. 
   
   Alternatively, run these commands from the terminal:
   ```bash
   # Compile all Java files
   javac -cp ".;lib/lib/mysql-connector-j-9.6.0.jar" model/*.java service/*.java ui/*.java util/*.java

   # Run the Login UI
   java -cp ".;lib/lib/mysql-connector-j-9.6.0.jar" ui.LoginUI
   ```

---

## 💡 Usage

1. Open the application (which starts on the `LoginUI`).
2. Log in with an Administrator account to add new students and available courses.
3. Once students are in the system, they can log in utilizing their auto-generated credentials, update their password, and begin registering for their semester courses.
4. Export the finalized schedule via the built-in HTML-to-PDF receipt generator.
