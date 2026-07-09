# Avtodiva Advanced (Full-Stack ERP)

---

## 💡Overview

This project represents a complete architectural transition from a legacy [Java Swing desktop client](https://github.com/AndreyPolezhaiev/avtodiva) to a modern web ecosystem. It is designed to handle school management operations with a focus on security, scalability, and maintainability.

---

## 🛠 Tech Stack

* **Backend:** Java 21, Spring Boot, Spring Security (JWT authentication).
* **Frontend:** Angular, TypeScript, HTML5, SCSS.
* **Database:** PostgreSQL (Neon), Liquibase.
* **Infrastructure:** Docker, Nginx (Reverse Proxy & SSL termination), Linux (Hetzner).

---

## 🏗 Architecture Highlights

* **Full-Stack Integration:** RESTful API architecture connecting the Angular frontend with a robust Spring Boot backend.
* **Secure Deployment:** Automated production environment setup using Docker and Nginx, ensuring secure traffic routing.
* **Data Integrity:** Versioned database management via Liquibase to handle schema evolution seamlessly.

---

## 📂 Project Structure

* `src/main/java/...`: Backend business logic and REST controllers.
* `src/main/resources/...`: Configuration and migration files.
* `docker-compose.yml`: Infrastructure orchestration.

---

## 🚀 How to Run
1. Clone the repository: `git clone https://github.com/AndreyPolezhaiev/avtodiva-advanced`
2. Configure your `.env` file.
3. Run with Docker: `docker-compose up --build`

---

## 🔗 Related Projects

* **Frontend Repository:** [AndreyPolezhaiev/avtodiva-frontend](https://github.com/AndreyPolezhaiev/avtodiva-frontend)

---

Developed and maintained by **Andrii Polezhaiev**.