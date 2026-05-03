# Accomodo – Complete Backend & Frontend Improvements Explained

**Project:** Accomodo – Living Made Easier  
**Batch:** 1CCVT | UPES Dehradun  
**Team:** Krish Jaju (590015913) · Sparsh Singhania · Aditya Singh Saini

This document is a comprehensive, academic-grade technical explanation of every architectural improvement and new system implemented across both the **Java backend** and the **JavaScript/HTML frontend** of Accomodo. Each concept is explained at a code level, mapping directly to the Advanced Java and software engineering topics from the curriculum.

---

## PART A — JAVA BACKEND IMPROVEMENTS

---

## 1. Custom Exception Handling

**Files:** `DatabaseOperationException.java` · `AccommodationNotFoundException.java`

Instead of generic `Exception` or `RuntimeException` catches, we created **domain-specific custom exceptions** that carry meaningful error context.

### 1.1 Unchecked Exception — `DatabaseOperationException`

```java
public class DatabaseOperationException extends RuntimeException {
    private final String errorCode;
    private final LocalDateTime timestamp;

    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "DB_ERR_" + System.currentTimeMillis();
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() { return errorCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
```

**Why this design:**
- Extends `RuntimeException` → **unchecked**. Database failures are systemic, so we don't force every calling method to declare `throws` in its signature, avoiding noisy boilerplate.
- Wraps the raw `SQLException` as the `cause`, practising **exception chaining** — the database implementation detail is hidden from the Servlet layer (encapsulation).
- Auto-generates a unique `errorCode` (millisecond timestamp suffix) making log correlation trivial during debugging.

### 1.2 Checked Exception — `AccommodationNotFoundException`

```java
public class AccommodationNotFoundException extends Exception {
    private final int requestedId;

    public AccommodationNotFoundException(String message, int requestedId) {
        super(message);
        this.requestedId = requestedId;
    }

    public int getRequestedId() { return requestedId; }
}
```

**Why this design:**
- Extends `Exception` → **checked**. Any method that may throw this (e.g., `getById()`) is forced by the Java compiler to declare it, ensuring all callers handle the "missing property" scenario explicitly, guaranteeing robust application behaviour.
- Carries `requestedId` as a field so log messages can immediately report which property ID triggered the failure.

---

## 2. Multithreading & Concurrency

**Files:** `AsyncLogger.java` · `AccommodationServlet.java`

Multithreading is used to offload non-critical, potentially slow I/O operations (file writes, log flushes) from the main HTTP request-response thread, improving perceived response time.

### 2.1 The Background Task — `AsyncLogger`

```java
public class AsyncLogger implements Runnable {
    private final String message;
    private final String level;

    public AsyncLogger(String message, String level) {
        this.message = message;
        this.level = level;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(500); // simulates disk I/O latency
            String entry = "[" + level + "] " + LocalDateTime.now() + " → " + message;
            Files.write(Paths.get("accomodo.log"), (entry + "\n").getBytes(),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted flag
        } catch (IOException e) {
            System.err.println("Logger failed: " + e.getMessage());
        }
    }
}
```

**Line-by-line:**
- `implements Runnable` — makes the class a blueprint for concurrent work. The `run()` method is the task body.
- `Thread.sleep(500)` — simulates slow disk I/O, proving the main thread is not blocked.
- `Thread.currentThread().interrupt()` — best practice for handling `InterruptedException`: restoring the flag so upstream callers can detect the interruption.

