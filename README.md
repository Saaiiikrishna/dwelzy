# Dwelzy - Smart Logistics Platform

Dwelzy is a comprehensive logistics platform that provides smart item analysis, booking management, and real-time tracking for deliveries. The platform consists of a Spring Boot backend, React admin dashboard, and Android mobile applications.

## 🏗️ Architecture

- **Backend**: Spring Boot with JPA, PostgreSQL/H2, Spring Security + JWT
- **Admin Dashboard**: React + TypeScript + TailwindCSS
- **Mobile Apps**: Android (Kotlin) for customers and drivers
- **Authentication**: JWT-based with role-based access control

## 🚀 Features

### Backend API
- ✅ User authentication and authorization (JWT)
- ✅ Item analysis with AI/ML integration (mock implementation)
- ✅ Booking management and tracking
- ✅ Driver assignment and scheduling
- ✅ Shipment scanning and event tracking
- ✅ Pricing calculation based on volume, distance, and surcharges
- ✅ Role-based access control (USER, DRIVER, ADMIN, HUB_MANAGER)

### Admin Dashboard (In Progress)
- 🔄 Real-time hub overview with WebSocket integration
- 🔄 Order management and detailed views
- 🔄 Driver status monitoring
- 🔄 QR code scanning interface

### Mobile Applications (Planned)
- 📱 Customer app for item scanning and booking
- 🚚 Driver app for delivery management

## 🛠️ Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.6+
- Git

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

3. Access the H2 console at: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:dwelzy_db`
   - Username: `sa`
   - Password: (empty)

4. API endpoints are available at: http://localhost:8080/api/v1/

### Admin Dashboard Setup

1. Navigate to the admin-dashboard directory:
```bash
cd admin-dashboard
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

4. Access the dashboard at: http://localhost:3000

## 📚 API Documentation

### Authentication Endpoints
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/register` - User registration

### Item Analysis
- `POST /api/v1/items/analyze` - Analyze item from image

### Booking Management
- `POST /api/v1/bookings` - Create new booking
- `GET /api/v1/bookings/{id}` - Get booking details

### Shipment Tracking
- `POST /api/v1/scan/process` - Process shipment scan
- `GET /api/v1/scan/booking/{bookingNumber}/events` - Get shipment events

### Test Endpoints
- `GET /api/v1/test/all` - Public access
- `GET /api/v1/test/user` - User access
- `GET /api/v1/test/driver` - Driver access
- `GET /api/v1/test/admin` - Admin access

## 🔐 Security

The application uses JWT-based authentication with the following roles:
- **USER**: Regular customers who can create bookings
- **DRIVER**: Delivery drivers who can scan and update shipments
- **ADMIN**: Full system access
- **HUB_MANAGER**: Hub-specific management permissions

## 🧪 Testing

### Backend Tests
```bash
cd backend
./mvnw test
```

### Frontend Tests
```bash
cd admin-dashboard
npm test
```

## 📦 Deployment

### Docker Deployment
```bash
# Build and run with Docker Compose
docker-compose up --build
```

### Manual Deployment
1. Build the backend JAR:
```bash
cd backend
./mvnw clean package
```

2. Build the frontend:
```bash
cd admin-dashboard
npm run build
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 📞 Support

For support and questions, please contact the development team.
