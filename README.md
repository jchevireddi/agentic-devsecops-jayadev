# Agentic DevSecOps Workflow

## Template Repository

**Template:** https://github.com/HHG-HAIL/agentic-devsecops-template

Contains `.github/agents/` folder with the following agents:

---

## Agents

**Business Analyst** (`.github/agents/business_analyst.md`)
- Converts business cases into GitHub issues: personas, journeys, epics, stories, and MVP prioritization

**Solution Architect** (`.github/agents/solution_architect.md`)
- Transforms stories into sequenced technical tasks with dependency mapping

**Backend Microservice Coder** (`.github/agents/microservice_coder.md`)
- Spring Boot microservices with REST APIs, JPA/Hibernate, and 85%+ test coverage

**React UI Agent** (`.github/agents/react_ui.md`)
- React components with Vite/Vitest and 85%+ test coverage

**Test Generator** (`.github/agents/test_generator.md`)
- Auto-generates tests for any language/framework

---

## Workflow

```
Business Analyst → Solution Architect → Backend/Frontend Agents → Test Generator
```

---

## Process

### Step 1: Setup

1. Clone the template repo into your new repo

---

### Step 2: Configure GitHub MCP Server

1. Go to your repo `settings/copilot/coding_agent`
2. Paste the following configuration:

```json
{
  "mcpServers": {
    "github-mcp-server": {
      "type": "http",
      "url": "https://api.githubcopilot.com/mcp/",
      "tools": ["*"]
    }
  }
}
```

---

### Step 3: Setup GitHub Token

1. Go to your repo `settings/environments`
2. Create environment named: **copilot**
3. In that environment, create a new secret:
   - **Name:** `COPILOT_MCP_GITHUB_PERSONAL_ACCESS_TOKEN`
   - **Value:** 
     - Go to https://github.com/settings/tokens
     - Select all permissions
     - Copy the generated token
     - Paste it as the secret value

---

### Step 4: Business Analyst Agent

1. Go to https://github.com/copilot/agents
2. Select your new repo
3. Select main branch
4. Assign Business Analyst agent
5. Provide business requirements prompt

**Prompt (I provided):**
```
The product is a Field Service Management System designed to help dispatchers and field technicians efficiently manage service tasks. The system allows a dispatcher to create new service tasks that include a title, description, client address, priority, and estimated duration. Dispatchers can assign tasks to technicians either through a map-based interface or by other assignment methods. The map view displays both technician locations and unassigned task locations in real time. The dispatcher can also reassign a task to another technician if the originally assigned technician becomes unavailable.

Field technicians use a mobile interface to view their assigned tasks, which display the title, description, address, and priority. The mobile system shows the location of each assigned task on a map, helping technicians navigate to client sites. Technicians can update the status of their assigned tasks, marking them as "In Progress" or "Completed." These updates are reflected in near real time on a central dashboard accessible to dispatchers. When marking a task as "Completed," technicians can enter a summary of the work performed.

The central dashboard displays key performance metrics such as total tasks, completed versus open tasks, average completion time, and task priority distribution. A more advanced analytics and reporting dashboard allows dispatch supervisors to monitor the overall operational status using a map-based view. It supports filtering, sorting, and aggregation of data by task type, geography, technician, and time period.

The system also manages real-time notifications and alerts. When a dispatcher assigns a task to a technician, the system automatically sends an alert to the requesting customer, informing them of the assigned technician's name and the estimated arrival time (ETA). Simultaneously, an alert is sent to the assigned technician. When a technician marks a task as "In Progress," the system alerts the customer with an updated ETA. The ETA can either be manually entered by the technician or automatically calculated based on the technician's current location, the task location, and real-time traffic conditions.

The overall solution should support both mobile and web-based interfaces to ensure that dispatchers, supervisors, and technicians can access the system seamlessly from different devices.

Based on this description, generate detailed personas (dispatcher, technician, supervisor, customer), user journeys, epics, and prioritized user stories to cover all key workflows and system interactions described above.
```

**Output:** 
- Generates epics, personas, stories, MVP
- Creates a pull request
- Creates new branch named `copilot/{branch-name}`

---

### Step 5: Solution Architect Agent