### 2.2 Spawning the Thread — `AccommodationServlet`

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // ... fetch data ...
    String logMsg = "GET /accommodations — " + allAccommodations.size() + " returned";
    Thread loggerThread = new Thread(new AsyncLogger(logMsg, "INFO"));
    loggerThread.start(); // non-blocking — main thread continues immediately
    // ... write JSON response to client ...
}
```

**Key concept:** `.start()` tells the JVM to allocate a new OS-level thread and execute `run()` there. The Servlet thread does not wait — it moves directly to serialising JSON and sending the HTTP response. The logging happens completely in parallel, invisible to the end user.

---

## 3. Persistent vs. Non-Persistent Data & JDBC

**File:** `AccommodationDAO.java`

| Type | Storage | Lifespan | Speed |
|---|---|---|---|
| **Persistent** | MySQL via JDBC | Survives restarts | Slower (disk) |
| **Non-Persistent** | `HashMap` in RAM | Lost on restart | O(1) — instant |

### 3.1 Try-With-Resources (JDBC Enhancement)

```java
try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
     PreparedStatement ps = conn.prepareStatement(SQL);
     ResultSet rs = ps.executeQuery()) {

    while (rs.next()) {
        list.add(mapResultSetToAccommodation(rs));
    }

} catch (SQLException e) {
    throw new DatabaseOperationException("Query failed", e);
}
```

**Why this matters:**
- Resources declared in the `try(...)` parentheses are **automatically closed** when the block exits — whether normally or via exception.
- Before this pattern, failing to call `.close()` caused **connection pool exhaustion** — one of the most common production database bugs.
- The raw `SQLException` is wrapped in our custom `DatabaseOperationException`, preventing JDBC implementation details from leaking into the Servlet layer.

---

## 4. The Collections Framework & Autoboxing/Unboxing

**File:** `AccommodationDAO.java`

We use four distinct Collection types, each chosen for a specific performance or behavioural guarantee.

### 4.1 HashMap — O(1) In-Memory Cache

```java
private final Map<Integer, Accommodation> accommodationCache = new HashMap<>();

// Populating (called once at init):
accommodationCache.put(acc.getPropertyId(), acc); // autoboxing: int → Integer (key)

// Retrieval:
Accommodation a = accommodationCache.get(id); // unboxing: Integer → int internally
```

- **Autoboxing:** `getPropertyId()` returns `int`. `Map` requires Object keys (`Integer`). Java silently boxes it.
- **Unboxing:** When you use the key to look up, Java unboxes the `Integer` wrapper back to `int` for comparison.
- **Time complexity:** O(1) average — hash lookup, no linear scan.

### 4.2 TreeMap — Automatic Price-Sort

```java
Map<Double, Accommodation> sortedByPrice = new TreeMap<>();
for (Accommodation acc : accommodationCache.values()) {
    sortedByPrice.put(acc.getPrice() + (acc.getPropertyId() * 0.000001), acc);
}
```

- `TreeMap` is backed by a **Red-Black tree**, auto-sorting by natural key order (ascending price here).
- The microscopic `propertyId * 0.000001` offset prevents price-identical properties from overwriting each other's keys.
- Autoboxing converts the primitive `double` to `Double` for the key.

### 4.3 HashSet — Unique Amenity Collection

```java
Set<String> uniqueAmenities = new HashSet<>();
for (Accommodation acc : allAccommodations) {
    for (String amenity : acc.getAmenities()) {
        uniqueAmenities.add(amenity.trim().toLowerCase());
    }
}
```

- `HashSet` rejects duplicate entries at the data-structure level — no `if(!list.contains(...))` guard needed.

### 4.4 TreeSet — Sorted Unique Property Types

```java
Set<String> sortedTypes = new TreeSet<>();
for (Accommodation acc : accommodationCache.values()) {
    sortedTypes.add(acc.getType()); // "Flat", "Hostel", "PG"
}
// Result is always: [Flat, Hostel, PG] — alphabetically sorted, no duplicates
```

- Used to populate filter dropdowns on the frontend — always consistent, always sorted.

---

## 5. Lambda Expressions & the Stream API

**File:** `AccommodationDAO.java`

We replaced traditional imperative `for` loops with the modern, declarative **Stream API + Lambda** approach.

```java
public List<Accommodation> filterProperties(double maxPrice, double maxDistance, String genderPref) {
    return accommodationCache.values().stream()
        .filter(acc -> acc.getPrice() <= maxPrice)
        .filter(acc -> acc.getDistance() <= maxDistance)
        .filter(acc -> {
            if (genderPref == null || "Any".equalsIgnoreCase(genderPref)) return true;
            return acc.getGenderPref().equalsIgnoreCase(genderPref);
        })
        .sorted(Comparator.comparingDouble(Accommodation::getPrice))
        .collect(Collectors.toList());
}
```

**Breakdown:**
| Operation | Concept | What it does |
|---|---|---|
| `.stream()` | Stream API | Converts `Collection` into a lazy data pipeline |
| `.filter(acc -> ...)` | Lambda / Predicate | Keeps only items where the condition is `true` |
| `.sorted(Comparator...)` | Method Reference | Sorts results by price ascending |
| `.collect(Collectors.toList())` | Terminal Op | Materialises the pipeline into a concrete `List` |

The **lambda arrow** `->` replaces a full anonymous class: `acc -> acc.getPrice() <= maxPrice` is equivalent to `new Predicate<Accommodation>() { public boolean test(Accommodation acc) { return acc.getPrice() <= maxPrice; } }` — but infinitely more readable.

---

## PART B — FRONTEND SYSTEM IMPROVEMENTS (JavaScript)

---

## 6. Mock Database — Browser `localStorage` Persistence

**File:** `js/app.js`

Since the live backend requires a running Tomcat server, we implemented a fully functional **client-side mock database** using the browser's `localStorage` API. This is architecturally identical to a key-value store (like Redis).

```javascript
// The "schema" — a JSON object stored under one key
function getDB() {
    let raw = localStorage.getItem('accomodo_db');
    if (!raw) {
        let emptyDB = { users: {} };
        localStorage.setItem('accomodo_db', JSON.stringify(emptyDB));
        return emptyDB;
    }
    return JSON.parse(raw); // deserialise JSON → JS Object
}

