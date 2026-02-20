# 🚀 Distributed URL Shortener

Production-ready URL shortening service built using Spring Boot, PostgreSQL, Redis, Docker, and deployed on Render.

---

## 🌍 Live Demo

🔗 API Base URL:
https://your-render-url.onrender.com

Test Endpoint:
https://your-render-url.onrender.com/api/test

---

## 🏗 Architecture

- Java 21
- Spring Boot
- PostgreSQL (Cloud DB)
- Redis (Caching Layer)
- Dockerized Application
- Prometheus + Actuator Monitoring
- Load Tested using JMeter

---

## 📌 Features

- Short URL generation
- Redirection service
- Click count tracking
- Redis caching for faster lookups
- Production deployment
- Cloud database integration
- Monitoring endpoints

---

## 🧪 Example API Usage

### Create Short URL

POST /api/shorten

Body:
https://google.com

### Redirect

GET /{shortCode}

---

## 📊 Monitoring

Actuator Health:
https://your-render-url.onrender.com/actuator/health

Prometheus Metrics:
https://your-render-url.onrender.com/actuator/prometheus

---

## 🐳 Dockerized

Built using multi-stage Docker build for production optimization.

---

## 📈 Performance

Load tested with JMeter:
- Concurrent users tested
- Stable under load

## Architectural Flow

<img width="1043" height="466" alt="image" src="https://github.com/user-attachments/assets/dad2dec7-f7f4-4b82-a5bd-2bc1bc0d0da7" />
