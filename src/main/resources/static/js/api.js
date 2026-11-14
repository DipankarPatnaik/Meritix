// ============================================================
//  api.js — Spring Boot REST Client
// ============================================================

const API_BASE = (window.location.origin && window.location.origin.startsWith('http'))
  ? window.location.origin + '/api'
  : 'http://localhost:8080/api';

// ── Session ──────────────────────────────────────────────────
const Session = {
  set(role, user) {
    sessionStorage.setItem('srm_session', JSON.stringify({ role, user, timestamp: Date.now() }));
  },
  get() {
    try {
      const s = sessionStorage.getItem('srm_session');
      return s ? JSON.parse(s) : null;
    } catch (e) {
      return null;
    }
  },
  clear() {
    sessionStorage.removeItem('srm_session');
  }
};

// ── Generic fetch helper ──────────────────────────────────────
async function apiFetch(path, options = {}) {
  try {
    const res = await fetch(API_BASE + path, {
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      ...options,
    });
    const json = await res.json();
    return json; // Always returns { success, message, data }
  } catch (err) {
    console.error('API Error:', err);
    return {
      success: false,
      message: 'Could not connect to backend server. Ensure Spring Boot is running on port 8080.',
      data: null
    };
  }
}

// ── Teacher API ───────────────────────────────────────────────
const TeacherAPI = {
  async signup(name, subject, loginId, password) {
    return apiFetch('/teachers/signup', {
      method: 'POST',
      body: JSON.stringify({ name, subject, loginId, password }),
    });
  },

  async login(loginId, password) {
    return apiFetch('/teachers/login', {
      method: 'POST',
      body: JSON.stringify({ loginId, password }),
    });
  },

  async getAll() {
    return apiFetch('/teachers');
  },

  async getById(id) {
    return apiFetch(`/teachers/${id}`);
  }
};

// ── Student API ───────────────────────────────────────────────
const StudentAPI = {
  async signup(name, rollNumber, loginId, password) {
    return apiFetch('/students/signup', {
      method: 'POST',
      body: JSON.stringify({ name, rollNumber, loginId, password }),
    });
  },

  async login(loginId, password) {
    return apiFetch('/students/login', {
      method: 'POST',
      body: JSON.stringify({ loginId, password }),
    });
  },

  async getAll() {
    return apiFetch('/students');
  },

  async getById(id) {
    return apiFetch(`/students/${id}`);
  },

  async getResults(studentId) {
    return apiFetch(`/students/${studentId}/results`);
  },
};

// ── Marks API ─────────────────────────────────────────────────
const MarksAPI = {
  // Teacher assigns or updates marks for a student
  async assign(teacherId, studentId, marks) {
    return apiFetch(`/marks/assign/${teacherId}`, {
      method: 'POST',
      body: JSON.stringify({ studentId, marks: Number(marks) }),
    });
  },

  // Get all marks a teacher has assigned
  async getByTeacher(teacherId) {
    return apiFetch(`/marks/teacher/${teacherId}`);
  },

  // Get the specific mark a teacher gave to a student
  async getOne(studentId, teacherId) {
    return apiFetch(`/marks/student/${studentId}/teacher/${teacherId}`);
  },

  // Delete/reset a mark
  async delete(teacherId, studentId) {
    return apiFetch(`/marks/teacher/${teacherId}/student/${studentId}`, {
      method: 'DELETE'
    });
  }
};