function saveDB(db) {
    localStorage.setItem('accomodo_db', JSON.stringify(db)); // serialise JS Object → JSON
}
```

**Properties of this approach:**
- **Persistent:** `localStorage` survives page refreshes, browser tab closes, and even browser restarts.
- **Schema:** The root object has a `users` map, keyed by email — equivalent to a SQL `PRIMARY KEY`.
- **Serialisation:** `JSON.stringify / JSON.parse` acts as our ORM, converting between runtime objects and the stored string format.

### 6.1 User Registration

```javascript
window.mockRegister = function(name, email, pass, gender, prefs, budget, food, sharing) {
    let db = getDB();
    if (db.users[email]) {
        return { success: false, message: 'Email already exists!' };
    }
    db.users[email] = {
        name, email, pass, gender, prefs, budget, food, sharing,
        recentSearches:    [],  // activity log
        viewedProperties:  []   // view history
    };
    saveDB(db);
    sessionStorage.setItem('activeUser', email); // auto-login
    return { success: true, message: 'Account created!' };
};
```

**Design decisions:**
- Duplicate email check mirrors a `UNIQUE` SQL constraint.
- `recentSearches` and `viewedProperties` are pre-initialised as empty arrays — equivalent to creating child table rows at user creation time.
- **`sessionStorage`** is used for the active session: unlike `localStorage`, it is cleared when the browser tab is closed, making it the correct storage tier for session tokens.

---

## 7. Session Management

**File:** `js/app.js`

```javascript
window.mockLogin = function(email, pass) {
    let db = getDB();
    if (db.users[email] && db.users[email].pass === pass) {
        sessionStorage.setItem('activeUser', email); // write session token
        return { success: true, message: 'Signed in!' };
    }
    return { success: false, message: 'Invalid credentials!' };
};

window.logout = function() {
    sessionStorage.removeItem('activeUser'); // destroy session token
    window.location.href = 'index.html';
};
```

**Two-tier storage model:**

| Storage | Scope | Lifespan | Purpose |
|---|---|---|---|
| `localStorage` | Origin | Permanent | User profiles, search history, view history |
| `sessionStorage` | Tab | Until tab close | Active login session token (the logged-in email) |

This mirrors the real-world split between a **database** (persistent user data) and a **JWT / session cookie** (temporary authentication token).

---

## 8. Activity Tracking

**File:** `js/app.js` · `js/search.js` · `details.html`

Every logged-in user's actions are silently tracked and appended to their profile — feeding the recommendation engine.

```javascript
// Called in search.js whenever the price slider moves
window.trackSearchHistory = function(query) {
    const email = sessionStorage.getItem('activeUser');
    if (!email) return; // Guard: skip for unauthenticated users
    let db = getDB();
    if (db.users[email]) {
        db.users[email].recentSearches.push({
            query: query,              // e.g., "Budget < 12000"
            time:  new Date().toISOString() // ISO timestamp
        });
        saveDB(db);
    }
};

