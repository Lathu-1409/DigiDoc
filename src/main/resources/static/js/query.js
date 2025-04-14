document.getElementById("queryForm").addEventListener("submit", async function (event) {
  event.preventDefault();

  const queryParams = new URLSearchParams({
      fileName: document.getElementById("fileName").value,
      subjectName: document.getElementById("subjectName").value,
      category: document.getElementById("category").value,
      uploadedByUsername: document.getElementById("uploadedByUsername").value
  });

  try {
      const response = await fetch(`/documents/query?${queryParams}`);
      if (response.ok) {
          const documents = await response.json();

          const tableBody = document.getElementById("resultsTableBody");
          tableBody.innerHTML = ""; // Clear previous results

          if (documents.length === 0) {
              const row = document.createElement("tr");
              row.innerHTML = `<td colspan="5">No documents found.</td>`;
              tableBody.appendChild(row);
              return;
          }

          documents.forEach((doc) => {
              const row = document.createElement("tr");
              row.innerHTML = `
                  <td>${doc.fileName || "N/A"}</td>
                  <td>${doc.subjectName || "N/A"}</td>
                  <td>${doc.category || "N/A"}</td>
                  <td>${doc.uploadedByUsername || "N/A"}</td>
                  <td><a href="/documents/download/${doc.id}" target="_blank">Download</a></td>
              `;
              tableBody.appendChild(row);
          });
      } else {
          const error = await response.text();
          alert("Error fetching documents: " + error);
      }
  } catch (err) {
      alert("Failed to fetch documents: " + err.message);
  }
});