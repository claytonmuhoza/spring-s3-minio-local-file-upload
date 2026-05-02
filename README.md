# Spring Boot S3 & MinIO File Upload

A containerized Spring Boot microservice to upload files to MinIO/S3, complete with an Angular UI for quick testing.

## Prerequisites
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)

## 📂 Structure
* **`frontend/`** : The Angular user interface.
* **`backend/`** : The Spring Boot REST API.
* **`docker-compose.yml`** : Orchestrates the frontend, backend, and MinIO storage.
* **`.env.example`** : Template for required environment variables.

## Quick Start

**1. Clone the repository**
```bash
git clone [https://github.com/your-username/spring-boot-s3-minio-file-upload.git](https://github.com/your-username/spring-boot-s3-minio-file-upload.git)
cd spring-boot-s3-minio-file-upload
```

**2. Setup environment variables**
Copy the example file to create your own `.env` file:
```bash
cp env-example.env .env
```
*(Optional: Open `.env` to modify the default MinIO credentials or bucket name).*

**3. Run the stack**
```bash
docker-compose --env-file <env-file> up -d --build
```

## Access the App
Once the containers are running, open your browser:
* **UI (Angular):** http://localhost:4200
* **API (Spring Boot):** http://localhost:8080
* **Storage Console (MinIO):** http://localhost:9001