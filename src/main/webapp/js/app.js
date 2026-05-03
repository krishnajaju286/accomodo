// app.js - Global Frontend Logic & Mock Database

document.addEventListener('DOMContentLoaded', () => {
    init3DTilt();
    initToastContainer();
    updateNavigationAuth();
    renderRecommendations();
});

window.propertyCatalog = [
    { id: 1, type: "Hostel", price: 15000, title: "Stanza Living - Bidholi House", gender: "Male", img: "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.2km", tags: "Boys • Food Incl.", chips: ["WiFi", "AC", "Laundry"] },
    { id: 2, type: "PG", price: 8000, title: "Elemento Girls Hostel", gender: "Female", img: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 0.5km", tags: "Girls • Food Incl.", chips: ["WiFi", "Security", "RO Water"] },
    { id: 3, type: "Flat", price: 22000, title: "Sunset View Flats", gender: "Any", img: "https://images.unsplash.com/photo-1502672260266-1c1e524164ed?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 3.5km", tags: "Entire 3BHK", chips: ["Furnished", "Parking", "Balcony"] },
    { id: 4, type: "Hostel", price: 12000, title: "The Hive Hostels - Premium", gender: "Female", img: "https://images.unsplash.com/photo-1595526114101-11b0e3be5384?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 2.0km", tags: "Girls • Bus Svc", chips: ["WiFi", "Library", "Bus"] },
    { id: 5, type: "Hostel", price: 14000, title: "Crystal Castle Boys Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 0.8km", tags: "Boys • Premium", chips: ["WiFi", "Gym", "Security"] },
    { id: 6, type: "Hostel", price: 13500, title: "Harjas Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 1.5km", tags: "Boys • Food Incl.", chips: ["WiFi", "Laundry", "Mess"] },
    { id: 7, type: "Hostel", price: 16000, title: "Belleza Boys Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.0km", tags: "Boys • AC Rooms", chips: ["WiFi", "AC", "Power Backup"] },
    { id: 8, type: "Hostel", price: 12500, title: "Scholars Paradise", gender: "Male", img: "https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 2.2km", tags: "Boys • Quiet Zone", chips: ["Library", "WiFi", "Meals"] },
    { id: 9, type: "Hostel", price: 14500, title: "A Wood Stock Girls Hostel", gender: "Female", img: "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 0.3km", tags: "Girls • Premium", chips: ["Security", "WiFi", "AC"] },
    { id: 10, type: "PG", price: 7500, title: "Green Valley PG", gender: "Male", img: "https://images.unsplash.com/photo-1505691938895-1758d7feb511?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 3.0km", tags: "Boys • Budget", chips: ["WiFi", "Self Cook", "RO Water"] },
    { id: 11, type: "PG", price: 9000, title: "Mountain View Girls PG", gender: "Female", img: "https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 1.2km", tags: "Girls • Safe", chips: ["Security", "WiFi", "Meals"] },
    { id: 12, type: "Flat", price: 18000, title: "Pine Residency 2BHK", gender: "Any", img: "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 4.0km", tags: "Semi-Furnished", chips: ["Parking", "Balcony"] },
    { id: 13, type: "Hostel", price: 11000, title: "Shanti Niwas Boys Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1555963966-b7ae5404b6ed?auto=format&fit=crop&q=80&w=600", loc: "Prem Nagar • 5.0km", tags: "Boys • Bus Svc", chips: ["Bus", "Meals", "WiFi"] },
    { id: 14, type: "PG", price: 6500, title: "Student Hub PG", gender: "Male", img: "https://images.unsplash.com/photo-1598928506311-c55f4eb52bfc?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.8km", tags: "Boys • Basic", chips: ["WiFi", "Shared Bath"] },
    { id: 15, type: "Flat", price: 25000, title: "Luxury Heights 3BHK", gender: "Any", img: "https://images.unsplash.com/photo-1600607686527-6fb886090705?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 2.5km", tags: "Fully Furnished", chips: ["AC", "Gym", "Pool"] },
    { id: 16, type: "PG", price: 10500, title: "Elite Girls Co-living", gender: "Female", img: "https://images.unsplash.com/photo-1524758631624-e2822e304c36?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 0.9km", tags: "Girls • Modern", chips: ["AC", "WiFi", "Housekeeping"] },
    { id: 17, type: "Hostel", price: 17000, title: "Stanza Living - Kandoli", gender: "Female", img: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 0.2km", tags: "Girls • Premium", chips: ["Meals", "Security", "Lounge"] },
    { id: 18, type: "Flat", price: 12000, title: "Cozy 1BHK Flat", gender: "Any", img: "https://images.unsplash.com/photo-1493809842364-78817add7ffb?auto=format&fit=crop&q=80&w=600", loc: "Prem Nagar • 4.5km", tags: "Independent", chips: ["Kitchen", "Balcony"] },
    { id: 19, type: "PG", price: 8500, title: "Sunrise Boys PG", gender: "Male", img: "https://images.unsplash.com/photo-1630699144339-420eefc4c77b?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 2.8km", tags: "Boys • Food Incl", chips: ["Meals", "WiFi"] },
    { id: 20, type: "Hostel", price: 13000, title: "The Hive Hostels - Basic", gender: "Male", img: "https://images.unsplash.com/photo-1518733057094-95b5ee1404c3?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 1.0km", tags: "Boys • Clean", chips: ["WiFi", "Laundry", "Meals"] },
    { id: 21, type: "Flat", price: 20000, title: "Valley View 2BHK", gender: "Any", img: "https://images.unsplash.com/photo-1502005097973-6a7082348e28?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 3.2km", tags: "Furnished", chips: ["Parking", "WiFi"] },
    { id: 22, type: "PG", price: 9500, title: "Comfort Zone Girls PG", gender: "Female", img: "https://images.unsplash.com/photo-1588854337236-6889d631faa8?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 0.8km", tags: "Girls • Safe", chips: ["Security", "Meals", "RO"] },
    { id: 23, type: "Hostel", price: 15500, title: "Oxford Boys Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1592595896616-c37162298647?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.4km", tags: "Boys • AC", chips: ["AC", "WiFi", "Gym"] },
    { id: 24, type: "PG", price: 7000, title: "Simple Stay Boys PG", gender: "Male", img: "https://images.unsplash.com/photo-1542314831-c6a4d14b18c0?auto=format&fit=crop&q=80&w=600", loc: "Prem Nagar • 6.0km", tags: "Boys • Budget", chips: ["Self Cook", "WiFi"] },
    { id: 25, type: "Flat", price: 15000, title: "Studio Apartment", gender: "Any", img: "https://images.unsplash.com/photo-1536376072261-38c75010e6c9?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 1.8km", tags: "Single", chips: ["Kitchen", "AC"] },
    { id: 26, type: "Hostel", price: 14000, title: "Campus View Girls Hostel", gender: "Female", img: "https://images.unsplash.com/photo-1565538810643-b5bdb714032a?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 0.5km", tags: "Girls • Very Close", chips: ["Meals", "Security"] },
    { id: 27, type: "PG", price: 11000, title: "Premium Boys PG", gender: "Male", img: "https://images.unsplash.com/photo-1505843513577-22bb7abd2128?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 2.0km", tags: "Boys • Premium", chips: ["AC", "Meals", "Housekeeping"] },
    { id: 28, type: "Flat", price: 28000, title: "Penthouse 3BHK", gender: "Any", img: "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 5.0km", tags: "Luxury", chips: ["AC", "Pool", "Gym"] },
    { id: 29, type: "Hostel", price: 11500, title: "Evergreen Boys Hostel", gender: "Male", img: "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&q=80&w=600", loc: "Prem Nagar • 5.5km", tags: "Boys • Bus", chips: ["Bus", "Meals"] },
    { id: 30, type: "PG", price: 8000, title: "Rose Girls PG", gender: "Female", img: "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 2.5km", tags: "Girls • Food Incl", chips: ["WiFi", "Meals"] },
    { id: 31, type: "Flat", price: 14000, title: "Modern 1BHK", gender: "Any", img: "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 3.0km", tags: "Furnished", chips: ["Kitchen", "WiFi"] },
    { id: 32, type: "Hostel", price: 16500, title: "Royal Girls Hostel", gender: "Female", img: "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.1km", tags: "Girls • AC", chips: ["AC", "Security", "Meals"] },
    { id: 33, type: "PG", price: 12000, title: "Executive Boys PG", gender: "Male", img: "https://images.unsplash.com/photo-1540518614846-7eded433c457?auto=format&fit=crop&q=80&w=600", loc: "Kandoli • 1.6km", tags: "Boys • Single Room", chips: ["Single", "AC", "Meals"] },
    { id: 34, type: "Flat", price: 19000, title: "Green View 2BHK", gender: "Any", img: "https://images.unsplash.com/photo-1502005097973-6a7082348e28?auto=format&fit=crop&q=80&w=600", loc: "Prem Nagar • 4.0km", tags: "Spacious", chips: ["Parking", "Balcony"] },
    { id: 35, type: "Hostel", price: 13500, title: "UPES Scholars Home", gender: "Male", img: "https://images.unsplash.com/photo-1554995207-c18c203602cb?auto=format&fit=crop&q=80&w=600", loc: "Bidholi • 1.9km", tags: "Boys • Study Focus", chips: ["Library", "WiFi", "Meals"] }
];

// --- MOCK DATABASE LOGIC (LocalStorage) ---

function getDB() {
    let db = localStorage.getItem('accomodo_db');
    if (!db) {
        db = { users: {} };
        localStorage.setItem('accomodo_db', JSON.stringify(db));
    } else {
        db = JSON.parse(db);
    }
    return db;
}

function saveDB(db) {
    localStorage.setItem('accomodo_db', JSON.stringify(db));
}

// User Auth functions
window.mockRegister = function(name, email, pass, gender, prefs, budget, food, sharing) {
    let db = getDB();
    if (db.users[email]) {
        return { success: false, message: 'Email already exists!' };
    }
    // Create user with empty history arrays
    db.users[email] = {
        name, email, pass, gender, prefs, budget, food, sharing,
        recentSearches: [],
        viewedProperties: []
    };
    saveDB(db);
    
    // Auto-login
    sessionStorage.setItem('activeUser', email);
    return { success: true, message: 'Account created successfully!' };
};

window.mockLogin = function(email, pass) {
    let db = getDB();
    if (db.users[email] && db.users[email].pass === pass) {
        sessionStorage.setItem('activeUser', email);
        return { success: true, message: 'Signed in successfully!' };
    }
    return { success: false, message: 'Invalid credentials!' };
};

window.logout = function() {
    sessionStorage.removeItem('activeUser');
    window.location.href = 'index.html';
};

// Tracking functions
window.trackSearchHistory = function(query) {
    const email = sessionStorage.getItem('activeUser');
    if (!email) return; // not logged in
    
    let db = getDB();
    if(db.users[email]) {
        db.users[email].recentSearches.push({ query, time: new Date().toISOString() });
        saveDB(db);
        console.log("Saved search to user profile: " + query);
    }
};

window.trackViewHistory = function(propertyId) {
    const email = sessionStorage.getItem('activeUser');
    if (!email) return;
    
    let db = getDB();
    if(db.users[email]) {
        db.users[email].viewedProperties.push({ propertyId, time: new Date().toISOString() });
        saveDB(db);
        console.log("Saved view to user profile: Property " + propertyId);
    }
};

// --- RECOMMENDATION ENGINE LOGIC ---

function renderRecommendations() {
    const container = document.getElementById('recommendations-container');
    if (!container) return; // Only runs on index.html
    
    const activeUserEmail = sessionStorage.getItem('activeUser');
    if (!activeUserEmail) return; // Not logged in, keep default static HTML
    
    let db = getDB();
    let user = db.users[activeUserEmail];
    
    // Scoring Algorithm
    let scoredProperties = window.propertyCatalog.map(prop => {
        let score = 0;
        
        // 1. Heavy Weight: Gender Check (Absolute dealbreaker if mismatch)
        if (prop.gender !== "Any" && prop.gender !== user.gender && prop.gender !== "Other") {
            score -= 999; 
        } else if (prop.gender === user.gender) {
            score += 50;
        }
        
        // 2. Heavy Weight: Property Type Preference
        if (prop.type === user.prefs) {
            score += 50;
        }
        
        // 3. Medium Weight: Search History (Parse budget)
        if (user.recentSearches && user.recentSearches.length > 0) {
            // Very simple parser: look for numbers in the most recent search
            const lastSearch = user.recentSearches[user.recentSearches.length - 1].query;
            const match = lastSearch.match(/\d+/);
            if (match) {
                const maxBudget = parseInt(match[0]);
                if (prop.price <= maxBudget) score += 30;
                else score -= 10;
            }
        }
        
        // 4. Medium Weight: View History
        if (user.viewedProperties && user.viewedProperties.length > 0) {
            user.viewedProperties.forEach(v => {
                if (v.propertyId == prop.id) score += 20; // Direct interest
                else {
                    // Check if they viewed a property of the same type
                    let viewedProp = window.propertyCatalog.find(p => p.id == v.propertyId);
                    if (viewedProp && viewedProp.type === prop.type) score += 10;
                }
            });
        }
        
        // 5. Specific Requirements Metric
        if (user.budget) {
            if (prop.price <= parseInt(user.budget)) score += 25;
            else score -= 30; // Penalty for being over stated budget
        }
        
        if (user.food) {
            if (user.food === "Self" && prop.tags.toLowerCase().includes("self cook")) score += 20;
            else if (user.food !== "Self" && prop.tags.toLowerCase().includes("food incl")) score += 15;
        }
        
        if (user.sharing === "Single" && prop.type === "Flat") {
            score += 15; // Flats imply more privacy
        }
        
        return { property: prop, score: score };
    });
    
    // Sort descending by score, take top 3 (filter out negatives)
    scoredProperties.sort((a, b) => b.score - a.score);
    let topProps = scoredProperties.filter(sp => sp.score > -100).slice(0, 3);
    
    if (topProps.length === 0) topProps = window.propertyCatalog.slice(0, 3); // Fallback
    
    // Generate UI
    let html = `
        <h2 class="section-head" style="color: var(--secondary);">✨ Recommended for You, ${user.name.split(' ')[0]}</h2>
        <div class="grid">
    `;
    
    topProps.forEach(sp => {
        let p = sp.property;
        let badgeClass = p.type.toLowerCase();
        html += `
            <div class="card" onclick="location.href='details.html?id=${p.id}'" title="Match Score: ${sp.score}">
                <div class="badge ${badgeClass}">${p.type}</div>
                <img src="${p.img}" alt="${p.title}" class="card-img">
                <div class="card-body">
                    <div class="card-price">₹${p.price.toLocaleString()} / mo</div>
                    <h3 class="card-title">${p.title}</h3>
                    <div class="card-info">
                        <span>${p.loc}</span>
                        <span>${p.tags}</span>
                    </div>
                    <div class="amenity-chips">
                        ${p.chips.map(c => `<span class="chip">${c}</span>`).join('')}
                    </div>
                </div>
            </div>
        `;
    });
    
    html += `</div>`;
    container.innerHTML = html;
    init3DTilt(); // Re-bind hover events for new cards
};

// --- UI LOGIC ---

function updateNavigationAuth() {
    const navContainer = document.getElementById('auth-nav-container');
    if (!navContainer) return;
    
    const activeUserEmail = sessionStorage.getItem('activeUser');
    
    if (activeUserEmail) {
        let db = getDB();
        let user = db.users[activeUserEmail];
        // User is logged in, show settings dropdown
        navContainer.innerHTML = `
            <div class="dropdown">
                <button class="btn-glass" style="border-color: var(--secondary);">⚙️ ${user.name.split(' ')[0]}</button>
                <div class="dropdown-content">
                    <a href="about.html">❓ Help & About</a>
                    <a href="#" onclick="logout(); return false;">🚪 Sign Out</a>
                </div>
            </div>
        `;
    } else {
        // Not logged in
        navContainer.innerHTML = `<button class="btn-glass" onclick="location.href='login.html'">Sign In</button>`;
    }
}

// 1. 3D Card Tilt Micro-Interactions
function init3DTilt() {
    const cards = document.querySelectorAll('.card, .list-card');
    cards.forEach(card => {
        card.addEventListener('mousemove', (e) => {
            const rect = card.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            const centerX = rect.width / 2;
            const centerY = rect.height / 2;
            const rotateX = ((y - centerY) / centerY) * -10;
            const rotateY = ((x - centerX) / centerX) * 10;
            card.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg) scale3d(1.02, 1.02, 1.02)`;
            card.style.borderColor = 'rgba(255, 255, 255, 0.3)';
        });
        card.addEventListener('mouseleave', () => {
            card.style.transform = `perspective(1000px) rotateX(0deg) rotateY(0deg) scale3d(1, 1, 1)`;
            card.style.borderColor = '';
        });
    });
}

// 2. Custom Toast Notification System
function initToastContainer() {
    if (!document.getElementById('toast-container')) {
        const container = document.createElement('div');
        container.id = 'toast-container';
        container.className = 'toast-container';
        document.body.appendChild(container);
    }
}

window.showToast = function(message, type = 'info') {
    const container = document.getElementById('toast-container');
    if (!container) return;
    
    const toast = document.createElement('div');
    toast.className = 'toast';
    
    let icon = 'ℹ️';
    if (type === 'success') icon = '✅';
    if (type === 'error') icon = '❌';
    if (type === 'heart') icon = '❤️';
    
    toast.innerHTML = `<span>${icon}</span> <span style="margin-left: 8px;">${message}</span>`;
    container.appendChild(toast);
    
    setTimeout(() => {
        toast.classList.add('fade-out');
        setTimeout(() => {
            if (toast.parentNode === container) {
                container.removeChild(toast);
            }
        }, 500);
    }, 3000);
};