1. Go back to agents panel
2. Same repo and branch
3. Assign Solution Architect agent
4. **Prompt:** "Look at the MVP generated from Business Analyst agent and create actionable tasks"

**Output:**
- Generates actionable tasks
- Creates another MVP Roadmap with phases on what to run first
- Creates a pull request
- Creates new branch named `copilot/{branch-name}`

---

### Step 6: Implementation Agents

1. Go to the Agents panel again
2. Select the same repository and branch (usually the main branch)
3. Based on the MVP roadmap generated by the Solution Architect agent, follow the task/issue order it provides. For each issue, assign the correct agent:
   - **Microservice Coder agent** → backend / API / database issues
   - **React UI agent** → frontend / UI / UX issues
4. After identifying the correct agent for the task, go to the Agents panel again, select:
   - your repository
   - the branch (main)
   - the agent you want to run
5. In the prompt, give a direct instruction like:

```
Work on issue #45
```
---

### Step 7: Code Review

1. Go to the pull request page after agent completes its task in the previous step
2. Assign Copilot for review

<img width="1887" height="906" alt="image" src="https://github.com/user-attachments/assets/36f86d7c-6440-4292-b91c-34b94b65696b" />

---

### Step 8: Implementation Fixes

1. Based on review feedback
2. In PR page, click the **"Implementation"** button

<img width="1905" height="903" alt="image" src="https://github.com/user-attachments/assets/232fdb3b-7ddd-45f8-80c1-171077f17641" />

3. Agent runs again and makes changes

---

### Step 7: Merge

1. Merge PR to main

---

### Step 8: Repeat

1. Repeat Steps 6-8 for all other issues in the Roadmap generated by the Solution Architect agent

---

### Step 9: Test Generator Agent

1. After backend and frontend agents finish working on all their issues
2. Go to agents panel
3. Assign Test Generator agent
4. Agent generates additional tests for the implemented code

---

## How to Run the Backend Project

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Database Configuration

The application uses H2 in-memory database by default. The configuration is as follows:
- **Database URL**: `jdbc:h2:mem:taskdb`
- **Username**: `sa`
- **Password**: (empty)
- **H2 Console**: Available at `http://localhost:8080/h2-console` when the application is running

The database schema is automatically created on application startup using Hibernate's `create-drop` strategy.

### Building the Project

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Build the project with Maven:
   ```bash
   mvn clean package
   ```

### Running Tests

Run all tests with code coverage:
```bash
mvn clean test
```

View the code coverage report:
```bash
open target/site/jacoco/index.html
```

The project maintains 84%+ test coverage across all layers.

### Running the Application

1. Start the application using Maven:
   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file directly:
   ```bash
   java -jar target/task-manager-0.0.1-SNAPSHOT.jar
   ```

2. The application will start on `http://localhost:8080`

3. You should see output similar to:
   ```
   Started TaskManagerApplication in X.XXX seconds
   ```

### Testing the API

#### Create a task using POST endpoint:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Fix HVAC System",
    "description": "Client reported AC not cooling properly",
    "address": "123 Main St, Springfield",
    "priority": "HIGH",
    "duration": 120
  }'
```

Expected response (with a generated UUID):
```json
{
  "id": "0f7af77b-0407-458c-9a37-28457fe64379",
  "title": "Fix HVAC System",
  "description": "Client reported AC not cooling properly",
  "address": "123 Main St, Springfield",
  "priority": "HIGH",
  "duration": 120
}
```

#### Retrieve all tasks using GET endpoint:
```bash
curl -X GET http://localhost:8080/api/tasks
```

Expected response when tasks exist:
```json
[
  {
    "id": "0f7af77b-0407-458c-9a37-28457fe64379",
    "title": "Fix HVAC System",
    "description": "Client reported AC not cooling properly",
    "address": "123 Main St, Springfield",
    "priority": "HIGH",
    "duration": 120
  },
  {
    "id": "bd2829f8-ca49-46c3-9d47-b23c4f616925",
    "title": "Install Water Heater",
    "description": "New water heater installation",
    "address": "456 Oak Ave, Metropolis",
    "priority": "MEDIUM",
    "duration": 180
  }
]
```

Expected response when no tasks exist:
```json
[]
```

### Available Endpoints

- **POST /api/tasks** - Create a new task
  - Accepts: JSON body with task fields (title, description, address, priority, duration)
  - Returns: Task object with generated UUID
  - The task is persisted to the H2 database
  - **Validation Rules:**
    - `title` - Required, cannot be blank
    - `address` - Required, cannot be blank
    - `priority` - Required, cannot be blank
    - `description` - Optional
    - `duration` - Optional
  - **Error Handling:** Returns HTTP 400 with field-specific error messages when validation fails

#### Validation Examples

Test with missing required fields:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{}'
```