// Called in details.html on page load
window.trackViewHistory = function(propertyId) {
    const email = sessionStorage.getItem('activeUser');
    if (!email) return;
    let db = getDB();
    if (db.users[email]) {
        db.users[email].viewedProperties.push({
            propertyId: propertyId,
            time:       new Date().toISOString()
        });
        saveDB(db);
    }
};
```

**Non-blocking design:** Both functions execute silently in the background. They never display UI or block any user-facing interaction — identical to how real analytics SDKs (e.g., Mixpanel, Amplitude) work.

---

## 9. The Personalised Recommendation Engine

**File:** `js/app.js` — `renderRecommendations()`

This is the most sophisticated system in the project. It implements a **multi-factor weighted scoring algorithm** that ranks all 35 properties against the active user's profile and surface the Top 3 matches on the homepage.

### 9.1 Scoring Algorithm

```javascript
let scoredProperties = window.propertyCatalog.map(prop => {
    let score = 0;

    // Factor 1 — Gender Compatibility (Absolute Weight)
    if (prop.gender !== "Any" && prop.gender !== user.gender) {
        score -= 999; // Elimination: wrong gender listing
    } else if (prop.gender === user.gender) {
        score += 50;  // Strong match bonus
    }

    // Factor 2 — Property Type Preference (Heavy Weight)
    if (prop.type === user.prefs) {
        score += 50;
    }

    // Factor 3 — Stated Budget at Registration (Heavy Weight)
    if (user.budget) {
        if (prop.price <= parseInt(user.budget)) score += 25;
        else score -= 30; // Over-budget penalty
    }

    // Factor 4 — Search History Budget (Medium Weight)
    if (user.recentSearches && user.recentSearches.length > 0) {
        const lastSearch = user.recentSearches[user.recentSearches.length - 1].query;
        const budgetMatch = lastSearch.match(/\d+/);
        if (budgetMatch) {
            const liveMaxBudget = parseInt(budgetMatch[0]);
            if (prop.price <= liveMaxBudget) score += 30;
            else score -= 10;
        }
    }

    // Factor 5 — View History (Behavioural Signal)
    if (user.viewedProperties && user.viewedProperties.length > 0) {
        user.viewedProperties.forEach(v => {
            if (v.propertyId == prop.id) score += 20; // Direct re-interest
            else {
                let seen = window.propertyCatalog.find(p => p.id == v.propertyId);
                if (seen && seen.type === prop.type) score += 10; // Type affinity
            }
        });
    }

    // Factor 6 — Food & Sharing Preferences (Registration Signals)
    if (user.food === "Self" && prop.tags.toLowerCase().includes("self cook")) score += 20;
    else if (user.food !== "Self" && prop.tags.toLowerCase().includes("food incl")) score += 15;

    if (user.sharing === "Single" && prop.type === "Flat") score += 15;

    return { property: prop, score };
});
```

### 9.2 Scoring Factor Summary

| Factor | Signal Source | Max Score |
|---|---|---|
| Gender match | Registration profile | +50 / -999 |
| Property type preference | Registration profile | +50 |
| Stated max budget (registration) | Registration profile | +25 / -30 |
| Live search budget | Search history | +30 / -10 |
| Re-viewed property | View history | +20 |
| Same-type affinity | View history | +10 |
| Food preference match | Registration profile | +15–20 |
| Single room preference | Registration profile | +15 |

### 9.3 Ranking & Rendering

```javascript
scoredProperties.sort((a, b) => b.score - a.score);       // Descending sort
let topProps = scoredProperties
    .filter(sp => sp.score > -100)   // Eliminate hard mismatches
    .slice(0, 3);                    // Top 3 only

