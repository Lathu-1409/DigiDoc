let isLoggedIn = false; // Tracks login status

// Navigation function with access control
function navigateTo(pageId) {
  const restrictedPages = ['choose-action-page', 'upload-page', 'query-page'];
  if (restrictedPages.includes(pageId) && !isLoggedIn) {
    alert('Please log in to access this page.');
    return navigateTo('login-page');
  }

  const pages = document.querySelectorAll('.page');
  pages.forEach(page => page.classList.remove('active'));
  document.getElementById(pageId).classList.add('active');
}

// Register User
document.getElementById('register-form').addEventListener('submit', async (e) => {
  e.preventDefault();
  const username = document.getElementById('username').value;
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const response = await fetch('/users/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, email, password })
    });

    if (response.ok) {
      alert('Registration successful! You can now log in.');
      navigateTo('login-page');
    } else {
      const result = await response.text();
      alert(`Registration failed: ${result}`);
    }
  } catch (error) {
    alert('An error occurred during registration.');
    console.error(error);
  }
});

// Login User
document.getElementById('login-form').addEventListener('submit', async (e) => {
  e.preventDefault();
  const email = document.getElementById('login-email').value;
  const password = document.getElementById('login-password').value;

  try {
    const response = await fetch('/users/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (response.ok) {
      isLoggedIn = true; // Mark user as logged in
      alert('Login successful!');
      navigateTo('choose-action-page'); // Redirect to action page
    } else {
      const result = await response.text();
      alert(`Login failed: ${result}`);
    }
  } catch (error) {
    alert('An error occurred during login.');
    console.error(error);
  }
});

// Upload Document
document.getElementById('upload-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const formData = new FormData();
  formData.append('fileName', document.getElementById('fileName').value);
  formData.append('subjectName', document.getElementById('subjectName').value);
  formData.append('category', document.getElementById('category').value);
  formData.append('uploadedByUsername', document.getElementById('uploadedBy').value);
  formData.append('file', document.getElementById('file').files[0]);

  try {
    const response = await fetch('/documents/upload', {
      method: 'POST',
      body: formData
    });

    if (response.ok) {
      alert('File uploaded successfully!');
      navigateTo('query-page'); // Redirect to query page
    } else {
      const result = await response.text();
      alert(`File upload failed: ${result}`);
    }
  } catch (error) {
    alert('An error occurred during file upload.');
    console.error(error);
  }
});

// Query Documents
document.getElementById('query-form').addEventListener('submit', async (e) => {
  e.preventDefault();

  const query = new URLSearchParams({
    fileName: document.getElementById('queryFileName').value,
    subjectName: document.getElementById('querySubjectName').value,
    category: document.getElementById('queryCategory').value,
    uploadedByUsername: document.getElementById('queryUploadedByUsername').value,
  });

  try {
    const response = await fetch(`/documents/query?${query.toString()}`);
    const documents = await response.json();

    const tableBody = document.getElementById('document-table').querySelector('tbody');
    tableBody.innerHTML = '';

    if (documents.length === 0) {
      const row = document.createElement('tr');
      row.innerHTML = '<td colspan="5">No documents found.</td>';
      tableBody.appendChild(row);
      return;
    }

    documents.forEach(doc => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${doc.fileName || 'N/A'}</td>
        <td>${doc.subjectName || 'N/A'}</td>
        <td>${doc.category || 'N/A'}</td>
        <td>${doc.uploadedByUsername || 'N/A'}</td>
        <td><a href="/documents/download/${doc.id}" target="_blank">Download</a></td>
      `;
      tableBody.appendChild(row);
    });
  } catch (error) {
    alert('An error occurred while querying documents.');
    console.error(error);
  }
});

// Download Document
document.getElementById('download-form').addEventListener('submit', async (e) => {
  e.preventDefault();
  const documentId = document.getElementById('documentId').value;

  try {
    const response = await fetch(`/documents/download/${documentId}`);
    if (response.ok) {
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'document.pdf';
      a.click();
    } else {
      alert('Failed to download document.');
    }
  } catch (error) {
    alert('An error occurred during document download.');
    console.error(error);
  }
});