Response (HTTP 400):
```json
{
  "title": "Title is required",
  "address": "Address is required",
  "priority": "Priority is required"
}
```

Test with blank title:
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "   ", "address": "123 Main St", "priority": "HIGH"}'
```

Response (HTTP 400):
```json
{
  "title": "Title is required"
}
```

- **GET /api/tasks** - Retrieve all tasks
  - Returns: JSON array of all tasks
  - Returns empty array `[]` when no tasks exist
  - Returns list of task objects when tasks exist

### Accessing the H2 Database Console

While the application is running, you can access the H2 database console:

1. Navigate to `http://localhost:8080/h2-console`
2. Use the following connection details:
   - **JDBC URL**: `jdbc:h2:mem:taskdb`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click "Connect" to browse the database

### Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/fieldservice/taskmanager/
│   │   │   ├── controller/     # REST API endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── repository/     # Data access layer
│   │   │   ├── entity/         # JPA entities
│   │   │   └── dto/            # Data transfer objects
│   │   └── resources/
│   │       └── application.properties  # Configuration
│   └── test/                   # Unit and integration tests
├── pom.xml                     # Maven configuration
└── target/                     # Build output
```

### Technology Stack

- **Spring Boot 3.2.0** - Application framework
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **Hibernate** - ORM
- **Lombok** - Boilerplate code reduction
- **JUnit 5** - Testing framework
- **Mockito** - Mocking framework
- **JaCoCo** - Code coverage

---

## How to Run the Frontend Project

### Prerequisites

- Node.js 18 or higher
- npm 9 or higher
- Backend server running on port 8080 (see above)

### Installing Dependencies

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

### Running Tests

Run all tests:
```bash
npm test
```

Run tests with coverage:
```bash
npm run test:coverage
```

The project maintains 100% test coverage on components.

### Running the Application

1. Ensure the backend is running on `http://localhost:8080` (see backend instructions above)

2. Start the development server:
   ```bash
   npm run dev
   ```

3. The application will start on `http://localhost:5173`

4. You should see output similar to:
   ```
   VITE v7.2.2  ready in 201 ms
   ➜  Local:   http://localhost:5173/
   ```

5. Open your browser and navigate to `http://localhost:5173`

### Using the Task Form

1. Fill in the required fields:
   - **Title** (required): Enter a task title
   - **Description** (optional): Enter task details
   - **Address** (required): Enter the service address
   - **Priority** (required): Select LOW, MEDIUM, or HIGH
   - **Estimated Duration** (optional): Enter duration in minutes

2. Click "Create Task" to submit

3. A success message will appear when the task is created successfully

4. The form will reset, ready for the next task

### Building for Production

Build the application:
```bash
npm run build
```

Preview the production build:
```bash
npm run preview
```

### Project Structure

```
frontend/
├── src/
│   ├── components/          # React components
│   │   ├── TaskForm.jsx     # Task creation form
│   │   ├── TaskForm.css     # Form styles
│   │   └── TaskForm.test.jsx # Component tests
│   ├── test/                # Test setup
│   │   └── setup.js         # Vitest configuration
│   ├── App.jsx              # Main application component
│   ├── App.css              # Application styles
│   └── main.jsx             # Application entry point
├── package.json             # Dependencies and scripts
├── vite.config.js           # Vite configuration
└── coverage/                # Test coverage reports (generated)
```

### Technology Stack

- **React 19.2.0** - UI framework
- **Vite 7.2.2** - Build tool and dev server
- **Vitest 4.0.10** - Testing framework
- **React Testing Library 16.3.0** - Component testing utilities
- **ESLint** - Code linting

