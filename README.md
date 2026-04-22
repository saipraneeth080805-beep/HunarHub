# HunarHub - Digital Marketplace for Local Micro-Entrepreneurs

A full-stack web application connecting local micro-entrepreneurs (cobblers, potters, tailors, artisans, small vendors) with customers through a digital marketplace.

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security (JWT Authentication)
- Spring Data JPA (Hibernate)
- PostgreSQL

### Frontend
- React.js 18
- React Router DOM
- Axios
- CSS3

## Features

### Customer Features
- Browse approved entrepreneurs
- View entrepreneur profiles and products
- Place service requests
- Purchase products
- View order and request history
- Leave reviews after order completion

### Entrepreneur Features
- Register and create profile
- Add and manage products
- Accept/reject service requests
- View incoming requests
- Track orders
- View earnings summary
- Manage availability

### Admin Features
- Approve/reject entrepreneur registrations
- Monitor all orders and requests
- View system analytics

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Node.js 16+ and npm

## Setup Instructions

### Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE hunarhub;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Backend Setup

1. Navigate to project root directory
2. Build the project:
```bash
mvn clean install
```

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user

### Customer APIs
- `GET /api/entrepreneurs` - Get all approved entrepreneurs
- `GET /api/entrepreneurs/{id}` - Get entrepreneur details
- `GET /api/products` - Get all available products
- `GET /api/entrepreneurs/{id}/products` - Get products by entrepreneur
- `POST /api/requests` - Create service request
- `POST /api/orders` - Place order
- `GET /api/orders/my` - Get my orders
- `GET /api/requests/my` - Get my service requests
- `POST /api/reviews` - Create review

### Entrepreneur APIs
- `POST /api/entrepreneur/products` - Create product
- `PUT /api/entrepreneur/products/{id}` - Update product
- `DELETE /api/entrepreneur/products/{id}` - Delete product
- `GET /api/entrepreneur/products` - Get my products
- `GET /api/entrepreneur/requests/incoming` - Get incoming requests
- `PUT /api/entrepreneur/requests/{id}/accept` - Accept request
- `PUT /api/entrepreneur/requests/{id}/reject` - Reject request
- `PUT /api/entrepreneur/requests/{id}/complete` - Complete request
- `GET /api/entrepreneur/orders` - Get my orders
- `GET /api/entrepreneur/earnings` - Get earnings summary

### Admin APIs
- `GET /api/admin/entrepreneurs/pending` - Get pending entrepreneurs
- `PUT /api/admin/entrepreneurs/{id}/approve` - Approve entrepreneur
- `PUT /api/admin/entrepreneurs/{id}/reject` - Reject entrepreneur
- `GET /api/admin/orders` - Get all orders
- `GET /api/admin/requests` - Get all requests

## Default Categories

The system supports the following categories:
- Cobbler
- Potter
- Tailor
- Artisan
- Vendor

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- CORS enabled for frontend

## Project Structure

```
HunarHub/
├── src/main/java/com/hunarhub/
│   ├── config/          # Configuration classes
│   ├── controller/      # REST controllers
│   ├── service/         # Business logic
│   ├── repository/      # Data access layer
│   ├── entity/          # JPA entities
│   ├── dto/             # Data transfer objects
│   ├── security/        # Security configuration
│   └── exception/        # Exception handling
├── src/main/resources/
│   └── application.properties
├── frontend/
│   ├── src/
│   │   ├── components/  # React components
│   │   ├── pages/       # Page components
│   │   ├── context/      # React context
│   │   └── services/    # API services
│   └── package.json
└── pom.xml
```

## Usage

1. **Register as Customer**: Create an account and start browsing entrepreneurs
2. **Register as Entrepreneur**: Create an account, fill in your profile, and wait for admin approval
3. **Admin Access**: Approve entrepreneurs and monitor the platform

## Future Enhancements

- Payment gateway integration
- Real-time notifications
- Image upload via Cloudinary
- Advanced search and filtering
- Pagination
- Rating-based sorting
- Email notifications

## License

This project is open source and available for educational purposes.
