// ============================================================
//  utils.js — Design System Helpers, Analytics & Academic Math
// ============================================================

// ── Toast Notification System ────────────────────────────────
function showToast(message, type = 'success', duration = 3500) {
  let container = document.getElementById('toastContainer');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toastContainer';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const icons = {
    success: `<svg class="toast-svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" clip-rule="evenodd"/></svg>`,
    error: `<svg class="toast-svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clip-rule="evenodd"/></svg>`,
    info: `<svg class="toast-svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7-4a1 1 0 11-2 0 1 1 0 012 0zM9 9a1 1 0 000 2v3a1 1 0 001 1h1a1 1 0 100-2v-3a1 1 0 00-1-1H9z" clip-rule="evenodd"/></svg>`,
    warning: `<svg class="toast-svg" viewBox="0 0 20 20" fill="currentColor"><path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd"/></svg>`
  };

  const toast = document.createElement('div');
  toast.className = `custom-toast toast-${type}`;
  toast.innerHTML = `
    <div class="toast-icon">${icons[type] || icons.info}</div>
    <div class="toast-content">
      <div class="toast-message">${message}</div>
    </div>
    <button class="toast-close" onclick="this.parentElement.remove()" aria-label="Close">&times;</button>
    <div class="toast-progress"></div>
  `;

  container.appendChild(toast);

  // Trigger entrance animation
  requestAnimationFrame(() => toast.classList.add('toast-visible'));

  const timer = setTimeout(() => {
    toast.classList.remove('toast-visible');
    setTimeout(() => toast.remove(), 300);
  }, duration);

  toast.addEventListener('mouseenter', () => clearTimeout(timer));
}

// ── Form Validation Helper ───────────────────────────────────
function validateRequired(fields) {
  for (const [name, value] of Object.entries(fields)) {
    if (value === undefined || value === null || !value.toString().trim()) {
      showToast(`${name} is required.`, 'error');
      return false;
    }
  }
  return true;
}

// ── Academic Grading & Math Calculations ─────────────────────
function marksGrade(marks) {
  if (marks === null || marks === undefined || marks === '') return 'N/A';
  const m = Number(marks);
  if (m >= 90) return 'A+';
  if (m >= 80) return 'A';
  if (m >= 70) return 'B';
  if (m >= 60) return 'C';
  if (m >= 50) return 'D';
  if (m >= 33) return 'E';
  return 'F';
}

function marksGradePoint(marks) {
  if (marks === null || marks === undefined || marks === '') return null;
  const m = Number(marks);
  if (m >= 90) return 10.0;
  if (m >= 80) return 9.0;
  if (m >= 70) return 8.0;
  if (m >= 60) return 7.0;
  if (m >= 50) return 6.0;
  if (m >= 33) return 4.0;
  return 0.0;
}

function marksColor(marks) {
  if (marks === null || marks === undefined || marks === '') return '#a0b3b6'; // muted
  const m = Number(marks);
  if (m >= 80) return '#b5f3cd'; // Pale Mint
  if (m >= 60) return '#a7e8e1'; // Soft Teal
  if (m >= 50) return '#f0c58a'; // Antique Gold
  if (m >= 33) return '#e3b078'; // Warm Ochre
  return '#fca5a5';              // Soft Rose
}

function marksBadgeClass(marks) {
  if (marks === null || marks === undefined || marks === '') return 'badge-neutral';
  const m = Number(marks);
  if (m >= 80) return 'badge-distinction';
  if (m >= 60) return 'badge-success';
  if (m >= 50) return 'badge-warning';
  if (m >= 33) return 'badge-pass';
  return 'badge-fail';
}

function calculateCGPA(marksArray, scale = 4.0) {
  const graded = marksArray.filter(m => m !== null && m !== undefined && !isNaN(m));
  if (graded.length === 0) return null;
  const total = graded.reduce((a, b) => a + Number(b), 0);
  const avg = total / graded.length;
  if (scale === 10.0) {
    return (avg / 10).toFixed(2);
  }
  // 4.0 Scale: (Avg / 100) * 4
  return ((avg / 100) * 4).toFixed(2);
}

function academicClassification(percentage) {
  if (percentage === null || percentage === undefined) return 'Results Awaited';
  const p = Number(percentage);
  if (p >= 85) return 'First Class with Distinction';
  if (p >= 70) return 'First Class';
  if (p >= 55) return 'Second Class';
  if (p >= 33) return 'Pass Class';
  return 'Unsuccessful';
}