// Inject HTML dynamically into #recommendations-container
container.innerHTML = html; // replaces static default cards
```

The top 3 cards are injected into `index.html` at runtime, replacing the generic "Top Recommendations" with a personalised "✨ Recommended for You, [Name]" section. Hovering over a card shows the debug **Match Score** in a tooltip for transparency.

---

## 10. Dynamic Navigation (Auth-Aware UI)

**File:** `js/app.js` — `updateNavigationAuth()`

The navbar dynamically switches between a **Sign In button** and a **Settings dropdown** depending on authentication state, running on every page load.

```javascript
function updateNavigationAuth() {
    const navContainer = document.getElementById('auth-nav-container');
    const email = sessionStorage.getItem('activeUser');

    if (email) {
        let user = getDB().users[email];
        navContainer.innerHTML = `
            <div class="dropdown">
                <button class="btn-glass">⚙️ ${user.name.split(' ')[0]}</button>
                <div class="dropdown-content">
                    <a href="about.html">❓ Help & About</a>
                    <a href="#" onclick="logout(); return false;">🚪 Sign Out</a>
                </div>
            </div>`;
    } else {
        navContainer.innerHTML = `
            <button class="btn-glass" onclick="location.href='login.html'">Sign In</button>`;
    }
}
```

**Pattern used:** This is the **Single Source of Truth** pattern. The nav state is never hardcoded in HTML — it is always derived from the live session at render time, ensuring consistency across all pages.

---

## 11. Property Catalog (35 Real-World Entries)

**File:** `js/app.js` — `window.propertyCatalog`

The property catalog is a JavaScript array of 35 structured objects, each representing a real or near-real property near UPES Bidholi/Kandoli, sourced from platforms like Stanza Living, The Hive Hostels, and Justdial listings.

```javascript
// Example entry structure
{ 
    id: 1, 
    type: "Hostel",           // "Hostel" | "PG" | "Flat"
    price: 15000,             // Monthly rent in INR
    title: "Stanza Living - Bidholi House",
    gender: "Male",           // "Male" | "Female" | "Any"
    img: "https://images.unsplash.com/...",
    loc: "Bidholi • 1.2km",
    tags: "Boys • Food Incl.",
    chips: ["WiFi", "AC", "Laundry"]
}
```

The catalog is declared on `window` (globally accessible) so it is available to both `app.js` (recommendation engine) and `properties.html` (rendering all 35 cards).

---

## 12. Schema Update (MySQL)

**File:** `schema_update.sql`

The database schema was extended to persist the new registration fields:

```sql
ALTER TABLE Users ADD COLUMN budget VARCHAR(50);
ALTER TABLE Users ADD COLUMN food_pref VARCHAR(50);
ALTER TABLE Users ADD COLUMN sharing_pref VARCHAR(50);
```

These columns correspond directly to the three new `<select>` dropdowns added to `login.html`, enabling future backend migration of the recommendation engine from `localStorage` to a real SQL-backed API.

---

## Summary of All Improvements

| # | Concept | Implementation |
|---|---|---|
| 1 | Custom Exceptions | `DatabaseOperationException`, `AccommodationNotFoundException` |
| 2 | Multithreading | `AsyncLogger` + `Thread.start()` in servlet |
| 3 | JDBC & Try-With-Resources | Auto-closing `Connection`, `PreparedStatement`, `ResultSet` |
| 4 | HashMap | O(1) in-memory accommodation cache |
| 5 | TreeMap | Auto price-sorted accommodation listing |
| 6 | HashSet | Unique amenity deduplication |
| 7 | TreeSet | Sorted unique property type filter options |
| 8 | Autoboxing / Unboxing | `int → Integer` in map keys, `double → Double` in TreeMap |
| 9 | Lambda & Stream API | Declarative `.filter().sorted().collect()` pipeline |
| 10 | Mock Database | `localStorage` JSON store with `getDB()` / `saveDB()` |
| 11 | Session Management | `sessionStorage` for auth token, `localStorage` for user data |
| 12 | Activity Tracking | `trackSearchHistory()`, `trackViewHistory()` |
| 13 | Recommendation Engine | 6-factor weighted scoring algorithm, Top-3 render |
| 14 | Auth-Aware Navigation | `updateNavigationAuth()` — settings dropdown vs sign-in |
| 15 | 35-Property Catalog | Real hostel/PG names from UPES Bidholi/Kandoli area |
| 16 | SQL Schema Extension | `budget`, `food_pref`, `sharing_pref` columns added |

---
*End of Document — Accomodo Backend & Frontend Complete Technical Explanation*
