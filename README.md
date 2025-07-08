
# 💰 Expense Tracker Backend (Microservices Architecture)

This is a **production-ready, microservices-based expense tracking backend system** designed for scale, security, and AI-powered automation. The backend uses **Kong API Gateway** with a custom **Lua plugin** for authentication, **Spring Boot microservices** for business logic, **Kafka** for asynchronous messaging, and **LangChain + Mistral AI** for intelligent bank SMS parsing.

---

## 🚀 Features

- 🛡️ **Kong API Gateway** with JWT authentication via custom Lua plugin  
- 🔐 Secure **login**, **signup**, and **refresh token** flow using **JWT**  
- 📬 **Message extraction service** (Flask + LangChain) parses SMS to auto-add expenses  
- 📊 **Expense service** to add, update, and view expenses  
- 👥 **User service** for user and admin management  
- 🧵 **Kafka** integration for decoupled async communication  
- 🐳 Fully containerized using **Docker & Docker Compose**  
- 📱 Frontend in progress using **React Native + Expo**  

---

## 🧱 Architecture Overview
                      ┌─────────────┐             ┌────────────┐
                      │   Frontend  │  ───────▶   │ Kong API  │
                      └─────────────┘             └──────┬─────┘
                                                         │
                             ┌────────────┬──────────────┼──────────────────────┐
                             ▼            ▼              ▼                      ▼
                        AuthService   UserService   ExpenseService   MessageExtractionService
                             │              │              │                    │
                             └──────────────┴──────────────┴────────────────────┘
                                          (All internal services Dockerized)
                                          
                                    Kafka (for async messaging between services)


---

## 🧪 Microservices Overview

| Service                     | Description                                              | Stack                     |
|-----------------------------|----------------------------------------------------------|---------------------------|
| `authservice`              | Handles login, signup, JWT refresh, `/ping` auth check  | Spring Boot               |
| `userservice`              | Manages users and admins                                 | Spring Boot               |
| `expenseservice`           | Add, update, fetch expenses                              | Spring Boot               |
| `messageextractingservice` | Extracts amount, merchant, currency from SMS using LLM   | Flask + LangChain         |
| `kong`                     | Routes + authenticates requests using custom Lua plugin  | Kong API Gateway + Lua    |
| `kafka`                    | Transfers parsed message to `expenseservice`            | Apache Kafka              |

---

## 🔐 Kong Gateway Auth Flow

1. **All routes go through Kong**  
2. Kong **intercepts every request**  
3. Routes to `/auth/ping` in `authservice`  
4. If JWT is **valid**, `user_id` is extracted and injected into the header  
5. Request is forwarded to the original service  
6. **Login, Signup, Refresh** endpoints are **exempt** from auth  

---

## 📤 AI-Driven Expense Automation

- **Message Extraction Service**:
  - Receives bank SMS (via mobile frontend or manually)
  - Uses **LangChain** + Gemini/Mistral AI to parse the message
  - Extracts: `amount`, `currency`, `merchant`
  - Publishes to Kafka topic `expense_service`

- **Expense Service**:
  - Consumes Kafka message
  - Adds the extracted data as a new expense

**Example Output:**
```json
{
  "amount": "300",
  "currency": "INR",
  "merchant": "Amazon",
}

```

## 🛠️ Setup & Run

### 1️⃣ Clone the Repo
```
git clone https://github.com/yourusername/expense-tracker-backend.git
cd expense-tracker-backend
```

### 2️⃣ Set Up Environment

Create `.env` files for:

- `authservice`, `userservice`, `expenseservice`: Spring Boot config  
- `messageextractingservice`: LLM API key  

**.env (example for messageextractingservice):**
```env
MISTRALAI_API_KEY=your_mistralai_key
```

### 3️⃣ Start All Containers

```
docker-compose up --build
```

### 4️⃣ Ports

| Service                   | Port  |
|---------------------------|-------|
| Kong Proxy (external)     | 8000  |
| Kong Admin API            | 8001  |
| Auth Service              | 8080  |
| User Service              | 8081  |
| Expense Service           | 8082  |
| Message Extraction Service| 8010  |
| Kafka                     | 9092  |

---

## ⚙️ Kong Plugin (Lua)

**Custom plugin:**

- File: `auth-validator/handler.lua`
- Intercepts request
- Calls `/auth/ping`
- Injects `user_id` if successful
- Allows skipping for `/auth/login`, `/auth/signup`, `/auth/refresh`

**Also includes:**

- `schema.lua` for plugin configuration
- Mounted via `kong.yml`

---

## ✅ Tested With

- ✅ Postman for API validation  
- ✅ Kafka topics via console producer/consumer  
- ✅ JWT protected routes and custom plugin behavior  

---

## 📦 Technologies Used

- 🔧 Spring Boot  
- 🧬 Flask  
- 🧠 LangChain, Mistral  
- 🐳 Docker + Compose  
- 🌐 Kong Gateway + Lua Plugin  
- 📩 Kafka  
- 🧪 JWT  
- 📱 React Native (in progress)  

---

## ⏭️ Coming Soon

- ✅ React Native Frontend  
- ✅ Push notifications  
- ✅ Expense analytics dashboard  