// ── Smooth Animated Numerical Counter ────────────────────────
function animateCounter(elementId, targetVal, duration = 1200, suffix = '', decimals = 0) {
  const el = document.getElementById(elementId);
  if (!el) return;
  if (targetVal === null || targetVal === undefined || isNaN(targetVal)) {
    el.textContent = '—';
    return;
  }

  const startVal = 0;
  const startTime = performance.now();
  const numTarget = Number(targetVal);

  function updateCount(currentTime) {
    const elapsed = currentTime - startTime;
    const progress = Math.min(elapsed / duration, 1);
    // Ease out cubic
    const easeProgress = 1 - Math.pow(1 - progress, 3);
    const current = startVal + (numTarget - startVal) * easeProgress;

    el.textContent = (decimals > 0 ? current.toFixed(decimals) : Math.round(current)) + suffix;

    if (progress < 1) {
      requestAnimationFrame(updateCount);
    } else {
      el.textContent = (decimals > 0 ? numTarget.toFixed(decimals) : Math.round(numTarget)) + suffix;
    }
  }

  requestAnimationFrame(updateCount);
}

// ── Subject Icon Generator ───────────────────────────────────
const SUBJECT_ICON_SVGS = [
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/><path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/></svg>`,
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><path d="m4.93 4.93 4.24 4.24"/><path d="m14.83 9.17 4.24-4.24"/><path d="m14.83 14.83 4.24 4.24"/><path d="m9.17 14.83-4.24 4.24"/></svg>`,
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="16 18 22 12 16 6"/><polyline points="8 6 2 12 8 18"/></svg>`,
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><ellipse cx="12" cy="5" rx="9" ry="3"/><path d="M3 5v14c0 1.66 4 3 9 3s9-1.34 9-3V5"/><path d="M3 12c0 1.66 4 3 9 3s9-1.34 9-3"/></svg>`,
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 2v20"/><path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/></svg>`,
  `<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>`
];

function subjectIcon(index) {
  return SUBJECT_ICON_SVGS[index % SUBJECT_ICON_SVGS.length];
}

function initials(name) {
  if (!name) return '??';
  return name.trim().split(/\s+/).map(w => w[0]).join('').toUpperCase().slice(0, 2);
}

// ── Auth Guard & Session Helpers ─────────────────────────────
function requireAuth(role) {
  const session = Session.get();
  if (!session || session.role !== role) {
    window.location.href = role === 'teacher' ? 'teacher-login.html' : 'student-login.html';
    return null;
  }
  return session.user;
}

function doLogout() {
  Session.clear();
  showToast('Logged out successfully. Redirecting...', 'info');
  setTimeout(() => {
    window.location.href = '../index.html';
  }, 500);
}

// ── Mobile Drawer Navigation Helpers ──────────────────────────
function toggleMobileSidebar() {
  const sidebar = document.querySelector('.app-sidebar');
  const backdrop = document.getElementById('sidebarBackdrop');
  if (!sidebar) return;

  const isOpen = sidebar.classList.contains('mobile-open');
  if (isOpen) {
    closeMobileSidebar();
  } else {
    sidebar.classList.add('mobile-open');
    if (backdrop) backdrop.classList.add('active');
    document.body.style.overflow = 'hidden';
  }
}

function closeMobileSidebar() {
  const sidebar = document.querySelector('.app-sidebar');
  const backdrop = document.getElementById('sidebarBackdrop');
  if (sidebar) sidebar.classList.remove('mobile-open');
  if (backdrop) backdrop.classList.remove('active');
  document.body.style.overflow = '';
}

// ── Confirmation Modal ───────────────────────────────────────
function showConfirmDialog(title, message, onConfirm, confirmText = 'Confirm', confirmStyle = 'danger') {
  let modal = document.getElementById('confirmModal');
  if (!modal) {
    modal = document.createElement('div');
    modal.id = 'confirmModal';
    modal.className = 'modal-backdrop';
    document.body.appendChild(modal);
  }

  modal.innerHTML = `
    <div class="modal-card">
      <div class="modal-header">
        <h3>${title}</h3>
        <button class="modal-close" onclick="closeConfirmModal()">&times;</button>
      </div>
      <div class="modal-body">
        <p>${message}</p>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" onclick="closeConfirmModal()">Cancel</button>
        <button class="btn btn-${confirmStyle}" id="modalConfirmBtn">${confirmText}</button>
      </div>
    </div>
  `;

  modal.classList.add('modal-visible');

  document.getElementById('modalConfirmBtn').onclick = () => {
    closeConfirmModal();
    if (typeof onConfirm === 'function') onConfirm();
  };
}

function closeConfirmModal() {
  const modal = document.getElementById('confirmModal');
  if (modal) modal.classList.remove('modal-visible');
}
