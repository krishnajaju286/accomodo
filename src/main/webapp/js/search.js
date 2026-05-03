// search.js - Filtering and Compare Logic

document.addEventListener('DOMContentLoaded', () => {
    initFilters();
    initCompare();
    simulateSkeletonLoading();
});

function simulateSkeletonLoading() {
    const skeletons = document.querySelectorAll('.skeleton-card');
    const realCards = document.querySelectorAll('.real-card');
    
    if (skeletons.length === 0) return;
    
    // Hide real cards initially
    realCards.forEach(card => card.style.display = 'none');
    
    // Simulate network delay
    setTimeout(() => {
        skeletons.forEach(s => s.style.display = 'none');
        realCards.forEach(card => card.style.display = 'flex');
        applyFilters(); // apply initial filters
    }, 1200);
}

function initFilters() {
    const priceRange = document.querySelector('input[type="range"]');
    
    if (priceRange) {
        priceRange.addEventListener('input', applyFilters);
    }
}

function applyFilters() {
    const priceRange = document.querySelector('input[type="range"]');
    if (!priceRange) return;
    
    const maxPrice = parseInt(priceRange.value, 10);
    const cards = document.querySelectorAll('.real-card');
    let visibleCount = 0;
    
    cards.forEach(card => {
        // Extract price from string
        const priceText = card.querySelector('.card-price').innerText;
        const price = parseInt(priceText.replace(/[^0-9]/g, ''), 10);
        
        let show = true;
        if (price > maxPrice) show = false;
        
        if (show) {
            card.style.display = 'flex';
            visibleCount++;
        } else {
            card.style.display = 'none';
        }
    });
    
    const heading = document.querySelector('.results h2');
    if (heading) {
        heading.innerText = `Found ${visibleCount} Properties`;
    }
    
    // Background tracking for recommendation engine
    if (window.trackSearchHistory) {
        window.trackSearchHistory('Budget < ' + maxPrice);
    }
}

let compareList = [];

function initCompare() {
    window.toggleCompare = function(id, title, price, checkbox, event) {
        event.stopPropagation(); // prevent card click
        
        const isChecked = checkbox.checked;
        const index = compareList.findIndex(item => item.id === id);
        
        if (!isChecked && index > -1) {
            compareList.splice(index, 1);
            showToast('Removed from compare', 'info');
        } else if (isChecked) {
            if (compareList.length >= 3) {
                showToast('Can only compare up to 3 properties', 'error');
                checkbox.checked = false; // revert
                return;
            }
            compareList.push({ id, title, price });
            showToast('Added to compare', 'success');
        }
        
        updateCompareBar();
    };
}

function updateCompareBar() {
    let bar = document.getElementById('compare-bar');
    
    if (!bar) {
        bar = document.createElement('div');
        bar.id = 'compare-bar';
        bar.className = 'compare-bar';
        document.body.appendChild(bar);
    }
    
    if (compareList.length === 0) {
        bar.classList.remove('show');
        return;
    }
    
    bar.innerHTML = `
        <div style="color: white; font-weight: bold;">
            Comparing ${compareList.length} properties
        </div>
        <button class="btn-glass" style="padding: 0.5rem 1rem; border-color: var(--secondary);" onclick="showCompareModal(event)">View Comparison</button>
    `;
    
    bar.classList.add('show');
}

window.showCompareModal = function(event) {
    if(event) event.stopPropagation();
    showToast('Comparison Feature Coming Soon!', 'info');
};
